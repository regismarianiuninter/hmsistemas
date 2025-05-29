package com.example.usuario.bilhete1;

import android.content.Context;
import android.util.Base64;
import android.util.Xml;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ConsomeAPI_BAN {




    public static String getAccessToken(String clientId, String clientSecret, String surlpd, String surltk, String scertificado, String ssenha)
    {
       //this.clientId = clientId;
        Pattern pat = Pattern.compile(".*\"access_token\"\\s*:\\s*\"([^\"]+)\".*");
        //String content = "grant_type=client_credentials&client_id=" +clientId+"&client_secret="+clientSecret;
        String content = "grant_type=client_credentials";
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        String accesstoken = "";
        try {
            URL url = new URL(surlpd+surltk);
            connection = (HttpURLConnection) url.openConnection();

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream fis = null;
            //File dircert = new File (getExternalFilesDir("Download").getAbsolutePath());
            fis = new FileInputStream(scertificado);
            keyStore.load(fis, ssenha.toCharArray());
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(keyStore, ssenha.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, null, null);
            sslSocketFactory = sslContext.getSocketFactory();
            if(connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)connection)
                        .setSSLSocketFactory(sslSocketFactory);
            }

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String baseAuthStr = clientId + ":" + clientSecret;
            byte[] authentication = Base64.encode(baseAuthStr.getBytes(), Base64.DEFAULT);
            //var base64authorization = Convert.ToBase64String(Xml.Encoding.GetEncoding("ASCII").GetBytes($"{client_id}:{client_secret}"));
            connection.setRequestProperty("Authorization", "Basic " + authentication);
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            PrintStream os = new PrintStream(connection.getOutputStream());
            os.print(content);
            os.close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            StringWriter out = new StringWriter(connection.getContentLength() > 0 ? connection.getContentLength() : 2048);
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            String response = out.toString();
            Matcher matcher = pat.matcher(response);
            if (matcher.matches() && matcher.groupCount() > 0) {
                accesstoken = matcher.group(1);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("IOException "+e.toString());
                }
            }
        }
        System.out.println("Retorno API: "+accesstoken);
        return accesstoken;
    }

    public static String geraToken(String url_padrao, String url_geratoken, String client_id, String client_secret, String dircertificado) {
        String sret = "";
        String access_token = "";

        try {
            URL url = new URL(url_padrao + url_geratoken);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("POST");
            request.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            request.setRequestProperty("Accept", "application/json");
            request.setDoOutput(true);

            String postData = "grant_type=client_credentials";
            byte[] bytes = postData.getBytes("ASCII");
            request.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            String baseAuthStr = client_id + ":" + client_secret;
           // String base64authorization = Base64.encodeToString((client_id + ":" + client_secret).getBytes,("ASCII"));

            //String base64authorization = Base64.encodeToString(baseAuthStr.getBytes(), Base64.DEFAULT);
            byte[] encodeValue = Base64.encode(baseAuthStr.getBytes(), Base64.DEFAULT);
            String base64authorization  = new String(encodeValue);
            base64authorization = base64authorization.replaceAll("\t", "");
            base64authorization = base64authorization.replaceAll("\r", "");
            base64authorization = base64authorization.replaceAll("\n", "");
            request.setRequestProperty("Authorization", "Basic " + base64authorization);

            // Load the certificate
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream keyStoreStream = new FileInputStream(dircertificado)) {
                keyStore.load(keyStoreStream, "@HMinfo@0824".toCharArray());
               // keyStore.load(keyStoreStream, "@HMinfo@0824".toCharArray());
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            if(request instanceof HttpsURLConnection) {
                ((HttpsURLConnection)request)
                        .setSSLSocketFactory(sslSocketFactory);
            }


            try (OutputStream os = request.getOutputStream()) {
                os.write(bytes);
            } catch (Exception e) {
                sret = e.toString();
            }

            try (BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = streamReader.readLine()) != null) {
                    result.append(line);
                }
                String sval = result.toString().replace("null", "0");
                //ObjectMapper objectMapper = new ObjectMapper();
                //Ret_Key token = objectMapper.readValue(sval, Ret_Key.class);
                //access_token = token.access_token;
                sret = sval;
            }

        } catch (Exception ex) {
            sret = ex.getMessage();
            return ex.getMessage();
        }

        return sret;
    }


}
