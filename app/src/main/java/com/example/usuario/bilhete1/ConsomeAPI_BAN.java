package com.example.usuario.bilhete1;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class ConsomeAPI_BAN {
    private final String baseUrl;
    private final String pfxFilePath;
    private final String pfxPassword;

    public ConsomeAPI_BAN(String baseUrl, String pfxFilePath, String pfxPassword) {
        this.baseUrl = baseUrl;
        this.pfxFilePath = pfxFilePath;
        this.pfxPassword = pfxPassword;
    }

    // Cria o JSON para a cobrança Pix
    public JSONObject createPixChargeJson(String chave, String empresa, String sval, String txid, String sdoc) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("chave", chave);
        json.put("merchantName", empresa);
        json.put("transactionAmount", sval);
        json.put("txId", txid);
        json.put("infoAdicional", "Referente ao documento: " + sdoc);
        return json;
    }


    // Gera o client_credentials em Base64 (clientId:clientSecret)
    public String generateClientCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    public SSLSocketFactory generateSSLSocketFactory() {
        try{
        // Configurar o KeyStore com o certificado PFX
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxFilePath)) {
            keyStore.load(fis, pfxPassword.toCharArray());
        }

        // Inicializar o KeyManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, pfxPassword.toCharArray());

        // Configurar o SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        return  sslContext.getSocketFactory();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccessToken(String clientId, String clientSecret) throws Exception {

            // Gerar client_credentials
            String clientCredentials = generateClientCredentials(clientId, clientSecret);

            // Criar a conexão HTTPS
            URL url = new URL(baseUrl + "/oauth/v1/access-token");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(generateSSLSocketFactory());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + clientCredentials);
            connection.setDoOutput(true);

            // Enviar o corpo da requisição
            String requestBody = "{\"grant_type\": \"client_credentials\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Ler a resposta
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 200 && responseCode < 300
                                    ? connection.getInputStream()
                                    : connection.getErrorStream()))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            connection.disconnect();

            if (responseCode >= 200 && responseCode < 300) {
                // Parsear o JSON e extrair o access_token
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("access_token");
            } else {
                throw new RuntimeException("Erro na requisição getAccessToken: " + responseCode + " - " + response.toString());
            }

    }

    public String getQrcodePix(String accessToken, String chave, String nomeEmpresa,String sval, String txId, String numeroPassagem) throws Exception{

        // Criar o JSON da requisição
        JSONObject jsonBody = createPixChargeJson(chave, nomeEmpresa, sval, txId, numeroPassagem);

        // Criar a conexão HTTPS
        URL url = new URL(baseUrl + "/pix-qrcode-cobranca/v1/qr-code/estatico/gerar");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setDoOutput(true);

        // Enviar o corpo da requisição
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Ler a resposta
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        responseCode >= 200 && responseCode < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        connection.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            // Parsear o JSON e extrair qrCodeText
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("qrCodeText");
        } else {
            throw new RuntimeException("Erro na requisição getQrcodePix: " + responseCode + " - " + response);
        }
    }



    // Consulta o status de uma cobrança Pix
    public String consultarPix(String accessToken, String txid, Date qrCodeDate) throws Exception {
        // Formatar inicio e fim com base na data do QR code
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateStr = sdf.format(qrCodeDate);
        String inicio = dateStr.substring(0, 11) + "00:00:00.000Z";
        String fim = dateStr.substring(0, 11) + "23:59:59.000Z";

        // Montar a URL de consulta
        String urlConsulta = String.format("/pix-qrcode-cobranca/v1/pix?inicio=%s&fim=%s&txid=%s", inicio, fim, txid);

        // Configurar o KeyStore com o certificado PFX
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(pfxFilePath)) {
            keyStore.load(fis, pfxPassword.toCharArray());
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, pfxPassword.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // Criar a conexão HTTPS
        URL url = new URL(baseUrl + urlConsulta);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        // Ler a resposta
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        responseCode >= 200 && responseCode < 300
                                ? connection.getInputStream()
                                : connection.getErrorStream()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        connection.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("pix")) {
                JSONArray pixArray = jsonResponse.getJSONArray("pix");
                if (pixArray.length() > 0) {
                    JSONObject pix = pixArray.getJSONObject(0);
                    String responseTxid = pix.getString("txid");
                    if (txid.equals(responseTxid)) {
                        return "CONCLUIDA";
                    }
                }
            }
            return "PENDENTE"; // Nenhuma transação encontrada ou txid não corresponde
        } else {
            throw new RuntimeException("Erro na requisição: " + responseCode + " - " + response.toString());
        }
    }
}
