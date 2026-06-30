package com.example.usuario.bilhete1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DB_TKT extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "TICKET";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 3;
    private static final String LOG_TAG = "TICKET";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_TKT(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_TKT_onCreate).split("\n");
        db.beginTransaction();
        try
        {
            // Cria a tabela e testa os dados
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_status ON TICKET(Status)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_dathor ON TICKET(Dathor)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_user   ON TICKET(Usuario)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_status_dathor_user " +
                    "ON TICKET(Status, Dathor, Usuario)");
        }
        catch (SQLException e)
        {
            Log.e("Erro ao criar", e.toString());
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to add a column
        if (newVersion > oldVersion) {

            if (oldVersion < 2) {
               // db.execSQL("ALTER TABLE TICKET ADD COLUMN Chvtkt TEXT DEFAULT ''");
                db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_status ON TICKET(Status)");
                db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_dathor ON TICKET(Dathor)");
                db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_user   ON TICKET(Usuario)");
            }
            if (oldVersion< 3) {
                db.execSQL("CREATE INDEX IF NOT EXISTS idx_tkt_status_dathor_user " +
                        "ON TICKET(Status, Dathor, Usuario)");
            }
        }
    }



    /**
     * Executa todos os comandos SQL passados no vetor String[]
     * @param db A base de dados onde os comandos serão executados
     * @param sql Um vetor de comandos SQL a serem executados
     */
    private void ExecutarComandosSQL(SQLiteDatabase db, String[] sql)
    {
        for( String s : sql )
            if (s.trim().length()>0)
                db.execSQL(s);
    }

    /*Retorna um TKT ordenado
      @param critério de ordenação
     */
    public DB_TKT.TktCursor RetornarTkt(DB_TKT.TktCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_TKT.TktCursor.CONSULTA + (ordenarPor == DB_TKT.TktCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_TKT.TktCursor cc = (DB_TKT.TktCursor) bd.rawQueryWithFactory(new DB_TKT.TktCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirTkt(String codigo, String chvtkt, String dathor, String tipser, String vlrtkt, String forpag, String terminal, String usuario, String codope, String sincser, String status)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Codigo", codigo);
            initialValues.put("Chvtkt", chvtkt);
            initialValues.put("Dathor", dathor);
            initialValues.put("Tipser", tipser);
            initialValues.put("Vlrtkt", vlrtkt);
            initialValues.put("Forpag", forpag);
            initialValues.put("Terminal", terminal);
            initialValues.put("Usuario", usuario);
            initialValues.put("Codope", codope);
            initialValues.put("Sincser", sincser);
            initialValues.put("Status", status);


            return db.insert("TICKET", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Tkt(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Tkt(String codigo,String id, String chvtkt, String dathor, String tipser, String vlrtkt, String forpag, String terminal, String usuario, String codope, String sincser, String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};
        valores.put("Codigo", codigo);
        valores.put("Chvtkt", chvtkt);
        valores.put("Dathor", dathor);
        valores.put("Tipser", tipser);
        valores.put("Vlrtkt", vlrtkt);
        valores.put("Forpag", forpag);
        valores.put("Terminal", terminal);
        valores.put("Usuario", usuario);
        valores.put("Codope", codope);
        valores.put("Sincser", sincser);
        valores.put("Status", status);



        getWritableDatabase().update("TICKET", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Tkt(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("TICKET", valores, "id=?", argumentos);

    }

    public static class TktCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM TICKET ORDER BY ID ";

        private TktCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_TKT.TktCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getCodigo()
        {
            return getString(getColumnIndexOrThrow("Codigo"));
        }
        public String getChvtkt()
        {
            return getString(getColumnIndexOrThrow("Chvtkt"));
        }
        public String getDathor() { return getString(getColumnIndexOrThrow("Dathor")); }
        public String getTipser() { return getString(getColumnIndexOrThrow("Tipser")); }
        public String getVlrtkt() { return getString(getColumnIndexOrThrow("Vlrtkt")); }
        public String getForpag() { return getString(getColumnIndexOrThrow("Forpag")); }
        public String getTerminal() { return getString(getColumnIndexOrThrow("Terminal")); }
        public String getUsuario() { return getString(getColumnIndexOrThrow("Usuario")); }
        public String getCodope() { return getString(getColumnIndexOrThrow("Codope")); }
        public String getSincser() { return getString(getColumnIndexOrThrow("Sincser")); }
        public String getStatus() { return getString(getColumnIndexOrThrow("Status")); }


    }


    public List<TicketModel> VerificaTkt(String sNum) {
        List<TicketModel> listaTickets = new ArrayList<TicketModel>();

        if(!sNum.isEmpty()) {
            SQLiteDatabase dbase = getReadableDatabase();
            Cursor cursor = dbase.rawQuery("SELECT * FROM " + "TICKET" + " WHERE Codigo = " + sNum, null);

            if(cursor.moveToFirst()) {
                do {
                    TicketModel element = new TicketModel();
                    element.setID(cursor.getString(0));
                    element.setCodigo(cursor.getString(1));
                    element.setStatus(cursor.getString(11));
                    listaTickets.add(element);
                } while(cursor.moveToNext());
            }
        }

        return listaTickets;
    }
    String BuscaTkt(String parametro){
        String selectQuery =
                "SELECT * FROM TICKET WHERE Codigo = " + "'" + parametro + "'";


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Chvtkt"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_TKT(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM TICKET WHERE Codigo = " + "'" + parametro + "'";



        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_TKT_ID(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM TICKET WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    public static class ResumoCursor extends SQLiteCursor {

        public static enum OrdenarPor {
            TotalDesc
        }

        private static final String SQL =
                "SELECT Forpag AS meio, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                        "FROM TICKET " +
                        "WHERE Status = 'PG' AND Dathor >= ? AND Dathor < ? " +
                        "%USUARIO%" +
                        "GROUP BY Forpag " +
                        "ORDER BY total DESC";

        private ResumoCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
                return new ResumoCursor(db, driver, editTable, query);
            }
        }

        // getters
        public String getMeio()  { return getString(getColumnIndexOrThrow("meio")); }
        public int    getQtd()   { return getInt(getColumnIndexOrThrow("qtd")); }
        public double getTotal() { return getDouble(getColumnIndexOrThrow("total")); }

        /** Abre o cursor já com os binds (ini/fim em "yyyy-MM-dd HH:mm:ss"; fim exclusivo). */
        public static ResumoCursor query(SQLiteDatabase db, String ini, String fim, @Nullable String usuario) {
            String sql = SQL.replace("%USUARIO%", (usuario == null || usuario.isEmpty()) ? "" : "AND Usuario = ? ");
            String[] args = (usuario == null || usuario.isEmpty())
                    ? new String[]{ini, fim}
                    : new String[]{ini, fim, usuario};
            return (ResumoCursor) db.rawQueryWithFactory(new DB_TKT.TktCursor.Factory(), sql, args, null);
        }
        /*public DB_TKT.TktCursor RetornarTkt(DB_TKT.TktCursor.OrdenarPor ordenarPor)
        {
            String sql = DB_TKT.TktCursor.CONSULTA + (ordenarPor == DB_TKT.TktCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
            SQLiteDatabase bd = getReadableDatabase();
            DB_TKT.TktCursor cc = (DB_TKT.TktCursor) bd.rawQueryWithFactory(new DB_TKT.TktCursor.Factory(), sql, null, null);
            cc.moveToFirst();
            return cc;
        }*/

    }

    public static class TotaisCursor extends SQLiteCursor {

        private static final String SQL =
                "SELECT COUNT(*) AS qtd_total, SUM(Vlrtkt) AS total_bruto " +
                        "FROM TICKET " +
                        "WHERE Status = 'PG' AND Dathor >= ? AND Dathor < ? " +
                        "%USUARIO%";

        private TotaisCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
                return new TotaisCursor(db, driver, editTable, query);
            }
        }

        public int    getQtdTotal()   { return getInt(getColumnIndexOrThrow("qtd_total")); }
        public double getTotalBruto() { return getDouble(getColumnIndexOrThrow("total_bruto")); }

        public static TotaisCursor query(SQLiteDatabase db, String ini, String fim, @Nullable String usuario) {
            String sql = SQL.replace("%USUARIO%", (usuario == null || usuario.isEmpty()) ? "" : "AND Usuario = ? ");
            String[] args = (usuario == null || usuario.isEmpty())
                    ? new String[]{ini, fim}
                    : new String[]{ini, fim, usuario};
            return (TotaisCursor) db.rawQueryWithFactory(new Factory(), sql, args, null);
        }
    }

    public static class DetalheCursor extends SQLiteCursor {

        private static final String SQL =
                "SELECT Dathor, Vlrtkt, Forpag, Usuario, Chvtkt, Tipser " +
                        "FROM TICKET " +
                        "WHERE Status = 'PG' AND Dathor >= ? AND Dathor < ? " +
                        "%USUARIO% " +
                        "ORDER BY Dathor ASC";

        private DetalheCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
                return new DetalheCursor(db, driver, editTable, query);
            }
        }

        // getters
        public String getDathor()  { return getString(getColumnIndexOrThrow("Dathor")); }
        public double getValor()   { return getDouble(getColumnIndexOrThrow("Vlrtkt")); }
        public String getForpag()  { return getString(getColumnIndexOrThrow("Forpag")); }
        public String getUsuario() { return getString(getColumnIndexOrThrow("Usuario")); }
        public String getChave()   { return getString(getColumnIndexOrThrow("Chvtkt")); }
        public String getTipser()  { return getString(getColumnIndexOrThrow("Tipser")); }

        public static DetalheCursor query(SQLiteDatabase db, String ini, String fim, @Nullable String usuario) {
            String sql = SQL.replace("%USUARIO%", (usuario == null || usuario.isEmpty()) ? "" : "AND Usuario = ? ");
            String[] args = (usuario == null || usuario.isEmpty())
                    ? new String[]{ini, fim}
                    : new String[]{ini, fim, usuario};
            return (DetalheCursor) db.rawQueryWithFactory(new Factory(), sql, args, null);
        }
    }

    //Relatorio detalhado


    /** Marca como 'F' tudo que foi apurado no intervalo (e, se passado, do usuário). */
    public int fecharTurno(@Nullable String diaBR, @Nullable String usuario) {
        // diaBR: "dd/MM/yyyy" ou null para todos os dias
        // usuario: login ou null para todos
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Status", "F");

        StringBuilder where = new StringBuilder("Status=?");
        List<String> args = new ArrayList<>();
        args.add("PG");

        if (diaBR != null && !diaBR.isEmpty()) {
            // compara só a parte da data (10 chars)
            where.append(" AND substr(Dathor,1,10)=?");
            args.add(diaBR);
        }
        if (usuario != null && !usuario.isEmpty()) {
            where.append(" AND Usuario=?");
            args.add(usuario);
        }

        int rows;
        db.beginTransaction();
        try {
            rows = db.update("TICKET", cv, where.toString(), args.toArray(new String[0]));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rows;
    }

    public int contarPG(@Nullable String diaBR, @Nullable String usuario) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder where = new StringBuilder("Status=?");
        List<String> args = new ArrayList<>();
        args.add("PG");

        if (diaBR != null && !diaBR.isEmpty()) {
            where.append(" AND substr(Dathor,1,10)=?"); // "dd/MM/yyyy"
            args.add(diaBR);
        }
        if (usuario != null && !usuario.isEmpty()) {
            where.append(" AND Usuario=?");
            args.add(usuario);
        }

        try (Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM TICKET WHERE " + where, args.toArray(new String[0]))) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    // Quantos registros estão com Status='F'
    public int contarStatusF() {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.rawQuery("SELECT COUNT(*) FROM TICKET WHERE Status='F'", null)) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    // Resumo por Data (dia) e Usuário — SOMENTE Status='F'
    public Cursor listarResumoFPorDiaUsuario() {
        SQLiteDatabase db = getReadableDatabase();
        String sql =
                "SELECT " +
                        "  (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)) AS diaISO, " +
                        "  Usuario AS usuario, " +
                        "  COUNT(*) AS qtd, " +
                        "  SUM(Vlrtkt) AS total " +
                        "FROM TICKET " +
                        "WHERE Status='F' " +
                        "GROUP BY (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)), Usuario " +
                        "ORDER BY diaISO ASC, Usuario ASC";
        return db.rawQuery(sql, null);
    }

    // Resumo por Forma de Pagamento — SOMENTE Status='F' (para o bloco final)
    public Cursor listarResumoFPagtoGeral() {
        SQLiteDatabase db = getReadableDatabase();
        String sql =
                "SELECT Forpag AS forma, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                        "FROM TICKET WHERE Status='F' " +
                        "GROUP BY Forpag ORDER BY Forpag ASC";
        return db.rawQuery(sql, null);
    }

    // Deletar TODOS os registros com Status='F'
    public int deletarTodosStatusF() {
        SQLiteDatabase db = getWritableDatabase();
        int rows;
        db.beginTransaction();
        try {
            rows = db.delete("TICKET", "Status=?", new String[]{"F"});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rows;
    }

    // (Opcional) Compactar o banco após apagar
    public void compactarBanco() {
        SQLiteDatabase db = getWritableDatabase();
        try { db.execSQL("PRAGMA wal_checkpoint(FULL)"); } catch (Exception ignored) {}
        try { db.execSQL("VACUUM"); } catch (Exception ignored) {}
    }

    public static class FechamentoPorDiaUsuarioCursor extends SQLiteCursor {
        private static final String SQL =
                "SELECT (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)) AS diaISO," +
                              " Usuario AS usuario, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                        "FROM TICKET " +
                        "WHERE Status='PG' " +
                        "GROUP BY (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)), Usuario " +
                        "ORDER BY diaISO ASC, Usuario ASC";

        private FechamentoPorDiaUsuarioCursor(SQLiteDatabase db, SQLiteCursorDriver dr, String et, SQLiteQuery q) {
            super(db, dr, et, q);
        }
        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver dr, String et, SQLiteQuery q) {
                return new FechamentoPorDiaUsuarioCursor(db, dr, et, q);
            }
        }

        public String getDia()     { return getString(getColumnIndexOrThrow("diaISO")); }
        public String getUsuario() { return getString(getColumnIndexOrThrow("usuario")); }
        public int    getQtd()     { return getInt(getColumnIndexOrThrow("qtd")); }
        public double getTotal()   { return getDouble(getColumnIndexOrThrow("total")); }

        public static FechamentoPorDiaUsuarioCursor query(SQLiteDatabase db) {
            return (FechamentoPorDiaUsuarioCursor)
                    db.rawQueryWithFactory(new Factory(), SQL, null, null);
        }
    }

    public static class TotalGeralCursor extends SQLiteCursor {
        private static final String SQL =
                "SELECT COUNT(*) AS qtd_total, SUM(Vlrtkt) AS total_geral " +
                        "FROM TICKET WHERE Status='PG'";
        private TotalGeralCursor(SQLiteDatabase db, SQLiteCursorDriver d, String e, SQLiteQuery q) { super(db,d,e,q); }
        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver d, String e, SQLiteQuery q) {
                return new TotalGeralCursor(db,d,e,q);
            }
        }
        public int getQtdTotal()        { return getInt(getColumnIndexOrThrow("qtd_total")); }
        public double getTotalGeral()   { return getDouble(getColumnIndexOrThrow("total_geral")); }
        public static TotalGeralCursor query(SQLiteDatabase db){
            return (TotalGeralCursor) db.rawQueryWithFactory(new Factory(), SQL, null, null);
        }
    }






}
