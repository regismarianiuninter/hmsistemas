package com.example.usuario.bilhete1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import org.kobjects.mime.Decoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLProtocolException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.net.NetworkInfo.State.CONNECTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class Funcoes_Android {


    static String getCurrentUTC(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String data_completa = dateFormat.format(data_atual);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat date = new SimpleDateFormat("ZZZZZ",Locale.getDefault());
        String localTime = date.format(currentLocalTime);




        return (data_completa+localTime);

    }

    public static String Adicionar_Dias(String dataini, int idias) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date a = new Date();
        try {
            a = inputFormat.parse(dataini);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(a);
        c.add(Calendar.DATE, +idias);
        a = c.getTime();
        String sdataatu = inputFormat.format(a);
        return  sdataatu;
    }

    //comparar o intervalo entre duas horas em minutos
    public static long validaDate(String horaInicial, String horaFinal) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();

            cal.setTime(sdf.parse(horaInicial)); //Transforma hora ini em milisegundos
            calFinal.setTime(sdf.parse(horaFinal)); //Transforma hora fin em milisegundos
            long horas = (calFinal.getTimeInMillis() - cal.getTimeInMillis()) / 60000; //transforma
            long diferenca = horas;

            return diferenca;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //comparar o intervalo entre duas horas em Segundos
    public static long validaDate_Seg(String horaInicial, String horaFinal) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();

            cal.setTime(sdf.parse(horaInicial)); //Transforma hora ini em milisegundos
            calFinal.setTime(sdf.parse(horaFinal)); //Transforma hora fin em milisegundos
            long horas = (calFinal.getTimeInMillis() - cal.getTimeInMillis()) / 1000; //transforma
            long diferenca = horas;

            return diferenca;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean Verifica_Datas(String datamaior, String datamenor) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar dataM = new GregorianCalendar();
        Calendar datam = new GregorianCalendar();
        try {
            dataM.setTime(format.parse(datamaior));
            datam.setTime(format.parse(datamenor));


            if ((datam.getTimeInMillis() == dataM.getTimeInMillis()) || (datam.getTimeInMillis() < dataM.getTimeInMillis())) {
                return true;
            }
            else {
                return  false;
            }
        } catch (ParseException ps) {
            System.out.println("Erro na comparacao: "+ps.toString());
            return false;
        }
    }

    public static boolean Data_Maior(String datprog, String datatu) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar dataP = new GregorianCalendar();
        Calendar dataA = new GregorianCalendar();
        try {
            dataP.setTime(format.parse(datprog)); //data programada
            dataA.setTime(format.parse(datatu)); //data atual


            if ((dataA.getTimeInMillis() == dataP.getTimeInMillis()) || (dataA.getTimeInMillis() > dataP.getTimeInMillis())) {
                return true;
            }
            else {
                return  false;
            }
        } catch (ParseException ps) {
            System.out.println("Erro na comparacao: "+ps.toString());
            return false;
        }
    }

    public static boolean Data_Hora(String datprog) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String datatu = format.format(data_atual);

        Calendar dataP = new GregorianCalendar();
        Calendar dataA = new GregorianCalendar();
        try {
            dataP.setTime(format.parse(datprog)); //data programada
            dataA.setTime(format.parse(datatu)); //data atual


            if ((dataA.getTimeInMillis() == dataP.getTimeInMillis()) || (dataA.getTimeInMillis() > dataP.getTimeInMillis())) {
                return true;
            }
            else {
                return  false;
            }
        } catch (ParseException ps) {
            System.out.println("Erro na comparacao: "+ps.toString());
            return false;
        }
    }


    public static boolean Data_Hora_Cancel(String sdatemi, int iqtd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String datatu = format.format(data_atual);

        Calendar dataE = new GregorianCalendar();
        Calendar dataA = new GregorianCalendar();
        try {
            dataE.setTime(format.parse(sdatemi)); //data de emissao
            dataA.setTime(format.parse(datatu)); //data atual

            //int itime = Integer.parseInt(dataA.getTimeInMillis() - dataE.getTimeInMillis());
            if ((dataA.getTimeInMillis() <= (dataE.getTimeInMillis())+iqtd) ) {
                return true;
            }
            else {
                return  false;
            }
        } catch (ParseException ps) {
            System.out.println("Erro na comparacao: "+ps.toString());
            return false;
        }
    }




    /*public static boolean netWorkdisponibilidade(Context cont){
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if(conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
            System.out.println("Achei Wi-Fi");
            conectado = true;
        }
        //Verifica o 3G
        else if(conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            System.out.println("Achei 3G");
            if (conmag.getActiveNetworkInfo().getState() == CONNECTED) {
                System.out.println("3G Conectado");
                conectado = true;
            } else {
                System.out.println("3G sem conexao");
                conectado = false;
            }
        }
        else{
            conectado = false;
        }
        return conectado;
    }*/




    /*public static boolean Verifica_Conexao(Context contexto) {
        System.out.println("Entrei no Verifica_Conexao");
        boolean conectado = netWorkdisponibilidade(contexto);
        DB_EMP dbempurl = new DB_EMP(contexto);

        if (conectado == true) {
           // boolean checkUrl = getUrlDisponivel(dbempurl.Busca_Dados_Emp(1, "Endews"));
            System.out.println("Encontrei Internet: "+conectado);
            return conectado;
        } else {
            return false;
        }
    }*/


    public static boolean Verifica_Conexao(Context context) {
        System.out.println("Entrei no Verifica_Conexao");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        if(Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0)== 1)
        {
            System.out.println("Verifica_Conexao Modo Aviao: ");
            return false;
        }
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    public static boolean getUrlDisponivel(String sURL){
        System.out.println("getUrlDisponivel: "+sURL);
        String urlName = null;
        if (urlName == null) {
            urlName = sURL;
        }
        java.net.HttpURLConnection urlConnection = null;
        try {
            java.net.URL url = new java.net.URL(urlName);
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            try {
                urlConnection.connect();
                System.out.println("Entrei no urlConnection");



                if (urlConnection.getResponseCode() != 200) {
                    System.out.println("getResponseCode() != 200: ");
                    urlConnection.disconnect();
                    return false;
                }
                return (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (SocketTimeoutException stm) {
                System.out.println("SocketTimeoutException Dentro: "+stm.toString());
                urlConnection.disconnect();
                return false;
            }
        } catch (java.net.SocketTimeoutException tmo) {
            System.out.println("getUrlDisponivel SocketTimeoutException: "+tmo.toString());
            return false;
        }catch (HttpRetryException http) {
            System.out.println("Erro http:"+http);
            return  false;
        } catch (SSLProtocolException ssl) {
            System.out.println("Erro SSL: "+ssl);
            return  false;
        } catch (SocketException sok) {
            System.out.println("Erro socket: "+sok);
            return false;
        }  catch (UnknownHostException un) {
            System.out.println("getUrlDisponivel UnknownHostException: "+un.toString());
            return false;
        } catch (IOException ie) {
            System.out.println("getUrlDisponivel IOException " + ie.toString());
            return false;
        } catch (Exception e) {
            System.out.println("getUrlDisponivel Exception: "+e.toString());
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    public static boolean UrlDisponivel(String sURL){
        System.out.println("getUrlDisponivel: "+sURL);
        String urlName = null;
        if (urlName == null) {
            urlName = sURL;
        }

        java.net.HttpURLConnection urlConnection = null;
        try {
            java.net.URL url = new java.net.URL(urlName);
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            try {
                urlConnection.connect();
                System.out.println("Entrei no urlConnection");



                if (urlConnection.getResponseCode() != 200) {
                    System.out.println("getResponseCode() != 200: ");
                    urlConnection.disconnect();
                    return false;
                }
                return (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (SocketTimeoutException stm) {
                System.out.println("SocketTimeoutException Dentro: "+stm.toString());
                urlConnection.disconnect();
                return false;
            }
        } catch (java.net.SocketTimeoutException tmo) {
            System.out.println("getUrlDisponivel SocketTimeoutException: "+tmo.toString());
            return false;
        }catch (HttpRetryException http) {
            System.out.println("Erro http:"+http);
            return  false;
        } catch (SSLProtocolException ssl) {
            System.out.println("Erro SSL: "+ssl);
            return  false;
        } catch (SocketException sok) {
            System.out.println("Erro socket: "+sok);
            return false;
        }  catch (UnknownHostException un) {
            System.out.println("getUrlDisponivel UnknownHostException: "+un.toString());
            return false;
        } catch (IOException ie) {
            System.out.println("getUrlDisponivel IOException " + ie.toString());
            return false;
        } catch (Exception e) {
            System.out.println("getUrlDisponivel Exception: "+e.toString());
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    public static void Cancela_Nonection (final java.net.HttpURLConnection urldisc) {
        final int MILISEGUNDOS = 500;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                System.out.println("Entrei no run");
                urldisc.disconnect();
            }
        }, MILISEGUNDOS);
    }


    public static boolean Conectado(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            String LogSync = null;
            String LogToUserTitle = null;
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                LogSync += "\nConectado a Internet 3G ";
                LogToUserTitle += "Conectado a Internet 3G ";
                //handler.sendEmptyMessage(0);
                //Log.d(TAG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                return true;
            } else if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                LogSync += "\nConectado a Internet WIFI ";
                LogToUserTitle += "Conectado a Internet WIFI ";
                //handler.sendEmptyMessage(0);
                //Log.d(TAG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                return true;
            } else {
                LogSync += "\nNão possui conexão com a internet ";
                LogToUserTitle += "Não possui conexão com a internet ";
                //handler.sendEmptyMessage(0);
                //Log.e(TAG,"Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                //Log.e(TAG,"Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                return false;
            }
        } catch (Exception e) {
            //Log.e(TAG,e.getMessage());
            return false;
        }
    }





    public static int getStatusCode(String urlPath) {
        try {
            URL url = new URL(urlPath); // http://www.seusite.com.br
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //passo 2, mando a String convertida em bytes (textoParaAssinar)
    @SuppressLint("MissingPermission")
    public static String assinar(byte[] textoParaAssinar, String sM,
                                      String sE, String sD, String sP, String sQ, String sDP, String sDQ, String sI ) {
        try {
            //passo 2 Gerar Algoritimo RSA-SHA1
            byte[] sha1hash = new byte[0];
            Signature signer = Signature.getInstance("SHA1withRSA");

            //passo 3 assinar resultado com certificado digital
            //signer.initSign(getPrivateKey(certificado, senha));

            signer.initSign(stringToPrivateKey(sM, sE, sD, sP, sQ, sDP, sDQ, sI));
            signer.update(textoParaAssinar);
            sha1hash = signer.sign();
            System.out.println("Erro Criei signer.sign(): ");

            //passo 4 converter resultado para Base64
            byte[] encodeValue = Base64.encode(sha1hash, Base64.DEFAULT);
            String chaveAssinada  = new String(encodeValue);

            return chaveAssinada;
            //return new Base64.encoder(signer.sign());
        } catch (SignatureException ex) {
            ex.printStackTrace();
            System.out.println("Erro Assinar: "+ex.toString());
            return null;
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            System.out.println("Erro Key: "+ex.toString());
            return null;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            System.out.println("Erro NoSuchAlgorithmException: " + ex.toString());
            return null;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            System.out.println("Erro IllegalArgumentException: " + ex.toString());
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Erro Exception: "+ex.toString());
            return null;
        }
    }



    //Assinar string usando arquivo PFX
    public static String assinar2(byte[]  desafio, File fcerti, String ssenha) {
        try {

            //passo 4
            Signature signer = Signature.getInstance("SHA1withRSA");
            char[] csenha;
            csenha = ssenha.toCharArray();
            signer.initSign(getPrivateKey("Certificado.pfx", csenha, fcerti));

            signer.update(desafio);
            //passo 4 converter resultado para Base64
            byte[] sha1hash = new byte[0];
            sha1hash = signer.sign();
            System.out.println("Erro Ate aqui passei: ");
            byte[] encodeValue = Base64.encode(sha1hash, Base64.DEFAULT);

            String chaveAssinada  = new String(encodeValue);
            return chaveAssinada;

        } catch (SignatureException ex) {
            ex.printStackTrace();
            System.out.println("Erro Exception: "+ex.toString());
            return null;
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            System.out.println("Erro Exception: "+ex.toString());
            return null;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            System.out.println("Erro Exception: "+ex.toString());
            return null;
        }

    }






    public static PrivateKey getPrivateKey(String certificado, char[] senha, File dir) {
        PrivateKey p = null;
        try {
            //FileInputStream fis = new FileInputStream(NfeUtil.generatePathToSecurity(path));
            InputStream fis = null;
            //File file = new File("build\\web\\certificados");

            //File sdCard = getExternalStorageDirectory();
            //File dir = new File (sdCard.getAbsolutePath() + "/Android/Data/certificados/");
            dir.mkdirs();

            String path = dir.getAbsolutePath() + "/" + certificado;

            //System.out.println("Cert: " + path);
            KeyStore ks = KeyStore.getInstance("PKCS12");
            fis = new FileInputStream(path);
            ks.load(fis, senha);
            Enumeration aliasesEnum = ks.aliases();
            String alias = "";
            while (aliasesEnum.hasMoreElements()) {
                alias = (String) aliasesEnum.nextElement();
               // System.out.println("While: " + alias);
                if (ks.isKeyEntry(alias)) {
                    //System.out.println("ks.isKeyEntry: " + alias);
                    break;
                }
            }
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
                    new KeyStore.PasswordProtection(senha));
            //System.out.println("getType: " + keyEntry.getCertificate().toString());
            p = keyEntry.getPrivateKey();
        } catch (UnrecoverableEntryException ex) {
            ex.printStackTrace();
            return null;
        } catch (CertificateException ex) {
            ex.printStackTrace();
            //System.out.println("CertificateException: " + ex.getMessage());
            return null;
        } catch (KeyStoreException ex) {
            //System.out.println("KeyStoreException: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException ex) {
            //System.out.println("NoSuchAlgorithmException: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            //System.out.println("IOException: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
        return p;
    }


    public static PrivateKey stringToPrivateKey(String sM, String sE, String sD, String sP, String sQ,
                                                String sDP, String sDQ, String sI)
    {
        PrivateKey pk = null;
        try {
            byte[] modulusBytes = Base64.decode(sM, Base64.DEFAULT);
            byte[] exponentBytes = Base64.decode(sE, Base64.DEFAULT);
            byte[] DBytes = Base64.decode(sD, Base64.DEFAULT);
            byte[] PBytes = Base64.decode(sP, Base64.DEFAULT);
            byte[] QBytes = Base64.decode(sQ, Base64.DEFAULT);
            byte[] DPBytes = Base64.decode(sDP, Base64.DEFAULT);
            byte[] DQBytes = Base64.decode(sDQ, Base64.DEFAULT);
            byte[] InverseQBytes = Base64.decode(sI, Base64.DEFAULT);

            BigInteger modulus = new BigInteger(1, modulusBytes );
            BigInteger exponent = new BigInteger(1, exponentBytes);
            BigInteger d = new BigInteger(1, DBytes);
            BigInteger p = new BigInteger(1, PBytes);
            BigInteger q = new BigInteger(1, QBytes);
            BigInteger dp = new BigInteger(1, DPBytes);
            BigInteger dq = new BigInteger(1, DQBytes);
            BigInteger inverseQ = new BigInteger(1, InverseQBytes);
            RSAPrivateCrtKeySpec spec = new RSAPrivateCrtKeySpec(modulus, exponent, d, p, q, dp, dq, inverseQ);
            //RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            pk = fact.generatePrivate( spec);
           // PublicKey pubKey = fact.generatePublic(rsaPubKey);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Erro Key: "+e.toString());
            e.printStackTrace();
        }
        return  pk;
    }







    public static boolean validaCPF(String cpf) {
        cpf = cpf.replace(".","").replace("-","").trim();
        if (cpf == null || cpf.length() != 11 || isCPFPadrao(cpf))
            return false;

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) { // CPF não possui somente números
            return false;
        }

        return calcDigVerif(cpf.substring(0, 9)).equals(cpf.substring(9, 11));
    }
    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        if (soma % 11 == 0 | soma % 11 == 1)
            primDig = new Integer(0);
        else
            primDig = new Integer(11 - (soma % 11));

        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        soma += primDig.intValue() * 2;
        if (soma % 11 == 0 | soma % 11 == 1)
            segDig = new Integer(0);
        else
            segDig = new Integer(11 - (soma % 11));

        return primDig.toString() + segDig.toString();
    }

    public static boolean isCPFPadrao(String stemcpf) {
        if (stemcpf.equals("00000000000")) {
            return true;
        }
        if (stemcpf.equals("11111111111")) {
            return true;
        }
        if (stemcpf.equals("22222222222")) {
            return true;
        }
        if (stemcpf.equals("33333333333")) {
            return true;
        }
        if (stemcpf.equals("44444444444")) {
            return true;
        }
        if (stemcpf.equals("55555555555")) {
            return true;
        }
        if (stemcpf.equals("66666666666")) {
            return true;
        }
        if (stemcpf.equals("77777777777")) {
            return true;
        }
        if (stemcpf.equals("88888888888")) {
            return true;
        }
        if (stemcpf.equals("99999999999")) {
            return true;
        }
        return  false;
    }

    public static boolean bissexto(int iano) {
        // se o ano for maior que 400
        if(iano % 400 == 0){
            return  true;
            // se o ano for menor que 400
        } else if((iano % 4 == 0) && (iano % 100 != 0)){
            return  true;
        } else{
            return false;
        }
    }




    public static String Validar_Xml(String snome, String slinha, String sori, String sdest, String sdatsai) {
        String spassou = "";
        String sret = "";
        String cOrixml = "";
        String cDesxml = "";
        String slinhaxml = "";
        String sdatsaixml = "";
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               File fXmlFile = new File(getExternalStorageDirectory().getAbsolutePath() + "/Android/Data/NOVOS/" + snome);
            Document doc = dBuilder.parse(fXmlFile);


            doc.getDocumentElement().normalize();
            NodeList nodeProc = null;

            nodeProc = doc.getElementsByTagName("BPe");
            int counter = nodeProc.getLength();
            for (int temp = 0; temp < nodeProc.getLength(); temp++) {

                Node nNode = nodeProc.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    NodeList nodeinfbpe = doc.getElementsByTagName("infBPe");
                    for (int tempinf = 0; tempinf < nodeinfbpe.getLength(); tempinf++) {

                        //Dados da Viagem
                        NodeList nodepassagem = doc.getElementsByTagName("infPassagem");
                        for (int temppass = 0; temppass < nodepassagem.getLength(); temppass++) {
                            Node npass = nodepassagem.item(temppass);
                            Element passElement = (Element) npass;
                            cOrixml = passElement.getElementsByTagName("cLocOrig").item(0).getTextContent();
                            cDesxml = passElement.getElementsByTagName("cLocDest").item(0).getTextContent();

                        }

                        //Dados da Viagem
                        NodeList nodeviagem = doc.getElementsByTagName("infViagem");
                        for (int tempvia = 0; tempvia < nodeviagem.getLength(); tempvia++) {
                            Node nviagem = nodeviagem.item(tempvia);
                            Element viaElement = (Element) nviagem;
                            slinhaxml = viaElement.getElementsByTagName("cPercurso").item(0).getTextContent();
                            sdatsaixml = viaElement.getElementsByTagName("dhViagem").item(0).getTextContent();


                        }


                        spassou = "S";
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (spassou.equals("S")) {
            if (slinhaxml.equals(slinha) && sdatsaixml.equals(sdatsai) && cOrixml.equals(sori) && cDesxml.equals(sdest)) {
                sret = "OK";
            }
        }

        return sret;
    }



}
