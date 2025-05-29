package com.example.usuario.bilhete1;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConsultaBPeWS {


    public static String webServiceConsulta() {
         final String NAMESPACE = "http://tempuri.org/";
         String URL = "http://hmvix.dyndns.info:2222/sohmab/tempService.wso?";
         String SOAP_ACTION = "http://hmvix.dyndns.info:2222/sohmab/tempService.wso/xml_bilhete";
         final String METODO = "xml_bilhete";

        SoapObject request = new SoapObject(NAMESPACE, METODO);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        String retorno = null;
        System.out.println("Entrei");
        // Adiciona parâmetros
        //request.addProperty("Type", "xml");
        //request.addProperty("Ws", sws);
        //request.addProperty("Chave", schave);
        //request.addProperty("Amb", samb);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,6000);


        try {
            System.out.println("Entrei no Try");
            //androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION + METODO, envelope);
            SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();


            String resultString = androidHttpTransport.responseDump;


            System.out.println("Retorno: "+resultString);
            if(resultString != null){
                retorno = resultString;
                System.out.println("Exception: "+retorno);
            }
        } catch (Exception e) {
            retorno = null;
            System.out.println("Exception: "+e.toString());
            e.printStackTrace();
        }
        return retorno;
    }


    public static String Consulta_Xml(String swsenvio, String sXmlBpe, String sChave, String slinha, String sdatsai, String snumcad,
                                      String streori, String stredes, String svlrtar, String svlremb, String svlrseg, String svlrarre,
                                      String sserie, String sdatemi, String sveiculo, String sagente, String stipvia, String spag, String scomando,
                                      String sassinado, String scancel, String smotivo, Context contexview) {

        String retorno = "";
        String snumbpe = sChave.substring(25, 34);
        int inumbpe = Integer.parseInt(snumbpe);
        DB_BPE dbbpe = new DB_BPE(contexview);
        String sori = dbbpe.Busca_Dados_Bpe(Integer.toString(inumbpe), "Treori");
        String sdes = dbbpe.Busca_Dados_Bpe(Integer.toString(inumbpe), "Tredes");
        int posO = sori.indexOf("-");
        int posD = sdes.indexOf("-");
        String sCodOri = sori.substring(0, (posO));
        String sCodDes = sdes.substring(0, (posD));
        int iOri = Integer.parseInt(sCodOri);
        int iDes = Integer.parseInt(sCodDes);
        DB_TRE dbtre = new DB_TRE(contexview);
        sori = dbtre.Busca_Dados_Tre(iOri, "Codigo");
        sdes = dbtre.Busca_Dados_Tre(iDes, "Codigo");

        if (streori.equals(sori) || stredes.equals(sdes)) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/xml_bilhete";
            final String METODO = "xml_bilhete";


            SoapObject request = new SoapObject(NAMESPACE, METODO);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);


            //System.out.println("Entrei");
            // Adiciona parâmetros
            request.addProperty("sxmlenvio", sXmlBpe);
            request.addProperty("schave", sChave);
            request.addProperty("slinha", slinha);
            request.addProperty("sdatsai", sdatsai);
            request.addProperty("snumcad", snumcad);
            request.addProperty("streori", streori);
            request.addProperty("stredes", stredes);
            request.addProperty("svlrtar", svlrtar);
            request.addProperty("svlremb", svlremb);
            request.addProperty("svlrseg", svlrseg);
            request.addProperty("svlrarre", svlrarre);
            request.addProperty("sserie", sserie);
            request.addProperty("sdatemi", sdatemi);
            request.addProperty("sveiculo", sveiculo);
            request.addProperty("sagente", sagente);
            request.addProperty("stipvia", stipvia);
            request.addProperty("spag", spag);
            request.addProperty("scomando", scomando);
            request.addProperty("sassinado", sassinado);
            request.addProperty("scancela", scancel);
            request.addProperty("smotivo", smotivo);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            int itimeout = 12000;
            if (scancel.equals("S")) {
                itimeout = 14000;
            }
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




    public static String Cancela_BPe_Xml(String swscancela, String sNumbpe, String sSerie, String sChave, String sMotivo) {

        //System.out.println("num: "+sNumbpe+" ser: "+sSerie+" ch: "+sChave);
        final String NAMESPACE = "http://tempuri.org/";
        String URL = swscancela+"?";
        String SOAP_ACTION = swscancela+"/xml_cancela";
        final String METODO = "xml_cancela";


        SoapObject request = new SoapObject(NAMESPACE, METODO);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        String retorno = null;
        //System.out.println("Entrei");
        // Adiciona parâmetros
        request.addProperty("snumbpe", sNumbpe);
        request.addProperty("sseriebpe", sSerie);
        request.addProperty("schave", sChave);
        request.addProperty("smotivo", sMotivo);


        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,12000);


        try {
            //System.out.println("Entrei no Try");
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION + METODO, envelope);
            //SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();


            String resultString = androidHttpTransport.responseDump;


            //System.out.println("Retorno xml: "+resultString);
            if(resultString != null){
                retorno = resultString;
                //System.out.println("Exception xml: "+retorno);
            }
        } catch (Exception e) {
            retorno = e.toString();
            //System.out.println("Exception xml2: "+e.toString());
            e.printStackTrace();
        }
        return retorno;
    }



    public static String Atualiza_Key(String swsenvio, Context contexview) {

        String retorno = "";

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/dados_certificado";
            final String METODO = "dados_certificado";


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

    public static String Atualiza_Tarifas(String swsenvio, Context contexview) {

        String retorno = "";

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/busca_tarifa";
            final String METODO = "busca_tarifa";


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



    public static String Consulta_Gratuidade(String swsenvio, Context contexview, String cpfusuario) throws IOException {

        WSActivity.DadosUsuarioGratuidade jsonRetorno = new WSActivity.DadosUsuarioGratuidade();

        if (!swsenvio.equals("")) {

            final String NAMESPACE = "http://tempuri.org/";
            String URL = swsenvio + "?";
            String SOAP_ACTION = swsenvio + "/Controle_Usuario_Gratuidade";
            final String METODO = "Controle_Usuario_Gratuidade";




            //request.addProperty("scpf", "10486731707");//existe
            //request.addProperty("scpf", "10486731709");// não existe



            boolean bcon = Funcoes_Android.Verifica_Conexao(contexview);
            if (bcon == true) {

                OkHttpClient client = new OkHttpClient();


                Request request2 = new Request.Builder()
                        .url("http://sohmab.hm.inf.br/sohmab/tempservice.wso/Controle_Usuario_Gratuidade/JSON/debug?scpf="+cpfusuario)
                        .build();


                try (Response response = client.newCall(request2).execute()) {

                    if(response.code() == 200){
                        String sret = response.body().string();

                        JSONArray jsonObject = new JSONArray(sret);
                        JSONObject jsonResponse = jsonObject.getJSONObject(0);


                        return sret;

                    }

                } catch (SocketTimeoutException | JSONException tm) {
                    String ola = "";
                }

            }


        }
        return "";
    }

    public static String Controla_Gratuidade(String swsenvio, String llstipope, String llsmesano, String llslinha, String llssentido, String llscpf, String llsnome, String llsendere, String llsbai, String llscid, String llscep, String llstel, String llsdoc, String llstipgra, int lliqtdS) throws IOException {
        final String NAMESPACE = "http://tempuri.org/";
        String URL = swsenvio+"?";
        String SOAP_ACTION = swsenvio+"/Controla_Gratuidade";
        final String METODO = "Controla_Gratuidade";

        SoapObject request = new SoapObject(NAMESPACE, METODO);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);

        String retorno = null;

        request.addProperty("stipope", llstipope);
        request.addProperty("smesano", llsmesano);
        request.addProperty("slinha", llslinha);
        request.addProperty("ssentido", llssentido);
        request.addProperty("scpf", llscpf);
        request.addProperty("snome", llsnome);
        request.addProperty("sendere", llsendere);
        request.addProperty("sbai", llsbai);
        request.addProperty("scid", llscid);
        request.addProperty("scep", llscep);
        request.addProperty("stel", llstel);
        request.addProperty("sdoc", llsdoc);
        request.addProperty("stipgra", llstipgra);
        request.addProperty("iqtd", lliqtdS);


        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,12000);


        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION + METODO, envelope);

            String resultString = androidHttpTransport.responseDump;

            if(resultString != null){
                retorno = resultString;

            }
        } catch (Exception e) {
            retorno = e.toString();
            e.printStackTrace();
        }

        return retorno;
    }

}

