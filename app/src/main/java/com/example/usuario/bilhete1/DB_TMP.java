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

public class DB_TMP extends SQLiteOpenHelper {
    /**
     * O nome do arquivo de base de dados no sistema de arquivos
     */
    private static final String NOME_BD = "TMP";
    /**
     * A versão da base de dados que esta classe compreende.
     */
    private static final int VERSAO_BD = 3;
    private static final String LOG_TAG = "TMP";
    /**
     * Mantém rastreamento do contexto que nós podemos carregar SQL
     */
    private final Context contexto;


    public DB_TMP(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] sql = contexto.getString(R.string.DB_TMP_onCreate).split("\n");
        db.beginTransaction();

        try {
            // Cria a tabela e testa os dados
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("Erro ao criar", e.toString());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If you need to add a column
        if (newVersion > oldVersion) {
            if (oldVersion < 2) {
                db.execSQL("ALTER TABLE TMP ADD COLUMN Vlrvale TEXT DEFAULT ''");

            }
            if (oldVersion < 3) {
                db.execSQL("ALTER TABLE TMP ADD COLUMN Vlrembd TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE TMP ADD COLUMN Vlrembo TEXT DEFAULT ''");
            }

        }
    }


    /**
     * Executa todos os comandos SQL passados no vetor String[]
     *
     * @param db  A base de dados onde os comandos serão executados
     * @param sql Um vetor de comandos SQL a serem executados
     */
    private void ExecutarComandosSQL(SQLiteDatabase db, String[] sql) {
        for (String s : sql)
            if (s.trim().length() > 0)
                db.execSQL(s);
    }

    /*Retorna um TMP ordenado
      @param critério de ordenação
     */
    public DB_TMP.TmpCursor RetornarTmp(DB_TMP.TmpCursor.OrdenarPor ordenarPor) {
        String sql = DB_TMP.TmpCursor.CONSULTA + (ordenarPor == DB_TMP.TmpCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_TMP.TmpCursor cc = (DB_TMP.TmpCursor) bd.rawQueryWithFactory(new DB_TMP.TmpCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirTmp(String codigo, String descri, String agente, String datemi, String qtdbil, String vlrtot, String vlrvale, String vlrembd, String vlrembo)

    {
        SQLiteDatabase db = getReadableDatabase();

        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Codigo", codigo);
            initialValues.put("Descri", descri);
            initialValues.put("Agente", agente);
            initialValues.put("Datemi", datemi);
            initialValues.put("Qqtbil", qtdbil);
            initialValues.put("Vlrtot", vlrtot);
            initialValues.put("Vlrvale", vlrvale);
            initialValues.put("Vlrembd", vlrembd);
            initialValues.put("Vlrembo", vlrembo);


            return db.insert("TMP", null, initialValues);
        } finally {
            db.close();
        }
    }

    public void deletar_TMP() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Tmp(String id, String codigo, String descri, String agente, String datemi, String qtdbil, String vlrtot, String vlrvale, String vlrembd, String vlrembo) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Codigo", codigo);
        valores.put("Descri", descri);
        valores.put("Agente", agente);
        valores.put("Datemi", datemi);
        valores.put("Qqtbil", qtdbil);
        valores.put("Vlrtot", vlrtot);
        valores.put("Vlrvale", vlrvale);
        valores.put("Vlrembd", vlrembd);
        valores.put("Vlrembo", vlrembo);


        getWritableDatabase().update("TMP", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Tmp(String id, String campo, String novoval) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("TMP", valores, "id=?", argumentos);

    }

    public static class TmpCursor extends SQLiteCursor {


        public static enum OrdenarPor {
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM TMP ORDER BY ID ";

        private TmpCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
                return new DB_TMP.TmpCursor(db, driver, editTable, query);
            }
        }

        public long getID() {
            return getLong(getColumnIndexOrThrow("ID"));
        }

        public String getCodigo() {
            return getString(getColumnIndexOrThrow("Codigo"));
        }

        public String getDescri() { return getString(getColumnIndexOrThrow("Descri"));}
        public String getAgente() { return getString(getColumnIndexOrThrow("Agente"));}
        public String getDatemi() { return getString(getColumnIndexOrThrow("Datemi"));}
        public String getQtdbil() { return getString(getColumnIndexOrThrow("Qqtbil"));}
        public String getVlrtot() { return getString(getColumnIndexOrThrow("Vlrtot"));}
        public String getVlrvale() { return getString(getColumnIndexOrThrow("Vlrvale"));}
        public String getVlrembd() { return getString(getColumnIndexOrThrow("Vlrembd"));}
        public String getVlrembo() { return getString(getColumnIndexOrThrow("Vlrembo"));}


    }


    String BuscaTmp(String parametro) {
        String selectQuery =
                "SELECT * FROM TMP WHERE Codigo = " + "'" + parametro + "'";


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Descri"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Tmp(String parametro, String scampo) {
        String selectQuery =
                "SELECT * FROM TMP WHERE Codigo = " + "'" + parametro + "'";
        String sretorno = "";

        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);


        if (cursor.getCount() <= 0) {
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


    String Busca_Dados_Tmp_ID(int parametro, String scampo) {
        String selectQuery =
                "SELECT * FROM TMP WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }
}



