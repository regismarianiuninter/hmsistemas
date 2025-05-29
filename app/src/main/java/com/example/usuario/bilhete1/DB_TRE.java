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
import android.widget.Toast;

public class DB_TRE extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "TRECHOS";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 2;
    private static final String LOG_TAG = "TRECHOS";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;

    public DB_TRE(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_TRE_onCreate).split("\n");
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
        /*if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE VIAGENS ADD COLUMN Tipvia TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE VIAGENS ADD COLUMN Tipser TEXT DEFAULT ''");
            db.execSQL("ALTER TABLE VIAGENS ADD COLUMN Prefix TEXT DEFAULT ''");
        }*/
    }



    /* @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirá todos os dados antigos");
        String[] sql = contexto.getString(R.string.DB_TRE_onUpgrade).split("\n");
        db.beginTransaction();

        try
        {
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            Log.e("Erro ao atualizar", e.toString());
            throw e;
        }
        finally
        {
            db.endTransaction();
        }

        // Isto é apenas didático. Na vida real, você terá de adicionar novas colunas e não apenas recriar o mesmo banco
        onCreate(db);
    }*/

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

    /*Retorna um TrechosCursor ordenado
      @param critério de ordenação
     */
    public TrechosCursor RetornarTrechos(TrechosCursor.OrdenarPor ordenarPor)
    {
        String sql = TrechosCursor.CONSULTA + (ordenarPor == TrechosCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        TrechosCursor cc = (TrechosCursor) bd.rawQueryWithFactory(new TrechosCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirTrecho(String codigo, String descri, String uf, String codmun)
    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Codigo", codigo);
            initialValues.put("Descri", descri);
            initialValues.put("UF", uf);
            initialValues.put("Codmun", codmun);
            return db.insert("Trechos", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Tre(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
       // db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE NAME = '"+NOME_BD+"'");
        //db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + NOME_BD + "'");
        //db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{NOME_BD});
       // db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = " + NOME_BD);
        db.close();
    }

    public void Atualizar_Tre(String idOld, String id, String codigo, String descri, String uf, String codmun) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {idOld};

        valores.put("ID", id);
        valores.put("Codigo", codigo);
        valores.put("Descri", descri);
        valores.put("UF", uf);
        valores.put("Codmun", codmun);

        getWritableDatabase().update("Trechos", valores, "id=?", argumentos);
    }


    public static class TrechosCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM Trechos ORDER BY ID ";

        private TrechosCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new TrechosCursor(db, driver, editTable, query);
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

        public String getDescri()
        {
            return getString(getColumnIndexOrThrow("Descri"));
        }

        public String getUF()
        {
            return getString(getColumnIndexOrThrow("UF"));
        }

        public String getCodmun()
        {
            return getString(getColumnIndexOrThrow("Codmun"));
        }


    }

    String BuscaTre(int parametro){
        String selectQuery =
                "SELECT * FROM Trechos WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Codigo"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Tre(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM Trechos WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Tre_Codigo(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM Trechos WHERE Codigo =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }




}