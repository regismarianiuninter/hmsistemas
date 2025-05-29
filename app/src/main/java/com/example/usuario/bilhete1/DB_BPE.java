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

public class DB_BPE extends SQLiteOpenHelper {
    /** O nome do arquivo de base de dados no sistema de arquivos */
    private static final String NOME_BD = "BPE";
    /** A versão da base de dados que esta classe compreende. */
    private static final int VERSAO_BD = 5;
    private static final String LOG_TAG = "BPE";
    /** Mantém rastreamento do contexto que nós podemos carregar SQL */
    private final Context contexto;


    public DB_BPE(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String[] sql = contexto.getString(R.string.DB_BPE_onCreate).split("\n");
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
                db.execSQL("ALTER TABLE BPE ADD COLUMN Codban TEXT DEFAULT ''");
                db.execSQL("ALTER TABLE BPE ADD COLUMN Codaut TEXT DEFAULT ''");
            }
            if (oldVersion < 5) {
                db.execSQL("ALTER TABLE BPE ADD COLUMN Nidpag TEXT DEFAULT ''");
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

    /*Retorna um BPE ordenado
      @param critério de ordenação
     */
    public DB_BPE.BpeCursor RetornarBpe(DB_BPE.BpeCursor.OrdenarPor ordenarPor)
    {
        String sql = DB_BPE.BpeCursor.CONSULTA + (ordenarPor == DB_BPE.BpeCursor.OrdenarPor.NomeCrescente ? "ASC" : "DESC");
        SQLiteDatabase bd = getReadableDatabase();
        DB_BPE.BpeCursor cc = (DB_BPE.BpeCursor) bd.rawQueryWithFactory(new DB_BPE.BpeCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }

    public long InserirBpe(String modser, String numbpe, String datemi, String chvbpe, String sitbpe, String agente, String tippas,
                           Double vlrpas, String nomvia, String treori, String tredes, String qtdimp, String transf,
                           String rsv001, String rsv002, String rsv003, String codlin, String codori, String coddes,
                           String datsai, String numcad, String vlrtar, String vlremb, String vlrseg, String vlrarr,
                           String codvei, String pagmto, String scodban, String scodaut,String nidpag)



    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("Modser", modser);
            initialValues.put("Numbpe", numbpe);
            initialValues.put("Datemi", datemi);
            initialValues.put("Chvbpe", chvbpe);
            initialValues.put("Sitbpe", sitbpe);
            initialValues.put("Agente", agente);
            initialValues.put("Tippas", tippas);
            initialValues.put("Vlrpas", vlrpas);
            initialValues.put("Nomvia", nomvia);
            initialValues.put("Treori", treori);
            initialValues.put("Tredes", tredes);
            initialValues.put("Qtdimp", qtdimp);
            initialValues.put("Transf", transf);
            initialValues.put("Rsv001", rsv001);
            initialValues.put("Rsv002", rsv002);
            initialValues.put("Rsv003", rsv003);
            initialValues.put("Codlin", codlin);
            initialValues.put("Codori", codori);
            initialValues.put("Coddes", coddes);
            initialValues.put("Datsai", datsai);
            initialValues.put("Numcad", numcad);
            initialValues.put("Vlrtar", vlrtar);
            initialValues.put("Vlremb", vlremb);
            initialValues.put("Vlrseg", vlrseg);
            initialValues.put("Vlrarr", vlrarr);
            initialValues.put("Codvei", codvei);
            initialValues.put("Pagmto", pagmto);
            initialValues.put("Codban", scodban);
            initialValues.put("Codaut", scodaut);
            initialValues.put("Nidpag", nidpag);


            return db.insert("BPE", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public void deletar_Bpe(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NOME_BD, null, null);
        db.close();
    }

    public void delete_item_Bpe(String sID) {
        SQLiteDatabase dbsqlite = getWritableDatabase();
        dbsqlite.delete(NOME_BD, "id="+sID, null);
    }

    public void Atualizar_Bpe(String id, String modser, String numbpe, String datemi, String chvbpe, String sitbpe, String agente, String tippas,
                              Double vlrpas, String nomvia, String treori, String tredes, String qtdimp, String transf,
                              String rsv001, String rsv002, String rsv003, String codlin, String codori, String coddes,
                              String datsai, String numcad, String vlrtar, String vlremb, String vlrseg, String vlrarr,
                              String codvei, String pagmto, String scodban, String scodaut, String nidpag) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put("Modser", modser);
        valores.put("Numbpe", numbpe);
        valores.put("Datemi", datemi);
        valores.put("Chvbpe", chvbpe);
        valores.put("Sitbpe", sitbpe);
        valores.put("Agente", agente);
        valores.put("Tippas", tippas);
        valores.put("Vlrpas", vlrpas);
        valores.put("Nomvia", nomvia);
        valores.put("Treori", treori);
        valores.put("Tredes", tredes);
        valores.put("Qtdimp", qtdimp);
        valores.put("Transf", transf);
        valores.put("Rsv001", rsv001);
        valores.put("Rsv002", rsv002);
        valores.put("Rsv003", rsv003);
        valores.put("Codlin", codlin);
        valores.put("Codori", codori);
        valores.put("Coddes", coddes);
        valores.put("Datsai", datsai);
        valores.put("Numcad", numcad);
        valores.put("Vlrtar", vlrtar);
        valores.put("Vlremb", vlremb);
        valores.put("Vlrseg", vlrseg);
        valores.put("Vlrarr", vlrarr);
        valores.put("Codvei", codvei);
        valores.put("Codban", scodban);
        valores.put("Codaut", scodaut);
        valores.put("Nidpag", nidpag);



        getWritableDatabase().update("BPE", valores, "id=?", argumentos);
    }

    public void Atualizar_Campo_Bpe(String id, String campo, String novoval){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues valores = new ContentValues();
        String[] argumentos = {id};

        valores.put(campo, novoval);

        getWritableDatabase().update("BPE", valores, "id=?", argumentos);

    }

    public static class BpeCursor extends SQLiteCursor
    {


        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM BPE ORDER BY ID ";

        private BpeCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new DB_BPE.BpeCursor(db, driver, editTable, query);
            }
        }

        public long getID()
        {
            return getLong(getColumnIndexOrThrow("ID"));
        }
        public String getModser()
        {
            return getString(getColumnIndexOrThrow("Modser"));
        }
        public String getNumbpe() { return getString(getColumnIndexOrThrow("Numbpe")); }
        public String getDatemi(){return getString(getColumnIndexOrThrow("Datemi"));}
        public String getChvbpe(){ return getString(getColumnIndexOrThrow("Chvbpe")); }
        public String getSitbpe() {return getString(getColumnIndexOrThrow("Sitbpe"));}
        public String getAgente(){ return getString(getColumnIndexOrThrow("Agente"));}
        public String getTippas(){ return getString(getColumnIndexOrThrow("Tippas"));}
        public String getVlrpas(){ return getString(getColumnIndexOrThrow("Vlrpas"));}
        public String getNomvia(){ return getString(getColumnIndexOrThrow("Nomvia"));}
        public String getTreori(){ return getString(getColumnIndexOrThrow("Treori"));}
        public String getTredes(){ return getString(getColumnIndexOrThrow("Tredes"));}
        public String getQtdimp(){ return getString(getColumnIndexOrThrow("Qtdimp"));}
        public String getTransf(){ return getString(getColumnIndexOrThrow("Transf"));}
        public String getRsv001(){ return getString(getColumnIndexOrThrow("Rsv001"));}
        public String getRsv002(){ return getString(getColumnIndexOrThrow("Rsv002"));}
        public String getRsv003(){ return getString(getColumnIndexOrThrow("Rsv003"));}
        public String getCodlin(){ return getString(getColumnIndexOrThrow("Codlin"));}
        public String getCodori(){ return getString(getColumnIndexOrThrow("Codori"));}
        public String getCoddes(){ return getString(getColumnIndexOrThrow("Coddes"));}
        public String getDatsai(){ return getString(getColumnIndexOrThrow("Datsai"));}
        public String getNumcad(){ return getString(getColumnIndexOrThrow("Numcad"));}
        public String getVlrtar(){ return getString(getColumnIndexOrThrow("Vlrtar"));}
        public String getVlremb(){ return getString(getColumnIndexOrThrow("Vlremb"));}
        public String getVlrseg(){ return getString(getColumnIndexOrThrow("Vlrseg"));}
        public String getVlrarr(){ return getString(getColumnIndexOrThrow("Vlrarr"));}
        public String getCodvei(){ return getString(getColumnIndexOrThrow("Codvei"));}
        public String getPagmto(){ return getString(getColumnIndexOrThrow("Pagmto"));}
        public String getCodban(){ return getString(getColumnIndexOrThrow("Codban"));}
        public String getCodaut(){ return getString(getColumnIndexOrThrow("Codaut"));}
        public String getNidpag(){ return getString(getColumnIndexOrThrow("Nidpag"));}



    }


    public List<BilhetesModel> VerificaBil(String sNum) {
        List<BilhetesModel> listaBilhetes = new ArrayList<BilhetesModel>();

        if(!sNum.isEmpty()) {
            SQLiteDatabase dbase = getReadableDatabase();
            Cursor cursor = dbase.rawQuery("SELECT * FROM " + "BPE" + " WHERE Numbpe = " + sNum, null);

            if(cursor.moveToFirst()) {
                do {
                    BilhetesModel element = new BilhetesModel();
                    element.setID(cursor.getString(0));
                    element.setNumbpe(cursor.getString(2));
                    element.setSitbpe(cursor.getString(5));
                    listaBilhetes.add(element);
                } while(cursor.moveToNext());
            }
        }

        return listaBilhetes;
    }


    String BuscaBpe(String parametro){
        String selectQuery =
                "SELECT * FROM BPE WHERE Numbpe =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String nomeString = cursor.getString(cursor.getColumnIndex("Chvbpe"));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }

    String Busca_Dados_Bpe(String parametro, String scampo){
        String selectQuery =
                "SELECT * FROM BPE WHERE Numbpe =" + parametro;


        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String nomeString = cursor.getString(cursor.getColumnIndex(scampo));

        StringBuilder conversor = new StringBuilder();
        conversor.append(nomeString);
        return conversor.toString();

    }



    public List<BilhetesModel> SelecionarTodos(){

        List<BilhetesModel> bilhetes = new ArrayList<BilhetesModel>();


        //MONTA A QUERY A SER EXECUTADA
        StringBuilder stringBuilderQuery = new StringBuilder();
        stringBuilderQuery.append(" SELECT ID,      ");
        stringBuilderQuery.append("        Modser,        ");
        stringBuilderQuery.append("        Numbpe,    ");
        stringBuilderQuery.append("        Datemi,        ");
        stringBuilderQuery.append("        Chvbpe,  ");
        stringBuilderQuery.append("        Sitbpe, ");
        stringBuilderQuery.append("        Agente,        ");
        stringBuilderQuery.append("        Tippas,        ");
        stringBuilderQuery.append("        Vlrpas,        ");
        stringBuilderQuery.append("        Nomvia,        ");
        stringBuilderQuery.append("        Treori,        ");
        stringBuilderQuery.append("        Tredes,        ");
        stringBuilderQuery.append("        Qtdimp,        ");
        stringBuilderQuery.append("        Transf,        ");
        stringBuilderQuery.append("        Rsv001,        ");
        stringBuilderQuery.append("        Rsv002,        ");
        stringBuilderQuery.append("        Rsv003,        ");
        stringBuilderQuery.append("        Codlin,        ");
        stringBuilderQuery.append("        Codori,        ");
        stringBuilderQuery.append("        Coddes,        ");
        stringBuilderQuery.append("        Datsai,        ");
        stringBuilderQuery.append("        Numcad,        ");
        stringBuilderQuery.append("        Vlrtar,        ");
        stringBuilderQuery.append("        Vlremb,        ");
        stringBuilderQuery.append("        Vlrseg,        ");
        stringBuilderQuery.append("        Vlrarr,        ");
        stringBuilderQuery.append("        Codvei,        ");
        stringBuilderQuery.append("        Pagmto,        ");
        stringBuilderQuery.append("        Codban,        ");
        stringBuilderQuery.append("        Codaut,        ");
        stringBuilderQuery.append("        Nidpag         ");
        stringBuilderQuery.append("  FROM  BPE       ");
        stringBuilderQuery.append(" ORDER BY ID  DESC    "); //Para Ordenar em ordem decrescente


        SQLiteDatabase banco = this.getWritableDatabase();

        //CONSULTANDO OS REGISTROS CADASTRADOS
        Cursor cursor = banco.rawQuery(stringBuilderQuery.toString(), null);

        /*POSICIONA O CURSOR NO PRIMEIRO REGISTRO*/
        cursor.moveToFirst();


        BilhetesModel bilhetesmodel;

        //REALIZA A LEITURA DOS REGISTROS ENQUANTO NÃO FOR O FIM DO CURSOR
        while (!cursor.isAfterLast()){

            /* CRIANDO UM NOVO BILHETE */
            bilhetesmodel =  new BilhetesModel();

            //ADICIONANDO OS DADOS DO BILHETE
            bilhetesmodel.setID(cursor.getString(cursor.getColumnIndex("ID")));
            bilhetesmodel.setModser(cursor.getString(cursor.getColumnIndex("Modser")));
            bilhetesmodel.setNumbpe(cursor.getString(cursor.getColumnIndex("Numbpe")));
            bilhetesmodel.setDatemi(cursor.getString(cursor.getColumnIndex("Datemi")));
            bilhetesmodel.setChvbpe(cursor.getString(cursor.getColumnIndex("Chvbpe")));
            bilhetesmodel.setSitbpe(cursor.getString(cursor.getColumnIndex("Sitbpe")));
            bilhetesmodel.setAgente(cursor.getString(cursor.getColumnIndex("Agente")));
            bilhetesmodel.setTiopas(cursor.getString(cursor.getColumnIndex("Tippas")));
            bilhetesmodel.setVlrpas(cursor.getString(cursor.getColumnIndex("Vlrpas")));
            bilhetesmodel.setNomvia(cursor.getString(cursor.getColumnIndex("Nomvia")));
            bilhetesmodel.setTreori(cursor.getString(cursor.getColumnIndex("Treori")));
            bilhetesmodel.setTredes(cursor.getString(cursor.getColumnIndex("Tredes")));
            bilhetesmodel.setQtdimp(cursor.getString(cursor.getColumnIndex("Qtdimp")));
            bilhetesmodel.setTransf(cursor.getString(cursor.getColumnIndex("Transf")));
            bilhetesmodel.setRsv001(cursor.getString(cursor.getColumnIndex("Rsv001")));
            bilhetesmodel.setRsv002(cursor.getString(cursor.getColumnIndex("Rsv002")));
            bilhetesmodel.setRsv003(cursor.getString(cursor.getColumnIndex("Rsv003")));
            bilhetesmodel.setCodlin(cursor.getString(cursor.getColumnIndex("Codlin")));
            bilhetesmodel.setCodori(cursor.getString(cursor.getColumnIndex("Codori")));
            bilhetesmodel.setCoddes(cursor.getString(cursor.getColumnIndex("Coddes")));
            bilhetesmodel.setDatsai(cursor.getString(cursor.getColumnIndex("Datsai")));
            bilhetesmodel.setNumcad(cursor.getString(cursor.getColumnIndex("Numcad")));
            bilhetesmodel.setVlrtar(cursor.getString(cursor.getColumnIndex("Vlrtar")));
            bilhetesmodel.setVlremb(cursor.getString(cursor.getColumnIndex("Vlremb")));
            bilhetesmodel.setVlrseg(cursor.getString(cursor.getColumnIndex("Vlrseg")));
            bilhetesmodel.setVlrarr(cursor.getString(cursor.getColumnIndex("Vlrarr")));
            bilhetesmodel.setCodvei(cursor.getString(cursor.getColumnIndex("Codvei")));
            bilhetesmodel.setPagmto(cursor.getString(cursor.getColumnIndex("Pagmto")));
            bilhetesmodel.setCodban(cursor.getString(cursor.getColumnIndex("Codban")));
            bilhetesmodel.setCodaut(cursor.getString(cursor.getColumnIndex("Codaut")));
            bilhetesmodel.setNidpag(cursor.getString(cursor.getColumnIndex("Nidpag")));


            //ADICIONANDO UM BILHETE NA LISTA
            bilhetes.add(bilhetesmodel);

            //VAI PARA O PRÓXIMO REGISTRO
            cursor.moveToNext();
        }

        //RETORNANDO A LISTA DE PESSOAS
        return bilhetes;

    }



}