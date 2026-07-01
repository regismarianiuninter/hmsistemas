package com.example.usuario.bilhete1.Utils;

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BPeDoc(data: PrintData) {
    Column(Modifier.fillMaxWidth().padding(24.dp)) {
        // Header emitente
        BasicText(data.emitente.xNome.orEmpty(), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        BasicText("CNPJ: " + (data.emitente.cnpj ?: "--"))
        BasicText(listOfNotNull(
            data.emitente.endereco?.xLgr?.plus(", " + (data.emitente.endereco.nro ?: "")),
            data.emitente.endereco?.xBairro,
            data.emitente.endereco?.xMun?.plus(" - " + (data.emitente.endereco.uf ?: "")),
            data.emitente.endereco?.cep
        ).joinToString(" • "))

        Spacer(Modifier.height(12.dp))
        BasicText("DOCUMENTO AUXILIAR DO BILHETE DE PASSAGEM ELETRÔNICO", fontSize = 16.sp, fontWeight = FontWeight.Bold)

        if (data.isContingencia) {
            Spacer(Modifier.height(8.dp))
            BasicText("EMITIDO EM CONTINGÊNCIA / Pendente de Autorização", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            BasicText("Just.: " + (data.ide.xJust ?: ""))
        }

        Spacer(Modifier.height(12.dp))
        // Viagem / Trecho
        BasicText("Trecho: " + (data.viagem.xPercurso ?: "--"), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicText("Serviço: " + (data.viagem.tpServ ?: "--"))
            BasicText("Data/Hora: " + (data.viagem.dhViagem ?: "--"))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicText("Poltrona: " + (data.viagem.poltrona ?: "--"))
            BasicText("Plataforma: " + (data.viagem.plataforma ?: "--"))
            BasicText("Prefixo: " + (data.viagem.prefixo ?: "--"))
        }

        Spacer(Modifier.height(8.dp))
        // Passageiro
        if (data.hasPassageiro) {
            BasicText("Passageiro: " + (data.passageiro?.xNome ?: "--"))
            data.passageiro?.cpf?.let { BasicText("CPF: $it") }
        } else {
            BasicText("PASSAGEIRO NÃO IDENTIFICADO")
        }

        Spacer(Modifier.height(8.dp))
        // Valores
        BasicText("Valores", fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicText("Tarifa: " + (data.valores.tarifa?.formatMoney() ?: "--"))
            BasicText("Seguro: " + (data.valores.seguro?.formatMoney() ?: "--"))
            BasicText("Outras: " + (data.valores.outras?.formatMoney() ?: "--"))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            BasicText("Desconto: " + (data.valores.vDesc?.formatMoney() ?: "--"))
            BasicText("Total: " + (data.valores.vBP?.formatMoney() ?: "--"))
            BasicText("A pagar: " + (data.valores.vPgto?.formatMoney() ?: "--"))
        }

        Spacer(Modifier.height(8.dp))
        // Pagamento
        if (data.isSemPagamento) {
            BasicText("Forma de pagamento: SEM PAGAMENTO")
        } else {
            BasicText("Forma de pagamento: " + (data.pagamento.xPag ?: data.pagamento.tPag ?: "--"))
            BasicText("Valor pago: " + (data.pagamento.vPag?.formatMoney() ?: "--"))
        }

        if (data.hasProtocolo) {
            Spacer(Modifier.height(8.dp))
            BasicText("Protocolo: " + (data.protocolo?.nProt ?: "--"))
            BasicText("Autorizado em: " + (data.protocolo?.dhRecbto ?: "--"))
        }

        Spacer(Modifier.height(12.dp))
        // Chave e QR
        BasicText("Chave de Acesso: " + (data.chave ?: "--"), fontSize = 12.sp)
        BasicText("QR Code: " + (data.qrCode ?: "--"), fontSize = 12.sp)

        Spacer(Modifier.height(12.dp))
        BasicText("Documento auxiliar, sem valor fiscal.", fontSize = 10.sp)
    }
}

private fun Double.formatMoney(): String = "R$ " + String.format(java.util.Locale("pt", "BR"), "%,.2f", this)
