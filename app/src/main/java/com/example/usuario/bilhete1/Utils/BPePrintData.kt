package com.example.usuario.bilhete1.Utils;

data class Emitente(
    val cnpj: String? = null,
    val xNome: String? = null,
    val ie: String? = null,
    val xFant: String? = null,
    val endereco: Endereco? = null
)

data class Endereco(
    val xLgr: String? = null,
    val nro: String? = null,
    val xBairro: String? = null,
    val xMun: String? = null,
    val cMun: String? = null,
    val uf: String? = null,
    val cep: String? = null,
    val fone: String? = null
)

data class Ide(
    val cUF: String? = null,
    val tpAmb: String? = null,
    val tpEmis: String? = null,   // 1=Normal, 2=Contingência
    val serie: String? = null,
    val nBP: String? = null,
    val dhEmi: String? = null,
    val dhCont: String? = null,
    val xJust: String? = null,
    val tpBPe: String? = null
)

data class Viagem(
    val xPercurso: String? = null,
    val tpServ: String? = null,
    val tpViagem: String? = null,
    val tpAcomodacao: String? = null,
    val poltrona: String? = null,
    val plataforma: String? = null,
    val prefixo: String? = null,
    val dhViagem: String? = null
)

data class Passageiro(
    val xNome: String? = null,
    val cpf: String? = null,
    val tpDoc: String? = null,
    val nDoc: String? = null
)

data class Componente(
    val tpComp: String,   // 01=Tarifa, 04=Seguro, 99=Outras
    val vComp: Double,
    val xComp: String? = null
)

data class Valores(
    val vBP: Double? = null,
    val vDesc: Double? = null,
    val vPgto: Double? = null,
    val vTroco: Double? = null,
    val componentes: List<Componente> = emptyList()
) {
    val tarifa: Double? get() = componentes.firstOrNull { it.tpComp == "01" }?.vComp
    val seguro: Double? get() = componentes.firstOrNull { it.tpComp == "04" }?.vComp
    val outras: Double? get() = componentes.firstOrNull { it.tpComp == "99" }?.vComp
}

data class Pagamento(
    val tPag: String? = null,
    val xPag: String? = null,
    val vPag: Double? = null
)

data class Protocolo(
    val nProt: String? = null,
    val dhRecbto: String? = null,
    val cStat: String? = null,
    val xMotivo: String? = null
)

data class PrintData(
    val ide: Ide = Ide(),
    val emitente: Emitente = Emitente(),
    val viagem: Viagem = Viagem(),
    val passageiro: Passageiro? = null,
    val valores: Valores = Valores(),
    val pagamento: Pagamento = Pagamento(),
    val protocolo: Protocolo? = null,
    val chave: String? = null,
    val qrCode: String? = null,
    val infoTributos: String? = null
) {
    // --- flags para o layout condicional ---
    val isContingencia: Boolean get() = ide.tpEmis == "2"
    val hasProtocolo: Boolean get() = !isContingencia && protocolo?.nProt != null && protocolo.dhRecbto != null
    val hasPassageiro: Boolean get() = passageiro?.xNome != null || passageiro?.cpf != null
    val isSemPagamento: Boolean get() = pagamento.xPag == "SEM PAGAMENTO" || ((pagamento.vPag ?: 0.0) == 0.0)
    val isGratuidade: Boolean get() = (valores.vDesc ?: 0.0) >= (valores.vBP ?: Double.MAX_VALUE)
}
