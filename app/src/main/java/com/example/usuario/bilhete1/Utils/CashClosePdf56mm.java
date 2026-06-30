package com.example.usuario.bilhete1.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class CashClosePdf56mm {

    private static final float MM_TO_PT = 72f / 25.4f;

    // TicketPdfGenerator56mm.java  (adicione no topo do arquivo)
    public static final class CloseReport {
        public String empresaLinha1;
        public String empresaLinha2;
        public boolean detalhado; // se true, imprime lista de itens por dia
        public List<DayBlock> dias = new ArrayList<>();
        public double totalGeral;
    }

    public static final class DayBlock {
        public String diaBR; // "23/10/2025"
        public List<UserLine> usuarios = new ArrayList<>();
        public double totalDia;
        // opcional (só se detalhado=true):
        public List<DetailItem> detalhes = new ArrayList<>();
    }

    public static final class UserLine {
        public String usuario; // "Reginaldo"
        public int qtd;        // quantidade de tickets
        public double total;   // soma R$
    }

    public static final class DetailItem {
        public String dathor;   // "yyyy-MM-dd HH:mm:ss"
        public String forpag;   // forma de pagamento
        public String tipser;   // serviço
        public double valor;    // R$
        public String chave;    // se quiser exibir
    }


    // TicketPdfGenerator56mm.java  (adicione abaixo do generate original)
    public static File generate(Context ctx, CloseReport r, String fileName) throws Exception {
        // ===== layout base do seu gerador 56–58 mm =====
        final float MM_TO_PT = 72f / 25.4f;
        final float pageWidthPt = Math.round(56f * MM_TO_PT); // ~160 pt
        final float margin = 6f;
        final float contentW = pageWidthPt - 2*margin;

        Paint small  = p(8f);
        Paint body   = p(9.5f);
        Paint bold   = pBold(10.5f);
        Paint title  = pBold(12.5f);
        Paint dotted = dottedPaint();

        // ===== medição da altura (dinâmica) =====
        float y = margin, h = y;

        // Cabeçalho
        h += blockHeight(centerLines(r.empresaLinha1, body, contentW), body);
        h += blockHeight(centerLines(r.empresaLinha2, body, contentW), body) + 4f;
        h += blockHeight(centerLines("FECHAMENTO DE CAIXA", title, contentW), title) + 8f;

        // Por dia
        for (DayBlock d : r.dias) {
            h += blockHeight(centerLines("Data " + d.diaBR, bold, contentW), bold) + 4f;
            for (UserLine u : d.usuarios) {
                h += body.getTextSize() + 2f; // Usuário
                h += body.getTextSize() + 2f; // Qtd
                h += body.getTextSize() + 4f; // Valor Total
            }
            h += body.getTextSize() + 6f; // Total da data

            if (r.detalhado && d.detalhes != null && !d.detalhes.isEmpty()) {
                h += blockHeight(centerLines("DETALHES", bold, contentW), bold) + 4f;
                for (DetailItem it : d.detalhes) {
                    h += small.getTextSize() + 2f; // linha 1 (data, forpag)
                    h += body.getTextSize() + 4f;  // linha 2 (serviço + valor)
                }
            }

            h += 6f; // espaçamento entre dias
        }

        // Total Geral
        h += blockHeight(centerLines("Total Geral  " + moeda(r.totalGeral), bold, contentW), bold) + 6f;

        h += margin;
        int pageH = (int) Math.ceil(h);

        // ===== desenhar =====
        PdfDocument pdf = new PdfDocument();
        PdfDocument.Page page = pdf.startPage(
                new PdfDocument.PageInfo.Builder((int) pageWidthPt, pageH, 1).create()
        );
        Canvas c = page.getCanvas();
        y = margin;

        y = drawCentered(c, r.empresaLinha1, body, margin, contentW, y);
        y = drawCentered(c, r.empresaLinha2, body, margin, contentW, y); y += 4f;
        y = drawCentered(c, "FECHAMENTO DE CAIXA", title, margin, contentW, y); y += 6f;

        for (DayBlock d : r.dias) {
            // Título do dia
            y = drawCentered(c, "Data " + d.diaBR, bold, margin, contentW, y); y += 2f;
            // Bloco por usuário
            for (UserLine u : d.usuarios) {
                y += body.getTextSize();
                c.drawText("Usuário " + (u.usuario == null ? "-" : u.usuario), margin, y, body);
                y += body.getTextSize() + 2f;
                c.drawText(String.format(java.util.Locale.US, "Total de Tickets %d", u.qtd), margin, y, body);
                y += body.getTextSize() + 2f;
                c.drawText("Valor Total      " + moeda(u.total), margin, y, body);
                y += 4f;
            }
            y += 2f;
            drawDotted(c, dotted, margin, pageWidthPt - margin, y); y += 6f;
            y = drawLeft(c, "Total da Data  " + moeda(d.totalDia), bold, margin, y); y += 6f;

            // Detalhes (opcional)
            if (r.detalhado && d.detalhes != null && !d.detalhes.isEmpty()) {
                y = drawCentered(c, "DETALHES", bold, margin, contentW, y); y += 2f;
                for (DetailItem it : d.detalhes) {
                    y += small.getTextSize();
                    c.drawText(fmtItemLinha1(it), margin, y, small); // ex: "23/10 14:31   PIX"
                    y += body.getTextSize() + 2f;
                    c.drawText(fmtItemLinha2(it), margin, y, body);  // ex: "Serviço — R$ 3,30"
                    y += 2f;
                }
                y += 6f;
            }
        }

        drawDotted(c, dotted, margin, pageWidthPt - margin, y); y += 8f;
        y = drawCentered(c, "Total Geral  " + moeda(r.totalGeral), bold, margin, contentW, y);

        pdf.finishPage(page);

        File dir = ctx.getExternalFilesDir(null);
        if (dir == null) dir = ctx.getFilesDir();
        File out = new File(dir, fileName == null ? "fechamento_56mm.pdf" : fileName);
        try (FileOutputStream fos = new FileOutputStream(out)) { pdf.writeTo(fos); }
        pdf.close();
        return out;
    }

    // ===== helpers internos (reuse os que já existem no seu arquivo, deixo aqui se precisar) =====
    private static Paint p(float sz){ Paint x=new Paint(Paint.ANTI_ALIAS_FLAG); x.setColor(Color.BLACK); x.setTextSize(sz); x.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)); return x; }
    private static Paint pBold(float sz){ Paint x=p(sz); x.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)); return x; }
    private static Paint dottedPaint(){ Paint r=new Paint(Paint.ANTI_ALIAS_FLAG); r.setColor(Color.BLACK); r.setStrokeWidth(0.9f); r.setPathEffect(new DashPathEffect(new float[]{4f,4f},0)); return r; }
    private static void drawDotted(Canvas c, Paint pr, float x1,float x2,float y){ c.drawLine(x1,y,x2,y,pr); }
    private static String moeda(double v){ return java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt","BR")).format(v); }
    private static String fmtHoraMin(String iso){ try{ java.text.SimpleDateFormat a=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US); java.text.SimpleDateFormat b=new java.text.SimpleDateFormat("dd/MM HH:mm", java.util.Locale.getDefault()); return b.format(a.parse(iso)); }catch(Exception e){ return iso; } }
    private static String fmtItemLinha1(DetailItem it){ String t = (it.dathor==null?"":fmtHoraMin(it.dathor)); String fp=(it.forpag==null?"":it.forpag); return (t + (fp.isEmpty()?"":"   "+fp)).trim(); }
    private static String fmtItemLinha2(DetailItem it){ String sv=(it.tipser==null?"":it.tipser); String mv = " — " + moeda(it.valor); return (sv.isEmpty()? moeda(it.valor) : (sv + mv)); }
    private static List<String> wrap(String s, Paint p, float maxW){ List<String> out=new ArrayList<>(); if(s==null) s=""; String[] w=s.trim().split("\\s+"); String line=""; for(String ww:w){ String t=line.isEmpty()?ww:line+" "+ww; if(p.measureText(t)<=maxW) line=t; else{ if(!line.isEmpty()) out.add(line); line=ww; } } if(!line.isEmpty()) out.add(line); if(out.isEmpty()) out.add(""); return out; }
    private static List<String> centerLines(String s, Paint p, float w){ return wrap(s,p,w); }
    private static float blockHeight(List<String> lines, Paint p){ return lines.size()*(p.getTextSize()+2f); }
    private static float drawCentered(Canvas c, String txt, Paint p, float margin, float w, float y){ for(String ln:wrap(txt,p,w)){ y+=p.getTextSize(); float x=margin+(w-p.measureText(ln))/2f; c.drawText(ln,x,y,p); y+=2f;} return y; }
    private static float drawLeft(Canvas c, String txt, Paint p, float margin, float y){ for(String ln:wrap(txt,p,(c.getWidth()-2*margin))){ y+=p.getTextSize(); c.drawText(ln, margin, y, p); y+=2f;} return y; }

}

