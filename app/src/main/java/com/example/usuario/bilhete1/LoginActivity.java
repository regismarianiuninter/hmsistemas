package com.example.usuario.bilhete1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class LoginActivity extends AppCompatActivity {
    private static int Activity_Dados = 1;
    private static String Nome_user = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Button btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // em ambos casos criar um Intent e adiciona um string nele (putExtra) e seta o Result
                EditText edtUser = findViewById(R.id.edtUsuario);
                EditText edtSenha = findViewById(R.id.edtSenha);
                String suser = edtUser.getText().toString();
                String ssenha = edtSenha.getText().toString();
                suser = suser.toUpperCase();
                if (!suser.equals("")) {
                    String sUsuario = "";
                    DB_USR dbu = new DB_USR(LoginActivity.this);
                    DB_USR.UsrCursor cursor = dbu.RetornarUsr(DB_USR.UsrCursor.OrdenarPor.NomeCrescente);
                    String sOK = "";
                    for( int i=0; i <cursor.getCount(); i++)
                    {
                        cursor.moveToPosition(i);
                        sUsuario = cursor.getUsrnom();
                        if (!sUsuario.equals("")){ //se encontrou algumr egistro
                            if (sUsuario.equals(suser)){//se encontrou o usuario digitado
                                String usrsen = cursor.getUsrsen();
                                if (ssenha.equals(usrsen)) { //Senha Confere
                                    sOK = "S";
                                    DB_EMP dbemplog = new DB_EMP(LoginActivity.this);
                                    if (sUsuario.equals("CAIXA")) { //se for usuario caixa marca ponto de venda como Rodoviaria
                                        dbemplog.Atualizar_Campo_Emp("1", "Pvenda", "R");
                                    } else { //caso contrario marca como Estrada
                                        dbemplog.Atualizar_Campo_Emp("1", "Pvenda", "E");
                                    }
                                }
                            }
                        }

                    }

                    if (sOK.equals("S")){ //se Usuarioe senha estao corretos
                        if (!suser.equals("HMINFO") && !suser.equals("CIELO")) {
                            Nome_user = suser;
                            Atualizacoes(suser);
                        } else {
                            Intent it = getIntent();

                            it.putExtra("user", suser);
                            it.putExtra("Activity_Dados", "0");

                            setResult(RESULT_OK, it);
                            // importante para voltar a primeira Activity pai
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Usuario ou Senha Invalidos.", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Usuario Invalido.", Toast.LENGTH_LONG).show();
                }



            }
        });


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
                DB_EMP dbempws = new DB_EMP(LoginActivity.this);
                String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                String susr = Nome_user;
                DB_USR dbu = new DB_USR(LoginActivity.this);
                String sultatu = dbu.Busca_Dados_Usr(susr, "Ultatu");
                String sidU = dbu.Busca_Dados_Usr(susr, "ID");
                String sret = Busca_Atualizacoes(sendews, LoginActivity.this);
                if (!sret.equals("")) {
                    sret = sret.replace("m:", "");
                    try {
                        String sxml = sret;
                        File sdCard = getExternalFilesDir("Download");
                        File dir = new File(sdCard.getAbsolutePath() );
                        //dir.mkdirs();
                        File fileExt = new File(dir, "RetWSATUALIZA.xml");

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
                                File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWSATUALIZA.xml");
                                Document doc = dBuilder.parse(fXmlFile);
                                doc.getDocumentElement().normalize();
                                NodeList nodeResponse = doc.getElementsByTagName("busca_atualizacaoResponse");
                                for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                    Node nNode = nodeResponse.item(temp);
                                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eElement = (Element) nNode;

                                        NodeList nodeResult = doc.getElementsByTagName("busca_atualizacaoResult");
                                        for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {

                                            Node nresult = nodeResult.item(tempresult);
                                            Element resultElement = (Element) nresult;
                                            //////////////////////////////////////////////
                                            NodeList nodeTipo = doc.getElementsByTagName("tPercursos");
                                            for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                                                Node ntipo = nodeTipo.item(temptipo);
                                                Element tipoElement = (Element) ntipo;
                                                String sdatprog = tipoElement.getElementsByTagName("sDatatu").item(0).getTextContent();
                                                String sdadoscert = tipoElement.getElementsByTagName("sDadosCert").item(0).getTextContent();
                                                String sClientID = tipoElement.getElementsByTagName("sClientID").item(0).getTextContent();
                                                String sIDMobipix = tipoElement.getElementsByTagName("sMobipix").item(0).getTextContent();
                                                //System.out.println("Sdados: "+sdadoscert);
                                                if (!sdadoscert.equals("")) { //salvar chave privada do certificado
                                                    //dir.mkdirs();
                                                    File filekey = new File(dir, "KEYEMP.xml");

                                                    //Cria o arquivo
                                                    filekey.getParentFile().mkdirs();

                                                    //Abre o arquivo
                                                    FileOutputStream foskey = null;
                                                    foskey = new FileOutputStream(filekey);

                                                    //Escreve no arquivo
                                                    foskey.write(sdadoscert.getBytes());

                                                    //Obrigatoriamente você precisa fechar
                                                    foskey.close();
                                                }
                                                if (!sClientID.equals("")) {
                                                    //dir.mkdirs();
                                                    File filekey = new File(dir, "IDEMP.xml");

                                                    //Cria o arquivo
                                                    filekey.getParentFile().mkdirs();

                                                    //Abre o arquivo
                                                    FileOutputStream foskey = null;
                                                    foskey = new FileOutputStream(filekey);

                                                    //Escreve no arquivo
                                                    foskey.write(sClientID.getBytes());

                                                    //Obrigatoriamente você precisa fechar
                                                    foskey.close();
                                                }
                                                if (!sIDMobipix.equals("")) {
                                                    //dir.mkdirs();
                                                    File filekey = new File(dir, "MOBIPIX.xml");

                                                    //Cria o arquivo
                                                    filekey.getParentFile().mkdirs();

                                                    //Abre o arquivo
                                                    FileOutputStream foskey = null;
                                                    foskey = new FileOutputStream(filekey);

                                                    //Escreve no arquivo
                                                    foskey.write(sIDMobipix.getBytes());

                                                    //Obrigatoriamente você precisa fechar
                                                    foskey.close();
                                                }
                                                if (!sdatprog.equals("") && !sdatprog.equals(sultatu)) {
                                                    //String sdatatu = Funcoes_Android.getCurrentUTC();
                                                    String sdatP = sdatprog.substring(0, 19);
                                                    //String sdatA = sdatatu.substring(0, 10);
                                                    //boolean bdatavalida = Funcoes_Android.Data_Maior(sdatP, sdatA);
                                                    boolean bdatavalida = Funcoes_Android.Data_Hora(sdatP);
                                                    if (bdatavalida) { //se a data for maior ou igual a data da atualizacao
                                                        DB_PER dbper = new DB_PER(LoginActivity.this);
                                                        dbper.deletar_Per();
                                                        NodeList nodetar = doc.getElementsByTagName("tTarifas");
                                                        for (int temptar = 0; temptar < nodetar.getLength(); temptar++) {

                                                            NodeList nodeili = doc.getElementsByTagName("tSEP_ILI");
                                                            for (int tempili = 0; tempili < nodeili.getLength(); tempili++) {
                                                                Node nili = nodeili.item(tempili);
                                                                Element iliElement = (Element) nili;
                                                                String sLin = iliElement.getElementsByTagName("nLinha").item(0).getTextContent();
                                                                if (!sLin.equals("") && !sLin.equals("0")) {
                                                                    String Origem = iliElement.getElementsByTagName("nTreori").item(0).getTextContent();
                                                                    String Destino = iliElement.getElementsByTagName("nTredes").item(0).getTextContent();
                                                                    String Tarifa = iliElement.getElementsByTagName("nvlrTar").item(0).getTextContent();
                                                                    String Seguro = iliElement.getElementsByTagName("nvlrSeg").item(0).getTextContent();
                                                                    String Arredonda = iliElement.getElementsByTagName("nvlrArre").item(0).getTextContent();
                                                                    String Tipvia = iliElement.getElementsByTagName("sTipvia").item(0).getTextContent();
                                                                    dbper.InserirPercurso(sLin, Origem, Destino, Tarifa, Seguro, Arredonda, Tipvia);
                                                                    spassou = "S";

                                                                }
                                                            }


                                                        }

                                                        //Procurar Viagens
                                                        DB_VIA dbvia = new DB_VIA(LoginActivity.this);
                                                        dbvia.deletar_Via();
                                                        NodeList nodeviagens = doc.getElementsByTagName("tViagens");
                                                        Log.i("Atualiza","Quantas Viagens: " + nodeviagens.getLength());
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

                                                        dbu.Atualizar_Campo_Usr(sidU, "Ultatu", sdatprog);
                                                        Intent it = getIntent();

                                                        it.putExtra("user", Nome_user);
                                                        it.putExtra("Activity_Dados", "0");

                                                        setResult(RESULT_OK, it);
                                                        finish();
                                                    } else {
                                                        Intent it = getIntent();

                                                        it.putExtra("user", Nome_user);
                                                        it.putExtra("Activity_Dados", "0");

                                                        setResult(RESULT_OK, it);
                                                        finish();

                                                    }

                                                } else {
                                                    Intent it = getIntent();

                                                    it.putExtra("user", Nome_user);
                                                    it.putExtra("Activity_Dados", "0");

                                                    setResult(RESULT_OK, it);
                                                    finish();

                                                }
                                            }
                                        }
                                    }
                                }
                                if (spassou.equals("")) {
                                    Intent it = getIntent();

                                    it.putExtra("user", Nome_user);
                                    it.putExtra("Activity_Dados", "0");

                                    setResult(RESULT_OK, it);
                                    finish();

                                }
                            } catch (Exception e) {
                                Intent it = getIntent();

                                it.putExtra("user", Nome_user);
                                it.putExtra("Activity_Dados", "0");

                                setResult(RESULT_OK, it);
                                finish();

                            }
                        }
                    } catch (Exception e) {
                        Intent it = getIntent();

                        it.putExtra("user", Nome_user);
                        it.putExtra("Activity_Dados", "0");

                        setResult(RESULT_OK, it);
                        finish();

                    }
                } else {
                    Intent it = getIntent();

                    it.putExtra("user", Nome_user);
                    it.putExtra("Activity_Dados", "0");

                    setResult(RESULT_OK, it);
                    finish();

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





    //Funcao que chama Dialog para verificar se tem atualizacoes no WS
    public void Atualizacoes(final String suser){
        // Initializing a new alert dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.informa_atualiza, null);

        final EditText edtatualiza = view.findViewById(R.id.edtAtualiza);
        edtatualiza.setText("Verificando Atualizações.\nAguarde por favor.");
        edtatualiza.setEnabled(false);

        builder.setView(view)

                // Add action buttons
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
                .setCancelable(false);


        // Create the alert dialog
        android.app.AlertDialog alert = builder.create();
        alert.show();

        Button theButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setEnabled(false);
        ExecutBackgrund();



    }

    public abstract class DialogButtonClickWrapper implements View.OnClickListener {

        private android.app.AlertDialog dialog;

        public DialogButtonClickWrapper(android.app.AlertDialog dialog) {
            this.dialog = dialog;
        }


        public void onClick(View v) {

            if(onClicked()){
                dialog.dismiss();
            }
        }

        protected abstract boolean onClicked();
    }


    public static String Busca_Atualizacoes(String swsenvio, Context contexview) {

        String retorno = "";

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/busca_atualizacao";
            final String METODO = "busca_atualizacao";


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
                // System.out.println("Entrei no Try");
                androidHttpTransport.debug = true;

                androidHttpTransport.call(SOAP_ACTION + METODO, envelope);

                // SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();


                String resultString = androidHttpTransport.responseDump;


                // System.out.println("Retorno xml: "+resultString);
                if (resultString != null) {
                    retorno = resultString;
                    System.out.println("Exception xml: " + retorno);
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
                //System.out.println(e.toString());
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EditText edtUser = findViewById(R.id.edtUsuario);
        EditText edtSenha = findViewById(R.id.edtSenha);
        String suser = edtUser.getText().toString();
        suser = suser.toUpperCase();
        if (suser.equals("") || !suser.equals("HMINFO")){ //se ainda nao fez login
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }



}
