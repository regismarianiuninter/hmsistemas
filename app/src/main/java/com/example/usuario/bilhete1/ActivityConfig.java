package com.example.usuario.bilhete1;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class ActivityConfig extends AppCompatActivity {
    SQLiteDatabase dbase;
    private static int Activity_Dados = 1;
    private static String ID_Old = "";
    private static String Nome_user = "";

    ActivityResultLauncher<Intent> startForresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Intent data = result.getData();
                    String sret = data.getStringExtra("msg");
                    String sorigemresult = data.getStringExtra("Activity_Dados");
                    if (sorigemresult.equals("2")){
                                System.out.println("Retorno da Listagem"+sret);

                                if (result.getResultCode() == RESULT_OK) {

                                    // mostra hint

                                    //EditText edtRet = findViewById(R.id.edtCnpj);
                                    //edtRet.setText(msg.toString());
                                    String sEmpresa = sret;
                                    int posID = sEmpresa.indexOf("-");
                                    String sCodID = sEmpresa.substring(0, (posID));
                                    int iID = Integer.parseInt(sCodID);

                                    ID_Old = sCodID;
                                    EditText formID = findViewById(R.id.edtID);
                                    EditText formcnpj = findViewById(R.id.edtCnpj);
                                    EditText formdescri = findViewById(R.id.edtDescri);
                                    EditText formie = findViewById(R.id.edtIE);
                                    EditText formim = findViewById(R.id.edtIM);
                                    EditText formcrt = findViewById(R.id.edtCRT);
                                    EditText formcnae = findViewById(R.id.edtCNAE);
                                    EditText formend = findViewById(R.id.edtEnd);
                                    EditText formnum = findViewById(R.id.edtNum);
                                    EditText formbairro = findViewById(R.id.edtBairro);
                                    EditText formcidade = findViewById(R.id.edtCid);
                                    EditText formuf = findViewById(R.id.edtUF);
                                    EditText formcep = findViewById(R.id.edtCEP);
                                    EditText formcodmun = findViewById(R.id.edtCodmun);
                                    EditText formfone = findViewById(R.id.edtFone);
                                    EditText formemail = findViewById(R.id.edtEmail);
                                    EditText formtar = findViewById(R.id.edtCodtar);
                                    EditText formamb = findViewById(R.id.edtAmb);
                                    EditText formmod = findViewById(R.id.edtMod);
                                    EditText formser = findViewById(R.id.edtSer);
                                    EditText formultimo = findViewById(R.id.edtUltbil);
                                    EditText formtipemi = findViewById(R.id.edtTipemi);
                                    EditText formtipbil = findViewById(R.id.edtTipbil);
                                    EditText formdatctg = findViewById(R.id.edtDatctg);
                                    EditText formspl = findViewById(R.id.edtSpl);
                                    EditText formaliq = findViewById(R.id.edtAliq);
                                    EditText formurl = findViewById(R.id.edtUrl);
                                    EditText formcrtemp = findViewById(R.id.edtCrtemp);
                                    EditText formcrtsen = findViewById(R.id.edtCrtSen);
                                    EditText formalitri = findViewById(R.id.edtAlitri);
                                    EditText formmaximp = findViewById(R.id.edtMaximp);
                                    EditText formconvei = findViewById(R.id.edtConvei);
                                    EditText formpvenda = findViewById(R.id.edtPvenda);
                                    EditText formendews = findViewById(R.id.edtEndews);
                                    EditText formrsv001 = findViewById(R.id.edtRsv001);
                                    EditText formrsv002 = findViewById(R.id.edtRsv002);
                                    EditText formrsv003 = findViewById(R.id.edtRsv003);
                                    Spinner formmodimp = findViewById(R.id.edtModimp);
                                    EditText formcartao = findViewById(R.id.edtCartao);
                                    EditText formbasseg = findViewById(R.id.edtBasseg);
                                    EditText formnaokey = findViewById(R.id.edtNaokey);
                                    EditText formmincan = findViewById(R.id.edtMincan);
                                    EditText formndstax = findViewById(R.id.edtNdstax);


                                    DB_EMP dbEmp = new DB_EMP(ActivityConfig.this);


                                    formID.setText(dbEmp.Busca_Dados_Emp(iID, "ID"));
                                    formcnpj.setText(dbEmp.Busca_Dados_Emp(iID, "Cnpj"));
                                    formdescri.setText(dbEmp.Busca_Dados_Emp(iID, "Descri"));
                                    formie.setText(dbEmp.Busca_Dados_Emp(iID, "Insest"));
                                    formim.setText(dbEmp.Busca_Dados_Emp(iID, "Insmun"));
                                    formcrt.setText(dbEmp.Busca_Dados_Emp(iID, "Codcrt"));
                                    formend.setText(dbEmp.Busca_Dados_Emp(iID, "Endere"));
                                    formnum.setText(dbEmp.Busca_Dados_Emp(iID, "Numero"));
                                    formbairro.setText(dbEmp.Busca_Dados_Emp(iID, "Bairro"));
                                    formcidade.setText(dbEmp.Busca_Dados_Emp(iID, "Cidade"));
                                    formuf.setText(dbEmp.Busca_Dados_Emp(iID, "UF"));
                                    formcep.setText(dbEmp.Busca_Dados_Emp(iID, "CEP"));
                                    formcodmun.setText(dbEmp.Busca_Dados_Emp(iID, "Codmun"));
                                    formfone.setText(dbEmp.Busca_Dados_Emp(iID, "Fone"));
                                    formemail.setText(dbEmp.Busca_Dados_Emp(iID, "Email"));
                                    formtar.setText(dbEmp.Busca_Dados_Emp(iID, "CodTar"));
                                    formamb.setText(dbEmp.Busca_Dados_Emp(iID, "Tipamb"));
                                    formmod.setText(dbEmp.Busca_Dados_Emp(iID, "Modelo"));
                                    formser.setText(dbEmp.Busca_Dados_Emp(iID, "Serie"));
                                    formultimo.setText(dbEmp.Busca_Dados_Emp(iID, "Ultbil"));
                                    formtipemi.setText(dbEmp.Busca_Dados_Emp(iID, "Tipemi"));
                                    formtipbil.setText(dbEmp.Busca_Dados_Emp(iID, "Tipbil"));
                                    formdatctg.setText(dbEmp.Busca_Dados_Emp(iID, "Datctg"));
                                    formcnae.setText(dbEmp.Busca_Dados_Emp(iID, "Cnae"));
                                    formspl.setText(dbEmp.Busca_Dados_Emp(iID, "Empspl"));
                                    formaliq.setText(dbEmp.Busca_Dados_Emp(iID, "Aliicm"));
                                    formurl.setText(dbEmp.Busca_Dados_Emp(iID, "Urlqrc"));
                                    formcrtemp.setText(dbEmp.Busca_Dados_Emp(iID, "Crtemp"));
                                    formcrtsen.setText(dbEmp.Busca_Dados_Emp(iID, "Crtsen"));
                                    formalitri.setText(dbEmp.Busca_Dados_Emp(iID, "Alitri"));
                                    formmaximp.setText(dbEmp.Busca_Dados_Emp(iID, "Maximp"));
                                    formconvei.setText(dbEmp.Busca_Dados_Emp(iID, "Convei"));
                                    formpvenda.setText(dbEmp.Busca_Dados_Emp(iID, "Pvenda"));
                                    formendews.setText(dbEmp.Busca_Dados_Emp(iID, "Endews"));
                                    formrsv001.setText(dbEmp.Busca_Dados_Emp(iID, "Rsv001"));
                                    formrsv002.setText(dbEmp.Busca_Dados_Emp(iID, "Rsv002"));
                                    formrsv003.setText(dbEmp.Busca_Dados_Emp(iID, "Rsv003"));
                                    String smodimp = dbEmp.Busca_Dados_Emp(iID, "Modimp");
                                    if (!smodimp.equals("")) {
                                        if (smodimp.equals("01")) {
                                            smodimp = "01 - SUNMI";
                                        }
                                        if (smodimp.equals("02")) {
                                            smodimp = "02 - CIELO LIO";
                                        }
                                        if (smodimp.equals("03")) {
                                            smodimp = "03 - ARNY BLUETOOTH";
                                        }
                                        if (smodimp.equals("04")) {
                                            smodimp = "04 - ARNY AIDL";
                                        }
                                        if (smodimp.equals("05")) {
                                            smodimp = "05 - DTS-2500";
                                        }
                                    } else {
                                        smodimp = "00 - DEFINIR IMPRESSORA";
                                    }
                                    if (!smodimp.equals(null)) {
                                        ArrayAdapter spntipgraAdapter = ArrayAdapter.createFromResource(ActivityConfig.this, R.array.impressora_array,
                                                android.R.layout.simple_spinner_dropdown_item);
                                        int spinnerPostion = spntipgraAdapter.getPosition(smodimp);
                                        formmodimp.setSelection(spinnerPostion);
                                        spinnerPostion = 0;
                                    }
                                    formcartao.setText(dbEmp.Busca_Dados_Emp(iID, "Cartao"));
                                    formbasseg.setText(dbEmp.Busca_Dados_Emp(iID, "Basseg"));
                                    formnaokey.setText(dbEmp.Busca_Dados_Emp(iID, "Naokey"));
                                    formmincan.setText(dbEmp.Busca_Dados_Emp(iID, "Mincan"));
                                    formndstax.setText(dbEmp.Busca_Dados_Emp(iID, "Ndstax"));


                                }


                    }
                }
            }


        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent Newintent = getIntent();
        Bundle bundle = Newintent.getExtras();
        String user = bundle.getString("USUARIO");
        Nome_user = user;

        final Spinner spinner = findViewById(R.id.edtModimp);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.impressora_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                    Altera_Config();

            }
        });

        Button btnNovo = findViewById(R.id.btnNovo);
        btnNovo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtcnpj = findViewById(R.id.edtCnpj);
                String sCNPJ = edtcnpj.getText().toString();
                int itot = sCNPJ.length();
                if (itot != 0){
                    Toast.makeText(ActivityConfig.this, "TELA"  , Toast.LENGTH_LONG).show();
                    Criar_Config_Tela();
                }
                else if (itot == 0) {
                    Toast.makeText(ActivityConfig.this, "TXT"  , Toast.LENGTH_LONG).show();
                    Criar_Config_Txt();
                }


            }
        });

        Button btnListar = findViewById(R.id.btnListar);
        btnListar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Listar_Config();
            }
        });

        Button btnimportar = findViewById(R.id.btnImportar);
        btnimportar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ExecutBackgrund();
            }
        });


    }



    public void Listar_Config(){
        Activity_Dados = 2;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(ActivityConfig.this, TreActivity.class);

        String txt = "EMP";
        Bundle bundle = new Bundle();

        bundle.putString("txt", txt);
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("Activity_Dados", "2");

        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);

        /*DB_EMP dbemp = new DB_EMP(this);
        SQLiteDatabase db = dbemp.getWritableDatabase();
        dbemp.onUpgrade(db,10, 11);*/





    }

    public void Altera_Config(){
        EditText formID = findViewById(R.id.edtID);
        EditText formcnpj = findViewById(R.id.edtCnpj);
        EditText formdescri = findViewById(R.id.edtDescri);
        EditText formie = findViewById(R.id.edtIE);
        EditText formim = findViewById(R.id.edtIM);
        EditText formcrt = findViewById(R.id.edtCRT);
        EditText formcnae = findViewById(R.id.edtCNAE);
        EditText formend = findViewById(R.id.edtEnd);
        EditText formnum = findViewById(R.id.edtNum);
        EditText formbairro = findViewById(R.id.edtBairro);
        EditText formcidade = findViewById(R.id.edtCid);
        EditText formuf = findViewById(R.id.edtUF);
        EditText formcep = findViewById(R.id.edtCEP);
        EditText formcodmun = findViewById(R.id.edtCodmun);
        EditText formfone = findViewById(R.id.edtFone);
        EditText formemail = findViewById(R.id.edtEmail);
        EditText formtar = findViewById(R.id.edtCodtar);
        EditText formamb = findViewById(R.id.edtAmb);
        EditText formmod = findViewById(R.id.edtMod);
        EditText formser = findViewById(R.id.edtSer);
        EditText formultimo = findViewById(R.id.edtUltbil);
        EditText formtipemi = findViewById(R.id.edtTipemi);
        EditText formtipbil = findViewById(R.id.edtTipbil);
        EditText formdatctg = findViewById(R.id.edtDatctg);
        EditText formspl = findViewById(R.id.edtSpl);
        EditText formaliq = findViewById(R.id.edtAliq);
        EditText formurl = findViewById(R.id.edtUrl);
        EditText formcrtemp = findViewById(R.id.edtCrtemp);
        EditText formcrtsen = findViewById(R.id.edtCrtSen);
        EditText formalitri = findViewById(R.id.edtAlitri);
        EditText formmaximp = findViewById(R.id.edtMaximp);
        EditText formconvei = findViewById(R.id.edtConvei);
        EditText formpvenda = findViewById(R.id.edtPvenda);
        EditText formendews = findViewById(R.id.edtEndews);
        EditText formrsv001 = findViewById(R.id.edtRsv001);
        EditText formrsv002 = findViewById(R.id.edtRsv002);
        EditText formrsv003 = findViewById(R.id.edtRsv003);
        Spinner formmodimp = findViewById(R.id.edtModimp);
        EditText formcartao = findViewById(R.id.edtCartao);
        EditText formbasseg = findViewById(R.id.edtBasseg);
        EditText formnaokey = findViewById(R.id.edtNaokey);
        EditText formmincan = findViewById(R.id.edtMincan);
        EditText formndstax = findViewById(R.id.edtNdstax);

        String sID_Old, sID, scnpj, sdescri, sie, sim, scrt, sendere, snum, sbairro, scidade, suf, scep, scodmun;
        String sfone, semail, star, samb, smod, sser, sultimo, stipemi, stipbil, sdatctg, scnae, empspl, saliq, surl;
        String scrtemp, scrtsen, salitri, smaximp, sconvei, spvenda, sendews, srsv001, srsv002, srsv003, smodimp, scartao;
        String sbasseg, snaokey, smincan, sndstax;
        sID = formID.getText().toString();
        scnpj = formcnpj.getText().toString();
        sdescri = formdescri.getText().toString();
        sie = formie.getText().toString();
        sim = formim.getText().toString();
        scrt = formcrt.getText().toString();
        sendere = formend.getText().toString();
        snum = formnum.getText().toString();
        sbairro = formbairro.getText().toString();
        scidade = formcidade.getText().toString();
        suf = formuf.getText().toString();
        scep = formcep.getText().toString();
        scodmun = formcodmun.getText().toString();
        sfone = formfone.getText().toString();
        semail = formemail.getText().toString();
        star = formtar.getText().toString();
        samb = formamb.getText().toString();
        smod = formmod.getText().toString();
        sser = formser.getText().toString();
        sultimo = formultimo.getText().toString();
        stipemi = formtipemi.getText().toString();
        stipbil = formtipbil.getText().toString();
        sdatctg = formdatctg.getText().toString();
        scnae = formcnae.getText().toString();
        empspl = formspl.getText().toString();
        saliq = formaliq.getText().toString();
        surl = formurl.getText().toString();
        scrtemp = formcrtemp.getText().toString();
        scrtsen = formcrtsen.getText().toString();
        salitri = formalitri.getText().toString();
        smaximp = formmaximp.getText().toString();
        sconvei = formconvei.getText().toString();
        spvenda = formpvenda.getText().toString();
        sendews = formendews.getText().toString();
        srsv001 = formrsv001.getText().toString();
        srsv002 = formrsv002.getText().toString();
        srsv003 = formrsv003.getText().toString();
        smodimp = formmodimp.getSelectedItem().toString();
        smodimp = smodimp.substring(0,2);
        scartao = formcartao.getText().toString();
        sbasseg = formbasseg.getText().toString();
        snaokey = formnaokey.getText().toString();
        smincan = formmincan.getText().toString();
        sndstax = formndstax.getText().toString();
        sID_Old = ID_Old;



        DB_EMP dbemp = new DB_EMP(this);
        dbemp.Atualizar_Emp(sID_Old,sID,scnpj, sdescri, sie, sim, scrt, sendere, snum, sbairro, scidade, suf, scodmun,
                scep, sfone, semail, star, samb, smod, sser, sultimo, stipemi, stipbil, sdatctg,  scnae, empspl, saliq, surl,
                scrtemp, scrtsen, salitri, smaximp, sconvei, spvenda, sendews, srsv001, srsv002, srsv003, smodimp, "",
                scartao, sbasseg, snaokey, smincan, "", sndstax);
    }

    public void Criar_Config_Txt(){
        Toast.makeText(ActivityConfig.this, "Criar_Config"  , Toast.LENGTH_LONG).show();

        DB_EMP dbempd = new DB_EMP(this);
        dbempd.deletar_Emp();

        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;


        try {
            File file = new File(sarquivo, "/EMPRESA.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));;
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                Toast.makeText(ActivityConfig.this, "Dentro do Arquivo"    , Toast.LENGTH_LONG).show();

                String scnpj      = linhaDoArquivo[0];
                String sdescri    = linhaDoArquivo[1];
                Toast.makeText(ActivityConfig.this, sdescri    , Toast.LENGTH_LONG).show();
                String sie        = linhaDoArquivo[2];
                String sim        = linhaDoArquivo[3];
                String scrt       = linhaDoArquivo[4];
                String sendere    = linhaDoArquivo[5];
                String snum       = linhaDoArquivo[6];
                String sbairro    = linhaDoArquivo[7];
                String scidade    = linhaDoArquivo[8];
                String suf        = linhaDoArquivo[9];
                String scodmun    = linhaDoArquivo[10];
                String scep       = linhaDoArquivo[11];
                String sfone      = linhaDoArquivo[12];
                String semail     = linhaDoArquivo[13];
                String sTar       = linhaDoArquivo[14];
                String samb       = linhaDoArquivo[15];
                String smod       = linhaDoArquivo[16];
                String sser       = linhaDoArquivo[17];
                String sultbil    = linhaDoArquivo[18];
                String stipemi    = linhaDoArquivo[19];
                String stipbil    = linhaDoArquivo[20];
                String sdatctg    = linhaDoArquivo[21];
                String scnae      = linhaDoArquivo[22];
                String sempspl    = linhaDoArquivo[23];
                String saliq      = linhaDoArquivo[24];
                String surl       = linhaDoArquivo[25];
                String scrtemp    = linhaDoArquivo[26];
                String scrtsen    = linhaDoArquivo[27];
                String salitri    = linhaDoArquivo[28];
                String smaximp    = linhaDoArquivo[29];
                String sconvei    = linhaDoArquivo[30];
                String spvenda    = linhaDoArquivo[31];
                String sendews    = linhaDoArquivo[32];
                String srsv001    = linhaDoArquivo[33];
                String srsv002    = linhaDoArquivo[34];
                String srsv003    = linhaDoArquivo[35];





                DB_EMP dbemp = new DB_EMP(this);
                    dbemp.InserirEmp(scnpj, sdescri, sie, sim, scrt, sendere, snum, sbairro, scidade, suf, scodmun,
                            scep, sfone, semail, sTar, samb, smod, sser, sultbil, stipemi, stipbil, sdatctg, scnae, sempspl,
                            saliq, surl, scrtemp, scrtsen, salitri, smaximp, sconvei, spvenda, sendews, srsv001, srsv002, srsv003, "", "", "", "", "", "", "", "");


            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void Criar_Config_Tela(){
        EditText formID = findViewById(R.id.edtID);
        EditText formcnpj = findViewById(R.id.edtCnpj);
        EditText formdescri = findViewById(R.id.edtDescri);
        EditText formie = findViewById(R.id.edtIE);
        EditText formim = findViewById(R.id.edtIM);
        EditText formcrt = findViewById(R.id.edtCRT);
        EditText formcnae = findViewById(R.id.edtCNAE);
        EditText formend = findViewById(R.id.edtEnd);
        EditText formnum = findViewById(R.id.edtNum);
        EditText formbairro = findViewById(R.id.edtBairro);
        EditText formcidade = findViewById(R.id.edtCid);
        EditText formuf = findViewById(R.id.edtUF);
        EditText formcep = findViewById(R.id.edtCEP);
        EditText formcodmun = findViewById(R.id.edtCodmun);
        EditText formfone = findViewById(R.id.edtFone);
        EditText formemail = findViewById(R.id.edtEmail);
        EditText formtar = findViewById(R.id.edtCodtar);
        EditText formamb = findViewById(R.id.edtAmb);
        EditText formmod = findViewById(R.id.edtMod);
        EditText formser = findViewById(R.id.edtSer);
        EditText formultimo = findViewById(R.id.edtUltbil);
        EditText formtipemi = findViewById(R.id.edtTipemi);
        EditText formtipbil = findViewById(R.id.edtTipbil);
        EditText formdatctg = findViewById(R.id.edtDatctg);
        EditText formspl    = findViewById(R.id.edtSpl);
        EditText formaliq = findViewById(R.id.edtAliq);
        EditText formurl = findViewById(R.id.edtUrl);
        EditText formcrtemp = findViewById(R.id.edtCrtemp);
        EditText formcrtsen = findViewById(R.id.edtCrtSen);
        EditText formalitri = findViewById(R.id.edtAlitri);
        EditText formmaximp = findViewById(R.id.edtMaximp);
        EditText formconvei = findViewById(R.id.edtConvei);
        EditText formpvenda = findViewById(R.id.edtPvenda);
        EditText formendews = findViewById(R.id.edtEndews);
        EditText formrsv001 = findViewById(R.id.edtRsv001);
        EditText formrsv002 = findViewById(R.id.edtRsv002);
        EditText formrsv003 = findViewById(R.id.edtRsv003);
        Spinner formmodimp = findViewById(R.id.edtModimp);
        EditText formcartao = findViewById(R.id.edtCartao);
        EditText formbasseg = findViewById(R.id.edtBasseg);
        EditText formnaokey = findViewById(R.id.edtNaokey);
        EditText formmincan = findViewById(R.id.edtMincan);
        EditText formndstax = findViewById(R.id.edtNdstax);


        String sID_Old, sID, scnpj, sdescri, sie, sim, scrt, sendere, snum, sbairro, scidade, suf, scep, scodmun;
        String sfone, semail, star, samb, smod, sser, sultimo, stipemi, stipbil, sdatctg, scnae, sempspl, saliq, surl;
        String scrtemp, scrtsen, salitri, smaximp, sconvei, spvenda, sendews, srsv001, srsv002, srsv003, smodimp, scartao;
        String sbasseg, snaokey, smincan, sndstax;
        sID = formID.getText().toString();
        scnpj = formcnpj.getText().toString();
        sdescri = formdescri.getText().toString();
        sie = formie.getText().toString();
        sim = formim.getText().toString();
        scrt = formcrt.getText().toString();
        sendere = formend.getText().toString();
        snum = formnum.getText().toString();
        sbairro = formbairro.getText().toString();
        scidade = formcidade.getText().toString();
        suf = formuf.getText().toString();
        scep = formcep.getText().toString();
        scodmun = formcodmun.getText().toString();
        sfone = formfone.getText().toString();
        semail = formemail.getText().toString();
        star = formtar.getText().toString();
        samb = formamb.getText().toString();
        smod = formmod.getText().toString();
        sser = formser.getText().toString();
        sultimo = formultimo.getText().toString();
        stipemi = formtipemi.getText().toString();
        stipbil = formtipbil.getText().toString();
        sdatctg = formdatctg.getText().toString();
        scnae = formcnae.getText().toString();
        sempspl = formspl.getText().toString();
        saliq = formaliq.getText().toString();
        surl = formurl.getText().toString();
        scrtemp = formcrtemp.getText().toString();
        scrtsen = formcrtsen.getText().toString();
        salitri = formalitri.getText().toString();
        smaximp = formmaximp.getText().toString();
        sconvei = formmaximp.getText().toString();
        spvenda = formmaximp.getText().toString();
        sendews = formmaximp.getText().toString();
        srsv001 = formrsv001.getText().toString();
        srsv002 = formrsv002.getText().toString();
        srsv003 = formrsv003.getText().toString();
        smodimp = formmodimp.getSelectedItem().toString();
        smodimp = smodimp.substring(0,2);
        scartao = formcartao.getText().toString();
        sbasseg = formbasseg.getText().toString();
        snaokey = formnaokey.getText().toString();
        smincan = formmincan.getText().toString();
        sndstax = formndstax.getText().toString();


        DB_EMP dbemp = new DB_EMP(this);
        dbemp.InserirEmp(scnpj, sdescri, sie, sim, scrt, sendere, snum, sbairro, scidade, suf, scodmun,
                scep, sfone, semail, star, samb, smod, sser, sultimo, stipemi, stipbil, sdatctg, scnae, sempspl,
                saliq, surl, scrtemp, scrtsen, salitri, smaximp, sconvei, spvenda, sendews, srsv001, srsv002, srsv003,
                smodimp, "", scartao, sbasseg, snaokey, smincan, "", sndstax);
    }


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

                ///////////
                EditText edtws = findViewById(R.id.edtEndews);
                String sendews = edtws.getText().toString();
                String sret = Busca_Cadastro(sendews, ActivityConfig.this);
                if (!sret.equals("")) {
                    sret = sret.replace("m:", "");
                    try {
                        String sxml = sret;
                        File sdCard = getExternalFilesDir("Download");
                        //System.out.println("sdCard: " + sdCard.getAbsolutePath());
                        File dir = new File(sdCard.getAbsolutePath() );
                        if (!dir.exists()) {
                            //System.out.println("Diretorio nao existe: ");
                            dir.mkdir();
                        }
                        File fileExt = new File(dir, "RetWSCADASTRO.xml");

                        //Cria o arquivo
                        fileExt.getParentFile().mkdirs();

                        //Abre o arquivo
                        FileOutputStream fosExt = null;
                        fosExt = new FileOutputStream(fileExt);

                        //Escreve no arquivo
                        fosExt.write(sxml.getBytes());

                        //Obrigatoriamente você precisa fechar
                        fosExt.close();
                        /////////////////////////////////////////////////
                        if (!sret.equals("")) { // so entra se retornou xml do ws
                            try {
                                String spassou = "";
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWSCADASTRO.xml");
                                Document doc = dBuilder.parse(fXmlFile);
                                doc.getDocumentElement().normalize();
                                NodeList nodeResponse = doc.getElementsByTagName("busca_EmpresaResponse");
                                for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                    Node nNode = nodeResponse.item(temp);
                                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eElement = (Element) nNode;

                                        NodeList nodeResult = doc.getElementsByTagName("busca_EmpresaResult");
                                        for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {

                                            Node nresult = nodeResult.item(tempresult);
                                            Element resultElement = (Element) nresult;
                                            //////////////////////////////////////////////
                                            NodeList nodeEmp = doc.getElementsByTagName("tSIS_EMP");
                                            for (int tempEmp = 0; tempEmp < nodeEmp.getLength(); tempEmp++) {
                                                Node ntipo = nodeEmp.item(tempEmp);
                                                Element empElement = (Element) ntipo;
                                                String scnpj = empElement.getElementsByTagName("INSC_CNPJ").item(0).getTextContent();
                                                String sdescri = empElement.getElementsByTagName("DESCRI").item(0).getTextContent();
                                                String sinsest = empElement.getElementsByTagName("INSC_EST").item(0).getTextContent();
                                                String sinsmun = empElement.getElementsByTagName("INSMUN").item(0).getTextContent();
                                                String scrt = empElement.getElementsByTagName("REGTRI").item(0).getTextContent();
                                                String sendere = empElement.getElementsByTagName("ENDERECO").item(0).getTextContent();
                                                String snum = empElement.getElementsByTagName("NUMERO").item(0).getTextContent();
                                                String sbairro = empElement.getElementsByTagName("BAIRRO").item(0).getTextContent();
                                                String scid = empElement.getElementsByTagName("MUNICIPIO").item(0).getTextContent();
                                                String suf = empElement.getElementsByTagName("SIGLUF").item(0).getTextContent();
                                                String scodmun = empElement.getElementsByTagName("CODMUN").item(0).getTextContent();
                                                String scep = empElement.getElementsByTagName("CEP").item(0).getTextContent();
                                                String stel = empElement.getElementsByTagName("TELEFONE").item(0).getTextContent();
                                                String semail = empElement.getElementsByTagName("EMAIL").item(0).getTextContent();
                                                String star = empElement.getElementsByTagName("CODTAR").item(0).getTextContent();
                                                String samb = empElement.getElementsByTagName("TIPAMB").item(0).getTextContent();
                                                String smod = empElement.getElementsByTagName("MDBPDR").item(0).getTextContent();
                                                String sser = empElement.getElementsByTagName("SERPDR").item(0).getTextContent();
                                                String scnae = empElement.getElementsByTagName("CDCNAE").item(0).getTextContent();
                                                String sempspl = empElement.getElementsByTagName("EMPSPL").item(0).getTextContent();
                                                String salibil = empElement.getElementsByTagName("ALIBIL").item(0).getTextContent();
                                                String salitri = empElement.getElementsByTagName("ALITRI").item(0).getTextContent();
                                                if (!scnpj.equals("")) { //Salvar dados da Empresa
                                                    DB_EMP dbemp = new DB_EMP(ActivityConfig.this);
                                                    dbemp.deletar_Emp();
                                                    dbemp.InserirEmp(scnpj, sdescri, sinsest, sinsmun, scrt, sendere, snum, sbairro, scid, suf, scodmun,
                                                            scep, stel, semail, star, samb, smod, sser, "0", "1", "0", "", scnae, sempspl,
                                                            salibil, "https://dfe-portal.svrs.rs.gov.br/bpe/qrcode", "", "", salitri, "0", "N", "E", sendews, "xx", "xx", "xx", "", "", "", "", "", "", "", "");
                                                }
                                                NodeList nodeLinhas = empElement.getElementsByTagName("tLinhas");
                                                for (int tempLinhas = 0; tempLinhas < nodeLinhas.getLength(); tempLinhas++) {
                                                    DB_LIN dblin = new DB_LIN(ActivityConfig.this);
                                                    dblin.deletar_Lin();
                                                    DB_PER dbper = new DB_PER(ActivityConfig.this);
                                                    dbper.deletar_Per();

                                                    DB_VIA dbvia = new DB_VIA(ActivityConfig.this);
                                                    dbvia.deletar_Via();

                                                    Node ntipos = nodeLinhas.item(tempLinhas);
                                                    if (ntipos.getNodeType() == Node.ELEMENT_NODE) {
                                                        Element LinhasElement = (Element) ntipos;
                                                        NodeList nodeSEP_LIN = LinhasElement.getElementsByTagName("tSEP_LIN");
                                                        for (int tempSEP_LIN = 0; tempSEP_LIN < nodeSEP_LIN.getLength(); tempSEP_LIN++) {
                                                            Node ntipoL = nodeSEP_LIN.item(tempSEP_LIN);
                                                            Element SEP_LINElement = (Element) ntipoL;
                                                            String scodigo = SEP_LINElement.getElementsByTagName("iCodigo").item(0).getTextContent();
                                                            String slinha = SEP_LINElement.getElementsByTagName("sDescri").item(0).getTextContent();
                                                            String sprefix = SEP_LINElement.getElementsByTagName("sPrefix").item(0).getTextContent();
                                                            dblin.InserirLin(scodigo, slinha, sprefix);

                                                            NodeList nodetarifas = SEP_LINElement.getElementsByTagName("tTarifas");
                                                            for (int temptarifas = 0; temptarifas < nodetarifas.getLength(); temptarifas++) {
                                                                Node ntipotar = nodetarifas.item(temptarifas);
                                                                Element tarifaElement = (Element) ntipotar;
                                                                if (ntipotar.getNodeType() == Node.ELEMENT_NODE) {
                                                                    NodeList nodeper = tarifaElement.getElementsByTagName("tSEP_ILI");
                                                                    for (int tempper = 0; tempper < nodeper.getLength(); tempper++) {
                                                                        Node ntipoP = nodeper.item(tempper);
                                                                        Element perElement = (Element) ntipoP;
                                                                        String sori = perElement.getElementsByTagName("nTreori").item(0).getTextContent();
                                                                        String sdes = perElement.getElementsByTagName("nTredes").item(0).getTextContent();
                                                                        String svtar = perElement.getElementsByTagName("nvlrTar").item(0).getTextContent();
                                                                        String svseg = perElement.getElementsByTagName("nvlrSeg").item(0).getTextContent();
                                                                        String svarre = perElement.getElementsByTagName("nvlrArre").item(0).getTextContent();
                                                                        String stipvia = perElement.getElementsByTagName("sTipvia").item(0).getTextContent();
                                                                        dbper.InserirPercurso(scodigo, sori, sdes, svtar, svseg, svarre, stipvia);
                                                                    }
                                                                }
                                                            }


                                                            NodeList nodeviagens = SEP_LINElement.getElementsByTagName("tViagens");
                                                            for (int tempviagens = 0; tempviagens < nodeviagens.getLength(); tempviagens++) {
                                                                Node ntipovg = nodeviagens.item(tempviagens);
                                                                Element viagensElement = (Element) ntipovg;
                                                                if (ntipovg.getNodeType() == Node.ELEMENT_NODE) {
                                                                    NodeList nodevia = viagensElement.getElementsByTagName("tSEP_VIF");
                                                                    for (int tempvia = 0; tempvia < nodevia.getLength(); tempvia++) {
                                                                        Node ntipoV = nodevia.item(tempvia);
                                                                        Element viaElement = (Element) ntipoV;
                                                                        String scodvia = viaElement.getElementsByTagName("iLinha").item(0).getTextContent();
                                                                        String sdescrivia = viaElement.getElementsByTagName("sDescri").item(0).getTextContent();
                                                                        String shora = viaElement.getElementsByTagName("sHora").item(0).getTextContent();
                                                                        String stipvia = viaElement.getElementsByTagName("sTipvia").item(0).getTextContent();
                                                                        String stipser = viaElement.getElementsByTagName("sTipser").item(0).getTextContent();
                                                                        String sprefixvia = viaElement.getElementsByTagName("sPrefix").item(0).getTextContent();
                                                                        dbvia.InserirViagem(scodvia, sdescrivia, shora, stipvia, stipser, sprefixvia);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                                DB_TRE dbtre = new DB_TRE(ActivityConfig.this);
                                                dbtre.deletar_Tre();

                                                NodeList nodetrechos = empElement.getElementsByTagName("tTrechos");
                                                for (int temptrechos = 0; temptrechos < nodetrechos.getLength(); temptrechos++) {System.out.println("Entrei no Trechos");
                                                    Node ntipotc = nodetrechos.item(temptrechos);
                                                    Element trechosElement = (Element) ntipotc;
                                                    if (ntipotc.getNodeType() == Node.ELEMENT_NODE) {
                                                        NodeList nodetre = trechosElement.getElementsByTagName("tSEP_TRE");
                                                        for (int temptre = 0; temptre < nodetre.getLength(); temptre++) {
                                                            Node ntipoT = nodetre.item(temptre);
                                                            Element treElement = (Element) ntipoT;
                                                            String scodtre = treElement.getElementsByTagName("iCodigo").item(0).getTextContent();
                                                            String sdesctre = treElement.getElementsByTagName("sDescri").item(0).getTextContent();
                                                            String suftre = treElement.getElementsByTagName("sUF").item(0).getTextContent();
                                                            String scodmuntre = treElement.getElementsByTagName("sCodmun").item(0).getTextContent();
                                                            dbtre.InserirTrecho(scodtre, sdesctre, suftre, scodmuntre);

                                                        }
                                                    }
                                                }
                                                DB_USR dbusr = new DB_USR(ActivityConfig.this);
                                                NodeList nodeagentes = empElement.getElementsByTagName("tAgentes");
                                                for (int tempagentes = 0; tempagentes < nodeagentes.getLength(); tempagentes++) {
                                                    Node ntipoag = nodeagentes.item(tempagentes);
                                                    Element agentesElement = (Element) ntipoag;
                                                    if (ntipoag.getNodeType() == Node.ELEMENT_NODE) {
                                                        NodeList nodeage = agentesElement.getElementsByTagName("tSEP_AGE");
                                                        for (int tempage = 0; tempage < nodeage.getLength(); tempage++) {
                                                            Node ntipoA = nodeage.item(tempage);
                                                            Element ageElement = (Element) ntipoA;
                                                            String snome = ageElement.getElementsByTagName("sNome").item(0).getTextContent();
                                                            String ssenha = ageElement.getElementsByTagName("sSenha").item(0).getTextContent();
                                                            String sfectur = ageElement.getElementsByTagName("sFectur").item(0).getTextContent();
                                                            dbusr.InserirUsr(snome, ssenha, sfectur, "");


                                                        }
                                                    }
                                                }

                                                File sdCard2 = getExternalFilesDir("Download");
                                                File dir2 = new File(sdCard2.getAbsolutePath() );
                                                File fileExt2 = new File(dir2, "VEICULOS.txt");

                                                //Cria o arquivo
                                                fileExt2.getParentFile().mkdirs();

                                                //Abre o arquivo
                                                FileOutputStream fosExt2 = null;
                                                fosExt2 = new FileOutputStream(fileExt2);



                                                NodeList nodeveiculos = empElement.getElementsByTagName("tVeiculos");
                                                for (int tempveiculos = 0; tempveiculos < nodeveiculos.getLength(); tempveiculos++) {
                                                    Node ntipove = nodeveiculos.item(tempveiculos);
                                                    Element veiculosElement = (Element) ntipove;
                                                    if (ntipove.getNodeType() == Node.ELEMENT_NODE) {
                                                        NodeList nodevei = veiculosElement.getElementsByTagName("tSCF_VEI");
                                                        for (int tempvei = 0; tempvei < nodevei.getLength(); tempvei++) {
                                                            Node ntipoV = nodevei.item(tempvei);
                                                            Element veiElement = (Element) ntipoV;
                                                            String splaca = veiElement.getElementsByTagName("sPlaca").item(0).getTextContent();

                                                            //Escreve no arquivo
                                                            fosExt2.write(splaca.getBytes());
                                                            fosExt2.write("\n".getBytes());

                                                        }
                                                    }
                                                }

                                                //Obrigatoriamente você precisa fechar
                                                fosExt2.close();


                                            }
                                        }
                                    }
                                }
                                if (spassou.equals("")) {


                                }
                            } catch (Exception e) {


                            }
                        }
                    } catch (Exception e) {


                    }
                } else {


                }
                //onPostExecute method (O que ira fazer depois de executar
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }

                });
            }
        });
    }







    public static String Busca_Cadastro(String swsenvio, Context contexview) {

        String retorno = "";

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/busca_Empresa";
            final String METODO = "busca_Empresa";


            SoapObject request = new SoapObject(NAMESPACE, METODO);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);


            //System.out.println("Entrei");
            // Adiciona parâmetros
            // request.addProperty("sxmlenvio", sXmlBpe);


            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            int itimeout = 25000;

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, itimeout);


            try {
                 //System.out.println("Entrei no Try");
                androidHttpTransport.debug = true;

                androidHttpTransport.call(SOAP_ACTION + METODO, envelope);

                // SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();


                String resultString = androidHttpTransport.responseDump;


                 //System.out.println("Retorno xml: "+resultString);
                if (resultString != null) {
                    retorno = resultString;
                    //System.out.println("Exception xml: " + retorno);
                    return retorno;
                } else {
                    retorno = "";
                    //System.out.println(retorno);
                    return retorno;
                }
            } catch (SocketTimeoutException tm) {
                //System.out.println("Timeout: "+tm.toString());
                retorno = "";
                return retorno;
            } catch (XmlPullParserException e) {
                //System.out.println(e.toString());
                retorno = "";
                return retorno;
            } catch (IOException e) {
                System.out.println(e.toString());
                retorno = "";
                return retorno;
            } catch (Exception e) {
                retorno = "";
                //System.out.println("Exception xml2: "+e.toString());
                e.printStackTrace();
                //return retorno;

            }

        }

        return retorno;
    }



}
