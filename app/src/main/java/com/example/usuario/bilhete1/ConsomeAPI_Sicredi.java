package com.example.usuario.bilhete1;

import android.util.Base64;
import android.util.Log;

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
import java.util.Locale;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class ConsomeAPI_Sicredi {
    private final String baseUrl;
    private final String pfxFilePath;
    private final String pfxPassword;

    public ConsomeAPI_Sicredi(String baseUrl, String pfxFilePath, String pfxPassword) {
        this.baseUrl = baseUrl;
        this.pfxFilePath = pfxFilePath;
        this.pfxPassword = pfxPassword;
    }

    // Gera o client_credentials em Base64 (clientId:clientSecret)
    public String generateClientCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    // Configura o SSLSocketFactory com o certificado PFX
    public SSLSocketFactory generateSSLSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(pfxFilePath)) {
                keyStore.load(fis, pfxPassword.toCharArray());
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, pfxPassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Throwable c = e;
            while (c.getCause() != null) c = c.getCause();
            Log.e("SSL", "Falha SSL (raiz): " + c.getClass().getName() + ": " + c.getMessage(), e);
            throw new RuntimeException("Erro ao configurar SSL", e);
        }
    }

    // Obtém o access token
    public String getAccessToken(String clientId, String clientSecret) throws Exception {
        String clientCredentials = generateClientCredentials(clientId, clientSecret);

        // Monta a URL com os parâmetros query string
        String urlWithParams = baseUrl + "/oauth/token?grant_type=client_credentials&scope=cob.write+cob.read+webhook.read+webhook.write";

        URL url = new URL(urlWithParams);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + clientCredentials);

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
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Erro ao obter token: " + responseCode + " - " + response.toString());
        }
    }

    // Cria a cobrança Pix imediata e retorna o pixCopiaECola
    public PixChargeResponse createPixCharge(String accessToken, String chave,
                                             String nomeDevedor, String cpfCnpjDevedor,
                                             String valorOriginal, String solicitacaoPagador,
                                             String infoAdicional, int expiracao) throws Exception {
        // Gera um txid único (26 caracteres alfanuméricos, conforme o exemplo do Postman)
        String txid = UUID.randomUUID().toString().replace("-", "").substring(0, 26);

        // Monta o JSON da requisição
        JSONObject json = new JSONObject();
        JSONObject calendario = new JSONObject();
        calendario.put("expiracao", expiracao);
        json.put("calendario", calendario);

        JSONObject valor = new JSONObject();
        valor.put("original", valorOriginal);
        json.put("valor", valor);

        json.put("chave", chave);
        json.put("solicitacaoPagador", solicitacaoPagador);

        if (infoAdicional != null && !infoAdicional.isEmpty()) {
            JSONArray infoAdicionais = new JSONArray();
            JSONObject info = new JSONObject();
            info.put("nome", "Detalhes do Pagamento");
            info.put("valor", infoAdicional);
            infoAdicionais.put(info);
            json.put("infoAdicionais", infoAdicionais);
        }

        // Cria a cobrança
        URL url = new URL(baseUrl + "/api/v2/cob/" + txid);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

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

        // Log da resposta para depuração
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response: " + response.toString());

        connection.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extrai o pixCopiaECola diretamente do response
            String pixCopiaECola = jsonResponse.has("pixCopiaECola")
                    ? jsonResponse.getString("pixCopiaECola")
                    : null;

            if (pixCopiaECola == null) {
                throw new RuntimeException("pixCopiaECola não encontrado na resposta");
            }

            PixChargeResponse result = new PixChargeResponse();
            result.txid = txid;
            result.pixCopiaECola = pixCopiaECola;
            result.valor = valorOriginal;
            result.status = jsonResponse.getString("status");

            return result;
        } else {
            throw new RuntimeException("Erro ao criar cobrança: " + responseCode + " - " + response.toString());
        }
    }

    // Obtém o pixCopiaECola consultando a cobrança pelo txid
    private String getPixCopiaECola(String accessToken, String txid) throws Exception {
        URL url = new URL(baseUrl + "/v2/cob/" + txid);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

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

            // O pixCopiaECola vem dentro do objeto loc
            if (jsonResponse.has("loc") && jsonResponse.getJSONObject("loc").has("pixCopiaECola")) {
                return jsonResponse.getJSONObject("loc").getString("pixCopiaECola");
            } else {
                throw new RuntimeException("pixCopiaECola não encontrado na resposta");
            }
        } else {
            throw new RuntimeException("Erro ao obter pixCopiaECola: " + responseCode + " - " + response.toString());
        }
    }

    // Consulta o status da cobrança
    public String consultarPix(String accessToken, String txid) throws Exception {
        URL url = new URL(baseUrl + "/api/v3/cob/" + txid);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

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
            String status = jsonResponse.getString("status");

            // Status possíveis: ATIVA, CONCLUIDA, REMOVIDA_PELO_USUARIO_RECEBEDOR, REMOVIDA_PELO_PSP
            if ("CONCLUIDA".equals(status)) {
                return "CONCLUIDA";
            } else {
                return "PENDENTE";
            }
        } else {
            throw new RuntimeException("Erro ao consultar cobrança: " + responseCode + " - " + response.toString());
        }
    }

    // Classe para retornar os dados da cobrança criada
    public static class PixChargeResponse {
        public String txid;
        public String pixCopiaECola;
        public String valor;
        public String status;
    }
}