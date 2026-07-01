package com.example.usuario.bilhete1.Utils;

import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.round

/**
 * Parser DOM simples, ignorando namespaces (usa busca por localName).
 * Serve como protótipo; em produção, considere XmlPullParser para performance.
 */
object BPeXmlParser {

    fun parse(input: InputStream): PrintData {
        val dbf = DocumentBuilderFactory.newInstance()
        dbf.isNamespaceAware = true
        val doc = dbf.newDocumentBuilder().parse(input)

        val ide = Ide(
            cUF = text(doc, "cUF"),
            tpAmb = text(doc, "tpAmb"),
            tpEmis = text(doc, "tpEmis"),
            serie = text(doc, "serie"),
            nBP = text(doc, "nBP"),
            dhEmi = text(doc, "dhEmi"),
            dhCont = text(doc, "dhCont"),
            xJust = text(doc, "xJust"),
            tpBPe = text(doc, "tpBPe")
        )

        val emit = Emitente(
            cnpj = text(doc, "CNPJ"),
            xNome = firstNonNull(doc, arrayOf("xNome", "xNomeEmit")),
            ie = text(doc, "IE"),
            xFant = text(doc, "xFant"),
            endereco = Endereco(
                xLgr = text(doc, "xLgr"),
                nro = text(doc, "nro"),
                xBairro = text(doc, "xBairro"),
                xMun = text(doc, "xMun"),
                cMun = text(doc, "cMun"),
                uf = text(doc, "UF"),
                cep = text(doc, "CEP"),
                fone = text(doc, "fone")
            )
        )

        val viagem = Viagem(
            xPercurso = text(doc, "xPercurso"),
            tpServ = text(doc, "tpServ"),
            tpViagem = text(doc, "tpViagem"),
            tpAcomodacao = text(doc, "tpAcomodacao"),
            poltrona = text(doc, "poltrona"),
            plataforma = text(doc, "plataforma"),
            prefixo = text(doc, "prefixo"),
            dhViagem = firstNonNull(doc, arrayOf("dhViagem", "dhSaida"))
        )

        val pass = run {
            val nome = text(doc, "xNome", parentLocal = "infPassageiro")
            val cpf = text(doc, "CPF")
            if (nome == null && cpf == null) null else Passageiro(
                xNome = nome,
                cpf = cpf,
                tpDoc = text(doc, "tpDoc"),
                nDoc = text(doc, "nDoc")
            )
        }

        val comps = nodes(doc, "Comp").mapNotNull { c ->
            val tp = c.childText("tpComp") ?: return@mapNotNull null
            val v = (c.childText("vComp") ?: "0").toDoubleOrNull() ?: 0.0
            Componente(tpComp = tp, vComp = v, xComp = c.childText("xComp"))
        }

        val valores = Valores(
            vBP = text(doc, "vBP")?.toDoubleOrNull(),
            vDesc = firstNonNull(doc, arrayOf("vDesc","vDesconto"))?.toDoubleOrNull(),
            vPgto = text(doc, "vPgto")?.toDoubleOrNull(),
            vTroco = text(doc, "vTroco")?.toDoubleOrNull(),
            componentes = comps
        )

        val pag = Pagamento(
            tPag = text(doc, "tPag"),
            xPag = text(doc, "xPag"),
            vPag = text(doc, "vPag")?.toDoubleOrNull()
        )

        val prot = Protocolo(
            nProt = text(doc, "nProt"),
            dhRecbto = text(doc, "dhRecbto"),
            cStat = text(doc, "cStat"),
            xMotivo = text(doc, "xMotivo")
        ).let { if (it.nProt == null && it.dhRecbto == null) null else it }

        val chave = text(doc, "chBPe")
        val qr = text(doc, "qrCodBPe")

        return PrintData(
            ide = ide,
            emitente = emit,
            viagem = viagem,
            passageiro = pass,
            valores = valores,
            pagamento = pag,
            protocolo = prot,
            chave = chave,
            qrCode = qr,
            infoTributos = null
        )
    }

    // --- helpers ---
    private fun Document.findByLocal(local: String, parentLocal: String? = null): Node? {
        val list = if (parentLocal == null) this.getElementsByTagNameNS("*", local)
                   else this.getElementsByTagNameNS("*", parentLocal)
        for (i in 0 until list.length) {
            val n = list.item(i)
            if (parentLocal == null) return n
            // search child with localName
            val cList = n.childNodes
            for (j in 0 until cList.length) {
                val c = cList.item(j)
                if (c.localName == local) return c
            }
        }
        return null
    }

    private fun text(doc: Document, local: String, parentLocal: String? = null): String? {
        val node = doc.findByLocal(local, parentLocal)
        return node?.textContent?.trim()?.ifEmpty { null }
    }

    private fun firstNonNull(doc: Document, candidates: Array<String>): String? {
        for (c in candidates) {
            val v = text(doc, c)
            if (v != null) return v
        }
        return null
    }

    private fun nodes(doc: Document, local: String): List<Node> {
        val res = mutableListOf<Node>()
        val nlist = doc.getElementsByTagNameNS("*", local)
        for (i in 0 until nlist.length) res += nlist.item(i)
        return res
    }

    private fun Node.childText(local: String): String? {
        val list = this.childNodes
        for (i in 0 until list.length) {
            val c = list.item(i)
            if (c.localName == local) return c.textContent?.trim()?.ifEmpty { null }
        }
        return null
    }
}
