package com.example.usuario.bilhete1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.usuario.bilhete1.Utils.BlueListViewAdapter;
import com.example.usuario.bilhete1.Utils.Mode;
import com.example.usuario.bilhete1.Utils.PermissionUtil;
import com.example.usuario.bilhete1.Utils.PrintfBlueListActivity;
import com.example.usuario.bilhete1.Utils.PrintfManager;
import com.example.usuario.bilhete1.Utils.Util;
import com.sunmi.utils.AidlUtil;
import com.example.usuario.bilhete1.Utils.PrintfBlueListActivity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.sunmi.utils.BitmapUtil;
import com.sunmi.utils.BluetoothUtil;
import com.sunmi.utils.ESCUtil;
import com.sunmi.utils.ThreadPoolManager;
import com.sunmi.utils.MemInfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "bilhete1";
    public static final int DO_PRINT = 0x10001;
    private TextView edtNome;
    private Button btnImprimir, btnVenver, btnPercursos, btnEmpresa, btnCadastros, btnGaveta;
    List<String> opcoes;
    ArrayAdapter<String> adaptador;
    private byte[] inputCommand ;
    private final int RUNNABLE_LENGHT = 2;
    private Random random = new Random();
    private IWoyouService woyouService;
    private boolean isBold, isUnderLine;
    private int record;
    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};
    private ICallback callback = null;
    private  TextView info;
    private static final int Activity_Trecho = 1;
    private static int Activity_Dados = 0;
    private static String Nome_user = "";



    private HashMap<String, Integer> alignCenter = new HashMap<>();
    private HashMap<String, Integer> alignLeft = new HashMap<>();
    private HashMap<String, Integer> alignRight = new HashMap<>();
    private HashMap<String, Integer> font_C120 = new HashMap<>();
    private HashMap<String, Integer> font_C122 = new HashMap<>();
    private HashMap<String, Integer> font_C124 = new HashMap<>();
    private HashMap<String, Integer> font_C126 = new HashMap<>();
    private HashMap<String, Integer> font_C128 = new HashMap<>();
    private HashMap<String, Integer> font_C220 = new HashMap<>();
    private HashMap<String, Integer> font_C222 = new HashMap<>();
    private HashMap<String, Integer> font_C224 = new HashMap<>();
    private HashMap<String, Integer> font_C226 = new HashMap<>();
    private HashMap<String, Integer> font_C228 = new HashMap<>();
    private HashMap<String, Integer> font_L120 = new HashMap<>();
    private HashMap<String, Integer> font_L122 = new HashMap<>();
    private HashMap<String, Integer> font_L124 = new HashMap<>();
    private HashMap<String, Integer> font_L126 = new HashMap<>();
    private HashMap<String, Integer> font_L128 = new HashMap<>();
    private HashMap<String, Integer> font_L220 = new HashMap<>();
    private HashMap<String, Integer> font_L222 = new HashMap<>();
    private HashMap<String, Integer> font_L224 = new HashMap<>();
    private HashMap<String, Integer> font_L226 = new HashMap<>();
    private HashMap<String, Integer> font_L228 = new HashMap<>();
    private HashMap<String, Integer> font_R120 = new HashMap<>();
    private HashMap<String, Integer> font_R122 = new HashMap<>();
    private HashMap<String, Integer> font_R124 = new HashMap<>();
    private HashMap<String, Integer> font_R126 = new HashMap<>();
    private HashMap<String, Integer> font_R128 = new HashMap<>();
    private HashMap<String, Integer> font_R220 = new HashMap<>();
    private HashMap<String, Integer> font_R222 = new HashMap<>();
    private HashMap<String, Integer> font_R224 = new HashMap<>();
    private HashMap<String, Integer> font_R226 = new HashMap<>();
    private HashMap<String, Integer> font_R228 = new HashMap<>();
    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothDevice printerBluetooth;
    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    private PrintfManager printfManager;
    private List<Mode> listData;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BlueListViewAdapter adapter = null;
    private List<BluetoothDevice> bluetoothDeviceArrayList;
    private ListView lv_blue_list,lv_already_blue_list;
    private TextView tv_blue_list_back, tv_blue_list_operation;
    private boolean isRegister;
    private Context context;



    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
            //setButtonEnable(true);
        }
    };

    private final int MSG_TEST = 1;
    private long printCount = 0;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == MSG_TEST){
               // testAll();
                long mm = MemInfo.getmem_UNUSED(MainActivity.this);
                if( mm < 100){
                    handler.sendEmptyMessageDelayed(MSG_TEST, 20000);
                }else{
                    handler.sendEmptyMessageDelayed(MSG_TEST, 800);
                }
                Log.i(TAG,"testAll: " + printCount + " Memory: " + mm);
            }
        }
    };

    private void test(){
        ThreadPoolManager.getInstance().executeTask(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    woyouService.printerSelfChecking(null);
                    woyouService.printText(" printed: " + printCount + " bills.\n\n\n\n", null);
                    printCount++;
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }});
    }

    ActivityResultLauncher<Intent> startForresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null) {
                if (result.getData() != null) {
                    EditText edtret = findViewById(R.id.edtRet);
                    // String sret = result.getData().getStringExtra(TreActivity.Activity_Dados);
                    Intent data = result.getData();
                    String sorigemresult = "";
                    String sret = "";
                    int iActivity_Dados = Activity_Dados;
                    if (iActivity_Dados == 104) {
                        String szxing = data.getAction();
                        if (szxing.equals("com.google.zxing.client.android.SCAN")) {
                            sorigemresult = "104";
                        }
                    } else {
                        sret = data.getStringExtra("msg");
                        sorigemresult = data.getStringExtra("Activity_Dados");
                    }
                    // se o resultado retornou o usuário
                    //Log.i("Result SCAN ", Integer.toString(sorigemresult.equals("2")));
                    if (sorigemresult.equals("0")) {

                            String sUser = data.getStringExtra("user");

                            if (result.getResultCode() == RESULT_OK) {

                                Nome_user = sUser;
                                if (sUser.equals("HMINFO")) {
                                    innitView();
                                } else { //se nao for usuario HMINFO mostrar apenas a tela de venda
                                    Abre_Venda();
                                }
                            } else {

                                Nome_user = "";
                                Abre_login();
                            }

                    }
                    // se o resultado retornou a Viagem
                    if (sorigemresult.equals("1")) {
                            String msg = data.getStringExtra("msg");

                            if (result.getResultCode() == RESULT_OK)  {

                                // mostra hint
                                //Toast.makeText(this, "Viagem: " + msg, Toast.LENGTH_LONG).show();
                                EditText edtViagem = findViewById(R.id.edtLinha);
                                edtViagem.setText(msg.toString());
                            }

                    }
                    // se o resultado retornou a Origem
                    if (sorigemresult.equals("2")) {
                            String msg = data.getStringExtra("msg");

                            if (result.getResultCode() == RESULT_OK) {

                                // mostra hint
                                //Toast.makeText(this, "Origem: " + msg, Toast.LENGTH_LONG).show();
                                EditText edtOrigem = findViewById(R.id.edtLinha);
                                edtOrigem.setText(msg.toString());
                            }


                    }
                    // se o resultado retornou o Destino
                    if (sorigemresult.equals("3")) {

                            String msg = data.getStringExtra("msg");

                            if (result.getResultCode() == RESULT_OK) {

                                EditText edtDestino = findViewById(R.id.edtLinha);
                                edtDestino.setText(msg.toString());

                            }

                    }
                    if (sorigemresult.equals("4")) {

                            String msg = data.getStringExtra("msg");

                            if (result.getResultCode() == RESULT_OK) {

                                // mostra hint
                                //Toast.makeText(this, "Destino: " + msg, Toast.LENGTH_LONG).show();
                                EditText edtPercurso = findViewById(R.id.edtLinha);
                                edtPercurso.setText(msg.toString());

                            }

                    }

                    //Retorno Key Banestes
                    if (sorigemresult.equals("6")) {
                        String msg = data.getStringExtra("msg");
                        Log.d("Gera Key Banestes", msg);
                    }

                    //Retorno gerar Token
                    if (sorigemresult.equals("60")) {
                        String msg = data.getStringExtra("msg");
                        Log.d("Gera Key Banestes", msg);
                    }

                    // Verificar retorno da tela de venda
                    if (sorigemresult.equals("99")) {

                        if (!Nome_user.equals("HMINFO")){ //se ainda nao fez login
                            Abre_login();
                        }

                    }
                    //Se retornou leitura da camera
                    if (sorigemresult.equals("104")) {
                        Log.i("Result = 104 ", sorigemresult);
                        if (result.getResultCode() == RESULT_OK) {
                            String contents = data.getStringExtra("SCAN_RESULT");
                            Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_LONG).show();
                            Log.i("CONTENT SCAN ", contents);

                        }  else {
                            sret = data.getStringExtra("msg");
                            sorigemresult = data.getStringExtra("Activity_Dados");
                        }
                    }
                    //retorno da consulta de usuario da gratuidade
                    if(sorigemresult.equals("2")){
                        if (result.getResultCode() == RESULT_OK) {
                            String contents = data.getStringExtra("RET_VERIFICA_USUARIO");
                            Log.i("RETORNO ", contents);
                        }
                    }
                } else {
                    if (!Nome_user.equals("HMINFO")){ //se ainda nao fez login
                        Abre_login();
                    }
                }
            }


        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        record = 17;
        isBold = false;
        isUnderLine = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Usuario_Inicial();
        context = this;
        initData();

        if (Nome_user.equals("") || !Nome_user.equals("HMINFO")){ //se ainda nao fez login
            Nome_user = "";
            Abre_login();
        }
        // Solicitar permissões
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.BLUETOOTH_CONNECT,
                            android.Manifest.permission.BLUETOOTH_SCAN},
                    1);
        }


        Button btnPrint = findViewById(R.id.btnPrint);
        Button btnVender = findViewById(R.id.btnVender);
        Button btnPercursos = findViewById(R.id.btnPercursos);
        Button btnEmpresa = findViewById(R.id.btnEmpresa);
        Button btnCadastros = findViewById(R.id.btnCadastros);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnassina = findViewById(R.id.btnAssinar);
        Button btnlerqr = findViewById(R.id.btnLerQR);
        Button btngerartoken = findViewById(R.id.btnGeratoken);
        Button btnConsultaUsrGratuidade = findViewById(R.id.btnConsultaUsrGratuidade);
        //Abrir cadastro de emrpedsas
        btnEmpresa.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Abre_Config();
            }
        });
        //Impressao de testes
        btnPrint.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText editText = findViewById(R.id.edtLinha);
                String slinha = editText.getText().toString();
                if (printfManager == null) {
                    Log.e(TAG, "PrintfManager é null");
                    Util.ToastText(context, "Erro: Impressora não inicializada");
                    return;
                }
                //PrintfBlueListActivity.startActivity(MainActivity.this);
                if (printfManager.isConnect()) {
                /*String testQR = "https://dfe-portal.svrs.rs.gov.br/bpe/qrcode?chBPe=32250127143718000193630170000000081000003632&tpAmb=2";
                try {
                    printfManager.printf_QRcode("A", "C", PrinterConverter.QRCodeDataToBytes(testQR, 100));
                    printfManager.printf_Texto("F", "C", "N", "N", "", 4);
                    printfManager.printf_QRcode("A", "C", PrinterConverter.QRCodeDataToBytes(testQR, 150));
                    printfManager.printf_Texto("F", "C", "N", "N", "", 4);
                    printfManager.printf_QRcode("A", "C", PrinterConverter.QRCodeDataToBytes(testQR, 180));
                    printfManager.printf_Texto("F", "C", "N", "N", "", 4);
                    printfManager.printf_QRcode("A", "C", PrinterConverter.QRCodeDataToBytes(testQR, 200));
                    printfManager.printf_Texto("F", "C", "N", "N", "", 4);
                } catch (PrinterConverter.PrinterException e) {
                    Log.e(TAG, "Erro ao imprimir QR Code de teste", e);
                    Toast.makeText(MainActivity.this, "Erro ao imprimir QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/
                    List<PrintfManager.PrintCommand> commands = new ArrayList<>();
                    commands.add(new PrintfManager.PrintCommand("C", "S", "N", "TESTE", 1));
                    commands.add(new PrintfManager.PrintCommand("C", "N", "N", "TESTE", 1));
                    commands.add(new PrintfManager.PrintCommand("C", "G", "N", "TESTE", 6));
                    printfManager.printBufferedText(commands);


                } else {
                    Log.d(TAG, "Impressora não conectada, abrindo PrintfBlueListActivity");
                    PrintfBlueListActivity.startActivity(MainActivity.this);
                }

            }
        });
        //Abrir tela de Vendas
        btnVender.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Abre_Venda();
            }
        });
        //AImportar Percursos
        btnPercursos.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Abre_Percursos();
            }
        });
        //Abrir e alterar Cadastros
        btnCadastros.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Consulta_Cadastros();
            }
        });

        //Limpar Base de dados do BP-e
        btnClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Limpar_Bilhetes();
            }
        });

        //Teste de Assinatura
        btnassina.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Assinar_String();
            }
        });

        //Ler QR-code
        btnlerqr.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                checkPermission();
                Ler_QRcode();
            }
        });

        //Gerar Token Banestes
        btngerartoken.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Gerar_Token();
            }
        });

        //Consulta Gratuidade por CPF
        btnConsultaUsrGratuidade.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                ConsultaGratuidadeUsr();
            }
        });



        // Add callback listener
        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle onback pressed
                Abre_login();
            }
        });






        //setStyles(20, 0);



    }

    private void initData() {
        try {
            printfManager = PrintfManager.getInstance(context);
            if (printfManager == null) {
                Log.e(TAG, "Falha ao inicializar PrintfManager");
                Util.ToastText(context, "Erro: Falha ao inicializar impressora");
                return;
            }
            listData = new ArrayList<>();
            listData.add(new Mode("Test-P26", 200, 320));
            listData.add(new Mode("Test-P16", 20, 188));
            printfManager.defaultConnection(MainActivity.this);
        } catch (Exception e) {
            Log.e(TAG, "Erro em initData: " + e.getMessage());
            Util.ToastText(context, "Erro ao inicializar dados");
        }
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
                String WScomando = "verificaUsuarioGratuidade";

                DB_EMP dbempws = new DB_EMP(MainActivity.this);
                String sendews = dbempws.Busca_Dados_Emp(1, "Endews");

                Activity_Dados = 22;
                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                Intent myIntent = new Intent(MainActivity.this, WSActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("USUARIO", Nome_user);
                bundle.putString("CANCEL",  "");
                bundle.putString("COMANDO",  WScomando);
                bundle.putString("Activity_Dados", "22");

                myIntent.putExtras(bundle);

                // chama esse intent e aguarda resultado
                startForresult.launch(myIntent);

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






    public void Abre_login() {
        Activity_Dados = 0;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Activity_Dados", "0");
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);
    }



    public void Usuario_Inicial(){
        DB_USR dbu = new DB_USR(this);
        DB_USR.UsrCursor cursor = dbu.RetornarUsr(DB_USR.UsrCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);


        }

        if (cursor.getCount() == 0){
            dbu.InserirUsr("HMINFO", "1hm26990", "S", "");
            dbu.InserirUsr("CIELO", "123456", "S", "");

        }
    }

    private void innitView(){
        new BitmapUtil();

        //info = (TextView) findViewById(R.id.info);

        callback = new ICallback.Stub() {

            @Override
            public void onRunResult(final boolean success) throws RemoteException {
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG,"printlength:" + value + "\n");
            }

            @Override
            public void onRaiseException(int code, final String msg) throws RemoteException {
                Log.i(TAG,"onRaiseException: " + msg);
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        info.append("onRaiseException = " + msg + "\n");
                    }});

            }
        };

        Intent intent=new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);


    }

    Bitmap mBitmap;


    public void PrintText() {
        int position = 0;

        EditText edtLinha = findViewById(R.id.edtLinha);

        String content = edtLinha.getText().toString();
        edtLinha.setText(mStrings[position]);
        record = position;
        float size = 30;
        AidlUtil.getInstance().printText(content, size, isBold, isUnderLine);
        //  printByBluTooth(content);
        // Imprimir();



    }


    //Imprimir Texto Digitado
    private void Imprimir(){

        //System.out.println("Entrei No Botao Imprimir");
        EditText edtLinha = findViewById(R.id.edtLinha);
        String stexto = edtLinha.getText().toString();
        System.out.println(stexto);
        List<Map<String, Integer>> stylesMap =  new ArrayList<>();
        stylesMap.add(font_L124);
        stylesMap.add(font_C126);
        stylesMap.add(font_R128);
        String[] textsToPrint = new String[] { "TESTE",  "TESTE" , "TESTE" };


    }

    @SuppressLint("MissingPermission")
    private void Chama_Impressao() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()) { //Bluetooth esta ativo
            try {

                if (printerBluetooth == null)
                    acharPrinterBluetooth();
                if (printerBluetooth == null)
                    return;

                BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

                impressora.connect();
                try {
                    //btnPrint.setEnabled(false);
                    // ((TextView) btnPrint).setText("Imprimindo: " + impressora.getRemoteDevice().getName());


                    iniciarImpressora(impressora.getOutputStream());
                    EditText edtTexto = findViewById(R.id.edtLinha);
                    String stexto = edtTexto.getText().toString();
                    OutputStream out = impressora.getOutputStream();


                    out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                    out.write(EscPosBase.alignCenter()); //Centralizado
                    out.write(EscPosBase.getFontSize_2());


                    out.write("Documento Auxiliar do Bilhete".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    out.write("de Passagem Eletronico".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());


                    out.write("EMITIDO EM CONTINGENCIA".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    out.write("Pendente de Autorizacao".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    out.write("EMITIDO EM HOMOLOGACAO".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    out.write("SEM VALOR FISCAL".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());


                    out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    out.write(EscPosBase.nextLine());


                    out.write(EscPosBase.nextLine(3));
                    out.flush();
                } finally {
                    // btnPrint.setEnabled(true);
                    impressora.close();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", Toast.LENGTH_LONG).show();
        }
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



    //Imprimir Linhas
    public void Imprime_Linha(final String slinha){
        ThreadPoolManager.getInstance().executeTask(new Runnable(){

            @Override
            public void run() {
                //  if( mBitmap == null ){
                //     mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.test);
                //  }
                try {
                    EditText edtLinha = findViewById(R.id.edtLinha);
                    float size = 30;
                   // String stexto = edtLinha.getText().toString();
                    //AidlUtil.getInstance().printText(stexto, size, isBold, isUnderLine);
                    woyouService.printTextWithFont(slinha, "", 36,null);

                    woyouService.lineWrap(4, null);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }});
    }


    public void Abre_Config(){
        Activity_Dados = 7;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, ActivityConfig.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("Activity_Dados", "7");
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);

    }

    public void Consulta_Cadastros(){
        Activity_Dados = 8;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, EditaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("Activity_Dados", "8");
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);

    }

    private void checkPermission() {
        // Verifica necessidade de verificacao de permissao
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Solicita permissao
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        100);
            } else {
                Toast.makeText(this, "Não há permissão para utilizar a camera!", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        100);
            }
        }
    }

    public void Ler_QRcode(){
        Intent intentz = new Intent("com.google.zxing.client.android.SCAN");
        //QR_CODE_MODE: QRCODE , ONE_D_MODE: Codigo de barras
        intentz.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startForresult.launch(intentz);

    }

    public void Gerar_Token(){


        Activity_Dados = 60;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, WSActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("USUARIO", Nome_user);
        bundle.putString("COMANDO", "TOKEN");
        bundle.putString("Activity_Dados", "60");



        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);
    }

    public void ConsultaGratuidadeUsr(){

        ExecutBackgrund();



    }




    public void Assinar_String(){
        //edtNome.setText("32220527143718000193630020000011152000001928");
        String sval = edtNome.getText().toString();
        String scodmod = ""; //Modulus
        String scodexp = ""; //Expoente
        String scodd = ""; //D
        String scodp = ""; //P
        String scodq = ""; //Q
        String scoddp = ""; //DP
        String scoddq = ""; //DQ
        String scodinv = ""; //InverseQ

        byte[] data = new byte[0];
        try {
            data = sval.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //////////////////////
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            System.out.println("Erro 1: " + getExternalFilesDir("Download").getAbsolutePath() + "/KEYEMP.xml");
            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/KEYEMP.xml");
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeResponse = doc.getElementsByTagName("RSAKeyValue");
            for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                Node nNode = nodeResponse.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    System.out.println("Erro 2 Achei chave: " );
                    scodmod = eElement.getElementsByTagName("Modulus").item(0).getTextContent();
                    scodexp = eElement.getElementsByTagName("Exponent").item(0).getTextContent();
                    scodd = eElement.getElementsByTagName("D").item(0).getTextContent();
                    scodp = eElement.getElementsByTagName("P").item(0).getTextContent();
                    scodq = eElement.getElementsByTagName("Q").item(0).getTextContent();
                    scoddp = eElement.getElementsByTagName("DP").item(0).getTextContent();
                    scodq = eElement.getElementsByTagName("DQ").item(0).getTextContent();
                    scodinv = eElement.getElementsByTagName("InverseQ").item(0).getTextContent();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /////////////////////
        String schaveAssign = "";
        DB_EMP dbemp = new DB_EMP(MainActivity.this);
        String snaokey = dbemp.Busca_Dados_Emp(1, "Naokey");
        if (!snaokey.equals("S")) { //assinar usando chave privada
            schaveAssign = Funcoes_Android.assinar(data, scodmod, scodexp, scodd, scodp, scodq, scoddp, scoddq, scodinv);
        } else { //assinar usando arquivo PFX
           String senhacrt = dbemp.Busca_Dados_Emp(1, "Crtsen");
            File dircert = new File (getExternalFilesDir("Download").getAbsolutePath());
            schaveAssign = Funcoes_Android.assinar2(data, dircert, senhacrt);
        }


        schaveAssign = schaveAssign.replaceAll("\r", "");
        schaveAssign = schaveAssign.replaceAll("\t", "");
        schaveAssign = schaveAssign.replaceAll("\n", "");
        edtNome.setText(schaveAssign);
    }


    public void Limpar_Bilhetes(){
        String slimp = infLimpar("Após Deletar não será possível recuperar.\nDeseja realmente Deletar?", "BPE");
    }


    public String infLimpar(String sMsg, final String sBase) {
        String sretaviso = "";
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.informa_pendentes, null);

        final EditText edtaviso = view.findViewById(R.id.edtAviso);
        edtaviso.setText(sMsg);
        edtaviso.setEnabled(false);

        builder.setView(view)

                // Add action buttons
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                     if (sBase.equals("BPE")) {
                         DB_BPE dbclearbpe = new DB_BPE(MainActivity.this);
                         dbclearbpe.deletar_Bpe();
                     }


                    }
                })
                .setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();

                    }
                });

        android.app.AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#EE6363"));

        //mudar a cor do botao de recusar
        Button brncancel = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        brncancel.setTextColor(Color.parseColor("#20910f"));


        return  sretaviso;

    }








    public void Abre_Percursos(){


        DB_USR dbu = new DB_USR(MainActivity.this);
        DB_USR.UsrCursor cursor = dbu.RetornarUsr(DB_USR.UsrCursor.OrdenarPor.NomeCrescente);
        String sOK = "";
        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            String sfectur = cursor.getFectur();
            if (!sfectur.equals("S")){ //se nao tiver permissao incluir
                  String snome = cursor.getUsrnom();
                  String sid = dbu.Busca_Dados_Usr(snome, "ID");
                  dbu.Atualizar_Campo_Usr(sid, "Fectur", "S");
            }

        }



    }




    public void Abre_Venda(){
        Activity_Dados = 99;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, ViaActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("USUARIO", Nome_user);
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);


    }

    public void Abre_Tela_Pix(){
        String sid = "08850bec-a8dc-492f-b7eb-fcf079136aac";
        String ssecret = "0276f36d-9663-4b28-993a-f8fb945e5c9a";



        /*Activity_Dados = 5;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, PixActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("CHAVE", "00020126580014br.gov.bcb.pix013641326a9c-114e-431d-aeed-29f5773f163e52040000530398654040.015802BR5917Reginaldo Mariani6009Sao Paulo62070503***630472D6");
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startActivityForResult(myIntent, Activity_Dados);*/
    }

    /*public void Abre_Venda_CIELO(){
        Activity_Dados = 99;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, CieloActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("USUARIO", Nome_user);
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startActivityForResult(myIntent, Activity_Dados);


    }*/




    public void printByBluTooth(String content) {
        try {
            if (isBold) {
                BluetoothUtil.sendData(ESCUtil.boldOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.boldOff());
            }

            if (isUnderLine) {
                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.underlineOff());
            }

            if (record < 17) {
                BluetoothUtil.sendData(ESCUtil.singleByte());
                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(record)));
            } else {
                BluetoothUtil.sendData(ESCUtil.singleByteOff());
                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(record)));
            }

            BluetoothUtil.sendData(content.getBytes(mStrings[record]));
            BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
        }
        return (byte) res;
    }

    public  void Chamada_Assinc () {
        Activity_Dados = 6;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(MainActivity.this, WSActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("USUARIO", Nome_user);
        bundle.putString("COMANDO", "GERKEY");
        bundle.putString("Activity_Dados", "6");




        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
