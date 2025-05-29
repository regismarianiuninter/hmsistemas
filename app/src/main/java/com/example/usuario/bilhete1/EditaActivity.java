package com.example.usuario.bilhete1;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class EditaActivity extends AppCompatActivity {

    private static int Activity_Dados = 1;
    private static String ID_Old = "";
    private static String Nome_user = "";


    ActivityResultLauncher<Intent> startForresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    EditText edtret = findViewById(R.id.edtRet);
                   // String sret = result.getData().getStringExtra(TreActivity.Activity_Dados);
                    Intent data = result.getData();
                    String sret = data.getStringExtra("msg");
                    String sorigemresult = data.getStringExtra("Activity_Dados");
                    if (sorigemresult.equals("2")){
                        edtret.setText(sret);
                    }
                }
            }


        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita);

        Intent Newintent = getIntent();
        Bundle bundle = Newintent.getExtras();
        String user = bundle.getString("USUARIO");
        Nome_user = user;


        Button btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                EditText edtid = findViewById(R.id.edt01);
                EditText edtDescri = findViewById(R.id.edt02);
                EditText edthora = findViewById(R.id.edt03);
                EditText edttipvia = findViewById(R.id.edt04);
                EditText edttipser = findViewById(R.id.edt05);
                EditText edtprefix = findViewById(R.id.edt06);

                edtid.setText("");
                edtDescri.setText("");
                edthora.setText("");
                edttipvia.setText("");
                edttipser.setText("");
                edtprefix.setText("");


                Activity_Dados = 2;
                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                Intent myIntent = new Intent(EditaActivity.this, TreActivity.class);
                EditText edtbase = findViewById(R.id.edtBase);

                String txt = edtbase.getText().toString();
                Bundle bundle = new Bundle();

                bundle.putString("txt", txt);
                bundle.putString("viagem", "000");
                bundle.putString("USUARIO", Nome_user);
                bundle.putString("LINHA", "");
                bundle.putString("Activity_Dados", "2");
                myIntent.putExtras(bundle);
                startForresult.launch(myIntent);


            }
        });



        Button btnExibir = findViewById(R.id.btnExibir);
        btnExibir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtbase = findViewById(R.id.edtBase);
                String txt = edtbase.getText().toString();
                if (txt.equals("TRECHO")) {
                    Monta_Tela_Tre();
                } else if (txt.equals("VIAGEM")){
                    Monta_Tela_Via();
                } else if (txt.equals("USUARIO")){
                    Monta_Tela_Usr();
                } else if (txt.equals("LINHAS")){
                    Monta_Tela_Lin();
                }
            }
        });


        Button btnAlterar = findViewById(R.id.btnAlterar);
        btnAlterar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtbase = findViewById(R.id.edtBase);
                String txt = edtbase.getText().toString();
                if (txt.equals("TRECHO")) {
                    Altera_Cadastro_Tre();
                } else if (txt.equals("VIAGEM")){
                    Altera_Cadastro_Via();
                } else if (txt.equals("USUARIO")){
                    Altera_Cadastro_Usr();
                } else if (txt.equals("LINHAS")){
                    Altera_Cadastro_Lin();
                }

            }
        });


        Button btnCriar = findViewById(R.id.btnCriar);
        btnCriar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtbase = findViewById(R.id.edtBase);
                String txt = edtbase.getText().toString();
                if (txt.equals("USUARIO")){
                    Cria_Cadastro_Usr();
                }

            }
        });

        Button btnCon = findViewById(R.id.btnConWs);
        btnCon.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Activity_Dados = 3;
                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                Intent myIntent = new Intent(EditaActivity.this, WSActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("CHV", "TESTE");
                bundle.putString("USUARIO", Nome_user);
                myIntent.putExtras(bundle);
                startForresult.launch(myIntent);



                ExecutBackgrund();
                // defina um Handler
                Handler mHandler = new Handler();

                // coloque esse código na ação de um botão
                // Envie o Runnable para a fila para ser executado em aproximadamente 2 segundos
                mHandler.postDelayed(mRun, 2000);

            }
        });




    }

    // defina um Runnable, para ser executado depois de um tempo
    private Runnable mRun = new Runnable () {
        @Override
        public void run() {


            Activity_Dados = 3;
            // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
            Intent myIntent = new Intent(EditaActivity.this, TreActivity.class);

            Bundle bundle = new Bundle();

            bundle.putString("txt", "XMLWS");
            bundle.putString("viagem", "000");
            bundle.putString("USUARIO", Nome_user);
            bundle.putString("LINHA", "");
            myIntent.putExtras(bundle);
            startForresult.launch(myIntent);


        }
    };

    private void ExecutBackgrund() {
        ExecutorService server =  Executors.newSingleThreadExecutor();
        server.execute(new Runnable() {
            @Override
            public void run() {
                //onPreExecute method (O que ira fazer antes de executar
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                //doInBackGround method of AsyncTask (O que ira executar em segundo plano)
                // Esta etapa é usada para executar a tarefa em background ou fazer a chamada para o webservice
                //String sret = ConsultaBPeWS.Consulta_Xml("", "");//ConsultaBPeWS.webServiceConsulta();
                String sret = "";
                System.out.println("Resultado: " + sret);
                if (!sret.equals("")){
                    sret = sret.replace("m:", "");
                    try {
                        String sxml = sret;

                        File sdCard = getExternalFilesDir("Download");
                        File dir = new File(sdCard.getAbsolutePath() );
                        //dir.mkdirs();
                        File fileExt = new File(dir, "RetWS.xml");

                        //Cria o arquivo
                        fileExt.getParentFile().mkdirs();

                        //Abre o arquivo
                        FileOutputStream fosExt = null;
                        fosExt = new FileOutputStream(fileExt);

                        //Escreve no arquivo
                        fosExt.write(sxml.getBytes());

                        //Obrigatoriamente você precisa fechar
                        fosExt.close();
                    } catch (Exception e) {
                        System.out.println("Ex Salvando: " + e.toString());
                        e.printStackTrace();
                    }
                }
                ///////////

                //onPostExecute method (O que ira fazer depois de executar
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }

                });
            }
        });
    }






    public void Monta_Tela_Tre(){
        EditText edtRet = findViewById(R.id.edtRet);
        String sOri = edtRet.getText().toString();
        int posO = sOri.indexOf("-");
        String sCodOri = sOri.substring(0, (posO));
        int iOri = Integer.parseInt(sCodOri);
        ID_Old = sCodOri;
        DB_TRE dbt = new DB_TRE(this);
        // String Sorigem = dbt.Busca_Dados_Tre(idOri, "Codigo");

        EditText edtCodigo = findViewById(R.id.edt01);
        EditText edtDescri = findViewById(R.id.edt02);
        EditText edtUF = findViewById(R.id.edt03);
        EditText edtCodmun = findViewById(R.id.edt04);
        EditText edtID = findViewById(R.id.edt05);

        edtCodigo.setText(dbt.Busca_Dados_Tre(iOri, "Codigo"));
        edtDescri.setText(dbt.Busca_Dados_Tre(iOri, "Descri"));
        edtUF.setText(dbt.Busca_Dados_Tre(iOri, "UF"));
        edtCodmun.setText(dbt.Busca_Dados_Tre(iOri, "Codmun"));
        edtID.setText(dbt.Busca_Dados_Tre(iOri, "ID"));
    }

    public void Monta_Tela_Via(){
        EditText edtRet = findViewById(R.id.edtRet);
        String sOri = edtRet.getText().toString();
        int posO = sOri.indexOf("-");
        String sCodOri = sOri.substring(0, (posO));
        int iOri = Integer.parseInt(sCodOri);

        DB_VIA dbv = new DB_VIA(this);

        EditText edtid = findViewById(R.id.edt01);
        EditText edtDescri = findViewById(R.id.edt02);
        EditText edthora = findViewById(R.id.edt03);
        EditText edttipvia = findViewById(R.id.edt04);
        EditText edttipser = findViewById(R.id.edt05);
        EditText edtprefix = findViewById(R.id.edt06);

        edtid.setText(sCodOri);
        ID_Old = sCodOri;
        edtDescri.setText(dbv.Busca_Dados_Via(iOri, "Viagem"));
        edthora.setText(dbv.Busca_Dados_Via(iOri, "Hora"));
        edttipvia.setText(dbv.Busca_Dados_Via(iOri, "Tipvia"));
        edttipser.setText(dbv.Busca_Dados_Via(iOri, "Tipser"));
        edtprefix.setText(dbv.Busca_Dados_Via(iOri, "Prefix"));
    }


    public void Monta_Tela_Usr(){
        EditText edtRet = findViewById(R.id.edtRet);
        String sUsr = edtRet.getText().toString();
        int posO = sUsr.indexOf("-");
        String sCodUsr = sUsr.substring(0, (posO));
        int iUsr = Integer.parseInt(sCodUsr);

        DB_USR dbu = new DB_USR(this);

        EditText edt01 = findViewById(R.id.edt01);
        EditText edt02 = findViewById(R.id.edt02);
        EditText edt03 = findViewById(R.id.edt03);
        EditText edt04 = findViewById(R.id.edt04);
        EditText edt05 = findViewById(R.id.edt05);
        EditText edt06 = findViewById(R.id.edt06);

        edt01.setText(sCodUsr);
        ID_Old = sCodUsr;
        edt02.setText(dbu.Busca_Dados_Usr_ID(iUsr, "Usrnom"));
        edt03.setText(dbu.Busca_Dados_Usr_ID(iUsr, "Usrsen"));
        edt04.setText(dbu.Busca_Dados_Usr_ID(iUsr, "Fectur"));
        edt05.setText(dbu.Busca_Dados_Usr_ID(iUsr, "Ultatu"));
        edt06.setText("");

    }


    public void Monta_Tela_Lin(){
        EditText edtRet = findViewById(R.id.edtRet);
        String sLin = edtRet.getText().toString();
        int posO = sLin.indexOf("-");
        String sCodLin = sLin.substring(0, (posO));
        int iLin = Integer.parseInt(sCodLin);

        DB_LIN dblin = new DB_LIN(this);

        EditText edt01 = findViewById(R.id.edt01);
        EditText edt02 = findViewById(R.id.edt02);
        EditText edt03 = findViewById(R.id.edt03);
        EditText edt04 = findViewById(R.id.edt04);
        EditText edt05 = findViewById(R.id.edt05);
        EditText edt06 = findViewById(R.id.edt06);

        edt01.setText(sCodLin);
        ID_Old = sCodLin;
        edt02.setText(dblin.Busca_Dados_Lin_ID(iLin, "Codigo"));
        edt03.setText(dblin.Busca_Dados_Lin_ID(iLin, "Descri"));
        edt04.setText(dblin.Busca_Dados_Lin_ID(iLin, "Prefix"));
        edt05.setText("");
        edt06.setText("");

    }

    public void Altera_Cadastro_Tre(){

        EditText edtRet = findViewById(R.id.edtRet);
        String sOri = edtRet.getText().toString();
        int posO = sOri.indexOf("-");
        String sCodOri = sOri.substring(0, (posO));
        int iOri = Integer.parseInt(sCodOri);

        EditText edtCodigo = findViewById(R.id.edt01);
        EditText edtDescri = findViewById(R.id.edt02);
        EditText edtUF = findViewById(R.id.edt03);
        EditText edtCodmun = findViewById(R.id.edt04);
        EditText edtID = findViewById(R.id.edt05);
        String scodigo, sdescri, suf, scodmun, sId;
        scodigo = edtCodigo.getText().toString();
        sdescri = edtDescri.getText().toString();
        suf = edtUF.getText().toString();
        scodmun = edtCodmun.getText().toString();
        sId = edtID.getText().toString();


        DB_TRE dbt = new DB_TRE(this);
        dbt.Atualizar_Tre(sCodOri,sId,scodigo,sdescri,suf,scodmun);
    }


    public void Altera_Cadastro_Via(){

        EditText edtRet = findViewById(R.id.edtRet);
        String sOri = edtRet.getText().toString();
        int posO = sOri.indexOf("-");
        String sCodOri = sOri.substring(0, (posO));
        int iOri = Integer.parseInt(sCodOri);

        EditText edtCodigo = findViewById(R.id.edt01);
        EditText edtDescri = findViewById(R.id.edt02);
        EditText edtHora = findViewById(R.id.edt03);
        EditText edtTipvia = findViewById(R.id.edt04);
        EditText edtTipser = findViewById(R.id.edt05);
        EditText edtPrefix = findViewById(R.id.edt06);
        String scodigo, sdescri, shora, stipvia, stipser, sprefix;
        scodigo = edtCodigo.getText().toString();
        sdescri = edtDescri.getText().toString();
        shora = edtHora.getText().toString();
        stipvia = edtTipvia.getText().toString();
        stipser = edtTipser.getText().toString();
        sprefix = edtPrefix.getText().toString();

        DB_VIA dbv = new DB_VIA(this);
        dbv.Atualizar_Via(sCodOri,scodigo,sdescri,shora,stipvia,stipser,sprefix);
    }


    public void Altera_Cadastro_Usr(){

        EditText edtRet = findViewById(R.id.edtRet);
        String sUsr = edtRet.getText().toString();
        int posO = sUsr.indexOf("-");
        String sCodUsr = sUsr.substring(0, (posO));
        int iUsr = Integer.parseInt(sCodUsr);

        EditText edt01 = findViewById(R.id.edt01);
        EditText edt02 = findViewById(R.id.edt02);
        EditText edt03 = findViewById(R.id.edt03);
        EditText edt04 = findViewById(R.id.edt04);
        EditText edt05 = findViewById(R.id.edt05);
        EditText edt06 = findViewById(R.id.edt06);
        String sID, snome, ssenha, sfectur, sultatu;
        sID = edt01.getText().toString();
        snome = edt02.getText().toString();
        ssenha = edt03.getText().toString();
        snome = snome.toUpperCase();
        sfectur = edt04.getText().toString();
        sultatu = edt05.getText().toString();


        DB_USR dbu = new DB_USR(this);
        dbu.Atualizar_Usr(sID,snome,ssenha,sfectur,sultatu);
    }


    public void Altera_Cadastro_Lin(){

        EditText edtRet = findViewById(R.id.edtRet);
        String sLin = edtRet.getText().toString();
        int posO = sLin.indexOf("-");
        String sCodLin = sLin.substring(0, (posO));

        EditText edt01 = findViewById(R.id.edt01);
        EditText edt02 = findViewById(R.id.edt02);
        EditText edt03 = findViewById(R.id.edt03);
        EditText edt04 = findViewById(R.id.edt04);
        EditText edt05 = findViewById(R.id.edt05);
        EditText edt06 = findViewById(R.id.edt06);
        String sID, scodigo, sdescri, sprefix;
        sID = edt01.getText().toString();
        scodigo = edt02.getText().toString();
        sdescri = edt03.getText().toString();
        sprefix = edt04.getText().toString();


        DB_LIN dblin= new DB_LIN(this);
        dblin.Atualizar_Lin(sID,scodigo,sdescri, sprefix);
    }

    public void Cria_Cadastro_Usr(){

        EditText edt02 = findViewById(R.id.edt02);
        EditText edt03 = findViewById(R.id.edt03);
        EditText edt04 = findViewById(R.id.edt04);

        String snome, ssenha, sfectur;
        snome = edt02.getText().toString();
        ssenha = edt03.getText().toString();
        snome = snome.toUpperCase();
        sfectur = edt04.getText().toString();

        DB_USR dbu = new DB_USR(this);
        dbu.InserirUsr(snome,ssenha,sfectur, "");

        edt02.setText("");
        edt03.setText("");
        edt04.setText("");
    }

}
