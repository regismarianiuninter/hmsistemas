package com.example.usuario.bilhete1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class WSActivity extends AppCompatActivity {

    private static String Nome_user = "";
    private static String Nome_Arquivo = "";
    private static String Retorno_WS = "";
    private static String Guarda_Texto = "";
    private static String LinviaWS = "";
    private static String DatviaWS = "";
    private static String NumcadWS = "";
    private static String TreoriWS = "";
    private static String TredesWS = "";
    private static String VlrtarWS = "";
    private static String VlrembWS = "";
    private static String VlrsegWS = "";
    private static String VlrarreWS = "";
    private static String SerieWS = "";
    private static String DatemiWS = "";
    private static String VeiculoWS = "";
    private static String AgenteWS = "";
    private static String TipviaWS = "";
    private static String PagWS = "";
    private static String WScomando = "";
    private static String MotivoWS = "";
    private static String URLWS = "";
    private static String CancelarWS = "";
    private static int GuardaQtd = 1;
    final Timer timer = new Timer();
    final Timer timerconsulta = new Timer();
    private static String Activity_Dados = "";
    private static String CPFUSR = "";
    private static String MESANO = "";
    private static String SENTIDO = "";

    private static String NOMEUSR = "";
    private static String DOCUSR = "";

    private static String TIPGRA = "";

    private ExecutorService serverw =  Executors.newSingleThreadExecutor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws);


        Intent Newintent = getIntent();

        Bundle bundle = Newintent.getExtras();

        String sarquivo = bundle.getString("XML");
        String user     = bundle.getString("USUARIO");
        String slinha   = bundle.getString("LINVIA");
        String sdatsai  = bundle.getString("DATVIA");
        String snumcad  = bundle.getString("NUMCAD");
        String streori  = bundle.getString("TREORI");
        String stredes  = bundle.getString("TREDES");
        String svlrtar  = bundle.getString("VLRTAR");
        String svlremb  = bundle.getString("VLREMB");
        String svlrseg  = bundle.getString("VLRSEG");
        String svlrarre = bundle.getString("VLRARRE");
        String sserie   = bundle.getString("SERIE");
        String sdatemi  = bundle.getString("DATEMI");
        String sveiculo = bundle.getString("VEICULO");
        String spag     = bundle.getString("PAGAMENTO");
        String stipvia  = bundle.getString("TIPVIA");
        String scomando  = bundle.getString("COMANDO");
        String smotivo  = bundle.getString("MOTIVO");
        String sUrlcon = bundle.getString("URLWS");
        String sCancel = bundle.getString("CANCEL");
        String scodigo = bundle.getString("Activity_Dados");
        String cpfusuario = bundle.getString("CPFUSR");
        String mesano = bundle.getString("MESANO");
        String ssentido = bundle.getString("SENTIDO");
        String snomeusr = bundle.getString("NOMEUSR");
        String sdocusr = bundle.getString("DOCUSR");
        String stipgra = bundle.getString("TIPGRA");

        Nome_Arquivo = sarquivo;
        Nome_user = user;
       // DB_USR dbu = new DB_USR(WSActivity.this);
      //  String usuario = dbu.Busca_Dados_Usr(user, "ID");
        System.out.println("Entrei na WSActivity Activity_Dados: " + scodigo);


        LinviaWS = slinha;
        DatviaWS = sdatsai;
        NumcadWS = snumcad;
        TreoriWS = streori;
        TredesWS = stredes;
        VlrtarWS = svlrtar;
        VlrembWS = svlremb;
        VlrsegWS = svlrseg;
        VlrarreWS = svlrarre;
        SerieWS = sserie;
        DatemiWS = sdatemi;
        VeiculoWS = sveiculo;
        AgenteWS = user;
        PagWS = spag;
        TipviaWS = stipvia;
        WScomando = scomando;
        MotivoWS = smotivo;
        URLWS = sUrlcon;
        CancelarWS = sCancel;
        Activity_Dados = scodigo;
        CPFUSR = cpfusuario;
        MESANO = mesano;
        SENTIDO = ssentido;
        NOMEUSR = snomeusr;
        DOCUSR = sdocusr;
        TIPGRA = stipgra;



        if (!scomando.equals("")) {
            if (!scomando.equals("CONEXAO")) {
                if (!scomando.equals("TOKEN")) {
                    int delay = 12000;   // delay de 5 seg.
                    if (!scomando.equals("KEY") && !scomando.equals("TARIFAS")) {
                        if (sCancel.equals("S")) {
                            delay = 14000;
                        }
                    } else {
                        delay = 25000;
                    }
                    int interval = 1000;  // intervalo de 1 seg.

                    System.out.println("Criando timer: ");
                    timer.schedule(new TimerTask() {
                        public void run() {
                            // colocar tarefas aqui ...
                            //System.out.println("Dentro do TimerTask: WS");
                            Intent i = new Intent();

                            i.putExtra("msg", "Tempo Excedido");
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }

                    }, delay, interval);

                    Retorno_WS = "";
                    ExecutBackgrund();
                } else { //Gerar Token PIX
                    Retorno_WS = "";
                    ExecutBackgrund();
                }//} else { //Gerar Token PIX
            } else {
                int delay = 5000;   // delay de 5 seg.
                int interval = 1000;  // intervalo de 1 seg.
                System.out.println("Criando timer: ");
                timerconsulta.schedule(new TimerTask() {
                    public void run() {
                        // colocar tarefas aqui ...
                        //System.out.println("Dentro do TimerTask: WS");
                        Intent i = new Intent();

                        i.putExtra("msg", "");
                        i.putExtra("Activity_Dados", Activity_Dados);
                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                        setResult(RESULT_OK, i);

                        // importante para voltar a primeira Activity pai
                        timerconsulta.cancel();
                        finish();
                    }

                }, delay, interval);


                boolean bcon = Funcoes_Android.Verifica_Conexao(WSActivity.this);
                System.out.println("Retornei do Verifica_Conexao");
                if (bcon == true) {
                    Intent i = new Intent();

                    i.putExtra("msg", "Conectado");
                    i.putExtra("Activity_Dados", Activity_Dados);
                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                    System.out.println("Conectado:");
                    setResult(RESULT_OK, i);

                    // importante para voltar a primeira Activity pai
                    timerconsulta.cancel();
                    finish();

                } else {
                    Intent i = new Intent();

                    i.putExtra("msg", "");
                    i.putExtra("Activity_Dados", Activity_Dados);
                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                    setResult(RESULT_OK, i);
                    System.out.println("Nao Conectado:");

                    // importante para voltar a primeira Activity pai
                    timerconsulta.cancel();
                    finish();
                }
            }

        }


    }


    public static class DadosUsuarioGratuidade {
        public String nome;
        public String documento;
        public String tipoGratuidade;
    }

    public static class RetornoWsConsultaUsuarioGratuidade{
        public String message;
        public DadosUsuarioGratuidade DadosUsuario;
        public String tErros;
    }


    private void ExecutBackgrund() {
        System.out.println("Entrei na ExecutBackgrund WSActivity Activity_Dados: "+Activity_Dados);
                serverw.submit(() -> {

                //doInBackGround method of AsyncTask (O que ira executar em segundo plano)
                // Esta etapa é usada para executar a tarefa em background ou fazer a chamada para o webservice
                String scomando = WScomando;
                String sret = "";
                if (scomando.equals("TRANSMITE") || scomando.equals("CONSULTA")) {
                    System.out.println("ExecutBackgrund Comando: "+scomando+" Activity_Dados: "+Activity_Dados);
                    String snome = Nome_Arquivo;
                    String schavebpe = snome.substring(0, 44);
                    String snumero = schavebpe.substring(25, 34);
                    ///////////////Ler conteudo do arquivo e mover para uma string
                    String svalida = "OK"; //Funcoes_Android.Validar_Xml(snome, LinviaWS, TreoriWS, TredesWS, DatviaWS);
                    //System.out.println(snome+" Validacao= "+svalida);
                    if (svalida.equals("OK")) { //so entrar se for mesma linha,data e hora,origem,destino
                        File arq;
                        String lstrlinha;
                        String stexto = "";
                        try {
                            File sdCard = getExternalFilesDir("Download");
                            arq = new File(sdCard.getAbsolutePath() + "/" + snome);
                            //arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
                            BufferedReader br = new BufferedReader(new FileReader(arq));

                            while ((lstrlinha = br.readLine()) != null) {
                                stexto = stexto + lstrlinha;
                            }


                        } catch (Exception e) {
                            System.out.println("Ex Lendo: " + e.toString());
                            e.printStackTrace();
                            Intent i = new Intent();

                            i.putExtra("msg", "x Lendo: " + e.toString());
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }

                        //////////////////////
                        DB_EMP dbempws = new DB_EMP(WSActivity.this);
                        String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                        System.out.println("Transmite 04 WS= :"+sendews+"Activit_dados"+Activity_Dados);
                        sret = "";
                        sret = ConsultaBPeWS.Consulta_Xml(sendews, stexto, schavebpe, LinviaWS, DatviaWS, NumcadWS, TreoriWS, TredesWS,
                                VlrtarWS, VlrembWS, VlrsegWS, VlrarreWS, SerieWS, DatemiWS, VeiculoWS, AgenteWS, TipviaWS, PagWS, WScomando,
                                "", CancelarWS, MotivoWS, getApplicationContext());
                        Retorno_WS = sret;
                        System.out.println("Transmite 05 Resultado ConsultaBPeWS: " + sret);
                        System.out.println("Transmite 05 Activity_Dados: " + Activity_Dados);
                        if (!sret.equals("")) {
                            timer.cancel();
                            sret = sret.replace("m:", "");
                            try {
                                String sxml = sret;

                                File sdCard = getExternalFilesDir("Download");
                                File dir = new File(sdCard.getAbsolutePath() );
                                //dir.mkdirs();
                                File fileExt = new File(dir, "RetWST.xml");

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
                                        String sautoriza = "Autorizado o uso do BP-e";
                                        String sautoriza2 = "Autorizado o uso do BP-e, autorizacao fora do prazo";
                                        String sautoriza3 = "Autorizado o uso do BP-e, autorização fora do prazo";
                                        String sautoriza4 = "Bilhete salvo com sucesso";
                                        String sautoriza5 = "Cancelamento de BP-e homologado";
                                        String sautoriza6 = "Autorizado o uso do BP-e, autorizaçao fora do prazo";
                                        if (sret.contains(sautoriza) || sret.contains(sautoriza2) || sret.contains(sautoriza3) || sret.contains(sautoriza4) || sret.contains(sautoriza5) || sret.contains(sautoriza6)) {
                                            timer.cancel();
                                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWST.xml");
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
                                                            String sMsgretws = tipoElement.getElementsByTagName("sRetorno").item(0).getTextContent();

                                                            Retorno_WS = sMsgretws;
                                                            System.out.println("Retorno_WS: " + sMsgretws);
                                                            String svai = "";
                                                            if (sMsgretws.equals("Autorizado o uso do BP-e")) {
                                                                svai = "S";
                                                            }
                                                            if (sMsgretws.equals("Autorizado o uso do BP-e, autorizacao fora do prazo")) {
                                                                svai = "S";
                                                            }
                                                            if (sMsgretws.equals("Autorizado o uso do BP-e, autorização fora do prazo")) {
                                                                svai = "S";
                                                            }
                                                            if (sMsgretws.equals("Bilhete salvo com sucesso")) {
                                                                svai = "S";
                                                            }
                                                            if (sMsgretws.equals("Cancelamento de BP-e homologado")) {
                                                                svai = "S";
                                                            }
                                                            if (svai.equals("S")) {
                                                                try {
                                                                    String sarqxml = Nome_Arquivo;
                                                                    String snumbpe = sarqxml.substring(25, 34);
                                                                    String schave = sarqxml.substring(0, 44);
                                                                    String snumbpechamada = schavebpe.substring(25, 34);
                                                                    if (snumbpe.equals(snumbpechamada)) { //somente se o retorno for do mesmo bilhete
                                                                        Guarda_Texto = schave;
                                                                        int iCodigo = Integer.parseInt(snumbpe);
                                                                        DB_BPE dbbpe = new DB_BPE(WSActivity.this);
                                                                        String IDBPE = dbbpe.Busca_Dados_Bpe(Integer.toString(iCodigo), "ID");
                                                                        if (!sMsgretws.equals("Bilhete salvo com sucesso") || !sMsgretws.equals("Cancelamento de BP-e homologado")) {

                                                                            sarqxml = sarqxml.replace("-bpe", "-procBPe");
                                                                            File sdCard2 = getExternalFilesDir("Download");
                                                                            File dir2 = new File(sdCard2.getAbsolutePath() );
                                                                            //dir.mkdirs();
                                                                            File fileExt2 = new File(dir2, sarqxml);

                                                                            //Cria o arquivo
                                                                            fileExt.getParentFile().mkdirs();

                                                                            //Abre o arquivo
                                                                            FileOutputStream fosExt2 = null;
                                                                            fosExt2 = new FileOutputStream(fileExt2);

                                                                            //Escreve no arquivo
                                                                            fosExt2.write(sarquivo.getBytes());

                                                                            //Obrigatoriamente você precisa fechar
                                                                            fosExt2.close();

                                                                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Sitbpe", "BA");//alterar situacao do bilhete como autorizado
                                                                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Transf", "S");//alterar como transferido
                                                                        } else {
                                                                            if (sMsgretws.equals("Cancelamento de BP-e homologado")) {
                                                                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Sitbpe", "CA");//alterar situacao do bilhete como autorizado
                                                                            }
                                                                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Transf", "S");//alterar como transferido

                                                                        }
                                                                        System.out.println("Vou finalizar o WSActivity: " + Activity_Dados);
                                                                        Intent i = new Intent();

                                                                        i.putExtra("msg", snumbpe + " - " + sMsgretws);

                                                                        i.putExtra("Activity_Dados", Activity_Dados);
                                                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                        setResult(RESULT_OK, i);

                                                                        // importante para voltar a primeira Activity pai
                                                                        timer.cancel();
                                                                        finish();

                                                                    } else {
                                                                        System.out.println("Retorno de outro Bilhete: ");
                                                                        Intent i = new Intent();

                                                                        i.putExtra("msg", "Retorno Inesperado: ");
                                                                        i.putExtra("Activity_Dados", Activity_Dados);
                                                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                        setResult(RESULT_OK, i);

                                                                        // importante para voltar a primeira Activity pai
                                                                        timer.cancel();
                                                                        finish();
                                                                    }
                                                                } catch (Exception e) {
                                                                    System.out.println("Ex Salvando: " + e.toString());
                                                                    e.printStackTrace();
                                                                    Intent i = new Intent();

                                                                    i.putExtra("msg", "Salvando: " + e.toString());
                                                                    i.putExtra("Activity_Dados", Activity_Dados);
                                                                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                    setResult(RESULT_OK, i);

                                                                    // importante para voltar a primeira Activity pai
                                                                    timer.cancel();
                                                                    finish();
                                                                }
                                                            } else if (!svai.equals("S")) {
                                                                Intent i = new Intent();

                                                                i.putExtra("msg", "sMsgretws: " + sMsgretws);
                                                                i.putExtra("Activity_Dados", Activity_Dados);
                                                                i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                setResult(RESULT_OK, i);

                                                                // importante para voltar a primeira Activity pai
                                                                timer.cancel();
                                                                finish();

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {//if (sret.contains(sautoriza) || sret.contains(sautoriza2))
                                            //System.out.println(schavebpe + "  Ret: " + sret);
                                            Intent i = new Intent();

                                            i.putExtra("msg", "Retorno Inesperado.");
                                            i.putExtra("Activity_Dados", Activity_Dados);
                                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                            setResult(RESULT_OK, i);

                                            // importante para voltar a primeira Activity pai
                                            timer.cancel();
                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Intent i = new Intent();

                                        i.putExtra("msg", "EX Lendo XML: " + e.toString());
                                        i.putExtra("Activity_Dados", Activity_Dados);
                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                        setResult(RESULT_OK, i);

                                        // importante para voltar a primeira Activity pai
                                        timer.cancel();
                                        finish();
                                    }
                                }


                                /////////////////////////////////////////////////
                            } catch (Exception e) {
                                //System.out.println("Ex Salvando: " + e.toString());
                                e.printStackTrace();
                                Intent i = new Intent();

                                i.putExtra("msg", "EX salvando proc: " + e.toString());
                                i.putExtra("Activity_Dados", Activity_Dados);
                                i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, i);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                            }
                        } else if (sret.equals("")) {
                            Intent i = new Intent();

                            i.putExtra("msg", "Sem retorno do WS");
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    }  else { //se nao for for mesma linha,data e hora,origem,destino
                        Intent i = new Intent();

                        i.putExtra("msg", "Tente mai tarde");
                        i.putExtra("Activity_Dados", Activity_Dados);
                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                        setResult(RESULT_OK, i);

                        // importante para voltar a primeira Activity pai
                        timer.cancel();
                        finish();
                    }
                }  else if (scomando.equals("CANCELA")) {
                    String snome = Nome_Arquivo;
                    String schavebpe = snome.substring(0, 44);
                    String snumero = schavebpe.substring(25, 34);
                    String snumbpe = Integer.toString(Integer.parseInt(snumero));
                    DB_EMP dbempws = new DB_EMP(WSActivity.this);
                    String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                    sret = ConsultaBPeWS.Cancela_BPe_Xml(sendews, snumbpe, SerieWS, schavebpe, MotivoWS);
                    Retorno_WS = sret;
                    System.out.println("Resultado: " + sret);
                    if (!sret.equals("")) {
                        timer.cancel();
                        sret = sret.replace("m:", "");
                        try {
                            String scancela = "Evento registrado e vinculado a BP-e";
                            String scancela2 = "Evento registrado e vinculado a BP-e, cancelamento fora do prazo";
                            String scancela3 = "Cancelamento de BP-e homologado";
                            if (sret.contains(scancela) || sret.contains(scancela2) || sret.contains(scancela3)) {
                                timer.cancel();
                                String sxml = sret;
                                File sdCard = getExternalFilesDir("Download");
                                File dir = new File(sdCard.getAbsolutePath() );
                                //dir.mkdirs();
                                File fileExt = new File(dir, "RetWSC.xml");

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
                                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWSC.xml");
                                        Document doc = dBuilder.parse(fXmlFile);
                                        doc.getDocumentElement().normalize();
                                        NodeList nodeResponse = doc.getElementsByTagName("xml_cancelaResponse");
                                        for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                            Node nNode = nodeResponse.item(temp);
                                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                                Element eElement = (Element) nNode;

                                                NodeList nodeResult = doc.getElementsByTagName("xml_cancelaResult");
                                                for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {


                                                    Node nresult = nodeResult.item(tempresult);
                                                    Element resultElement = (Element) nresult;
                                                    //////////////////////////////////////////////
                                                    NodeList nodeTipo = doc.getElementsByTagName("tRET_WBS");
                                                    for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                                                        Node ntipo = nodeTipo.item(temptipo);
                                                        Element tipoElement = (Element) ntipo;
                                                        String sarquivo = tipoElement.getElementsByTagName("sXml").item(0).getTextContent();
                                                        String sMsgretws = tipoElement.getElementsByTagName("sRetorno").item(0).getTextContent();
                                                        Retorno_WS = sMsgretws;
                                                        System.out.println("Retorno_WS: " + sMsgretws);
                                                        String svai = "";
                                                        if (sMsgretws.equals("Evento registrado e vinculado a BP-e")) {  svai = "S";}
                                                        if (sMsgretws.equals("Evento registrado e vinculado a BP-e, cancelamento fora do prazo")) {  svai = "S";}
                                                        if (sMsgretws.equals("Cancelamento de BP-e homologado")) { svai = "S";}
                                                        if (svai.equals("S")) {
                                                            try {
                                                                int iCodigo = Integer.parseInt(snumero);
                                                                DB_BPE dbbpe = new DB_BPE(WSActivity.this);
                                                                String IDBPE = dbbpe.Busca_Dados_Bpe(Integer.toString(iCodigo), "ID");
                                                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Sitbpe", "CA");//alterar situacao do bilhete como Cancelado
                                                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Rsv001", "S");
                                                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Rsv003", MotivoWS);


                                                                Intent i = new Intent();

                                                                i.putExtra("msg", sMsgretws);
                                                                i.putExtra("numbpe", Integer.toString(iCodigo));
                                                                i.putExtra("Activity_Dados", Activity_Dados);
                                                                i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                setResult(RESULT_OK, i);

                                                                // importante para voltar a primeira Activity pai
                                                                timer.cancel();
                                                                finish();


                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Intent i = new Intent();

                                                                i.putExtra("msg", "Retorno: " + e.toString());
                                                                i.putExtra("Activity_Dados", Activity_Dados);
                                                                i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                                setResult(RESULT_OK, i);

                                                                // importante para voltar a primeira Activity pai
                                                                timer.cancel();
                                                                finish();
                                                            }
                                                        } else if (!svai.equals("S")) {
                                                            Intent i = new Intent();

                                                            i.putExtra("msg", "Retorno: " + sMsgretws);
                                                            i.putExtra("Activity_Dados", Activity_Dados);
                                                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                            setResult(RESULT_OK, i);

                                                            // importante para voltar a primeira Activity pai
                                                            timer.cancel();
                                                            finish();

                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Intent i = new Intent();

                                        i.putExtra("msg", "EX Lendo XML: " + e.toString());
                                        i.putExtra("Activity_Dados", Activity_Dados);
                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                        setResult(RESULT_OK, i);

                                        // importante para voltar a primeira Activity pai
                                        timer.cancel();
                                        finish();
                                    }
                                }
                            } else { //if (sret.contains(scancela) || sret.contains(scancela2))
                                Intent i = new Intent();

                                i.putExtra("msg", "Retorno Inesperado");
                                i.putExtra("Activity_Dados", Activity_Dados);
                                i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, i);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                            }


                            /////////////////////////////////////////////////
                        } catch (Exception e) {
                            //System.out.println("Ex Salvando: " + e.toString());
                            e.printStackTrace();
                            Intent i = new Intent();

                            i.putExtra("msg", "EX salvando proc: " + e.toString());
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    } else if (sret.equals("")) {
                        Intent i = new Intent();

                        i.putExtra("msg", "Sem retorno do WS");
                        i.putExtra("Activity_Dados", Activity_Dados);
                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                        setResult(RESULT_OK, i);

                        // importante para voltar a primeira Activity pai
                        timer.cancel();
                        finish();
                    }

                } else if (scomando.equals("KEY")) {
                    DB_EMP dbempws = new DB_EMP(WSActivity.this);
                    String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                    sret = ConsultaBPeWS.Atualiza_Key(sendews, WSActivity.this);
                    Retorno_WS = sret;
                    System.out.println("Resultado: " + sret);
                    if (!sret.equals("")) {
                        timer.cancel();
                        sret = sret.replace("m:", "");
                        try {
                            timer.cancel();
                            String sxml = sret;
                            File sdCard = getExternalFilesDir("Download");
                            File dir = new File(sdCard.getAbsolutePath() );
                            //dir.mkdirs();
                            File fileExt = new File(dir, "RetWSKEY.xml");

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
                                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                    File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWSKEY.xml");
                                    Document doc = dBuilder.parse(fXmlFile);
                                    doc.getDocumentElement().normalize();
                                    NodeList nodeResponse = doc.getElementsByTagName("dados_certificadoResponse");
                                    for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                        Node nNode = nodeResponse.item(temp);
                                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement = (Element) nNode;

                                            NodeList nodeResult = doc.getElementsByTagName("dados_certificadoResult");
                                            for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {

                                                Node nresult = nodeResult.item(tempresult);
                                                Element resultElement = (Element) nresult;
                                                //////////////////////////////////////////////
                                                NodeList nodeTipo = doc.getElementsByTagName("tRET_WBS");
                                                for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                                                    Node ntipo = nodeTipo.item(temptipo);
                                                    Element tipoElement = (Element) ntipo;
                                                    String sarquivo = tipoElement.getElementsByTagName("sXml").item(0).getTextContent();
                                                    if (!sarquivo.equals("")) {
                                                        //dir.mkdirs();
                                                        File filekey = new File(dir, "KEYEMP.xml");

                                                        //Cria o arquivo
                                                        filekey.getParentFile().mkdirs();

                                                        //Abre o arquivo
                                                        FileOutputStream foskey = null;
                                                        foskey = new FileOutputStream(filekey);

                                                        //Escreve no arquivo
                                                        foskey.write(sarquivo.getBytes());

                                                        //Obrigatoriamente você precisa fechar
                                                        foskey.close();

                                                        Intent i = new Intent();
                                                        i.putExtra("msg", "");
                                                        i.putExtra("Activity_Dados", Activity_Dados);
                                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                        setResult(RESULT_OK, i);
                                                        timer.cancel();
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Intent i = new Intent();

                                    i.putExtra("msg", "EX Lendo XML: " + e.toString());
                                    i.putExtra("Activity_Dados", Activity_Dados);
                                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                    setResult(RESULT_OK, i);

                                    // importante para voltar a primeira Activity pai
                                    timer.cancel();
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent i = new Intent();

                            i.putExtra("msg", "EX Lendo XML: " + e.toString());
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    } else {
                        Intent i = new Intent();

                        i.putExtra("msg", "sMsgretws: " + "Nao encontrei Arquivo");
                        i.putExtra("Activity_Dados", Activity_Dados);
                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                        setResult(RESULT_OK, i);

                        // importante para voltar a primeira Activity pai
                        timer.cancel();
                        finish();
                    }
                } else if (scomando.equals("TARIFAS")) {
                    DB_EMP dbempws = new DB_EMP(WSActivity.this);
                    String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                    sret = ConsultaBPeWS.Atualiza_Tarifas(sendews, WSActivity.this);
                    Retorno_WS = sret;
                    System.out.println("Resultado: " + sret);

                    if (!sret.equals("")) {
                        timer.cancel();
                        sret = sret.replace("m:", "");
                        try {
                            timer.cancel();
                            String sxml = sret;
                            File sdCard = getExternalFilesDir("Download");
                            File dir = new File(sdCard.getAbsolutePath() );
                            //dir.mkdirs();
                            File fileExt = new File(dir, "RetWSTARIFAS.xml");

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
                                    File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/RetWSTARIFAS.xml");
                                    Document doc = dBuilder.parse(fXmlFile);
                                    doc.getDocumentElement().normalize();
                                    NodeList nodeResponse = doc.getElementsByTagName("busca_tarifaResponse");
                                    for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                                        Node nNode = nodeResponse.item(temp);
                                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement = (Element) nNode;

                                            NodeList nodeResult = doc.getElementsByTagName("busca_tarifaResult");
                                            for (int tempresult = 0; tempresult < nodeResult.getLength(); tempresult++) {

                                                Node nresult = nodeResult.item(tempresult);
                                                Element resultElement = (Element) nresult;
                                                //////////////////////////////////////////////
                                                NodeList nodeTipo = doc.getElementsByTagName("tPercursos");
                                                for (int temptipo = 0; temptipo < nodeTipo.getLength(); temptipo++) {
                                                    Node ntipo = nodeTipo.item(temptipo);
                                                    Element tipoElement = (Element) ntipo;
                                                    String sDatatu = tipoElement.getElementsByTagName("sDatatu").item(0).getTextContent();
                                                    if (!sDatatu.equals("")) {
                                                        DB_PER dbper = new DB_PER(WSActivity.this);
                                                        dbper.deletar_Per();
                                                        NodeList nodetar = doc.getElementsByTagName("tTarifas");
                                                        for (int temptar = 0; temptar < nodetar.getLength(); temptar++) {

                                                            NodeList nodeili = doc.getElementsByTagName("tSEP_ILI");
                                                            for (int tempili = 0; tempili < nodeili.getLength(); tempili++) {
                                                                Node nili = nodeili.item(tempili);
                                                                Element iliElement = (Element) nili;
                                                                String sLin = iliElement.getElementsByTagName("nLinha").item(0).getTextContent();
                                                                if (!sLin.equals("") && !sLin.equals("0")) {
                                                                    String Origem    = iliElement.getElementsByTagName("nTreori").item(0).getTextContent();
                                                                    String Destino   = iliElement.getElementsByTagName("nTredes").item(0).getTextContent();
                                                                    String Tarifa    = iliElement.getElementsByTagName("nvlrTar").item(0).getTextContent();
                                                                    String Seguro    = iliElement.getElementsByTagName("nvlrSeg").item(0).getTextContent();
                                                                    String Arredonda = iliElement.getElementsByTagName("nvlrArre").item(0).getTextContent();
                                                                    String Tipvia    = iliElement.getElementsByTagName("sTipvia").item(0).getTextContent();
                                                                    dbper.InserirPercurso(sLin, Origem, Destino, Tarifa, Seguro, Arredonda, Tipvia);
                                                                    spassou = "S";

                                                                }
                                                            }


                                                        }
                                                        Intent i = new Intent();
                                                        i.putExtra("msg", "");
                                                        i.putExtra("Activity_Dados", Activity_Dados);
                                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                        setResult(RESULT_OK, i);
                                                        timer.cancel();
                                                        finish();

                                                    } else {
                                                        Intent i = new Intent();
                                                        i.putExtra("msg", "");
                                                        i.putExtra("Activity_Dados", Activity_Dados);;
                                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                                        setResult(RESULT_OK, i);
                                                        timer.cancel();
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (spassou.equals("")) {
                                        Intent i = new Intent();
                                        i.putExtra("msg", "");
                                        i.putExtra("Activity_Dados", Activity_Dados);
                                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                        setResult(RESULT_OK, i);
                                        timer.cancel();
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Intent i = new Intent();

                                    i.putExtra("msg", "EX Lendo XML: " + e.toString());
                                    i.putExtra("Activity_Dados", Activity_Dados);
                                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                    setResult(RESULT_OK, i);

                                    // importante para voltar a primeira Activity pai
                                    timer.cancel();
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent i = new Intent();

                            i.putExtra("msg", "EX Lendo XML: " + e.toString());
                            i.putExtra("Activity_Dados", Activity_Dados);
                            i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, i);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    } else {
                        Intent i = new Intent();

                        i.putExtra("msg", "sMsgretws: " + "Nao encontrei Arquivo");
                        i.putExtra("Activity_Dados", Activity_Dados);
                        i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                        setResult(RESULT_OK, i);

                        // importante para voltar a primeira Activity pai
                        timer.cancel();
                        finish();
                    }
                } else if (scomando.equals("TOKEN")) {
                    File dircert = new File (getExternalFilesDir("Download").getAbsolutePath());
                    String scertificado = dircert+"/certificadotoken.pfx";
                    //sret = ConsomeAPI_BAN.getAccessToken("08850bec-a8dc-492f-b7eb-fcf079136aac", "0276f36d-9663-4b28-993a-f8fb945e5c9a", "https://api-pix.banestes.b.br", "/oauth/v1/access-token", scertificado, "@HMinfo@0824");
                    sret = ConsomeAPI_BAN.geraToken("https://api-pix.banestes.b.br", "/oauth/v1/access-token", "08850bec-a8dc-492f-b7eb-fcf079136aac", "0276f36d-9663-4b28-993a-f8fb945e5c9a", scertificado);
                    Intent i = new Intent();

                    i.putExtra("msg", "sMsgretws Retorno Token: " + sret);
                    i.putExtra("Activity_Dados", Activity_Dados);
                    i.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                    System.out.println("sMsgretws Retorno Token: "+sret);
                    setResult(RESULT_OK, i);

                    // importante para voltar a primeira Activity pai
                    timer.cancel();
                    finish();
                } else if (scomando.equals("verificaUsuarioGratuidade")) {
                    String cpfcon = CPFUSR;
                    String spassou = "";
                    String scpf = CPFUSR;
                    String smesano = MESANO;
                    String ssentido = SENTIDO;
                    String linha = LinviaWS;
                    String nomeusr = NOMEUSR;
                    String docusr = DOCUSR;
                    String tipgra = TIPGRA;
                    DB_EMP dbempws = new DB_EMP(WSActivity.this);
                    String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                    try {
                        sret = ConsultaBPeWS.Consulta_Gratuidade(sendews, WSActivity.this, cpfcon);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Retorno_WS = sret;
                    System.out.println("Resultado: " + sret);
                    if (!sret.equals("")) {
                        try {
                            JSONArray jsonObject = new JSONArray(sret);
                            JSONObject jsonResponse = jsonObject.getJSONObject(0);
                            Boolean bErro = Boolean.parseBoolean(jsonResponse.getString("tErros"));

                            if (!bErro) {
                                ///Atualizar Banco de dados
                                String susergra = "";


                                DB_GRA db_gra = new DB_GRA(WSActivity.this);
                                DB_GRA.GraCursor cursor = db_gra.RetornarGra(DB_GRA.GraCursor.OrdenarPor.NomeCrescente);
                                for (int ib = 0; ib < cursor.getCount(); ib++) {
                                    cursor.moveToPosition(ib);
                                    String scpfDB = cursor.getCpfpas();
                                    String smesDB = cursor.getMesano();
                                    String slinhaDB = cursor.getLinha();
                                    String ssentidoDB = cursor.getSentido();
                                    if (!scpfDB.equals("")) { //se encontrou algumr egistro
                                        if (scpfDB.equals(scpf) && slinhaDB.equals(linha) && smesDB.equals(smesano) &&ssentidoDB.equals(ssentido)) {//se encontrou mesmos dados
                                            susergra = "S";
                                        }
                                    }
                                }


                                if (!susergra.equals("S")) {
                                    //se nao existir bo banco de dados, cadastrar
                                    DB_GRA newgra = new DB_GRA(WSActivity.this);
                                    newgra.InserirGra(scpf, nomeusr, docusr, LinviaWS, ssentido, tipgra, "", smesano, "0", "0", "0");
                                }

                                Intent ig = new Intent();

                                ig.putExtra("msg", "Lendo XML Gratuidade: ");
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("NOMEUSR", nomeusr);
                                ig.putExtra("DOCUSE", docusr);
                                ig.putExtra("TIPGRA", tipgra);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                                spassou = "S";

                            } else {
                                String message = jsonResponse.getJSONObject("tRetDadosUsr").getString("tipoGratuidade");
                                Intent ig = new Intent();

                                ig.putExtra("msg", "EX Lendo XML Gratuidade: " + message);
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                                spassou = "S";
                            }

                            if (spassou.equals("")) {
                                Intent ig = new Intent();

                                ig.putExtra("msg", "EX Lendo XML Gratuidade: ");
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                            }

                        } catch (Exception e) {
                            Intent ig = new Intent();

                            ig.putExtra("msg", "EX Lendo XML Gratuidade: "+e.toString());
                            ig.putExtra("Activity_Dados", Activity_Dados);
                            ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, ig);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    }


                } else if (scomando.equals("CONSULTA_GRATUIDADE")) {
                    String cpfcon = CPFUSR;
                    String spassou = "";
                    String scpf = CPFUSR;
                    String smesano = MESANO;
                    String ssentido = SENTIDO;
                    String linha = LinviaWS;
                    String nomeusr = NOMEUSR;
                    String docusr = DOCUSR;
                    String tipgra = TIPGRA;
                    DB_EMP dbempws = new DB_EMP(WSActivity.this);
                    String sendews = dbempws.Busca_Dados_Emp(1, "Endews");
                    try {
                        sret = ConsultaBPeWS.Controla_Gratuidade(sendews, "C", smesano, linha, ssentido, scpf, nomeusr, "", "", "", "", "", docusr, tipgra, 0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Retorno_WS = sret;
                    System.out.println("Resultado: " + sret);
                    if (!sret.equals("")) {
                        try {
                            int iqtd = Integer.parseInt(sret);

                            if (iqtd>0) {
                                ///Atualizar Banco de dados
                                String susergra = "";


                                DB_GRA db_gra = new DB_GRA(WSActivity.this);
                                DB_GRA.GraCursor cursor = db_gra.RetornarGra(DB_GRA.GraCursor.OrdenarPor.NomeCrescente);
                                for (int ib = 0; ib < cursor.getCount(); ib++) {
                                    cursor.moveToPosition(ib);
                                    String scpfDB = cursor.getCpfpas();
                                    String smesDB = cursor.getMesano();
                                    String slinhaDB = cursor.getLinha();
                                    String ssentidoDB = cursor.getSentido();
                                    if (!scpfDB.equals("")) { //se encontrou algumr egistro
                                        if (scpfDB.equals(scpf) && slinhaDB.equals(linha) && smesDB.equals(smesano) &&ssentidoDB.equals(ssentido)) {//se encontrou mesmos dados
                                            susergra = "S";
                                            String sqtdDB = cursor.getQtdpas();
                                            int iqtdDB = Integer.parseInt(sqtdDB);
                                            if (iqtd > iqtdDB) { //atualizar quantidade no banco de dados
                                                String sidgra = String.valueOf(cursor.getID()) ;
                                                db_gra.Atualizar_Campo_Gra(sidgra, "Qtdpas", sret);
                                            }
                                        }
                                    }
                                }


                                if (!susergra.equals("S")) {
                                    //se nao existir bo banco de dados, cadastrar
                                    DB_GRA newgra = new DB_GRA(WSActivity.this);
                                    newgra.InserirGra(scpf, nomeusr, docusr, LinviaWS, ssentido, tipgra, "", smesano, sret, "0", "0");
                                }

                                Intent ig = new Intent();

                                ig.putExtra("msg", "Lendo XML Gratuidade: ");
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("NOMEUSR", nomeusr);
                                ig.putExtra("DOCUSE", docusr);
                                ig.putExtra("TIPGRA", tipgra);
                                ig.putExtra("QTDUSE", sret);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                                spassou = "S";

                            } else {
                                Intent ig = new Intent();

                                ig.putExtra("msg", "EX Lendo XML Gratuidade: " + sret);
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                                spassou = "S";
                            }

                            if (spassou.equals("")) {
                                Intent ig = new Intent();

                                ig.putExtra("msg", "EX Lendo XML Gratuidade: ");
                                ig.putExtra("Activity_Dados", Activity_Dados);
                                ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                                setResult(RESULT_OK, ig);

                                // importante para voltar a primeira Activity pai
                                timer.cancel();
                                finish();
                            }

                        } catch (Exception e) {
                            Intent ig = new Intent();

                            ig.putExtra("msg", "EX Lendo XML Gratuidade: "+e.toString());
                            ig.putExtra("Activity_Dados", Activity_Dados);
                            ig.putExtra("XML", Nome_Arquivo); // Retorna o XML original
                            setResult(RESULT_OK, ig);

                            // importante para voltar a primeira Activity pai
                            timer.cancel();
                            finish();
                        }
                    }
                }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverw.shutdown();
    }



}
