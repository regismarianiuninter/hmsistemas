package com.example.usuario.bilhete1;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.usuario.bilhete1.Utils.CashClosePdf56mm;
import com.example.usuario.bilhete1.Utils.PdfFilePrintAdapter;
import com.example.usuario.bilhete1.Utils.TicketData;
import com.example.usuario.bilhete1.Utils.UsbEscPosPrinter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActivateBathroom extends AppCompatActivity {

    // Configurações do Sicredi - AJUSTE CONFORME SEU AMBIENTE
    private static String SICREDI_BASE_URL = "";
    private static String SICOOB_BASE_URL = "";

    private static String PFX_FILE_PATH = ""; // Ajustar caminho
    private static String PFX_PASSWORD = "";
    private static String CLIENT_ID = "";
    private static String CLIENT_SECRET = "";
    private static String CHAVE_PIX = "";
    private static String NOME_EMPRESA = "";
    private static String CNPJ_EMPRESA = "";
    private List<TicketModel> Tickets;
    private ListView listaMenu;
    private ArrayAdapter<String>menuAdapter;

    private AutoCompleteTextView serviceDropdown;
    private TextView textPrice;
    private TextView textQuantity;
    private ImageView imageQr;
    private MaterialButton buttonFinalize;
    private MaterialButton buttonQuantityMinus, buttonQuantityPlus;
    private MaterialCardView cardQr;
    private MaterialButtonToggleGroup paymentToggle;
    private MaterialButton radioCash, radioPix;

    private final Locale brLocale = new Locale("pt", "BR");
    private final NumberFormat currency = NumberFormat.getCurrencyInstance(brLocale);

    private ConsomeAPI_Sicredi apiSicredi;

    private ConsomeAPI_Sicoob apiSicoob;
    private String currentAccessToken;
    private String currentTxid;
    private Handler pollingHandler;
    private Runnable pollingRunnable;
    private boolean isPolling = false;
    private static String Nome_user = "";

    private ExecutorService executorService;

    private UsbEscPosPrinter usbPrinter;
    private BluetoothDevice printerBluetooth;
    private ExecutorService io = Executors.newSingleThreadExecutor();
    private Handler main = new Handler(Looper.getMainLooper());
    private boolean emitindoCortesia = false;
    private int ticketQuantity = 1;
    private static final int MIN_TICKET_QUANTITY = 1;
    private static final int MAX_TICKET_QUANTITY = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate_bathroom_qrcode_sell);
        Intent Newintent = getIntent();

        Bundle bundle = Newintent.getExtras();
        String user     = bundle.getString("USUARIO");
        Nome_user = user;
        usbPrinter = new UsbEscPosPrinter(this);

        bindViews();
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    try {
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                            Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                        } else {
                            detalhesPeriodo();
                        }

                    }catch (Exception e){
                        Toast.makeText(ActivateBathroom.this,
                                "Erro ao imprimir:\n"+e.toString(),
                                Toast.LENGTH_LONG).show();
                        System.out.println("Erro ao imprimir:\n"+e.toString());
                    }
                } else if (position == 1) {
                    try {
                        fluxoFechamentoTurno(view);
                    }catch (Exception e){
                        Toast.makeText(ActivateBathroom.this,
                        "Erro ao imprimir:\n"+e.toString(),
                        Toast.LENGTH_LONG).show();
                        System.out.println("Erro ao imprimir:\n"+e.toString());
                    }
                } else if (position == 2) {
                    DB_USR dbusr = new DB_USR(ActivateBathroom.this);
                    String suser = Nome_user;
                    String sfectur = dbusr.Busca_Dados_Usr(Nome_user, "Fectur");
                    if (sfectur.equals("S")) {
                        supervisorFecharCaixaGeralResumo();
                    }
                } else if (position == 3) {
                    confirmarCortesia();
                } else if (position == 4) {
                    confirmarAnalogico();
                }
            }
        });

        Menu_Lateral();
        initializeAPI();
        setupServiceDropdown();
        setupQuantityButtons();
        setupPaymentToggle();
        setupFinalizeButton();

        executorService = Executors.newSingleThreadExecutor();
        pollingHandler = new Handler(Looper.getMainLooper());
    }




    private void Menu_Lateral() {
        String[] osArray = {"Relatório de Vendas", "Fechar Turno", "Fechar Caixa", "Liberação (Sem Valor)", "Bilhete sem Leitura"};
        menuAdapter = new ArrayAdapter<String>(ActivateBathroom.this, android.R.layout.simple_list_item_1, osArray);
        listaMenu.setAdapter(menuAdapter);
    }

    private void bindViews() {
        serviceDropdown = findViewById(R.id.spinnerService);
        textPrice = findViewById(R.id.textPrice);
        textQuantity = findViewById(R.id.textQuantity);
        imageQr = findViewById(R.id.imageQr);
        buttonFinalize = findViewById(R.id.buttonFinalize);
        buttonQuantityMinus = findViewById(R.id.buttonQuantityMinus);
        buttonQuantityPlus = findViewById(R.id.buttonQuantityPlus);
        cardQr = findViewById(R.id.cardQr);
        paymentToggle = findViewById(R.id.radioGroupPayment);
        radioCash = findViewById(R.id.radioCash);
        radioPix = findViewById(R.id.radioPix);
        //VINCULANDO O LISTVIEW DA TELA AO OBJETO CRIADO
        listaMenu = (ListView) ActivateBathroom.this.findViewById(R.id.lista_Menu);
    }

    private void initializeAPI() {
        DB_EMP dbemp = new DB_EMP(ActivateBathroom.this);
        File dircert = new File(getExternalFilesDir("Download").getAbsolutePath());
        String scertificado = dircert + "/" + dbemp.Busca_Dados_Emp(1, "Bancrt");
        PFX_FILE_PATH = scertificado;
        PFX_PASSWORD = dbemp.Busca_Dados_Emp(1,"Bansen");
        CLIENT_ID = dbemp.Busca_Dados_Emp(1,"Cliidb");
        SICREDI_BASE_URL = dbemp.Busca_Dados_Emp(1,"Banwse");
       // SICOOB_BASE_URL = dbemp.Busca_Dados_Emp(1,"Banwse");
        CLIENT_SECRET = dbemp.Busca_Dados_Emp(1,"Clisec");
        CHAVE_PIX = dbemp.Busca_Dados_Emp(1,"Banchv");
        NOME_EMPRESA = dbemp.Busca_Dados_Emp(1,"Descri");
        CNPJ_EMPRESA = dbemp.Busca_Dados_Emp(1,"Cnpj");
        apiSicredi = new ConsomeAPI_Sicredi(SICREDI_BASE_URL, PFX_FILE_PATH, PFX_PASSWORD);
        //apiSicoob = new ConsomeAPI_Sicoob(SICOOB_BASE_URL, PFX_FILE_PATH, PFX_PASSWORD);
    }

    private void setupServiceDropdown() {
        DB_SER dbser = new DB_SER(ActivateBathroom.this);
        DB_SER.SerCursor cursor = dbser.RetornarSer(DB_SER.SerCursor.OrdenarPor.NomeCrescente);
        String[] services;
        String sser = "";

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            sser = sser + cursor.getTipser() + ",";
        }

        services = sser.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                services
        );
        serviceDropdown.setAdapter(adapter);

        if (services.length > 0) {
            serviceDropdown.setText(services[0], false);
            updatePriceForPosition(0);
        }

        serviceDropdown.setOnItemClickListener((parent, view, position, id) -> {
            updatePriceForPosition(position);
            resetPaymentSelection();
        });
    }

    private void setupQuantityButtons() {
        setTicketQuantity(MIN_TICKET_QUANTITY);
        buttonQuantityMinus.setOnClickListener(v -> changeTicketQuantity(-1));
        buttonQuantityPlus.setOnClickListener(v -> changeTicketQuantity(1));
    }

    private void changeTicketQuantity(int delta) {
        int newQuantity = ticketQuantity + delta;
        if (newQuantity < MIN_TICKET_QUANTITY || newQuantity > MAX_TICKET_QUANTITY) {
            return;
        }
        setTicketQuantity(newQuantity);
        resetPaymentSelection();
    }

    private void setTicketQuantity(int quantity) {
        ticketQuantity = Math.max(MIN_TICKET_QUANTITY, Math.min(MAX_TICKET_QUANTITY, quantity));
        textQuantity.setText(String.valueOf(ticketQuantity));
        buttonQuantityMinus.setEnabled(ticketQuantity > MIN_TICKET_QUANTITY);
        buttonQuantityPlus.setEnabled(ticketQuantity < MAX_TICKET_QUANTITY);
        updateDisplayedTotal();
    }

    private void resetPaymentSelection() {
        paymentToggle.clearChecked();
        cardQr.setVisibility(View.GONE);
        imageQr.setImageBitmap(null);
        buttonFinalize.setEnabled(false);
        currentTxid = null;
        stopPolling();
    }

    private void setupPaymentToggle() {
        paymentToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.radioCash) {
                cardQr.setVisibility(View.GONE);
                buttonFinalize.setEnabled(true);
                stopPolling();
            } else if (checkedId == R.id.radioPix) {
                buttonFinalize.setEnabled(false);
                generatePixCharge();
            }
        });
    }

    private void setupFinalizeButton() {
        buttonFinalize.setOnClickListener(v -> {
            String service = serviceDropdown.getText().toString();
            String payment = paymentToggle.getCheckedButtonId() == R.id.radioPix ? "Pix" : "Dinheiro";

            if (payment.equals("Pix")) {
                /*Toast.makeText(this,
                        "Aguardando confirmação do pagamento Pix. Escaneie o QR code para pagar.",
                        Toast.LENGTH_LONG).show();*/
            } else {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                    Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                } else {
                    finalizeTickets(service, payment, "");
                }
            }
        });
    }

    private void updatePriceForPosition(int position) {
        updateDisplayedTotal();
    }

    private void updateDisplayedTotal() {
        double price = getSelectedServiceUnitPrice();
        textPrice.setText(currency.format(price * ticketQuantity));
    }

    private double getSelectedServiceUnitPrice() {
        String service = serviceDropdown.getText().toString();
        if (service == null || service.trim().isEmpty()) {
            return 0.0;
        }
        DB_SER dbser = new DB_SER(ActivateBathroom.this);
        String sval = dbser.Busca_Dados_Ser(service, "Vlrser");
        return Double.valueOf(sval).doubleValue();
    }

    private void generatePixCharge() {
        cardQr.setVisibility(View.VISIBLE);
        buttonFinalize.setEnabled(false);

        Toast.makeText(this, "Gerando cobrança Pix...", Toast.LENGTH_SHORT).show();

        executorService.execute(() -> {
            try {
                // Obter token
                currentAccessToken = apiSicredi.getAccessToken(CLIENT_ID, CLIENT_SECRET);
                //currentAccessToken = apiSicoob.getAccessToken(CLIENT_ID, CLIENT_SECRET);

                // Obter valor do serviço
                String service = serviceDropdown.getText().toString();
                DB_SER dbser = new DB_SER(ActivateBathroom.this);
                String sval = dbser.Busca_Dados_Ser(service, "Vlrser");
                //String sval = "0.01";
                double valorTotal = Double.parseDouble(sval) * ticketQuantity;
                String valorFormatado = String.format(Locale.US, "%.2f", valorTotal);

                // Criar cobrança Pix
                ConsomeAPI_Sicredi.PixChargeResponse response = apiSicredi.createPixCharge(
                        currentAccessToken,
                        CHAVE_PIX,
                        NOME_EMPRESA,
                        CNPJ_EMPRESA,
                        valorFormatado,
                        "Pagamento - " + service + " x" + ticketQuantity,
                        "Ticket de Acesso",
                        3600 // 1 hora de expiração
                );
                /*ConsomeAPI_Sicoob.PixChargeResponse response = apiSicoob.createPixCharge(
                        currentAccessToken,
                        CHAVE_PIX,
                        NOME_EMPRESA,
                        CNPJ_EMPRESA,
                        valorFormatado,
                        "Pagamento - " + service,
                        "Ticket de Acesso",
                        3600 // 1 hora de expiração
                );*/

                currentTxid = response.txid;

                // Gerar QR Code
                final Bitmap qrBitmap = QRCodeGenerator.generateQRCode(
                        response.pixCopiaECola,
                        512,
                        512
                );

                runOnUiThread(() -> {
                    imageQr.setImageBitmap(qrBitmap);
                    buttonFinalize.setEnabled(true);
                    //Toast.makeText(this, "QR Code gerado! Escaneie para pagar.", Toast.LENGTH_LONG).show();

                    // Inicia o polling para verificar o pagamento
                    startPolling();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Erro ao gerar cobrança: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    cardQr.setVisibility(View.GONE);
                    paymentToggle.clearChecked();
                });
            }
        });
    }

    private void startPolling() {
        if (isPolling) return;

        isPolling = true;
        final long startTime = System.currentTimeMillis();
        final long maxPollingTime = 5 * 60 * 1000; // 5 minutos

        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPolling || System.currentTimeMillis() - startTime > maxPollingTime) {
                    stopPolling();
                    runOnUiThread(() -> Toast.makeText(ActivateBathroom.this,
                            "Verificação automática encerrada. Tente novamente ou entre em contato.",
                            Toast.LENGTH_LONG).show());
                    return;
                }

                executorService.execute(() -> {
                    try {
                        String status = apiSicredi.consultarPix(currentAccessToken, currentTxid);
                       // String status = apiSicoob.consultarPix(currentAccessToken, currentTxid);
                        runOnUiThread(() -> {
                            if ("CONCLUIDA".equals(status)) {
                                stopPolling();
                                onPaymentConfirmed(currentTxid);
                            } else {
                                pollingHandler.postDelayed(pollingRunnable, 3000);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getMessage().contains("401")) {
                            // Tenta renovar o token
                            try {
                                currentAccessToken = apiSicredi.getAccessToken(CLIENT_ID, CLIENT_SECRET);
                               // currentAccessToken = apiSicoob.getAccessToken(CLIENT_ID, CLIENT_SECRET);
                                runOnUiThread(() -> Toast.makeText(ActivateBathroom.this,
                                        "Token renovado, continuando verificação...",
                                        Toast.LENGTH_SHORT).show());
                                pollingHandler.postDelayed(pollingRunnable, 3000);
                            } catch (Exception tokenEx) {
                                runOnUiThread(() -> {
                                    stopPolling();
                                    Toast.makeText(ActivateBathroom.this,
                                            "Erro ao renovar token: " + tokenEx.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                            }
                        } else {
                            runOnUiThread(() -> {
                                stopPolling();
                                Toast.makeText(ActivateBathroom.this,
                                        "Erro na verificação: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                });
            }
        };

        pollingHandler.post(pollingRunnable);
    }

    private void stopPolling() {
        isPolling = false;
        if (pollingRunnable != null) {
            pollingHandler.removeCallbacks(pollingRunnable);
        }
    }

    private void onPaymentConfirmed(String sope) {
        String service = serviceDropdown.getText().toString();

        Toast.makeText(this, "Pagamento confirmado!", Toast.LENGTH_LONG).show();
        buttonFinalize.setEnabled(true); // Habilitar o botão após confirmação
        finalizeTickets(service, "Pix", sope);
    }

    private void finalizeTickets(String service, String payment, String operacao) {
        List<String> codigos = new ArrayList<>();
        for (int i = 0; i < ticketQuantity; i++) {
            String scodigo = finalizeSingleTicket(service, payment, operacao);
            if (!scodigo.equals("")) {
                codigos.add(scodigo);
            }
        }

        if (!codigos.isEmpty()) {
            imprimirTickets(codigos, 0);
            resetScreen();
        }
    }

    private void finalizeTicket(String service, String price, String payment, String operacao) {
        String scodigo = finalizeSingleTicket(service, payment, operacao);
        if (!scodigo.equals("")) {
            List<String> codigos = new ArrayList<>();
            codigos.add(scodigo);
            imprimirTickets(codigos, 0);
            resetScreen();
        }
    }

    private String finalizeSingleTicket(String service, String payment, String operacao) {
        // Iniciar pagamento em Dinheiro
        String scodigo = Cria_Ticket(payment);
        if (!scodigo.equals("")){
            DB_TKT ticket = new DB_TKT(ActivateBathroom.this);
            String sID = ticket.Busca_Dados_TKT(scodigo, "ID");
           Date ddata = new Date();
           String sdata = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(ddata);
           String sano, smes, scnpj, scoduf, scodmun, sserie, smod, smodser, sticket, stip, ssequencia, sDV, sagencia;
           sano = sdata.substring(8, 10);
           smes = sdata.substring(3, 5);
           ticket.Atualizar_Campo_Tkt(sID, "Dathor", sdata);
           DB_EMP dbemp = new DB_EMP(ActivateBathroom.this);
           scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
           scodmun = dbemp.Busca_Dados_Emp(1, "Codmun");
           sserie = dbemp.Busca_Dados_Emp(1, "Serie");
           stip = dbemp.Busca_Dados_Emp(1, "Tipamb");
           sagencia = dbemp.Busca_Dados_Emp(1, "Agencia");
           scoduf = scodmun.substring(0, 2);
           smod = "00";
           smodser = (smod+("000" + sserie).substring(sserie.length()));
           sticket = ("000000000" + scodigo).substring(scodigo.length());
           ssequencia = ("00000000" + scodigo).substring(scodigo.length());
           String schave = "";
           schave = scoduf + sano + smes + scnpj + smodser + sticket + stip + ssequencia;
           sDV = Gerar_DVTicket(schave);
           schave = schave+sDV;
           DB_SER dbser = new DB_SER(ActivateBathroom.this);
           String sval = dbser.Busca_Dados_Ser(service, "Vlrser");
           ticket.Atualizar_Campo_Tkt(sID, "Chvtkt", schave);
           ticket.Atualizar_Campo_Tkt(sID, "Tipser", service);
           if (!payment.equals("SEM PAGAMENTO") && !payment.equals("PAGO ANTERIORMENTE")) {//Quando for Cordesia, não salva valor
               ticket.Atualizar_Campo_Tkt(sID, "Vlrtkt", sval);
           }
           ticket.Atualizar_Campo_Tkt(sID, "Forpag", payment);
           ticket.Atualizar_Campo_Tkt(sID, "Terminal", sagencia);
           ticket.Atualizar_Campo_Tkt(sID, "Usuario", Nome_user);
           ticket.Atualizar_Campo_Tkt(sID, "Status", "PG");
           ticket.Atualizar_Campo_Tkt(sID, "Codope", operacao);

        }

        return scodigo;
    }

    private void imprimirTickets(List<String> codigos, int index) {
        if (index >= codigos.size()) {
            return;
        }

        BackgrundPDF(codigos.get(index), () -> imprimirTickets(codigos, index + 1));
    }

    private String Cria_Ticket(String spagamento){
        String sticket = "";
        String sExiste = "";
        String sGrava = "";
        int iID = 1;
        DB_EMP dbe = new DB_EMP(this);
        String sultimo = dbe.Busca_Dados_Emp(iID, "Ultbil");

        /////Verificar se o ticket anterior foi concluído
        if (!sultimo.equals("0")) {
            DB_TKT ticket = new DB_TKT(ActivateBathroom.this);
            Tickets = ticket.VerificaTkt(sultimo);
            if(Tickets.size() > 0) {
                sExiste = "S";
            }
            if (sExiste.equals("S")){
                //verificar status do último ticket
                String sSit = ticket.Busca_Dados_TKT(sultimo, "Status");
                if (sSit.equals("DG")) { //ticket nao foi finalizado, aproveitar numeracao
                    String IDTKTANT = ticket.Busca_Dados_TKT(sultimo, "ID");
                    sticket = sultimo;
                    if (spagamento.equals("SEM PAGAMENTO") || spagamento.equals("PAGO ANTERIORMENTE")) {
                        ticket.Atualizar_Tkt(IDTKTANT, sticket, "", "", "", "", spagamento, "", "", "", "", "PG");
                    } else {
                        ticket.Atualizar_Tkt(IDTKTANT, sticket, "", "", "", "", "", "", "", "", "", "");
                    }

                } else if (!sSit.equals("DG")) { //Ticket está em outra situação ou ocorreu um erro com o anterior
                    sGrava = "S";
                }
            } else {
                sGrava = "S";
            }
        } else if (sultimo.equals("0")){
            sGrava = "S";
        }

        if (sGrava.equals("S")) { // so gravar se o bilhete anterior foi finalizado
            int ultbil = Integer.parseInt(sultimo);
            int numbil = (ultbil + 1);
            sticket = Integer.toString(numbil);

            DB_TKT ticket = new DB_TKT(ActivateBathroom.this);
            ticket.InserirTkt(sticket, "", "", "", "", "", "", "", "", "", "");
            System.out.println("Passei Inserir");
            dbe.Atualizar_Campo_Emp(Integer.toString(iID), "Ultbil", sticket);
        }

        return sticket;
    }


    public String Gerar_DVTicket(String chaveSemDigito) throws InputMismatchException {

        // UMA CHAVE DE ACESSO DE E BPTEM 44 DIGITOS, ENTAO O CALCULO SE DÁ COM OS 43 ANTERIORES
        if (chaveSemDigito.length() != 43) {
            throw new InputMismatchException("Chave Invalida possui " + chaveSemDigito.length());
        }
        int[] aux = new int[chaveSemDigito.length()];
        int variavel = 2;
        int total = 0;
        int dv = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt("" + chaveSemDigito.charAt(i));
            aux[i] = aux[i] * variavel;
            variavel++;
            if (variavel > 9)
                variavel = 2;
            total += aux[i];
        }
        total = total % 11;//Porque o total é divido por onze após as somas...
        if (total == 0 || total == 1)
            dv = 0;
        else
            dv = 11 - total;




        return Integer.toString(dv);

    }


    private void BackgrundPDF(String scodigo) {
        BackgrundPDF(scodigo, null);
    }

    private void BackgrundPDF(String scodigo, Runnable onFinished) {
        ExecutorService server = Executors.newSingleThreadExecutor();
        server.execute(new Runnable() {
            File filepdf = null;
            @Override
            public void run() {
                //onPreExecute method (O que ira fazer antes de executar
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                //doInBackGround method of AsyncTask (O que ira executar em segundo plano)
                DB_TKT ticket = new DB_TKT(ActivateBathroom.this);
                DB_EMP dbemp = new DB_EMP(ActivateBathroom.this);


                try {
                    TicketData t = new TicketData();
                    // -> Preencha a partir do DB_TKT:
                    t.chave  = ticket.Busca_Dados_TKT(scodigo, "Chvtkt"); // 44 dittos

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                    try {
                        Date date = sdf.parse(ticket.Busca_Dados_TKT(scodigo, "Dathor"));
                        t.dathor = date; // new Date();   // ou a data/hora do registro
                    } catch (ParseException e) {
                        System.err.println("Error parsing date string: " + e.getMessage());
                        e.printStackTrace();
                    }
                    String svalor = ticket.Busca_Dados_TKT(scodigo, "Vlrtkt");
                    double dvalor = 0.00;
                    String sforpag = ticket.Busca_Dados_TKT(scodigo, "Forpag");
                    if (!sforpag.equals("SEM PAGAMENTO") && !sforpag.equals("PAGO ANTERIORMENTE")) {
                        dvalor = Double.valueOf(svalor).doubleValue();
                    }
                    t.valor  = dvalor;
                    t.forpag = sforpag;
                    t.nomusr = Nome_user;               // DB_TKT.Nomusr

                    // (opcionais de layout)
                    t.cabecalho1 = "Estação Rodoviária";
                    t.cabecalho2 = "Alderico Tedoldi";
                    String sservivo = ticket.Busca_Dados_TKT(scodigo, "Tipser");
                    t.servico    = sservivo;
                    String stipser = sservivo.substring(0,1);
                    String susuario = ticket.Busca_Dados_TKT(scodigo, "Usuario");
                    String sdathor = ticket.Busca_Dados_TKT(scodigo, "Dathor");
                    String schave = ticket.Busca_Dados_TKT(scodigo, "Chvtkt");
                    String sidpvd = dbemp.Busca_Dados_Emp(1, "Idepdv");
                    /*JSONObject obj = new JSONObject();
                    obj.put("idPVD",  sidpvd);
                    obj.put("chvTKT", schave);
                    obj.put("tpServ", stipser);  // número
                    obj.put("dhEmi",  sdathor);
                    obj.put("xUsuario", susuario);
                    //"<chvTKT>"  + schave + "</chvTKT>" + "<tpServ>" + stipser +"</tpServ>" + "<dhEmi>" + sdathor +"</dhEmi>" + "<xUsuario>" + susuario +"</xUsuario>";
                    String jsonQRcode = obj.toString(); */    // {"idPVD":"51005025",...}
                    String sQRCode = "idPVD=" + sidpvd + "?chBPe=" + schave + "&tpServ=" + stipser + "&dhEmi=" + sdathor + "&xUsuario=" + susuario;
                    t.sqrcode = sQRCode;
                    t.ticketnumber = ticket.Busca_Dados_TKT(scodigo, "Codigo");
                    // Gera PDF
                    File pdf = TicketPdfGenerator56mm.generate(ActivateBathroom.this, t, "ticket_56mm_" + scodigo + ".pdf");
                    // 2) Criar o URI para o MESMO arquivo // ⭐
                    Uri uri = androidx.core.content.FileProvider.getUriForFile(
                            getApplicationContext(),
                            ActivateBathroom.this.getPackageName() + ".fileprovider",         // => com.example.usuario.bilhete1.fileprovider
                            pdf
                    );
                    // 3) Imprimir em Video com adapter por URI (uma única vez) // ⭐
                    /*PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
                    PrintAttributes attrs = new PrintAttributes.Builder()
                            .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
                            .build();

                    pm.print("TicketLiberacao", new PdfFilePrintAdapter(ActivateBathroom.this, uri), attrs);*/
                    filepdf = pdf;

                } catch (Exception ex) {
                    System.out.println("Erro no BackgrundPDF: " + ex.getMessage());
                    ex.printStackTrace();
                }
                ///////////

                //onPostExecute method (O que ira fazer depois de executar
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      // printTicketFromHere(filepdf);
                        try {
                            // 2) Envie direto para a impressora interna via USB:
                          //  usbPrinter.printPdfSilently(filepdf);
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                                Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Imprime_PDF_BT(filepdf);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (onFinished != null) {
                                onFinished.run();
                            }
                        }
                    }

                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void acharPrinterBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não foi encontrado ou não disponível neste equipamento.", Toast.LENGTH_SHORT).show();
            // Device doesn't support Bluetooth
            return;
        }

        /*if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }*/

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bondedDevice : bondedDevices) {
            if (bondedDevice.getName().toLowerCase().contains("print")) {
                printerBluetooth = bondedDevice;
                break;
            }
        }
    }

    private void iniciarImpressora(OutputStream out) throws IOException {
        out.write(EscPosBase.init_printer());
    }

    @SuppressLint("MissingPermission")
    public void Imprime_PDF_BT(File pdfFile) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // 1) Garantir referência da impressora (mesmo fluxo que você usa)
            if (printerBluetooth == null) acharPrinterBluetooth();
            if (printerBluetooth == null) {
                Toast.makeText(this, "Impressora Bluetooth não encontrada/pareada", Toast.LENGTH_LONG).show();
                return;
            }

            // 2) Abrir socket (mantenho seu padrão com insecure + UUID aleatório, pois já funciona no seu ambiente)
            BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(java.util.UUID.randomUUID());
            bluetoothAdapter.cancelDiscovery(); // importante antes de conectar
            impressora.connect();

            try {
                OutputStream out = impressora.getOutputStream();

                // 3) Reset/init ESC/POS (seu método)
                iniciarImpressora(out); // out.write(EscPosBase.init_printer());

                // 4) Renderizar o PDF em largura de cupom (56–58 mm → 384 px)

                Bitmap color = Funcoes_Android.pdfToBitmap384(this, pdfFile);
                Bitmap mono  = Funcoes_Android.ditherToMono(color);
                Funcoes_Android.sendRasterImage(out, mono);
                out.write(new byte[]{0x0A, 0x0A, 0x0A});

                // 8) Feed final (e corte se existir)
                out.write(new byte[]{0x0A, 0x0A, 0x0A}); // 3 linhas
                // Muitas 58 mm não têm guilhotina:
                // out.write(new byte[]{0x1D, 0x56, 0x42, 0x10}); // GS V B n (se suportar)

                out.flush();
            } finally {
                try { impressora.close(); } catch (Exception ignore) {}
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao imprimir PDF via BT: " + e.getMessage());

            Toast.makeText(this, "Erro ao imprimir PDF via BT: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void resetScreen() {
        paymentToggle.clearChecked();
        cardQr.setVisibility(View.GONE);
        buttonFinalize.setEnabled(false);
        currentTxid = null;
        stopPolling();
        setTicketQuantity(MIN_TICKET_QUANTITY);

        // Volta para o primeiro serviço
        DB_SER dbser = new DB_SER(ActivateBathroom.this);
        DB_SER.SerCursor cursor = dbser.RetornarSer(DB_SER.SerCursor.OrdenarPor.NomeCrescente);
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            serviceDropdown.setText(cursor.getTipser(), false);
            updatePriceForPosition(0);
        }
    }

    // Campo da Activity para acompanhar o estado
    private android.print.PrintJob printJob;
    private final android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());

    private final Runnable checkJobState = new Runnable() {
        @Override public void run() {
            if (printJob == null) return;
            if (printJob.isCompleted() || printJob.isCancelled() || printJob.isFailed()) {
                // Opcional: fechar a tela após a impressão
                // finish();
                return;
            }
            handler.postDelayed(this, 400);
        }
    };

    // Campo opcional para evitar prints duplicados
    private boolean printing = false;

    private void printTicketFromHere(java.io.File pdfFile) {
        if (printing) return;               // evita disparo duplo
        printing = true;

        // 1) Uri do MESMO arquivo recebido
        Uri uri = androidx.core.content.FileProvider.getUriForFile(
                getApplicationContext(),
                ActivateBathroom.this.getPackageName() + ".fileprovider", // => com.example.usuario.bilhete1.fileprovider
                pdfFile
        );

        // 2) Adapter ÚNICO por URI (não crie/outro File aqui)
        android.print.PrintDocumentAdapter adapter = new PdfFilePrintAdapter(this, uri);

        // 3) Dispara impressão UMA VEZ
        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        printJob = pm.print(
                "Ticket",
                adapter,
                new PrintAttributes.Builder()
                        .setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME)
                        .build()
        );

        // 4) Monitora e libera o "lock" ao terminar
        handler.post(new Runnable() {
            @Override public void run() {
                if (printJob == null) { printing = false; return; }
                if (printJob.isCompleted() || printJob.isCancelled() || printJob.isFailed()) {
                    printing = false;       // libera para uma próxima impressão
                    // finish(); // se quiser fechar a tela aqui
                } else {
                    handler.postDelayed(this, 400);
                }
            }
        });
    }






    /*private static final UUID SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @SuppressLint("MissingPermission")
    private void printPDFtoMBP(java.io.File pdfFile) throws Exception {
        BluetoothDevice dev = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("AA:BB:CC:DD:EE:FF");
        BluetoothSocket sock = dev.createRfcommSocketToServiceRecord(SPP);
        sock.connect();
        OutputStream os = sock.getOutputStream();

// Reset
        os.write(new byte[]{0x1B,0x40});

// Render PDF -> 384 px -> mono
        Bitmap bmp = pdfToBitmap384(this, pdfFile);
        Bitmap mono = ditherToMono(bmp);

// Enviar GS v 0 (mesma lógica do USB, mas escrevendo em 'os')
        sendRasterImage(os, mono);

        os.write(new byte[]{0x0A,0x0A,0x0A});
        os.flush(); sock.close();
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usbPrinter != null) usbPrinter.cleanup();
        stopPolling();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void emitirTicketCortesia() {
        if (emitindoCortesia) return;
        emitindoCortesia = true;
        try {
            // ... (seu código de emissão & impressão) ...
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                emitindoCortesia = false;
            } else {
                String service = serviceDropdown.getText().toString();
                finalizeTicket(service, "0,00", "SEM PAGAMENTO", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao emitir cortesia: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            emitindoCortesia = false;
        }
    }

    private void emitirTicketAnalogico() {
        if (emitindoCortesia) return;
        emitindoCortesia = true;
        try {
            // ... (seu código de emissão & impressão) ...
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                emitindoCortesia = false;
            } else {
                String service = serviceDropdown.getText().toString();
                finalizeTicket(service, "0,00", "PAGO ANTERIORMENTE", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao emitir cortesia: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            emitindoCortesia = false;
        }
    }

    private void confirmarCortesia() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Emitir liberação sem valor?")
                .setMessage("Gerar ticket com R$ 0,00 e forma de pagamento: SEM PAGAMENTO.")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Emitir", (d, w) -> emitirTicketCortesia())
                .show();
    }

    private void confirmarAnalogico() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Liberação Manual?")
                .setMessage("Gerar ticket de Liberação para Bilhetes sem Leitura")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Emitir", (d, w) -> emitirTicketCortesia())
                .show();
    }
    public void onFecharSemPeriodoClick(View v) {
        v.setEnabled(false);
        Toast.makeText(this, "Gerando relatório…", Toast.LENGTH_SHORT).show();

        io.execute(() -> {
            File pdf;
            try {
                SQLiteDatabase db = new DB_TKT(this).getReadableDatabase();

                // 1) Lê agrupado por DIA e USUÁRIO (Status='PG')
                Map<String, List<TicketPdfGenerator56mm.UserLine>> map = new LinkedHashMap<>();
                double totalGeral = 0.0;

                try (Cursor c = db.rawQuery("SELECT (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)) AS diaISO," +
                        " Usuario AS usuario, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                                "FROM TICKET " +
                                "WHERE Status='PG' " +
                                "GROUP BY (substr(Dathor,7,4) || '-' || substr(Dathor,4,2) || '-' || substr(Dathor,1,2)), Usuario " +
                                "ORDER BY diaISO ASC, Usuario ASC", null)) {
                    while (c.moveToNext()) {
                        String diaISO = c.getString(c.getColumnIndexOrThrow("diaISO"));
                        String diaBR  = toBR(diaISO); // <<<<<<

                        TicketPdfGenerator56mm.UserLine u = new TicketPdfGenerator56mm.UserLine();
                        u.usuario = c.getString(c.getColumnIndexOrThrow("usuario"));
                        u.qtd     = c.getInt(c.getColumnIndexOrThrow("qtd"));
                        u.total   = c.getDouble(c.getColumnIndexOrThrow("total"));

                        map.computeIfAbsent(diaBR, k -> new ArrayList<>()).add(u);
                        totalGeral += u.total;
                    }
                }



                // 2) Monta o objeto CloseReport
                TicketPdfGenerator56mm.CloseReport rep = new TicketPdfGenerator56mm.CloseReport();
                for (Map.Entry<String, List<TicketPdfGenerator56mm.UserLine>> e : map.entrySet()) {
                    TicketPdfGenerator56mm.DayBlock d = new TicketPdfGenerator56mm.DayBlock();
                    d.diaBR    = e.getKey(); // <<<<<< garante que imprime a data
                    d.usuarios = e.getValue();
                    d.totalDia = e.getValue().stream().mapToDouble(x -> x.total).sum();
                    rep.dias.add(d);
                }
                rep.totalGeral = totalGeral;


                try (Cursor p = db.rawQuery(
                        "SELECT Forpag AS forma, COUNT(*) AS qtd, SUM(Vlrtkt) AS total " +
                                "FROM TICKET WHERE Status='PG' GROUP BY Forpag ORDER BY Forpag", null)) {
                    while (p.moveToNext()) {
                        TicketPdfGenerator56mm.PayLine pl = new TicketPdfGenerator56mm.PayLine();
                        pl.forma = p.getString(p.getColumnIndexOrThrow("forma"));
                        pl.qtd   = p.getInt   (p.getColumnIndexOrThrow("qtd"));
                        pl.total = p.getDouble(p.getColumnIndexOrThrow("total"));
                        rep.resumoPagtoGeral.add(pl);
                    }
                }

                // 3) Gera o PDF pelo MESMO gerador
                pdf = TicketPdfGenerator56mm.generate2(this, rep, "fechamento_sem_periodo.pdf");


            } catch (Exception ex) {
                String msg = ex.getMessage();
                main.post(() -> { Toast.makeText(this, "Erro: " + msg, Toast.LENGTH_LONG).show(); v.setEnabled(true); });
                System.out.println("Erro ao imprimir:\n"+msg);
                return;
            }

            File finalPdf = pdf;
            main.post(() -> {
                try {
                    // Imprimir como você já faz (BT/USB/PrintManager)
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        Imprime_PDF_BT(finalPdf);
                    }
                    Toast.makeText(this, "Relatório impresso.", Toast.LENGTH_SHORT).show();
                    // 2) Pergunta se deseja marcar como F (sua função que já fizemos)
                    DB_USR dbusr = new DB_USR(ActivateBathroom.this);
                    String suser = Nome_user;
                    String sfectur = dbusr.Busca_Dados_Usr(Nome_user, "Fectur");
                    if (sfectur.equals("S")) {
                        perguntarFecharTurno(/*diaBR*/ null, /*usuario*/ null);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao imprimir:\n"+e.getMessage());
                    Toast.makeText(this, "Falha ao imprimir: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    v.setEnabled(true);
                }
            });
        });
    }

    // ActivateBathroom.java (ou sua Activity)
    private void perguntarFecharTurno(@Nullable String diaBR, @Nullable String usuario) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Fechar turno?")
                .setMessage("Deseja marcar como FECHADO todos os tickets deste turno?")
                .setCancelable(false)
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", (dlg, which) -> {
                    // faz em background para não travar a UI
                    new Thread(() -> {
                        DB_TKT tkt = new DB_TKT(getApplicationContext());
                        int afetados = tkt.fecharTurno(diaBR, usuario); // passe null,null para todos os 'PG'
                        runOnUiThread(() -> {
                            android.widget.Toast
                                    .makeText(this, "Turno fechado. Registros alterados: " + afetados,
                                            android.widget.Toast.LENGTH_LONG)
                                    .show();
                        });
                    }).start();
                })
                .show();
    }

    // Chame este fluxo quando o usuário pedir o fechamento
    private void fluxoFechamentoTurno(View view) {
        DB_TKT tkt = new DB_TKT(getApplicationContext());
        int qtdPG = tkt.contarPG(/*diaBR*/ null, /*usuario*/ null); // use aquele método contarPG

        if (qtdPG == 0) {
            Toast.makeText(this, "Não há tickets pendentes (PG) para este turno.", Toast.LENGTH_LONG).show();
            return; // evita imprimir relatório vazio
        }

        // 1) Gera e imprime o RESUMO normalmente
        onFecharSemPeriodoClick(view); // seu método que monta o CloseReport e chama TicketPdfGenerator56mm
    }





    public void detalhesPeriodo() throws Exception {
        // montar DetailedReport
        DB_TKT db = new DB_TKT(ActivateBathroom.this);
        TicketPdfGenerator56mm.DetailedReport rep = new TicketPdfGenerator56mm.DetailedReport();
        rep.empresa1 = "Estação Rodoviária";
        rep.empresa2 = "Alderico Tedoldi";

        double total = 0.0;
        try (Cursor c = db.getReadableDatabase().rawQuery(
                "SELECT Codigo AS ticket, Tipser AS tipo, Dathor AS dathor, Vlrtkt AS valor, Forpag AS forpag " +
                        "FROM TICKET WHERE Status='PG' " +
                        "ORDER BY DATE(Dathor), Dathor, Codigo", null)) {

            while (c.moveToNext()) {
                TicketPdfGenerator56mm.TicketLine tl = new TicketPdfGenerator56mm.TicketLine();
                tl.ticket    = c.getString(c.getColumnIndexOrThrow("ticket"));
                tl.tipo      = c.getString(c.getColumnIndexOrThrow("tipo"));

                String iso   = c.getString(c.getColumnIndexOrThrow("dathor")); // "yyyy-MM-dd HH:mm:ss"
                tl.dataHoraBR = iso; //toBRDateTime(iso); // ver helper abaixo

                tl.valor     = c.getDouble(c.getColumnIndexOrThrow("valor"));
                total += tl.valor;

                String fp     = c.getString(c.getColumnIndexOrThrow("forpag"));
                tl.forpag     = mapForma(fp); // opcional (veja abaixo)
                rep.itens.add(tl);
            }
        }
        rep.totalGeral = total;

// gerar + imprimir
        File pdf = TicketPdfGenerator56mm.generate3(this, rep, "fechamento_detalhado_56mm.pdf");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
            return;
        } else {
            Imprime_PDF_BT(pdf);
        }



    }
    // helper
    private static String toBRDateTime(String iso){
        try {
            java.text.SimpleDateFormat in  = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.US);
            java.text.SimpleDateFormat out = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault());
            return out.format(in.parse(iso));
        } catch (Exception e) { return iso; }
    }

    private static String toBR(String iso){ // "yyyy-MM-dd" -> "dd/MM/yyyy"
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(iso);
            return d.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) { return iso; }
    }

    private static String mapForma(String fp){
        if (fp == null) return "-";
        switch (fp.trim().toUpperCase()) {
            case "01": case "DINHEIRO": return "DINHEIRO";
            case "03": case "CREDITO":  return "CRÉDITO";
            case "04": case "DEBITO":   return "DÉBITO";
            case "05": return "VALE TRANSP.";
            case "06": case "PIX":      return "PIX";
            case "99": return "OUTROS";
            default: return fp;
        }
    }

    public void supervisorFecharCaixaGeralResumo() {
        new Thread(() -> {
            DB_TKT dbt = new DB_TKT(getApplicationContext());
            int qtdF = dbt.contarStatusF();

            runOnUiThread(() -> {
                if (qtdF == 0) {
                    android.widget.Toast.makeText(this,
                            "Não há movimentos com Status = F para fechar.",
                            android.widget.Toast.LENGTH_LONG).show();
                    return;
                }

                // 1) Gera e imprime o RESUMO de todos os F
                new Thread(() -> {
                    try {
                        File pdf = gerarFechamentoGeralResumo();
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                            Toast.makeText(ActivateBathroom.this, "Bluetooth está desativado", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            Imprime_PDF_BT(pdf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> android.widget.Toast.makeText(this,
                                "Erro ao gerar/imprimir: " + e.getMessage(),
                                android.widget.Toast.LENGTH_LONG).show());
                        return;
                    }

                    // 2) Após a impressão, pergunta se deseja APAGAR os F
                    runOnUiThread(() -> {
                        new androidx.appcompat.app.AlertDialog.Builder(this)
                                .setTitle("Fechar CAIXA GERAL?")
                                .setMessage("Encontrados " + qtdF + " registro(s) com Status = F.\n" +
                                        "Deseja remover todos para liberar espaço?")
                                .setNegativeButton("Não", null)
                                .setPositiveButton("Sim", (d, w) -> {
                                    new Thread(() -> {
                                        int apagados = dbt.deletarTodosStatusF();
                                        dbt.compactarBanco(); // opcional
                                        runOnUiThread(() -> android.widget.Toast.makeText(this,
                                                "Removidos " + apagados + " registro(s).",
                                                android.widget.Toast.LENGTH_LONG).show());
                                    }).start();
                                })
                                .setCancelable(false)
                                .show();
                    });
                }).start();
            });
        }).start();
    }

    private File gerarFechamentoGeralResumo() throws Exception {
        // Monta o CloseReport (mesmo modelo já usado no fecha-turno)
        TicketPdfGenerator56mm.CloseReport rep = new TicketPdfGenerator56mm.CloseReport();
        rep.empresa1 = "Estação Rodoviária";
        rep.empresa2 = "Alderico Tedoldi";

        DB_TKT tkt = new DB_TKT(getApplicationContext());

        // 1) Data -> Usuários (qtd/total)
        Map<String, java.util.List<TicketPdfGenerator56mm.UserLine>> map = new java.util.LinkedHashMap<>();
        double totalGeral = 0.0;

        try (Cursor c = tkt.listarResumoFPorDiaUsuario()) {
            while (c.moveToNext()) {
                String diaISO = c.getString(c.getColumnIndexOrThrow("diaISO"));
                String diaBR  = toBR(diaISO);

                TicketPdfGenerator56mm.UserLine u = new TicketPdfGenerator56mm.UserLine();
                u.usuario = c.getString(c.getColumnIndexOrThrow("usuario"));
                u.qtd     = c.getInt   (c.getColumnIndexOrThrow("qtd"));
                u.total   = c.getDouble(c.getColumnIndexOrThrow("total"));

                map.computeIfAbsent(diaBR, k -> new java.util.ArrayList<>()).add(u);
                totalGeral += u.total;
            }
        }

        rep.dias = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, java.util.List<TicketPdfGenerator56mm.UserLine>> e : map.entrySet()) {
            TicketPdfGenerator56mm.DayBlock d = new TicketPdfGenerator56mm.DayBlock();
            d.diaBR = e.getKey();
            d.usuarios.addAll(e.getValue());
            double somaDia = 0.0;
            for (TicketPdfGenerator56mm.UserLine u : d.usuarios) somaDia += u.total;
            d.totalDia = somaDia;
            rep.dias.add(d);
        }
        rep.totalGeral = totalGeral;

        // 2) Resumo por Forma de Pagamento (final do PDF)
        try (Cursor p = tkt.listarResumoFPagtoGeral()) {
            while (p.moveToNext()) {
                TicketPdfGenerator56mm.PayLine pl = new TicketPdfGenerator56mm.PayLine();
                pl.forma = p.getString(p.getColumnIndexOrThrow("forma"));
                pl.qtd   = p.getInt   (p.getColumnIndexOrThrow("qtd"));
                pl.total = p.getDouble(p.getColumnIndexOrThrow("total"));
                rep.resumoPagtoGeral.add(pl);
            }
        }

        // 3) Gera PDF (usa o overload resumido que já fizemos no TicketPdfGenerator56mm)
        return TicketPdfGenerator56mm.generate2(this, rep, "fechamento_geral_resumo_56mm.pdf");
    }




}
