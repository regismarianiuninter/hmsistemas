package com.example.usuario.bilhete1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.security.keystore.KeyInfo;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Response;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;
import woyou.aidlservice.jiuiv5.ICallback;

import com.example.usuario.bilhete1.Utils.Mode;
import com.example.usuario.bilhete1.Utils.PrintfBlueListActivity;
import com.example.usuario.bilhete1.Utils.PrintfManager;
import com.example.usuario.bilhete1.Utils.SharedPreferencesManager;
import com.example.usuario.bilhete1.Utils.Util;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.sunmi.utils.AidlUtil;
import com.sunmi.utils.BitmapUtil;
import com.sunmi.utils.BluetoothUtil;
import com.sunmi.utils.ESCUtil;
import com.sunmi.utils.ThreadPoolManager;
import com.sunmi.utils.MemInfo;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.ksoap2.serialization.SoapPrimitive;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.SystemClock.sleep;

import static com.example.usuario.bilhete1.Utils.PrintfManager.boldOff;
import static com.example.usuario.bilhete1.Utils.PrintfManager.boldOn;

import com.example.usuario.bilhete1.Bpe_Consulta;
import com.x4fare.mobipix.onboard.sdk.LocalTransactionStatus;
import com.x4fare.mobipix.onboard.sdk.MobiPixOnboard;
import com.x4fare.mobipix.onboard.sdk.OnboardCallback;
import com.x4fare.mobipix.onboard.sdk.RemoteTransactionStatus;
import com.x4fare.mobipix.onboard.sdk.dto.ChargeRiderResponseDTO;
import com.x4fare.mobipix.onboard.sdk.dto.InitResponseDTO;
import com.x4fare.mobipix.onboard.sdk.dto.MobiPixResponseDTO;
import com.x4fare.mobipix.onboard.sdk.dto.VoidTransactionResponseDTO;
import com.example.usuario.bilhete1.Utils.PrintfManager;


public class ViaActivity extends AppCompatActivity {

    private EditText edtViagem, edtOrigem, edtDestino;
    private List<BilhetesModel> Bilhetes;
    BilhetesModel currentModel;
    private String viagem, trecho;
    private List<String> Viagens = new ArrayList<String>();
    List<String> opcoes;
    private static int Activity_Dados = 1;
    private static String Guarda_Texto = "";
    private static final String TAG = "bilhete1";
    public static final int DO_PRINT = 0x10001;
    private IWoyouService woyouService;
    private MobiPixOnboard mobipix;
    private byte[] inputCommand ;
    private final int RUNNABLE_LENGHT = 2;
    private Random random = new Random();
    private boolean isBold, isUnderLine;
    private int record;
    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};
    private ICallback callback = null;
    private  TextView info;
    Bitmap mBitmap;
    private ListView listViewBPe;
    private ListView listMenu;
    private ArrayAdapter<String> mAdapter;
    SoapPrimitive resultString;
    private static String Nome_user = "";
    private static String Linha_Trab = "";
    private static String Nome_Arquivo = "";
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
    private static String TipviaWS = "";
    private static String PagWS = "";
    private static String WScomando = "";
    private static String SmotivoWS = "";
    private static String CancelarWS = "";
    private static String Retpendentes = "";
    private static String RetWSActivity = "";
    private static String TipoDoc = "";
    private static String HoraInicio = "";
    private static String UltimoTouch = "";
    private static boolean EstouConectado = false;
    private static String MsgAgrupa = "";
    private static int iTotpendentes = 0;
    private static int iPendentes = 0;
    private static String GuardaNome = "";
    private static String GuardaDoc = "";
    private static String GuardaCPF = "";
    private static String GuardaCad = "";
    private static Double GuardaValor = 0.00;
    private static String GuardaVia = "";
    private static String GuardaOri = "";
    private static String GuardaDes = "";
    private static String EnviaConexao = "";
    private static String GuardaLinha = "";
    private static String GuardaPagto = "";
    private static String GuardaBand = "";
    private static String GuardaAut = "";
    private static String GuardaID = "";
    private static int GuardaQtd = 1;
    private static String Guarda_valor = "";
    private static String GuardaMobi = "";
    private static String Guarda_QRcode = "";
    private static String Guarda_Activity = "";
    private static String CPFUSR = "";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    View viewConexao;



    private HashMap<String, Integer> alignCenter = new HashMap<>();
    private HashMap<String, Integer> alignLeft = new HashMap<>();
    private HashMap<String, Integer> alignRight = new HashMap<>();
    private HashMap<String, Integer> font_C116 = new HashMap<>();
    private HashMap<String, Integer> font_C118 = new HashMap<>();
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
    private BluetoothSocket bluetoothSocket;
    private BluetoothAdapter bluetoothAdapter;
    private StringBuilder msgBuilder = new StringBuilder();
    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
    private DB_BPE.BpeCursor cursore;
    private ActivityResultLauncher<Intent> forResult;
    private View viewDialog;
    private PrintfManager printfManager;
    private List<Mode> listData;
    private Context context;
    private OutputStream outputStream;
    private String sgtpEmis = "";
    private String sgxdesconto = "";
    private String sgQRcode = "";
    private String sgtottrib = "";






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

    // Interface aninhada dentro da ViaActivity
    public interface LinhaSelecionadaCallback {
        void onLinhaSelecionada(String linha);
    }

    public void Select_Linha2(LinhaSelecionadaCallback callback) {
        ContextThemeWrapper cw = new ContextThemeWrapper(this, R.style.AlertDialogTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(cw);

        builder.setTitle("Em qual Linha vamos trabalhar?");

        final String[] linhas;
        String sLinha = "";
        List<String> itens = new ArrayList<>();
        DB_LIN db = new DB_LIN(ViaActivity.this);
        DB_LIN.LinCursor cursor = db.RetornarLin(DB_LIN.LinCursor.OrdenarPor.NomeCrescente);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            sLinha = (sLinha + cursor.getCodigo() + "-" + cursor.getDescri()) + ",";
        }

        linhas = sLinha.split(",");

        final String[] selectedLinha = {""};

        builder.setSingleChoiceItems(
                linhas,
                -1,
                (dialogInterface, i) -> {
                    String selectedItem = Arrays.asList(linhas).get(i);
                    if (!selectedItem.equals("")) {
                        int posL = selectedItem.indexOf("-");
                        selectedLinha[0] = selectedItem.substring(0, posL);
                    }
                });

        builder.setPositiveButton("Confirmar", null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(v -> {
            String slinSel = selectedLinha[0];
            if (!slinSel.equals("")) {
                System.out.println("Estou dentro Select_Linha: "+slinSel);
                Linha_Trab = slinSel;
                dialog.dismiss();
                if (callback != null) {
                    callback.onLinhaSelecionada(Linha_Trab);
                }
                DB_EMP dbempv = new DB_EMP(ViaActivity.this);
                String sconvei = dbempv.Busca_Dados_Emp(1, "Convei");
                if (sconvei.equals("S")) {
                    infVeiculo();
                }
            } else {
                Toast.makeText(ViaActivity.this, "Selecione uma Linha.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private final int MSG_TEST = 1;
    private long printCount = 0;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == MSG_TEST){
                // testAll();
                long mm = MemInfo.getmem_UNUSED(ViaActivity.this);
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        record = 17;
        isBold = false;
        isUnderLine = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_via);

        final EditText edtVia = findViewById(R.id.edtVia);
        edtVia.setEnabled(false);
        final EditText edtOri = findViewById(R.id.edtOrigem);
        edtOri.setEnabled(false);
        final EditText edtDes = findViewById(R.id.edtDestino);
        edtDes.setEnabled(false);
        final EditText edtpas = findViewById(R.id.edtNomePas);
        edtpas.setEnabled(false);
        final EditText edtdoc = findViewById(R.id.edtDocPas);
        edtdoc.setEnabled(false);
        final EditText edtcpfpas = findViewById(R.id.edtCPFPas);
        edtcpfpas.setEnabled(false);
        final EditText edttipgra = findViewById(R.id.edtTipGra);
        edttipgra.setEnabled(false);
        final EditText edttipdes = findViewById(R.id.edtTipDes);
        edttipdes.setEnabled(false);

        final TextView edtqtd = findViewById(R.id.edtQtd);

        edtqtd.setText("1");



        ImageView imageView = findViewById(R.id.animacao);
        imageView.setVisibility(View.INVISIBLE);

        Intent Newintent = getIntent();
        Bundle bundle = Newintent.getExtras();
        String user = bundle.getString("USUARIO");
        String sorigemchamado = bundle.getString("USUARIO");
        Nome_user = user;
        Guarda_Activity = sorigemchamado;
        context = this;
        ///Ativar Impressora AR-2500
        initData();

        // Verificar permissões Bluetooth (necessário para API 31+)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.BLUETOOTH_CONNECT,
                            android.Manifest.permission.BLUETOOTH_SCAN},
                    1);
        }



        Button btnPagamento = findViewById(R.id.btnPagamento);
        DB_EMP ddemp = new DB_EMP(ViaActivity.this);
        String scartao = ddemp.Busca_Dados_Emp(1, "Cartao");
        if (scartao.equals("S")) {
            btnPagamento.setEnabled(true);
            //StartCielo();
        } else {
            btnPagamento.setEnabled(false);
        }

        Button btnpay = findViewById(R.id.btnPay);
        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/MOBIPIX.xml");
        if (fXmlFile.exists()) {
           btnpay.setEnabled(true);
        } else {
            btnpay.setEnabled(false);
        }

        //setStyles(18, 0);
        String slinhatrab = Linha_Trab;
        System.out.println("Vou chamar Select_Linha: "+slinhatrab);
        if (slinhatrab.equals("")) {
            // Select_Linha();

            Select_Linha2(new LinhaSelecionadaCallback() {
                @Override
                public void onLinhaSelecionada(String linha) {
                    //Toast.makeText(ViaActivity.this, "Linha selecionada: " + linha, Toast.LENGTH_LONG).show();
                    // Coloque aqui o que você quer fazer após a seleção
                    Linha_Trab = linha;
                }
            });

            slinhatrab = Linha_Trab;

        }


        //VINCULANDO O LISTVIEW DA TELA AO OBJETO CRIADO
        listMenu = (ListView)this.findViewById(R.id.itens_Menu);
        listViewBPe = (ListView)this.findViewById(R.id.itens_Menu);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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


        ///////
        System.out.println("Retornei da Select_Linha: "+slinhatrab);
        if (!printfManager.isConnect()) {
            Log.d(TAG, "Impressora não conectada, abrindo PrintfBlueListActivity");
            PrintfBlueListActivity.startActivity(ViaActivity.this);

        }


        ////Setar Seguro Facultativo como padrao true
        final CheckBox ckbSeguro = findViewById(R.id.ckbSeguro);
        ckbSeguro.setChecked(true);
        ////Funcao que avisa quando o Seguro foi marcado ou desmarcado
        ckbSeguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckbSeguro.isChecked()) {

                    Toast.makeText(ViaActivity.this, "O Valor do Seguro foi incluso.", Toast.LENGTH_LONG).show();
                    Confere_campos();
                }
                else if (!ckbSeguro.isChecked())
                {
                    Toast.makeText(ViaActivity.this, "O Valor do Seguro foi retirado.", Toast.LENGTH_LONG).show();
                    Confere_campos();
                }
            }
        });



        Button btnViagem = findViewById(R.id.btnVia);
        btnViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Dados = 1;
                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                String txt = "VIAGEM";
                Bundle bundle = new Bundle();

                bundle.putString("txt", txt);
                bundle.putString("USUARIO", Nome_user);
                bundle.putString("LINHA", Linha_Trab);
                bundle.putString("Activity_Dados", "1");
                myIntent.putExtras(bundle);

                // chama esse intent e aguarda resultado
                startForresult.launch(myIntent);

            }
        });

        Button btnOrigem = findViewById(R.id.btnOri);
        btnOrigem.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtVia = findViewById(R.id.edtVia);
                String sVia = edtVia.getText().toString();
                if (!sVia.equals("Selecione a Viagem")) { //somente procurar trecho se existit viagem selecionada
                    int posV = sVia.indexOf("-");
                    String sCodVia = sVia.substring(0, (posV));

                    Activity_Dados = 2;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                    String txt = "TRECHO";
                    Bundle bundle = new Bundle();

                    bundle.putString("txt", txt);
                    bundle.putString("viagem", sCodVia);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("LINHA", Linha_Trab);
                    bundle.putString("Activity_Dados", "2");
                    myIntent.putExtras(bundle);

                    // chama esse intent e aguarda resultado
                    startForresult.launch(myIntent);
                }

            }
        });

        Button btnDestino = findViewById(R.id.btnDes);
        btnDestino.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtVia = findViewById(R.id.edtVia);
                String sVia = edtVia.getText().toString();
                if (!sVia.equals("Selecione a Viagem")) { //somente procurar trecho se existit viagem selecionada
                    int posV = sVia.indexOf("-");
                    String sCodVia = sVia.substring(0, (posV));

                    Activity_Dados = 3;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                    String txt = "TRECHO";
                    Bundle bundle = new Bundle();

                    bundle.putString("txt", txt);
                    bundle.putString("viagem", sCodVia);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("LINHA", Linha_Trab);
                    bundle.putString("Activity_Dados", "3");
                    myIntent.putExtras(bundle);

                    // chama esse intent e aguarda resultado
                    startForresult.launch(myIntent);
                }

            }
        });

        //botao de cadastro do passageiro da gratuidade
        findViewById(R.id.btnIdent).setOnClickListener(Verifica_campos_viagem);



        Button btnmais = findViewById(R.id.btnMais);
        btnmais.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TextView edtqtd = findViewById(R.id.edtQtd);
                String sqtd = edtqtd.getText().toString();
                int iqtd = Integer.parseInt(sqtd);
                if (iqtd < 9) {
                    iqtd = iqtd + 1;
                    sqtd = Integer.toString(iqtd);
                    edtqtd.setText(sqtd);
                }
                Confere_campos();
            }
        });

        Button btnmenos = findViewById(R.id.btnMenos);
        btnmenos.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TextView edtqtd = findViewById(R.id.edtQtd);
                String sqtd = edtqtd.getText().toString();
                int iqtd = Integer.parseInt(sqtd);
                if (iqtd > 1) {
                    iqtd = iqtd - 1;
                    sqtd = Integer.toString(iqtd);
                    edtqtd.setText(sqtd);
                }
                Confere_campos();
            }
        });

        Button btnconexao = findViewById(R.id.btnConexao);
        btnconexao.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtvia = findViewById(R.id.edtVia);
                EditText edtori = findViewById(R.id.edtOrigem);
                EditText edtdes = findViewById(R.id.edtDestino);
                //guarda os dados para depois reposicionar
                GuardaVia = edtvia.getText().toString();
                GuardaOri = edtori.getText().toString();
                GuardaDes = edtdes.getText().toString();
                GuardaLinha = Linha_Trab;

                String sconex = infConexao();
            }
        });

        Button btnPagto = findViewById(R.id.btnPagamento);
        btnPagto.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TextView txtTarifa = findViewById(R.id.txtValor);
                String svalor = txtTarifa.getText().toString();
                DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                String sultbil = dbemp.Busca_Dados_Emp(1,"Ultbil");
                if (!svalor.equals("0.00")) {
                    svalor = svalor.replace(",","");
                    int ivalor = Integer.parseInt(svalor);
                    int idoc = Integer.parseInt(sultbil);
                    GuardaPagto = "";
                    GuardaBand = "";
                    GuardaAut = "";
                    GuardaID = "";
                }


            }
        });

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TextView txtTarifa = findViewById(R.id.txtValor);
                String svalor = txtTarifa.getText().toString();
                DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                GuardaPagto = "";
                Guarda_QRcode = "";
                if (!svalor.equals("0.00")) {
                    svalor = svalor.replace(",","");
                    int ivalor = Integer.parseInt(svalor);
                        //ivalor = (ivalor * 100);
                        String svalpay = Integer.toString(ivalor);
                        Guarda_valor = svalpay;
                        File fXmlFiledel = new File(getExternalFilesDir("Download").getAbsolutePath() + "/qrcode.xml");
                        boolean bfileexist = fXmlFiledel.exists();
                        if (bfileexist) {
                            fXmlFiledel.delete();
                        }
                        dbemp.Atualizar_Campo_Emp("1", "Ultqrc", "");
                        GuardaMobi = "";
                        openCamera(svalpay);


                }


            }
        });



        Button btnFinaliza = findViewById(R.id.btnFinalizar);
        btnFinaliza.setEnabled(false);
        btnFinaliza.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                PagWS = "";
                Inicia_Venda();
            }
        });







        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    CarregarBilhetesCadastrados();
                } else if (position == 1){
                    Abre_Consulta();
                } else if (position == 2){
                    String susuario = Nome_user;
                    if (susuario.equals("HMINFO") || susuario.equals("CAIXA")) { Abre_Parametros();}
                } else if (position == 3){
                    DB_USR dbusr = new DB_USR(ViaActivity.this);
                    String sfectur = dbusr.Busca_Dados_Usr(Nome_user,"Fectur");
                    if (sfectur.equals("S")) {Chama_Encerrar();}
                } else if (position == 4){
                    WScomando = "PENDENTES";
                    Chama_Verifica("");
                }
                //Toast.makeText(ViaActivity.this, "Item Selecionado: "+position+" - "+id, Toast.LENGTH_SHORT).show();
            }
        });



        Menu_Lateral();
        Inicia_Mobipix();

        DB_EMP dbemp = new DB_EMP(ViaActivity.this);
        DB_EMP.EmpCursor cursor = dbemp.RetornarEmp(DB_EMP.EmpCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);


        }

        if (cursor.getCount() != 0) { //se existe alguma empresa cadastrada
		    WScomando = "PENDENTES";
            Chama_Verifica("E");
        }

        //File sdCard = getExternalStorageDirectory();
        File sdCard = getExternalFilesDir("Download");
        File dir = new File(sdCard.getAbsolutePath() );
        dir.mkdirs();
        File file = new File(dir, "KEYEMP.xml");
        if (file.exists()) {


        } else {

            if (cursor.getCount() != 0) { //se existe alguma empresa cadastrada
                String sws = dbemp.Busca_Dados_Emp(1, "Endews");
                if (!sws.equals("")) { //se existe endereco de ws
                    WScomando = "KEY";
                    Activity_Dados = 19;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntentkey = new Intent(ViaActivity.this, WSActivity.class);

                    String txt = Nome_Arquivo;
                    Bundle bundlekey = new Bundle();

                    bundlekey.putString("USUARIO", Nome_user);
                    bundlekey.putString("COMANDO", "KEY");
                    bundle.putString("Activity_Dados", "19");
                    myIntentkey.putExtras(bundlekey);

                    // chama esse intent e aguarda resultado
                    startForresult.launch(myIntentkey);
                }
            }
        }




        // Inicia o processamento ao carregar a activity (se desejado)
        WScomando = "PENDENTES";
        Chama_Verifica("E");

       // Registre o ActivityResultLauncher no onCreate
        forResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    View vdialog = viewDialog;
                    EditText edtcpfcli = vdialog.findViewById(R.id.edtCPFPas);

                    if (result != null && result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Intent data = result.getData();
                            String sret = data.getStringExtra("msg");
                            String sorigemresult = data.getStringExtra("Activity_Dados");
                            if (sorigemresult.equals("21")) {

                                String scpfpas = CPFUSR;// edtcpfcli.getText().toString();
                                String snomepas = "";
                                String sdocpas = "";
                                String stipgra = "";
                                DB_GRA db_gra = new DB_GRA(ViaActivity.this);
                                DB_GRA.GraCursor cursorg = db_gra.RetornarGra(DB_GRA.GraCursor.OrdenarPor.NomeCrescente);
                                for (int i = 0; i < cursorg.getCount(); i++) {
                                    cursorg.moveToPosition(i);
                                    String scpfDB = cursorg.getCpfpas();
                                    if (!scpfDB.equals("")) {
                                        if (scpfDB.equals(scpfpas)) {
                                            snomepas = cursorg.getNompas();
                                            sdocpas = cursorg.getDocpas();
                                            stipgra = cursorg.getTipgra();
                                            EditText edtnomepas = vdialog.findViewById(R.id.edtnome);
                                            EditText edtdocpas = vdialog.findViewById(R.id.edtrg);
                                            Spinner spntipgra = vdialog.findViewById(R.id.spnTipgra);
                                            edtnomepas.setText(snomepas);
                                            edtdocpas.setText(sdocpas);
                                            if (!stipgra.equals("")) {
                                                if (stipgra.equals("02")) {
                                                    stipgra = "02 - IDOSO";
                                                } else if (stipgra.equals("03")) {
                                                    stipgra = "03 - CRIANCA";
                                                } else if (stipgra.equals("04")) {
                                                    stipgra = "04 - DEFICIENTE FISICO";
                                                } else if (stipgra.equals("99")) {
                                                    stipgra = "99 - TEA - TRANSTORNO DO ESPECTRO AUTISTA";
                                                }
                                            } else {
                                                stipgra = "00 - SEM GRATUIDADE";
                                            }
                                            if (!stipgra.equals(null)) {
                                                ArrayAdapter spntipgraAdapter = ArrayAdapter.createFromResource(
                                                        ViaActivity.this,
                                                        R.array.gratuidade_array,
                                                        android.R.layout.simple_spinner_dropdown_item
                                                );
                                                int spinnerPosition = spntipgraAdapter.getPosition(stipgra);
                                                spntipgra.setSelection(spinnerPosition);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        );

    }

    private void initData() {
        try {
            printfManager = PrintfManager.getInstance(ViaActivity.this);
            if (printfManager == null) {
                Log.e(TAG, "Falha ao inicializar PrintfManager");
                Util.ToastText(context, "Erro: Falha ao inicializar impressora");
                return;
            }
            //listData = new ArrayList<>();
            //listData.add(new Mode("Test-P26", 200, 320));
            //listData.add(new Mode("Test-P16", 20, 188));
            printfManager.defaultConnection(ViaActivity.this);
        } catch (Exception e) {
            Log.e(TAG, "Erro em initData: " + e.getMessage());
            Util.ToastText(context, "Erro ao inicializar dados");
            PrintfBlueListActivity.startActivity(ViaActivity.this);
        }
    }

    private int currentIndex = 0; // Índice do bilhete atual


    public void Envia_Pendentes_DB() {
        RetWSActivity = "";
        iPendentes = 0;
        currentIndex = 0;
        DB_BPE db = new DB_BPE(ViaActivity.this);
        cursore = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

        System.out.println("Iniciando Envia_Pendentes_DB com iTotpendentes: " + iTotpendentes);

        if (iTotpendentes == 0) {
            infMensagens("Nenhum bilhete pendente encontrado.", "");
            System.out.println("Nenhum bilhete pendente, encerrando o processamento.");
            return;
        }

        //progressText.setText("Processando: 0/" + iTotpendentes);
        enviarProximoBilhete(); // Inicia o envio do primeiro bilhete
    }

    private void enviarProximoBilhete() {
        if (currentIndex >= cursore.getCount()) {
            return; // Todos os bilhetes foram processados
        }

        cursore.moveToPosition(currentIndex);
        String stransf = cursore.getTransf();
        if (stransf.equals("")) {
            String ssit = cursore.getSitbpe();
            if (ssit.equals("CT") || ssit.equals("DG")) {
                System.out.println("Pendente BP-e " + cursore.getNumbpe());

                Intent intent = criarIntentBilhete(cursore);
                String snumero = intent.getStringExtra("XML").substring(0, 44).substring(25, 34);
                String sdatchamada = Funcoes_Android.getCurrentUTC();
                System.out.println(snumero + " - " + sdatchamada + " Chamei Transmissao Pendentes: Cancela=" + CancelarWS);

                System.out.println("Lançando WSActivity para bilhete: " + snumero);
                startForresult.launch(intent);
                currentIndex++; // Avança para o próximo bilhete
                return; // Sai do método, espera o callback
            }
        }
        currentIndex++;
        enviarProximoBilhete(); // Continua procurando o próximo bilhete pendente
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        String sdatt = Funcoes_Android.getCurrentUTC();

        String sultdat = UltimoTouch;
        if (!sultdat.equals("")) {
            String shorini = sultdat.substring(11, 16); //hora anterior
            String shorfin = sdatt.substring(11, 16); //hora atual

            //Verifica se a transmissao demorou mais de 1 minuto
            long intervalo = Funcoes_Android.validaDate(shorini, shorfin);
            if (intervalo > 30) {
			    WScomando = "PENDENTES";
                Chama_Verifica("");
            }

            UltimoTouch = sdatt;
        } else {
            UltimoTouch = sdatt;
        }

        return super.dispatchTouchEvent(ev);
    }

    private void openCamera(String svalor){
        DB_EMP dbemp = new DB_EMP(ViaActivity.this);
        String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled() || !smodimp.equals("03") || !smodimp.equals("05")) {//se estiver ligado ou se for outro modelo de impressora
            Guarda_valor = svalor;
            Activity_Dados = 104;
            //Intent intentz = new Intent("com.google.zxing.client.android.SCAN");
            Intent intentz = new Intent(getApplicationContext(),CaptureActivity.class);
            intentz.setAction("com.google.zxing.client.android.SCAN");
            intentz.putExtra("SAVE_HISTORY", false);
            // QR_CODE_MODE: QRCODE , ONE_D_MODE: Codigo de barras
            intentz.putExtra("SCAN_MODE", "QR_CODE_MODE");
            Bundle bundle = new Bundle();
            bundle.putString("Activity_Dados", "104");
            intentz.putExtras(bundle);

            // chama esse intent e aguarda resultado
            startForresult.launch(intentz);;
        } else {
            //Bluetooth desativado
            infMensagens("Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", "");
        }


    }



    View.OnClickListener Verifica_campos_viagem = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final EditText edtVia = findViewById(R.id.edtVia);
            String sVia = edtVia.getText().toString();

            EditText edtOri = findViewById(R.id.edtOrigem);
            String sOri = edtOri.getText().toString();

            EditText edtDes = findViewById(R.id.edtDestino);
            String sDest = edtDes.getText().toString();

            String sDefaultViagem = "Selecione a Viagem";
            String sDefaultOrigem = "Selecione a Origem";
            String sDefaultDestino = "Selecione o Destino";

            if(sVia.equals(sDefaultViagem) || sOri.equals(sDefaultOrigem) || sDest.equals(sDefaultDestino)){
                CharSequence text = "Preencha os campos Viagem, Origem e Destino!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(ViaActivity.this , text, duration);
                toast.show();
            }else{
                infPassageiro(v);
            }

        }
    };

   public void Inicia_Venda() {
       DB_EMP dbemp = new DB_EMP(ViaActivity.this);
       String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
       BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
       if (bluetoothAdapter.isEnabled() || !smodimp.equals("03") || !smodimp.equals("05")) {//se estiver ligado ou se for outro modelo de impressora
           TextView edtqtd = findViewById(R.id.edtQtd);
           String sqtd = edtqtd.getText().toString();
           int iqtd = Integer.parseInt(sqtd);
           GuardaQtd = iqtd;
           Chama_Fechamento();
           edtqtd.setText("1");
       } else {
           //Bluetooth desativado
           infMensagens("Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", "");
       }

   }

   public void Inicia_Mobipix() {
        DB_EMP dbemp = new DB_EMP(ViaActivity.this);
       String stipamb = dbemp.Busca_Dados_Emp(1, "Tipamb");
       String sambiente = "";
       String sIDApi = "";
       String sKeyApi = "";
       if (stipamb.equals("1")) { sambiente = "PROD";} //Producao
       if (!stipamb.equals("1")) { sambiente = "QA";} //Ambiente de testes
       //Procurar XML com as chaves
       try {
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/MOBIPIX.xml");
           Document doc = dBuilder.parse(fXmlFile);
           doc.getDocumentElement().normalize();
           NodeList nodeResponse = doc.getElementsByTagName("MobipixValues");
           for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

               Node nNode = nodeResponse.item(temp);
               if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                   Element eElement = (Element) nNode;


                   sIDApi = eElement.getElementsByTagName("AppID").item(0).getTextContent();
                   sKeyApi = eElement.getElementsByTagName("ApiKey").item(0).getTextContent();

               }
           }

       } catch (Exception e) {
           e.printStackTrace();
       }


       try {
               com.x4fare.mobipix.onboard.sdk.MobiPixOnboard.init(ViaActivity.this, sambiente, sIDApi, sKeyApi, "HM", new OnboardCallback<MobiPixResponseDTO<InitResponseDTO>>() {
                   @Override
                   public void onSuccess(MobiPixResponseDTO<InitResponseDTO> response) {
                       // processar a logica de sucesso
                       Log.i(TAG,"callbackpay INI: " + response.getStatus());
                   }

                   @Override
                   public void onError(MobiPixResponseDTO<InitResponseDTO> response) {
                       // processar a logica de sucesso
                   }
               });


       } catch (Exception e) {
           Log.i(TAG,"Erro Callback: " + e.toString());
           infMensagens("Erro: "+e.toString(), "");
       }

   }



    public void Chama_Fechamento() {
        EditText edtcad = findViewById(R.id.edtPoltrona);
        String scad = edtcad.getText().toString();


        String svai = "";
        if (!scad.equals("")) {
            int icad = Integer.parseInt(scad);
            if (icad < 60) { svai = "S";}
        } else {
            svai = "S";
        }
        if (svai.equals("S")) {
            TextView txtvalor = findViewById(R.id.txtValor);
            String sval = txtvalor.getText().toString();
            if (!sval.equals("0.00")) {
                Calcula_Valor_DB();
                WScomando = "CONSULTA";
                Activity_Dados = 17;
                //WScomando = "TRANSMITE";
                //Activity_Dados = 12;
                ExecutBackgrundTESTE();
                //ViaActivity.Teste_Conexao newtest = new ViaActivity.Teste_Conexao();
                //newtest.execute();
            }
        } else {
            Toast.makeText(ViaActivity.this, "Número de Poltrona Inválido.", Toast.LENGTH_SHORT).show();
        }
    }



    //Funcao que chama Dialog para selecionar a Linha de trabalho
    public void Select_Linha(){
        // Initializing a new alert dialog
        ContextThemeWrapper cw = new ContextThemeWrapper( this, R.style.AlertDialogTheme );
        final AlertDialog.Builder builder = new AlertDialog.Builder( cw );

        // Set the alert dialog title
        builder.setTitle("Em qual Linha vamos trabalhar?");

        //Lista de itens
        final String[] linhas;
        String sLinha = "";
        List<String> itens = new ArrayList();
        DB_LIN db = new DB_LIN(ViaActivity.this);
        DB_LIN.LinCursor cursor = db.RetornarLin(DB_LIN.LinCursor.OrdenarPor.NomeCrescente);
        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            sLinha = (sLinha+cursor.getCodigo()+"-"+cursor.getDescri())+",";

        }

        linhas = sLinha.split(",");

        // Set a single choice items list for alert dialog


        builder.setSingleChoiceItems(
                linhas, // Items list
                -1, // Index of checked item (-1 = no selection)
                new DialogInterface.OnClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the alert dialog selected item's text
                        String selectedItem = Arrays.asList(linhas).get(i);
                        if (!selectedItem.equals("")) {
                            int posL = selectedItem.indexOf("-");
                            String sCodLinha = selectedItem.substring(0, (posL));
                            Linha_Trab = sCodLinha;


                        }
                    }
                });

        // Set the a;ert dialog positive button
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Just dismiss the alert dialog after selection
                // Or do something now

               // Toast.makeText(ViaActivity.this, "Confirmar: " + i, Toast.LENGTH_LONG).show();
            }
        });

        // Create the alert dialog
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog

        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new DialogButtonClickWrapper(dialog) {

            @Override
            protected boolean onClicked() {
                String slinSel = Linha_Trab;
                boolean bclick = true;
                if (!slinSel.equals("")){
                     bclick = true;
                    //return true;//Retornando true fecha o dialog
                    DB_EMP dbempv = new DB_EMP(ViaActivity.this);
                    String sconvei = dbempv.Busca_Dados_Emp(1, "Convei");
                    if (sconvei.equals("S")) { infVeiculo(); } //se controla veiculos
                } else if (slinSel.equals("")) {
                    Toast.makeText(ViaActivity.this, "Selecione uma Linha.", Toast.LENGTH_LONG).show();
                    bclick = false;
                    //return false;//Retornando false o dialog não é fechado
                }
                return bclick;
            }

        });
    }

    private void StartCielo() {
        /*String sID = "";
        String sToken = "";
        //////////////////////
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/IDEMP.xml");
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeResponse = doc.getElementsByTagName("DadosID");
            for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                Node nNode = nodeResponse.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    sID = eElement.getElementsByTagName("ID").item(0).getTextContent();
                    sToken = eElement.getElementsByTagName("Token").item(0).getTextContent();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////
        infoManager = new InfoManager();
        Credentials credentials = new Credentials( sID,sToken);
        orderManager = new OrderManager(credentials, this);

        PaymentListener paymentListener = new PaymentListener() {
            @Override
            public void onStart() {
                Log.d("SDKClient", "O pagamento começou.");
            }

            @Override
            public void onPayment(Order order) {
                Log.d("SDKClient", "Um pagamento foi realizado.");
            }

            @Override
            public void onCancel() {
                Log.d("SDKClient", "A operação foi cancelada.");
            }

            @Override
            public void onError(PaymentError paymentError) {
                Log.d("SDKClient", "Houve um erro no pagamento.");
                Log.d("SDKClient", paymentError.getDescription());
            }
        };*/
    }





    private void Start_Cielo() {
        /*String sID = "";
        String sToken = "";
        //////////////////////
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/IDEMP.xml");
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeResponse = doc.getElementsByTagName("DadosID");
            for (int temp = 0; temp < nodeResponse.getLength(); temp++) {

                Node nNode = nodeResponse.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    sID = eElement.getElementsByTagName("ID").item(0).getTextContent();
                    sToken = eElement.getElementsByTagName("Token").item(0).getTextContent();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //////////
        infoManager = new InfoManager();
        Credentials credentials = new Credentials( sID,sToken);
        orderManager = new OrderManager(credentials, this);
        orderManager.bind(this, null);*/
    }



    private void configSDK_Cancel(final String snumero, final String smotivo2) {
        /*orderManager.bind(this, new ServiceBindListener() {

            @Override public void onServiceBoundError(Throwable throwable) {
                Toast.makeText(getApplicationContext(),
                        String.format("Erro fazendo bind do servico de ordem",
                                throwable.getMessage()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceBound() {
                if (ordercancel != null && ordercancel.getPayments().size() > 0) {
                    Payment pgTO = ordercancel.getPayments().get(0);
                    CancellationRequest request = new CancellationRequest.Builder()
                            .orderId(pgTO.getId())
                            .authCode(pgTO.getAuthCode())
                            .cieloCode(pgTO.getCieloCode())
                            .value(pgTO.getAmount())
                            .build();

                    orderManager.cancelOrder(request, new CancellationListener() {

                        @Override
                        public void onSuccess(Order cancelledOrder) {
                            //Log.d(TAG, "ON SUCCESS");
                            cancelledOrder.cancel();
                            orderManager.updateOrder(cancelledOrder);

                            //Toast.makeText(ViaActivity.this, "CANCELADO COM SUCESSO", Toast.LENGTH_SHORT).show();
                            //ordercancel = cancelledOrder;
                            DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                            String IDBPE = dbbpe.Busca_Dados_Bpe(snumero, "ID");
                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Nidpag", "");
                            Chama_Cancela(snumero, smotivo2);
                            orderManager.unbind();
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG, "ON CANCEL");

                            Toast.makeText(ViaActivity.this, "OPERACAO ANULADA", Toast.LENGTH_SHORT).show();
                            orderManager.unbind();
                        }

                        @Override
                        public void onError(PaymentError paymentError) {
                            Log.d(TAG, "ON ERROR");

                            Toast.makeText(ViaActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                            orderManager.unbind();
                        }
                    });
                }


            }

            @Override
            public void onServiceUnbound() {

            }
        });
        orderManager.unbind();*/
    }




    public void cancelPayment(final String snumero, final String smotivo2) {

        /*if (ordercancel != null && ordercancel.getPayments().size() > 0) {
            Payment pgTO = ordercancel.getPayments().get(0);
            CancellationRequest request = new CancellationRequest.Builder()
                    .orderId(ordercancel.getId())
                    .authCode(pgTO.getAuthCode())
                    .cieloCode(pgTO.getCieloCode())
                    .value(pgTO.getAmount())
                    .build();

            orderManager.cancelOrder(request, new CancellationListener() {

                @Override
                public void onSuccess(Order cancelledOrder) {
                    cancelledOrder.cancel();
                    orderManager.updateOrder(cancelledOrder);

                    //Toast.makeText(ViaActivity.this, "SUCESSO", Toast.LENGTH_SHORT).show();
                    ordercancel = cancelledOrder;
                    DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                    String IDBPE = dbbpe.Busca_Dados_Bpe(snumero, "ID");
                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Nidpag", "");
                    Chama_Cancela(snumero, smotivo2);
                }

                @Override
                public void onCancel() {

                    Toast.makeText(ViaActivity.this, "CANCELADO", Toast.LENGTH_SHORT).show();
                    orderManager.unbind();
                }

                @Override
                public void onError(PaymentError paymentError) {

                    Toast.makeText(ViaActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                    orderManager.unbind();
                }
            });
            orderManager.unbind();
        }*/
    }



    public abstract class DialogButtonClickWrapper implements View.OnClickListener {

        private AlertDialog dialog;

        public DialogButtonClickWrapper(AlertDialog dialog) {
            this.dialog = dialog;
        }


        public void onClick(View v) {

            if(onClicked()){
                dialog.dismiss();
            }
        }

        protected abstract boolean onClicked();
    }


    //Chama Dialog para informar dados do Passageiro
    public void infPassageiro(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.passageiro, null);
        // Spinner element
        final Spinner spinner = view.findViewById(R.id.spnTipdoc);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.tipdoc_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        final Spinner spntipgra = view.findViewById(R.id.spnTipgra);
        ArrayAdapter spntipgraAdapter = ArrayAdapter.createFromResource(this, R.array.gratuidade_array,
                android.R.layout.simple_spinner_dropdown_item);
        spntipgra.setAdapter(spntipgraAdapter);

        final Spinner spntipdes = view.findViewById(R.id.spnTipdes);
        ArrayAdapter spntipdesAdapter = ArrayAdapter.createFromResource(this, R.array.desconto_array,
                android.R.layout.simple_spinner_dropdown_item);
        spntipdes.setAdapter(spntipdesAdapter);

        final EditText edtnomepas = view.findViewById(R.id.edtnome);
        edtnomepas.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                        EditText edtpas = findViewById(R.id.edtNomePas);
                        EditText edtdoc = findViewById(R.id.edtDocPas);
                        EditText edtcpfpas = findViewById(R.id.edtCPFPas);
                        EditText edttipgra = findViewById(R.id.edtTipGra);
                        EditText edttipdes = findViewById(R.id.edtTipDes);
                        edtpas.setText("");
                        edtdoc.setText("");
                        edtcpfpas.setText("");
                        edttipgra.setText("");
                        edttipdes.setText("");
                        TipoDoc = "";
                        dialog.dismiss();
                    }
                });



        AlertDialog alert = builder.create();
        alert.show();
        final EditText edtcpfcli = view.findViewById(R.id.edtCPF);
        edtcpfcli.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                }
            }
        });


        edtcpfcli.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() == 11){
                    //Procurar se Existe Dados no WS
                    String scpfcli = edtcpfcli.getText().toString();
                    //Procurar se existe cadastro
                    if (scpfcli.length() >=11 && !scpfcli.equals("CPF (Opcional)")) {
                        boolean bvalida = Funcoes_Android.validaCPF(scpfcli);
                        EditText edtpas = findViewById(R.id.edtNomePas);
                        EditText edtdoc = findViewById(R.id.edtDocPas);
                        EditText edttipgra = findViewById(R.id.edtTipGra);
                        String snome = edtpas.getText().toString();
                        String sdoc = edtdoc.getText().toString();
                        String stipgra = edttipgra.getText().toString();
                        if (bvalida) {
                            WScomando = "verificaUsuarioGratuidade";
                            CPFUSR = scpfcli;
                            Activity_Dados = 21;
                            String linha = Linha_Trab;
                            String origem = GuardaOri;
                            String destino = GuardaDes;
                            String dataatu = Funcoes_Android.getCurrentUTC();
                            String sdata = dataatu.substring(0,10);
                            String sano = sdata.substring(0, 4);
                            String smes = sdata.substring(5, 7);
                            String smesatu = (smes+sano);
                            //procura os dados do percurso para encontrar sentido da viagem
                            DB_PER dbp = new DB_PER(ViaActivity.this);
                            DB_PER.PercursosCursor cursorper = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);
                            String ssentido = "I";
                            for (int i = 0; i < cursorper.getCount(); i++) {
                                cursorper.moveToPosition(i);
                                if (cursorper.getLinha().equals(linha)) {//mesma linha
                                    if (cursorper.getOrigem().equals(origem)) {//mesma origem
                                        if (cursorper.getDestino().equals(destino)) {//mesmo destino
                                            ssentido = cursorper.getTipvia();
                                        }
                                    }
                                }
                            }

                            // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                            Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("USUARIO", Nome_user);
                            bundle.putString("CANCEL", "");
                            bundle.putString("COMANDO", WScomando);
                            bundle.putString("CPFUSR", CPFUSR);
                            bundle.putString("MESANO", smesatu);
                            bundle.putString("LINVIA", Linha_Trab);
                            bundle.putString("SENTIDO", ssentido);
                            bundle.putString("NOMEUSR", snome);
                            bundle.putString("DOCUSR", sdoc);
                            bundle.putString("TIPGRA", stipgra);
                            bundle.putString("Activity_Dados", "21");

                            myIntent.putExtras(bundle);

                            myIntent.putExtras(bundle);

                                System.out.println("Lançando verificaUsuarioGratuidade: ");
                                viewDialog = view;
                                forResult.launch(myIntent);



                        } else {
                            infMensagens("CPF inválido.\nDeve conter 11 Dígitos.", "");
                            //Toast.makeText(ViaActivity.this, "CPF inválido.\nDeve conter 11 Dígitos.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        ///TESTE
        Button theButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new DialogButtonClickWrapper(alert) {

            @Override
            protected boolean onClicked() {
                String svalida = "";
                String sERRO = "";
                EditText edtnome = view.findViewById(R.id.edtnome);
                EditText edtrg = view.findViewById(R.id.edtrg);
                EditText edtcpf = view.findViewById(R.id.edtCPF);
                Spinner spntipgra = view.findViewById(R.id.spnTipgra);
                Spinner spntipdes = view.findViewById(R.id.spnTipdes);
                EditText edtpas = findViewById(R.id.edtNomePas);
                EditText edtdoc = findViewById(R.id.edtDocPas);
                EditText edtcpfpas = findViewById(R.id.edtCPFPas);
                EditText edttipgra = findViewById(R.id.edtTipGra);
                EditText edttipdes = findViewById(R.id.edtTipDes);
                String snome = edtnome.getText().toString();
                String sdoc = edtrg.getText().toString();
                snome = snome.toUpperCase();
                sdoc = sdoc.toUpperCase();
                String scpf = edtcpf.getText().toString();
                String stipgra = spntipgra.getSelectedItem().toString();
                String stipdes = spntipdes.getSelectedItem().toString();
                edtpas.setText(snome);
                edtdoc.setText(sdoc);
                edtcpfpas.setText(scpf);
                stipgra = stipgra.substring(0,2);
                edttipgra.setText(stipgra);
                edttipdes.setText(stipdes.substring(0,1));
                String stipdoc = spinner.getSelectedItem().toString();
                TipoDoc = stipdoc;
                int qtddoc = sdoc.length();


                if ((qtddoc > 20) || (qtddoc < 2)) {
                    sERRO = sERRO + "Documento inválido.\nDeve conter entre 2 e 20 Dígitos.\n";
                    //infMensagens("Documento inválido.\nDeve conter entre 2 e 20 Dígitos.", "");
                    //Toast.makeText(ViaActivity.this, "Documento inválido.\nDeve conter entre 2 e 20 Dígitos.", Toast.LENGTH_LONG).show();
                    edtpas.setText("");
                    edtdoc.setText("");
                    edtcpfpas.setText("");
                    edttipgra.setText("");
                    edttipdes.setText("");
                    TipoDoc = "";
                    svalida = "E";
                }
                if (!scpf.equals("") && !scpf.equals("CPF (Opcional)")) { //se o CPF for informado fazer a validacao
                    boolean bvalida = Funcoes_Android.validaCPF(scpf);
                    if (!bvalida) {
                        //infMensagens("CPF inválido.", "");
                        sERRO = sERRO + "CPF inválido.\n";
                        // Toast.makeText(ViaActivity.this, "CPF inválido.", Toast.LENGTH_LONG).show();
                        edtpas.setText("");
                        edtdoc.setText("");
                        edtcpfpas.setText("");
                        edttipgra.setText("");
                        edttipdes.setText("");
                        TipoDoc = "";
                        svalida = "E";
                    }
                }
                if (!stipgra.equals("") && !stipgra.equals("00")) {
                    if (scpf.equals("") || scpf.equals("CPF (Opcional)")) { //se o CPF nao for informado
                        sERRO = sERRO + "Para Gratuidade é obrigatório informar o CPF.\n";
                        //infMensagens("Para Gratuidade é obrigatório informar o CPF.", "");
                        // Toast.makeText(ViaActivity.this, "Para Gratuidade é obrigatório informar o CPF.", Toast.LENGTH_LONG).show();
                        edtpas.setText("");
                        edtdoc.setText("");
                        edtcpfpas.setText("");
                        edttipgra.setText("");
                        edttipdes.setText("");
                        TipoDoc = "";
                        svalida = "E";

                    }
                    if (stipdes.equals("") || stipdes.equals("Sem Desconto")){
                        sERRO = sERRO + "Para Gratuidade é obrigatório \n Informar o Tipo de Desconto.\n";
                        //infMensagens("Para Gratuidade é obrigatório \n Informar o Tipo de Desconto.", "");
                        //Toast.makeText(ViaActivity.this, "Para Gratuidade é obrigatório \n Informar o Tipo de Desconto.", Toast.LENGTH_LONG).show();
                        edtpas.setText("");
                        edtdoc.setText("");
                        edtcpfpas.setText("");
                        edttipgra.setText("");
                        edttipdes.setText("");
                        TipoDoc = "";
                        svalida = "E";
                    }
                }

                //CASO SEJA GRATUIDADE, VALIDAR CONDICOES DE USO
                if (!stipgra.equals("") && !stipgra.equals("00")) {
                   int qtduse = 0;
                    String linha = Linha_Trab;
                    String origem = GuardaOri;
                    String destino = GuardaDes;
                    String dataatu = Funcoes_Android.getCurrentUTC();
                    String sdata = dataatu.substring(0,10);
                    String sano = sdata.substring(0, 4);
                    String smes = sdata.substring(5, 7);
                    String smesatu = (smes+sano);

                    //procura os dados do percurso para encontrar sentido da viagem
                    DB_PER dbp = new DB_PER(ViaActivity.this);
                    DB_PER.PercursosCursor cursorper = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);
                    String ssentido = "I";
                    for (int i = 0; i < cursorper.getCount(); i++) {
                        cursorper.moveToPosition(i);
                        if (cursorper.getLinha().equals(linha)) {//mesma linha
                            if (cursorper.getOrigem().equals(origem)) {//mesma origem
                                if (cursorper.getDestino().equals(destino)) {//mesmo destino
                                   ssentido = cursorper.getTipvia();
                                }
                            }
                        }
                    }

                    DB_GRA db_gra = new DB_GRA(ViaActivity.this);
                    DB_GRA.GraCursor cursor = db_gra.RetornarGra(DB_GRA.GraCursor.OrdenarPor.NomeCrescente);
                    for (int ib = 0; ib < cursor.getCount(); ib++) {
                        cursor.moveToPosition(ib);
                        String scpfDB = cursor.getCpfpas();
                        String smesDB = cursor.getMesano();
                        String slinhaDB = cursor.getLinha();
                        String ssentidoDB = cursor.getSentido();
                        if (!scpfDB.equals("")) { //se encontrou algumr egistro
                            if (scpfDB.equals(scpf) && slinhaDB.equals(linha) && smesDB.equals(smesatu) &&ssentidoDB.equals(ssentido)) {//se encontrou mesmos dados
                                 String sqtduse = cursor.getQtdpas();
                                 qtduse = Integer.parseInt(sqtduse);
                                 if (qtduse >= 2) {
                                     svalida = "E";
                                     sERRO = sERRO + "Usuario excedeu limite para esta linha no mês.\n";
                                 }
                            }
                        }
                    }
                }

                if(!svalida.equals("E")){
                    //Verificar se o cadastro existe, caso não exista, efeturar nesse momento
                    if (!stipgra.equals("") && !stipgra.equals("00")) {
                        String susergra = "";

                        DB_GRA db_gra = new DB_GRA(ViaActivity.this);
                        DB_GRA.GraCursor cursor = db_gra.RetornarGra(DB_GRA.GraCursor.OrdenarPor.NomeCrescente);
                        for (int ib = 0; ib < cursor.getCount(); ib++) {
                            cursor.moveToPosition(ib);
                            String scpfDB = cursor.getCpfpas();
                            if (!scpfDB.equals("")) { //se encontrou algumr egistro
                                if (scpfDB.equals(scpf)) {//se encontrou o passageiro
                                    susergra = "S";
                                }
                            }
                        }

                        if (!susergra.equals("S")) {
                            //se nao existir bo banco de dados, cadastrar
                            String linha = Linha_Trab;
                            String origem = GuardaOri;
                            String destino = GuardaDes;
                            String dataatu = Funcoes_Android.getCurrentUTC();
                            String sano = dataatu.substring(0, 4);
                            String smes = dataatu.substring(5, 2);
                            String smesatu = (smes+sano);
                            //procura os dados do percurso para encontrar sentido da viagem
                            DB_PER dbp = new DB_PER(ViaActivity.this);
                            DB_PER.PercursosCursor cursorper = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);
                            String ssentido = "I";
                            for (int i = 0; i < cursorper.getCount(); i++) {
                                cursorper.moveToPosition(i);
                                if (cursorper.getLinha().equals(linha)) {//mesma linha
                                    if (cursorper.getOrigem().equals(origem)) {//mesma origem
                                        if (cursorper.getDestino().equals(destino)) {//mesmo destino
                                            ssentido = cursorper.getTipvia();
                                        }
                                    }
                                }
                            }

                            WScomando = "CONSULTA_GRATUIDADE";
                            Activity_Dados = 22;
                            // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                            Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putString("USUARIO", Nome_user);
                            bundle.putString("CANCEL", "");
                            bundle.putString("COMANDO", WScomando);
                            bundle.putString("CPFUSR", CPFUSR);
                            bundle.putString("MESANO", smesatu);
                            bundle.putString("LINVIA", Linha_Trab);
                            bundle.putString("SENTIDO", ssentido);
                            bundle.putString("NOMEUSR", snome);
                            bundle.putString("DOCUSR", sdoc);
                            bundle.putString("TIPGRA", stipgra);
                            bundle.putString("Activity_Dados", "22");

                            myIntent.putExtras(bundle);

                            myIntent.putExtras(bundle);

                            System.out.println("Lançando CONSULTA_GRATUIDADE: ");
                            viewDialog = view;
                            forResult.launch(myIntent);



                       }
                    }
                    return true;//Retornando true fecha o dialog
                } else {
                    infMensagens(sERRO, "");
                    return false;//Retornando false o dialog não é fechado
                }
            }
        });
        //////

        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#20910f"));

    }



    //Chama Dialog para informar Motivo de cancelamento ou nao embarque
    public String infMotivo(View v, final String snumero) {
        String sretmot = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.informa_motivo, null);

        // Spinner element
        final Spinner spinner = view.findViewById(R.id.spnMotivos);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.motivos_array,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        final EditText edtmotivo2 = view.findViewById(R.id.edtMotivo);
        edtmotivo2.setEnabled(false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                //int item = arg0.getSelectedItemPosition();
                if (arg2 == 5) {
                    edtmotivo2.setEnabled(true);
                    Toast.makeText(ViaActivity.this, "Digite o motivo por favor.", Toast.LENGTH_LONG).show();
                } else {
                    edtmotivo2.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        builder.setView(view)

                // Add action buttons
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Spinner spinner = view.findViewById(R.id.spnMotivos);

                        EditText edtmotivo2 = view.findViewById(R.id.edtMotivo);
                        String smotivo1 = spinner.getSelectedItem().toString();
                        String smotivo2 = edtmotivo2.getText().toString();
                        int iitem = spinner.getSelectedItemPosition();
                        if (iitem == 5) {
                            if (smotivo2.equals("")) {
                                Toast.makeText(ViaActivity.this, "Motivo Inválido. ", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            smotivo2 = smotivo1;
                        }

                        SmotivoWS = smotivo2;
                        Log.i(TAG,"smotivo2: " + smotivo2);
                        if (!smotivo2.equals("")) {
                                Chama_Cancela(snumero, smotivo2);

                        }

                    }
                })
                .setNegativeButton("Retornar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                        SmotivoWS = "";
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#EE6363"));

        sretmot = SmotivoWS;
        return  sretmot;

    }


    //Chama Dialog para informar Mensagens de retorno, erro etc...
    public String infMensagens(final String sMsg, String sGrave) {
        String sretmot = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.informa_mensagens, null);


        final EditText edtmens = view.findViewById(R.id.edtMensagem);
        edtmens.setText(sMsg);
        edtmens.setEnabled(false);


        builder.setView(view)

                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {



                    }
                });

        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#20910f"));
       if (sGrave.equals("S")) {
           pbutton.setEnabled(false);

       }
        sretmot = SmotivoWS;
        return  sretmot;

    }


    //Chama Dialog para informar Mensagens de retorno, erro etc...
    public String infVeiculo() {
        String sretmot = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.informa_veiculo, null);


        final EditText edtmens = view.findViewById(R.id.edtMensagem);
        edtmens.setText("Confirma Veículo?");
        edtmens.setEnabled(false);

        final DB_EMP dbempvei = new DB_EMP(ViaActivity.this);
        final String splaca = dbempvei.Busca_Dados_Emp(1, "Rsv002");
        // Spinner element
        final Spinner spinner = view.findViewById(R.id.spnPlaca);
        opcoes = new ArrayList<String>();
        File sarquivo = new File(getExternalFilesDir("Download").getAbsolutePath() );
        LinkedList<String> linhas = null;

        try {
            File file = new File(sarquivo, "/VEICULOS.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
            String recebe_string;
            linhas = new LinkedList<String>();
            while ((recebe_string = buffer.readLine()) != null) {
                linhas.add(recebe_string);

                String[] linhaDoArquivo = recebe_string.split(";");
                String splavei = linhaDoArquivo[0];
                opcoes.add(splavei);
            }


            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(ViaActivity.this, android.R.layout.simple_spinner_dropdown_item, opcoes);
        spinner.setAdapter(adapterSpinner);
        spinner.setSelection(adapterSpinner.getPosition(splaca));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                ((TextView) arg0.getChildAt(0)).setTextSize(24);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                ((TextView) arg0.getChildAt(0)).setTextSize(24);
            }
        });

        builder.setView(view)

                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         String splaca2 = spinner.getSelectedItem().toString();
                         if (!splaca2.equals("") && !splaca2.equals(splaca)) {
                             splaca2 = splaca2.replace("-", "");
                             splaca2 = splaca2.replace(".", "");
                             splaca2 = splaca2.replace("/", "");
                             splaca2 = splaca2.toUpperCase();
                             dbempvei.Atualizar_Campo_Emp("1", "Rsv002", splaca2);


                         }

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#20910f"));

        sretmot = SmotivoWS;
        return  sretmot;

    }


    //Chama Dialog para informar Conexao com outra Linha
    public String infConexao() {
        String sretmot = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        final LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.abre_conexao, null);
        viewConexao = view;

        final EditText edtlinhac = view.findViewById(R.id.edtLinhac);
        edtlinhac.setEnabled(false);
        final EditText edtviac = view.findViewById(R.id.edtViac);
        edtviac.setEnabled(false);
        final EditText edtoric = view.findViewById(R.id.edtOrigemc);
        edtoric.setEnabled(false);
        final EditText edtdesc = view.findViewById(R.id.edtDestinoc);
        edtdesc.setEnabled(false);
        final TextView txtvalorini = view.findViewById(R.id.txtValorini);
        String svalini = String.format("%.2f", GuardaValor);
        txtvalorini.setText(svalini);
        final TextView txtvalorc = view.findViewById(R.id.txtValorc);
        txtvalorc.setText("0.00");
        final TextView txtvalortot = view.findViewById(R.id.txtValortot);
        txtvalortot.setText(svalini);

        ////Setar Seguro Facultativo como padrao true
        final CheckBox ckbSeguroc = view.findViewById(R.id.ckbSeguroc);
        ckbSeguroc.setChecked(true);

        final TextView edtQtdcone = view.findViewById(R.id.edtQtdcone);
        edtQtdcone.setEnabled(false);
        edtQtdcone.setText("1");
        final Button btnmaiscome = view.findViewById(R.id.btnMaiscone);
        final Button btnmenoscone = view.findViewById(R.id.btnMenoscone);
        btnmaiscome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sqtdcone = edtQtdcone.getText().toString();
                int iqtdcone = Integer.parseInt(sqtdcone);
                if (iqtdcone < 9) {
                    iqtdcone = iqtdcone + 1;
                    sqtdcone = Integer.toString(iqtdcone);
                    edtQtdcone.setText(sqtdcone);
                }
                Confere_campos_DG(view);
            }
        });

        btnmenoscone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sqtdcone = edtQtdcone.getText().toString();
                int iqtdcone = Integer.parseInt(sqtdcone);
                if (iqtdcone > 1) {
                    iqtdcone = iqtdcone - 1;
                    sqtdcone = Integer.toString(iqtdcone);
                    edtQtdcone.setText(sqtdcone);
                }
                Confere_campos_DG(view);
            }
        });



        ////Funcao que avisa quando o Seguro foi marcado ou desmarcado
        ckbSeguroc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckbSeguroc.isChecked()) {

                    Toast.makeText(ViaActivity.this, "O Valor do Seguro foi incluso.", Toast.LENGTH_LONG).show();
                    Confere_campos_DG(view);
                }
                else if (!ckbSeguroc.isChecked())
                {
                    Toast.makeText(ViaActivity.this, "O Valor do Seguro foi retirado.", Toast.LENGTH_LONG).show();
                    Confere_campos_DG(view);
                }
            }
        });

        Button btnlinhac = view.findViewById(R.id.btnLinhac);
        btnlinhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Dados = 100;
                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                String txt = "LINHASC";
                Bundle bundle = new Bundle();

                bundle.putString("txt", txt);
                bundle.putString("USUARIO", Nome_user);
                bundle.putString("LINHA", Linha_Trab); //???????????
                bundle.putString("Activity_Dados", "100");
                myIntent.putExtras(bundle);
                startForresult.launch(myIntent);

            }
        });

        Button btnviac = view.findViewById(R.id.btnViac);
        btnviac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slinhac = edtlinhac.getText().toString();
                if (!slinhac.equals("Selecione a Linha")) {
                    int posL = slinhac.indexOf("-");
                    String sCodLinha = slinhac.substring(0, (posL));

                    Activity_Dados = 101;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                    String txt = "VIAGEM";
                    Bundle bundle = new Bundle();

                    bundle.putString("txt", txt);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("LINHA", sCodLinha);
                    bundle.putString("Activity_Dados", "101");
                    myIntent.putExtras(bundle);
                    startForresult.launch(myIntent);
                }

            }
        });

        Button btnorigemc = view.findViewById(R.id.btnOric);
        btnorigemc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtVia = view.findViewById(R.id.edtViac);
                String sVia = edtVia.getText().toString();
                if (!sVia.equals("Selecione a Viagem")) { //somente procurar trecho se existit viagem selecionada
                    int posV = sVia.indexOf("-");
                    String sCodVia = sVia.substring(0, (posV));

                    Activity_Dados = 102;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                    String txt = "TRECHO";
                    Bundle bundle = new Bundle();

                    bundle.putString("txt", txt);
                    bundle.putString("viagem", sCodVia);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("LINHA", Linha_Trab);
                    bundle.putString("Activity_Dados", "102");
                    myIntent.putExtras(bundle);
                    startForresult.launch(myIntent);
                }

            }
        });

        Button btndestinoc = view.findViewById(R.id.btnDesc);
        btndestinoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtVia = view.findViewById(R.id.edtViac);
                String sVia = edtVia.getText().toString();
                if (!sVia.equals("Selecione a Viagem")) { //somente procurar trecho se existit viagem selecionada
                    int posV = sVia.indexOf("-");
                    String sCodVia = sVia.substring(0, (posV));

                    Activity_Dados = 103;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, TreActivity.class);

                    String txt = "TRECHO";
                    Bundle bundle = new Bundle();

                    bundle.putString("txt", txt);
                    bundle.putString("viagem", sCodVia);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("LINHA", Linha_Trab);
                    bundle.putString("Activity_Dados", "103");
                    myIntent.putExtras(bundle);
                    startForresult.launch(myIntent);
                }

            }
        });


        builder.setView(view)

                // Add action buttons
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String svia = edtviac.getText().toString();
                        String sori = edtoric.getText().toString();
                        String sdes = edtdesc.getText().toString();
                        String sqtdcone = edtQtdcone.getText().toString();
                        EditText edtvia = findViewById(R.id.edtVia);
                        EditText edtori = findViewById(R.id.edtOrigem);
                        EditText edtdes = findViewById(R.id.edtDestino);
                        CheckBox ckbseguro = findViewById(R.id.ckbSeguro);
                        EditText edtnome = findViewById(R.id.edtNomePas);
                        EditText edtdoc = findViewById(R.id.edtDocPas);
                        EditText edtcpf = findViewById(R.id.edtCPFPas);
                        EditText edtcad = findViewById(R.id.edtPoltrona);
                        TextView edtqtd = findViewById(R.id.edtQtd);

                        edtvia.setText(svia);
                        edtori.setText(sori);
                        edtdes.setText(sdes);
                        edtnome.setText(GuardaNome);
                        edtdoc.setText(GuardaDoc);
                        edtcpf.setText(GuardaCPF);
                        edtcad.setText(GuardaCad);
                        edtqtd.setText(sqtdcone);



                        if (ckbSeguroc.isChecked()) {
                            ckbseguro.setChecked(true);
                        }
                        else if (!ckbSeguroc.isChecked())
                        {
                            ckbseguro.setChecked(false);
                        }

                        Confere_campos();
                        EnviaConexao = "S";
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#20910f"));


        return  sretmot;

    }



    public String Verifica_Pendentes() {
        String sretpen = "";
        int ipen = 0;
        DB_BPE db = new DB_BPE(ViaActivity.this);
        DB_BPE.BpeCursor cursor = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

        for (int iv = 0; iv < cursor.getCount(); iv++) {
            cursor.moveToPosition(iv);

            String stransf = cursor.getTransf();
            if (stransf.equals("")) { //ainda nao transferiu para o servidor
                String ssit = cursor.getSitbpe();
                if (ssit.equals("CT") || ssit.equals("DG")) { //contingencia
                    ipen = ipen+1;

                }
            }

        }
        if (ipen > 0) { //encontrou documentos pendentes de autorizacao
            sretpen = ipen+" Bilhete(s) pendente(s).";
            iTotpendentes = ipen;
        }
        return  sretpen;
    }

    private Intent criarIntentBilhete(DB_BPE.BpeCursor cursore) {
        Intent intent = new Intent(this, WSActivity.class);
        Bundle bundle = new Bundle();
        String chaveBPe = cursore.getChvbpe();
        bundle.putString("XML", chaveBPe + "-bpe.xml");
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("LINVIA", cursore.getCodlin());
        bundle.putString("DATVIA", cursore.getDatsai());
        bundle.putString("NUMCAD", cursore.getNumcad());
        bundle.putString("TREORI", cursore.getCodori());
        bundle.putString("TREDES", cursore.getCoddes());
        bundle.putString("VLRTAR", cursore.getVlrtar());
        bundle.putString("VLREMB", cursore.getVlremb());
        bundle.putString("VLRSEG", cursore.getVlrseg());
        bundle.putString("VLRARRE", cursore.getVlrarr());
        bundle.putString("SERIE", cursore.getModser().substring(2, 5));
        bundle.putString("DATEMI", cursore.getDatemi());
        bundle.putString("VEICULO", cursore.getCodvei());
        bundle.putString("PAGAMENTO", cursore.getPagmto());
        bundle.putString("TIPVIA", obterTipVia(cursore));
        bundle.putString("COMANDO", "CONSULTA");
        bundle.putString("MOTIVO", cursore.getRsv003());
        bundle.putString("CANCEL", cursore.getRsv001());
        bundle.putString("Activity_Dados", "14");
        intent.putExtras(bundle);
        return intent;
    }

    private String obterTipVia(DB_BPE.BpeCursor cursore) {
        DB_PER dbper = new DB_PER(this);
        DB_PER.PercursosCursor percursor = dbper.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);
        String stipvia = "I";
        for (int iper = 0; iper < percursor.getCount(); iper++) {
            percursor.moveToPosition(iper);
            if (percursor.getLinha().equals(cursore.getCodlin()) &&
                    percursor.getOrigem().equals(cursore.getCodori()) &&
                    percursor.getDestino().equals(cursore.getCoddes())) {
                stipvia = percursor.getTipvia();
                break;
            }
        }
        return stipvia;
    }


    //Chama Dialog para informar Motivo de cancelamento ou nao embarque
    public String infAviso(String sMsg) {
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
                        Retpendentes = "S";
                        //String senvia = Envia_Pendentes_DB();
                        msgBuilder.delete(0, msgBuilder.length());
                        Envia_Pendentes_DB();
                        Retpendentes = RetWSActivity;

                    }
                })
                .setNegativeButton("Mais Tarde", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                        Retpendentes = "";
                    }
                });

        android.app.AlertDialog alert = builder.create();
        alert.show();
        //mudar a cor do botao de confirmar
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.parseColor("#20910f"));

        //mudar a cor do botao de recusar
        Button brncancel = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        brncancel.setTextColor(Color.parseColor("#EE6363"));

        sretaviso = Retpendentes;

        return  sretaviso;

    }





    private void Menu_Lateral() {
        String[] osArray = { "Bilhetes Emitidos", "Relatório de Vendas", "Configurações", "Encerrar Viagens", "Verificar Pendentes"};
        mAdapter = new ArrayAdapter<String>(ViaActivity.this, android.R.layout.simple_list_item_1, osArray);
        listMenu.setAdapter(mAdapter);
    }

    ActivityResultLauncher<Intent> resultadoActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Intent data = result.getData();
                    String sret = data.getStringExtra("msg");
                    String sorigemresult = data.getStringExtra("Activity_Dados");
                    if (sorigemresult.equals("17")){
                        if (result.getResultCode() == RESULT_OK) {
                            System.out.println("Retornei WSActivity Verificar Conexao: "+sret);
                            if (sret.equals("Conectado")) {
                                EstouConectado = true;
                            } else {
                                EstouConectado = false;
                            }

                        } else {
                            EstouConectado = false;
                        }
                        System.out.println("Retorno do WS: "+EstouConectado);
                        String scomando = WScomando;
                        DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                        if (scomando.equals("CONSULTA")) {
                            String sdataCT = "";
                            String sCTG = dbemp.Busca_Dados_Emp(1, "Tipemi");
                            WScomando = "TRANSMITE";
                            Activity_Dados = 12;
                            if (EstouConectado) {
                                dbemp.Atualizar_Campo_Emp("1", "Tipemi", "1");
                                dbemp.Atualizar_Campo_Emp("1", "Datctg", "");
                                Gerar_XML("N");
                                System.out.println("Gerei XML Activity_Dados: " + Activity_Dados);
                            } else {
                                sdataCT = Funcoes_Android.getCurrentUTC();
                                dbemp.Atualizar_Campo_Emp("1", "Tipemi", "2");
                                dbemp.Atualizar_Campo_Emp("1", "Datctg", sdataCT);
                                Gerar_XML("N");
                                System.out.println("Gerei XML Activity_Dados: " + Activity_Dados);
                            }
                        }
                        if (scomando.equals("BUSCAGRA")){
                            if (EstouConectado) {
                                dbemp.Atualizar_Campo_Emp("1", "Tipemi", "1");
                                dbemp.Atualizar_Campo_Emp("1", "Datctg", "");
                                Gerar_XML("N");
                                System.out.println("Gerei XML Activity_Dados: " + Activity_Dados);
                            }
                        }
                    }
                }
            }


        }
    });

    ActivityResultLauncher<Intent> startForresult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        System.out.println("startForresult chamado com resultado: " + (result != null ? result.getResultCode() : "null"));
        if (result != null && result.getData() != null) {
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

                    System.out.println("Entrei na startForresult " + "Retorno: "+sorigemresult);

                    DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                    String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
                    if (sorigemresult.equals("1")) {


                        if (result.getResultCode() == RESULT_OK) {

                            // mostra hint
                            //Toast.makeText(this, "Viagem: " + msg, Toast.LENGTH_LONG).show();
                            EditText edtViagem = findViewById(R.id.edtVia);
                            edtViagem.setText(sret);
                            EditText edtOrigem = findViewById(R.id.edtOrigem);
                            EditText edtDestino = findViewById(R.id.edtDestino);
                            EditText edtcad = findViewById(R.id.edtPoltrona);
                            TextView txtTarifa = findViewById(R.id.txtValor);
                            CheckBox ckbSeguro = findViewById(R.id.ckbSeguro);
                            ckbSeguro.setChecked(true);
                            edtOrigem.setText("Selecione a Origem");
                            edtDestino.setText("Selecione o Destino");
                            txtTarifa.setText("0.00");
                            edtcad.setText("");
                        } else {
                            Confere_campos();
                        }
                    }
                    // se o resultado retornou a Origem
                    if (sorigemresult.equals("2")) {
                        if (result.getResultCode() == RESULT_OK) {
                            EditText edtOrigem = findViewById(R.id.edtOrigem);
                            edtOrigem.setText(sret);
                            Confere_campos();
                        } else {
                            Confere_campos();
                        }
                    }
                    // se o resultado retornou o Destino
                    if (sorigemresult.equals("3")) {

                        if (result.getResultCode() == RESULT_OK) {

                            EditText edtDestino = findViewById(R.id.edtDestino);
                            edtDestino.setText(sret);
                            Confere_campos();

                        } else {
                            Confere_campos();
                        }

                    }
                    if (sorigemresult.equals("5")) {

                        if (result.getResultCode() == RESULT_OK) {

                            EditText edtDestino = findViewById(R.id.edtDestino);
                            edtDestino.setText(sret);
                            Confere_campos();

                        } else {
                            Confere_campos();
                        }

                    }

                    // se o resultado retornou do WS
                    if (sorigemresult.equals("12") || sorigemresult.equals("14")) {
                        String sfimCONEXAO;
                        sfimCONEXAO = "";
                        String resultado = "";

                        if (result.getResultCode() == RESULT_OK) {
                            if (sorigemresult.equals("12")) {
                                String sdatchamada = Funcoes_Android.getCurrentUTC();
                                System.out.println(sdatchamada + "Retornei Transmissao Normal:");
                                String sdathoratu = HoraInicio;
                                String sdathoratu2 = Funcoes_Android.getCurrentUTC();
                                String shorfin = sdathoratu2.substring(11, 19); //hora atual
                                String shorini = sdathoratu.substring(11, 19); //hora anterior

                                //Verifica se a transmissao demorou mais de 1 minuto
                                long intervalo = Funcoes_Android.validaDate_Seg(shorini, shorfin);
                                System.out.println(sdatchamada + "Demorei x segundos:" + shorini + "   " + shorfin + " = " + intervalo);
                                if (intervalo > 10) {
                                    //Habilia a contingência
                                    dbemp.Atualizar_Campo_Emp("1", "Tipemi", "2");
                                    dbemp.Atualizar_Campo_Emp("1", "Datctg", sdathoratu2);
                                    // String inf = infMensagens("Contingência Ativada.");
                                }
                                if (smodimp.equals("01")) {
                                    Imprime_BPe(); //SUNMI
                                    int iqtd = GuardaQtd;
                                    if (iqtd > 1) {
                                        iqtd = (iqtd - 1);
                                        GuardaQtd = iqtd;
                                        Chama_Fechamento();
                                    } else {
                                        sfimCONEXAO = "S";
                                    }
                                } else if (smodimp.equals("02")) {
                                    Imprime_BPe_Lio(); //CIELO LIO
                                    int iqtd = GuardaQtd;
                                    if (iqtd == 1) {
                                        GuardaPagto = "";
                                        GuardaMobi = "";
                                        GuardaBand = "";
                                        GuardaAut = "";
                                        GuardaID = "";
                                        Guarda_QRcode = "";
                                        sfimCONEXAO = "S";
                                    } else {
                                        if (iqtd > 1) {
                                            iqtd = (iqtd - 1);
                                            GuardaQtd = iqtd;
                                            Chama_Fechamento();
                                        }
                                    }
                                } else if (smodimp.equals("03")) {
                                    Imprime_BPe_BT(); //ARNY SP5 Bluetooth
                                    int iqtd = GuardaQtd;
                                    if (iqtd > 1) {
                                        iqtd = (iqtd - 1);
                                        GuardaQtd = iqtd;
                                        Chama_Fechamento();
                                    } else {
                                        sfimCONEXAO = "S";
                                        GuardaMobi = "";
                                        Guarda_QRcode = "";
                                    }
                                } else if (smodimp.equals("05")) {
                                    Imprime_BPe_DTS(); //AR-2500 Bluetooth
                                    int iqtd = GuardaQtd;
                                    if (iqtd > 1) {
                                        iqtd = (iqtd - 1);
                                        GuardaQtd = iqtd;
                                        Chama_Fechamento();
                                    } else {
                                        sfimCONEXAO = "S";
                                        GuardaMobi = "";
                                        Guarda_QRcode = "";
                                    }
                                }
                                EditText edtCad = findViewById(R.id.edtPoltrona); //Poltrona
                                EditText edtNomPas = findViewById(R.id.edtNomePas); //nome do passageiro
                                EditText edtDocPas = findViewById(R.id.edtDocPas); //documento de Identidade do passageiro
                                EditText edtcpfpas = findViewById(R.id.edtCPFPas); //CPF do Passageiro Opcional
                                EditText edttipgra = findViewById(R.id.edtTipGra); //Tipo de Gratuidade do passageiro
                                EditText edttipdes = findViewById(R.id.edtTipDes); //Tipo de desconto (100 ou 50%)
                                EditText edtvia = findViewById(R.id.edtVia);
                                EditText edtori = findViewById(R.id.edtOrigem);
                                EditText edtdes = findViewById(R.id.edtDestino);
                                edtCad.setText("");
                                edtNomPas.setText("");
                                edtDocPas.setText("");
                                edtcpfpas.setText("");
                                edttipgra.setText("");
                                edttipdes.setText("");
                                if (EnviaConexao.equals("S")) {
                                    if (sfimCONEXAO.equals("S")) {
                                        edtvia.setText(GuardaVia);
                                        edtori.setText(GuardaOri);
                                        edtdes.setText(GuardaDes);
                                        Linha_Trab = GuardaLinha;
                                        Calcula_Valor_DB();
                                        EnviaConexao = "";
                                    }
                                }
                                Menu_Lateral();


                            } //transmissao em tempo real
                            if (sorigemresult.equals("14")) {
                                String xml = data.getStringExtra("XML"); // Pegamos o XML retornado
                                String snumero = (xml != null && xml.length() >= 34) ? xml.substring(25, 34) : "unknown";
                                if (result.getResultCode() == RESULT_OK) {
                                    RetWSActivity = sret;
                                    resultado = snumero + " - " + sret;
                                    System.out.println(Funcoes_Android.getCurrentUTC() + " Retornei Transmissao de Pendentes: " + resultado);
                                } else {
                                    RetWSActivity = "Erro: " + sret;
                                    resultado = snumero + " - Erro: " + sret;
                                    System.out.println(Funcoes_Android.getCurrentUTC() + " Retornei Transmissao Pendentes Erro: " + resultado);
                                }
                                synchronized (msgBuilder) {
                                    msgBuilder.append(resultado).append("\n");
                                    iPendentes++;
                                    System.out.println("Bilhete " + snumero + " concluído com resultado: " + resultado);
                                    System.out.println("Totpen: " + iTotpendentes + " ipen: " + iPendentes);

                                    runOnUiThread(() -> {
                                       // progressText.setText("Processando: " + iPendentes + "/" + iTotpendentes);
                                        if (iPendentes == iTotpendentes) {
                                            MsgAgrupa = msgBuilder.toString();
                                            System.out.println("Exibindo mensagem final: " + MsgAgrupa);
                                            infMensagens(MsgAgrupa, "");
                                         //   progressText.setText("Concluído!");
                                        } else {
                                            // Envia o próximo bilhete após o retorno do anterior
                                            enviarProximoBilhete();
                                        }
                                    });
                                }
                            }
                        } else {
                            System.out.println("Erro: Resultado ou data é null no startForresult");
                            // Se houver erro, tenta enviar o próximo bilhete
                            runOnUiThread(this::enviarProximoBilhete);
                        }
                        if (result.getResultCode() == RESULT_CANCELED) {
                            ///Nao conseguiu transmitir chamar novo bilhete em contingencia
                            if (sorigemresult.equals("12")) {
                                //Verificar se o BP-e realmente nao foi autorizado
                                String chaveBPe = Guarda_Texto;
                                String snumbpe = chaveBPe.substring(25, 34);
                                int inumbpe = Integer.parseInt(snumbpe);
                                DB_BPE dbbpe = new DB_BPE(getApplicationContext());
                                String ssit = dbbpe.Busca_Dados_Bpe(Integer.toString(inumbpe), "Sitbpe");
                                EditText edtCad = findViewById(R.id.edtPoltrona); //Poltrona
                                EditText edtNomPas = findViewById(R.id.edtNomePas); //nome do passageiro
                                EditText edtDocPas = findViewById(R.id.edtDocPas); //documento de Identidade do passageiro
                                EditText edtcpfpas = findViewById(R.id.edtCPFPas); //CPF do Passageiro Opcional
                                EditText edttipgra = findViewById(R.id.edtTipGra); //Tipo de Gratuidade do passageiro
                                EditText edttipdes = findViewById(R.id.edtTipDes); //Tipo de desconto (100 ou 50%)
                                EditText edtvia = findViewById(R.id.edtVia);
                                EditText edtori = findViewById(R.id.edtOrigem);
                                EditText edtdes = findViewById(R.id.edtDestino);
                                if (!ssit.equals("BA")) {
                                    String sdatchamada = Funcoes_Android.getCurrentUTC();
                                    System.out.println(sdatchamada + "Retornei TRansmissao Normal Erro:");
                                    //infMensagens(msg);
                                    Gerar_XML("E");
                                    int iqtd = GuardaQtd;
                                    if (iqtd == 1) {
                                        GuardaPagto = "";
                                        GuardaBand = "";
                                        GuardaAut = "";
                                        GuardaID = "";
                                        GuardaMobi = "";
                                        sfimCONEXAO = "S";
                                        Guarda_QRcode = "";
                                    } else {
                                        if (iqtd > 1) {
                                            iqtd = (iqtd - 1);
                                            GuardaQtd = iqtd;
                                            Chama_Fechamento();
                                        }
                                    }
                                    Menu_Lateral();
                                } else {
                                    if (smodimp.equals("01")) {
                                        Imprime_BPe(); //SUNMI
                                        int iqtd = GuardaQtd;
                                        if (iqtd > 1) {
                                            iqtd = (iqtd - 1);
                                            GuardaQtd = iqtd;
                                            Chama_Fechamento();
                                        }
                                    } else if (smodimp.equals("03")) {
                                        Imprime_BPe_BT(); //ARNY SP5 Bluetooth
                                        int iqtd = GuardaQtd;
                                        if (iqtd > 1) {
                                            iqtd = (iqtd - 1);
                                            GuardaQtd = iqtd;
                                            Chama_Fechamento();
                                        }
                                    } else if (smodimp.equals("05")) {
                                        Imprime_BPe_DTS(); //ARNY SP5 Bluetooth
                                        int iqtd = GuardaQtd;
                                        if (iqtd > 1) {
                                            iqtd = (iqtd - 1);
                                            GuardaQtd = iqtd;
                                            Chama_Fechamento();
                                        }
                                    } else {
                                        Imprime_BPe_Lio(); //CIELO LIO
                                        int iqtd = GuardaQtd;
                                        if (iqtd == 1) {
                                            GuardaPagto = "";
                                            GuardaBand = "";
                                            GuardaAut = "";
                                            GuardaID = "";
                                            sfimCONEXAO = "S";
                                        } else {
                                            if (iqtd > 1) {
                                                iqtd = (iqtd - 1);
                                                GuardaQtd = iqtd;
                                                Chama_Fechamento();
                                            }
                                        }
                                    }
                                    edtCad.setText("");
                                    edtNomPas.setText("");
                                    edtDocPas.setText("");
                                    edtcpfpas.setText("");
                                    edttipgra.setText("");
                                    edttipdes.setText("");
                                    if (EnviaConexao.equals("S")) {
                                        if (sfimCONEXAO.equals("S")) {
                                            edtvia.setText(GuardaVia);
                                            edtori.setText(GuardaOri);
                                            edtdes.setText(GuardaDes);
                                            Linha_Trab = GuardaLinha;
                                            Calcula_Valor_DB();
                                            EnviaConexao = "";
                                            Menu_Lateral();
                                        }
                                    }
                                }


                            }
                            if (sorigemresult.equals("14")) {
                                synchronized (msgBuilder) {
                                    msgBuilder.append(resultado).append("\n");
                                    iPendentes++;
                                    System.out.println("concluído com resultado: " + resultado);
                                    System.out.println("Totpen: " + iTotpendentes + " ipen: " + iPendentes);

                                    runOnUiThread(() -> {
                                        // progressText.setText("Processando: " + iPendentes + "/" + iTotpendentes);
                                        if (iPendentes == iTotpendentes) {
                                            MsgAgrupa = msgBuilder.toString();
                                            System.out.println("Exibindo mensagem final: " + MsgAgrupa);
                                            infMensagens(MsgAgrupa, "");
                                            //   progressText.setText("Concluído!");
                                        } else {
                                            // Envia o próximo bilhete após o retorno do anterior
                                            enviarProximoBilhete();
                                        }
                                    });
                                }
                            }

                        }

                    }


                    // se o resultado retornou do WS cancelamento
                    if (sorigemresult.equals("13")) {
                        if (result.getResultCode() == RESULT_OK) {
                            String snumero = data.getStringExtra("numbpe");
                            if (!snumero.equals("")) {
                                if (smodimp.equals("01")) {
                                    Imprime_Cancel(snumero); //SUNMI
                                } else if (smodimp.equals("02")) {
                                    Imprime_Cancel_LIO(snumero); //CIELO LIO
                                } else if (smodimp.equals("03")) {
                                    Imprime_Cancel_BT(snumero); //ARNY
                                }
                            }
                            infMensagens("Bilhete Cancelado com Sucesso!", "");
                        }

                        if (result.getResultCode() == RESULT_CANCELED) {
                            //Toast.makeText(this, "Erro: " + msg, Toast.LENGTH_LONG).show();
                            ///Nao conseguiu cancelar
                            infMensagens(sret, "");

                        }


                    }

                    // se o resultado retornou da tela de parametros
                    if (sorigemresult.equals("15")) {

                        if (result.getResultCode() == RESULT_OK) {

                            Menu_Lateral();

                        }

                    }
                    // se o resultado retornou do verifica conexao
                    if (sorigemresult.equals("17")) {

                        if (result.getResultCode() == RESULT_OK) {
                            System.out.println("Retornei WSActivity Verificar Conexao: "+sret);
                            if (sret.equals("Conectado")) {
                                EstouConectado = true;
                            } else {
                                EstouConectado = false;
                            }

                        } else {
                            EstouConectado = false;
                        }

                    }
                    // se o resultado retornou do verifica gratuidade
                    if (sorigemresult.equals("21")) {

                        if (result.getResultCode() == RESULT_OK) {
                            Intent Newintent = getIntent();
                            Bundle bundle = Newintent.getExtras();
                            String susrgra = bundle.getString("NomeUSR");
                            String sdocgra = bundle.getString("DocUSR");
                            String stipogra = bundle.getString("TipoUSR");
                            System.out.println("Retornei Verificar Gratuidade: "+sret);


                        }

                    }
                    if (sorigemresult.equals("22")) {

                        if (result.getResultCode() == RESULT_OK) {
                            Intent Newintent = getIntent();
                            Bundle bundle = Newintent.getExtras();
                            String susrgra = bundle.getString("NomeUSR");
                            String sdocgra = bundle.getString("DocUSR");
                            String stipogra = bundle.getString("TipoUSR");
                            System.out.println("Retornei Verificar Gratuidade: "+sret);
                            int iqtdret = Integer.parseInt(sret);
                            if (iqtdret>=2) {
                                infMensagens("Usuario excedeu limite para esta linha no mês.", "");
                                EditText edtpas = findViewById(R.id.edtNomePas);
                                EditText edtdoc = findViewById(R.id.edtDocPas);
                                EditText edtcpfpas = findViewById(R.id.edtCPFPas);
                                EditText edttipgra = findViewById(R.id.edtTipGra);
                                EditText edttipdes = findViewById(R.id.edtTipDes);
                                edtpas.setText("");
                                edtdoc.setText("");
                                edtcpfpas.setText("");
                                edttipgra.setText("");
                                edttipdes.setText("");
                                TipoDoc = "";
                            }
                            Calcula_Valor_DB();

                        }

                    }

                    //retornou Linha de conexao
                    if (sorigemresult.equals("100")) {
                        EditText edtlinhac = viewConexao.findViewById(R.id.edtLinhac);
                        EditText edtViagem = viewConexao.findViewById(R.id.edtViac);
                        EditText edtOrigem = viewConexao.findViewById(R.id.edtOrigemc);
                        EditText edtDestino = viewConexao.findViewById(R.id.edtDestinoc);
                        TextView txtTarifa = viewConexao.findViewById(R.id.txtValorc);
                        CheckBox ckbSeguro = viewConexao.findViewById(R.id.ckbSeguroc);
                        TextView txtvalortot = viewConexao.findViewById(R.id.txtValortot);

                        if (result.getResultCode() == RESULT_OK) {
                            edtlinhac.setText(sret);
                            ckbSeguro.setChecked(true);
                            edtViagem.setText("Selecione a Viagem");
                            edtOrigem.setText("Selecione a Origem");
                            edtDestino.setText("Selecione o Destino");
                            txtTarifa.setText("0.00");
                            String stotal = String.format("%.2f", GuardaValor);
                            txtvalortot.setText(stotal);
                        } else {
                            edtlinhac.setText("Selecione a Linha");
                            ckbSeguro.setChecked(true);
                            edtViagem.setText("Selecione a Viagem");
                            edtOrigem.setText("Selecione a Origem");
                            edtDestino.setText("Selecione o Destino");
                            txtTarifa.setText("0.00");
                            String stotal = String.format("%.2f", GuardaValor);
                            txtvalortot.setText(stotal);
                        }

                    } //

                    //retornou viagem de conexao
                    if (sorigemresult.equals("101")) {

                        if (result.getResultCode() == RESULT_OK) {

                            EditText edtViagem = viewConexao.findViewById(R.id.edtViac);
                            edtViagem.setText(sret);
                            EditText edtOrigem = viewConexao.findViewById(R.id.edtOrigemc);
                            EditText edtDestino = viewConexao.findViewById(R.id.edtDestinoc);
                            TextView txtTarifa = viewConexao.findViewById(R.id.txtValorc);
                            CheckBox ckbSeguro = viewConexao.findViewById(R.id.ckbSeguroc);
                            ckbSeguro.setChecked(true);
                            edtOrigem.setText("Selecione a Origem");
                            edtDestino.setText("Selecione o Destino");
                            txtTarifa.setText("0.00");
                        }

                    } //101

                    // se o resultado retornou a Origem da conexao
                    if (sorigemresult.equals("102")) {

                        if (result.getResultCode() == RESULT_OK) {

                            // mostra hint
                            //Toast.makeText(this, "Origem: " + msg, Toast.LENGTH_LONG).show();
                            EditText edtOrigem = viewConexao.findViewById(R.id.edtOrigemc);
                            edtOrigem.setText(sret);
                            Confere_campos_DG(viewConexao);
                        } else {
                            Confere_campos_DG(viewConexao);
                        }


                    }
                    // se o resultado retornou o Destino da conexao
                    if (sorigemresult.equals("103")) {
                        if (result.getResultCode() == RESULT_OK) {

                            // mostra hint
                            //Toast.makeText(this, "Destino: " + msg, Toast.LENGTH_LONG).show();
                            EditText edtDestino = viewConexao.findViewById(R.id.edtDestinoc);
                            edtDestino.setText(sret);
                            Confere_campos_DG(viewConexao);

                        } else {
                            Confere_campos_DG(viewConexao);
                        }

                    }
                    //Se retornou leitura da camera
                    if (sorigemresult.equals("104")) {
                        if (result.getResultCode() == RESULT_OK) {
                            String contents = data.getStringExtra("SCAN_RESULT");
                            String value = contents;
                            Guarda_QRcode = value;
                            dbemp.Atualizar_Campo_Emp("1", "Ultqrc", value);
                            //Toast.makeText(getApplicationContext(), contents, Toast.LENGTH_LONG).show();
                            Log.i("CONTENT SCAN ", contents);
                            Log.i("CONTENT VALUE", value);
                            if (!value.equals("")) {

                                //DB_EMP dbempvenda = new DB_EMP(ViaActivity.this);
                                //String sqrc = dbempvenda.Busca_Dados_Emp(1, "Ultqrc");
                                Log.i(TAG, "Retorno Leitura: " + value);
                                if (!value.equals("")) {

                                    EditText edtVia = findViewById(R.id.edtVia);
                                    String sser = dbemp.Busca_Dados_Emp(1, "Serie");
                                    String svia = edtVia.getText().toString();
                                    TextView txtTarifa = findViewById(R.id.txtValor);
                                    String svalor = txtTarifa.getText().toString();
                                    svalor = svalor.replace(",", "");
                                    com.x4fare.mobipix.onboard.sdk.MobiPixOnboard.getInstance().chargePassenger(ViaActivity.this, svalor, value, sser, svia, new OnboardCallback<MobiPixResponseDTO<ChargeRiderResponseDTO>>() {
                                        @Override
                                        public void onSuccess(MobiPixResponseDTO<ChargeRiderResponseDTO> response) {
                                            // processar a logica de sucesso
                                            int istatus = response.getStatus();
                                            Log.i(TAG, "callbackpay PAS: " + istatus);
                                            if (istatus == 200) {
                                                ChargeRiderResponseDTO responsechar = ChargeRiderResponseDTO.class.cast(response.getResponse());

                                                LocalTransactionStatus retstatus = responsechar.getLocalTransactionStatus();
                                                String status = retstatus.toString();
                                                //infMensagens(status, "");
                                                if (status.equals("AUTHORIZED")) {
                                                    //Salvar a forma de pagamento
                                                    GuardaMobi = "VALE TRANSPORTE";
                                                    GuardaID = responsechar.getLocalAuthCode();
                                                    Inicia_Venda();
                                                } else {
                                                    String retresponse = responsechar.getMessage();
                                                    infMensagens(retresponse, "");
                                                }


                                            }

                                        }

                                        @Override
                                        public void onError(MobiPixResponseDTO<ChargeRiderResponseDTO> response) {
                                            // processar a logica de erro
                                            String serro = "ERRO";
                                            int istatus = response.getStatus();
                                            if (istatus == 400) {
                                                serro = "Requisição inválida";
                                            } else if (istatus == 401) {
                                                serro = "Usuário não autorizado";
                                            } else if (istatus == 500) {
                                                serro = "Erro no servidor do App";
                                            } else {
                                                serro = response.getMessageKey();
                                            }
                                            infMensagens(response.getStatus() + " = " + serro, "");

                                        }
                                    });


                                }
                            }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // Handle cancel
                    }

                    }


            }

    });


    public void Imprime_Cancel(final String snumero) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {

            @Override
            public void run() {
                if (mBitmap == null) {
                    mBitmap = BitmapFactory.decodeFile(getExternalFilesDir("Download").getAbsolutePath() + "/logoemp.jpg");
                    //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
                }
                try {
                    try {


                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());
                        DB_BPE dbbpe = new DB_BPE(getApplicationContext());
                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");
                        float size = 30;
                        woyouService.setAlignment(1, callback);
                        woyouService.printBitmap(mBitmap, callback);
                        woyouService.lineWrap(1, null);

                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                        woyouService.setAlignment(1, callback); //Alinhamento
                        //0 - Alinhar a Esquerda
                        //1 - Centralizado
                        //2 - Alinhado a Direita
                        woyouService.printTextWithFont((dbemp.Busca_Dados_Emp(1, "Descri")), "", 24, null);
                        woyouService.lineWrap(1, null);

                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x0}, callback); // Desativar Fonte em Negrito

                        String scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
                        String sIe = dbemp.Busca_Dados_Emp(1, "Insest");


                        woyouService.printTextWithFont("CNPJ: " + scnpj + " IE: " + sIe, "", 22, null);
                        woyouService.lineWrap(1, null);

                        String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                        sendere = dbemp.Busca_Dados_Emp(1, "Endere");
                        snum = dbemp.Busca_Dados_Emp(1, "Numero");
                        sbai = dbemp.Busca_Dados_Emp(1, "Bairro");
                        scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                        suf = dbemp.Busca_Dados_Emp(1, "UF");
                        sendp1 = sendere + ", " + snum;
                        sendp2 = sbai + ", " + scid + "-" + suf;

                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                        woyouService.printTextWithFont(sendp1, "", 18, null);
                        woyouService.lineWrap(1, null);
                        woyouService.printTextWithFont(sendp2, "", 18, null);
                        woyouService.lineWrap(2, null);

                        woyouService.printTextWithFont("CANCELAMENTO", "", 28, null);
                        woyouService.lineWrap(1, null);
                        String ssit = dbbpe.Busca_Dados_Bpe(snumero, "Sitbpe");
                        if (ssit.equals("CT")) { //emitido em contingencia
                            woyouService.lineWrap(1, null);
                            woyouService.printTextWithFont("EMITIDO EM CONTINGÊNCIA", "", 28, null);
                            woyouService.lineWrap(1, null);
                            woyouService.printTextWithFont("Pendente de Autorização", "", 22, null);
                            woyouService.lineWrap(1, null);
                        }

                        String slinha = dbbpe.Busca_Dados_Bpe(snumero, "Nomvia");
                        String sori = dbbpe.Busca_Dados_Bpe(snumero, "Treori");
                        String sdes = dbbpe.Busca_Dados_Bpe(snumero, "Tredes");
                        String svalor = dbbpe.Busca_Dados_Bpe(snumero, "Vlrpas");

                        woyouService.lineWrap(1, null);
                        woyouService.printTextWithFont("Viagem: "+slinha, "", 22, null);
                        woyouService.lineWrap(1, null);
                        woyouService.printTextWithFont("Origem: "+sori, "", 22, null);
                        woyouService.lineWrap(1, null);
                        woyouService.printTextWithFont("Destino: "+sdes, "", 22, null);
                        woyouService.lineWrap(2, null);

                        String sNROBPE = (("000000000" + snumero).substring(snumero.length()));
                        String Modser =  dbbpe.Busca_Dados_Bpe(snumero, "Modser");
                        String sSERBPE = Modser.substring(2, (5));

                        woyouService.printTextWithFont("BP-e nº ", "", 24,null);
                        woyouService.printTextWithFont((sNROBPE)+"    ", "", 24,null);
                        woyouService.printTextWithFont("Série ", "", 24,null);
                        woyouService.printTextWithFont((sSERBPE)+"", "", 24,null);
                        woyouService.lineWrap(2, null);


                        String sespaco = "";
                        String sval = "Valor Total R$"+svalor;
                        Integer iqtd = sval.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32-iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda
                        woyouService.printTextWithFont("Valor Total R$" + sespaco, "", 24, null);
                        woyouService.printTextWithFont(svalor, "", 24, null);
                        woyouService.lineWrap(2, null);

                        String shoreve = Funcoes_Android.getCurrentUTC();
                        String sanoe = shoreve.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        String smese = shoreve.substring(5, (7));
                        String sdiae = shoreve.substring(8, (10));
                        String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                        String sHorae = shoreve.substring(11, (19));

                        woyouService.printTextWithFont("DH Evento: "+sdEmi+"  "+sHorae, "", 24, null);
                        woyouService.lineWrap(2, null);

                        woyouService.printTextWithFont("MOTIVO", "", 24, null);
                        woyouService.lineWrap(1, null);

                        String smotivo = dbbpe.Busca_Dados_Bpe(snumero, "Rsv003");
                        woyouService.printTextWithFont(smotivo, "", 24, null);
                        if (sespacos.equals("xx")) {
                            woyouService.lineWrap(4, null);
                        } else {
                            int iesp = Integer.parseInt(sespacos);
                            woyouService.lineWrap(iesp, null);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    public void Imprime_Cancel_BT(final String snumero) {
                try {
                    if (printerBluetooth == null)
                        acharPrinterBluetooth();
                    if (printerBluetooth == null)
                        return;
                    DB_BPE dbbpe = new DB_BPE(getApplicationContext());
                    String smotivo2 = "";
                    BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

                    impressora.connect();
                    try {
                        iniciarImpressora(impressora.getOutputStream());
                        OutputStream out = impressora.getOutputStream();

                        out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                        out.write(EscPosBase.alignCenter()); //Centralizado
                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");

                        out.write(dbemp.Busca_Dados_Emp(1,"Descri").getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());
                        out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito

                        String scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
                        String sIe = dbemp.Busca_Dados_Emp(1, "Insest");

                        String sinscricao = "CNPJ: " + scnpj + " IE: " + sIe;
                        out.write(sinscricao.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());

                        String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                        sendere = dbemp.Busca_Dados_Emp(1, "Endere");
                        snum = dbemp.Busca_Dados_Emp(1, "Numero");
                        sbai = dbemp.Busca_Dados_Emp(1, "Bairro");
                        scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                        suf = dbemp.Busca_Dados_Emp(1, "UF");
                        sendp1 = sendere + ", " + snum;
                        sendp2 = sbai + ", " + scid + "-" + suf;

                        out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito
                        out.write(sendp1.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());
                        out.write(sendp2.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));
                        out.write(EscPosBase.getFontTall());//Aumentar o tamanho da fonte
                        out.write("CANCELAMENTO".getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());
                        out.write(EscPosBase.getFontNormal());//Fonte tamanho Normal

                        String ssit = dbbpe.Busca_Dados_Bpe(snumero, "Sitbpe");
                        if (ssit.equals("CT")) { //emitido em contingencia
                            out.write(EscPosBase.nextLine());
                            out.write("EMITIDO EM CONTINGENCIA".getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(1));
                            out.write("Pendente de Autorizacao".getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(1));

                        }

                        String slinha = dbbpe.Busca_Dados_Bpe(snumero, "Nomvia");
                        String sori = dbbpe.Busca_Dados_Bpe(snumero, "Treori");
                        String sdes = dbbpe.Busca_Dados_Bpe(snumero, "Tredes");
                        String svalor = dbbpe.Busca_Dados_Bpe(snumero, "Vlrpas");

                        String svia = "Viagem: "+slinha;
                        out.write(svia.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(1));
                        String sorigem = "Origem: "+sori;
                        out.write(sorigem.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(1));
                        String sdestino = "Destino: "+sdes;
                        out.write(sdestino.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(1));


                        String sNROBPE = (("000000000" + snumero).substring(snumero.length()));
                        String Modser =  dbbpe.Busca_Dados_Bpe(snumero, "Modser");
                        String sSERBPE = Modser.substring(2, (5));
                        String snumser = ("BP-e nro "+sNROBPE+"    Serie "+sSERBPE);
                        out.write(snumser.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                        String sespaco = "";
                        String sval = "Valor Total R$"+svalor;
                        Integer iqtd = sval.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32-iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        out.write(EscPosBase.alignLeft()); //Esquerda
                        //out.write(EscPosBase.alignCenter()); //Centralizado
                        String svalbpe = "Valor Total R$" + sespaco;
                        out.write(svalbpe.getBytes(StandardCharsets.UTF_8));
                        out.write(svalor.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                        String shoreve = Funcoes_Android.getCurrentUTC();
                        String sanoe = shoreve.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        String smese = shoreve.substring(5, (7));
                        String sdiae = shoreve.substring(8, (10));
                        String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                        String sHorae = shoreve.substring(11, (19));

                        String sdataeve = "DH Evento: "+sdEmi+"  "+sHorae;
                        out.write(sdataeve.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));

                        out.write("MOTIVO".getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(1));


                        String smotivo = dbbpe.Busca_Dados_Bpe(snumero, "Rsv003");
                        out.write(smotivo.getBytes(StandardCharsets.UTF_8));
                        smotivo2 = smotivo;

                        if (sespacos.equals("xx")) {
                            out.write(EscPosBase.nextLine(4));
                        } else {
                            int iesp = Integer.parseInt(sespacos);
                            out.write(EscPosBase.nextLine(iesp));
                        }



                    } finally {
                        // btnPrint.setEnabled(true);
                        impressora.close();
                    }
                    //Se for vale transporte, proceder estorno
                    String sidpagto = dbbpe.Busca_Dados_Bpe(snumero, "Nidpag");
                    String stippag = dbbpe.Busca_Dados_Bpe(snumero, "Pagmto");
                    if (!sidpagto.equals("")) {
                        Log.i(TAG,"stippag: " + stippag);
                        if (stippag.equals("05")) {
                            com.x4fare.mobipix.onboard.sdk.MobiPixOnboard.getInstance().voidTransaction(ViaActivity.this, sidpagto , new OnboardCallback<MobiPixResponseDTO<VoidTransactionResponseDTO>>() {
                                @Override
                                public void onSuccess(MobiPixResponseDTO<VoidTransactionResponseDTO> response) {
                                    // processar a logica de sucesso
                                    int istatus = response.getStatus();
                                    Log.i(TAG,"callbackpay Calc: " + istatus);
                                    if (istatus == 200) {
                                        VoidTransactionResponseDTO responsecanc = VoidTransactionResponseDTO.class.cast(response.getResponse());
                                        RemoteTransactionStatus retstatus = responsecanc.getRollbackTransactionStatus();
                                        String status = retstatus.toString();
                                        // Log.i(TAG,"Status Cancel: " + status);
                                        if (status.equals("AUTHORIZED")) {
                                            //infMensagens("Venda Estornada.", "");
                                        } else {
                                            infMensagens(responsecanc.getMessage(), "");
                                        }


                                    }
                                }

                                @Override
                                public void onError(MobiPixResponseDTO<VoidTransactionResponseDTO> response) {
                                    // processar a logica de sucesso
                                    infMensagens(response.getMessageKey(), "");
                                    Log.i(TAG,"Erro Cancel: " + response.getErrorDetails());
                                }
                            });

                        } else {
                            DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                            String sultbil = dbemp.Busca_Dados_Emp(1, "Ultbil");
                            if (sultbil.equals(snumero)) {
                                Start_Cielo();
                                cancelPayment(snumero, smotivo2);
                            } else {
                                infMensagens("Pagamento com cartão.\nNão foi a última venda.\nCancelamento não Permitido.", "");
                            }
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }


    }


    public void Imprime_Cancel_LIO(final String snumero) {
                /*if (mBitmap == null) {
                    //mBitmap = BitmapFactory.decodeFile(getExternalStorageDirectory().getAbsolutePath() + "/Android/Data/NOVOS/logoemp.jpg");
                    //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
                }
                    try {
                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());
                        DB_BPE dbbpe = new DB_BPE(getApplicationContext());
                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");
                        PrinterManager printerManagerT = new PrinterManager(ViaActivity.this);

                        //printerManagerT.printImage(mBitmap, alignCenter, printerListener);
                        printerManagerT.printText("   ", alignLeft, printerListener);
                        printerManagerT.printText(dbemp.Busca_Dados_Emp(1,"Descri"), font_C126, printerListener);


                        String scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
                        String sIe = dbemp.Busca_Dados_Emp(1, "Insest");

                        printerManagerT.printText("CNPJ: "+scnpj+" IE: "+sIe, font_C220, printerListener);


                        String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                        sendere = dbemp.Busca_Dados_Emp(1, "Endere");
                        snum = dbemp.Busca_Dados_Emp(1, "Numero");
                        sbai = dbemp.Busca_Dados_Emp(1, "Bairro");
                        scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                        suf = dbemp.Busca_Dados_Emp(1, "UF");
                        sendp1 = sendere + ", " + snum;
                        sendp2 = sbai + ", " + scid + "-" + suf;

                        printerManagerT.printText(sendp1, font_C118, printerListener);
                        printerManagerT.printText(sendp2, font_C118, printerListener);


                        printerManagerT.printText("CANCELAMENTO", font_C128, printerListener);

                        String ssit = dbbpe.Busca_Dados_Bpe(snumero, "Sitbpe");
                        if (ssit.equals("CT")) { //emitido em contingencia
                            printerManagerT.printText("EMITIDO EM CONTINGÊNCIA", font_C128, printerListener);
                            printerManagerT.printText("Pendente de Autorização", font_C124, printerListener);
                        }

                        String slinha = dbbpe.Busca_Dados_Bpe(snumero, "Nomvia");
                        String sori = dbbpe.Busca_Dados_Bpe(snumero, "Treori");
                        String sdes = dbbpe.Busca_Dados_Bpe(snumero, "Tredes");
                        String svalor = dbbpe.Busca_Dados_Bpe(snumero, "Vlrpas");

                        printerManagerT.printText("Viagem: "+slinha, font_C120, printerListener);
                        printerManagerT.printText("Origem: "+sori, font_C120, printerListener);
                        printerManagerT.printText("Destino: "+sdes, font_C120, printerListener);


                        String sNROBPE = (("000000000" + snumero).substring(snumero.length()));
                        String Modser =  dbbpe.Busca_Dados_Bpe(snumero, "Modser");
                        String sSERBPE = Modser.substring(2, (5));

                        printerManagerT.printText("BP-e nº "+sNROBPE+"  Série "+sSERBPE, font_C124, printerListener);
                        printerManagerT.printText(" ", font_C124, printerListener);


                        String sespaco = "";
                        String sval = "Valor Total R$"+svalor;
                        Integer iqtd = sval.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32-iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }

                        //printerManagerT.printText("Valor Total R$" + sespaco+svalor, alignLeft, printerListener);

                        List<Map<String, Integer>> stylesMap5 =  new ArrayList<>();
                        stylesMap5.add(font_L126);
                        stylesMap5.add(font_R126);
                        String[] textsToPrint = new String[] { "Valor Total R$",  svalor  };

                        printerManagerT.printMultipleColumnText(textsToPrint, stylesMap5, printerListener);
                        printerManagerT.printText("   ", alignLeft, printerListener);


                        String shoreve = Funcoes_Android.getCurrentUTC();
                        String sanoe = shoreve.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        String smese = shoreve.substring(5, (7));
                        String sdiae = shoreve.substring(8, (10));
                        String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                        String sHorae = shoreve.substring(11, (19));


                        printerManagerT.printText("DH Evento: "+sdEmi+"  "+sHorae, font_C126, printerListener);
                        printerManagerT.printText(" ", font_C126, printerListener);
                        printerManagerT.printText("MOTIVO", font_C126, printerListener);




                        String smotivo = dbbpe.Busca_Dados_Bpe(snumero, "Rsv003");
                        printerManagerT.printText(smotivo, font_C126, printerListener);

                        if (sespacos.equals("xx")) {
                            printerManagerT.printText("   \n\n\n", alignLeft, printerListener);
                        } else {
                            int iesp = Integer.parseInt(sespacos);
                            String slinhas = "";
                            for( int i=0; i <iesp; i++)
                            {
                                slinhas=slinhas+"\n";


                            }
                            printerManagerT.printText(slinhas, alignLeft, printerListener);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

    }



    public void Confere_campos(){
        EditText edtVia = findViewById(R.id.edtVia);
        EditText edtOrigem = findViewById(R.id.edtOrigem);
        EditText edtDestino = findViewById(R.id.edtDestino);
        final Button btnFinalizar = findViewById(R.id.btnFinalizar);
        String sVia = edtVia.getText().toString();
        String sOri = edtOrigem.getText().toString();
        String sDes = edtDestino.getText().toString();
        if (!sVia.equals("Selecione a Viagem")){
            if (!sOri.equals("Selecione a Origem")){
                if (!sDes.equals("Selecione o Destino")){
                    Calcula_Valor_DB();
                    btnFinalizar.setEnabled(true);
                }
            }
        }



    }


    public void Confere_campos_DG(View viewc){
        EditText edtVia = viewc.findViewById(R.id.edtViac);
        EditText edtOrigem = viewc.findViewById(R.id.edtOrigemc);
        EditText edtDestino = viewc.findViewById(R.id.edtDestinoc);
        String sVia = edtVia.getText().toString();
        String sOri = edtOrigem.getText().toString();
        String sDes = edtDestino.getText().toString();
        if (!sVia.equals("Selecione a Viagem")){
            if (!sOri.equals("Selecione a Origem")){
                if (!sDes.equals("Selecione o Destino")){
                    Calcula_Valor_DG(viewc);
                }
            }
        }



    }


    public void Calcula_Valor_DB() {
        EditText edtVia = findViewById(R.id.edtVia);
        EditText edtOrigem = findViewById(R.id.edtOrigem);
        EditText edtDestino = findViewById(R.id.edtDestino);
        TextView edtqtd = findViewById(R.id.edtQtd);
        EditText edttipgra = findViewById(R.id.edtTipGra); //Tipo de Gratuidade do passageiro
        EditText edttipdes = findViewById(R.id.edtTipDes); //Tipo de Desconto
        String stipgra = edttipgra.getText().toString();
        String stipdes = edttipdes.getText().toString();


        String sqtd = edtqtd.getText().toString();
        int iqtd = Integer.parseInt(sqtd);
        String sVia = edtVia.getText().toString();
        String sOri = edtOrigem.getText().toString();
        String sDes = edtDestino.getText().toString();
        Double vlrTar, vlrSeg, vlrArre, vlrPas, vlremb;
        int posV = sVia.indexOf("-");
        int posO = sOri.indexOf("-");
        int posD = sDes.indexOf("-");
        String sCodVia = sVia.substring(0, (posV));
        String sCodOri = sOri.substring(0, (posO));
        String sCodDes = sDes.substring(0, (posD));
        int iVia = Integer.parseInt(sCodVia);
        int iOri = Integer.parseInt(sCodOri);
        int iDes = Integer.parseInt(sCodDes);

        DB_EMP dbemp = new DB_EMP(this);
        String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
        if (spvenda.equals("R")) { //rodiviaria
            vlremb=Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
        } else {
            vlremb = 0.00;
        }

        DB_VIA db = new DB_VIA(this);
        String Slinha = db.BuscaVia(iVia);

        TextView txtTarifa = findViewById(R.id.txtValor);
        txtTarifa.setText("0.00");

        DB_PER dbp = new DB_PER(this);
        DB_PER.PercursosCursor cursor = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            if (cursor.getLinha().equals(Slinha)){//mesma linha
                DB_TRE dbt = new DB_TRE(this);
                String Sorigem = dbt.BuscaTre(iOri);
                if (cursor.getOrigem().equals(Sorigem)){//mesma origem
                    String Sdestino = dbt.BuscaTre(iDes);
                    if (cursor.getDestino().equals(Sdestino)){//mesmo destino
                        //Toast.makeText(this, cursor.getTarifa().toString(), Toast.LENGTH_LONG).show();




                        vlrTar=Double.valueOf(cursor.getTarifa().toString()).doubleValue();
                        vlrSeg = Double.valueOf(cursor.getSeguro().toString()).doubleValue();
                        vlrArre=Double.valueOf(cursor.getArredonda().toString()).doubleValue();

                        final CheckBox ckbSeguro = findViewById(R.id.ckbSeguro);
                        if (!ckbSeguro.isChecked()){
                            vlrSeg = 0.00;
                        }
                        String sdstax = dbemp.Busca_Dados_Emp(1, "Ndstax");
                        if (!stipgra.equals("") && !stipgra.equals("00") && !stipdes.equals("")){
                            if (stipdes.equals("1")) {//Desconto integral
                                vlrTar = 0.00;
                                vlrSeg = 0.00;
                                vlrArre = 0.00;
                                if (!sdstax.equals("S")) {//desconta sobre taxa de embarque
                                    vlremb = 0.00;
                                }
                            }
                            if (stipdes.equals("2")) {//Desconto parcial
                                vlrTar = (vlrTar/2);
                                if (ckbSeguro.isChecked()){
                                    vlrSeg = (vlrSeg/2);
                                }
                                if (vlrArre>0) {vlrArre = (vlrArre/2);}
                                if (vlrArre<0) {vlrArre = vlrArre = 0.00;}
                                if (!sdstax.equals("S")) {//desconta sobre taxa de embarque
                                    vlremb = (vlremb/2);
                                }
                            }
                        }

                        vlrPas=((vlrTar+vlremb+vlrSeg+vlrArre)*iqtd);


                        String resultado = String.format("%.2f", vlrPas);



                        txtTarifa.setText(resultado);
                    }
                }
            }

        }



    }



    public void Calcula_Valor_DG(View viewc) {
        EditText edtVia = viewc.findViewById(R.id.edtViac);
        EditText edtOrigem = viewc.findViewById(R.id.edtOrigemc);
        EditText edtDestino = viewc.findViewById(R.id.edtDestinoc);
        TextView edtqtdcone = viewc.findViewById(R.id.edtQtdcone);
        TextView txtvalorini = viewc.findViewById(R.id.txtValorini);
        String sqtdcone = edtqtdcone.getText().toString();
        int iqtdcone = Integer.parseInt(sqtdcone);

        String sVia = edtVia.getText().toString();
        String sOri = edtOrigem.getText().toString();
        String sDes = edtDestino.getText().toString();
        Double vlrTar, vlrSeg, vlrArre, vlrPas, vlremb, vlrini, vlrtot;
        int posV = sVia.indexOf("-");
        int posO = sOri.indexOf("-");
        int posD = sDes.indexOf("-");
        String sCodVia = sVia.substring(0, (posV));
        String sCodOri = sOri.substring(0, (posO));
        String sCodDes = sDes.substring(0, (posD));
        int iVia = Integer.parseInt(sCodVia);
        int iOri = Integer.parseInt(sCodOri);
        int iDes = Integer.parseInt(sCodDes);

        DB_EMP dbemp = new DB_EMP(this);
        String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
        if (spvenda.equals("R")) { //rodiviaria
            vlremb=Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
        } else {
            vlremb = 0.00;
        }

        DB_VIA db = new DB_VIA(this);
        String Slinha = db.BuscaVia(iVia);


        DB_PER dbp = new DB_PER(this);
        DB_PER.PercursosCursor cursor = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);

        for( int i=0; i <cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            if (cursor.getLinha().equals(Slinha)){//mesma linha
                DB_TRE dbt = new DB_TRE(this);
                String Sorigem = dbt.BuscaTre(iOri);
                if (cursor.getOrigem().equals(Sorigem)){//mesma origem
                    String Sdestino = dbt.BuscaTre(iDes);
                    if (cursor.getDestino().equals(Sdestino)){//mesmo destino
                        //Toast.makeText(this, cursor.getTarifa().toString(), Toast.LENGTH_LONG).show();

                        vlrTar=Double.valueOf(cursor.getTarifa().toString()).doubleValue();
                        vlrSeg = Double.valueOf(cursor.getSeguro().toString()).doubleValue();
                        vlrArre=Double.valueOf(cursor.getArredonda().toString()).doubleValue();

                        final CheckBox ckbSeguro = viewc.findViewById(R.id.ckbSeguroc);
                        if (!ckbSeguro.isChecked()){
                            vlrSeg = 0.00;
                        }

                        vlrPas=((vlrTar+vlremb+vlrSeg+vlrArre)*iqtdcone);
                        String svalini = String.format("%.2f", (GuardaValor*iqtdcone));
                        txtvalorini.setText(svalini);
                        vlrtot=((GuardaValor*iqtdcone)+vlrPas);


                        String resultado = String.format("%.2f", vlrPas);
                        String stotal = String.format("%.2f", vlrtot);

                        TextView txtTarifa = viewc.findViewById(R.id.txtValorc);
                        txtTarifa.setText(resultado);
                        TextView txtvalortot = viewc.findViewById(R.id.txtValortot);
                        txtvalortot.setText(stotal);
                    }
                }
            }

        }



    }




    public void Gerar_XML(String sChamada){
        EditText edtVia = findViewById(R.id.edtVia);//codigo da viagem
        EditText edtOrigem = findViewById(R.id.edtOrigem);//trecho de origem
        EditText edtDestino = findViewById(R.id.edtDestino);//trecho de destino
        EditText edtNomPas = findViewById(R.id.edtNomePas); //nome do passageiro
        EditText edtDocPas = findViewById(R.id.edtDocPas); //documento de Identidade do passageiro
        EditText edtCpfPas = findViewById(R.id.edtCPFPas); //CPF Opcional
        EditText edttipgra = findViewById(R.id.edtTipGra); //Tipo de Gratuidade do passageiro
        EditText edttipdes = findViewById(R.id.edtTipDes); //Tipo de Desconto
        String sVia = edtVia.getText().toString();
        String sOri = edtOrigem.getText().toString();
        String sDes = edtDestino.getText().toString();
        String sTipgra = edttipgra.getText().toString();
        String sTipdes = edttipdes.getText().toString();
        Double vlrTar, vlrSeg, vlrArre, vlrPas, vlrpag, vlremb, destar, desemb, desseg, desarre, totaldes;
        int posV = sVia.indexOf("-");
        int posO = sOri.indexOf("-");
        int posD = sDes.indexOf("-");
        String sCodVia = sVia.substring(0, (posV));
        String sCodOri = sOri.substring(0, (posO));
        String sCodDes = sDes.substring(0, (posD));
        int iVia = Integer.parseInt(sCodVia);
        int iOri = Integer.parseInt(sCodOri);
        int iDes = Integer.parseInt(sCodDes);
        String IDBPE = "";
        String sMotivo = "";
        String sdataCT = "";
        String sCTG = "";
        String sfimCONEXAO;
        sfimCONEXAO = "";
        destar = 0.00;
        desemb = 0.00;
        desseg = 0.00;
        desarre = 0.00;
        totaldes = 0.00;

        boolean bconect = false;

        DB_EMP dbemp = new DB_EMP(this);
        sCTG = dbemp.Busca_Dados_Emp(1, "Tipemi");
        String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");

        if (sChamada.equals("N")) { //chamada foi Normal
            if (sCTG.equals("2")) { //emissao em contingencia informa campo obrigatorios
                sdataCT = dbemp.Busca_Dados_Emp(1, "Datctg");
                sMotivo = "TRECHO SEM CONEXAO COM INTERNET";
            }

        } else if (sChamada.equals("E")) { ///Chamada veio de um erro ao transmitir e entrara em contingencia
            sdataCT = Funcoes_Android.getCurrentUTC();
            sMotivo = "TRECHO SEM CONEXAO COM INTERNET";
            dbemp.Atualizar_Campo_Emp("1", "Tipemi", "2");
            dbemp.Atualizar_Campo_Emp("1", "Datctg", sdataCT);
            sCTG = "2";
        }


        DB_VIA dbvia = new DB_VIA(this);
        String Slinha = dbvia.BuscaVia(iVia);


        String sultimo = dbemp.Busca_Dados_Emp(1, "Ultbil");
        String sExiste = "";
        String sgeraxml = "";
        /////Verificar se o bilhete anterior foi concluído
        if (sChamada.equals("E")) { //retornou com erro da transmissao
            if (!sultimo.equals("0")) {
                DB_BPE dbconferebil = new DB_BPE(ViaActivity.this);
                Bilhetes = dbconferebil.VerificaBil(sultimo);
                if (Bilhetes.size() > 0) {
                    sExiste = "S";
                }
                if (sExiste.equals("S")) {
                    String sSit = dbconferebil.Busca_Dados_Bpe(sultimo, "Sitbpe");
                    if (sSit.equals("BA")) { //Retornou com erro, mas foi autorizado
                        sgeraxml = "";
                        String schavebpeaut = dbconferebil.Busca_Dados_Bpe(sultimo, "Chvbpe");

                        int iqtd = GuardaQtd;
                        if (smodimp.equals("01")) {
                            Imprime_BPe(); //SUNMI
                            if (iqtd == 1) {
                                sfimCONEXAO = "S";
                            }
                        } else if (smodimp.equals("03")) {
                            Imprime_BPe_BT(); //ARNY SP5 Bluetooth
                            if (iqtd == 1) {
                                sfimCONEXAO = "S";
                                GuardaMobi = "";
                                GuardaID = "";
                                Guarda_QRcode = "";
                            }
                        } else if (smodimp.equals("05")) {
                            Imprime_BPe_DTS(); //ARNY SP5 Bluetooth
                            if (iqtd == 1) {
                                sfimCONEXAO = "S";
                                GuardaMobi = "";
                                GuardaID = "";
                                Guarda_QRcode = "";
                            }
                        } else {
                            Imprime_BPe_Lio(); //CIELO LIO

                            if (iqtd == 1) {
                                GuardaPagto = "";
                                GuardaBand = "";
                                GuardaAut = "";
                                GuardaID = "";
                                sfimCONEXAO = "S";
                            }
                        }
                        if (EnviaConexao.equals("S")) {
                            EditText edtCad = findViewById(R.id.edtPoltrona); //Poltrona
                            EditText edtcpfpas = findViewById(R.id.edtCPFPas); //CPF do Passageiro Opcional
                            EditText edtvia = findViewById(R.id.edtVia);
                            EditText edtori = findViewById(R.id.edtOrigem);
                            EditText edtdes = findViewById(R.id.edtDestino);
                            edtCad.setText("");
                            edtNomPas.setText("");
                            edtDocPas.setText("");
                            edtcpfpas.setText("");
                            edttipgra.setText("");
                            if (EnviaConexao.equals("S")) {
                                if (sfimCONEXAO.equals("S")) {
                                    edtvia.setText(GuardaVia);
                                    edtori.setText(GuardaOri);
                                    edtdes.setText(GuardaDes);
                                    Linha_Trab = GuardaLinha;
                                    Calcula_Valor_DB();
                                    EnviaConexao = "";
                                }
                            }
                        }
                    } else if (!sSit.equals("BA")) { //bilhete está em outra situação ou ocorreu um erro com o anterior
                        sgeraxml = "S";
                    }
                } else {
                    sgeraxml = "S";
                }
            } else if (sultimo.equals("0")) {
                sgeraxml = "S";
            }
        } else { sgeraxml = "S";}


        if (sgeraxml.equals("S")) { //Gerar Novo Bilhete

            String novo = Criar_BPE(sVia, sOri, sDes, sChamada);
            DB_BPE dbbpe = new DB_BPE(this);
            IDBPE = dbbpe.Busca_Dados_Bpe(novo, "ID");
            String stpEmis = dbemp.Busca_Dados_Emp(1, "Tipemi");


            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory
                        .newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                // cria o elemento BPe
                Element rootElement = document.createElement("BPe");
                document.appendChild(rootElement);
                rootElement.setAttribute("xmlns", "http://www.portalfiscal.inf.br/bpe");

                // cria abertura do infBPe
                Element infBPeElement = document.createElement("infBPe");
                rootElement.appendChild(infBPeElement);
                //////////CRIAR CHAVE DE ACESSO//////////////
                String sID = "BPe";
                String sVersao = "1.00";
                String schave = "";
                String sdata = Funcoes_Android.getCurrentUTC();
                String sano = sdata.substring(2, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                String smes = sdata.substring(5, (7));
                String codmun = dbemp.Busca_Dados_Emp(1, "Codmun");
                String coduf = codmun.substring(0, (2));
                String scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
                String modser = dbbpe.Busca_Dados_Bpe(novo, "Modser");
                String smod = modser.substring(0, (2));
                String sser = modser.substring(2, (5));
                Integer iserie = Integer.parseInt(sser);
                String snumero = ("000000000" + novo).substring(novo.length());
                String srecnum = dbbpe.Busca_Dados_Bpe(novo, "ID");
                srecnum = ("00000000" + srecnum).substring(srecnum.length());
                schave = (coduf + sano + smes + scnpj + modser + snumero + stpEmis + srecnum);
                String sDV = Gerar_DVBPe(schave);
                String splaca = dbemp.Busca_Dados_Emp(1, "Rsv002");
                VeiculoWS = splaca;
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Codvei", splaca);
                //////////FIM CRIAR CHAVE DE ACESSO//////////////
                String chaveBPe = (schave + sDV);//GERAR DÍGITO VERIFICADOR
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Chvbpe", chaveBPe);
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Rsv002", splaca);
                sID = (sID + chaveBPe);

                infBPeElement.setAttribute("Id", sID);
                infBPeElement.setAttribute("versao", sVersao);


                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Datemi", sdata);

                String sultdatven = dbemp.Busca_Dados_Emp(1, "Ultdat");
                if (!sultdatven.equals("")){
                    String sdatatu = Funcoes_Android.getCurrentUTC();
                    String sanoa = sdatatu.substring(0, 4);
                    String smesa = sdatatu.substring(5,7);
                    String sdiaa = sdatatu.substring(8, 10);
                    sdatatu = sdiaa+"/"+smesa+"/"+sanoa;
                    //System.out.println("Data Atual: "+sdatatu);

                    String sanou = sultdatven.substring(0, 4);
                    String smesu = sultdatven.substring(5, 7);
                    String sdiau = sultdatven.substring(8, 10);
                    sultdatven = sdiau+"/"+smesu+"/"+sanou;
                    //System.out.println("Ultima Venda: "+sultdatven);
                    boolean bdata = Funcoes_Android.Verifica_Datas(sdatatu, sultdatven);
                    //System.out.println("Valida Data: "+bdata);
                    if (bdata) { //data valida
                        dbemp.Atualizar_Campo_Emp("1", "Ultdat", sdata);
                    }

                } else {
                    dbemp.Atualizar_Campo_Emp("1", "Ultdat", sdata);
                }

                String stipamb = dbemp.Busca_Dados_Emp(1, "Tipamb");

                Element ideElement = document.createElement("ide");
                infBPeElement.appendChild(ideElement);
                Element cUFElement = document.createElement("cUF");
                ideElement.appendChild(cUFElement);
                cUFElement.appendChild(document.createTextNode(coduf));
                Element tpAmbElement = document.createElement("tpAmb");
                ideElement.appendChild(tpAmbElement);
                tpAmbElement.appendChild(document.createTextNode(stipamb));
                Element modElement = document.createElement("mod");
                ideElement.appendChild(modElement);
                modElement.appendChild(document.createTextNode(smod));
                Element serElement = document.createElement("serie");
                ideElement.appendChild(serElement);
                serElement.appendChild(document.createTextNode(iserie.toString()));
                SerieWS = iserie.toString();
                Element nbpeElement = document.createElement("nBP");
                ideElement.appendChild(nbpeElement);
                nbpeElement.appendChild(document.createTextNode(novo));
                Element cbpElement = document.createElement("cBP");
                ideElement.appendChild(cbpElement);
                cbpElement.appendChild(document.createTextNode(srecnum));
                Element dvElement = document.createElement("cDV");
                ideElement.appendChild(dvElement);
                dvElement.appendChild(document.createTextNode(sDV)); //digito verificador
                Element modalElement = document.createElement("modal");
                ideElement.appendChild(modalElement);
                modalElement.appendChild(document.createTextNode("1"));//modal rodiviario
                Element dhemiElement = document.createElement("dhEmi");
                ideElement.appendChild(dhemiElement);
                dhemiElement.appendChild(document.createTextNode(sdata));
                DatemiWS = sdata;
                Element tpemisElement = document.createElement("tpEmis");
                ideElement.appendChild(tpemisElement);
                tpemisElement.appendChild(document.createTextNode(stpEmis));
                Element procElement = document.createElement("verProc");
                ideElement.appendChild(procElement);
                procElement.appendChild(document.createTextNode("1.00"));
                Element tpbpeElement = document.createElement("tpBPe");
                ideElement.appendChild(tpbpeElement);
                tpbpeElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Tipbil")));
                Element indpresElement = document.createElement("indPres");
                ideElement.appendChild(indpresElement);
                String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda"); //ponto de venda
                String sindpres = "5"; //Estrada - Operacao presencial embarcada
                if (spvenda.equals("R")) {
                    sindpres = "1";
                } //Rooviaria - Operacao presencial nao embarcada
                indpresElement.appendChild(document.createTextNode(sindpres)); //
                if (spvenda.equals("R")) { //rodoviaria incluir taxa de embarque
                    vlremb = Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
                } else {
                    vlremb = 0.00;
                }

                //Inicio da prescacao
                DB_TRE dbtre = new DB_TRE(this);
                Element ufiniElement = document.createElement("UFIni");
                ideElement.appendChild(ufiniElement);
                ufiniElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iOri, "UF")));
                Element muniniElement = document.createElement("cMunIni");
                ideElement.appendChild(muniniElement);
                muniniElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iOri, "Codmun")));

                //Destino da prestacao
                Element uffimElement = document.createElement("UFFim");
                ideElement.appendChild(uffimElement);
                uffimElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iDes, "UF")));
                Element munfimElement = document.createElement("cMunFim");
                ideElement.appendChild(munfimElement);
                munfimElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iDes, "Codmun")));

                //Tag Contingencia caso exista
                if (stpEmis.equals("2")) { //tipo de emissao em contingencia
                    Element datCTElement = document.createElement("dhCont");
                    ideElement.appendChild(datCTElement);
                    datCTElement.appendChild(document.createTextNode(sdataCT)); //Data e hora de entrada em contingencia
                    Element justElement = document.createElement("xJust");
                    ideElement.appendChild(justElement);
                    justElement.appendChild(document.createTextNode(sMotivo)); //Justificativa da contingencia
                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Sitbpe", "CT"); //salva no bilhete que foi contingencia
                }

                //Abrir Tag emitente
                Element emitElement = document.createElement("emit");
                infBPeElement.appendChild(emitElement);
                Element cnpjElement = document.createElement("CNPJ");
                emitElement.appendChild(cnpjElement);
                cnpjElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Cnpj")));
                Element ieElement = document.createElement("IE");
                emitElement.appendChild(ieElement);
                ieElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Insest")));
                Element nomeElement = document.createElement("xNome");
                emitElement.appendChild(nomeElement);
                nomeElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Descri")));
                Element imElement = document.createElement("IM");
                emitElement.appendChild(imElement);
                imElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Insmun")));
                Element cnaeElement = document.createElement("CNAE");
                emitElement.appendChild(cnaeElement);
                cnaeElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Cnae")));
                Element crtElement = document.createElement("CRT");
                emitElement.appendChild(crtElement);
                crtElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Codcrt")));
                //Endereco do emitente
                Element enderElement = document.createElement("enderEmit");
                emitElement.appendChild(enderElement);
                Element lgrElement = document.createElement("xLgr");
                enderElement.appendChild(lgrElement);
                lgrElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Endere")));
                Element numeElement = document.createElement("nro");
                enderElement.appendChild(numeElement);
                numeElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Numero")));
                //Complemento???
                Element bairroElement = document.createElement("xBairro");
                enderElement.appendChild(bairroElement);
                bairroElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Bairro")));
                Element cmunElement = document.createElement("cMun");
                enderElement.appendChild(cmunElement);
                cmunElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Codmun")));
                Element cidElement = document.createElement("xMun");
                enderElement.appendChild(cidElement);
                cidElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Cidade")));
                Element cepElement = document.createElement("CEP");
                enderElement.appendChild(cepElement);
                cepElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "CEP")));
                Element ufemiElement = document.createElement("UF");
                enderElement.appendChild(ufemiElement);
                ufemiElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "UF")));
                Element foneElement = document.createElement("fone");
                enderElement.appendChild(foneElement);
                foneElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Fone")));
                Element emaElement = document.createElement("email");
                enderElement.appendChild(emaElement);
                emaElement.appendChild(document.createTextNode(dbemp.Busca_Dados_Emp(1, "Email")));
                //Emitente continuacao
                String sANTT = dbemp.Busca_Dados_Emp(1, "CodTar");
                if (!sANTT.equals("0")) {
                    Element tarElement = document.createElement("TAR");
                    emitElement.appendChild(tarElement);
                    tarElement.appendChild(document.createTextNode(sANTT));
                }

                /////??????? FALTA DEFINIR COMO SERAO INFORMADOS ESTES DADOS
                //////Identificação do Comprador do BP-e
                ///////Identificação da agência/preposto/terceiro que comercializou o BP-e

                /////Informações dos BP-e de Substituição para remarcação e/ou transferência

                //Informacoes da passagem
                Element passagemElement = document.createElement("infPassagem");
                infBPeElement.appendChild(passagemElement);
                Element coriElement = document.createElement("cLocOrig");
                passagemElement.appendChild(coriElement);
                coriElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iOri, "Codigo")));
                TreoriWS = dbtre.Busca_Dados_Tre(iOri, "Codigo");
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Codori", dbtre.Busca_Dados_Tre(iOri, "Codigo"));
                Element locoriElement = document.createElement("xLocOrig");
                passagemElement.appendChild(locoriElement);
                locoriElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iOri, "Descri")));
                Element cdesElement = document.createElement("cLocDest");
                passagemElement.appendChild(cdesElement);
                cdesElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iDes, "Codigo")));
                TredesWS = dbtre.Busca_Dados_Tre(iDes, "Codigo");
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Coddes", dbtre.Busca_Dados_Tre(iDes, "Codigo"));
                Element locdesElement = document.createElement("xLocDest");
                passagemElement.appendChild(locdesElement);
                locdesElement.appendChild(document.createTextNode(dbtre.Busca_Dados_Tre(iDes, "Descri")));
                Element dhembElement = document.createElement("dhEmb");
                passagemElement.appendChild(dhembElement);
                dhembElement.appendChild(document.createTextNode(sdata)); //data de embarque igual ou superior da data de emissao
                String sanov = sdata.substring(0, (4));
                int ianoemi = Integer.parseInt(sanov);
                int ianovenc = (ianoemi + 1);
                String sanovenc = Integer.toString(ianovenc);
                String sdatvenc = sdata.replace(sanov, sanovenc);
                String smesdia = sdatvenc.substring(5, 10);
                if (smesdia.equals("02-29")) {
                    String smesvenc = "02-28";
                    sdatvenc = sdatvenc.replace(smesdia, smesvenc);//o ano seguinte nao sera bisexto
                }
                Element dhvencElement = document.createElement("dhValidade");
                passagemElement.appendChild(dhvencElement);
                dhvencElement.appendChild(document.createTextNode(sdatvenc));//validade da passagem

                /////Dados do Passageiro ??????????
                String sNome = edtNomPas.getText().toString();
                String sNumdoc = edtDocPas.getText().toString();
                String sCPF = edtCpfPas.getText().toString();
                String stipgra = edttipgra.getText().toString();
                GuardaNome = sNome;
                GuardaDoc = sNumdoc;
                GuardaCPF = sCPF;
                if (!sNome.equals("") && !sNumdoc.equals("")) {
                    Element passageiroElement = document.createElement("infPassageiro");
                    passagemElement.appendChild(passageiroElement);
                    Element nompasElement = document.createElement("xNome");
                    passageiroElement.appendChild(nompasElement);
                    sNome = sNome.toUpperCase();
                    nompasElement.appendChild(document.createTextNode(sNome));
                    if (!sCPF.equals("")) { //se informou o CPF
                        Element cpfElement = document.createElement("CPF");
                        passageiroElement.appendChild(cpfElement);
                        cpfElement.appendChild(document.createTextNode(sCPF));
                    }
                    String stipdoc = TipoDoc;
                    stipdoc = stipdoc.substring(0, 1);
                    Element tipdocElement = document.createElement("tpDoc");
                    passageiroElement.appendChild(tipdocElement);
                    tipdocElement.appendChild(document.createTextNode(stipdoc));//1-RG 2-Título de Eleitor 3-Passaporte 4-CNH
                    Element docpasElement = document.createElement("nDoc");
                    passageiroElement.appendChild(docpasElement);
                    docpasElement.appendChild(document.createTextNode(sNumdoc));
                }

                Element infviagemElement = document.createElement("infViagem");
                infBPeElement.appendChild(infviagemElement);
                Element clinhaElement = document.createElement("cPercurso");
                infviagemElement.appendChild(clinhaElement);
                clinhaElement.appendChild(document.createTextNode(dbvia.Busca_Dados_Via(iVia, "Linha")));
                LinviaWS = dbvia.Busca_Dados_Via(iVia, "Linha");
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Codlin", dbvia.Busca_Dados_Via(iVia, "Linha"));
                Element xlinhaElement = document.createElement("xPercurso");
                infviagemElement.appendChild(xlinhaElement);
                xlinhaElement.appendChild(document.createTextNode(dbvia.Busca_Dados_Via(iVia, "Viagem")));
                Element tpviaElement = document.createElement("tpViagem");
                infviagemElement.appendChild(tpviaElement);
                tpviaElement.appendChild(document.createTextNode(dbvia.Busca_Dados_Via(iVia, "Tipvia")));
                Element tpserElement = document.createElement("tpServ");
                infviagemElement.appendChild(tpserElement);
                tpserElement.appendChild(document.createTextNode(dbvia.Busca_Dados_Via(iVia, "Tipser")));
                Element tpacomoElement = document.createElement("tpAcomodacao");
                infviagemElement.appendChild(tpacomoElement);
                tpacomoElement.appendChild(document.createTextNode("1")); //Acento/Poltrona
                Element tptreElement = document.createElement("tpTrecho");
                infviagemElement.appendChild(tptreElement);
                tptreElement.appendChild(document.createTextNode("1")); //Normal

                //pega date e hora atuais
                String sdatsai = Funcoes_Android.getCurrentUTC();
                int itime = sdatsai.indexOf("T");
                String shoratu = sdatsai.substring((itime + 1), (itime + 9));
                String shorsai = dbvia.Busca_Dados_Via(iVia, "Hora");
                //pega hora da viagem
                shorsai = (shorsai + ":00");
                //troca a hora atual pela hora da viagem
                sdatsai = sdatsai.replace(shoratu, shorsai);
                //Toast.makeText(this, sdatsai, Toast.LENGTH_LONG).show();

                Element dhviaElement = document.createElement("dhViagem");
                infviagemElement.appendChild(dhviaElement);
                dhviaElement.appendChild(document.createTextNode(sdatsai));
                DatviaWS = sdatsai;
                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Datsai", sdatsai);
                String codlinha = dbvia.Busca_Dados_Via(iVia, "Linha");
                DB_LIN dblinha = new DB_LIN(this);
                Element prefixElement = document.createElement("prefixo");
                infviagemElement.appendChild(prefixElement);
                prefixElement.appendChild(document.createTextNode(dblinha.Busca_Dados_Lin(codlinha, "Prefix")));

                EditText edtPoltrona = findViewById(R.id.edtPoltrona);
                String spoltrona = edtPoltrona.getText().toString();
                if (spoltrona.equals("")) {
                    //nao informou poltrona
                    NumcadWS = "";
                    GuardaCad = "";
                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Numcad", "");
                } else {
                    int icad = Integer.parseInt(spoltrona);
                    if (icad < 60) {
                        Element cadElement = document.createElement("poltrona");
                        infviagemElement.appendChild(cadElement);
                        cadElement.appendChild(document.createTextNode(Integer.toString(icad)));
                        NumcadWS = spoltrona;
                        GuardaCad = spoltrona;
                        dbbpe.Atualizar_Campo_Bpe(IDBPE, "Numcad", spoltrona);
                    } else {
                        //Nao informar cadeira maior que 60
                        NumcadWS = "";
                        GuardaCad = "";
                        dbbpe.Atualizar_Campo_Bpe(IDBPE, "Numcad", "");
                    }
                }

                //////Informacoes do valor do BP-e
                Element infvalorElement = document.createElement("infValorBPe");
                infBPeElement.appendChild(infvalorElement);

                //procura os dados do percurso para compor o valor do bilhete
                DB_PER dbp = new DB_PER(this);
                DB_PER.PercursosCursor cursor = dbp.RetornarPercursos(DB_PER.PercursosCursor.OrdenarPor.NomeCrescente);

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    if (cursor.getLinha().equals(Slinha)) {//mesma linha
                        DB_TRE dbt = new DB_TRE(this);
                        String Sorigem = dbt.BuscaTre(iOri);
                        if (cursor.getOrigem().equals(Sorigem)) {//mesma origem
                            String Sdestino = dbt.BuscaTre(iDes);
                            if (cursor.getDestino().equals(Sdestino)) {//mesmo destino
                                //Toast.makeText(this, cursor.getTarifa().toString(), Toast.LENGTH_LONG).show();

                                vlrTar = Double.valueOf(cursor.getTarifa().toString()).doubleValue();
                                vlrSeg = Double.valueOf(cursor.getSeguro().toString()).doubleValue();
                                vlrArre = Double.valueOf(cursor.getArredonda().toString()).doubleValue();
                                final CheckBox ckbSeguro = findViewById(R.id.ckbSeguro);
                                if (!ckbSeguro.isChecked()) {
                                    vlrSeg = 0.00;
                                }

                                String sdstax = dbemp.Busca_Dados_Emp(1, "Ndstax");
                                if (!stipgra.equals("") && !stipgra.equals("00") && !sTipdes.equals("")){
                                    if (sTipdes.equals("1")) {//Desconto integral
                                        destar = vlrTar;
                                        desseg = vlrSeg;
                                        vlrArre = 0.00;
                                        if (!sdstax.equals("S")) {//desconta sobre taxa de embarque
                                            desemb = vlremb;
                                        }
                                    }
                                    if (sTipdes.equals("2")) {//Desconto parcial
                                        destar = (vlrTar/2);
                                        if (ckbSeguro.isChecked()){
                                            desseg = (vlrSeg/2);
                                        }
                                        if (vlrArre>0) {desarre = (vlrArre/2);}
                                        if (vlrArre<0) {vlrArre = vlrArre = 0.00;}
                                        if (!sdstax.equals("S")) {//desconta sobre taxa de embarque
                                            desemb = (vlremb/2);
                                        }
                                    }
                                    totaldes = (destar+desemb+desseg+desarre);
                                    vlrTar = (vlrTar-destar);
                                    vlrSeg = (vlrSeg-desseg);
                                    vlremb = (vlremb-desemb);
                                    vlrArre = (vlrArre-desarre);
                                }



                                VlrtarWS = vlrTar.toString();
                                VlrsegWS = vlrSeg.toString();
                                VlrarreWS = vlrArre.toString();
                                VlrembWS = vlremb.toString();
                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlrtar", vlrTar.toString());
                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlremb", vlremb.toString());
                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlrseg", vlrSeg.toString());
                                dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlrarr", vlremb.toString());
                                //Toast.makeText(this, "Tipvia: "+cursor.getTipvia(), Toast.LENGTH_LONG).show();
                                TipviaWS = cursor.getTipvia();


                                double vlrdesc = 0.00;
                                String sdesconto = "";
                                if (vlrArre < 0) {
                                    vlrdesc = (vlrArre * -1);
                                    vlrPas = vlrTar + vlremb + vlrSeg - vlrdesc;
                                    String resultado = String.format("%.2f", (vlrPas + vlrdesc));

                                    String valBP = resultado.replace(",", ".");

                                    Element vbpElement = document.createElement("vBP");
                                    infvalorElement.appendChild(vbpElement);
                                    vbpElement.appendChild(document.createTextNode(valBP)); //Valor do Bilhete

                                    //vlrdesc = 0;
                                    sdesconto = String.format("%.2f", vlrdesc);
                                    String valDesc = sdesconto.replace(",", ".");

                                    Element vdescElement = document.createElement("vDesconto");
                                    infvalorElement.appendChild(vdescElement);
                                    vdescElement.appendChild(document.createTextNode(valDesc));

                                    vlrpag = vlrTar + vlremb + vlrSeg + vlrArre;
                                    GuardaValor = vlrpag;
                                    String valpag = String.format("%.2f", vlrpag);
                                    String valPgto = valpag.replace(",", ".");
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlrpas", valpag);

                                    Element vpagElement = document.createElement("vPgto");
                                    infvalorElement.appendChild(vpagElement);
                                    vpagElement.appendChild(document.createTextNode(valPgto));

                                    Element trocoElement = document.createElement("vTroco");
                                    infvalorElement.appendChild(trocoElement);
                                    trocoElement.appendChild(document.createTextNode("0.00"));

                                    Element tpdescElement = document.createElement("tpDesconto");
                                    infvalorElement.appendChild(tpdescElement);
                                    tpdescElement.appendChild(document.createTextNode("99"));//tipo de desconto

                                    Element xdescElement = document.createElement("xDesconto");
                                    infvalorElement.appendChild(xdescElement);
                                    xdescElement.appendChild(document.createTextNode("Outros"));

                                    Element cdescElement = document.createElement("cDesconto");
                                    infvalorElement.appendChild(cdescElement);
                                    cdescElement.appendChild(document.createTextNode("99"));


                                } else {
                                    vlrPas = vlrTar + vlremb + vlrSeg + vlrArre;
                                    String resultado2 = String.format("%.2f", vlrPas);

                                    String valBP2 = resultado2.replace(",", ".");

                                    Element vbpElement = document.createElement("vBP");
                                    infvalorElement.appendChild(vbpElement);
                                    vbpElement.appendChild(document.createTextNode(valBP2)); //Valor do Bilhete


                                    Element vdescElement = document.createElement("vDesconto");
                                    infvalorElement.appendChild(vdescElement);
                                    vdescElement.appendChild(document.createTextNode("0.00"));

                                    vlrpag = vlrTar + vlremb + vlrSeg + vlrArre;
                                    GuardaValor = vlrpag;
                                    String valpag2 = String.format("%.2f", vlrpag);
                                    String valPgto2 = valpag2.replace(",", ".");
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Vlrpas", valpag2);

                                    Element vpagElement = document.createElement("vPgto");
                                    infvalorElement.appendChild(vpagElement);
                                    vpagElement.appendChild(document.createTextNode(valPgto2));

                                    Element trocoElement = document.createElement("vTroco");
                                    infvalorElement.appendChild(trocoElement);
                                    trocoElement.appendChild(document.createTextNode("0.00"));

                                }

                                ////Componentes do valor do bilhete
                                String sTarifa = String.format("%.2f", vlrTar);
                                String sTxemb = String.format("%.2f", vlremb);
                                String sSeguto = String.format("%.2f", vlrSeg);
                                String sOutros = "";
                                String valTar = sTarifa.replace(",", ".");
                                String valSeg = sSeguto.replace(",", ".");
                                String valemb = sTxemb.replace(",", ".");


                                Element compElement = document.createElement("Comp");
                                infvalorElement.appendChild(compElement);
                                Element tpcompElement = document.createElement("tpComp");
                                compElement.appendChild(tpcompElement);
                                tpcompElement.appendChild(document.createTextNode("01")); //Tarifa
                                Element vcompElement = document.createElement("vComp");
                                compElement.appendChild(vcompElement);
                                vcompElement.appendChild(document.createTextNode(valTar));

                                if (spvenda.equals("R")) { //Rodoviaria destaca taxa de embarque
                                    Element compeElement = document.createElement("Comp");
                                    infvalorElement.appendChild(compeElement);
                                    Element tpcompeElement = document.createElement("tpComp");
                                    compeElement.appendChild(tpcompeElement);
                                    tpcompeElement.appendChild(document.createTextNode("03")); //Taxa de Embarque
                                    Element vcompeElement = document.createElement("vComp");
                                    compeElement.appendChild(vcompeElement);
                                    vcompeElement.appendChild(document.createTextNode(valemb));
                                }

                                Element compsElement = document.createElement("Comp");
                                infvalorElement.appendChild(compsElement);
                                Element tpcompsElement = document.createElement("tpComp");
                                compsElement.appendChild(tpcompsElement);
                                tpcompsElement.appendChild(document.createTextNode("04")); //Seguro
                                Element vcompsElement = document.createElement("vComp");
                                compsElement.appendChild(vcompsElement);
                                vcompsElement.appendChild(document.createTextNode(valSeg));


                                if (vlrArre > 0) {
                                    sOutros = String.format("%.2f", vlrArre);
                                    String valOut = sOutros.replace(",", ".");
                                    Element compoElement = document.createElement("Comp");
                                    infvalorElement.appendChild(compoElement);
                                    Element tpcompoElement = document.createElement("tpComp");
                                    compoElement.appendChild(tpcompoElement);
                                    tpcompoElement.appendChild(document.createTextNode("99")); //Outros
                                    Element vcompoElement = document.createElement("vComp");
                                    compoElement.appendChild(vcompoElement);
                                    vcompoElement.appendChild(document.createTextNode(valOut));

                                }


                                //Informacoes do imposto
                                Element impElement = document.createElement("imp");
                                infBPeElement.appendChild(impElement);
                                String simples = dbemp.Busca_Dados_Emp(1, "Empspl");
                                if (simples.equals("N")) { //empresas do regime normal
                                    Element icmsElement = document.createElement("ICMS");
                                    impElement.appendChild(icmsElement);
                                    Element icms00Element = document.createElement("ICMS00");
                                    icmsElement.appendChild(icms00Element);
                                    Element cstElement = document.createElement("CST");
                                    icms00Element.appendChild(cstElement);
                                    cstElement.appendChild(document.createTextNode("00"));
                                    String sbasseg = dbemp.Busca_Dados_Emp(1,"Basseg");

                                    double baseicms = vlrTar + vlrArre;
                                    //se a o seguro compoe a base de calculo do ICMS
                                    if (sbasseg.equals("S")) {
                                        baseicms = baseicms + vlrSeg;
                                    }
                                    String sBase = String.format("%.2f", baseicms);
                                    String valBase = sBase.replace(",", ".");
                                    Element baseElement = document.createElement("vBC");
                                    icms00Element.appendChild(baseElement);
                                    baseElement.appendChild(document.createTextNode(valBase));
                                    double aliq = Double.valueOf(dbemp.Busca_Dados_Emp(1, "Aliicm")).doubleValue();
                                    String sAliq = String.format("%.2f", aliq);
                                    String valAliq = sAliq.replace(",", ".");
                                    Element aliqElement = document.createElement("pICMS");
                                    icms00Element.appendChild(aliqElement);
                                    aliqElement.appendChild(document.createTextNode(valAliq));
                                    double icms = (baseicms / 100 * aliq);
                                    BigDecimal valorExato = new BigDecimal(icms).setScale(2, RoundingMode.HALF_DOWN);
                                    String sIcms = String.format("%.2f", valorExato);
                                    String valICMS = sIcms.replace(",", ".");
                                    Element vicmsElement = document.createElement("vICMS");
                                    icms00Element.appendChild(vicmsElement);
                                    vicmsElement.appendChild(document.createTextNode(valICMS));


                                } else { //empresas do simples nacional
                                    Element icmssnElement = document.createElement("ICMSSN");
                                    impElement.appendChild(icmssnElement);
                                    Element cstsnElement = document.createElement("CST");
                                    icmssnElement.appendChild(cstsnElement);
                                    cstsnElement.appendChild(document.createTextNode("90"));
                                    Element indsnElement = document.createElement("indSN");
                                    icmssnElement.appendChild(indsnElement);
                                    indsnElement.appendChild(document.createTextNode("1"));

                                }

                                //Valor Total dos Tributos
                                double aliqtri = Double.valueOf(dbemp.Busca_Dados_Emp(1, "Alitri")).doubleValue();
                                double vtrib = (vlrPas / 100 * aliqtri);
                                BigDecimal tribExato = new BigDecimal(vtrib).setScale(2, RoundingMode.HALF_DOWN);
                                String stottrib = String.format("%.2f", tribExato);
                                stottrib = stottrib.replace(",", ".");
                                Element vtribElement = document.createElement("vTotTrib");
                                impElement.appendChild(vtribElement);
                                vtribElement.appendChild(document.createTextNode(stottrib));


                                //infAdFisco 2 Informações adicionais de interesse do Fisco???


                                //Informacoes do pagamento
                                Element pagElement = document.createElement("pag");
                                infBPeElement.appendChild(pagElement);
                                Element tpagElement = document.createElement("tPag");
                                pagElement.appendChild(tpagElement);
                                String spagmto = GuardaPagto;
                                String spagmobi = GuardaMobi;
                                Log.i(TAG,"spagmobi: " + spagmobi);
                                String stippag = "";
                                if (spagmto.equals("") && spagmobi.equals("")) {
                                    tpagElement.appendChild(document.createTextNode("01"));
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Pagmto", "DINHEIRO");
                                } else {
                                    if (!spagmto.equals("")) {
                                        if (spagmto.equals("CREDITO")) {
                                            stippag = "03";
                                        } //Credito
                                        else if (spagmto.equals("DEBITO")) {
                                            stippag = "04";
                                        } //Debito
                                        else {
                                            stippag = "99";
                                        }
                                        tpagElement.appendChild(document.createTextNode(stippag));
                                        dbbpe.Atualizar_Campo_Bpe(IDBPE, "Pagmto", stippag);
                                    }
                                    if (!spagmobi.equals("")) {
                                        if (spagmobi.equals("VALE TRANSPORTE")) {
                                            stippag = "05";
                                            String sidtrs = GuardaID;
                                            tpagElement.appendChild(document.createTextNode(stippag));
                                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Pagmto", stippag);
                                            dbbpe.Atualizar_Campo_Bpe(IDBPE, "Nidpag", sidtrs);
                                        } //Vale Transporte
                                    }
                                }

                                vlrpag = vlrTar + vlremb + vlrSeg + vlrArre;
                                String sValpag = String.format("%.2f", vlrpag);
                                String valForma = sValpag.replace(",", ".");
                                Element vpagElement = document.createElement("vPag");
                                pagElement.appendChild(vpagElement);
                                vpagElement.appendChild(document.createTextNode(valForma));

                                if (!spagmto.equals("")) {
                                    String sband = GuardaBand;
                                    String sautoriza = GuardaAut;
                                    String scodband = "";
                                    String sIDpag = GuardaID;
                                    if (sband.equals("VISA") || sband.equals("Visa") ) { scodband = "01";}
                                    else if (sband.equals("MASTERCARD") || sband.equals("Mastercard")) {scodband = "02";}
                                    else if (sband.equals("ELO") || sband.equals("Elo")) {scodband = "05";}
                                    else { scodband = "99";}
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Codban", sband);
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Codaut", sautoriza);
                                    dbbpe.Atualizar_Campo_Bpe(IDBPE, "Nidpag", sIDpag);
                                    Element cardElement = document.createElement("card");
                                    pagElement.appendChild(cardElement);
                                    Element integraElement = document.createElement("tpIntegra");
                                    cardElement.appendChild(integraElement);
                                    integraElement.appendChild(document.createTextNode("2"));
                                    Element bandElement = document.createElement("tBand");
                                    cardElement.appendChild(bandElement);
                                    bandElement.appendChild(document.createTextNode(scodband));
                                    Element autElement = document.createElement("cAut");
                                    cardElement.appendChild(autElement);
                                    autElement.appendChild(document.createTextNode(sautoriza));
                                    spagmto = sband+" "+spagmto;
                                    PagWS = spagmto;
                                }
                                if (!spagmobi.equals("")) {
                                    PagWS = spagmobi;
                                }


                            }
                        }
                    }

                }

                //Informacoes adicionais
                //informacoes do responsavel tecnico
                Element resptecElement = document.createElement("infRespTec");
                infBPeElement.appendChild(resptecElement);
                Element cnpjrespElement = document.createElement("CNPJ");
                resptecElement.appendChild(cnpjrespElement);
                cnpjrespElement.appendChild(document.createTextNode("00545878000102"));
                Element nomerespElement = document.createElement("xContato");
                resptecElement.appendChild(nomerespElement);
                nomerespElement.appendChild(document.createTextNode("MARCIO CAETANO"));
                Element emarespElement = document.createElement("email");
                resptecElement.appendChild(emarespElement);
                emarespElement.appendChild(document.createTextNode("hm@hm.inf.br"));
                Element fonerespElement = document.createElement("fone");
                fonerespElement.appendChild(document.createTextNode("2737226990"));
                resptecElement.appendChild(fonerespElement);

                // cria Informacoes suplementares
                Element infCplElement = document.createElement("infBPeSupl");
                rootElement.appendChild(infCplElement);
                Element QRcodeElement = document.createElement("qrCodBPe");
                infCplElement.appendChild(QRcodeElement);
                String sQRCode = "";
                if (stpEmis.equals("2")) { //emitindo em contingencia
                    //Passi 1 - Converter texto para byte
                    byte[] data = new byte[0];
                    try {
                        data = chaveBPe.getBytes("UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } finally {
                        String scodmod = ""; //Modulus
                        String scodexp = ""; //Expoente
                        String scodd = ""; //D
                        String scodp = ""; //P
                        String scodq = ""; //Q
                        String scoddp = ""; //DP
                        String scoddq = ""; //DQ
                        String scodinv = ""; //InverseQ

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
                        sQRCode = dbemp.Busca_Dados_Emp(1, "Urlqrc") + "?chBPe=" + chaveBPe + "&tpAmb=" + dbemp.Busca_Dados_Emp(1, "Tipamb") + "&sign=" + schaveAssign;
                    }
                } else if (stpEmis.equals("1")) { //emissao normal
                    sQRCode = dbemp.Busca_Dados_Emp(1, "Urlqrc") + "?chBPe=" + chaveBPe + "&tpAmb=" + dbemp.Busca_Dados_Emp(1, "Tipamb");
                }
                QRcodeElement.appendChild(document.createCDATASection(sQRCode)); //para colocar o texto dentro de ![CDATA[]]


                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                Properties outFormat = new Properties();
                outFormat.setProperty(OutputKeys.INDENT, "yes");
                outFormat.setProperty(OutputKeys.METHOD, "xml");
                outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                outFormat.setProperty(OutputKeys.VERSION, "1.0");
                outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperties(outFormat);
                DOMSource domSource = new DOMSource(document.getDocumentElement());
                OutputStream output = new ByteArrayOutputStream();
                StreamResult result = new StreamResult(output);
                transformer.transform(domSource, result);

                String xmlString = output.toString();


                //////////////////////
                FileOutputStream fos;
                try {
                    File sdCard = getExternalFilesDir("Download");
                    File dir = new File(sdCard.getAbsolutePath() );
                    dir.mkdirs();
                    File file = new File(dir, chaveBPe + "-bpe.xml");
                    Nome_Arquivo = (chaveBPe + "-bpe.xml");
                    fos = new FileOutputStream(file);
                    fos.write(xmlString.getBytes());
                    fos.flush();
                    fos.close();

                } catch (Exception e) {
                }
                ///////////////////////////
                Guarda_Texto = chaveBPe;
                sfimCONEXAO = "";
                if (stpEmis.equals("2")) { //emitindo em contingencia
                    EditText edtCad = findViewById(R.id.edtPoltrona); //Poltrona
                    EditText edtcpfpas = findViewById(R.id.edtCPFPas); //CPF do Passageiro Opcional
                    if (smodimp.equals("01")) {
                        Imprime_BPe(); //SUNMI
                        int iqtd = GuardaQtd;
                        if (iqtd > 1) {
                            iqtd = (iqtd - 1);
                            GuardaQtd = iqtd;
                            Chama_Fechamento();
                        } else {
                            sfimCONEXAO = "S";
                        }
                    } else if (smodimp.equals("03")) {
                        Imprime_BPe_BT(); //ARNY SP5 Bluetooth
                        int iqtd = GuardaQtd;
                        if (iqtd > 1) {
                            iqtd = (iqtd - 1);
                            GuardaQtd = iqtd;
                            Chama_Fechamento();
                        } else {
                            sfimCONEXAO = "S";
                            GuardaMobi = "";
                            GuardaID = "";
                            Guarda_QRcode = "";
                        }
                    } else if (smodimp.equals("05")) {
                        Imprime_BPe_DTS(); //AR-2500 Bluetooth
                        int iqtd = GuardaQtd;
                        if (iqtd > 1) {
                            iqtd = (iqtd - 1);
                            GuardaQtd = iqtd;
                            Chama_Fechamento();
                        } else {
                            sfimCONEXAO = "S";
                            GuardaMobi = "";
                            GuardaID = "";
                            Guarda_QRcode = "";
                        }
                    } else {
                        Imprime_BPe_Lio(); //CIELO LIO
                        int iqtd = GuardaQtd;
                        if (iqtd == 1) {
                            GuardaPagto = "";
                            GuardaBand = "";
                            GuardaAut = "";
                            GuardaID = "";
                            sfimCONEXAO = "S";
                        } else {
                            if (iqtd > 1) {
                                iqtd = (iqtd - 1);
                                GuardaQtd = iqtd;
                                Chama_Fechamento();
                            }
                        }
                    }
                    edtCad.setText("");
                    edtNomPas.setText("");
                    edtDocPas.setText("");
                    edtcpfpas.setText("");
                    Menu_Lateral();
                    if (EnviaConexao.equals("S")) {

                        EditText edtvia = findViewById(R.id.edtVia);
                        EditText edtori = findViewById(R.id.edtOrigem);
                        EditText edtdes = findViewById(R.id.edtDestino);
                        edtCad.setText("");
                        edtNomPas.setText("");
                        edtDocPas.setText("");
                        edtcpfpas.setText("");
                        if (EnviaConexao.equals("S")) {
                            if (sfimCONEXAO.equals("S")) {
                                edtvia.setText(GuardaVia);
                                edtori.setText(GuardaOri);
                                edtdes.setText(GuardaDes);
                                Linha_Trab = GuardaLinha;
                                Calcula_Valor_DB();
                                EnviaConexao = "";
                            }
                        }
                    }
                } else if (stpEmis.equals("1")) { //emitindo em contingencia
                    WScomando = "TRANSMITE";
                    Activity_Dados = 12;
                    // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                    Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);

                    String txt = Nome_Arquivo;
                    Bundle bundle = new Bundle();

                    bundle.putString("XML", txt);
                    bundle.putString("USUARIO", Nome_user);

                    bundle.putString("LINVIA", LinviaWS);
                    bundle.putString("DATVIA", DatviaWS);
                    bundle.putString("NUMCAD", NumcadWS);
                    bundle.putString("TREORI", TreoriWS);
                    bundle.putString("TREDES", TredesWS);
                    bundle.putString("VLRTAR", VlrtarWS);
                    bundle.putString("VLRSEG", VlrsegWS);
                    bundle.putString("VLRARRE", VlrarreWS);
                    bundle.putString("SERIE", SerieWS);
                    bundle.putString("DATEMI", DatemiWS);
                    bundle.putString("VEICULO", VeiculoWS);
                    bundle.putString("PAGAMENTO", PagWS);
                    bundle.putString("TIPVIA", TipviaWS);
                    bundle.putString("COMANDO", WScomando);
                    bundle.putString("MOTIVO", "");
                    bundle.putString("CANCEL", "");
                    bundle.putString("Activity_Dados", "12");
                    myIntent.putExtras(bundle);



                    String sdathoratu = Funcoes_Android.getCurrentUTC();
                    HoraInicio = sdathoratu;

                    // chama esse intent e aguarda resultado
                    String sdatchamada = Funcoes_Android.getCurrentUTC();
                    System.out.println(sdatchamada + "Chamei Transmissao Normal:");
                    startForresult.launch(myIntent);


                }


            } catch (ParserConfigurationException e) {
            } catch (TransformerConfigurationException e) {
            } catch (TransformerException e) {
            }
        } //if (sgeraxml.equals("S")) { //Gerar Novo Bilhete
    }



    public void Imprime_BPe(){
        ThreadPoolManager.getInstance().executeTask(new Runnable(){

            @Override
            public void run() {
                if( mBitmap == null ){
                    mBitmap = BitmapFactory.decodeFile(getExternalFilesDir("Download").getAbsolutePath() + "/logoemp.jpg");
                    //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
                }
                try {
                    try {
                        String sNROBPE = "";
                        String sSERBPE = "";
                        String stpEmis = "";
                        String sdatemi = "";
                        String sQRcode = "";
                        String stottrib = "";
                        String sPassageiro = "";
                        String sDocpas = "";
                        String sCPF = "";
                        String stipamb = "";
                        String sxdesconto = "";
                        String chaveBPe = Guarda_Texto;
                        System.out.println("chaveBPe: "+chaveBPe);
                        stpEmis = chaveBPe.substring(34, (35)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        //Toast.makeText(ViaActivity.this, "Tipo: "+stpEmis, Toast.LENGTH_LONG).show();
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        String sextencao = "";
                        if (stpEmis.equals("1")){
                            sextencao = "-procBPe.xml";
                        }
                        else if (stpEmis.equals("2")) {
                            sextencao = "-bpe.xml";
                        }
                        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        //Document doc = dBuilder.parse(getAssets().open("Arquivo.xml"));
                        Document doc = dBuilder.parse(fXmlFile);

                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");

                        doc.getDocumentElement().normalize();
                        NodeList nodeProc = null;
                        //NodeList nodebpe = null;
                        if (stpEmis.equals("1")){
                            nodeProc = doc.getElementsByTagName("bpeProc");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        } else if (stpEmis.equals("2")) {
                            nodeProc = doc.getElementsByTagName("BPe");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        }
                        int counter = nodeProc.getLength();
                        for (int temp = 0; temp < nodeProc.getLength(); temp++) {

                            Node nNode = nodeProc.item(temp);

                           // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;

                                NodeList nodeinfbpe = doc.getElementsByTagName("infBPe");
                                for (int tempinf = 0; tempinf < nodeinfbpe.getLength(); tempinf++) {


                                    Node ninf = nodeinfbpe.item(tempinf);
                                    Element infElement = (Element) ninf;
                                    //////////////////////////////////////////////
                                    NodeList nodeide = doc.getElementsByTagName("ide");
                                    for (int tempide = 0; tempide < nodeide.getLength(); tempide++){
                                        Node nide = nodeide.item(tempide);
                                        Element ideElement = (Element) nide;
                                        sNROBPE = ideElement.getElementsByTagName("nBP").item(0).getTextContent();
                                        sSERBPE = ideElement.getElementsByTagName("serie").item(0).getTextContent();
                                        sdatemi = ideElement.getElementsByTagName("dhEmi").item(0).getTextContent();
                                        stipamb = ideElement.getElementsByTagName("tpAmb").item(0).getTextContent();
                                        int inumero = Integer.parseInt(sNROBPE);
                                        DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                                        String sIDBpe = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "ID");
                                        String sqtdimp = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "Qtdimp");
                                        int iqtdatu = (Integer.parseInt(sqtdimp)+1);
                                        dbbpe.Atualizar_Campo_Bpe(sIDBpe, "Qtdimp", Integer.toString(iqtdatu));
                                    }

                                    //////////////////////////////////////////////





                                    float size = 30;
                                    woyouService.setAlignment(1, callback);
                                    woyouService.printBitmap(mBitmap, callback);
                                    woyouService.lineWrap(1, null);

                                    woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                                    woyouService.setAlignment (1, callback); //Alinhamento
                                                                                      //0 - Alinhar a Esquerda
                                                                                      //1 - Centralizado
                                                                                      //2 - Alinhado a Direita
                                    woyouService.printTextWithFont((dbemp.Busca_Dados_Emp(1,"Descri")), "", 24,null);
                                    woyouService.lineWrap(1, null);

                                    woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x0}, callback); // Desativar Fonte em Negrito

                                    String scnpj = dbemp.Busca_Dados_Emp(1,"Cnpj");
                                    String sIe = dbemp.Busca_Dados_Emp(1,"Insest");



                                    woyouService.printTextWithFont("CNPJ: "+scnpj+" IE: "+sIe, "", 22,null);
                                    woyouService.lineWrap(1, null);

                                    String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                                    sendere = dbemp.Busca_Dados_Emp(1,"Endere");
                                    snum = dbemp.Busca_Dados_Emp(1, "Numero");
                                    sbai = dbemp.Busca_Dados_Emp(1,"Bairro");
                                    scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                                    suf = dbemp.Busca_Dados_Emp(1, "UF");
                                    sendp1 = sendere+", "+snum;
                                    sendp2 = sbai+", "+scid+"-"+suf;

                                    woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                                    woyouService.printTextWithFont(sendp1, "", 18,null);
                                    woyouService.lineWrap(1, null);
                                    woyouService.printTextWithFont(sendp2, "", 18,null);
                                    woyouService.lineWrap(1, null);

                                    woyouService.printTextWithFont("Documento Auxiliar do Bilhete", "", 22,null);
                                    woyouService.lineWrap(1, null);
                                    woyouService.printTextWithFont("de Passagem Eletrônico", "", 22,null);
                                    woyouService.lineWrap(1, null);
                                    if (stpEmis.equals("2")){
                                        woyouService.printTextWithFont("EMITIDO EM CONTINGÊNCIA", "", 28,null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("Pendente de Autorização", "", 22,null);
                                        woyouService.lineWrap(1, null);
                                    }
                                    if (stipamb.equals("2")) { //Homologacao
                                        woyouService.printTextWithFont("--------------------------------", "", 24,null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("EMITIDO EM HOMOLOGAÇÃO", "", 26,null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("SEM VALOR FISCAL", "", 26,null);
                                        woyouService.lineWrap(1, null);
                                    }


                                    woyouService.printTextWithFont("--------------------------------", "", 24,null);
                                    woyouService.lineWrap(1, null);


                                    woyouService.setAlignment (0, callback); //Alinhamento

                                    ///////////
                                    //Dados da Viagem
                                    String sdatemb = "";
                                    NodeList nodeviagem1 = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem1.getLength(); tempvia++){
                                        Node nviagem1 = nodeviagem1.item(tempvia);
                                        Element viaElement1 = (Element) nviagem1;
                                        sdatemb = viaElement1.getElementsByTagName("dhViagem").item(0).getTextContent();

                                    }

                                    NodeList nodepassagem = doc.getElementsByTagName("infPassagem");
                                    for (int temppass = 0; temppass < nodepassagem.getLength(); temppass++){
                                        Node npass = nodepassagem.item(temppass);
                                        Element passElement = (Element) npass;
                                        DB_TRE dbtre = new DB_TRE(getApplicationContext());
                                        String cOri = passElement.getElementsByTagName("cLocOrig").item(0).getTextContent();
                                        String xOri = passElement.getElementsByTagName("xLocOrig").item(0).getTextContent();
                                        String ufOri = dbtre.Busca_Dados_Tre_Codigo(cOri, "UF");
                                        String sOrigem = xOri+" ("+ufOri+")";
                                        String cDes = passElement.getElementsByTagName("cLocDest").item(0).getTextContent();
                                        String xDes = passElement.getElementsByTagName("xLocDest").item(0).getTextContent();
                                        String ufDes = dbtre.Busca_Dados_Tre_Codigo(cDes, "UF");
                                        String sDestino = xDes+" ("+ufDes+")";
                                        //String sdatemb = passElement.getElementsByTagName("dhEmb").item(0).getTextContent();
                                        String sano = sdatemb.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smes = sdatemb.substring(5, (7));
                                        String sdia = sdatemb.substring(8, (10));
                                        String sdEmb = sdia+"/"+smes+"/"+sano;
                                        String sHora = sdatemb.substring(11, (16));
                                        woyouService.setAlignment (1, callback); //Alinhamento centralizado
                                        woyouService.printTextWithFont("Origem:", "", 18,null);
                                        woyouService.printTextWithFont(sOrigem, "", 24,null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("Destino:", "", 18,null);
                                        woyouService.printTextWithFont(sDestino, "", 24,null);
                                        woyouService.lineWrap(1, null);

                                        woyouService.printTextWithFont("Data:", "", 18,null);
                                        woyouService.printTextWithFont(sdEmb, "", 24,null);
                                        woyouService.printTextWithFont(" | Horário:", "", 18,null);
                                        woyouService.printTextWithFont(sHora, "", 24,null);
                                        woyouService.lineWrap(1, null);


                                        NodeList nodepassageiro = doc.getElementsByTagName("infPassageiro");
                                        for (int temppessoa = 0; temppessoa < nodepassageiro.getLength(); temppessoa++) {
                                            Node npessoa = nodepassageiro.item(temppessoa);
                                            if (npessoa.getNodeType() == Node.ELEMENT_NODE) {
                                                Element pessoaElement = (Element) npessoa;

                                                sPassageiro = pessoaElement.getElementsByTagName("xNome").item(0).getTextContent();
                                                sDocpas = pessoaElement.getElementsByTagName("nDoc").item(0).getTextContent();
                                                NodeList tagCPF = doc.getElementsByTagName("CPF");
                                                for (int icpf = 0; icpf < tagCPF.getLength(); icpf++){
                                                    Node ncpf = tagCPF.item(icpf);
                                                    if (ncpf.getNodeType() == Node.ELEMENT_NODE) {
                                                        sCPF = pessoaElement.getElementsByTagName("CPF").item(0).getTextContent();
                                                    }
                                                }

                                            }
                                        }


                                    }

                                    //Dados da Viagem
                                    NodeList nodeviagem = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem.getLength(); tempvia++){
                                        Node nviagem = nodeviagem.item(tempvia);
                                        Element viaElement = (Element) nviagem;
                                        NodeList ncad = doc.getElementsByTagName("poltrona");
                                        for (int tempcad = 0; tempcad < ncad.getLength(); tempcad++) {
                                            String scad = viaElement.getElementsByTagName("poltrona").item(0).getTextContent();
                                            woyouService.printTextWithFont("Poltrona:  ", "", 22, null);
                                            woyouService.printTextWithFont(scad, "", 24, null);
                                            woyouService.lineWrap(1, null);
                                        }

                                        String sprefix = viaElement.getElementsByTagName("prefixo").item(0).getTextContent();
                                        String slinha = viaElement.getElementsByTagName("xPercurso").item(0).getTextContent();
                                        String stpserv = viaElement.getElementsByTagName("tpServ").item(0).getTextContent();
                                        ///Tipo de serviço
                                        String tiposerv = "";
                                        if (stpserv.equals("1")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("2")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("3")){ tiposerv = "Semileito";}
                                        if (stpserv.equals("4")){ tiposerv = "Leito Com AR";}
                                        if (stpserv.equals("5")){ tiposerv = "Leito Sem AR";}
                                        if (stpserv.equals("6")){ tiposerv = "Executivo";}

                                        woyouService.printTextWithFont("Prefixo: ", "", 18, null);
                                        woyouService.printTextWithFont(sprefix, "", 20, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("Linha:", "", 18, null);
                                        woyouService.printTextWithFont(slinha, "", 24, null);
                                        woyouService.lineWrap(1, null);

                                        woyouService.printTextWithFont("  TIPO: ", "", 18, null);
                                        woyouService.printTextWithFont(tiposerv, "", 24, null);

                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("--------------------------------", "", 24,null);
                                        //woyouService.printTextWithFont("________________________________", "", 12,null);
                                        woyouService.lineWrap(1, null);
                                    }

                                    //falores que compoem o bilhete
                                    NodeList nodeinfvalor = doc.getElementsByTagName("infValorBPe");
                                    for (int tempvalor = 0; tempvalor < nodeinfvalor.getLength(); tempvalor++){
                                        Node nvalor = nodeinfvalor.item(tempvalor);
                                        Element valorElement = (Element)  nvalor;
                                        String svalBPe = valorElement.getElementsByTagName("vBP").item(tempvalor).getTextContent();
                                        String sdesconto = valorElement.getElementsByTagName("vDesconto").item(tempvalor).getTextContent();
                                        String valorpgto = valorElement.getElementsByTagName("vPgto").item(tempvalor).getTextContent();
                                        if (!sdesconto.equals("0.00")) {
                                            sxdesconto = valorElement.getElementsByTagName("xDesconto").item(tempvalor).getTextContent();
                                        }

                                        NodeList nodecomp = doc.getElementsByTagName("Comp"); //composicao do valor
                                        for (int tempcomp = 0; tempcomp < nodecomp.getLength(); tempcomp++){
                                            Node ncomp = nodecomp.item(tempcomp);
                                            if (ncomp.getNodeType() == Node.ELEMENT_NODE) {
                                                Element compElement = (Element) ncomp;


                                                String tpcomp = compElement.getElementsByTagName("tpComp").item(0).getTextContent();
                                                String valcomp = compElement.getElementsByTagName("vComp").item(0).getTextContent();
                                                String stipo = "";
                                                String sespaco = "";
                                                if (tpcomp.equals("01")) { stipo = "Tarifa";}
                                                if (tpcomp.equals("02")) { stipo = "Pedágio";}
                                                if (tpcomp.equals("03")) { stipo = "Tx Embarque";}
                                                if (tpcomp.equals("04")) { stipo = "Seguro";}
                                                if (tpcomp.equals("99")) { stipo = "Outros";}
                                                String sval = stipo+valcomp;
                                                Integer iqtd = sval.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32-iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }

                                                woyouService.setAlignment(0, callback); //Alinhamento Esquerda
                                                woyouService.printTextWithFont(stipo + sespaco, "", 24, null);
                                                woyouService.printTextWithFont(valcomp, "", 24, null);
                                                woyouService.lineWrap(1, null);
                                            }





                                        }
                                        String sespaco = "";
                                        String sval = "Valor Total R$"+svalBPe;
                                        Integer iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda
                                        woyouService.printTextWithFont("Valor Total R$" + sespaco, "", 24, null);
                                        //woyouService.setAlignment (2, callback); //Alinhamento Direita
                                        woyouService.printTextWithFont(svalBPe, "", 24, null);
                                        woyouService.lineWrap(1, null);

                                        sespaco = "";
                                        sval = "Desconto R$"+sdesconto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }

                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda
                                        woyouService.printTextWithFont("Desconto R$" + sespaco, "", 24, null);
                                        woyouService.printTextWithFont(sdesconto, "", 24, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda

                                        sespaco = "";
                                        sval = "Valor a Pagar R$"+valorpgto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }

                                        woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                                        woyouService.printTextWithFont("Valor a Pagar R$" + sespaco, "", 24, null);
                                        //woyouService.setAlignment (2, callback); //Alinhamento Direita
                                        woyouService.printTextWithFont(valorpgto, "", 24, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda

                                        sespaco = "";
                                        sval = "Dinheiro"+valorpgto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }


                                        woyouService.printTextWithFont("FORMA DE PAGAMENTO    VALOR PAGO", "", 24, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda
                                        woyouService.printTextWithFont("Dinheiro" + sespaco, "", 24, null);
                                        woyouService.printTextWithFont(valorpgto, "", 24, null);
                                        woyouService.lineWrap(2, null);
                                        woyouService.setAlignment (0, callback); //Alinhamento Esquerda

                                    }

                                    //falores dos impostos
                                        NodeList nodeimp = doc.getElementsByTagName("imp");
                                            for (int tempimp = 0; tempimp < nodeimp.getLength(); tempimp++) {
                                                Node nimp = nodeimp.item(tempimp);
                                                Element impElement = (Element) nimp;
                                                NodeList ntrib = doc.getElementsByTagName("vTotTrib");
                                                for (int temptrib = 0; temptrib < ntrib.getLength(); temptrib++) {
                                                    stottrib = impElement.getElementsByTagName("vTotTrib").item(0).getTextContent();

                                                }
                                        }
                                    ////Dados do BP-e
                                    woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x0}, callback); // Desativar Fonte em Negrito
                                    woyouService.setAlignment (1, callback); //Alinhamento Centralizado
                                    woyouService.printTextWithFont("Consulte pela Chave de Acesso em:", "", 22, null);
                                    woyouService.lineWrap(1, null);
                                    woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                                    woyouService.printTextWithFont(dbemp.Busca_Dados_Emp(1, "Urlqrc"), "", 22, null);
                                    woyouService.lineWrap(1, null);

                                    //Element infbpeElement = (Element) nodeinfbpe;
                                    String sId = infElement.getAttribute("Id");
                                    String sChave = sId.substring(3, 47);
                                    woyouService.printTextWithFont(sChave, "", 16, null);
                                    woyouService.lineWrap(1, null);

                                    if (!sPassageiro.equals("")){
                                        woyouService.printTextWithFont("PASSAGEIRO DOC: "+sDocpas, "", 22, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont(sPassageiro, "", 22, null);
                                        woyouService.lineWrap(1, null);
                                        if (!sCPF.equals("")) {
                                            woyouService.printTextWithFont("CPF: "+sCPF, "", 22, null);
                                            woyouService.lineWrap(1, null);
                                        }
                                    } else if (sPassageiro.equals("")) {
                                        woyouService.printTextWithFont("PASSAGEIRO NÃO IDENTIFICADO", "", 22, null);
                                        woyouService.lineWrap(1, null);
                                    }

                                    if (!sxdesconto.equals("")) {
                                        woyouService.printTextWithFont("Tipo de Desconto: "+sxdesconto, "", 20, null);
                                        woyouService.lineWrap(1, null);
                                    }
                                     //identificacao do bilhete
                                        sNROBPE = (("000000000" + sNROBPE).substring(sNROBPE.length()));
                                        sSERBPE = (("000" + sSERBPE).substring(sSERBPE.length()));

                                        woyouService.printTextWithFont("BP-e nº ", "", 24,null);
                                        woyouService.printTextWithFont((sNROBPE)+"    ", "", 24,null);
                                        woyouService.printTextWithFont("Série ", "", 24,null);
                                        woyouService.printTextWithFont((sSERBPE)+"", "", 24,null);
                                        woyouService.lineWrap(1, null);

                                        String sanoe = sdatemi.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smese = sdatemi.substring(5, (7));
                                        String sdiae = sdatemi.substring(8, (10));
                                        String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                                        String sHorae = sdatemi.substring(11, (19));

                                        woyouService.printTextWithFont(sdEmi+"     ", "", 24,null);
                                        woyouService.printTextWithFont(sHorae, "", 24,null);
                                        woyouService.lineWrap(1, null);






                                }
                                //Informacoes suplementares
                                NodeList nodeinfCpl = doc.getElementsByTagName("infBPeSupl");
                                for (int tempinfsub = 0; tempinfsub < nodeinfCpl.getLength(); tempinfsub++) {


                                    Node ninfsub = nodeinfCpl.item(tempinfsub);
                                    Element infsubElement = (Element) ninfsub;
                                    sQRcode = infsubElement.getElementsByTagName("qrCodBPe").item(0).getTextContent();

                                }







                            }

                            /////Dados de Autorização.
                            if (stpEmis.equals("2")){ //Contingencia
                                woyouService.printTextWithFont("EMITIDO EM CONTINGÊNCIA", "", 24,null);
                                woyouService.lineWrap(1, null);
                                woyouService.printTextWithFont("Pendente de Autorização", "", 20,null);
                                woyouService.lineWrap(2, null);
                            } else if (stpEmis.equals("1")) { //Emissao Normal
                                NodeList nodeprotbpe = doc.getElementsByTagName("protBPe");
                                for (int tempprot = 0; tempprot < nodeprotbpe.getLength(); tempprot++) {
                                    Node nprotbpe = nodeprotbpe.item(tempprot);
                                    Element protElement = (Element) nprotbpe;
                                    NodeList nodeinfprot = doc.getElementsByTagName("infProt");
                                    for (int tempinfprot = 0; tempinfprot < nodeinfprot.getLength(); tempinfprot++) {
                                        Node ninfprot = nodeinfprot.item(tempinfprot);
                                        Element infprotElement = (Element) ninfprot;
                                        String sprot = infprotElement.getElementsByTagName("nProt").item(0).getTextContent();
                                        String sdataut = infprotElement.getElementsByTagName("dhRecbto").item(0).getTextContent();
                                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x0}, callback); // Desativar Fonte em Negrito
                                        woyouService.printTextWithFont("Protocolo de Autorização: ", "", 18, null);
                                        woyouService.printTextWithFont(sprot, "", 20, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.printTextWithFont("Data de Autorização: ", "", 18, null);
                                        String sanoA = sdataut.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smesA = sdataut.substring(5, (7));
                                        String sdiaA = sdataut.substring(8, (10));
                                        String sdAut = sdiaA + "/" + smesA + "/" + sanoA;
                                        String sHoraA = sdataut.substring(11, (19));
                                        woyouService.printTextWithFont(sdAut + " ", "", 20, null);
                                        woyouService.printTextWithFont(sHoraA, "", 20, null);
                                        woyouService.lineWrap(2, null);
                                    }
                                }
                            }



                            if (stpEmis.equals("2")) {
                                woyouService.printQRCode(sQRcode, 3, 1, callback);
                            } else if (stpEmis.equals("1")){
                                woyouService.printQRCode(sQRcode, 5, 1, callback);
                            }
                            woyouService.lineWrap(1, null);

                            if (stottrib != ""){
                                stottrib = stottrib.replace(".", ",");
                                String sobs = "Informação dos Tributos Totais Incidentes (Lei Federal 12.741/2012): R$ " + stottrib;
                                woyouService.sendRAWData(new byte [] {0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                                woyouService.printTextWithFont(sobs, "", 18, null);
                            }
                            if (sespacos.equals("xx")) {
                                woyouService.lineWrap(4, null);
                            } else {
                                int iesp = Integer.parseInt(sespacos);
                                woyouService.lineWrap(iesp, null);
                            }


                        }


                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("Erro Imp01: "+e.toString());
                    }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("Erro Imp02: "+e.toString());
            }

        }});


    }



    public void Imprime_BPe_Lio(){
       /* if( mBitmap == null ){
           // mBitmap = BitmapFactory.decodeFile(getExternalStorageDirectory().getAbsolutePath() + "/Android/Data/NOVOS/logoemp.jpg");
            //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
        }

                    try {
                        String sNROBPE = "";
                        String sSERBPE = "";
                        String stpEmis = "";
                        String sdatemi = "";
                        String sQRcode = "";
                        String stottrib = "";
                        String sPassageiro = "";
                        String sDocpas = "";
                        String sCPF = "";
                        String stipamb = "";
                        String sxdesconto = "";
                        String chaveBPe = Guarda_Texto;
                        System.out.println("chaveBPe: "+chaveBPe);
                        stpEmis = chaveBPe.substring(34, (35)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        //Toast.makeText(ViaActivity.this, "Tipo: "+stpEmis, Toast.LENGTH_LONG).show();
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        String sextencao = "";
                        if (stpEmis.equals("1")){
                            sextencao = "-procBPe.xml";
                        }
                        else if (stpEmis.equals("2")) {
                            sextencao = "-bpe.xml";
                        }
                        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        //Document doc = dBuilder.parse(getAssets().open("Arquivo.xml"));
                        Document doc = dBuilder.parse(fXmlFile);

                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");
                        PrinterManager printerManagerT = new PrinterManager(ViaActivity.this);

                        List<Map<String, Integer>> stylesMap =  new ArrayList<>();
                        stylesMap.add(font_L126);
                        stylesMap.add(font_R126);


                        doc.getDocumentElement().normalize();
                        NodeList nodeProc = null;
                        //NodeList nodebpe = null;
                        if (stpEmis.equals("1")){
                            nodeProc = doc.getElementsByTagName("bpeProc");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        } else if (stpEmis.equals("2")) {
                            nodeProc = doc.getElementsByTagName("BPe");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        }
                        int counter = nodeProc.getLength();
                        for (int temp = 0; temp < nodeProc.getLength(); temp++) {

                            Node nNode = nodeProc.item(temp);

                            // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;

                                NodeList nodeinfbpe = doc.getElementsByTagName("infBPe");
                                for (int tempinf = 0; tempinf < nodeinfbpe.getLength(); tempinf++) {


                                    Node ninf = nodeinfbpe.item(tempinf);
                                    Element infElement = (Element) ninf;
                                    //////////////////////////////////////////////
                                    NodeList nodeide = doc.getElementsByTagName("ide");
                                    for (int tempide = 0; tempide < nodeide.getLength(); tempide++){
                                        Node nide = nodeide.item(tempide);
                                        Element ideElement = (Element) nide;
                                        sNROBPE = ideElement.getElementsByTagName("nBP").item(0).getTextContent();
                                        sSERBPE = ideElement.getElementsByTagName("serie").item(0).getTextContent();
                                        sdatemi = ideElement.getElementsByTagName("dhEmi").item(0).getTextContent();
                                        stipamb = ideElement.getElementsByTagName("tpAmb").item(0).getTextContent();
                                        int inumero = Integer.parseInt(sNROBPE);
                                        DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                                        String sIDBpe = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "ID");
                                        String sqtdimp = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "Qtdimp");
                                        int iqtdatu = (Integer.parseInt(sqtdimp)+1);
                                        dbbpe.Atualizar_Campo_Bpe(sIDBpe, "Qtdimp", Integer.toString(iqtdatu));
                                    }

                                    //////////////////////////////////////////////

                                    //printerManagerT.printImage(mBitmap, alignCenter, printerListener);
                                    //printerManagerT.printText("   ", alignLeft, printerListener);

                                    printerManagerT.printText(dbemp.Busca_Dados_Emp(1,"Descri"), font_C126, printerListener);
                                    //printerManagerT.printText("   ", alignLeft, printerListener);

                                    String scnpj = dbemp.Busca_Dados_Emp(1,"Cnpj");
                                    String sIe = dbemp.Busca_Dados_Emp(1,"Insest");


                                    printerManagerT.printText("CNPJ: "+scnpj+" IE: "+sIe, font_C220, printerListener);

                                    String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                                    sendere = dbemp.Busca_Dados_Emp(1,"Endere");
                                    snum = dbemp.Busca_Dados_Emp(1, "Numero");
                                    sbai = dbemp.Busca_Dados_Emp(1,"Bairro");
                                    scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                                    suf = dbemp.Busca_Dados_Emp(1, "UF");
                                    sendp1 = sendere+", "+snum;
                                    sendp2 = sbai+", "+scid+"-"+suf;

                                    printerManagerT.printText(sendp1, font_C118, printerListener);
                                    printerManagerT.printText(sendp2, font_C118, printerListener);

                                    printerManagerT.printText("Documento Auxiliar do Bilhete", font_C224, printerListener);
                                    printerManagerT.printText("de Passagem Eletrônico", font_C224, printerListener);

                                    if (stpEmis.equals("2")){
                                        printerManagerT.printText("EMITIDO EM CONTINGÊNCIA", font_C128, printerListener);
                                        printerManagerT.printText("Pendente de Autorização", font_C124, printerListener);
                                    }
                                    if (stipamb.equals("2")) { //Homologacao
                                        printerManagerT.printText("--------------------------------", font_C226, printerListener);
                                        printerManagerT.printText("EMITIDO EM HOMOLOGAÇÃO", font_C128, printerListener);
                                        printerManagerT.printText("SEM VALOR FISCAL", font_C128, printerListener);
                                    }

                                    printerManagerT.printText("--------------------------------", font_C226, printerListener);


                                    ///////////
                                    //Dados da Viagem
                                    String sdatemb = "";
                                    NodeList nodeviagem1 = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem1.getLength(); tempvia++){
                                        Node nviagem1 = nodeviagem1.item(tempvia);
                                        Element viaElement1 = (Element) nviagem1;
                                        sdatemb = viaElement1.getElementsByTagName("dhViagem").item(0).getTextContent();

                                    }

                                    NodeList nodepassagem = doc.getElementsByTagName("infPassagem");
                                    for (int temppass = 0; temppass < nodepassagem.getLength(); temppass++){
                                        Node npass = nodepassagem.item(temppass);
                                        Element passElement = (Element) npass;
                                        DB_TRE dbtre = new DB_TRE(getApplicationContext());
                                        String cOri = passElement.getElementsByTagName("cLocOrig").item(0).getTextContent();
                                        String xOri = passElement.getElementsByTagName("xLocOrig").item(0).getTextContent();
                                        String ufOri = dbtre.Busca_Dados_Tre_Codigo(cOri, "UF");
                                        String sOrigem = xOri+" ("+ufOri+")";
                                        String cDes = passElement.getElementsByTagName("cLocDest").item(0).getTextContent();
                                        String xDes = passElement.getElementsByTagName("xLocDest").item(0).getTextContent();
                                        String ufDes = dbtre.Busca_Dados_Tre_Codigo(cDes, "UF");
                                        String sDestino = xDes+" ("+ufDes+")";
                                        //String sdatemb = passElement.getElementsByTagName("dhEmb").item(0).getTextContent();
                                        String sano = sdatemb.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smes = sdatemb.substring(5, (7));
                                        String sdia = sdatemb.substring(8, (10));
                                        String sdEmb = sdia+"/"+smes+"/"+sano;
                                        String sHora = sdatemb.substring(11, (16));

                                        printerManagerT.printText("Origem: "+sOrigem, font_C220, printerListener);
                                        printerManagerT.printText("Destino: "+sDestino, font_C220, printerListener);
                                        printerManagerT.printText("Data: "+sdEmb+" | Horário:"+sHora, font_C224, printerListener);



                                        NodeList nodepassageiro = doc.getElementsByTagName("infPassageiro");
                                        for (int temppessoa = 0; temppessoa < nodepassageiro.getLength(); temppessoa++) {
                                            Node npessoa = nodepassageiro.item(temppessoa);
                                            if (npessoa.getNodeType() == Node.ELEMENT_NODE) {
                                                Element pessoaElement = (Element) npessoa;

                                                sPassageiro = pessoaElement.getElementsByTagName("xNome").item(0).getTextContent();
                                                sDocpas = pessoaElement.getElementsByTagName("nDoc").item(0).getTextContent();
                                                NodeList tagCPF = doc.getElementsByTagName("CPF");
                                                for (int icpf = 0; icpf < tagCPF.getLength(); icpf++){
                                                    Node ncpf = tagCPF.item(icpf);
                                                    if (ncpf.getNodeType() == Node.ELEMENT_NODE) {
                                                        sCPF = pessoaElement.getElementsByTagName("CPF").item(0).getTextContent();
                                                    }
                                                }

                                            }
                                        }


                                    }

                                    //Dados da Viagem
                                    NodeList nodeviagem = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem.getLength(); tempvia++){
                                        Node nviagem = nodeviagem.item(tempvia);
                                        Element viaElement = (Element) nviagem;
                                        NodeList ncad = doc.getElementsByTagName("poltrona");
                                        for (int tempcad = 0; tempcad < ncad.getLength(); tempcad++) {
                                            String scad = viaElement.getElementsByTagName("poltrona").item(0).getTextContent();
                                            printerManagerT.printText("Poltrona: "+scad, font_C222, printerListener);
                                         }

                                        String sprefix = viaElement.getElementsByTagName("prefixo").item(0).getTextContent();
                                        String slinha = viaElement.getElementsByTagName("xPercurso").item(0).getTextContent();
                                        String stpserv = viaElement.getElementsByTagName("tpServ").item(0).getTextContent();
                                        ///Tipo de serviço
                                        String tiposerv = "";
                                        if (stpserv.equals("1")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("2")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("3")){ tiposerv = "Semileito";}
                                        if (stpserv.equals("4")){ tiposerv = "Leito Com AR";}
                                        if (stpserv.equals("5")){ tiposerv = "Leito Sem AR";}
                                        if (stpserv.equals("6")){ tiposerv = "Executivo";}

                                        printerManagerT.printText("Prefixo: "+sprefix, font_C122, printerListener);
                                        printerManagerT.printText("Linha: "+slinha, font_C122, printerListener);
                                        printerManagerT.printText("TIPO: "+tiposerv, font_C222, printerListener);
                                        printerManagerT.printText("---------------------------------------------", font_C220, printerListener);

                                    }

                                    //falores que compoem o bilhete
                                    String valorpgto = "";
                                    NodeList nodeinfvalor = doc.getElementsByTagName("infValorBPe");
                                    for (int tempvalor = 0; tempvalor < nodeinfvalor.getLength(); tempvalor++){
                                        Node nvalor = nodeinfvalor.item(tempvalor);
                                        Element valorElement = (Element)  nvalor;
                                        String svalBPe = valorElement.getElementsByTagName("vBP").item(tempvalor).getTextContent();
                                        String sdesconto = valorElement.getElementsByTagName("vDesconto").item(tempvalor).getTextContent();
                                        valorpgto = valorElement.getElementsByTagName("vPgto").item(tempvalor).getTextContent();
                                        if (!sdesconto.equals("0.00")) {
                                            sxdesconto = valorElement.getElementsByTagName("xDesconto").item(tempvalor).getTextContent();
                                        }

                                        NodeList nodecomp = doc.getElementsByTagName("Comp"); //composicao do valor
                                        for (int tempcomp = 0; tempcomp < nodecomp.getLength(); tempcomp++){
                                            Node ncomp = nodecomp.item(tempcomp);
                                            if (ncomp.getNodeType() == Node.ELEMENT_NODE) {
                                                Element compElement = (Element) ncomp;


                                                String tpcomp = compElement.getElementsByTagName("tpComp").item(0).getTextContent();
                                                String valcomp = compElement.getElementsByTagName("vComp").item(0).getTextContent();
                                                String stipo = "";
                                                String sespaco = "";
                                                if (tpcomp.equals("01")) { stipo = "Tarifa";}
                                                if (tpcomp.equals("02")) { stipo = "Pedágio";}
                                                if (tpcomp.equals("03")) { stipo = "Tx Embarque";}
                                                if (tpcomp.equals("04")) { stipo = "Seguro";}
                                                if (tpcomp.equals("99")) { stipo = "Outros";}
                                                String sval = stipo+valcomp;
                                                Integer iqtd = sval.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32-iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }

                                                String[] textsToPrint2 = new String[] { stipo,  valcomp  };
                                                List<Map<String, Integer>> stylesMap2 =  new ArrayList<>();
                                                stylesMap2.add(font_L120);
                                                stylesMap2.add(font_R120);

                                                printerManagerT.printMultipleColumnText(textsToPrint2, stylesMap2, printerListener);



                                            }





                                        }
                                        String sespaco = "";
                                        String sval = "Valor Total R$"+svalBPe;
                                        Integer iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }

                                        String[] textsToPrint3 = new String[] { "Valor Total R$",  svalBPe  };
                                        List<Map<String, Integer>> stylesMap3 =  new ArrayList<>();
                                        stylesMap3.add(font_L120);
                                        stylesMap3.add(font_R120);
                                        printerManagerT.printMultipleColumnText(textsToPrint3, stylesMap3, printerListener);


                                        sespaco = "";
                                        sval = "Desconto R$"+sdesconto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }


                                        String[] textsToPrint4 = new String[] { "Desconto R$",  sdesconto  };
                                        printerManagerT.printMultipleColumnText(textsToPrint4, stylesMap3, printerListener);//printerManagerT.printText("   ", alignLeft, printerListener);


                                        sespaco = "";
                                        sval = "Valor a Pagar R$"+valorpgto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }

                                        String[] textsToPrint5 = new String[] { "Valor a Pagar R$",  valorpgto  };
                                        printerManagerT.printMultipleColumnText(textsToPrint5, stylesMap3, printerListener);//printerManagerT.printText("   ", alignLeft, printerListener);



                                    }


                                    //FORMAS DE PAGAMENTO
                                    NodeList nodepag = doc.getElementsByTagName("pag");
                                    for (int temppag = 0; temppag < nodepag.getLength(); temppag++) {
                                        Node npag = nodepag.item(temppag);
                                        Element pagElement = (Element) npag;
                                        String stPag = pagElement.getElementsByTagName("tPag").item(0).getTextContent();
                                        String sforma = "";
                                        if (stPag.equals("01")) { sforma = "DINHEIRO";}
                                        else if (stPag.equals("03")) { sforma = "CARTAO CREDITO";}
                                        else if (stPag.equals("04")) { sforma = "CARTAO DEBITO";}
                                        else if (stPag.equals("05")) { sforma = "VALE TRANSPORTE";}
                                        else { sforma = "OUTROS";}

                                        String sespaco = "";
                                        String sval = sforma+valorpgto;
                                        int iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }

                                        List<Map<String, Integer>> stylesMap4 =  new ArrayList<>();
                                        stylesMap4.add(font_L120);
                                        stylesMap4.add(font_C120);
                                        stylesMap4.add(font_R120);
                                        String[] textsToPrint6 = new String[] { "FORMA DE", "PAGAMENTO",  "VALOR PAGO"  };
                                        printerManagerT.printMultipleColumnText(textsToPrint6, stylesMap4, printerListener);//printerManagerT.printText("   ", alignLeft, printerListener);
                                        String[] textsToPrint7 = new String[] { sforma, "", valorpgto  };
                                        printerManagerT.printMultipleColumnText(textsToPrint7, stylesMap4, printerListener);//printerManagerT.printText("   ", alignLeft, printerListener);
                                        printerManagerT.printText(" ", font_C126, printerListener);
                                    }
                                    ////////////





                                    //falores dos impostos
                                    NodeList nodeimp = doc.getElementsByTagName("imp");
                                    for (int tempimp = 0; tempimp < nodeimp.getLength(); tempimp++) {
                                        Node nimp = nodeimp.item(tempimp);
                                        Element impElement = (Element) nimp;
                                        NodeList ntrib = doc.getElementsByTagName("vTotTrib");
                                        for (int temptrib = 0; temptrib < ntrib.getLength(); temptrib++) {
                                            stottrib = impElement.getElementsByTagName("vTotTrib").item(0).getTextContent();

                                        }
                                    }
                                    ////Dados do BP-e
                                    printerManagerT.printText("Consulte pela Chave de Acesso em:", font_C122, printerListener);
                                    printerManagerT.printText(dbemp.Busca_Dados_Emp(1, "Urlqrc"), font_C116, printerListener);

                                    //Element infbpeElement = (Element) nodeinfbpe;
                                    String sId = infElement.getAttribute("Id");
                                    String sChave = sId.substring(3, 47);
                                    setStyles(18, 1);
                                    printerManagerT.printText(sChave, font_C116, printerListener);
                                    //printerManagerT.printText("   ", alignLeft, printerListener);


                                    if (!sPassageiro.equals("")){
                                        printerManagerT.printText("PASSAGEIRO DOC: "+sDocpas, font_C124, printerListener);
                                        printerManagerT.printText(sPassageiro, font_C224, printerListener);


                                        if (!sCPF.equals("")) {
                                            printerManagerT.printText("CPF: "+sCPF, font_C122, printerListener);
                                        }
                                    } else if (sPassageiro.equals("")) {
                                        printerManagerT.printText("PASSAGEIRO NÃO IDENTIFICADO", font_C122, printerListener);;
                                    }

                                    if (!sxdesconto.equals("")) {
                                        printerManagerT.printText("Tipo de Desconto: "+sxdesconto, font_C122, printerListener);
                                    }
                                    //identificacao do bilhete
                                    sNROBPE = (("000000000" + sNROBPE).substring(sNROBPE.length()));
                                    sSERBPE = (("000" + sSERBPE).substring(sSERBPE.length()));

                                    setStyles(26, 3);
                                    printerManagerT.printText("BP-e nº "+sNROBPE+"    "+"Série "+sSERBPE, font_C126, printerListener);
                                    //printerManagerT.printText("   ", alignLeft, printerListener);


                                    String sanoe = sdatemi.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                    String smese = sdatemi.substring(5, (7));
                                    String sdiae = sdatemi.substring(8, (10));
                                    String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                                    String sHorae = sdatemi.substring(11, (19));

                                    printerManagerT.printText(sdEmi+"     "+sHorae, alignCenter, printerListener);
                                    //printerManagerT.printText("   ", alignLeft, printerListener);



                                }
                                //Informacoes suplementares
                                NodeList nodeinfCpl = doc.getElementsByTagName("infBPeSupl");
                                for (int tempinfsub = 0; tempinfsub < nodeinfCpl.getLength(); tempinfsub++) {


                                    Node ninfsub = nodeinfCpl.item(tempinfsub);
                                    Element infsubElement = (Element) ninfsub;
                                    sQRcode = infsubElement.getElementsByTagName("qrCodBPe").item(0).getTextContent();

                                }







                            }

                            /////Dados de Autorização.
                            if (stpEmis.equals("2")){ //Contingencia
                                printerManagerT.printText("EMITIDO EM CONTINGÊNCIA", font_C128, printerListener);
                                printerManagerT.printText("Pendente de Autorização", font_C124, printerListener);
                                printerManagerT.printText("   ", font_L124, printerListener);
                            } else if (stpEmis.equals("1")) { //Emissao Normal
                                NodeList nodeprotbpe = doc.getElementsByTagName("protBPe");
                                for (int tempprot = 0; tempprot < nodeprotbpe.getLength(); tempprot++) {
                                    Node nprotbpe = nodeprotbpe.item(tempprot);
                                    Element protElement = (Element) nprotbpe;
                                    NodeList nodeinfprot = doc.getElementsByTagName("infProt");
                                    for (int tempinfprot = 0; tempinfprot < nodeinfprot.getLength(); tempinfprot++) {
                                        Node ninfprot = nodeinfprot.item(tempinfprot);
                                        Element infprotElement = (Element) ninfprot;
                                        String sprot = infprotElement.getElementsByTagName("nProt").item(0).getTextContent();
                                        String sdataut = infprotElement.getElementsByTagName("dhRecbto").item(0).getTextContent();
                                        printerManagerT.printText("Protocolo de Autorização: "+sprot, font_C116, printerListener);


                                        String sanoA = sdataut.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smesA = sdataut.substring(5, (7));
                                        String sdiaA = sdataut.substring(8, (10));
                                        String sdAut = sdiaA + "/" + smesA + "/" + sanoA;
                                        String sHoraA = sdataut.substring(11, (19));
                                        printerManagerT.printText("Data de Autorização: "+sdAut + " "+sHoraA, font_C120, printerListener);
                                        printerManagerT.printText("   ", font_L124, printerListener);

                                    }
                                }
                            }

                            setStyles(20, 0);

                            if (stpEmis.equals("2")) {
                                printerManagerT.printQrCode(sQRcode, PrinterAttributes.VAL_ALIGN_CENTER, 300, printerListener);
                            } else if (stpEmis.equals("1")){
                                printerManagerT.printQrCode(sQRcode, PrinterAttributes.VAL_ALIGN_CENTER, 250, printerListener);
                            }
                            printerManagerT.printText("   ", font_L124, printerListener);

                            if (stottrib != ""){
                                stottrib = stottrib.replace(".", ",");
                                String sobs = "Informação dos Tributos Totais Incidentes (Lei Federal 12.741/2012): R$ " + stottrib;
                                printerManagerT.printText(sobs, font_C120, printerListener);
                                printerManagerT.printText("   ", font_L124, printerListener);

                            }
                            if (sespacos.equals("xx")) {
                                printerManagerT.printText("   \n\n\n", font_L124, printerListener);
                            } else {
                                int iesp = Integer.parseInt(sespacos);
                                String slinhas = "";
                                for( int i=0; i <iesp; i++)
                                {
                                    slinhas=slinhas+"\n";


                                }
                                printerManagerT.printText(slinhas, font_L124, printerListener);
                            }


                        }

                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("Erro Imp01: "+e.toString());
                    }*/




    }

    ////IMPRESSAO BLUETOOTH

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
    public void Imprime_BPe_BT(){
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
                        String sNROBPE = "";
                        String sSERBPE = "";
                        String stpEmis = "";
                        String sdatemi = "";
                        String sQRcode = "";
                        String stottrib = "";
                        String sPassageiro = "";
                        String sDocpas = "";
                        String sCPF = "";
                        String stipamb = "";
                        String sxdesconto = "";
                        String chaveBPe = Guarda_Texto;
                        System.out.println("chaveBPe: "+chaveBPe);
                        stpEmis = chaveBPe.substring(34, (35)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        //Toast.makeText(ViaActivity.this, "Tipo: "+stpEmis, Toast.LENGTH_LONG).show();
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        String sextencao = "";
                        if (stpEmis.equals("1")){
                            sextencao = "-procBPe.xml";
                        }
                        else if (stpEmis.equals("2")) {
                            sextencao = "-bpe.xml";
                        }
                        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        //Document doc = dBuilder.parse(getAssets().open("Arquivo.xml"));
                        System.out.println("Erro ao Ler XML : "+getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        Document doc = dBuilder.parse(fXmlFile);

                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        String sespacos = dbemp.Busca_Dados_Emp(1,"Rsv003");
                        iniciarImpressora(impressora.getOutputStream());
                        OutputStream out = impressora.getOutputStream();

                        doc.getDocumentElement().normalize();
                        NodeList nodeProc = null;
                        //NodeList nodebpe = null;
                        if (stpEmis.equals("1")){
                            nodeProc = doc.getElementsByTagName("bpeProc");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        } else if (stpEmis.equals("2")) {
                            nodeProc = doc.getElementsByTagName("BPe");
                            //nodebpe = doc.getElementsByTagName("BPe");
                        }
                        int counter = nodeProc.getLength();
                        for (int temp = 0; temp < nodeProc.getLength(); temp++) {

                            Node nNode = nodeProc.item(temp);

                            // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;

                                NodeList nodeinfbpe = doc.getElementsByTagName("infBPe");
                                for (int tempinf = 0; tempinf < nodeinfbpe.getLength(); tempinf++) {


                                    Node ninf = nodeinfbpe.item(tempinf);
                                    Element infElement = (Element) ninf;
                                    //////////////////////////////////////////////
                                    NodeList nodeide = doc.getElementsByTagName("ide");
                                    for (int tempide = 0; tempide < nodeide.getLength(); tempide++){
                                        Node nide = nodeide.item(tempide);
                                        Element ideElement = (Element) nide;
                                        sNROBPE = ideElement.getElementsByTagName("nBP").item(0).getTextContent();
                                        sSERBPE = ideElement.getElementsByTagName("serie").item(0).getTextContent();
                                        sdatemi = ideElement.getElementsByTagName("dhEmi").item(0).getTextContent();
                                        stipamb = ideElement.getElementsByTagName("tpAmb").item(0).getTextContent();
                                        int inumero = Integer.parseInt(sNROBPE);
                                        DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                                        String sIDBpe = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "ID");
                                        String sqtdimp = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "Qtdimp");
                                        int iqtdatu = (Integer.parseInt(sqtdimp)+1);
                                        dbbpe.Atualizar_Campo_Bpe(sIDBpe, "Qtdimp", Integer.toString(iqtdatu));
                                    }

                                    //////////////////////////////////////////////


                                    out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                    out.write(EscPosBase.alignCenter()); //Centralizado


                                    out.write(dbemp.Busca_Dados_Emp(1,"Descri").getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito

                                    String scnpj = dbemp.Busca_Dados_Emp(1,"Cnpj");
                                    String sIe = dbemp.Busca_Dados_Emp(1,"Insest");

                                    String sinscricao = ("CNPJ: "+scnpj);
                                    out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito
                                    out.write((EscPosBase.getFontsmall()));
                                    out.write(sinscricao.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    sinscricao = ("IE: "+sIe);
                                    out.write(sinscricao.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());


                                    String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                                    sendere = dbemp.Busca_Dados_Emp(1,"Endere");
                                    snum = dbemp.Busca_Dados_Emp(1, "Numero");
                                    sbai = dbemp.Busca_Dados_Emp(1,"Bairro");
                                    scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                                    suf = dbemp.Busca_Dados_Emp(1, "UF");
                                    sendp1 = sendere+", "+snum;
                                    sendp2 = sbai+", "+scid+"-"+suf;

                                   // out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                    //out.write((EscPosBase.getFontsmall()));//Fonte pequena
                                    out.write(sendp1.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    out.write(sendp2.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    out.write((EscPosBase.getFontNormal()));//Fonte tamanho normal

                                    out.write("Documento Auxiliar do Bilhete".getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    out.write("de Passagem Eletronico".getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    if (stpEmis.equals("2")){
                                        out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                        out.write("EMITIDO EM CONTINGENCIA".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write("Pendente de Autorizacao".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito
                                    }
                                    if (stipamb.equals("2")) { //Homologacao
                                        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write("EMITIDO EM HOMOLOGACAO".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write("SEM VALOR FISCAL".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                    }

                                    out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    out.write(EscPosBase.alignLeft()); //Esquerda

                                    ///////////
                                    //Dados da Viagem
                                    String sdatemb = "";
                                    NodeList nodeviagem1 = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem1.getLength(); tempvia++){
                                        Node nviagem1 = nodeviagem1.item(tempvia);
                                        Element viaElement1 = (Element) nviagem1;
                                        sdatemb = viaElement1.getElementsByTagName("dhViagem").item(0).getTextContent();

                                    }

                                    NodeList nodepassagem = doc.getElementsByTagName("infPassagem");
                                    for (int temppass = 0; temppass < nodepassagem.getLength(); temppass++){
                                        Node npass = nodepassagem.item(temppass);
                                        Element passElement = (Element) npass;
                                        DB_TRE dbtre = new DB_TRE(getApplicationContext());
                                        String cOri = passElement.getElementsByTagName("cLocOrig").item(0).getTextContent();
                                        String xOri = passElement.getElementsByTagName("xLocOrig").item(0).getTextContent();
                                        String ufOri = dbtre.Busca_Dados_Tre_Codigo(cOri, "UF");
                                        String sOrigem = xOri+" ("+ufOri+")";
                                        String cDes = passElement.getElementsByTagName("cLocDest").item(0).getTextContent();
                                        String xDes = passElement.getElementsByTagName("xLocDest").item(0).getTextContent();
                                        String ufDes = dbtre.Busca_Dados_Tre_Codigo(cDes, "UF");
                                        String sDestino = xDes+" ("+ufDes+")";
                                        //String sdatemb = passElement.getElementsByTagName("dhEmb").item(0).getTextContent();
                                        String sano = sdatemb.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smes = sdatemb.substring(5, (7));
                                        String sdia = sdatemb.substring(8, (10));
                                        String sdEmb = sdia+"/"+smes+"/"+sano;
                                        String sHora = sdatemb.substring(11, (16));

                                        out.write(EscPosBase.alignCenter()); //Centralizado
                                        out.write("Origem:".getBytes(StandardCharsets.UTF_8));
                                        out.write(sOrigem.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write("Destino:".getBytes(StandardCharsets.UTF_8));
                                        out.write(sDestino.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write("Data:".getBytes(StandardCharsets.UTF_8));
                                        out.write(sdEmb.getBytes(StandardCharsets.UTF_8));
                                        out.write(" | Horario:".getBytes(StandardCharsets.UTF_8));
                                        out.write(sHora.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());



                                        NodeList nodepassageiro = doc.getElementsByTagName("infPassageiro");
                                        for (int temppessoa = 0; temppessoa < nodepassageiro.getLength(); temppessoa++) {
                                            Node npessoa = nodepassageiro.item(temppessoa);
                                            if (npessoa.getNodeType() == Node.ELEMENT_NODE) {
                                                Element pessoaElement = (Element) npessoa;

                                                sPassageiro = pessoaElement.getElementsByTagName("xNome").item(0).getTextContent();
                                                sDocpas = pessoaElement.getElementsByTagName("nDoc").item(0).getTextContent();
                                                NodeList tagCPF = doc.getElementsByTagName("CPF");
                                                for (int icpf = 0; icpf < tagCPF.getLength(); icpf++){
                                                    Node ncpf = tagCPF.item(icpf);
                                                    if (ncpf.getNodeType() == Node.ELEMENT_NODE) {
                                                        sCPF = pessoaElement.getElementsByTagName("CPF").item(0).getTextContent();
                                                    }
                                                }

                                            }
                                        }


                                    }

                                    //Dados da Viagem
                                    NodeList nodeviagem = doc.getElementsByTagName("infViagem");
                                    for (int tempvia = 0; tempvia < nodeviagem.getLength(); tempvia++){
                                        Node nviagem = nodeviagem.item(tempvia);
                                        Element viaElement = (Element) nviagem;
                                        NodeList ncad = doc.getElementsByTagName("poltrona");
                                        for (int tempcad = 0; tempcad < ncad.getLength(); tempcad++) {
                                            String scad = viaElement.getElementsByTagName("poltrona").item(0).getTextContent();

                                            out.write("Poltrona:  ".getBytes(StandardCharsets.UTF_8));
                                            out.write(scad.getBytes(StandardCharsets.UTF_8));
                                            out.write(EscPosBase.nextLine());
                                        }

                                        String sprefix = viaElement.getElementsByTagName("prefixo").item(0).getTextContent();
                                        String slinha = viaElement.getElementsByTagName("xPercurso").item(0).getTextContent();
                                        String stpserv = viaElement.getElementsByTagName("tpServ").item(0).getTextContent();
                                        ///Tipo de serviço
                                        String tiposerv = "";
                                        if (stpserv.equals("1")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("2")){ tiposerv = "Convencional";}
                                        if (stpserv.equals("3")){ tiposerv = "Semileito";}
                                        if (stpserv.equals("4")){ tiposerv = "Leito Com AR";}
                                        if (stpserv.equals("5")){ tiposerv = "Leito Sem AR";}
                                        if (stpserv.equals("6")){ tiposerv = "Executivo";}

                                        out.write("Prefixo:  ".getBytes(StandardCharsets.UTF_8));
                                        out.write(sprefix.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write("Linha:".getBytes(StandardCharsets.UTF_8));
                                        out.write(slinha.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write("  TIPO: ".getBytes(StandardCharsets.UTF_8));
                                        out.write(tiposerv.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                    }

                                    //falores que compoem o bilhete
                                    NodeList nodeinfvalor = doc.getElementsByTagName("infValorBPe");
                                    for (int tempvalor = 0; tempvalor < nodeinfvalor.getLength(); tempvalor++){
                                        Node nvalor = nodeinfvalor.item(tempvalor);
                                        Element valorElement = (Element)  nvalor;
                                        String svalBPe = valorElement.getElementsByTagName("vBP").item(tempvalor).getTextContent();
                                        String sdesconto = valorElement.getElementsByTagName("vDesconto").item(tempvalor).getTextContent();
                                        String valorpgto = valorElement.getElementsByTagName("vPgto").item(tempvalor).getTextContent();
                                        if (!sdesconto.equals("0.00")) {
                                            sxdesconto = valorElement.getElementsByTagName("xDesconto").item(tempvalor).getTextContent();
                                        }

                                        NodeList nodecomp = doc.getElementsByTagName("Comp"); //composicao do valor
                                        for (int tempcomp = 0; tempcomp < nodecomp.getLength(); tempcomp++){
                                            Node ncomp = nodecomp.item(tempcomp);
                                            if (ncomp.getNodeType() == Node.ELEMENT_NODE) {
                                                Element compElement = (Element) ncomp;


                                                String tpcomp = compElement.getElementsByTagName("tpComp").item(0).getTextContent();
                                                String valcomp = compElement.getElementsByTagName("vComp").item(0).getTextContent();
                                                String stipo = "";
                                                String sespaco = "";
                                                if (tpcomp.equals("01")) { stipo = "Tarifa";}
                                                if (tpcomp.equals("02")) { stipo = "Pedágio";}
                                                if (tpcomp.equals("03")) { stipo = "Tx Embarque";}
                                                if (tpcomp.equals("04")) { stipo = "Seguro";}
                                                if (tpcomp.equals("99")) { stipo = "Outros";}
                                                String sval = stipo+valcomp;
                                                Integer iqtd = sval.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32-iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }

                                                out.write(EscPosBase.alignLeft()); //Esquerda
                                                stipo = stipo + sespaco;
                                                out.write(stipo.getBytes(StandardCharsets.UTF_8));
                                                out.write(valcomp.getBytes(StandardCharsets.UTF_8));
                                                out.write(EscPosBase.nextLine());

                                            }





                                        }
                                        String sespaco = "";
                                        String sval = "Valor Total R$"+svalBPe;
                                        Integer iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String stotal = "Valor Total R$" + sespaco;
                                        out.write(stotal.getBytes(StandardCharsets.UTF_8));
                                        out.write(svalBPe.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        sespaco = "";
                                        sval = "Desconto R$"+sdesconto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String stotaldesc = "Desconto R$" + sespaco;
                                        out.write(stotaldesc.getBytes(StandardCharsets.UTF_8));
                                        out.write(sdesconto.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        out.write(EscPosBase.alignLeft()); //Esquerda
                                        sespaco = "";
                                        sval = "Valor a Pagar R$"+valorpgto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        //out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                        String svalapagar = "Valor a Pagar R$" + sespaco;
                                        out.write(svalapagar.getBytes(StandardCharsets.UTF_8));
                                        out.write(valorpgto.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());


                                        //Fromas de pagamento
                                        String sforpag = "";
                                        NodeList nodeinfpag = doc.getElementsByTagName("pag");
                                        for (int temppag = 0; temppag < nodeinfpag.getLength(); temppag++) {
                                            Node npag = nodeinfpag.item(temppag);
                                            Element pagElement = (Element) npag;
                                            String stpag = pagElement.getElementsByTagName("tPag").item(temppag).getTextContent();
                                            if (stpag.equals("01")) {
                                                sforpag = "DINHEIRO";
                                            }
                                            if (stpag.equals("03")) {
                                                sforpag = "CREDITO";
                                            }
                                            if (stpag.equals("04")) {
                                                sforpag = "DEBITO";
                                            }
                                            if (stpag.equals("05")) {
                                                sforpag = "VALE TRANSPORTE";
                                            }
                                            if (stpag.equals("99")) {
                                                sforpag = "OUTROS";
                                            }
                                        }
                                        //////////////////////////////



                                        sespaco = "";
                                        sval = sforpag+valorpgto;
                                        iqtd = sval.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32-iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        out.write("FORMA DE PAGAMENTO    VALOR PAGO".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        String sdinheiro = sforpag + sespaco;
                                        out.write(sdinheiro.getBytes(StandardCharsets.UTF_8));
                                        out.write(valorpgto.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                    }

                                    //falores dos impostos
                                    NodeList nodeimp = doc.getElementsByTagName("imp");
                                    for (int tempimp = 0; tempimp < nodeimp.getLength(); tempimp++) {
                                        Node nimp = nodeimp.item(tempimp);
                                        Element impElement = (Element) nimp;
                                        NodeList ntrib = doc.getElementsByTagName("vTotTrib");
                                        for (int temptrib = 0; temptrib < ntrib.getLength(); temptrib++) {
                                            stottrib = impElement.getElementsByTagName("vTotTrib").item(0).getTextContent();

                                        }
                                    }
                                    ////Dados do BP-e
                                    out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito
                                    out.write(EscPosBase.alignCenter()); //Centralizado
                                    out.write((EscPosBase.getFontsmall()));
                                    out.write("Consulte pela Chave de Acesso em:".getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                   // out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                    String surl = dbemp.Busca_Dados_Emp(1, "Urlqrc");
                                    out.write(surl.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());


                                    //Element infbpeElement = (Element) nodeinfbpe;
                                    String sId = infElement.getAttribute("Id");
                                    String sChave = sId.substring(3, 47);
                                    out.write(sChave.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());
                                    out.write((EscPosBase.getFontNormal()));

                                    if (!sPassageiro.equals("")){
                                        String spass = "PASSAGEIRO DOC: "+sDocpas;
                                        out.write(spass.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write(sPassageiro.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());

                                        if (!sCPF.equals("")) {
                                            String scpfcli = "CPF: "+sCPF;
                                            out.write(scpfcli.getBytes(StandardCharsets.UTF_8));
                                            out.write(EscPosBase.nextLine());
                                        }
                                    } else if (sPassageiro.equals("")) {
                                        out.write("PASSAGEIRO NAO IDENTIFICADO".getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                    }

                                    if (!sxdesconto.equals("")) {
                                        String stipdesc = "Tipo de Desconto: "+sxdesconto;
                                        out.write(stipdesc.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                    }
                                    //identificacao do bilhete
                                    sNROBPE = (("000000000" + sNROBPE).substring(sNROBPE.length()));
                                    sSERBPE = (("000" + sSERBPE).substring(sSERBPE.length()));

                                    out.write("BP-e n ".getBytes(StandardCharsets.UTF_8));
                                    String snumesp = (sNROBPE)+"    ";
                                    out.write(snumesp.getBytes(StandardCharsets.UTF_8));
                                    out.write("Serie ".getBytes(StandardCharsets.UTF_8));
                                    out.write(sSERBPE.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    String sanoe = sdatemi.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                    String smese = sdatemi.substring(5, (7));
                                    String sdiae = sdatemi.substring(8, (10));
                                    String sdEmi = sdiae+"/"+smese+"/"+sanoe;
                                    String sHorae = sdatemi.substring(11, (19));

                                    sdEmi = sdEmi+"     ";
                                    out.write(sdEmi.getBytes(StandardCharsets.UTF_8));
                                    out.write(sHorae.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                }
                                //Informacoes suplementares
                                NodeList nodeinfCpl = doc.getElementsByTagName("infBPeSupl");
                                for (int tempinfsub = 0; tempinfsub < nodeinfCpl.getLength(); tempinfsub++) {
                                    Node ninfsub = nodeinfCpl.item(tempinfsub);
                                    Element infsubElement = (Element) ninfsub;
                                    sQRcode = infsubElement.getElementsByTagName("qrCodBPe").item(0).getTextContent();

                                }
                            }

                            /////Dados de Autorização.
                            if (stpEmis.equals("2")){ //Contingencia
                                out.write("EMITIDO EM CONTINGENCIA".getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());
                                out.write("Pendente de Autorizacao".getBytes(StandardCharsets.UTF_8));
                                //out.write(EscPosBase.nextLine());
                                //out.write(EscPosBase.nextLine());
                            } else if (stpEmis.equals("1")) { //Emissao Normal
                                NodeList nodeprotbpe = doc.getElementsByTagName("protBPe");
                                for (int tempprot = 0; tempprot < nodeprotbpe.getLength(); tempprot++) {
                                    Node nprotbpe = nodeprotbpe.item(tempprot);
                                    Element protElement = (Element) nprotbpe;
                                    NodeList nodeinfprot = doc.getElementsByTagName("infProt");
                                    for (int tempinfprot = 0; tempinfprot < nodeinfprot.getLength(); tempinfprot++) {
                                        Node ninfprot = nodeinfprot.item(tempinfprot);
                                        Element infprotElement = (Element) ninfprot;
                                        String sprot = infprotElement.getElementsByTagName("nProt").item(0).getTextContent();
                                        String sdataut = infprotElement.getElementsByTagName("dhRecbto").item(0).getTextContent();
                                        out.write(EscPosBase.getFontWNormal()); // Desativar Fonte em Negrito
                                        out.write("Protocolo de Autorizaçca: ".getBytes(StandardCharsets.UTF_8));
                                        out.write(sprot.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());
                                        out.write("Data de Autorizacao: ".getBytes(StandardCharsets.UTF_8));

                                        String sanoA = sdataut.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smesA = sdataut.substring(5, (7));
                                        String sdiaA = sdataut.substring(8, (10));
                                        String sdAut = sdiaA + "/" + smesA + "/" + sanoA;
                                        String sHoraA = sdataut.substring(11, (19));

                                        sdAut = sdAut + " ";
                                        out.write(sdAut.getBytes(StandardCharsets.UTF_8));
                                        out.write(sHoraA.getBytes(StandardCharsets.UTF_8));
                                       // out.write(EscPosBase.nextLine(2));

                                    }
                                }
                            }



                            if (stpEmis.equals("2")) {
                                out.write(EscPosBase.nextLine());
                                out.write(EscPosBase.alignCenter());
                                out.write(PrinterConverter.QRCodeDataToBytes(sQRcode, 300));
                                out.write(EscPosBase.alignCenter());
                               // out.write(EscPosBase.nextLine());

                            } else if (stpEmis.equals("1")){
                                out.write(EscPosBase.nextLine());
                                out.write(EscPosBase.alignCenter());
                                out.write(PrinterConverter.QRCodeDataToBytes(sQRcode, 200));
                                out.write(EscPosBase.alignCenter());
                                //out.write(EscPosBase.nextLine());
                            }


                            if (stottrib != ""){
                                stottrib = stottrib.replace(".", ",");
                                String sobs = "Informacao dos Tributos Totais Incidentes (Lei Federal 12.741/2012): R$ " + stottrib;
                                //out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                out.write(sobs.getBytes(StandardCharsets.UTF_8));
                            }
                            if (sespacos.equals("xx")) {
                                out.write(EscPosBase.nextLine(4));
                            } else {
                                int iesp = Integer.parseInt(sespacos);
                                out.write(EscPosBase.nextLine(iesp));
                            }


                        }



            } finally {
                // btnPrint.setEnabled(true);
                impressora.close();
            }
            } catch (Exception e) {
                System.out.println("Erro ao executar impressao : "+e.toString());
                Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", Toast.LENGTH_LONG).show();
        }

    }

    //DTS2500
    public void testPrint() {
        Log.d(TAG, "Iniciando teste de impressão");
        if (!printfManager.isConnect()) {
            Log.e(TAG, "Impressora não conectada");
            Toast.makeText(this, "Erro: Impressora não conectada", Toast.LENGTH_SHORT).show();
            return;
        }
        List<PrintfManager.PrintCommand> commands = new ArrayList<>();
        commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Teste DTS-2500", 1));
        commands.add(new PrintfManager.PrintCommand("C", "S", "B", "Linha em negrito", 2));
        try {
            printfManager.printBufferedText(commands);
            Log.d(TAG, "Teste de impressão enviado");
            Toast.makeText(this, "Teste enviado", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Erro no teste de impressão: " + e.getMessage(), e);
            Toast.makeText(this, "Erro no teste: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @SuppressLint("MissingPermission")
    public void Imprime_BPe_DTS()  {
        if (printfManager == null) {
            PrintfBlueListActivity.startActivity(ViaActivity.this);
        }

        String chaveBPe = Guarda_Texto;
        if (printfManager.isConnect()) {
            try {
                List<PrintfManager.PrintCommand> commands = new ArrayList<>();
                        //printfManager.defaultConnection(ViaActivity.this);
                        System.out.println("chaveBPe: " + chaveBPe);
                        String stpEmis;
                        stpEmis = chaveBPe.substring(34, (35)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                        sgtpEmis = stpEmis;
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        String sextencao = "";
                        if (stpEmis.equals("1")) {
                            sextencao = "-procBPe.xml";
                        } else if (stpEmis.equals("2")) {
                            sextencao = "-bpe.xml";
                        }
                        File fXmlFile = new File(getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        //Document doc = dBuilder.parse(getAssets().open("Arquivo.xml"));
                        System.out.println("Ler XML : " + getExternalFilesDir("Download").getAbsolutePath() + "/" + chaveBPe + sextencao);
                        Document doc = dBuilder.parse(fXmlFile);

                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());
                           String stipoemis = chaveBPe.substring(34, (35));
                           String sespacos = dbemp.Busca_Dados_Emp(1, "Rsv003");


                            doc.getDocumentElement().normalize();
                            NodeList nodeProc = null;
                            //NodeList nodebpe = null;
                            if (stipoemis.equals("1")) {
                                nodeProc = doc.getElementsByTagName("bpeProc");
                                //nodebpe = doc.getElementsByTagName("BPe");
                            } else if (stipoemis.equals("2")) {
                                nodeProc = doc.getElementsByTagName("BPe");
                                //nodebpe = doc.getElementsByTagName("BPe");
                            }
                            int counter = nodeProc.getLength();
                            for (int temp = 0; temp < nodeProc.getLength(); temp++) {

                                Node nNode = nodeProc.item(temp);

                                // System.out.println("\nCurrent Element :" + nNode.getNodeName());

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;

                                    NodeList nodeinfbpe = doc.getElementsByTagName("infBPe");
                                    for (int tempinf = 0; tempinf < nodeinfbpe.getLength(); tempinf++) {
                                        String sNROBPE = "";
                                        String sSERBPE = "";
                                        String sdatemi = "";
                                        String stottrib = "";
                                        String sPassageiro = "";
                                        String sDocpas = "";
                                        String sCPF = "";
                                        String stipamb = "";
                                        Node ninf = nodeinfbpe.item(tempinf);
                                        Element infElement = (Element) ninf;
                                        //////////////////////////////////////////////
                                        NodeList nodeide = doc.getElementsByTagName("ide");
                                        for (int tempide = 0; tempide < nodeide.getLength(); tempide++) {
                                            Node nide = nodeide.item(tempide);
                                            Element ideElement = (Element) nide;
                                            sNROBPE = ideElement.getElementsByTagName("nBP").item(0).getTextContent();
                                            sSERBPE = ideElement.getElementsByTagName("serie").item(0).getTextContent();
                                            sdatemi = ideElement.getElementsByTagName("dhEmi").item(0).getTextContent();
                                            stipamb = ideElement.getElementsByTagName("tpAmb").item(0).getTextContent();
                                            int inumero = Integer.parseInt(sNROBPE);
                                            DB_BPE dbbpe = new DB_BPE(ViaActivity.this);
                                            String sIDBpe = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "ID");
                                            String sqtdimp = dbbpe.Busca_Dados_Bpe(Integer.toString(inumero), "Qtdimp");
                                            int iqtdatu = (Integer.parseInt(sqtdimp) + 1);
                                            dbbpe.Atualizar_Campo_Bpe(sIDBpe, "Qtdimp", Integer.toString(iqtdatu));
                                        }

                                        //////////////////////////////////////////////
                                        commands.add(new PrintfManager.PrintCommand("C", "N", "B", dbemp.Busca_Dados_Emp(1, "Descri"), 1));
                                        String scnpj = dbemp.Busca_Dados_Emp(1, "Cnpj");
                                        String sIe = dbemp.Busca_Dados_Emp(1, "Insest");
                                        String sinscricao = ("CNPJ: " + scnpj);
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "B", sinscricao, 1));
                                        sinscricao = ("IE: " + sIe);
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "B", sinscricao, 1));


                                        String sendp1, sendp2, sendere, snum, sbai, scid, suf;
                                        sendere = dbemp.Busca_Dados_Emp(1, "Endere");
                                        snum = dbemp.Busca_Dados_Emp(1, "Numero");
                                        sbai = dbemp.Busca_Dados_Emp(1, "Bairro");
                                        scid = dbemp.Busca_Dados_Emp(1, "Cidade");
                                        suf = dbemp.Busca_Dados_Emp(1, "UF");
                                        sendp1 = sendere + ", " + snum;
                                        sendp2 = sbai + ", " + scid + "-" + suf;

                                        commands.add(new PrintfManager.PrintCommand("C", "S", "N", sendp1, 1));
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "N", sendp2, 1));

                                        commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Documento Auxiliar do Bilhete", 1));
                                        commands.add(new PrintfManager.PrintCommand("C", "N", "N", "de Passagem Eletronico", 1));


                                        if (stpEmis.equals("2")) {
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "B", "EMITIDO EM CONTINGENCIA", 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "B", "Pendente de Autorizacao", 1));
                                        }
                                        if (stipamb.equals("2")) { //Homologacao
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "B", "--------------------------------", 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "B", "EMITIDO EM HOMOLOGACAO", 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "B", "SEM VALOR FISCAL", 1));
                                        }
                                        commands.add(new PrintfManager.PrintCommand("C", "N", "B", "--------------------------------", 1));
                                        ///////////
                                        //Dados da Viagem
                                        String sdatemb = "";
                                        NodeList nodeviagem1 = doc.getElementsByTagName("infViagem");
                                        for (int tempvia = 0; tempvia < nodeviagem1.getLength(); tempvia++) {
                                            Node nviagem1 = nodeviagem1.item(tempvia);
                                            Element viaElement1 = (Element) nviagem1;
                                            sdatemb = viaElement1.getElementsByTagName("dhViagem").item(0).getTextContent();

                                        }

                                        NodeList nodepassagem = doc.getElementsByTagName("infPassagem");
                                        for (int temppass = 0; temppass < nodepassagem.getLength(); temppass++) {
                                            Node npass = nodepassagem.item(temppass);
                                            Element passElement = (Element) npass;
                                            DB_TRE dbtre = new DB_TRE(ViaActivity.this);
                                            String cOri = passElement.getElementsByTagName("cLocOrig").item(0).getTextContent();
                                            String xOri = passElement.getElementsByTagName("xLocOrig").item(0).getTextContent();
                                            String ufOri = dbtre.Busca_Dados_Tre_Codigo(cOri, "UF");
                                            String sOrigem = xOri + " (" + ufOri + ")";
                                            String cDes = passElement.getElementsByTagName("cLocDest").item(0).getTextContent();
                                            String xDes = passElement.getElementsByTagName("xLocDest").item(0).getTextContent();
                                            String ufDes = dbtre.Busca_Dados_Tre_Codigo(cDes, "UF");
                                            String sDestino = xDes + " (" + ufDes + ")";
                                            //String sdatemb = passElement.getElementsByTagName("dhEmb").item(0).getTextContent();
                                            String sano = sdatemb.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                            String smes = sdatemb.substring(5, (7));
                                            String sdia = sdatemb.substring(8, (10));
                                            String sdEmb = sdia + "/" + smes + "/" + sano;
                                            String sHora = sdatemb.substring(11, (16));

                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Origem: "+sOrigem, 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Destino: "+sDestino, 2));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Data: "+sdEmb+" | Horario:"+sHora, 2));


                                            NodeList nodepassageiro = doc.getElementsByTagName("infPassageiro");
                                            for (int temppessoa = 0; temppessoa < nodepassageiro.getLength(); temppessoa++) {
                                                Node npessoa = nodepassageiro.item(temppessoa);
                                                if (npessoa.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element pessoaElement = (Element) npessoa;

                                                    sPassageiro = pessoaElement.getElementsByTagName("xNome").item(0).getTextContent();
                                                    sDocpas = pessoaElement.getElementsByTagName("nDoc").item(0).getTextContent();
                                                    NodeList tagCPF = doc.getElementsByTagName("CPF");
                                                    for (int icpf = 0; icpf < tagCPF.getLength(); icpf++) {
                                                        Node ncpf = tagCPF.item(icpf);
                                                        if (ncpf.getNodeType() == Node.ELEMENT_NODE) {
                                                            sCPF = pessoaElement.getElementsByTagName("CPF").item(0).getTextContent();
                                                        }
                                                    }

                                                }
                                            }


                                        }

                                        //Dados da Viagem
                                        NodeList nodeviagem = doc.getElementsByTagName("infViagem");
                                        for (int tempvia = 0; tempvia < nodeviagem.getLength(); tempvia++) {
                                            Node nviagem = nodeviagem.item(tempvia);
                                            Element viaElement = (Element) nviagem;
                                            NodeList ncad = doc.getElementsByTagName("poltrona");
                                            for (int tempcad = 0; tempcad < ncad.getLength(); tempcad++) {
                                                String scad = viaElement.getElementsByTagName("poltrona").item(0).getTextContent();
                                                commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Poltrona:  "+scad, 1));
                                            }

                                            String sprefix = viaElement.getElementsByTagName("prefixo").item(0).getTextContent();
                                            String slinha = viaElement.getElementsByTagName("xPercurso").item(0).getTextContent();
                                            String stpserv = viaElement.getElementsByTagName("tpServ").item(0).getTextContent();
                                            ///Tipo de serviço
                                            String tiposerv = "";
                                            if (stpserv.equals("1")) {
                                                tiposerv = "Convencional";
                                            }
                                            if (stpserv.equals("2")) {
                                                tiposerv = "Convencional";
                                            }
                                            if (stpserv.equals("3")) {
                                                tiposerv = "Semileito";
                                            }
                                            if (stpserv.equals("4")) {
                                                tiposerv = "Leito Com AR";
                                            }
                                            if (stpserv.equals("5")) {
                                                tiposerv = "Leito Sem AR";
                                            }
                                            if (stpserv.equals("6")) {
                                                tiposerv = "Executivo";
                                            }
                                            commands.add(new PrintfManager.PrintCommand("C", "S", "N", "Prefixo:  "+sprefix, 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Linha:  "+slinha, 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "S", "N", "  TIPO:  "+tiposerv, 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "--------------------------------", 1));


                                        }

                                        //falores que compoem o bilhete
                                        NodeList nodeinfvalor = doc.getElementsByTagName("infValorBPe");
                                        for (int tempvalor = 0; tempvalor < nodeinfvalor.getLength(); tempvalor++) {
                                            Node nvalor = nodeinfvalor.item(tempvalor);
                                            Element valorElement = (Element) nvalor;
                                            String svalBPe = valorElement.getElementsByTagName("vBP").item(tempvalor).getTextContent();
                                            String sdesconto = valorElement.getElementsByTagName("vDesconto").item(tempvalor).getTextContent();
                                            String valorpgto = valorElement.getElementsByTagName("vPgto").item(tempvalor).getTextContent();
                                            if (!sdesconto.equals("0.00")) {
                                                String sxdesconto;
                                                sxdesconto = valorElement.getElementsByTagName("xDesconto").item(tempvalor).getTextContent();
                                                sgxdesconto = sxdesconto;
                                            }

                                            NodeList nodecomp = doc.getElementsByTagName("Comp"); //composicao do valor
                                            for (int tempcomp = 0; tempcomp < nodecomp.getLength(); tempcomp++) {
                                                Node ncomp = nodecomp.item(tempcomp);
                                                if (ncomp.getNodeType() == Node.ELEMENT_NODE) {
                                                    Element compElement = (Element) ncomp;


                                                    String tpcomp = compElement.getElementsByTagName("tpComp").item(0).getTextContent();
                                                    String valcomp = compElement.getElementsByTagName("vComp").item(0).getTextContent();
                                                    String stipo = "";
                                                    String sespaco = "";
                                                    if (tpcomp.equals("01")) {
                                                        stipo = "Tarifa";
                                                    }
                                                    if (tpcomp.equals("02")) {
                                                        stipo = "Pedágio";
                                                    }
                                                    if (tpcomp.equals("03")) {
                                                        stipo = "Tx Embarque";
                                                    }
                                                    if (tpcomp.equals("04")) {
                                                        stipo = "Seguro";
                                                    }
                                                    if (tpcomp.equals("99")) {
                                                        stipo = "Outros";
                                                    }
                                                    String sval = stipo + valcomp;
                                                    Integer iqtd = sval.length();
                                                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                        Integer iresto = (32 - iqtd);
                                                        while (sespaco.length() < iresto) {
                                                            sespaco = " " + sespaco;
                                                        }
                                                    }

                                                    stipo = stipo + sespaco + valcomp;
                                                    commands.add(new PrintfManager.PrintCommand("L", "N", "N", stipo, 1));


                                                }


                                            }
                                            String sespaco = "";
                                            String sval = "Valor Total R$" + svalBPe;
                                            Integer iqtd = sval.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            String stotal = "Valor Total R$" + sespaco + svalBPe;
                                            commands.add(new PrintfManager.PrintCommand("L", "N", "N", stotal, 1));

                                            sespaco = "";
                                            sval = "Desconto R$" + sdesconto;
                                            iqtd = sval.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            String stotaldesc = "Desconto R$" + sespaco + sdesconto;
                                            commands.add(new PrintfManager.PrintCommand("L", "N", "N", stotaldesc, 1));

                                            sespaco = "";
                                            sval = "Valor a Pagar R$" + valorpgto;
                                            iqtd = sval.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            //out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                            String svalapagar = "Valor a Pagar R$" + sespaco + valorpgto;
                                            commands.add(new PrintfManager.PrintCommand("L", "N", "N", svalapagar, 1));


                                            //Fromas de pagamento
                                            String sforpag = "";
                                            NodeList nodeinfpag = doc.getElementsByTagName("pag");
                                            for (int temppag = 0; temppag < nodeinfpag.getLength(); temppag++) {
                                                Node npag = nodeinfpag.item(temppag);
                                                Element pagElement = (Element) npag;
                                                String stpag = pagElement.getElementsByTagName("tPag").item(temppag).getTextContent();
                                                if (stpag.equals("01")) {
                                                    sforpag = "DINHEIRO";
                                                }
                                                if (stpag.equals("03")) {
                                                    sforpag = "CREDITO";
                                                }
                                                if (stpag.equals("04")) {
                                                    sforpag = "DEBITO";
                                                }
                                                if (stpag.equals("05")) {
                                                    sforpag = "VALE TRANSPORTE";
                                                }
                                                if (stpag.equals("99")) {
                                                    sforpag = "OUTROS";
                                                }
                                            }
                                            //////////////////////////////


                                            sespaco = "";
                                            sval = sforpag + valorpgto;
                                            iqtd = sval.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            commands.add(new PrintfManager.PrintCommand("L", "N", "N", "FORMA DE PAGAMENTO    VALOR PAGO", 1));
                                            String sdinheiro = sforpag + sespaco + valorpgto;
                                            commands.add(new PrintfManager.PrintCommand("L", "N", "N", sdinheiro, 1));

                                        }
                                        NodeList nodeimp = doc.getElementsByTagName("imp");
                                        for (int tempimp = 0; tempimp < nodeimp.getLength(); tempimp++) {
                                            Node nimp = nodeimp.item(tempimp);
                                            Element impElement = (Element) nimp;
                                            NodeList ntrib = doc.getElementsByTagName("vTotTrib");
                                            for (int temptrib = 0; temptrib < ntrib.getLength(); temptrib++) {
                                                stottrib = impElement.getElementsByTagName("vTotTrib").item(0).getTextContent();
                                                sgtottrib = stottrib;
                                            }
                                        }
                                        ////Dados do BP-e
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "N", "Consulte pela Chave de Acesso em:", 1));

                                        String surl = dbemp.Busca_Dados_Emp(1, "Urlqrc");
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "N", surl, 1));


                                        //Element infbpeElement = (Element) nodeinfbpe;
                                        String sId = infElement.getAttribute("Id");
                                        String sChave = sId.substring(3, 47);
                                        commands.add(new PrintfManager.PrintCommand("C", "S", "N", sId+sChave, 1));

                                        if (!sPassageiro.equals("")) {
                                            String spass = "PASSAGEIRO DOC: " + sDocpas;
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", spass, 1));
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", sPassageiro, 1));

                                            if (!sCPF.equals("")) {
                                                String scpfcli = "CPF: " + sCPF;
                                                commands.add(new PrintfManager.PrintCommand("C", "N", "N", scpfcli, 1));
                                            }
                                        } else if (sPassageiro.equals("")) {
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "PASSAGEIRO NAO IDENTIFICADO", 1));
                                        }
                                        String sxdesconto = sgxdesconto;
                                        if (!sxdesconto.equals("")) {
                                            String stipdesc = "Tipo de Desconto: " + sxdesconto;
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", stipdesc, 1));
                                        }
                                        //identificacao do bilhete
                                        sNROBPE = (("000000000" + sNROBPE).substring(sNROBPE.length()));
                                        sSERBPE = (("000" + sSERBPE).substring(sSERBPE.length()));

                                        String snumesp = "BP-e n "+(sNROBPE) + "    Serie "+sSERBPE;
                                        commands.add(new PrintfManager.PrintCommand("C", "N", "N", snumesp, 1));

                                        String sanoe = sdatemi.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                        String smese = sdatemi.substring(5, (7));
                                        String sdiae = sdatemi.substring(8, (10));
                                        String sdEmi = sdiae + "/" + smese + "/" + sanoe;
                                        String sHorae = sdatemi.substring(11, (19));

                                        sdEmi = sdEmi + "     "+sHorae;
                                        commands.add(new PrintfManager.PrintCommand("C", "N", "N", sdEmi, 1));

                                    }
                                    //Informacoes suplementares
                                    NodeList nodeinfCpl = doc.getElementsByTagName("infBPeSupl");
                                    for (int tempinfsub = 0; tempinfsub < nodeinfCpl.getLength(); tempinfsub++) {
                                        Node ninfsub = nodeinfCpl.item(tempinfsub);
                                        Element infsubElement = (Element) ninfsub;
                                        String sQRcode;
                                        sQRcode = infsubElement.getElementsByTagName("qrCodBPe").item(0).getTextContent();
                                        sgQRcode = sQRcode;
                                    }
                                }

                                /////Dados de Autorização.
                                if (stpEmis.equals("2")) { //Contingencia
                                    commands.add(new PrintfManager.PrintCommand("C", "N", "B", "EMITIDO EM CONTINGENCIA", 1));
                                    commands.add(new PrintfManager.PrintCommand("C", "N", "B", "Pendente de Autorizacao", 1));;
                                } else if (stpEmis.equals("1")) { //Emissao Normal
                                    NodeList nodeprotbpe = doc.getElementsByTagName("protBPe");
                                    for (int tempprot = 0; tempprot < nodeprotbpe.getLength(); tempprot++) {
                                        Node nprotbpe = nodeprotbpe.item(tempprot);
                                        Element protElement = (Element) nprotbpe;
                                        NodeList nodeinfprot = doc.getElementsByTagName("infProt");
                                        for (int tempinfprot = 0; tempinfprot < nodeinfprot.getLength(); tempinfprot++) {
                                            Node ninfprot = nodeinfprot.item(tempinfprot);
                                            Element infprotElement = (Element) ninfprot;
                                            String sprot = infprotElement.getElementsByTagName("nProt").item(0).getTextContent();
                                            String sdataut = infprotElement.getElementsByTagName("dhRecbto").item(0).getTextContent();
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", "Protocolo de Autorizaçca: "+sprot, 1));
                                            //outputStream.write(EscPosBase.nextLine());
                                            //outputStream.write("Data de Autorizacao: ".getBytes(StandardCharsets.UTF_8));

                                            String sanoA = sdataut.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                                            String smesA = sdataut.substring(5, (7));
                                            String sdiaA = sdataut.substring(8, (10));
                                            String sdAut = sdiaA + "/" + smesA + "/" + sanoA;
                                            String sHoraA = sdataut.substring(11, (19));

                                            sdAut = "Data de Autorizacao: "+sdAut + " "+sHoraA;
                                            commands.add(new PrintfManager.PrintCommand("C", "N", "N", sdAut, 2));


                                        }
                                    }
                                }
                                printfManager.printBufferedText(commands);


                                String sQRcode = sgQRcode;
                                if (stpEmis.equals("2")) {
                                    try {
                                        printfManager.printf_QRcode("A", "C", PrinterConverter.QRCodeDataToBytes(sQRcode, 300));
                                    } catch (PrinterConverter.PrinterException e) {
                                        Log.e(TAG, "Erro ao imprimir QR-Code", e);
                                    }

                                } else if (stpEmis.equals("1")) {
                                    try {
                                        printfManager.printf_QRcode("A","C", PrinterConverter.QRCodeDataToBytes(sQRcode, 200));
                                    } catch (PrinterConverter.PrinterException e) {
                                        Log.e(TAG, "Erro ao imprimir QR-Code", e);
                                    }
                                }
                                List<PrintfManager.PrintCommand> commands2 = new ArrayList<>();
                                commands2.add(new PrintfManager.PrintCommand("L", "S", "N", "", 2));
                                String stottrib = sgtottrib;
                                if (stottrib != "") {
                                    stottrib = stottrib.replace(".", ",");
                                    String sobs = "Informacao dos Tributos Totais Incidentes (Lei Federal 12.741/2012): R$ " + stottrib;
                                    //out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                                    commands2.add(new PrintfManager.PrintCommand("L", "S", "N", sobs, 1));
                                }
                                if (sespacos.equals("xx")) {
                                    commands.add(new PrintfManager.PrintCommand("L", "S", "N", "", 4));
                                } else {
                                    int iesp = Integer.parseInt(sespacos);
                                    commands2.add(new PrintfManager.PrintCommand("L", "S", "N", "", iesp));
                                }
                                printfManager.printBufferedText(commands2);

                            }



                } catch(Exception e){
                    System.out.println("Erro ao executar impressao : " + e.toString());
                    Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
        }

    }

    private void setStyles(int iSize, int iType) {
        /*alignLeft.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        alignLeft.put(PrinterAttributes.KEY_TYPEFACE, iType);
        alignLeft.put(PrinterAttributes.KEY_TEXT_SIZE, iSize);

        alignCenter.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        alignCenter.put(PrinterAttributes.KEY_TYPEFACE, iType);
        alignCenter.put(PrinterAttributes.KEY_TEXT_SIZE, iSize);

        alignRight.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        alignRight.put(PrinterAttributes.KEY_TYPEFACE, iType);
        alignRight.put(PrinterAttributes.KEY_TEXT_SIZE, iSize);
        //Alinhado a Esquerda
        //Fonte 1
        //Fonte 1 Alinnhado a Esquerda Tamanho 20
        font_L120.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L120.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_L120.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 1 Alinnhado a Esquerda Tamanho 22
        font_L122.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L122.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_L122.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 1 Alinnhado a Esquerda Tamanho 24
        font_L124.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L124.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_L124.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 1 Alinnhado a Esquerda Tamanho 26
        font_L126.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L126.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_L126.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 1 Alinnhado a Esquerda Tamanho 28
        font_L128.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L128.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_L128.put(PrinterAttributes.KEY_TEXT_SIZE, 28);
        //Fonte 2
        //Fonte 2 Alinnhado a Esquerda Tamanho 20
        font_L220.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L220.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_L220.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 2 Alinnhado a Esquerda Tamanho 22
        font_L222.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L222.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_L222.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 2 Alinnhado a Esquerda Tamanho 24
        font_L224.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L224.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_L224.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 2 Alinnhado a Esquerda Tamanho 26
        font_L226.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L226.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_L226.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 2 Alinnhado a Esquerda Tamanho 28
        font_L228.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_LEFT);
        font_L228.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_L228.put(PrinterAttributes.KEY_TEXT_SIZE, 28);
        //Alinhado Centralizado
        //Fonte 1
        //Fonte 1 Alinnhado Centralizado Tamanho 16
        font_C116.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C116.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C116.put(PrinterAttributes.KEY_TEXT_SIZE, 16);
        //Fonte 1 Alinnhado Centralizado Tamanho 18
        font_C118.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C118.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C118.put(PrinterAttributes.KEY_TEXT_SIZE, 18);
        //Fonte 1 Alinnhado Centralizado Tamanho 20
        font_C120.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C120.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C120.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 1 Alinnhado Centralizado Tamanho 22
        font_C122.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C122.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C122.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 1 Alinnhado Centralizado Tamanho 24
        font_C124.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C124.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C124.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 1 Alinnhado Centralizado Tamanho 26
        font_C126.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C126.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C126.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 1 Alinnhado Centralizado Tamanho 28
        font_C128.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C128.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_C128.put(PrinterAttributes.KEY_TEXT_SIZE, 28);
        //Fonte 2
        //Fonte 2 Alinnhado Centralizado Tamanho 20
        font_C220.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C220.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_C220.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 2 Alinnhado Centralizado Tamanho 22
        font_C222.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C222.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_C222.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 2 Alinnhado Centralizado Tamanho 24
        font_C224.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C224.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_C224.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 2 Alinnhado Centralizado Tamanho 26
        font_C226.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C226.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_C226.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 2 Alinnhado Centralizado Tamanho 28
        font_C228.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_CENTER);
        font_C228.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_C228.put(PrinterAttributes.KEY_TEXT_SIZE, 28);
        //Alinhado a Direita
        //Fonte 1
        //Fonte 1 Alinnhado a Direita Tamanho 20
        font_R120.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R120.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_R120.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 1 Alinnhado a Direita Tamanho 22
        font_R122.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R122.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_R122.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 1 Alinnhado a Direita Tamanho 24
        font_R124.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R124.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_R124.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 1 Alinnhado a Direita Tamanho 26
        font_R126.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R126.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_R126.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 1 Alinnhado a Direita Tamanho 28
        font_R128.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R128.put(PrinterAttributes.KEY_TYPEFACE, 1);
        font_R128.put(PrinterAttributes.KEY_TEXT_SIZE, 28);
        //Fonte 2
        //Fonte 2 Alinnhado a Direita Tamanho 20
        font_R220.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R220.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_R220.put(PrinterAttributes.KEY_TEXT_SIZE, 20);
        //Fonte 2 Alinnhado a Direita Tamanho 22
        font_R222.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R222.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_R222.put(PrinterAttributes.KEY_TEXT_SIZE, 22);
        //Fonte 2 Alinnhado a Direita Tamanho 24
        font_R224.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R224.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_R224.put(PrinterAttributes.KEY_TEXT_SIZE, 24);
        //Fonte 2 Alinnhado a Direita Tamanho 26
        font_R226.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R226.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_R226.put(PrinterAttributes.KEY_TEXT_SIZE, 26);
        //Fonte 2 Alinnhado a Direita Tamanho 28
        font_R228.put(PrinterAttributes.KEY_ALIGN, PrinterAttributes.VAL_ALIGN_RIGHT);
        font_R228.put(PrinterAttributes.KEY_TYPEFACE, 3);
        font_R228.put(PrinterAttributes.KEY_TEXT_SIZE, 28);*/

    }


    public void Chama_Reimpressao(String snum){
        DB_EMP dbemp = new DB_EMP(ViaActivity.this);
        String sqtd = dbemp.Busca_Dados_Emp(1, "Maximp");
        String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
        int iqtd = Integer.parseInt(sqtd);
        if (iqtd > 0) {
            DB_BPE openbpe = new DB_BPE(this);
            String sqtdimp = openbpe.Busca_Dados_Bpe(snum, "Qtdimp");
            int iqtdimp = Integer.parseInt(sqtdimp);
            if (iqtdimp < iqtd) {
                String schave = openbpe.Busca_Dados_Bpe(snum, "Chvbpe");
                Guarda_Texto = schave;
                if (smodimp.equals("01")) {
                    Imprime_BPe(); //SUNMI
                } else if (smodimp.equals("02")) {
                    Imprime_BPe_Lio(); //CIELO LIO
                    GuardaPagto = "";
                    GuardaBand = "";
                    GuardaAut = "";
                    GuardaID = "";
                } else if (smodimp.equals("03")) {
                    Imprime_BPe_BT(); //ARNY BLUETOOTH
                } else if (smodimp.equals("05")) {
                    //BLUETOOTH DTS-2500
                    Imprime_BPe_DTS();
                    //testPrint();
                }
            } else {
                Toast.makeText(ViaActivity.this, "Reimpressão não permitida.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ViaActivity.this, "Reimpressão não permitida.", Toast.LENGTH_LONG).show();
        }

        Menu_Lateral();
    }


    public void Chama_Cancela(String snum, String smotivo){
        DB_BPE openbpe = new DB_BPE(this);
        String schave = openbpe.Busca_Dados_Bpe(snum, "Chvbpe");
        String ssit = openbpe.Busca_Dados_Bpe(snum, "Sitbpe");
        String sserie = schave.substring(22, 25);
        sserie = Integer.toString(Integer.parseInt(sserie));
        if (ssit.equals("BA")) { //autorizado
            SerieWS = sserie;
            Nome_Arquivo = schave;
            WScomando = "CANCELA";
            ExecutBackgrund();
            //ViaActivity.Teste_Conexao testecanc = new ViaActivity.Teste_Conexao();
            //testecanc.execute();
        } else if (ssit.equals("CT")) { //Contingencia
            String sIDBpe = openbpe.Busca_Dados_Bpe(snum, "ID");
            openbpe.Atualizar_Campo_Bpe(sIDBpe, "Rsv001", "S");
            openbpe.Atualizar_Campo_Bpe(sIDBpe, "Rsv003", SmotivoWS);
            DB_EMP dbemp = new DB_EMP(ViaActivity.this);
            String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
            if (smodimp.equals("01")) {
                Imprime_Cancel(snum); //SUNMI
            } else if (smodimp.equals("02")) {
                Imprime_Cancel_LIO(snum); //CIELO LIO
            } else if (smodimp.equals("03")) {
                Imprime_Cancel_BT(snum); //ARNY
            } else if (smodimp.equals("05")) {
                //BLUETOOTH DTS-2500
                Imprime_BPe_DTS();
            }
            infMensagens("Bilhete Cancelado com Sucesso!", "");

        }

        Menu_Lateral();
    }



    public void Chama_Copia(String snum){
        DB_BPE openbpe = new DB_BPE(this);
        String sViagem = openbpe.Busca_Dados_Bpe(snum, "Nomvia");
        String sOrigem = openbpe.Busca_Dados_Bpe(snum, "Treori");
        String sDestino = openbpe.Busca_Dados_Bpe(snum, "Tredes");
        String sValor = openbpe.Busca_Dados_Bpe(snum, "Vlrpas");

        EditText edtVia = findViewById(R.id.edtVia);
        EditText edtOrigem = findViewById(R.id.edtOrigem);
        EditText edtDestino = findViewById(R.id.edtDestino);
        TextView txtValor = findViewById(R.id.txtValor);
        EditText edtCad = findViewById(R.id.edtPoltrona);
        final Button btnFinalizar = findViewById(R.id.btnFinalizar);


        edtVia.setText(sViagem);
        edtOrigem.setText(sOrigem);
        edtDestino.setText(sDestino);
        txtValor.setText(sValor);

        edtCad.setText("");
        edtCad.requestFocus();



        Calcula_Valor_DB();
        Menu_Lateral();
        btnFinalizar.setEnabled(true);


    }

    public void deleta_Bilhete(String snum) {
        String susuario = Nome_user;
        if (susuario.equals("HMINFO")) {
            DB_BPE dbbilhete = new DB_BPE(ViaActivity.this);
            int inumero = Integer.parseInt(snum);
            //System.out.println("Bilhete: "+inumero );

            String sid = dbbilhete.Busca_Dados_Bpe(Integer.toString(inumero), "ID");
            //System.out.println("ID Bilhete: "+sid );

            dbbilhete.delete_item_Bpe(sid);
            CarregarBilhetesCadastrados();
        }
    }






    String Criar_BPE(String svia, String sori, String sdes, String sChamada){
       String numbpe = "";
        int iID = 1;
        String sGrava = "";
        String sExiste = "";

       DB_EMP dbe = new DB_EMP(this);
       String smod = dbe.Busca_Dados_Emp(iID, "Modelo");
       String sser = dbe.Busca_Dados_Emp(iID, "Serie");
       String sultimo = dbe.Busca_Dados_Emp(iID, "Ultbil");
       /////Verificar se o bilhete anterior foi concluído
        if (!sultimo.equals("0")) {
            DB_BPE dbconferebil = new DB_BPE(ViaActivity.this);
            Bilhetes = dbconferebil.VerificaBil(sultimo);
            if(Bilhetes.size() > 0) {
                sExiste = "S";
            }
            if (sExiste.equals("S")){
                //currentModel = Bilhetes.get(elemento_id);
                String sSit = dbconferebil.Busca_Dados_Bpe(sultimo, "Sitbpe");
                if (sSit.equals("DG")) { //bilhete nao foi finalizado, aproveitar numeracao
                    String IDBPEANT = dbconferebil.Busca_Dados_Bpe(sultimo, "ID");
                    String modser = (smod+("000" + sser).substring(sser.length()));
                    numbpe = sultimo;
                    dbconferebil.Atualizar_Bpe(IDBPEANT, modser, numbpe, "", "", "DG", Nome_user, "N", 0.00, svia, sori, sdes, "0", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

                } else if (!sSit.equals("DG")) { //bilhete está em outra situação ou ocorreu um erro com o anterior
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
            numbpe = Integer.toString(numbil);
            String modser = (smod + ("000" + sser).substring(sser.length()));

            DB_BPE newbpe = new DB_BPE(this);
            newbpe.InserirBpe(modser, numbpe, "", "", "DG", Nome_user, "N", 0.00, svia, sori, sdes,  "0", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            System.out.println("Passei Inserir");
            dbe.Atualizar_Campo_Emp(Integer.toString(iID), "Ultbil", numbpe);
        }

       return numbpe;
    }


    String Gerar_DVBPe(String chaveSemDigito) throws InputMismatchException {

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


    public void Abre_Consulta(){
        Activity_Dados = 11;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(ViaActivity.this, ConsultaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("Activity_Dados", "11");
        myIntent.putExtras(bundle);

        // chama esse intent e aguarda resultado
        startForresult.launch(myIntent);

    }

    public void Abre_Parametros(){
        Activity_Dados = 15;
        // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
        Intent myIntent = new Intent(ViaActivity.this, ParametrosActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("USUARIO", Nome_user);
        bundle.putString("Activity_Dados", "15");
        startForresult.launch(myIntent);

    }

    public void Chama_Encerrar() {
        Intent intent = new Intent(this, EncerraActivity.class);
        startActivity(intent);

    }

    public void Chama_Verifica(String schamada) {
        ExecutBackgrund();
        boolean bconect = EstouConectado;
        if (bconect) {
            iPendentes = 0;
            ///Procedimentos para Verificar pendentes
            String sret = Verifica_Pendentes();
            if (!sret.equals("")) {
                String smensagem = "O Sistema identificou " + sret + "\nDeseja efetuar a transmissão agora?";
                String aviso = infAviso(smensagem);
                if (!aviso.equals("")) {
                    infMensagens(aviso, "");
                }
            } else {
                String smensagem = "Nao encontrou registros pendentes.";
                //somente mostra a mensagem se foi solicitade apesquisa
                if (!schamada.equals("E")) {
                    infMensagens(smensagem, "");
                }
            }
        }
    }

    //MÉTODO QUE CONSULTA OS BILHETES CADASTRADOS
    //exibe em uma caixa lateral
    protected  void CarregarBilhetesCadastrados(){

        DB_BPE dbbpe =  new DB_BPE(this);
        //BUSCA BILHETES CADASTRADOS
        List<BilhetesModel> bilhetes = dbbpe.SelecionarTodos();

        //SETA O ADAPTER DA LISTA COM OS REGISTROS RETORNADOS DA BASE
        listViewBPe.setAdapter(new Bpe_Consulta(this, bilhetes));
    }


    private void ExecutBackgrundTESTE() {
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
                DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                String surl = dbemp.Busca_Dados_Emp(1, "Endews");
                Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                Activity_Dados = 17;
                String txt = Nome_Arquivo;
                Bundle bundle = new Bundle();

                bundle.putString("URLWS", surl);
                bundle.putString("USUARIO", Nome_user);
                bundle.putString("COMANDO", "CONEXAO");
                bundle.putString("Activity_Dados", "17");

                myIntent.putExtras(bundle);
                synchronized (msgBuilder) {
                    msgBuilder.append("").append("\n");
                    System.out.println("Lançando WSActivity Verificar Conexao: 01");

                    runOnUiThread(() -> {
                        resultadoActivity.launch(myIntent);
                    });
                    //System.out.println("Retorno do WS: "+EstouConectado);


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

            /*System.out.println("1");//
            int resultado = minhaFuncaoDemorada();
            if (resultado == 5) {
                System.out.println(resultado);// Aguarda a execução antes de imprimir
                System.out.println("3");//
            } else if (resultado >= 6){
                System.out.println("Nao Encontrado");//
            }*/
    }

    public int minhaFuncaoDemorada(){
        System.out.println("2");//
        //sleep(2000);  // Simulando um processamento demorado
        int ival = 0;
        for (int ie = 0; ie < 25; ie++) {
            sleep(500);
            boolean bcon = Funcoes_Android.Verifica_Conexao(ViaActivity.this);
            System.out.println("Conectado? "+bcon);//
            ival = ie;
            System.out.println(ie);//
        }
        return ival;
    }

    private void ExecutBackgrund() {
                // Este passo é usado para configurar a tarefa, por exemplo, mostrando uma barra de progresso na interface do usuário.
                ImageView imageView = findViewById(R.id.animacao);
                imageView.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.drawable.aguarde);
                Button btnvia = findViewById(R.id.btnVia);
                Button btnori = findViewById(R.id.btnOri);
                Button btndes = findViewById(R.id.btnDes);
                Button btnfin = findViewById(R.id.btnFinalizar);
                btnvia.setEnabled(false);
                btnori.setEnabled(false);
                btndes.setEnabled(false);
                btnfin.setEnabled(false);
                boolean bconect = EstouConectado;
                 String scomandoatu = "";


                AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
                animation.start();

                //doInBackGround method of AsyncTask (O que ira executar em segundo plano)
                DB_EMP dbemp = new DB_EMP(ViaActivity.this);
                String sentrouCTG = dbemp.Busca_Dados_Emp(1, "Datctg");
                if (!sentrouCTG.equals("")) {
                    String shorini = sentrouCTG.substring(11, 16); //achar a hora de contingÃªncia
                    String sdatcont = sentrouCTG.substring(0, 10); //data que entrou em contingencia
                    String sdatatu = Funcoes_Android.getCurrentUTC();
                    String sdatatual = sdatatu.substring(0, 10);
                    String shorfin = sdatatu.substring(11, 16); //hora atual
                    //verificar se a hora atual esta no intervalo de 5 min
                    long intervalo = Funcoes_Android.validaDate(shorini, shorfin);
                    System.out.println("Intervalo: "+intervalo+" "+sdatatual+ " "+sdatcont);
                    if (intervalo > 0 || !sdatatual.equals(sdatcont)) { //ja passou do intervalo de 1 minuto ou ja e outra data
                        String surl = dbemp.Busca_Dados_Emp(1, "Endews");
                        Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                        Activity_Dados = 17;
                        String txt = Nome_Arquivo;
                        Bundle bundle = new Bundle();

                        bundle.putString("URLWS", surl);
                        bundle.putString("USUARIO", Nome_user);
                        bundle.putString("COMANDO", "CONEXAO");
                        bundle.putString("Activity_Dados", "17");

                        myIntent.putExtras(bundle);
                        synchronized (msgBuilder) {
                            msgBuilder.append("").append("\n");
                            System.out.println("Lançando WSActivity Verificar Conexao: 01");

                            runOnUiThread(() -> {
                                startForresult.launch(myIntent);
                            });
                            bconect = EstouConectado;
                            System.out.println("onPostExecute: "+bconect);
                            scomandoatu = WScomando;
                        }

                    }
                } else {
                    String surl = dbemp.Busca_Dados_Emp(1, "Endews");
                    Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                    Activity_Dados = 17;
                    String txt = Nome_Arquivo;
                    Bundle bundle = new Bundle();

                    bundle.putString("URLWS", surl);
                    bundle.putString("USUARIO", Nome_user);
                    bundle.putString("COMANDO", "CONEXAO");
                    bundle.putString("Activity_Dados", "17");

                    myIntent.putExtras(bundle);
                    synchronized (msgBuilder) {
                        msgBuilder.append("").append("\n");
                        System.out.println("Lançando WSActivity Verificar Conexao: 02");

                        runOnUiThread(() -> {
                            startForresult.launch(myIntent);
                        });
                        // O resultado da execução em background é passado para este passo como um parâmetro.
                        bconect = EstouConectado;
                        System.out.println("onPostExecute: "+bconect);
                        scomandoatu = WScomando;
                    }

                }





                            dbemp = new DB_EMP(ViaActivity.this);
                            String sultdatven = dbemp.Busca_Dados_Emp(1, "Ultdat");
                            if (!sultdatven.equals("")){
                                String sdatatu = Funcoes_Android.getCurrentUTC();
                                String sanoa = sdatatu.substring(0, 4);
                                String smesa = sdatatu.substring(5,7);
                                String sdiaa = sdatatu.substring(8, 10);
                                sdatatu = sdiaa+"/"+smesa+"/"+sanoa;
                                //System.out.println("Data Atual: "+sdatatu);

                                String sanou = sultdatven.substring(0, 4);
                                String smesu = sultdatven.substring(5, 7);
                                String sdiau = sultdatven.substring(8, 10);
                                sultdatven = sdiau+"/"+smesu+"/"+sanou;
                                //System.out.println("Ultima Venda: "+sultdatven);
                                boolean bdata = Funcoes_Android.Verifica_Datas(sdatatu, sultdatven);
                                //System.out.println("Valida Data: "+bdata);
                                if (!bdata) { //data invalida
                                    infMensagens("A Data do Dispositivo está incorreta."+sdatatu+"\nTente Reiniciar o Dispositivo.\nCaso o erro persista, contacte o suporte.", "S");
                                }

                            }

                        if (scomandoatu.equals("TRANSMITE")) {
                           /*System.out.println("Ex Back TRANSMITE Activity_Dados: "+Activity_Dados);
                            String sdataCT = "";
                            dbemp = new DB_EMP(ViaActivity.this);
                            String sCTG = dbemp.Busca_Dados_Emp(1, "Tipemi");
                            if (sCTG.equals("1")) { //nao esta em contingencia, verificar conexao
                                if (bconect == false) {//sem conexao, entrar em contingencia
                                    sdataCT = Funcoes_Android.getCurrentUTC();
                                    dbemp.Atualizar_Campo_Emp("1", "Tipemi", "2");
                                    dbemp.Atualizar_Campo_Emp("1", "Datctg", sdataCT);
                                }

                            } else if (sCTG.equals("2")) { //Esta em contingencia, verificar se a conexao voltou
                                if (bconect == true) {//voltou conexao, entrar em contingencia
                                    dbemp.Atualizar_Campo_Emp("1", "Tipemi", "1");
                                    dbemp.Atualizar_Campo_Emp("1", "Datctg", "");
                                }
                            }
                            //if (bconect){Activity_Dados = 12;}
                            Gerar_XML("N");
                            System.out.println("Gerei XML Activity_Dados: "+Activity_Dados);*/
                        } else if (scomandoatu.equals("CANCELA")) {
                            if (bconect == true) { //conexao ok
                                Activity_Dados = 13;
                                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                                Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);

                                String txt = Nome_Arquivo;
                                Bundle bundle = new Bundle();

                                bundle.putString("XML", txt);
                                bundle.putString("USUARIO", Nome_user);

                                bundle.putString("LINVIA",  LinviaWS);
                                bundle.putString("DATVIA",  DatviaWS);
                                bundle.putString("NUMCAD",  NumcadWS);
                                bundle.putString("TREORI",  TreoriWS);
                                bundle.putString("TREDES",  TredesWS);
                                bundle.putString("VLRTAR",  VlrtarWS);
                                bundle.putString("VLRSEG",  VlrsegWS);
                                bundle.putString("VLRARRE", VlrarreWS);
                                bundle.putString("SERIE",   SerieWS);
                                bundle.putString("DATEMI",  DatemiWS);
                                bundle.putString("VEICULO", VeiculoWS);
                                bundle.putString("PAGAMENTO", PagWS);
                                bundle.putString("TIPVIA",   TipviaWS);
                                bundle.putString("COMANDO",  WScomando);
                                bundle.putString("MOTIVO",  SmotivoWS);
                                bundle.putString("CANCEL", "");
                                bundle.putString("Activity_Dados", "13");




                                myIntent.putExtras(bundle);

                                // chama esse intent e aguarda resultado
                                Future<String> future = executor.submit(() -> {
                                    if (!isFinishing()) {
                                        System.out.println("Lançando WSActivity Cancelar Bilhete: ");
                                        runOnUiThread(() -> startForresult.launch(myIntent));
                                        // Aguarda até que o resultado seja processado (controlado pelo futuro)
                                        try {
                                            Thread.sleep(1000); // Timeout de 1 segundo como fallback
                                            System.out.println("Timeout atingido para Cancelar Bilheteo: ");
                                            return "Timeout";
                                        } catch (InterruptedException e) {
                                            System.out.println("Interrompido enquanto esperava WSActivity: " + e.toString());
                                            return "Erro: " + e.getMessage();
                                        }
                                    } else {
                                        System.out.println("Activity finalizada, abortando chamada a WSActivity");
                                        return "Activity Finalizada";
                                    }
                                });

                                try {
                                    String resultado = future.get(15, TimeUnit.SECONDS); // Espera com timeout
                                    System.out.println("Consulta Conexao concluído com resultado: " + resultado);
                                } catch (Exception e) {
                                    System.out.println("Erro ao Cancelar Bilhete " +  ": " + e.toString());
                                }
                            } else {
                                String inf = infMensagens("Sem Conexão para Cancelar!\nO Bilhete não será cancelado.", "");
                            }

                        } else if (scomandoatu.equals("verificaUsuarioGratuidade")) {
                            if (bconect == true) { //conexao ok
                                Activity_Dados = 21;
                                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                                Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                                WScomando = scomandoatu;
                                Bundle bundle = new Bundle();

                                bundle.putString("USUARIO", Nome_user);
                                bundle.putString("CANCEL", "");
                                bundle.putString("COMANDO", WScomando);
                                bundle.putString("CPFUSR", CPFUSR);
                                bundle.putString("Activity_Dados", "21");

                                myIntent.putExtras(bundle);

                                myIntent.putExtras(bundle);
                                synchronized (msgBuilder) {
                                    msgBuilder.append("").append("\n");
                                    System.out.println("Lançando verificaUsuarioGratuidade: ");

                                    runOnUiThread(() -> {
                                        startForresult.launch(myIntent);
                                    });
                                }
                            }
                        } else if (scomandoatu.equals("CONSULTA_GRATUIDADE")) {
                            if (bconect == true) { //conexao ok
                                Activity_Dados = 22;
                                // cria um Intent e diz que o pai sou eu mesmo e o filho é o TelaDois
                                Intent myIntent = new Intent(ViaActivity.this, WSActivity.class);
                                WScomando = scomandoatu;
                                Bundle bundle = new Bundle();

                                bundle.putString("USUARIO", Nome_user);
                                bundle.putString("CANCEL", "");
                                bundle.putString("COMANDO", WScomando);
                                bundle.putString("CPFUSR", CPFUSR);
                                bundle.putString("Activity_Dados", "22");

                                myIntent.putExtras(bundle);

                                myIntent.putExtras(bundle);
                                synchronized (msgBuilder) {
                                    msgBuilder.append("").append("\n");
                                    System.out.println("Lançando CONSULTA_GRATUIDADE: ");

                                    runOnUiThread(() -> {
                                        startForresult.launch(myIntent);
                                    });
                                }
                            }
                        }

                        animation.stop();
                        imageView.setVisibility(View.INVISIBLE);
                        btnvia.setEnabled(true);
                        btnori.setEnabled(true);
                        btndes.setEnabled(true);
                        btnfin.setEnabled(true);

        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }


}
