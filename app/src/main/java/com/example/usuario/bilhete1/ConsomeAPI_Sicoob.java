package com.example.usuario.bilhete1;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class ConsomeAPI_Sicoob {
    private static final String TAG = "SICOOB_PIX";
    private static final String SICOOB_TOKEN_URL = "https://auth.sicoob.com.br/auth/realms/cooperado/protocol/openid-connect/token";

    private final String baseUrl;
    private final String pfxFilePath;
    private final String pfxPassword;
    private String currentClientId;

    public ConsomeAPI_Sicoob(String baseUrl, String pfxFilePath, String pfxPassword) {
        this.baseUrl = baseUrl;
        this.pfxFilePath = pfxFilePath == null ? "" : pfxFilePath.trim();
        this.pfxPassword = pfxPassword == null ? "" : pfxPassword.trim();
    }

    // Gera o client_credentials em Base64 (clientId:clientSecret)
    public String generateClientCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    // Configura o SSLSocketFactory com o certificado PFX
    public SSLSocketFactory generateSSLSocketFactory() {
        try {
            File pfxFile = new File(pfxFilePath);
            if (!pfxFile.exists() || !pfxFile.isFile()) {
                throw new RuntimeException("Certificado PIX nao encontrado: " + pfxFilePath);
            }
            if (pfxFile.length() == 0) {
                throw new RuntimeException("Certificado PIX vazio: " + pfxFilePath);
            }

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(pfxFile)) {
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
            throw new RuntimeException(
                    "Falha ao carregar certificado PIX. Arquivo: " + pfxFilePath
                            + ". Verifique se este e o PFX correto e se a senha cadastrada confere. Detalhe: "
                            + c.getClass().getSimpleName() + ": " + c.getMessage(),
                    e);
        }
    }

    // Obtém o access token
    public String getAccessToken(String clientId, String clientSecret) throws Exception {
        clientId = clientId == null ? "" : clientId.trim();
        currentClientId = clientId;
        String requestBody =
                "grant_type=client_credentials" +
                        "&client_id=" + encodeFormValue(clientId) +
                        "&scope=" + encodeFormValue("cob.write cob.read webhook.read webhook.write");

        // Monta a URL com os parâmetros query string
        String urlWithParams = getTokenUrl();

        URL url = new URL(urlWithParams);
        Log.d(TAG, "Obtendo token em: " + url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "*/*");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
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

        connection.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("access_token");
        } else {
            throw new RuntimeException("Erro ao obter token: " + responseCode + " - " + response.toString());
        }
    }

    // Cria a cobrança Pix imediata e retorna o pixCopiaECola
    private String encodeFormValue(String value) throws Exception {
        return URLEncoder.encode(value == null ? "" : value, "UTF-8");
    }

    private String getTokenUrl() {
        if (baseUrl != null && baseUrl.toLowerCase(Locale.ROOT).contains("sandbox")) {
            return "https://sandbox.sicoob.com.br/sicoob/sandbox/auth/realms/cooperado/protocol/openid-connect/token";
        }
        return SICOOB_TOKEN_URL;
    }

    private String getPixApiBaseUrl() {
        String url = baseUrl == null ? "" : baseUrl.trim();
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        String lowerUrl = url.toLowerCase(Locale.ROOT);
        if (lowerUrl.endsWith("/pix/api/v2")) {
            return url;
        }

        return url + "/pix/api/v2";
    }

    private void addClientIdHeader(HttpsURLConnection connection) {
        if (currentClientId != null && !currentClientId.trim().isEmpty()) {
            connection.setRequestProperty("client_id", currentClientId);
        }
    }

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
        URL url = new URL(getPixApiBaseUrl() + "/cob/" + txid);
        Log.d(TAG, "Criando cobranca Pix em: " + url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Accept", "application/json");
        addClientIdHeader(connection);
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
            String pixCopiaECola = jsonResponse.optString("pixCopiaECola", "");
            if (pixCopiaECola.isEmpty()) {
                pixCopiaECola = jsonResponse.optString("brcode", "");
            }

            if (pixCopiaECola.isEmpty()) {
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
        URL url = new URL(getPixApiBaseUrl() + "/cob/" + txid);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        addClientIdHeader(connection);

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
        URL url = new URL(getPixApiBaseUrl() + "/cob/" + txid);
        Log.d(TAG, "Consultando cobranca Pix em: " + url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(generateSSLSocketFactory());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        addClientIdHeader(connection);

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
