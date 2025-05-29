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

public class DB_GRA extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "GRA";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 5;
    private static final String LOG_TAG = "GRA";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_GRA(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_GRA_onCreate).split("\n");
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

            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE GRA ADD COLUMN Tipdes TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE GRA ADD COLUMN Mesano TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE GRA ADD COLUMN Qtdpas TEXT DEFAULT ''");
            }
            if (oldVersion <4) {
                db.execSQL("ALTER TABLE GRA RENAME COLUMN Nomepas TO Nompas");
            }
            if (oldVersion <5) {
                db.execSQL("ALTER TABLE GRA ADD COLUMN Qtdmes TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE GRA ADD COLUMN Qtdlin TEXT DEFAULT ''");
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

    /*Retorna um GRA ordenado
      @param critério de ordenação
     */
    public DB_GRA.GraCursor RetornarGra(DB_GRA.GraCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_GRA.GraCursor.CONSULTA + (ordenarPor == DB_GRA.GraCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_GRA.GraCursor cc = (DB_GRA.GraCursor) bd.rawQueryWithFactory(new DB_GRA.GraCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirGra(String cpfpas, String nomepas, String docpas, String linha, String sentido, String tipgra, String tipdes, String mesano, String qtdpas, String qtdmes, String qtdlin)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Cpfpas", cpfpas);
            initialValues.put("Nompas", nomepas);
            initialValues.put("Docpas", docpas);
            initialValues.put("Linha", linha);
            initialValues.put("Sentido", sentido);
            initialValues.put("Tipgra", tipgra);
            initialValues.put("Tipdes", tipdes);
            initialValues.put("Mesano", mesano);
            initialValues.put("Qtdpas", qtdpas);
            initialValues.put("Qtdmes", qtdmes);
            initialValues.put("Qtdlin", qtdlin);


            return db.insert("GRA", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Gra(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Gra(String id, String cpfpas, String nompas, String docpas, String linha, String sentido, String tipgra, String tipdes, String mesano, String qtdpas, String qtdmes, String qtdlin) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Cpfpas", cpfpas);
        valores.put("Nompas", nompas);
        valores.put("Docpas", docpas);
        valores.put("Linha", linha);
        valores.put("Sentido", sentido);
        valores.put("Tipgra", tipgra);
        valores.put("Tipdes", tipdes);
        valores.put("Mesano", mesano);
        valores.put("Qtdpas", qtdpas);
        valores.put("Qtdmes", qtdmes);
        valores.put("Qtdlin", qtdlin);



        getWritableDatabase().update("GRA", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Gra(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("GRA", valores, "id=?", argumentos);

    }

    public static class GraCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM GRA ORDER BY ID ";

        private GraCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_GRA.GraCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getCpfpas()
        {
            return getString(getColumnIndexOrThrow("Cpfpas"));
        }
        public String getNompas() { return getString(getColumnIndexOrThrow("Nompas")); }
        public String getDocpas() { return getString(getColumnIndexOrThrow("Docpas")); }
        public String getLinha() { return getString(getColumnIndexOrThrow("Linha")); }
        public String getSentido() { return getString(getColumnIndexOrThrow("Sentido")); }
        public String getTipgra() { return getString(getColumnIndexOrThrow("Tipgra")); }
        public String getTipdes() { return getString(getColumnIndexOrThrow("Tipdes")); }
        public String getMesano() { return getString(getColumnIndexOrThrow("Mesano")); }
        public String getQtdpas() { return getString(getColumnIndexOrThrow("Qtdpas")); }
        public String getQtdmes() { return getString(getColumnIndexOrThrow("Qtdmes")); }
        public String getQtdlin() { return getString(getColumnIndexOrThrow("Qtdlin")); }




    }

    String BuscaGra(String parametro){
        String selectQuery =
                "SELECT * FROM GRA WHERE Cpfpas = " + "'" + parametro + "'";


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Nompas"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Gra(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM GRA WHERE Cpfpas = " + "'" + parametro + "'";



        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_GRA_ID(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM GRA WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

}
