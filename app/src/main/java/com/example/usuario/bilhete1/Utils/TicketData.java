package com.example.usuario.bilhete1.Utils;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class TicketData {
    // Campos do seu DB_TKT
    public String chave;           // 44 dígitos
    public Date   dathor;          // data/hora da venda
    public double valor;           // valor do ticket
    public String forpag;          // forma/meio de pagamento
    public String nomusr;          // usuário que efetuou a venda

    // Campos de layout/textos
    public String cabecalho1;    //Paerw1 no nome = "Terminal Rodoviário"
    public String cabecalho2;  //Parte2 do nome = "Alderico Tedoldi"
    public String titulo     = "TICKET DE ACESSO";
    public String servico;     //Tipo de Servivo
    public String sqrcode;      //QR-code do ticket
    public String ticketnumber; //Codigo do Ticket

    // Helpers de formatação
    public String valorBR() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        return nf.format(valor);
    }
    public String dataHoraExibicao() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(dathor);
    }
    public String dataHoraParaQR() {
        // Ajuste se quiser outro padrão no QR
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(dathor);
    }
    public String payloadQR() {
        // Requisito: concatenação de Chave + Dathor
        // Aqui uso Dathor formatado sem separadores para evitar espaços.
        return (sqrcode == null ? "" : sqrcode);
    }


}
