package com.example.usuario.bilhete1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.os.Environment.getExternalStorageDirectory;

import androidx.appcompat.app.AppCompatActivity;


public class TreActivity extends AppCompatActivity {
    ArrayAdapter<String> listAdapter;
    ArrayList<String> result;
    ListView lista;
    List<String> opcoes;
    ArrayAdapter<String> adaptador;
    SQLiteDatabase dbase;
    private static String Nome_user = "";
    private static String Linha_Trab = "";
    public static  String Activity_Dados = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trechos_lista);
        lista = (ListView)findViewById(R.id.listaTre);
        Button btnConf = findViewById(R.id.btnConf);
        final EditText EdtSel = findViewById(R.id.edtSel);
        EdtSel.setEnabled(false);
        Intent Newintent = getIntent();

        Bundle bundle = Newintent.getExtras();

        String txt = bundle.getString("txt");

        String svia = bundle.getString("viagem");
        String user = bundle.getString("USUARIO");
        String slintra = bundle.getString("LINHA");
        String scodigo = bundle.getString("Activity_Dados");
        Nome_user = user;
        Linha_Trab = slintra;
        Activity_Dados = scodigo;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        if (txt.equals("TRECHO")) {
            if (svia.equals("000")) {
                Listar_Todos_Trechos();//mostrar todos os trechos
            } else {
                Listar_Trechos_DB(svia);//somente os trechos da linha selecionada
            }
        } else if (txt.equals("VIAGEM")) {
            Listar_Viagens_DB();
            //Listar_Viagens("");
        } else if (txt.equals("PERCURSO")){
            Listar_Percursos_DB();
        } else if (txt.equals("EMP")){
            Listar_EMP_DB();
        } else if (txt.equals("USUARIO")){
            Listar_USR_DB();
        } else if (txt.equals("LINHAS")) {
            Listar_LIN_DB("");
        } else if (txt.equals("LINHASC")){
            Listar_LIN_DB("C");
        } else if (txt.equals("LINHASWS")){
            Listar_LinhasWS();
        } else if (txt.equals("XMLWS")){
            Listar_XMLWS();
        }

        btnConf.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // em ambos casos criar um Intent e adiciona um string nele (putExtra) e seta o Result
                String sTrecho = EdtSel.getText().toString();
                if (sTrecho != null || sTrecho != "") {

                    Intent i = getIntent();

                    i.putExtra("msg", sTrecho);
                    i.putExtra("Activity_Dados", Activity_Dados);
                    setResult(RESULT_OK,i);
                    finish();
                    //setResult(1, i);


                } else {
                    Intent i = new Intent();

                    i.putExtra("msg", "Nao Escolheu o Trecho");

                    //setResult(2, i);
                    setResult(2,i);
                    finish();
                }



                // importante para voltar a primeira Activity pai

                finish();


            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int posicao, long id) {

                // TODO Auto-generated method stub
                Object listItem = lista.getItemAtPosition(posicao);
                /*Toast.makeText(TreActivity.this,
                        "Posição Selecionada:" + listItem, Toast.LENGTH_LONG)
                        .show();*/
                EditText EdtSel = findViewById(R.id.edtSel);
                EdtSel.setText(listItem.toString());
            }
        });

        final Button btnNovo = findViewById(R.id.btnNovo);
        if (!Nome_user.equals("HMINFO")){
            btnNovo.setEnabled(false);
        }
        btnNovo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent Newintent = getIntent();

                Bundle bundle = Newintent.getExtras();

                String txt = bundle.getString("txt");


                if (txt.equals("VIAGEM")) {
                    Listar_Viagens("S");
                    //Atualizar Viagens
                } else if (txt.equals("PERCURSO")){
                    //Atualizar Percursos
                    Listar_Percursos("S");
                } else if (txt.equals("TRECHO")){
                    //Atualizar Trechos
                    Listar_TrechosG("S");
                } else if (txt.equals("LINHAS")){
                    //Atualizar Linhas
                    Listar_Linhas("S");
                } else if (txt.equals("USUARIO")){
                    //Cadastrar Usuarios
                    Listar_Usuarios("S");
                }



            }
        });

        final Button btnUpgrade = findViewById(R.id.btnUpgrade);
        if (!Nome_user.equals("HMINFO")){
            btnUpgrade.setEnabled(false);
        }
        btnUpgrade.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent Newintent = getIntent();

                Bundle bundle = Newintent.getExtras();

                String txt = bundle.getString("txt");

                Upgrade_DB(txt);


            }
        });



    }





    public void Listar_Viagens(String sGrava) {

        opcoes = new ArrayList<String>();
        DB_VIA dbv = new DB_VIA(this);
        dbv.deletar_Via();

        ListView listaTrechos = findViewById(R.id.listaTre);
        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/VIAGENS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                // String[] linhaDoArquivo = bufferedReader.readLine().split(";");
                String CodLin = linhaDoArquivo[0];
                String DesVia = linhaDoArquivo[1];
                String HorVia = linhaDoArquivo[2];
                String Tipvia = linhaDoArquivo[3];
                String Tipser = linhaDoArquivo[4];
                String Prefix = linhaDoArquivo[5];
               // String SViagem = (CodLin.toString()+"-"+DesVia.toString()+"-"+ HorVia.toString());
               // opcoes.add(SViagem.toString());
                if (sGrava.equals("S")){

                    DB_VIA dbNew = new DB_VIA(this);
                    dbNew.InserirViagem(CodLin, DesVia, HorVia, Tipvia, Tipser, Prefix);

                }
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaTrechos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaTrechos.setAdapter(adaptador);*/

       Listar_Viagens_DB();
    }

    public void Listar_Percursos(String sGrava) {

        opcoes = new ArrayList<String>();
        DB_PER db = new DB_PER(this);
        db.deletar_Per();

        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath());
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/PERCURSOS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                String Codlin  = linhaDoArquivo[0];
                String Origem  = linhaDoArquivo[1];
                String Destino = linhaDoArquivo[2];
                String Tarifa  = linhaDoArquivo[3];
                String Seguro  = linhaDoArquivo[4];
                String Arredonda = linhaDoArquivo[5];
                String Tipvia    = linhaDoArquivo[6];
                if (sGrava.equals("S")){

                    DB_PER dbNew = new DB_PER(this);
                    dbNew.InserirPercurso(Codlin, Origem, Destino, Tarifa, Seguro, Arredonda, Tipvia);

                }
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       Listar_Percursos_DB();
    }


    public void Listar_Linhas(String sGrava) {

        opcoes = new ArrayList<String>();
        DB_LIN db = new DB_LIN(this);
        db.deletar_Lin();

        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/LINHAS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                String Codlin  = linhaDoArquivo[0];
                String Descri  = linhaDoArquivo[1];
                String Prefix  = linhaDoArquivo[2];
                if (sGrava.equals("S")){

                    DB_LIN dbNew = new DB_LIN(this);
                    dbNew.InserirLin(Codlin, Descri, Prefix);

                }
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       Listar_LIN_DB("");
    }


    public void Listar_Usuarios(String sGrava) {

        opcoes = new ArrayList<String>();
        DB_USR db = new DB_USR(this);

        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/USUARIOS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                String Nome   = linhaDoArquivo[0];
                String Senha  = linhaDoArquivo[1];
                String Fectur = linhaDoArquivo[2];
                if (sGrava.equals("S")){

                    DB_USR dbNew = new DB_USR(this);
                    dbNew.InserirUsr(Nome, Senha, Fectur, "");

                }
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

      Listar_USR_DB();
    }



    public void Listar_TrechosG(String sGrava) {
        DB_TRE dbt = new DB_TRE(this);
        dbt.deletar_Tre();


        opcoes = new ArrayList<String>();

        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/TRECHOS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                String Codigo  = linhaDoArquivo[0];
                String Descri  = linhaDoArquivo[1];
                String UF      = linhaDoArquivo[2];
                String Codmun  = linhaDoArquivo[3];
                if (sGrava.equals("S")){

                    DB_TRE db = new DB_TRE(this);
                    db.InserirTrecho(Codigo, Descri, UF, Codmun);

                }
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

      Listar_Todos_Trechos();
    }


    private void Listar_Viagens_DB(){
        opcoes = new ArrayList<String>();
        String slinha = Linha_Trab;
        //Toast.makeText(TreActivity.this, "Linha Selecionada: " + slinha, Toast.LENGTH_LONG).show();

        ListView listaTrechos = findViewById(R.id.listaTre);
        DB_VIA db = new DB_VIA(this);
        DB_VIA.ViagensCursor cursor = db.RetornarViagens(DB_VIA.ViagensCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            if (!slinha.equals("")) { //filtro por linha
                String slinvia = cursor.getLinha();
                if (slinvia.equals(slinha)) { //linha da viagem mesma que o filtro
                    String SViagem = (cursor.getID() + "-" + cursor.getViagem().toString() + "-" + cursor.getHora().toString());
                    opcoes.add(SViagem.toString());
                }
            } else if (slinha.equals("")){
                String SViagem = (cursor.getID() + "-" + cursor.getViagem().toString() + "-" + cursor.getHora().toString());
                opcoes.add(SViagem.toString());
            }

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaTrechos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaTrechos.setAdapter(adaptador);
    }

    private void Listar_Percursos_DB(){
        opcoes = new ArrayList<String>();


        ListView listaPercursos = findViewById(R.id.listaTre);
        DB_PER db = new DB_PER(this);
        DB_PER.PercursosCursor cursor = db.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            String sPercurso = (cursor.getLinha()+"-"+cursor.getOrigem().toString()+" X "+ cursor.getDestino().toString());
            opcoes.add(sPercurso.toString());

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaPercursos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaPercursos.setAdapter(adaptador);
    }


    private void Listar_Trechos_DB(String scodvia){
        //Toast.makeText(this, "Linha: " + svia, Toast.LENGTH_LONG).show();
        int iVia = Integer.parseInt(scodvia);
        DB_VIA dbv = new DB_VIA(this);
        String Slinha = dbv.BuscaVia(iVia);

        opcoes = new ArrayList<String>();
        ListView listaPercursos = findViewById(R.id.listaTre);




        DB_TRE db = new DB_TRE(this);
        DB_TRE.TrechosCursor cursor = db.RetornarTrechos(DB_TRE.TrechosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);

            String svai = "";
            DB_PER dbp = new DB_PER(this);
            DB_PER.PercursosCursor cursorper = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);

            for( int ip=0; ip <cursorper.getCount(); ip++)
            {
                cursorper.moveToPosition(ip);
                if (cursorper.getLinha().equals(Slinha)){//mesma linha
                    if (cursorper.getOrigem().equals(cursor.getCodigo())){
                        svai = "S";
                    }
                }
            }


            if (svai == "S") {
                String sTrecho = (cursor.getID() + "-" + cursor.getDescri().toString());
                opcoes.add(sTrecho.toString());
            }

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaPercursos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaPercursos.setAdapter(adaptador);
    }

    private void Listar_Todos_Trechos(){

        opcoes = new ArrayList<String>();
        ListView listaPercursos = findViewById(R.id.listaTre);




        DB_TRE db = new DB_TRE(this);
        DB_TRE.TrechosCursor cursor = db.RetornarTrechos(DB_TRE.TrechosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);

                String sTrecho = (cursor.getID() + "-" + cursor.getDescri().toString());
                opcoes.add(sTrecho);

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaPercursos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaPercursos.setAdapter(adaptador);
    }


    private void Listar_EMP_DB(){
        opcoes = new ArrayList<String>();


        ListView listaEmpresa = findViewById(R.id.listaTre);
        DB_EMP db = new DB_EMP(this);
        DB_EMP.EmpCursor cursor = db.RetornarEmp(DB_EMP.EmpCursor.OrdenarPor.NomeCrescente);
        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            String sEmp = (cursor.getID()+"-"+cursor.getDescri().toString());
            opcoes.add(sEmp.toString());

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaEmpresa.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaEmpresa.setAdapter(adaptador);
    }


    private void Listar_USR_DB(){
        opcoes = new ArrayList<String>();


        ListView listaUsuarios = findViewById(R.id.listaTre);
        DB_USR db = new DB_USR(this);
        DB_USR.UsrCursor cursor = db.RetornarUsr(DB_USR.UsrCursor.OrdenarPor.NomeCrescente);
        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            String sUsr = (cursor.getID()+"-"+cursor.getUsrnom());
            opcoes.add(sUsr);

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaUsuarios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaUsuarios.setAdapter(adaptador);
    }


    private void Listar_LIN_DB(String sview){
        opcoes = new ArrayList<String>();


        ListView listaUsuarios = findViewById(R.id.listaTre);
        DB_LIN db = new DB_LIN(this);
        DB_LIN.LinCursor cursor = db.RetornarLin(DB_LIN.LinCursor.OrdenarPor.NomeCrescente);
        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            String sLinha = "";
            if (sview.equals("C")) {
                sLinha = (cursor.getCodigo()+"-"+cursor.getDescri());
            } else {
                sLinha = (cursor.getID()+"-"+cursor.getCodigo()+"-"+cursor.getDescri());
            }
            opcoes.add(sLinha);

        }
        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaUsuarios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaUsuarios.setAdapter(adaptador);
    }


    private void Listar_LinhasWS(){
        opcoes = new ArrayList<String>();


        ListView listaLinhas = findViewById(R.id.listaTre);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWS.xml" );
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeResponse = doc.getElementsByTagName("lista_linhasResponse");
            for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                Node nNode = nodeResponse.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    NodeList nodeResult = doc.getElementsByTagName("lista_linhasResult");
                    for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {


                        Node nresult = nodeResult.item(tempresult);
                        Element resultElement = (Element) nresult;
                        //////////////////////////////////////////////
                        NodeList nodeTipo = doc.getElementsByTagName("tSEP_LIN");
                        for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                            Node ntipo = nodeTipo.item(temptipo);
                            Element tipoElement = (Element) ntipo;
                            String sNome = tipoElement.getElementsByTagName("sDescri").item(0).getTextContent();
                            opcoes.add(sNome);

                        }
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }




        adaptador = new ArrayAdapter<String>(TreActivity.this, android.R.layout.simple_list_item_single_choice, opcoes);
        listaLinhas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaLinhas.setAdapter(adaptador);
    }


    private void Listar_XMLWS(){
        opcoes = new ArrayList<String>();


        ListView listaLinhas = findViewById(R.id.listaTre);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWS.xml" );
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeResponse = doc.getElementsByTagName("xml_bilheteResponse");
            for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                Node nNode = nodeResponse.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    NodeList nodeResult = doc.getElementsByTagName("xml_bilheteResult");
                    for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {


                        Node nresult = nodeResult.item(tempresult);
                        Element resultElement = (Element) nresult;
                        //////////////////////////////////////////////
                        NodeList nodeTipo = doc.getElementsByTagName("tRET_WBS");
                        for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                            Node ntipo = nodeTipo.item(temptipo);
                            Element tipoElement = (Element) ntipo;
                            String sarquivo = tipoElement.getElementsByTagName("sXml").item(0).getTextContent();
                            try {

                                File sdCard = getExternalFilesDir("Download");
                                File dir = new File(sdCard.getAbsolutePath() );
                                //dir.mkdirs();
                                File fileExt = new File(dir, "RetXMLWS.xml");

                                //Cria o arquivo
                                fileExt.getParentFile().mkdirs();

                                //Abre o arquivo
                                FileOutputStream fosExt = null;
                                fosExt = new FileOutputStream(fileExt);

                                //Escreve no arquivo
                                fosExt.write(sarquivo.getBytes());

                                //Obrigatoriamente você precisa fechar
                                fosExt.close();
                            } catch (Exception e) {
                                System.out.println("Ex Salvando: " + e.toString());
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }


    public void Upgrade_DB(String txt){
        /*if (txt.equals("VIAGEM")) {
            //Atualizar Viagens
            DB_VIA dbvia = new DB_VIA(this);
            SQLiteDatabase db = dbvia.getWritableDatabase();
            dbvia.onUpgrade(db,3, 4);
        } else if (txt.equals("PERCURSO")){
            //Atualizar Percursos

        } else if (txt.equals("TRECHO")){
            //Atualizar Trechos

        }*/
        if (txt.equals("USUARIO")) {
           DB_USR dbusr = new DB_USR(TreActivity.this);
           SQLiteDatabase db = dbusr.getWritableDatabase();
           dbusr.onUpgrade(db, 1, 2);
        }
    }


}
