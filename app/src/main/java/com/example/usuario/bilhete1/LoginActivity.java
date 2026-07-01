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

    private static String Novo_Usuario = "";
    private static boolean Atualizando_Usuarios = false;
    private static android.app.AlertDialog alert;


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
                Novo_Usuario = "";
                Atualizando_Usuarios = false;
                if (!suser.equals("")) {
                    String sUsuario = "";
                    DB_USR dbu = new DB_USR(LoginActivity.this);
                    DB_USR.UsrCursor cursor = dbu.RetornarUsr(DB_USR.UsrCursor.OrdenarPor.NomeCrescente);
                    String sOK = "";
                    String satuusr = "";
                    for( int i=0; i <cursor.getCount(); i++)
                    {
                        cursor.moveToPosition(i);
                        sUsuario = cursor.getUsrnom();
                        if (!sUsuario.equals("")){ //se encontrou algumr egistro
                            if (sUsuario.equals(suser)){//se encontrou o usuario digitado
                                String usrsen = cursor.getUsrsen();
                                if (ssenha.equals(usrsen)) { //Senha Confere
                                    sOK = "S";
                                    satuusr = cursor.getAtuusr();
                                    DB_EMP dbemplog = new DB_EMP(LoginActivity.this);
                                    if (!sUsuario.equals("HMINFO")) { //primeiro acesso provoca erro
                                        if (sUsuario.equals("CAIXA")) { //se for usuario caixa marca ponto de venda como Rodoviaria
                                            dbemplog.Atualizar_Campo_Emp("1", "Pvenda", "R");
                                        } else { //caso contrario marca como Estrada
                                            dbemplog.Atualizar_Campo_Emp("1", "Pvenda", "E");
                                        }
                                    }
                                }
                            }
                        }

                    }

                    if (sOK.equals("S")){ //se Usuario e senha estao corretos
                        if (!suser.equals("HMINFO") && !suser.equals("CIELO")) {
                            Nome_user = suser;
                            if ("S".equals(satuusr)) {
                                confirmarAtualizacaoUsuarios();
                            } else {
                                Atualizacoes(suser);
                            }
                        } else {
                            Intent it = getIntent();

                            it.putExtra("user", suser);
                            it.putExtra("Activity_Dados", "0");

                            setResult(RESULT_OK, it);
                            // importante para voltar a primeira Activity pai
                            finish();
                        }
                    }else {
                       // Novo_Usuario = suser;
                       // confirmarUsuario();
                        Toast.makeText(LoginActivity.this, "Usuario ou Senha Invalidos.", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(LoginActivity.this, "Usuario Invalido.", Toast.LENGTH_LONG).show();
                }



            }
        });




    }

    private void confirmarUsuario() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Usuário não encontrado.")
                .setMessage("Deseja buscar atualização no servidor?")
                .setPositiveButton("Sim", (d, w) -> {
                    Atualizacoes("");
                })
                .setNegativeButton("Cancelar", (d, w) -> {
                    Toast.makeText(this, "Usuario Invalido.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }


    private void confirmarAtualizacaoUsuarios() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Atualizar usuarios")
                .setMessage("Deseja consultar novos usuarios no servidor?")
                .setPositiveButton("Sim", (d, w) -> {
                    Atualizando_Usuarios = true;
                    Novo_Usuario = Nome_user;
                    Atualizacoes(Nome_user);
                })
                .setNegativeButton("Nao", (d, w) -> {
                    Atualizando_Usuarios = false;
                    Novo_Usuario = "";
                    Atualizacoes(Nome_user);
                })
                .show();
    }

    private void finalizarLogin(String usuario) {
        Intent it = getIntent();

        it.putExtra("user", usuario);
        it.putExtra("Activity_Dados", "0");

        setResult(RESULT_OK, it);
        finish();
    }

    private void avisarUsuariosIncluidos(final int incluidos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Usuarios novos incluidos: " + incluidos, Toast.LENGTH_LONG).show();
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
                String sultatu = "";
                String sidU = "";
                if (!susr.equals("")) {
                    sultatu = dbu.Busca_Dados_Usr(susr, "Ultatu");
                    sidU = dbu.Busca_Dados_Usr(susr, "ID");
                }
                if (!Atualizando_Usuarios) {
                    String sret = Busca_Atualizacoes(sendews, LoginActivity.this);
                    if (!sret.equals("")) {
                        sret = sret.replace("m:", "");
                        try {
                            String sxml = sret;
                            File sdCard = getExternalFilesDir("Download");
                            File dir = new File(sdCard.getAbsolutePath());
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
                                                    String satuserv = tipoElement.getElementsByTagName("sAtuserv").item(0).getTextContent();
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
                                                    /////PROCURAR TIPO DE SERVIÇO
                                                    if (satuserv.equals("S")) {
                                                        DB_SER dbser = new DB_SER(LoginActivity.this);
                                                        dbser.deletar_SER();
                                                        NodeList nodeserv = doc.getElementsByTagName("tServicos");
                                                        for (int tempser = 0; tempser < nodeserv.getLength(); tempser++) {

                                                            NodeList nodetps = doc.getElementsByTagName("tSEP_TPS");
                                                            for (int temptps = 0; temptps < nodetps.getLength(); temptps++) {
                                                                Node ntps = nodetps.item(temptps);
                                                                Element tpsElement = (Element) ntps;
                                                                String scodigo = tpsElement.getElementsByTagName("iCodigo").item(0).getTextContent();
                                                                if (!scodigo.equals("") && !scodigo.equals("0")) {
                                                                    String Tipser = tpsElement.getElementsByTagName("sTipser").item(0).getTextContent();
                                                                    String Descri = tpsElement.getElementsByTagName("sDescri").item(0).getTextContent();
                                                                    String Valor = tpsElement.getElementsByTagName("nValor").item(0).getTextContent();
                                                                    Tipser = Tipser + "-" + Descri;
                                                                    dbser.InserirSer(Tipser, Valor);
                                                                    spassou = "S";

                                                                }
                                                            }


                                                        }
                                                    }




                                                    NodeList nodeDadospix = doc.getElementsByTagName("tPegapix");
                                                    for (int temppix = 0; temppix < nodeDadospix.getLength(); temppix++) {

                                                        NodeList nodepix = doc.getElementsByTagName("tDADOS_PIX");
                                                        for (int tempdadospix = 0; tempdadospix < nodepix.getLength(); tempdadospix++) {
                                                            Node npix = nodepix.item(tempdadospix);
                                                            Element pixElement = (Element) npix;
                                                            String swspix = pixElement.getElementsByTagName("surlpdr").item(0).getTextContent();
                                                            if (!swspix.equals("") && !swspix.equals("0")) {
                                                                String scliid = pixElement.getElementsByTagName("scliid").item(0).getTextContent();
                                                                String sclisec = pixElement.getElementsByTagName("sclisec").item(0).getTextContent();
                                                                String schvpix = pixElement.getElementsByTagName("schvpix").item(0).getTextContent();
                                                                String ssencer = pixElement.getElementsByTagName("ssencer").item(0).getTextContent();
                                                                String snomcer = pixElement.getElementsByTagName("snomcer").item(0).getTextContent();
                                                                dbempws.Atualizar_Campo_Emp("1", "Banwse", swspix);
                                                                dbempws.Atualizar_Campo_Emp("1", "Cliidb", scliid);
                                                                dbempws.Atualizar_Campo_Emp("1", "Clisec", sclisec);
                                                                dbempws.Atualizar_Campo_Emp("1", "Banchv", schvpix);
                                                                dbempws.Atualizar_Campo_Emp("1", "Bansen", ssencer);
                                                                dbempws.Atualizar_Campo_Emp("1", "Bancrt", snomcer);
                                                                spassou = "S";

                                                            }
                                                        }


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
                                                            Log.i("Atualiza", "Quantas Viagens: " + nodeviagens.getLength());
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
                                                            if (!sidU.equals("")) {
                                                                dbu.Atualizar_Campo_Usr(sidU, "Ultatu", sdatprog);
                                                            }
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
                } else {
                    //Buscar por novos usuarios
                    String sret = Busca_Usuarios(sendews, LoginActivity.this);
                    if (!sret.equals("")) {
                        sret = sret.replace("m:", "");
                        try {
                            //Salvar XML com retorno
                            String sxml = sret;
                            File sdCard = getExternalFilesDir("Download");
                            File dir = new File(sdCard.getAbsolutePath());
                            //dir.mkdirs();
                            File fileExt = new File(dir, "RetAtualiza_Usr.xml");

                            //Cria o arquivo
                            fileExt.getParentFile().mkdirs();

                            //Abre o arquivo
                            FileOutputStream fosExt = null;
                            fosExt = new FileOutputStream(fileExt);

                            //Escreve no arquivo
                            fosExt.write(sxml.getBytes());

                            //Obrigatoriamente você precisa fechar
                            fosExt.close();

                            ////////////////////////
                            if (!sret.equals("")) { // so entra se retornou xml do ws
                                try {
                                    String spassou = "";
                                    int usuariosIncluidos = 0;
                                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                    File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetAtualiza_Usr.xml");
                                    Document doc = dBuilder.parse(fXmlFile);
                                    doc.getDocumentElement().normalize();
                                    NodeList nodeResponse = doc.getElementsByTagName("lista_AgentesResponse");
                                    for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                        Node nNode = nodeResponse.item(temp);
                                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement = (Element) nNode;

                                            NodeList nodeResult = doc.getElementsByTagName("lista_AgentesResult");
                                            for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {

                                                Node nresult = nodeResult.item(tempresult);
                                                Element resultElement = (Element) nresult;
                                                //////////////////////////////////////////////
                                                /////PROCURAR USUARIOS
                                                DB_USR dbuser = new DB_USR(LoginActivity.this);

                                                    NodeList nodeusr = doc.getElementsByTagName("tSEP_AGE");
                                                    for (int tempusr = 0; tempusr < nodeusr.getLength(); tempusr++) {
                                                        Node nusr = nodeusr.item(tempusr);
                                                        Element usrElement = (Element) nusr;
                                                        String snome = usrElement.getElementsByTagName("sNome").item(0).getTextContent().trim();
                                                        String ssenha = usrElement.getElementsByTagName("sSenha").item(0).getTextContent().trim();
                                                        String sfectur = usrElement.getElementsByTagName("sFectur").item(0).getTextContent().trim();
                                                        String stipage = "";
                                                        NodeList nodeTipusr = usrElement.getElementsByTagName("sTipusr");
                                                        if (nodeTipusr.getLength() > 0 && nodeTipusr.item(0) != null) {
                                                            stipage = nodeTipusr.item(0).getTextContent().trim();
                                                        }
                                                        String stippvd = "";
                                                        if (stipage.equals("O")) {
                                                            stippvd = "S";
                                                        } //Tipo Operador indica que abre outra tela de PVD
                                                        if (!dbuser.UsuarioExiste(snome)) {
                                                            dbuser.InserirUsr(snome, ssenha, sfectur, "", stippvd, "");
                                                            usuariosIncluidos++;
                                                            spassou = "S";
                                                        }
                                                    }



                                            }
                                        }
                                    }


                                    if (spassou.equals("")) {
                                        alert.cancel();
                                        avisarUsuariosIncluidos(usuariosIncluidos);
                                        finalizarLogin(Nome_user);

                                    } else {
                                        avisarUsuariosIncluidos(usuariosIncluidos);
                                        finalizarLogin(Nome_user);
                                    }
                                } catch (Exception e) {
                                    alert.cancel();
                                    finalizarLogin(Nome_user);

                                }
                            } else {
                                alert.cancel();
                                finalizarLogin(Nome_user);
                            }

                           } catch (Exception e) {
                            alert.cancel();
                            finalizarLogin(Nome_user);

                        }
                    } else {
                        alert.cancel();
                        finalizarLogin(Nome_user);
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
        alert = builder.create();
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


    public static String Busca_Usuarios(String swsenvio, Context contexview) {

        String retorno = "";

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/lista_Agentes";
            final String METODO = "lista_Agentes";


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
