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

public class DB_VIA extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "VIAGENS";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 4;
    private static final String LOG_TAG = "VIAGENS";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;

    public DB_VIA(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_VIA_onCreate).split("\n");
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
   /* public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirá todos os dados antigos");
        String[] sql = contexto.getString(R.string.DB_VIA_onUpgrade).split("\n");
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

    /*Retorna um ViagensCursor ordenado
      @param critério de ordenação
     */
    public ViagensCursor RetornarViagens(ViagensCursor.OrdenarPor ordenarPor)
    {
        String sql = ViagensCursor.CONSULTA + (ordenarPor == ViagensCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        ViagensCursor cc = (ViagensCursor) bd.rawQueryWithFactory(new ViagensCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirViagem(String linha, String viagem, String hora, String tipvia, String tipser, String prefix)
    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Linha", linha);
            initialValues.put("Viagem", viagem);
            initialValues.put("Hora", hora);
            initialValues.put("Tipvia", tipvia);
            initialValues.put("Tipser", tipser);
            initialValues.put("Prefix", prefix);
            return db.insert("Viagens", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Via(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }


    public void Atualizar_Via(String oldID, String id, String viagem, String hora, String tipvia, String tipser, String prefix) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {oldID};

        valores.put("ID", id);
        valores.put("Viagem", viagem);
        valores.put("Hora", hora);
        valores.put("Tipvia", tipvia);
        valores.put("Tipser", tipser);
        valores.put("Prefix", prefix);


        getWritableDatabase().update("VIAGENS", valores, "id=?", argumentos);
    }

    public static class ViagensCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM Viagens ORDER BY ID ";

        private ViagensCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new ViagensCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }

        public String getLinha()
        {
            return getString(getColumnIndexOrThrow("Linha"));
        }

        public String getViagem()
        {
            return getString(getColumnIndexOrThrow("Viagem"));
        }

        public String getHora()
        {
            return getString(getColumnIndexOrThrow("Hora"));
        }

        public String getTipvia() { return getString(getColumnIndexOrThrow("Tipvia"));}

        public String getTipser() { return getString(getColumnIndexOrThrow("Tipser"));}

        public String getPrefix() { return getString(getColumnIndexOrThrow("Prefix"));}

    }



    String BuscaVia(int parametro){
        String selectQuery =
                "SELECT * FROM Viagens WHERE ID =" + parametro;

        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Linha"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Via(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM Viagens WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }


}
