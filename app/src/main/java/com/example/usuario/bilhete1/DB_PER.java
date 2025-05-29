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

public class DB_PER extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "PERCURSOS";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 2;
    private static final String LOG_TAG = "PERCURSOS";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;

    public DB_PER(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_PER_onCreate).split("\n");
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(LOG_TAG, "Atualizando a base de dados da versão " + oldVersion + " para " + newVersion + ", que destruirá todos os dados antigos");
        String[] sql = contexto.getString(R.string.DB_PER_onUpgrade).split("\n");
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

    /*Retorna um PercursosCursor ordenado
      @param critério de ordenação
     */
    public PercursosCursor RetornarPercursos(PercursosCursor.OrdenarPor ordenarPor)
    {
        String sql = PercursosCursor.CONSULTA + (ordenarPor == PercursosCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        PercursosCursor cc = (PercursosCursor) bd.rawQueryWithFactory(new PercursosCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirPercurso(String linha, String origem, String destino, String tarifa, String seguro, String arredonda, String tipvia)
    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Linha", linha);
            initialValues.put("Origem", origem);
            initialValues.put("Destino", destino);
            initialValues.put("Tarifa", tarifa);
            initialValues.put("Seguro", seguro);
            initialValues.put("Arredonda", arredonda);
            initialValues.put("Tipvia", tipvia);
            return db.insert("Percursos", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Per(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public static class PercursosCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM Percursos ORDER BY ID ";

        private PercursosCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new PercursosCursor(db, driver, editTable, query);
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

        public String getOrigem()
        {
            return getString(getColumnIndexOrThrow("Origem"));
        }

        public String getDestino()
        {
            return getString(getColumnIndexOrThrow("Destino"));
        }

        public String getTarifa()
        {
            return getString(getColumnIndexOrThrow("Tarifa"));
        }

        public String getSeguro()
        {
            return getString(getColumnIndexOrThrow("Seguro"));
        }

        public String getArredonda()
        {
            return getString(getColumnIndexOrThrow("Arredonda"));
        }

        public String getTipvia()
        {
            return getString(getColumnIndexOrThrow("Tipvia"));
        }
    }

    String BuscaPer(String slinha, String sorigem, String sdestino, String scampo){
        String selectQuery =
                "SELECT * FROM Percursos WHERE Linha =" + slinha;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }


}
