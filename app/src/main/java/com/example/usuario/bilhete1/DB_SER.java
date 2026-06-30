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

public class DB_SER extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "SER";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 1;
    private static final String LOG_TAG = "SER";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_SER(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_SER_onCreate).split("\n");
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
               // db.execSQL("ALTER TABLE SER ADD COLUMN Tipdes TEXT DEFAULT ''");
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

    /*Retorna um SER ordenado
      @param critério de ordenação
     */
    public DB_SER.SerCursor RetornarSer(DB_SER.SerCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_SER.SerCursor.CONSULTA + (ordenarPor == DB_SER.SerCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_SER.SerCursor cc = (DB_SER.SerCursor) bd.rawQueryWithFactory(new DB_SER.SerCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirSer(String tipser, String vlrser)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Tipser", tipser);
            initialValues.put("Vlrser", vlrser);



            return db.insert("SER", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_SER(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Ser(String id, String tipser, String vlrser) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Tipser", tipser);
        valores.put("Vlrser", vlrser);




        getWritableDatabase().update("SER", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Ser(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("SER", valores, "id=?", argumentos);

    }

    public static class SerCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM SER ORDER BY ID ";

        private SerCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_SER.SerCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getTipser()
        {
            return getString(getColumnIndexOrThrow("Tipser"));
        }
        public String getVlrser() { return getString(getColumnIndexOrThrow("Vlrser")); }





    }

    String BuscaSer(String parametro){
        String selectQuery =
                "SELECT * FROM SER WHERE Tipser = " + "'" + parametro + "'";


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String valString = cursor.getString(cursor.getColumnIndex("Vlrser"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(valString);
        return conversor.toString();

    }

    String Busca_Dados_Ser(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM SER WHERE Tipser = " + "'" + parametro + "'";



        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_SER_ID(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM SER WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

}

