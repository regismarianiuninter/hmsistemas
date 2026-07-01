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



public class DB_USR extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "USR";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 5;
    private static final String LOG_TAG = "USR";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_USR(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_USR_onCreate).split("\n");
        db.beginTransaction();

        try
        {
            // Cria a tabela e testa os dados
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
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
            if (oldVersion < 4) {
                db.execSQL("ALTER TABLE USR ADD COLUMN Tippvd TEXT DEFAULT ''");
            }
            if (oldVersion < 5) {
                db.execSQL("ALTER TABLE USR ADD COLUMN Atuusr TEXT DEFAULT ''");
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

    /*Retorna um USR ordenado
      @param critério de ordenação
     */
    public DB_USR.UsrCursor RetornarUsr(DB_USR.UsrCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_USR.UsrCursor.CONSULTA + (ordenarPor == DB_USR.UsrCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_USR.UsrCursor cc = (DB_USR.UsrCursor) bd.rawQueryWithFactory(new DB_USR.UsrCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirUsr(String usrnom, String usrsen, String fectur, String ultatu, String tippvd, String atuusr)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Usrnom", usrnom);
            initialValues.put("Usrsen", usrsen);
            initialValues.put("Fectur", fectur);
            initialValues.put("Ultatu", ultatu);
            initialValues.put("Tippvd", tippvd);
            initialValues.put("Atuusr", atuusr);

            return db.insert("USR", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Usr(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Usr(String id, String usrnom, String usrsen, String fectur, String ultatu, String tippvd, String atuusr) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Usrnom", usrnom);
        valores.put("Usrsen", usrsen);
        valores.put("Fectur", fectur);
        valores.put("Ultatu", ultatu);
        valores.put("Tippvd", tippvd);
        valores.put("Atuusr", atuusr);



        getWritableDatabase().update("USR", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Usr(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("USR", valores, "id=?", argumentos);

    }

    public boolean UsuarioExiste(String usrnom) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM USR WHERE UPPER(TRIM(Usrnom)) = UPPER(TRIM(?)) LIMIT 1", new String[]{usrnom});
        try {
            return cursor.moveToFirst();
        } finally {
            cursor.close();
        }
    }

    public static class UsrCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM USR ORDER BY ID ";

        private UsrCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_USR.UsrCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getUsrnom()
        {
            return getString(getColumnIndexOrThrow("Usrnom"));
        }
        public String getUsrsen() { return getString(getColumnIndexOrThrow("Usrsen")); }
        public String getFectur() { return getString(getColumnIndexOrThrow("Fectur")); }
        public String getUltatu() { return getString(getColumnIndexOrThrow("Ultatu")); }
        public String getTippvd() { return getString(getColumnIndexOrThrow("Tippvd")); }
        public String getAtuusr() { return getString(getColumnIndexOrThrow("Atuusr")); }



    }

    String BuscaUsr(String parametro){
        String selectQuery =
                "SELECT * FROM Usr WHERE Usrnom = " + "'" + parametro + "'";


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Usrsen"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Usr(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM USR WHERE Usrnom = " + "'" + parametro + "'";



        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Usr_ID(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM USR WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }







}
