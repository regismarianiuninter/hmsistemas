package com.example.usuario.bilhete1.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CashCloseRepository {

    private final SQLiteDatabase db;

    public static final class SummaryRow {
        public String meio;
        public int    qtd;
        public double total;
    }

    public static final class SummaryTotals {
        public int    qtdTotal;
        public double totalBruto;
    }

    public static final class DetailRow {
        public String dathor;   // "yyyy-MM-dd HH:mm:ss"
        public double valor;
        public String forpag;
        public String nomusr;
        public String chave;
        public String tipser;
    }

    public CashCloseRepository(SQLiteDatabase db) {
        this.db = db;
    }

    public List<SummaryRow> loadSummary(String ini, String fim, @Nullable String usuario) {
        String sql = "SELECT Forpag AS meio, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                "FROM TICKET WHERE Dathor >= ? AND Dathor < ? " +
                (usuario == null ? "" : "AND Usuario = ? ") +
                "GROUP BY Forpag ORDER BY total DESC";
        String[] args = usuario == null ? new String[]{ini, fim} : new String[]{ini, fim, usuario};

        Cursor c = db.rawQuery(sql, args);
        List<SummaryRow> out = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                SummaryRow r = new SummaryRow();
                r.meio  = c.getString(c.getColumnIndexOrThrow("meio"));
                r.qtd   = c.getInt(c.getColumnIndexOrThrow("qtd"));
                r.total = c.getDouble(c.getColumnIndexOrThrow("total"));
                out.add(r);
            }
        } finally { c.close(); }
        return out;
    }

    public SummaryTotals loadTotals(String ini, String fim, @Nullable String usuario) {
        String sql = "SELECT COUNT(*) AS qtd_total, SUM(Vlrtkt) AS total_bruto " +
                "FROM TICKET WHERE Dathor >= ? AND Dathor < ? " +
                (usuario == null ? "" : "AND Usuario = ?");
        String[] args = usuario == null ? new String[]{ini, fim} : new String[]{ini, fim, usuario};
        Cursor c = db.rawQuery(sql, args);
        SummaryTotals t = new SummaryTotals();
        try {
            if (c.moveToFirst()) {
                t.qtdTotal   = c.getInt(c.getColumnIndexOrThrow("qtd_total"));
                t.totalBruto = c.getDouble(c.getColumnIndexOrThrow("total_bruto"));
            }
        } finally { c.close(); }
        return t;
    }

    public List<DetailRow> loadDetails(String ini, String fim, @Nullable String usuario) {
        String sql = "SELECT Dathor, Vlrtkt, Forpag, Usuario, Chvtkt, Tipser " +
                "FROM TICKET WHERE Dathor >= ? AND Dathor < ? " +
                (usuario == null ? "" : "AND Usuario = ? ") +
                "ORDER BY Dathor ASC";
        String[] args = usuario == null ? new String[]{ini, fim} : new String[]{ini, fim, usuario};

        Cursor c = db.rawQuery(sql, args);
        List<DetailRow> out = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                DetailRow r = new DetailRow();
                r.dathor = c.getString(c.getColumnIndexOrThrow("Dathor"));
                r.valor  = c.getDouble(c.getColumnIndexOrThrow("Vlrtkt"));
                r.forpag = c.getString(c.getColumnIndexOrThrow("Forpag"));
                r.nomusr = c.getString(c.getColumnIndexOrThrow("Usuario"));
                r.chave  = c.getString(c.getColumnIndexOrThrow("Chvtkt"));
                r.tipser = c.getString(c.getColumnIndexOrThrow("Tipser"));
                out.add(r);
            }
        } finally { c.close(); }
        return out;
    }
}

