package com.example.usuario.bilhete1;


import android.content.Context;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;

import com.example.usuario.bilhete1.Utils.QrUtils;
import com.example.usuario.bilhete1.Utils.TicketData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class TicketPdfGenerator56mm {

    private TicketPdfGenerator56mm() {}

    private static final float MM_TO_PT = 72f / 25.4f;
    private static final float M = 6f; // no escopo da classe


    public static File generate(Context ctx, TicketData data, String fileName) throws Exception {
        // Largura para bobina 56mm
        final float pageWidthPt = Math.round(56f * MM_TO_PT); // ~160 pt
        final float margin = 6f;                              // um pouco maior p/ segurança
        final float contentWidth = pageWidthPt - margin*2;

        // Paints (tamanhos pensados pra térmica)
        Paint small  = basePaint(7.6f);
        Paint body   = basePaint(9.0f);
        Paint bold   = basePaint(10.0f); bold.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        Paint title  = basePaint(12.5f); title.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        Paint center = basePaint(9.2f);

        Paint faint  = basePaint(8.0f);  faint.setAlpha(180);
        Paint rule   = new Paint(Paint.ANTI_ALIAS_FLAG); rule.setColor(Color.BLACK); rule.setStrokeWidth(0.9f);

        // Linha pontilhada igual cupom
        Paint dotted = new Paint(rule);
        dotted.setPathEffect(new DashPathEffect(new float[]{4f, 4f}, 0));

        // Medir altura (simples: soma aproximada por linha)
        float totalH = margin;

        totalH += blockHeight(wrap(data.cabecalho1, body, contentWidth), body);
        totalH += blockHeight(wrap(data.cabecalho2, body, contentWidth), body);
        totalH += 6f;

        totalH += blockHeight(wrap(data.titulo, title, contentWidth), title);
        totalH += 8f;
        totalH += 2f;  // regra
        totalH += lineHeight(body) * 2; // "Serviço  Valor" + linha SANITÁRIOS/valor
        totalH += 2f;  // regra

        totalH += blockHeight(wrap("TOTAL " + data.valorBR(), bold, contentWidth), bold);
        totalH += 10f;

        totalH += blockHeight(wrap("Forma de Pagamento", center, contentWidth), center);
        totalH += blockHeight(wrap(data.forpag == null ? "" : data.forpag.toUpperCase(Locale.ROOT), bold, contentWidth), bold);
        totalH += 6f;
        totalH += 2f; // regra

        totalH += blockHeight(wrap("Usuário: " + safe(data.nomusr), body, contentWidth), body);
        totalH += blockHeight(wrap("Data e Hora ", bold, contentWidth), bold);
        totalH += 6f;
        totalH += blockHeight(wrap(data.dataHoraExibicao(), bold, contentWidth), bold);
        totalH += 10f;

        // QR
        float qrW = contentWidth * 0.70f;
        totalH += qrW + 8f;

        totalH += blockHeight(wrap(maskChave(data.sqrcode), small, contentWidth), small); // mostrar chave embaixo
        totalH += 6f;

        totalH += margin;

        int pageHeight = (int)Math.ceil(totalH);

        // ===== Desenho =====
        PdfDocument pdf = new PdfDocument();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder((int)pageWidthPt, pageHeight, 1).create();
        PdfDocument.Page page = pdf.startPage(info);
        Canvas c = page.getCanvas();

        float y = margin;

        // Cabeçalho (centralizado)
        y = drawCentered(c, data.cabecalho1, body, margin, contentWidth, y);
        y = drawCentered(c, data.cabecalho2, body, margin, contentWidth, y);
        y += 6f;

        // Título
        y = drawCentered(c, data.titulo, title, margin, contentWidth, y);
        y += 8f;
        //Ticket Numero
        //y = drawCentered(c, ("Nº "+data.ticketnumber), title, margin, contentWidth, y);
       // y += 6f;
        // Linha pontilhada
        drawRule(c, dotted, margin, pageWidthPt - margin, y); y += 6f;

        // "Serviço" e "Valor" (cabeçalho colunas)
        float colLeftX  = margin;
        float colRightX = margin + contentWidth; // ancoragem à direita

        y += body.getTextSize();
        c.drawText("Serviço", colLeftX, y, body);
        drawRight(c, "Valor", body, colRightX, y);

        // Linha com SANITÁRIOS e valor (estilo do print)
        y += body.getTextSize() + 2f;
        c.drawText(data.servico, colLeftX, y, small);
        drawRight(c, data.valorBR(), body, colRightX, y);

        // Linha pontilhada
        y += 6f;
        drawRule(c, dotted, margin, pageWidthPt - margin, y); y += 8f;

        // TOTAL (centralizado)
        y = drawCentered(c, "TOTAL " + data.valorBR(), bold, margin, contentWidth, y);
        y += 8f;

        // Forma de pagamento
        y = drawCentered(c, "Forma de Pagamento", center, margin, contentWidth, y);
        y = drawCentered(c, (data.forpag == null ? "" : data.forpag.toUpperCase(Locale.ROOT)), bold, margin, contentWidth, y);

        // Linha pontilhada
        y += 6f;
        drawRule(c, dotted, margin, pageWidthPt - margin, y); y += 8f;

        // Usuário + Data/Hora (alinhado à esquerda como no print; “Data e Hora …” em negrito)
        y += body.getTextSize();
        c.drawText("Usuário: " + safe(data.nomusr), margin, y, body);
        y += 6f + bold.getTextSize();
        c.drawText("Data e Hora ", margin, y, bold);
        y += 6f + bold.getTextSize();
        c.drawText(data.dataHoraExibicao(), margin, y, bold);
        y += 10f;

        // QR (payload = Chave + Dathor)
        Bitmap qr = QrUtils.makeQr(data.payloadQR(), 600);
        float qrLeft = margin + (contentWidth - qrW)/2f;
        RectF rect = new RectF(qrLeft, y, qrLeft + qrW, y + qrW);
        c.drawBitmap(qr, null, rect, null);
        y += qrW + 8f;

        // Chave (44 dígitos em blocos pra leitura)
        List<String> chaveLines = wrap(maskChave(data.chave), small, contentWidth);
        for (String ln : chaveLines) {
            y += small.getTextSize();
            c.drawText(ln, margin, y, small);
            y += 2f;
        }
        y += 2f;

        pdf.finishPage(page);

        File dir = ctx.getExternalFilesDir(null);        if (dir == null) dir = ctx.getFilesDir();
        File out = new File(dir, fileName == null ? "ticket_liberacao_56mm.pdf" : fileName);

        try (FileOutputStream fos = new FileOutputStream(out)) {
            pdf.writeTo(fos);
        }
        pdf.close();

        return out;
    }




    // ===== Helpers =====
    private static Paint basePaint(float size) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        p.setTextSize(size);
        p.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        return p;
    }
    private static String safe(String s) { return s == null ? "" : s; }

    private static float lineHeight(Paint p){ return p.getTextSize() + 2f; }

    private static void drawRule(Canvas c, Paint rule, float x1, float x2, float y) {
        c.drawLine(x1, y, x2, y, rule);
    }

    private static float drawCentered(Canvas c, String txt, Paint p, float margin, float contentWidth, float y) {
        List<String> lines = wrap(safe(txt), p, contentWidth);
        for (String ln : lines) {
            float w = p.measureText(ln);
            float x = margin + (contentWidth - w)/2f;
            y += p.getTextSize();
            c.drawText(ln, x, y, p);
            y += 2f;
        }
        return y;
    }


    private static List<String> wrap(String s, Paint p, float maxW) {
        List<String> out = new ArrayList<>();
        if (s == null || s.trim().isEmpty()) { out.add(""); return out; }
        String[] words = s.trim().split("\\s+");
        String line = "";
        for (String w : words) {
            String test = line.isEmpty() ? w : (line + " " + w);
            if (p.measureText(test) <= maxW) line = test;
            else { if (!line.isEmpty()) out.add(line); line = w; }
        }
        if (!line.isEmpty()) out.add(line);
        return out;
    }

    private static float blockHeight(List<String> lines, Paint p) {
        return lines.size() * (p.getTextSize() + 2f);
    }

    private static String maskChave(String chave) {
        if (chave == null) return "";
        String digits = chave.replaceAll("\\D+", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            sb.append(digits.charAt(i));
            if ((i+1) % 4 == 0 && (i+1) < digits.length()) sb.append(' ');
        }
        return sb.toString();
    }

    // ===== MODELOS DO RELATÓRIO =====
    public static final class CloseReport {
        public String empresa1, empresa2;
        public List<PayLine> resumoPagtoGeral = new ArrayList<>();
        public double totalGeral;
        public List<DayBlock> dias = new ArrayList<DayBlock>();
    }
    public static final class DayBlock {
        public String diaBR;                 // ex: "23/10/2025"
        public List<UserLine> usuarios = new ArrayList<UserLine>();
        public double totalDia;
    }
    public static final class UserLine {
        public String usuario;
        public int qtd;
        public double total;
    }

    public static final class PayLine {
        public String forma; // DINHEIRO, PIX, CRÉDITO...
        public int qtd;
        public double total;
    }

    // ===== OVERLOAD: GERAR RELATÓRIO 56mm =====
    public static File generate2(Context ctx, CloseReport r, String fileName) throws Exception {
            // --- Constantes de layout ---
        final float MM_TO_PT = 72f / 25.4f;
        final int   PAGE_W   = Math.round(56f * MM_TO_PT);   // ~160 pt
        final float MARGIN   = 6f;
        final float CONTENT_W= PAGE_W - 2*MARGIN;

        // fontes
        Paint pBody  = mkPaint(9.5f, false);
        Paint pSmall = mkPaint(8.0f, false);
        Paint pBold  = mkPaint(10.5f, true);
        Paint pTitle = mkPaint(12.0f, true);
        Paint pDot   = mkDotted();

        final float gap   = 2f;
        final float rowH  = pBody.getTextSize() + gap;

        // ===== 1) MEDIÇÃO (altura exata) =====
        float h = MARGIN;

        // Cabeçalho
        h += blockHeightCentered(nonNull(r.empresa1), pBody, CONTENT_W);
        h += blockHeightCentered(nonNull(r.empresa2), pBody, CONTENT_W);
        h += 2f;
        h += blockHeightCentered("FECHAMENTO DE TURNO", pTitle, CONTENT_W);
        h += 4f; h += 1f; h += 4f; // linha pontilhada + espaçamentos

        final float colUserMaxW = CONTENT_W * 0.62f - 6f;

        for (DayBlock d : r.dias) {
            h += blockHeightCentered("Data " + nonNull(d.diaBR), pBold, CONTENT_W);
            h += 4f;

            // cabeçalho da tabela
            h += pSmall.getTextSize(); h += 2f; h += 1f; h += 2f;

            if (d.usuarios == null || d.usuarios.isEmpty()) {
                h += rowH + 2f;
            } else {
                for (UserLine u : d.usuarios) {
                    List<String> lines = wrap(nonNull(u.usuario), pBody, colUserMaxW);
                    h += rowH;                          // 1ª linha (com qtd/total)
                    h += (lines.size()-1) * rowH;       // linhas extras do nome
                    h += 2f;
                }
            }

            h += 1f; h += 6f;                           // linha + espaço
            h += pBold.getTextSize() + 2f;              // Total da Data
            h += 8f;
        }

        // Total Geral
        h += 1f; h += 6f;
        h += blockHeightCentered("Total Geral  " + moeda(r.totalGeral), pBold, CONTENT_W);
        h += 6f;

        // Resumo por Forma de Pagamento (final)
        if (r.resumoPagtoGeral != null && !r.resumoPagtoGeral.isEmpty()) {
            h += 1f; h += 6f;
            h += blockHeightCentered("Por Forma de Pagamento", pBold, CONTENT_W);
            h += 2f;
            h += pSmall.getTextSize(); h += 2f; h += 1f; h += 2f; // cabeçalho + linha
            for (PayLine pl : r.resumoPagtoGeral) {
                List<String> lines = wrap(nonNull(pl.forma), pBody, colUserMaxW);
                h += rowH;                        // 1ª + qtd/total
                h += (lines.size()-1) * rowH;     // extras
                h += 2f;
            }
            h += 4f; h += 1f; h += 6f;
            h += blockHeightCentered("Total (pagamentos)  " + moeda(
                    r.resumoPagtoGeral.stream().mapToDouble(x -> x.total).sum()
            ), pBold, CONTENT_W);
            h += 4f;
        }

        h += MARGIN;
        int PAGE_H = Math.round(h);

        // ===== 2) DESENHO =====
        PdfDocument pdf = new PdfDocument();
        PdfDocument.Page page = pdf.startPage(new PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, 1).create());
        Canvas c = page.getCanvas();
        float y = MARGIN;

        // Cabeçalho
        y = drawCentered(c, nonNull(r.empresa1), pBody, MARGIN, CONTENT_W, y);
        y = drawCentered(c, nonNull(r.empresa2), pBody, MARGIN, CONTENT_W, y);
        y += 2f;
        y = drawCentered(c, "FECHAMENTO DE TURNO", pTitle, MARGIN, CONTENT_W, y);
        y += 4f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 4f;

        final float colUserX = MARGIN;
        final float colQtdX  = MARGIN + CONTENT_W * 0.62f;
        final float colTotX  = MARGIN + CONTENT_W;

        for (DayBlock d : r.dias) {
            y = drawCentered(c, "Data " + nonNull(d.diaBR), pBold, MARGIN, CONTENT_W, y);
            y += 4f;

            // cabeçalho tabela
            c.drawText("Usuário", colUserX, (y += pSmall.getTextSize()), pSmall);
            drawRight(c, "Qtd",   pSmall, colQtdX, y);
            drawRight(c, "Total", pSmall, colTotX, y);
            y += 2f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 2f;

            if (d.usuarios == null || d.usuarios.isEmpty()) {
                c.drawText("-", colUserX, (y += rowH), pBody);
                drawRight(c, "0",        pBody, colQtdX, y);
                drawRight(c, moeda(0.0), pBody, colTotX, y);
                y += 2f;
            } else {
                for (UserLine u : d.usuarios) {
                    List<String> lines = wrap(nonNull(u.usuario), pBody, colUserMaxW);
                    // 1ª linha
                    c.drawText(lines.get(0), colUserX, (y += rowH), pBody);
                    drawRight(c, String.valueOf(u.qtd), pBody, colQtdX, y);
                    drawRight(c, moeda(u.total),        pBody, colTotX, y);
                    // extras
                    for (int i = 1; i < lines.size(); i++) {
                        c.drawText(lines.get(i), colUserX, (y += rowH), pBody);
                    }
                    y += 2f;
                }
            }

            c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 6f;
            String totalDia = "Total da Data  " + moeda(d.totalDia);
            y += pBold.getTextSize();
            drawRight(c, totalDia, pBold, MARGIN + CONTENT_W, y);
            y += 10f;
        }

        // Total Geral
        c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 6f;
        y = drawCentered(c, "Total Geral  " + moeda(r.totalGeral), pBold, MARGIN, CONTENT_W, y);
        y += 6f;

        // Resumo por Forma de Pagamento
        if (r.resumoPagtoGeral != null && !r.resumoPagtoGeral.isEmpty()) {
            c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 6f;
            y = drawCentered(c, "Por Forma de Pagamento", pBold, MARGIN, CONTENT_W, y);
            y += 2f;
            c.drawText("Forma", MARGIN, (y += pSmall.getTextSize()), pSmall);
            drawRight(c, "Qtd",   pSmall, colQtdX, y);
            drawRight(c, "Total", pSmall, colTotX, y);
            y += 2f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 2f;

            for (PayLine pl : r.resumoPagtoGeral) {
                List<String> lines = wrap(nonNull(pl.forma), pBody, colUserMaxW);
                c.drawText(lines.get(0), MARGIN, (y += rowH), pBody);
                drawRight(c, String.valueOf(pl.qtd), pBody, colQtdX, y);
                drawRight(c, moeda(pl.total),        pBody, colTotX, y);
                for (int i = 1; i < lines.size(); i++) {
                    c.drawText(lines.get(i), MARGIN, (y += rowH), pBody);
                }
                y += 2f;
            }

            double somaResumo = 0;
            for (PayLine pl : r.resumoPagtoGeral) somaResumo += pl.total;

            y += 4f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 6f;
            y = drawCentered(c, "Total (pagamentos)  " + moeda(somaResumo), pBold, MARGIN, CONTENT_W, y);
            y += 4f;
        }

        pdf.finishPage(page);

        File dir = ctx.getExternalFilesDir(null); if (dir==null) dir = ctx.getFilesDir();
        File out = new File(dir, fileName==null? "fechamento_56mm.pdf" : fileName);
        try (FileOutputStream fos = new FileOutputStream(out)) { pdf.writeTo(fos); }
        pdf.close();
        return out;
    }



    // ===== RELATÓRIO DETALHADO =====
    public static final class DetailedReport {
        public String empresa1, empresa2;
        public List<TicketLine> itens = new ArrayList<>();
        public double totalGeral;
    }

    public static final class TicketLine {
        public String ticket;      // Codigo (ou ID)
        public String tipo;        // Tipser (ex.: SANITÁRIOS)
        public String dataHoraBR;  // "dd/MM/yyyy HH:mm:ss"
        public double valor;       // Vlrtkt
        public String forpag;       // << NOVO (ex.: DINHEIRO/PIX/CRÉDITO)
    }


    // ===== helpers já usados acima =====
        private static Paint mkPaint(float sz, boolean bold){
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(Color.BLACK); p.setTextSize(sz);
            p.setTypeface(Typeface.create(Typeface.MONOSPACE, bold?Typeface.BOLD:Typeface.NORMAL));
            return p;
        }
        private static Paint mkDotted(){
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(Color.BLACK); p.setStrokeWidth(0.9f);
            p.setPathEffect(new DashPathEffect(new float[]{4f,4f}, 0));
            return p;
        }
        private static String nonNull(String s){ return s==null? "": s; }
        private static String moeda(double v){
            return java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt","BR")).format(v);
        }
        private static float blockHeightCentered(String txt, Paint p, float width){
            float h = 0f; for (String ln : wrap(txt, p, width)) { h += p.getTextSize() + 2f; } return h;
        }

        private static void drawRight(Canvas c, String txt, Paint p, float rightX, float y){
            c.drawText(txt, rightX - p.measureText(txt), y, p);
        }

    private static String safeDia(String diaBR, String diaISO) {
        if (diaBR != null && !diaBR.isEmpty()) return diaBR;
        if (diaISO != null && !diaISO.isEmpty()) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(diaISO);
                return d.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception ignored) {}
            return diaISO;
        }
        return "";
    }

    public static File generate3(Context ctx, DetailedReport r, String fileName) throws Exception {
        final float MM_TO_PT = 72f / 25.4f;
        final int   PAGE_W   = Math.round(56f * MM_TO_PT);   // ~160pt
        final float MARGIN   = 6f;
        final float CONTENT_W= PAGE_W - 2*MARGIN;

        // fontes
        Paint pHdr   = mkPaint(8.5f, true);
        Paint pBody  = mkPaint(9.0f, false);
        Paint pBold  = mkPaint(10.5f, true);
        Paint pTitle = mkPaint(12.0f, true);
        Paint pDot   = mkDotted();

        final float gap   = 2f;
        final float rowH  = pBody.getTextSize() + gap;

        // colunas (duas colunas l1; na l2, valor à direita)
        final float col1X    = MARGIN;                 // Código / DataHora
        final float col2X    = MARGIN + CONTENT_W*0.52f; // Tipo na mesma linha do Código
        final float colValRX = MARGIN + CONTENT_W;     // Valor alinhado à direita na 2ª linha

        final float maxCodW  = CONTENT_W*0.48f - 4f;   // larguras máximas para quebra
        final float maxTipoW = CONTENT_W*0.46f - 4f;
        final float maxDataW = CONTENT_W*0.70f - 4f;   // sobra espaço pro valor à direita

        // ===== 1) MEDIR ALTURA =====
        float h = MARGIN;

        h += blockHeightCentered(nonNull(r.empresa1), pBody, CONTENT_W);
        h += blockHeightCentered(nonNull(r.empresa2), pBody, CONTENT_W);
        h += 2f;
        h += blockHeightCentered("RELATÓRIO DETALHADO", pTitle, CONTENT_W);
        h += 4f; h += 1f; h += 4f;

        // cabeçalho “duas linhas”
        h += pHdr.getTextSize();       // Linha 1 títulos
        h += pHdr.getTextSize();       // Linha 2 títulos
        h += 2f; h += 1f; h += 2f;

        if (r.itens == null || r.itens.isEmpty()) {
            h += rowH*2 + 4f;
        } else {
            for (TicketLine t : r.itens) {
                // antes: l1 (código/tipo) + l2 (data/valor)
                int l1 = Math.max(
                        wrap(nonNull(t.ticket), pBody, maxCodW).size(),
                        wrap(nonNull(t.tipo),   pBody, maxTipoW).size()
                );
                int l2 = Math.max(
                        wrap(nonNull(t.dataHoraBR), pBody, maxDataW).size(),
                        1 // valor = 1 linha
                );
               // NOVO: linha 3 - forma de pagamento (texto "Pgto: <forma>")
                int l3 = wrap("Pgto: " + nonNull(t.forpag), pBody, CONTENT_W - 4f).size();

                h += rowH * l1;     // linha 1
                h += rowH * l2;     // linha 2
                h += rowH * l3;     // linha 3 (pagamento)  <<<<<
                h += 2f;            // respiro entre itens
            }
        }

        h += 4f; h += 1f; h += 6f;
        h += blockHeightCentered("Total Geral  " + moeda(r.totalGeral), pBold, CONTENT_W);
        h += 6f;

        h += MARGIN;
        int PAGE_H = Math.round(h);

        // ===== 2) DESENHAR =====
        PdfDocument pdf = new PdfDocument();
        PdfDocument.Page page = pdf.startPage(new PdfDocument.PageInfo.Builder(PAGE_W, PAGE_H, 1).create());
        Canvas c = page.getCanvas();
        float y = MARGIN;

        y = drawCentered(c, nonNull(r.empresa1), pBody, MARGIN, CONTENT_W, y);
        y = drawCentered(c, nonNull(r.empresa2), pBody, MARGIN, CONTENT_W, y);
        y += 2f;
        y = drawCentered(c, "RELATÓRIO DETALHADO", pTitle, MARGIN, CONTENT_W, y);
        y += 4f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 4f;

        // cabeçalho (duas linhas)
        c.drawText("Código", col1X, (y += pHdr.getTextSize()), pHdr);
        c.drawText("Tipo",   col2X, y, pHdr);
        c.drawText("Data/Hora", col1X, (y += pHdr.getTextSize()), pHdr);
        drawRight(c, "Valor", pHdr, colValRX, y);

        y += 2f; c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 2f;

        if (r.itens == null || r.itens.isEmpty()) {
            // duas linhas vazias
            c.drawText("-", col1X, (y += rowH), pBody);
            c.drawText("-", col2X, y, pBody);
            c.drawText("-", col1X, (y += rowH), pBody);
            drawRight(c, moeda(0), pBody, colValRX, y);
            y += 4f;
        } else {
            for (TicketLine t : r.itens) {
                List<String> cod = wrap(nonNull(t.ticket),     pBody, maxCodW);
                List<String> tip = wrap(nonNull(t.tipo),       pBody, maxTipoW);
                List<String> dat = wrap(nonNull(t.dataHoraBR), pBody, maxDataW);

                int l1 = Math.max(cod.size(), tip.size());
                int l2 = Math.max(dat.size(), 1);

                // Linha 1: Código (col1) + Tipo (col2)
                for (int i = 0; i < l1; i++) {
                    String sCod = i < cod.size() ? cod.get(i) : "";
                    String sTip = i < tip.size() ? tip.get(i) : "";
                    c.drawText(sCod, col1X, (y += rowH), pBody);
                    c.drawText(sTip, col2X, y, pBody);
                }

                // Linha 2: Data/Hora (col1) + Valor (direita)
                for (int i = 0; i < l2; i++) {
                    String sDat = i < dat.size() ? dat.get(i) : "";
                    c.drawText(sDat, col1X, (y += rowH), pBody);
                    if (i == 0) drawRight(c, moeda(t.valor), pBody, colValRX, y);
                }

                // Linha 3: Forma de Pagamento (texto corrido à esquerda)
                List<String> pg = wrap("Pgto: " + nonNull(t.forpag), pBody, CONTENT_W - 4f);
                for (int i = 0; i < pg.size(); i++) {
                    c.drawText(pg.get(i), col1X, (y += rowH), pBody);
                }

                y += 2f;
            }
        }

        c.drawLine(MARGIN, y, PAGE_W - MARGIN, y, pDot); y += 6f;
        y = drawCentered(c, "Total Geral  " + moeda(r.totalGeral), pBold, MARGIN, CONTENT_W, y);
        y += 6f;

        pdf.finishPage(page);

        File dir = ctx.getExternalFilesDir(null); if (dir==null) dir = ctx.getFilesDir();
        File out = new File(dir, fileName==null? "fechamento_detalhado_56mm.pdf" : fileName);
        try (FileOutputStream fos = new FileOutputStream(out)) { pdf.writeTo(fos); }
        pdf.close();
        return out;
    }




}
