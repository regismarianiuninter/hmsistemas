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

import java.util.ArrayList;
import java.util.List;

public class DB_LIN extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "LIN";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 1;
    private static final String LOG_TAG = "LIN";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_LIN(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_LIN_onCreate).split("\n");
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
        //if (newVersion > oldVersion) {
        //    db.execSQL("ALTER TABLE LIN ADD COLUMN Cnae TEXT DEFAULT ''");
        //}
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

    /*Retorna um LIN ordenado
      @param critério de ordenação
     */
    public DB_LIN.LinCursor RetornarLin(DB_LIN.LinCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_LIN.LinCursor.CONSULTA + (ordenarPor == DB_LIN.LinCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_LIN.LinCursor cc = (DB_LIN.LinCursor) bd.rawQueryWithFactory(new DB_LIN.LinCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirLin(String codigo, String descri, String prefix)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Codigo", codigo);
            initialValues.put("Descri", descri);
            initialValues.put("Prefix", prefix);


            return db.insert("LIN", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Lin(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Lin(String id, String codigo, String descri, String prefix) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Codigo", codigo);
        valores.put("Descri", descri);
        valores.put("Prefix", prefix);



        getWritableDatabase().update("LIN", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Lin(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("LIN", valores, "id=?", argumentos);

    }

    public static class LinCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM LIN ORDER BY ID ";

        private LinCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_LIN.LinCursor(db, driver, editTable, query);
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
        public String getDescri() { return getString(getColumnIndexOrThrow("Descri")); }
        public String getPrefix() { return getString(getColumnIndexOrThrow("Prefix")); }



    }


    String BuscaLin(String parametro){
        String selectQuery =
                "SELECT * FROM LIN WHERE Codigo =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Descri"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Lin(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM LIN WHERE Codigo =" + parametro;
        String sretorno = "";

        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);


        if(cursor.getCount() <= 0){
            cursor.close();
            sretorno = "";
        } else {
            cursor.moveToFirst();
            String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

            StringBuilder conversor = new StringBuilder();
            conversor.append(nomeString);
            sretorno = conversor.toString();
        }
        return sretorno;
    }


    String Busca_Dados_Lin_ID(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM LIN WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }







}