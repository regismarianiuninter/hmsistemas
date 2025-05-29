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

public class DB_EMP extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "EMP";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 23;
    private static final String LOG_TAG = "EMP";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;

    public DB_EMP(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_EMP_onCreate).split("\n");
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
            if (oldVersion < 17) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Ultdat TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE EMP ADD COLUMN Cartao TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE EMP ADD COLUMN Basseg TEXT DEFAULT ''");
            }
            else if (oldVersion < 18) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Cartao TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE EMP ADD COLUMN Basseg TEXT DEFAULT ''");
            }
            else if (oldVersion < 19) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Basseg TEXT DEFAULT ''");
            }
            else if (oldVersion < 20) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Naokey TEXT DEFAULT ''");
            }
            else if (oldVersion < 21) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Mincan TEXT DEFAULT ''");
            }
            else if (oldVersion < 22) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Ultqrc TEXT DEFAULT ''");
            }
            else if (oldVersion < 23) {
                db.execSQL("ALTER TABLE EMP ADD COLUMN Ndstax TEXT DEFAULT ''");
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

    /*Retorna um Emp ordenado
      @param critério de ordenação
     */
    public DB_EMP.EmpCursor RetornarEmp(DB_EMP.EmpCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_EMP.EmpCursor.CONSULTA + (ordenarPor == DB_EMP.EmpCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_EMP.EmpCursor cc = (DB_EMP.EmpCursor) bd.rawQueryWithFactory(new DB_EMP.EmpCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirEmp(String cnpj, String descri, String insest, String insmun, String codcrt, String endere, String numero, String bairro,
                           String cidade, String uf, String codmun, String cep, String fone, String email, String codTar, String tipamb,
                           String modelo, String serie, String ultbil, String tipemi, String tipbil, String datctg, String cnae, String empspl,
                           String aliicm, String urlqrc, String crtemp, String crtsen, String alitri, String maximp, String convei, String pvenda, String endews,
                           String rsv001, String rsv002, String rsv003, String modimp, String ultdat, String cartao, String basseg, String naokey, String mincan, String ultqrc, String ndstax)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Cnpj", cnpj);
            initialValues.put("Descri", descri);
            initialValues.put("Insest", insest);
            initialValues.put("Insmun", insmun);
            initialValues.put("Codcrt", codcrt);
            initialValues.put("Endere", endere);
            initialValues.put("Numero", numero);
            initialValues.put("Bairro", bairro);
            initialValues.put("Cidade", cidade);
            initialValues.put("UF", uf);
            initialValues.put("Codmun", codmun);
            initialValues.put("CEP", cep);
            initialValues.put("Fone", fone);
            initialValues.put("Email", email);
            initialValues.put("CodTar", codTar);
            initialValues.put("Tipamb", tipamb);
            initialValues.put("Modelo", modelo);
            initialValues.put("Serie", serie);
            initialValues.put("Ultbil", ultbil);
            initialValues.put("Tipemi", tipemi);
            initialValues.put("Tipbil", tipbil);
            initialValues.put("Datctg", datctg);
            initialValues.put("Cnae", cnae);
            initialValues.put("Empspl", empspl);
            initialValues.put("Aliicm", aliicm);
            initialValues.put("Urlqrc", urlqrc);
            initialValues.put("Crtemp", crtemp);
            initialValues.put("Crtsen", crtsen);
            initialValues.put("Alitri", alitri);
            initialValues.put("Maximp", maximp);
            initialValues.put("Convei", convei);
            initialValues.put("Pvenda", pvenda);
            initialValues.put("Endews", endews);
            initialValues.put("Rsv001", rsv001);
            initialValues.put("Rsv002", rsv002);
            initialValues.put("Rsv003", rsv003);
            initialValues.put("Modimp", modimp);
            initialValues.put("Ultdat", ultdat);
            initialValues.put("Cartao", cartao);
            initialValues.put("Basseg", basseg);
            initialValues.put("Naokey", naokey);
            initialValues.put("Mincan", mincan);
            initialValues.put("Ultqrc", ultqrc);
            initialValues.put("Ndstax", ndstax);

            return db.insert("EMP", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Emp(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void Atualizar_Emp(String oldID, String id, String cnpj, String descri, String insest, String insmun, String codcrt, String endere, String numero, String bairro,
                              String cidade, String uf, String codmun, String cep, String fone, String email, String codTar, String tipamb,
                              String modelo, String serie, String ultbil, String tipemi, String tipbil, String datctg, String cnae, String empspl,
                              String aliicm, String urlqrc, String crtemp, String crtsen, String alitri, String maximp, String convei, String pvenda, String endews,
                              String rsv001, String rsv002, String rsv003, String modimp, String ultdat, String cartao, String basseg, String naokey, String mincan,
                              String ultqrc, String ndstax) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {oldID};

        valores.put("ID", id);
        valores.put("Cnpj", cnpj);
        valores.put("Descri", descri);
        valores.put("Insest", insest);
        valores.put("Insmun", insmun);
        valores.put("Codcrt", codcrt);
        valores.put("Endere", endere);
        valores.put("Numero", numero);
        valores.put("Bairro", bairro);
        valores.put("Cidade", cidade);
        valores.put("UF", uf);
        valores.put("Codmun", codmun);
        valores.put("CEP", cep);
        valores.put("Fone", fone);
        valores.put("Email", email);
        valores.put("CodTar", codTar);
        valores.put("Tipamb", tipamb);
        valores.put("Modelo", modelo);
        valores.put("Serie", serie);
        valores.put("Ultbil", ultbil);
        valores.put("Tipemi", tipemi);
        valores.put("Tipbil", tipbil);
        valores.put("Datctg", datctg);
        valores.put("Cnae", cnae);
        valores.put("Empspl", empspl);
        valores.put("Aliicm", aliicm);
        valores.put("Urlqrc", urlqrc);
        valores.put("Crtemp", crtemp);
        valores.put("Crtsen", crtsen);
        valores.put("Alitri", alitri);
        valores.put("Maximp", maximp);
        valores.put("Convei", convei);
        valores.put("Pvenda", pvenda);
        valores.put("Endews", endews);
        valores.put("Rsv001", rsv001);
        valores.put("Rsv002", rsv002);
        valores.put("Rsv003", rsv003);
        valores.put("Modimp", modimp);
        valores.put("Ultdat", ultdat);
        valores.put("Cartao", cartao);
        valores.put("Basseg", basseg);
        valores.put("Naokey", naokey);
        valores.put("Mincan", mincan);
        valores.put("Ultqrc", ultqrc);
        valores.put("Ndstax", ndstax);

        getWritableDatabase().update("EMP", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Emp(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("EMP", valores, "id=?", argumentos);

    }

    public static class EmpCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM EMP ORDER BY ID ";

        private EmpCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_EMP.EmpCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getCnpj()
        {
            return getString(getColumnIndexOrThrow("Cnpj"));
        }
        public String getDescri() { return getString(getColumnIndexOrThrow("Descri")); }
        public String getInsest(){return getString(getColumnIndexOrThrow("Insest"));}
        public String getInsmun(){ return getString(getColumnIndexOrThrow("Insmun")); }
        public String getCodcrt() {return getString(getColumnIndexOrThrow("Codcrt"));}
        public String getEndere(){ return getString(getColumnIndexOrThrow("Endere"));}
        public String getNumero() { return getString(getColumnIndexOrThrow("Numero"));}
        public String getBairro(){return getString(getColumnIndexOrThrow("Bairro"));}
        public String getCidade(){return getString(getColumnIndexOrThrow("Cidade"));}
        public String getUF(){ return getString(getColumnIndexOrThrow("UF"));}
        public String getCodmun(){ return getString(getColumnIndexOrThrow("Codmun")); }
        public String getCEP(){ return getString(getColumnIndexOrThrow("CEP"));}
        public String getFone(){return getString(getColumnIndexOrThrow("Fone"));}
        public String getEmail(){ return getString(getColumnIndexOrThrow("Email"));}
        public String getCodtar(){return getString(getColumnIndexOrThrow("CodTar"));}
        public String getTipamb(){return getString(getColumnIndexOrThrow("Tipamb"));}
        public String getModelo(){return getString(getColumnIndexOrThrow("Modelo"));}
        public String getSerie(){return getString(getColumnIndexOrThrow("Serie"));}
        public String getUltbil(){return getString(getColumnIndexOrThrow("Ultbil"));}
        public String getTipemi(){return getString(getColumnIndexOrThrow("Tipemi"));}
        public String getTipbil(){return getString(getColumnIndexOrThrow("Tipbil"));}
        public String getDatctg(){return getString(getColumnIndexOrThrow("Datctg"));}
        public String getCnae() {return getString(getColumnIndexOrThrow("Cnae"));}
        public String getEmpspl() {return getString(getColumnIndexOrThrow("Empspl"));}
        public String getAliicm() {return getString(getColumnIndexOrThrow("Aliicm"));}
        public String getUrlqrc() {return getString(getColumnIndexOrThrow("Urlqrc"));}
        public String getCrtemp() {return getString(getColumnIndexOrThrow("Crtemp"));}
        public String getCrtsen() {return getString(getColumnIndexOrThrow("Crtsen"));}
        public String getAlitri() {return getString(getColumnIndexOrThrow("Alitri"));}
        public String getMaximp(){ return getString(getColumnIndexOrThrow("Maximp"));}
        public String getConvei(){ return getString(getColumnIndexOrThrow("Convei"));}
        public String getPvenda(){ return getString(getColumnIndexOrThrow("Pvenda"));}
        public String getEndews(){ return getString(getColumnIndexOrThrow("Endews"));}
        public String getRsv001(){ return getString(getColumnIndexOrThrow("Rsv001"));}
        public String getRsv002(){ return getString(getColumnIndexOrThrow("Rsv002"));}
        public String getRsv003(){ return getString(getColumnIndexOrThrow("Rsv003"));}
        public String getModimp(){ return getString(getColumnIndexOrThrow("Modimp"));}
        public String getUltdat(){ return getString(getColumnIndexOrThrow("Ultdat"));}
        public String getCartao(){ return getString(getColumnIndexOrThrow("Cartao"));}
        public String getBasseg(){ return getString(getColumnIndexOrThrow("Basseg"));}
        public String getNaokey(){ return getString(getColumnIndexOrThrow("Naokey"));}
        public String getMincan(){ return getString(getColumnIndexOrThrow("Mincan"));}
        public String getUltqrc(){ return getString(getColumnIndexOrThrow("Ultqrc"));}
        public String getNdstax(){ return getString(getColumnIndexOrThrow("Ndstax"));}


    }

    String BuscaEmp(int parametro){
        String selectQuery =
                "SELECT * FROM EMP WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Cnpj"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    public String Busca_Dados_Emp(int parametro, String scampo){
        String selectQuery =
                "SELECT * FROM EMP WHERE ID =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }



}
