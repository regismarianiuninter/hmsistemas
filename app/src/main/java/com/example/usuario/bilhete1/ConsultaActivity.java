package com.example.usuario.bilhete1;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.utils.MemInfo;
import com.sunmi.utils.ThreadPoolManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

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

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static android.os.Environment.getExternalStorageDirectory;

public class ConsultaActivity extends AppCompatActivity {
    private ListView listViewBPe;
    private static final String TAG = "bilhete1";
    public static final int DO_PRINT = 0x10001;
    private IWoyouService woyouService;
    private byte[] inputCommand;
    private final int RUNNABLE_LENGHT = 2;
    private Random random = new Random();
    private boolean isBold, isUnderLine;
    private int record;
    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};
    private ICallback callback = null;
    private TextView info;
    Bitmap mBitmap;
    private static String Nome_user = "";
    private static int Activity_Dados = 11;

    private double NgDinheiro;
    private double NgOutros;


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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TEST) {
                // testAll();
                long mm = MemInfo.getmem_UNUSED(ConsultaActivity.this);
                if (mm < 100) {
                    handler.sendEmptyMessageDelayed(MSG_TEST, 20000);
                } else {
                    handler.sendEmptyMessageDelayed(MSG_TEST, 800);
                }
                Log.i(TAG, "testAll: " + printCount + " Memory: " + mm);
            }
        }
    };

    private void test() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {

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
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        record = 17;
        isBold = false;
        isUnderLine = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel);

        Intent Newintent = getIntent();
        Bundle bundle = Newintent.getExtras();
        String user = bundle.getString("USUARIO");
        Nome_user = user;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        EditText edtuser = findViewById(R.id.edtUsrvenda);
        edtuser.setText(Nome_user);


        //Limpar Base temporaria
        DB_TMP RELTMP = new DB_TMP(ConsultaActivity.this);
        RELTMP.deletar_TMP();


        callback = new ICallback.Stub() {

            @Override
            public void onRunResult(final boolean success) throws RemoteException {
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG, "printlength:" + value + "\n");
            }

            @Override
            public void onRaiseException(int code, final String msg) throws RemoteException {
                Log.i(TAG, "onRaiseException: " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info.append("onRaiseException = " + msg + "\n");
                    }
                });

            }
        };

        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);

        // setStyles(18,0);


        Button btnimp = findViewById(R.id.btnImprel);
        btnimp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cklist = findViewById(R.id.ckbListar);
                TextView txtaguarde = findViewById(R.id.txtAguarde);
                DB_EMP dbemp = new DB_EMP(ConsultaActivity.this);
                String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
                if (smodimp.equals("01")) { //SUNMI
                    if (cklist.isChecked()) {
                        txtaguarde.setText("Aguarde...");
                        Listar_Vendas();
                        txtaguarde.setText("");
                    } else {
                        txtaguarde.setText("Aguarde...");
                        Listar_Vendas_Agrupado();
                        txtaguarde.setText("");
                    }
                }
                if (smodimp.equals("02")) {
                    txtaguarde.setText("Aguarde...");
                    txtaguarde.setText("");
                }
                if (smodimp.equals("03")) { //SUNMI
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter.isEnabled()) {
                        if (cklist.isChecked()) {
                            txtaguarde.setText("Aguarde...");

                            Listar_Vendas_BT();

                            txtaguarde.setText("");
                        } else {
                            txtaguarde.setText("Aguarde...");
                            Listar_Vendas_Agrupado_BT();
                            txtaguarde.setText("");
                        }
                    } else {
                        Toast.makeText(ConsultaActivity.this, "Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        DB_USR dbusr = new DB_USR(ConsultaActivity.this);
        String sfectur = dbusr.Busca_Dados_Usr(Nome_user, "Fectur");
        CheckBox ckfecha = findViewById(R.id.ckbFectur);
        if (sfectur.equals("S")) {
            ckfecha.setEnabled(true);
        } else {
            ckfecha.setEnabled(false);
        }
        ckfecha.setChecked(false);

        CheckBox ckbger = findViewById(R.id.ckbGeral);
        if (Nome_user.equals("HMINFO")) {
            ckbger.setEnabled(true);
        } else {
            ckbger.setEnabled(false);
        }
        ckbger.setChecked(false);

    }


    public void Listar_Vendas() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {

            @Override
            public void run() {
                if (mBitmap == null) {
                    mBitmap = BitmapFactory.decodeFile(getExternalFilesDir("Download").getAbsolutePath() + "/logoemp.jpg");
                    //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
                }
                try {
                    try {
                        float size = 30;
                        String sdia, smes, sano, sdatemi;
                        String sviagem = "";
                        String snomeuser = "";
                        double vlrtot, vlrbil, vlrsub, vlrusr, vlrdig, vlrdia, vlremb, totalemb, totliq;
                        vlrtot = 0;
                        vlrsub = 0;
                        vlrusr = 0;
                        vlrdig = 0;
                        vlrdia = 0;
                        totalemb = 0;
                        vlremb = 0;
                        totliq = 0;
                        Integer iqtdbil = 0;
                        Integer iqtdsub = 0;
                        Integer iqtdusr = 0;
                        Integer iqtdcan = 0;
                        Integer iqtddig = 0;
                        Integer iqtdpen = 0;
                        Integer iqtddia = 0;
                        String sDatcaixa = "";

                        // int iret = XmlPontos("C", 0, "Origem", "Destino");
                        //iret = XmlPontos("A", 0, "Origem", "Destino");
                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        String sespacos = dbemp.Busca_Dados_Emp(1, "Rsv003");

                        CheckBox cklist = findViewById(R.id.ckbListar);
                        CheckBox ckfecha = findViewById(R.id.ckbFectur);

                        String sdathor = Funcoes_Android.getCurrentUTC();
                        sano = sdathor.substring(0, (4));
                        smes = sdathor.substring(5, (7));
                        sdia = sdathor.substring(8, (10));
                        sdatemi = sdia + "/" + smes + "/" + sano;
                        EditText edtuser = findViewById(R.id.edtUsrvenda);
                        String susuario = edtuser.getText().toString();
                        woyouService.setAlignment(1, callback);//alinhamento
                        woyouService.printBitmap(mBitmap, callback);
                        woyouService.lineWrap(1, null);

                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                        woyouService.setAlignment(1, callback); //Alinhamento
                        //0 - Alinhar a Esquerda
                        //1 - Centralizado
                        //2 - Alinhado a Direita
                        woyouService.printTextWithFont("Data de Impressão: " + sdatemi, "", 24, null);
                        woyouService.lineWrap(1, null);
                        // if (!susuario.equals("")) {
                        //    woyouService.printTextWithFont("USUARIO: " + susuario, "", 24, null);
                        //    woyouService.lineWrap(1, null);
                        // }
                        //woyouService.printTextWithFont("_________________", "", 24,null);
                        //woyouService.lineWrap(1, null);

                        DB_BPE db = new DB_BPE(ConsultaActivity.this);
                        DB_BPE.BpeCursor cursor = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

                        for (int ibp = 0; ibp < cursor.getCount(); ibp++) {
                            cursor.moveToPosition(ibp);
                            String sdatven = cursor.getDatemi();
                            String sdatsai = sdatven.substring(0, (10));
                            String sagente = cursor.getAgente();
                            String sfecha = cursor.getTippas();
                            String stransf = cursor.getTransf();
                            if (!stransf.equals("S")) {
                                iqtdpen = iqtdpen + 1;
                            }
                            if (!sfecha.equals("S")) { //se ainda nao fechou turno
                                String sSitbpe = cursor.getSitbpe();

                                String scancel = cursor.getRsv001(); //se marcou para cancelar

                                System.out.println("N: " + cursor.getNumbpe() + "Sit: " + sSitbpe + " Cancel: " + scancel);
                                if ((sSitbpe.equals("BA") && !scancel.equals("S")) || (sSitbpe.equals("CT") && !scancel.equals("S"))) { //Autorizados e contingencia


                                    String slinha = cursor.getNomvia();
                                    if (!slinha.equals(sviagem)) {
                                        woyouService.setAlignment(0, callback); //Alinhamento


                                        if (iqtdsub > 0) { //imprimir subtotal

                                            String subtotal = String.format("%.2f", vlrsub);
                                            subtotal = subtotal.replace(".", ",");


                                            String sespaco = "";
                                            String sqtd = "Bilhetes da Viagem: " + iqtdsub;
                                            Integer iqtd = sqtd.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            woyouService.printTextWithFont("Bilhetes da Viagem: " + sespaco + iqtdsub, "", 22, null);
                                            woyouService.lineWrap(1, null);

                                            sespaco = "";
                                            String svalor = "Total da Viagem: " + subtotal;
                                            iqtd = svalor.length();
                                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                Integer iresto = (32 - iqtd);
                                                while (sespaco.length() < iresto) {
                                                    sespaco = " " + sespaco;
                                                }
                                            }
                                            woyouService.printTextWithFont("Total da Viagem: " + sespaco + subtotal, "", 22, null);
                                            woyouService.lineWrap(2, null);


                                            vlrsub = 0;
                                            iqtdsub = 0;
                                        } else {
                                            woyouService.lineWrap(1, null);
                                        }

                                        if (!sdatsai.equals(sDatcaixa)) { //se a data for diferente
                                            woyouService.setAlignment(0, callback); //Alinhamento
                                            if (iqtddia > 0) {
                                                String subtot = String.format("%.2f", vlrdia);
                                                subtot = subtot.replace(".", ",");


                                                String sespaco = "";
                                                String sqtd = "Bilhetes da Data: " + iqtddia;
                                                Integer iqtd = sqtd.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32 - iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }
                                                woyouService.printTextWithFont("Bilhetes da Data: " + sespaco + iqtddia, "", 24, null);
                                                woyouService.lineWrap(1, null);

                                                sespaco = "";
                                                String svalor = "Total da Data: " + subtot;
                                                iqtd = svalor.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32 - iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }
                                                woyouService.printTextWithFont("Total da Data: " + sespaco + subtot, "", 24, null);
                                                woyouService.lineWrap(2, null);


                                                vlrdia = 0;
                                                iqtddia = 0;
                                            } else {
                                                woyouService.lineWrap(1, null);
                                            }
                                            String sdiad, smesd, sanod, sdata;
                                            sanod = sdatsai.substring(0, (4));
                                            smesd = sdatsai.substring(5, (7));
                                            sdiad = sdatsai.substring(8, (10));
                                            sdata = sdiad + "/" + smesd + "/" + sanod;
                                            woyouService.printTextWithFont("Data da Venda: " + sdata, "", 24, null);
                                            woyouService.lineWrap(1, null);
                                            sDatcaixa = sdatsai;


                                        } //if (!sdatsai.equals(sDatcaixa)) { //se a data for diferente

                                        String snome = cursor.getAgente();
                                        if (!snome.equals(snomeuser)) {
                                            if (iqtdusr > 0) { //subtotal do Usuario

                                                String usrtotal = String.format("%.2f", vlrusr);
                                                usrtotal = usrtotal.replace(".", ",");


                                                String sespaco = "";
                                                String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                                                Integer iqtd = sqtd.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32 - iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }
                                                woyouService.printTextWithFont("Bilhetes do Usuario: " + sespaco + iqtdusr, "", 22, null);
                                                woyouService.lineWrap(1, null);

                                                sespaco = "";
                                                String svalor = "Total do Usuario: " + usrtotal;
                                                iqtd = svalor.length();
                                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                                    Integer iresto = (32 - iqtd);
                                                    while (sespaco.length() < iresto) {
                                                        sespaco = " " + sespaco;
                                                    }
                                                }
                                                woyouService.printTextWithFont("Total do Usuario: " + sespaco + usrtotal, "", 22, null);
                                                woyouService.lineWrap(2, null);


                                                vlrusr = 0;
                                                iqtdusr = 0;
                                            }


                                            woyouService.setAlignment(0, callback); //Alinhamento
                                            woyouService.lineWrap(1, null);
                                            woyouService.printTextWithFont("USUARIO: " + snome, "", 24, null);
                                            woyouService.lineWrap(1, null);

                                            snomeuser = snome;
                                        }


                                        int posV = slinha.indexOf("-");
                                        int iqtdv = slinha.length();
                                        String sVia = slinha.substring((posV + 1), (iqtdv));


                                        woyouService.printTextWithFont(sVia, "", 22, null);
                                        woyouService.lineWrap(1, null);

                                        sviagem = slinha;


                                    }
                                    System.out.println("Dentro do bpe");
                                    String sbilhete = cursor.getNumbpe();
                                    String sori = cursor.getTreori();
                                    String sdes = cursor.getTredes();
                                    String sval = cursor.getVlrpas();

                                    int posO = sori.indexOf("-");
                                    int iqtdo = sori.length();
                                    String sOri = sori.substring((posO + 1), (iqtdo));


                                    int posD = sdes.indexOf("-");
                                    int iqtdd = sdes.length();
                                    String sDes = sdes.substring((posD + 1), (iqtdd));


                                    String strecho = sOri + "x" + sDes;
                                    sval = sval.replace(",", ".");
                                    vlrbil = Double.valueOf(sval).doubleValue();
                                    vlrtot = vlrtot + vlrbil;
                                    vlrsub = vlrsub + vlrbil;
                                    vlrusr = vlrusr + vlrbil;
                                    vlrdia = vlrdia + vlrbil;
                                    sval = String.format("%.2f", (vlrbil));
                                    sval = sval.replace(".", ",");
                                    if (cklist.isChecked()) { //se marcou para imprimir todos os bilhetes
                                        woyouService.setAlignment(0, callback); //Alinhamento
                                        //0 - Alinhar a Esquerda
                                        //1 - Centralizado
                                        //2 - Alinhado a Direita

                                        woyouService.printTextWithFont(strecho, "", 18, null);
                                        woyouService.lineWrap(1, null);
                                        woyouService.setAlignment(2, callback); //Alinhamento
                                        String shorbil = sdatven.substring(11, (16));
                                        String sespaco = "";
                                        String svalor = "Bilhete: " + sbilhete + "  " + shorbil + "   " + sval;
                                        Integer iqtd = svalor.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32 - iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        woyouService.printTextWithFont("Bilhete: " + sbilhete + "  " + shorbil + "   " + sval, "", 22, null);
                                        woyouService.lineWrap(1, null);
                                    }
                                    iqtdsub = iqtdsub + 1;
                                    iqtdbil = iqtdbil + 1;
                                    iqtdusr = iqtdusr + 1;
                                    iqtddia = iqtddia + 1;


                                } else if (sSitbpe.equals("CA")) {
                                    iqtdcan = iqtdcan + 1;
                                } else if (sSitbpe.equals("DG")) {
                                    iqtddig = iqtddig + 1;
                                    String sval = cursor.getVlrpas();
                                    vlrbil = Double.valueOf(sval).doubleValue();
                                    vlrdig = vlrdig + vlrbil;

                                }

                                if (sSitbpe.equals("CT") && scancel.equals("S")) {
                                    iqtdcan = iqtdcan + 1;
                                }

                                if (sSitbpe.equals("BA") && scancel.equals("S")) {
                                    iqtdcan = iqtdcan + 1;
                                }

                                if (ckfecha.isChecked()) { ///marcar como fechado
                                    String snum = cursor.getNumbpe();
                                    String sid = db.Busca_Dados_Bpe(snum, "ID");
                                    db.Atualizar_Campo_Bpe(sid, "Tippas", "S");

                                }
                            }
                        }


                        if (iqtdsub > 0) { //imprimir subtotal da viagem
                            String subtotal = String.format("%.2f", vlrsub);
                            subtotal = subtotal.replace(".", ",");


                            String sespaco = "";
                            String sqtd = "Bilhetes da Viagem: " + iqtdsub;
                            Integer iqtd = sqtd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes da Viagem: " + sespaco + iqtdsub, "", 22, null);
                            woyouService.lineWrap(1, null);

                            sespaco = "";
                            String svalor = "Total da Viagem: " + subtotal;
                            iqtd = svalor.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Total da Viagem: " + sespaco + subtotal, "", 22, null);
                            woyouService.lineWrap(1, null);


                        }


                        if (iqtddia > 0) {
                            String subtot = String.format("%.2f", vlrdia);
                            subtot = subtot.replace(".", ",");


                            String sespaco = "";
                            String sqtd = "Bilhetes da Data: " + iqtddia;
                            Integer iqtd = sqtd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes da Data: " + sespaco + iqtddia, "", 24, null);
                            woyouService.lineWrap(1, null);

                            sespaco = "";
                            String svalor = "Total da Data: " + subtot;
                            iqtd = svalor.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Total da Data: " + sespaco + subtot, "", 24, null);
                            woyouService.lineWrap(2, null);

                        }

                        if (iqtdusr > 0) { //subtotal do Usuario
                            woyouService.lineWrap(1, null);
                            String usrtotal = String.format("%.2f", vlrusr);
                            usrtotal = usrtotal.replace(".", ",");


                            String sespaco = "";
                            String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                            Integer iqtd = sqtd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes do Usuario: " + sespaco + iqtdusr, "", 22, null);
                            woyouService.lineWrap(1, null);

                            sespaco = "";
                            String svalor = "Total do Usuario: " + usrtotal;
                            iqtd = svalor.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Total do Usuario: " + sespaco + usrtotal, "", 22, null);
                            woyouService.lineWrap(2, null);

                        }


                        woyouService.lineWrap(2, null);
                        String stotal = String.format("%.2f", vlrtot);
                        stotal = stotal.replace(".", ",");


                        String sespaco = "";
                        String sqtd = "Bilhetes Vendidos: " + iqtdbil;
                        Integer iqtd = sqtd.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Bilhetes Vendidos: " + sespaco + iqtdbil, "", 24, null);
                        woyouService.lineWrap(1, null);

                        sespaco = "";
                        String svalor = "Valor Total: " + stotal;
                        iqtd = svalor.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Valor Total: " + sespaco + stotal, "", 24, null);
                        woyouService.lineWrap(2, null);

                        String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
                        if (spvenda.equals("R")) { //rodiviaria calcular taxa de embarque
                            vlremb = Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
                            totalemb = iqtdbil * vlremb;
                            String staxa = String.format("%.2f", totalemb);
                            staxa = staxa.replace(".", ",");
                            sespaco = "";
                            String svlrtax = "Taxa de Embarque: " + staxa;
                            iqtd = svlrtax.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Taxa de Embarque: " + sespaco + staxa, "", 24, null);
                            woyouService.lineWrap(2, null);
                            totliq = vlrtot - totalemb;
                            String stotliq = String.format("%.2f", totliq);
                            stotliq = stotliq.replace(".", ",");
                            sespaco = "";
                            String stotalliq = "Tarifa + Seguro: " + stotliq;
                            iqtd = stotalliq.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Tarifa + Seguro: " + sespaco + stotliq, "", 24, null);
                            woyouService.lineWrap(2, null);

                        }


                        if (iqtdcan > 0) { //mostrar quantidade cancelada
                            woyouService.lineWrap(2, null);

                            String sespacocan = "";
                            String sqtdcan = "Bilhetes Cancelados: " + iqtdcan;
                            Integer iqtdc = sqtdcan.length();
                            if (iqtdc < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdc);
                                while (sespacocan.length() < iresto) {
                                    sespacocan = " " + sespacocan;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes Cancelados: " + sespacocan + iqtdcan, "", 24, null);
                            woyouService.lineWrap(2, null);


                        }

                        if (iqtddig > 0) {
                            woyouService.lineWrap(2, null);
                            String stotdig = String.format("%.2f", vlrdig);
                            stotdig = stotdig.replace(".", ",");


                            String sespacod = "";
                            String sqtdd = "Bilhetes em Aberto: " + iqtddig;
                            Integer iqtdd = sqtdd.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacod.length() < iresto) {
                                    sespacod = " " + sespacod;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes em Aberto: " + sespacod + iqtddig, "", 24, null);
                            woyouService.lineWrap(1, null);

                            sespacod = "";
                            String svalord = "Valor em Aberto: " + stotdig;
                            iqtdd = svalord.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacod.length() < iresto) {
                                    sespacod = " " + sespacod;
                                }
                            }
                            woyouService.printTextWithFont("Valor em Aberto: " + sespacod + stotdig, "", 24, null);
                            woyouService.lineWrap(2, null);
                        }

                        if (iqtdpen > 0) { //existe pendentes
                            String sespacop = "";
                            String sqtdp = "Pendentes de Transmissão: " + iqtdpen;
                            Integer iqtdp = sqtdp.length();
                            if (iqtdp < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdp);
                                while (sespacop.length() < iresto) {
                                    sespacop = " " + sespacop;
                                }
                            }
                            woyouService.printTextWithFont("Pendentes de Transmissão: " + sespacop + iqtdpen, "", 24, null);
                            woyouService.lineWrap(2, null);
                        }

                        if (sespacos.equals("xx")) {
                            woyouService.lineWrap(2, null);
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


    public void Listar_Vendas_Agrupado() {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {

            @Override
            public void run() {
                if (mBitmap == null) {
                    mBitmap = BitmapFactory.decodeFile(getExternalFilesDir("Download").getAbsolutePath() + "/logoemp.jpg");
                    //mBitmap = BitmapFactory.decodeResource(getResources(), R.raw.logoemp);
                }
                try {
                    try {
                        float size = 30;
                        String sdia, smes, sano, sdatemi;
                        String sviagem = "";
                        String snomeuser = "";
                        double vlrtot, vlrbil, vlrsub, vlrusr, vlrdig, vlrdia, vlremb, totalemb, totliq, nvlrpas, nvalor;
                        vlrtot = 0;
                        vlrsub = 0;
                        vlrusr = 0;
                        vlrdig = 0;
                        vlrdia = 0;
                        totalemb = 0;
                        vlremb = 0;
                        totliq = 0;
                        Integer iqtdbil = 0;
                        Integer iqtdsub = 0;
                        Integer iqtdusr = 0;
                        Integer iqtdcan = 0;
                        Integer iqtddig = 0;
                        Integer iqtdpen = 0;
                        Integer iqtddia = 0;
                        String sDatcaixa = "";

                        //Limpar Base temporaria
                        DB_TMP RELTMP = new DB_TMP(ConsultaActivity.this);
                        RELTMP.deletar_TMP();

                        //Dados a Empresa
                        DB_EMP dbemp = new DB_EMP(getApplicationContext());

                        CheckBox cklist = findViewById(R.id.ckbListar);
                        CheckBox ckfecha = findViewById(R.id.ckbFectur);

                        DB_BPE db = new DB_BPE(ConsultaActivity.this);
                        DB_BPE.BpeCursor cursor = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

                        for (int ibp = 0; ibp < cursor.getCount(); ibp++) {
                            cursor.moveToPosition(ibp);
                            String sdatven = cursor.getDatemi();
                            String sdatsai = sdatven.substring(0, (10));
                            String sagente = cursor.getAgente();
                            String sfecha = cursor.getTippas();
                            String stransf = cursor.getTransf();
                            if (!stransf.equals("S")) {
                                iqtdpen = iqtdpen + 1;
                            }
                            if (!sfecha.equals("S")) { //se ainda nao fechou turno
                                String sSitbpe = cursor.getSitbpe();

                                String scancel = cursor.getRsv001(); //se marcou para cancelar
                                if ((sSitbpe.equals("BA") && !scancel.equals("S")) || (sSitbpe.equals("CT") && !scancel.equals("S"))) { //Autorizados e contingencia
                                    String susr = cursor.getAgente();
                                    String sdat = cursor.getDatemi();
                                    String semissao = sdat.substring(0, 10);
                                    String svia = cursor.getNomvia();
                                    String vlrpas = cursor.getVlrpas();
                                    vlrpas = vlrpas.replace(",", ".");
                                    nvlrpas = Double.valueOf(vlrpas).doubleValue();
                                    String scodigo = susr + semissao + svia;
                                    DB_TMP.TmpCursor cursortmp = RELTMP.RetornarTmp(DB_TMP.TmpCursor.OrdenarPor.NomeCrescente);
                                    String sOK = "";
                                    for (int itmp = 0; itmp < cursortmp.getCount(); itmp++) {
                                        cursortmp.moveToPosition(itmp);

                                    }
                                    if (cursortmp.getCount() > 0) {
                                        sOK = "S";
                                    }


                                    String svai = "";
                                    if (sOK.equals("S")) {
                                        for (int itmp = 0; itmp < cursortmp.getCount(); itmp++) {
                                            cursortmp.moveToPosition(itmp);
                                            String scodtmp = cursortmp.getCodigo();
                                            if (scodigo.equals(scodtmp)) {
                                                svai = "S";

                                            }
                                        }
                                    }

                                    if (svai.equals("S")) { //Achei combinacao de usuario,data,viagem
                                        String sval = RELTMP.Busca_Dados_Tmp(scodigo, "Vlrtot");
                                        String sqtd = RELTMP.Busca_Dados_Tmp(scodigo, "Qqtbil");
                                        sval = sval.replace(",", ".");
                                        nvalor = Double.valueOf(sval).doubleValue();

                                        String stot = String.format("%.2f", (nvalor + nvlrpas));
                                        String idTMP = RELTMP.Busca_Dados_Tmp(scodigo, "ID");
                                        RELTMP.Atualizar_Campo_Tmp(idTMP, "Vlrtot", stot);
                                        int iqtd = Integer.parseInt(sqtd);
                                        iqtd = (iqtd + 1);
                                        RELTMP.Atualizar_Campo_Tmp(idTMP, "Qqtbil", Integer.toString(iqtd));


                                    } else {
                                        RELTMP.InserirTmp(scodigo, svia, susr, semissao, "1", String.format("%.2f", (nvlrpas)), "", "", "");
                                    }


                                } else if (sSitbpe.equals("CA")) {
                                    iqtdcan = iqtdcan + 1;
                                } else if (sSitbpe.equals("DG")) {
                                    iqtddig = iqtddig + 1;
                                    String sval = cursor.getVlrpas();
                                    vlrbil = Double.valueOf(sval).doubleValue();
                                    vlrdig = vlrdig + vlrbil;

                                }

                                if (sSitbpe.equals("CT") && scancel.equals("S")) {
                                    iqtdcan = iqtdcan + 1;
                                }

                                if (sSitbpe.equals("BA") && scancel.equals("S")) {
                                    iqtdcan = iqtdcan + 1;
                                }

                                if (ckfecha.isChecked()) { ///marcar como fechado
                                    String snum = cursor.getNumbpe();
                                    String sid = db.Busca_Dados_Bpe(snum, "ID");
                                    db.Atualizar_Campo_Bpe(sid, "Tippas", "S");

                                }
                            }
                        }

                        ////Comeca imprimir aqui
                        String sespacos = dbemp.Busca_Dados_Emp(1, "Rsv003");


                        String sdathor = Funcoes_Android.getCurrentUTC();
                        sano = sdathor.substring(0, (4));
                        smes = sdathor.substring(5, (7));
                        sdia = sdathor.substring(8, (10));
                        sdatemi = sdia + "/" + smes + "/" + sano;
                        EditText edtuser = findViewById(R.id.edtUsrvenda);
                        String susuario = edtuser.getText().toString();
                        woyouService.setAlignment(1, callback);//alinhamento
                        woyouService.printBitmap(mBitmap, callback);
                        woyouService.lineWrap(1, null);

                        woyouService.sendRAWData(new byte[]{0x1b, 0x45, 0x1}, callback); // Ativar Fonte em Negrito
                        woyouService.setAlignment(1, callback); //Alinhamento
                        //0 - Alinhar a Esquerda
                        //1 - Centralizado
                        //2 - Alinhado a Direita
                        woyouService.printTextWithFont("Data de Impressão: " + sdatemi, "", 24, null);
                        woyouService.lineWrap(1, null);
                        //if (!susuario.equals("")) {
                        //   woyouService.printTextWithFont("USUARIO: " + susuario, "", 24, null);
                        //  woyouService.lineWrap(1, null);
                        //}
                        //woyouService.printTextWithFont("_________________", "", 24,null);
                        //woyouService.lineWrap(1, null);

                        woyouService.lineWrap(1, null);
                        DB_TMP.TmpCursor cursortmp = RELTMP.RetornarTmp(DB_TMP.TmpCursor.OrdenarPor.NomeCrescente);
                        String sUltdat = "";
                        for (int i = 0; i < cursortmp.getCount(); i++) {
                            cursortmp.moveToPosition(i);
                            String svia = cursortmp.getDescri();
                            String sqtdbil = cursortmp.getQtdbil();
                            String svaltot = cursortmp.getVlrtot();
                            String sDatatu = cursortmp.getDatemi();
                            String suser = cursortmp.getAgente();


                            if (!sDatatu.equals(sUltdat) && !sUltdat.equals("")) {
                                String subtot = String.format("%.2f", vlrdia);
                                subtot = subtot.replace(".", ",");


                                String sespaco = "";
                                String sqtd = "Bilhetes da Data: " + iqtddia;
                                Integer iqtd = sqtd.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                woyouService.printTextWithFont("Bilhetes da Data: " + sespaco + iqtddia, "", 24, null);
                                woyouService.lineWrap(1, null);

                                sespaco = "";
                                String svalor = "Total da Data: " + subtot;
                                iqtd = svalor.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                woyouService.printTextWithFont("Total da Data: " + sespaco + subtot, "", 24, null);
                                woyouService.lineWrap(2, null);
                                iqtddia = 0;
                                vlrdia = 0;
                            }


                            if (!suser.equals(snomeuser)) {
                                if (iqtdusr > 0) { //subtotal do Usuario

                                    String usrtotal = String.format("%.2f", vlrusr);
                                    usrtotal = usrtotal.replace(".", ",");


                                    String sespaco = "";
                                    String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                                    Integer iqtd = sqtd.length();
                                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                        Integer iresto = (32 - iqtd);
                                        while (sespaco.length() < iresto) {
                                            sespaco = " " + sespaco;
                                        }
                                    }
                                    woyouService.printTextWithFont("Bilhetes do Usuario: " + sespaco + iqtdusr, "", 22, null);
                                    woyouService.lineWrap(1, null);

                                    sespaco = "";
                                    String svalor = "Total do Usuario: " + usrtotal;
                                    iqtd = svalor.length();
                                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                        Integer iresto = (32 - iqtd);
                                        while (sespaco.length() < iresto) {
                                            sespaco = " " + sespaco;
                                        }
                                    }
                                    woyouService.printTextWithFont("Total do Usuario: " + sespaco + usrtotal, "", 22, null);
                                    woyouService.lineWrap(2, null);


                                    vlrusr = 0;
                                    iqtdusr = 0;
                                }


                                woyouService.setAlignment(0, callback); //Alinhamento
                                woyouService.printTextWithFont("USUARIO: " + suser, "", 24, null);
                                woyouService.lineWrap(1, null);

                            }


                            if (!sDatatu.equals(sUltdat)) {
                                String sdiav, smesv, sanov, sdatav;
                                sanov = sDatatu.substring(0, 4);
                                smesv = sDatatu.substring(5, 7);
                                sdiav = sDatatu.substring(8, 10);
                                sdatav = sdiav + "/" + smesv + "/" + sanov;


                                woyouService.printTextWithFont("Data da Venda: " + sdatav, "", 24, null);
                                woyouService.lineWrap(2, null);

                            }


                            sUltdat = sDatatu;
                            snomeuser = suser;
                            svaltot = svaltot.replace(",", ".");
                            vlrsub = Double.valueOf(svaltot).doubleValue();
                            int iqtd = Integer.parseInt(sqtdbil);
                            iqtddia = iqtddia + iqtd;
                            iqtdbil = iqtdbil + iqtd;
                            iqtdusr = iqtdusr + iqtd;
                            vlrdia = vlrdia + vlrsub;
                            vlrtot = vlrtot + vlrsub;
                            vlrusr = vlrusr + vlrsub;


                            int posV = svia.indexOf("-");
                            Integer iqtdvia = svia.length();
                            svia = svia.substring((posV + 1), (iqtdvia));

                            String sespacov = "";
                            Integer iqtdd = svia.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacov.length() < iresto) {
                                    sespacov = " " + sespacov;
                                }
                            }

                            woyouService.printTextWithFont(svia + sespacov, "", 24, null);
                            woyouService.lineWrap(1, null);
                            sespacov = "";
                            String sqtdd = "Bilhetes da Viagem: " + sqtdbil;
                            iqtdd = sqtdd.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacov.length() < iresto) {
                                    sespacov = " " + sespacov;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes da Viagem" + sespacov + sqtdbil, "", 24, null);
                            woyouService.lineWrap(1, null);

                            sespacov = "";
                            sqtdd = "Total da Viagem: " + svaltot;
                            iqtdd = sqtdd.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacov.length() < iresto) {
                                    sespacov = " " + sespacov;
                                }
                            }
                            woyouService.printTextWithFont("Total da Viagem" + sespacov + svaltot, "", 24, null);
                            woyouService.lineWrap(2, null);

                        }


                        String subtot = String.format("%.2f", vlrdia);
                        subtot = subtot.replace(".", ",");


                        String sespaco = "";
                        String sqtd = "Bilhetes da Data: " + iqtddia;
                        Integer iqtd = sqtd.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Bilhetes da Data: " + sespaco + iqtddia, "", 24, null);
                        woyouService.lineWrap(1, null);

                        sespaco = "";
                        String svalor = "Total da Data: " + subtot;
                        iqtd = svalor.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Total da Data: " + sespaco + subtot, "", 24, null);
                        woyouService.lineWrap(2, null);
                        iqtddia = 0;
                        vlrdia = 0;


                        if (iqtdusr > 0) { //subtotal do Usuario
                            String usrtotal = String.format("%.2f", vlrusr);
                            usrtotal = usrtotal.replace(".", ",");


                            sespaco = "";
                            sqtd = "Bilhetes do Usuario: " + iqtdusr;
                            iqtd = sqtd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes do Usuario: " + sespaco + iqtdusr, "", 22, null);
                            woyouService.lineWrap(1, null);

                            sespaco = "";
                            svalor = "Total do Usuario: " + usrtotal;
                            iqtd = svalor.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Total do Usuario: " + sespaco + usrtotal, "", 22, null);
                            woyouService.lineWrap(2, null);

                        }


                        String stotal = String.format("%.2f", vlrtot);
                        stotal = stotal.replace(".", ",");


                        sespaco = "";
                        sqtd = "Bilhetes Vendidos: " + iqtdbil;
                        iqtd = sqtd.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Bilhetes Vendidos: " + sespaco + iqtdbil, "", 24, null);
                        woyouService.lineWrap(1, null);

                        sespaco = "";
                        svalor = "Valor Total: " + stotal;
                        iqtd = svalor.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        woyouService.printTextWithFont("Valor Total: " + sespaco + stotal, "", 24, null);
                        woyouService.lineWrap(2, null);

                        String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
                        if (spvenda.equals("R")) { //rodiviaria calcular taxa de embarque
                            vlremb = Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
                            totalemb = iqtdbil * vlremb;
                            String staxa = String.format("%.2f", totalemb);
                            staxa = staxa.replace(".", ",");
                            sespaco = "";
                            String svlrtax = "Taxa de Embarque: " + staxa;
                            iqtd = svlrtax.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Taxa de Embarque: " + sespaco + staxa, "", 24, null);
                            woyouService.lineWrap(2, null);
                            totliq = vlrtot - totalemb;
                            String stotliq = String.format("%.2f", totliq);
                            stotliq = stotliq.replace(".", ",");
                            sespaco = "";
                            String stotalliq = "Tarifa + Seguro: " + stotliq;
                            iqtd = stotalliq.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            woyouService.printTextWithFont("Tarifa + Seguro: " + sespaco + stotliq, "", 24, null);
                            woyouService.lineWrap(2, null);

                        }


                        if (iqtdcan > 0) { //mostrar quantidade cancelada
                            woyouService.lineWrap(2, null);

                            String sespacocan = "";
                            String sqtdcan = "Bilhetes Cancelados: " + iqtdcan;
                            Integer iqtdc = sqtdcan.length();
                            if (iqtdc < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdc);
                                while (sespacocan.length() < iresto) {
                                    sespacocan = " " + sespacocan;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes Cancelados: " + sespacocan + iqtdcan, "", 24, null);
                            woyouService.lineWrap(2, null);


                        }

                        if (iqtddig > 0) {
                            woyouService.lineWrap(2, null);
                            String stotdig = String.format("%.2f", vlrdig);
                            stotdig = stotdig.replace(".", ",");


                            String sespacod = "";
                            String sqtdd = "Bilhetes em Aberto: " + iqtddig;
                            Integer iqtdd = sqtdd.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacod.length() < iresto) {
                                    sespacod = " " + sespacod;
                                }
                            }
                            woyouService.printTextWithFont("Bilhetes em Aberto: " + sespacod + iqtddig, "", 24, null);
                            woyouService.lineWrap(1, null);

                            sespacod = "";
                            String svalord = "Valor em Aberto: " + stotdig;
                            iqtdd = svalord.length();
                            if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdd);
                                while (sespacod.length() < iresto) {
                                    sespacod = " " + sespacod;
                                }
                            }
                            woyouService.printTextWithFont("Valor em Aberto: " + sespacod + stotdig, "", 24, null);
                            woyouService.lineWrap(2, null);
                        }

                        if (iqtdpen > 0) { //existe pendentes
                            String sespacop = "";
                            String sqtdp = "Pendentes de Transmissão: " + iqtdpen;
                            Integer iqtdp = sqtdp.length();
                            if (iqtdp < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtdp);
                                while (sespacop.length() < iresto) {
                                    sespacop = " " + sespacop;
                                }
                            }
                            woyouService.printTextWithFont("Pendentes de Transmissão: " + sespacop + iqtdpen, "", 24, null);
                            woyouService.lineWrap(2, null);
                        }

                        if (sespacos.equals("xx")) {
                            woyouService.lineWrap(2, null);
                        } else {
                            int iesp = Integer.parseInt(sespacos);
                            woyouService.lineWrap(iesp, null);
                        }


                        //Limpar Base temporaria
                        RELTMP.deletar_TMP();


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


    ////IMPRESSAO BLUETOOTH

    @SuppressLint("MissingPermission")
    private void acharPrinterBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não foi encontrado ou não disponível neste equipamento.", Toast.LENGTH_SHORT).show();
            // Device doesn't support Bluetooth
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
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
    public void Listar_Vendas_BT() {
        try {
            if (printerBluetooth == null)
                acharPrinterBluetooth();
            if (printerBluetooth == null)
                return;

            BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

            impressora.connect();
            try {
                iniciarImpressora(impressora.getOutputStream());
                OutputStream out = impressora.getOutputStream();
                String sdia, smes, sano, sdatemi;
                String sviagem = "";
                String snomeuser = "";
                double vlrtot, vlrbil, vlrsub, vlrusr, vlrdig, vlrdia, vlremb, totalemb, totliq, vlrvale, vlrembd, vlrembo;
                vlrtot = 0;
                vlrsub = 0;
                vlrusr = 0;
                vlrdig = 0;
                vlrdia = 0;
                totalemb = 0;
                vlremb = 0;
                totliq = 0;
                vlrvale = 0;
                vlrembd = 0;
                vlrembo = 0;
                Integer iqtdbil = 0;
                Integer iqtdsub = 0;
                Integer iqtdusr = 0;
                Integer iqtdcan = 0;
                Integer iqtddig = 0;
                Integer iqtdpen = 0;
                Integer iqtddia = 0;
                String sDatcaixa = "";

                // int iret = XmlPontos("C", 0, "Origem", "Destino");
                //iret = XmlPontos("A", 0, "Origem", "Destino");
                //Dados a Empresa
                DB_EMP dbemp = new DB_EMP(getApplicationContext());

                String sespacos = dbemp.Busca_Dados_Emp(1, "Rsv003");

                CheckBox cklist = findViewById(R.id.ckbListar);
                CheckBox ckfecha = findViewById(R.id.ckbFectur);
                CheckBox ckbger = findViewById(R.id.ckbGeral);
                String sgeral = "";
                if (ckbger.isChecked()) {
                    sgeral = "S";
                }

                String sdathor = Funcoes_Android.getCurrentUTC();
                sano = sdathor.substring(0, (4));
                smes = sdathor.substring(5, (7));
                sdia = sdathor.substring(8, (10));
                sdatemi = sdia + "/" + smes + "/" + sano;
                EditText edtuser = findViewById(R.id.edtUsrvenda);
                String susuario = edtuser.getText().toString();

                //out.write(EscPosBase.getFontWBold()); // Ativar Fonte em Negrito
                out.write(EscPosBase.alignCenter()); //Centralizado
                String sdatimp = "Data de Impressao: " + sdatemi;
                out.write(sdatimp.getBytes(StandardCharsets.UTF_8));
                out.write(EscPosBase.nextLine());

                DB_BPE db = new DB_BPE(ConsultaActivity.this);
                DB_BPE.BpeCursor cursor = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

                for (int ibp = 0; ibp < cursor.getCount(); ibp++) {
                    cursor.moveToPosition(ibp);
                    String sdatven = cursor.getDatemi();
                    String sdatsai = sdatven.substring(0, (10));
                    String sagente = cursor.getAgente();
                    String sfecha = cursor.getTippas();
                    String stransf = cursor.getTransf();
                    if (!stransf.equals("S")) {
                        iqtdpen = iqtdpen + 1;
                    }
                    if (!sfecha.equals("S") || sgeral.equals("S")) { //se ainda nao fechou turno ou se imprime todos
                        String sSitbpe = cursor.getSitbpe();

                        String scancel = cursor.getRsv001(); //se marcou para cancelar

                        System.out.println("N: " + cursor.getNumbpe() + "Sit: " + sSitbpe + " Cancel: " + scancel);
                        if ((sSitbpe.equals("BA") && !scancel.equals("S")) || (sSitbpe.equals("CT") && !scancel.equals("S"))) { //Autorizados e contingencia


                            String slinha = cursor.getNomvia();
                            if (!slinha.equals(sviagem)) {
                                out.write(EscPosBase.alignLeft()); //Esquerda


                                if (iqtdsub > 0) { //imprimir subtotal

                                    String subtotal = String.format("%.2f", vlrsub);
                                    subtotal = subtotal.replace(".", ",");


                                    String sespaco = "";
                                    String sqtd = "Bilhetes da Viagem: " + iqtdsub;
                                    Integer iqtd = sqtd.length();
                                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                        Integer iresto = (32 - iqtd);
                                        while (sespaco.length() < iresto) {
                                            sespaco = " " + sespaco;
                                        }
                                    }
                                    if (cklist.isChecked()) {
                                        out.write(EscPosBase.nextLine(1));
                                    }
                                    String sbilvia = "Bilhetes da Viagem: " + sespaco + iqtdsub;
                                    out.write(sbilvia.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    sespaco = "";
                                    String svalor = "Total da Viagem: " + subtotal;
                                    iqtd = svalor.length();
                                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                        Integer iresto = (32 - iqtd);
                                        while (sespaco.length() < iresto) {
                                            sespaco = " " + sespaco;
                                        }
                                    }
                                    String stotvia = "Total da Viagem: " + sespaco + subtotal;
                                    out.write(stotvia.getBytes(StandardCharsets.UTF_8));
                                    if (cklist.isChecked()) {
                                        out.write(EscPosBase.nextLine(2));
                                    } else {
                                        out.write(EscPosBase.nextLine());
                                    }

                                    vlrsub = 0;
                                    iqtdsub = 0;
                                } else {
                                    out.write(EscPosBase.nextLine());
                                }

                                if (!sdatsai.equals(sDatcaixa)) { //se a data for diferente
                                    if (iqtddia > 0) {
                                        String subtot = String.format("%.2f", vlrdia);
                                        subtot = subtot.replace(".", ",");


                                        String sespaco = "";
                                        String sqtd = "Bilhetes da Data: " + iqtddia;
                                        Integer iqtd = sqtd.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32 - iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String sbildat = "Bilhetes da Data: " + sespaco + iqtddia;
                                        out.write(sbildat.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());


                                        sespaco = "";
                                        String svalor = "Total da Data: " + subtot;
                                        iqtd = svalor.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32 - iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String stotdat = "Total da Data: " + sespaco + subtot;
                                        out.write(stotdat.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine(2));


                                        vlrdia = 0;
                                        iqtddia = 0;
                                    } else {
                                        out.write(EscPosBase.nextLine());
                                    }
                                    String sdiad, smesd, sanod, sdata;
                                    sanod = sdatsai.substring(0, (4));
                                    smesd = sdatsai.substring(5, (7));
                                    sdiad = sdatsai.substring(8, (10));
                                    sdata = sdiad + "/" + smesd + "/" + sanod;
                                    String svendasdat = "Data da Venda: " + sdata;
                                    out.write(svendasdat.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    sDatcaixa = sdatsai;


                                } //if (!sdatsai.equals(sDatcaixa)) { //se a data for diferente

                                String snome = cursor.getAgente();
                                if (!snome.equals(snomeuser)) {
                                    if (iqtdusr > 0) { //subtotal do Usuario

                                        String usrtotal = String.format("%.2f", vlrusr);
                                        usrtotal = usrtotal.replace(".", ",");


                                        String sespaco = "";
                                        String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                                        Integer iqtd = sqtd.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32 - iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String sbilusr = "Bilhetes do Usuario: " + sespaco + iqtdusr;
                                        out.write(sbilusr.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine());


                                        sespaco = "";
                                        String svalor = "Total do Usuario: " + usrtotal;
                                        iqtd = svalor.length();
                                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                            Integer iresto = (32 - iqtd);
                                            while (sespaco.length() < iresto) {
                                                sespaco = " " + sespaco;
                                            }
                                        }
                                        String stotusr = "Total do Usuario: " + sespaco + usrtotal;
                                        out.write(stotusr.getBytes(StandardCharsets.UTF_8));
                                        out.write(EscPosBase.nextLine(2));


                                        vlrusr = 0;
                                        iqtdusr = 0;
                                    }


                                    out.write(EscPosBase.nextLine());
                                    String snomusr = "USUARIO: " + snome;
                                    out.write(snomusr.getBytes(StandardCharsets.UTF_8));
                                    out.write(EscPosBase.nextLine());

                                    snomeuser = snome;
                                }


                                int posV = slinha.indexOf("-");
                                int iqtdv = slinha.length();
                                String sVia = slinha.substring((posV + 1), (iqtdv));

                                out.write(sVia.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());
                                sviagem = slinha;


                            }
                            System.out.println("Dentro do bpe");
                            String sbilhete = cursor.getNumbpe();
                            String sori = cursor.getTreori();
                            String sdes = cursor.getTredes();
                            String sval = cursor.getVlrpas();

                            int posO = sori.indexOf("-");
                            int iqtdo = sori.length();
                            String sOri = sori.substring((posO + 1), (iqtdo));


                            int posD = sdes.indexOf("-");
                            int iqtdd = sdes.length();
                            String sDes = sdes.substring((posD + 1), (iqtdd));


                            //Log.i(TAG,"Taxbem: " + vlremb);


                            String strecho = sOri + "x" + sDes;
                            sval = sval.replace(",", ".");
                            vlrbil = Double.valueOf(sval).doubleValue();
                            vlrtot = vlrtot + vlrbil;
                            vlrsub = vlrsub + vlrbil;
                            vlrusr = vlrusr + vlrbil;
                            vlrdia = vlrdia + vlrbil;
                            String stippag = cursor.getPagmto();
                            if (stippag.equals("05")) {
                                vlrvale = vlrvale + vlrbil;
                            }


                            //Verificar se foi cobrada taxa de embarque
                            vlremb = Double.valueOf(cursor.getVlremb()).doubleValue();
                            if (vlremb > 0) {
                                totalemb = (totalemb + vlremb);
                                if (stippag.equals("05") || stippag.equals("06")) { //se for vale transp ou Pix
                                    vlrembo = vlrembo + vlremb;
                                    Log.i(TAG, "Forma: " + stippag + " = " + vlrembo);
                                } else {
                                    vlrembd = vlrembd + vlremb;
                                    Log.i(TAG, "Forma: " + stippag + " = " + vlrembd);
                                }
                            }

                            sval = String.format("%.2f", (vlrbil));
                            sval = sval.replace(".", ",");
                            if (cklist.isChecked()) { //se marcou para imprimir todos os bilhetes
                                out.write(strecho.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());
                                out.write(EscPosBase.alignRight());//Direita

                                String shorbil = sdatven.substring(11, (16));
                                String sespaco = "";
                                String svalor = "Bilhete: " + sbilhete + "  " + shorbil + "   " + sval;
                                Integer iqtd = svalor.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                String sdetbil = "Bilhete: " + sbilhete + "  " + shorbil + "   " + sval;
                                out.write(sdetbil.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());

                            }
                            iqtdsub = iqtdsub + 1;
                            iqtdbil = iqtdbil + 1;
                            iqtdusr = iqtdusr + 1;
                            iqtddia = iqtddia + 1;


                        } else if (sSitbpe.equals("CA")) {
                            iqtdcan = iqtdcan + 1;
                        } else if (sSitbpe.equals("DG")) {
                            iqtddig = iqtddig + 1;
                            String sval = cursor.getVlrpas();
                            vlrbil = Double.valueOf(sval).doubleValue();
                            vlrdig = vlrdig + vlrbil;

                        }

                        if (sSitbpe.equals("CT") && scancel.equals("S")) {
                            iqtdcan = iqtdcan + 1;
                        }

                        if (sSitbpe.equals("BA") && scancel.equals("S")) {
                            iqtdcan = iqtdcan + 1;
                        }

                        if (ckfecha.isChecked()) { ///marcar como fechado
                            String snum = cursor.getNumbpe();
                            String sid = db.Busca_Dados_Bpe(snum, "ID");
                            db.Atualizar_Campo_Bpe(sid, "Tippas", "S");

                        }
                    }
                }


                if (iqtdsub > 0) { //imprimir subtotal da viagem
                    String subtotal = String.format("%.2f", vlrsub);
                    subtotal = subtotal.replace(".", ",");


                    String sespaco = "";
                    String sqtd = "Bilhetes da Viagem: " + iqtdsub;
                    Integer iqtd = sqtd.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    if (cklist.isChecked()) {
                        out.write(EscPosBase.nextLine());
                    }
                    String sbilvia = "Bilhetes da Viagem: " + sespaco + iqtdsub;
                    out.write(sbilvia.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());


                    sespaco = "";
                    String svalor = "Total da Viagem: " + subtotal;
                    iqtd = svalor.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String stotvia = "Total da Viagem: " + sespaco + subtotal;
                    out.write(stotvia.getBytes(StandardCharsets.UTF_8));
                    if (cklist.isChecked()) {
                        out.write(EscPosBase.nextLine(2));
                    } else {
                        out.write(EscPosBase.nextLine());
                    }


                }


                if (iqtddia > 0) {
                    String subtot = String.format("%.2f", vlrdia);
                    subtot = subtot.replace(".", ",");


                    String sespaco = "";
                    String sqtd = "Bilhetes da Data: " + iqtddia;
                    Integer iqtd = sqtd.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String sbildat = "Bilhetes da Data: " + sespaco + iqtddia;
                    out.write(sbildat.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    sespaco = "";
                    String svalor = "Total da Data: " + subtot;
                    iqtd = svalor.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String stotdat = "Total da Data: " + sespaco + subtot;
                    out.write(stotdat.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));

                }

                if (iqtdusr > 0) { //subtotal do Usuario
                    out.write(EscPosBase.nextLine());
                    String usrtotal = String.format("%.2f", vlrusr);
                    usrtotal = usrtotal.replace(".", ",");


                    String sespaco = "";
                    String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                    Integer iqtd = sqtd.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String sbilusr = "Bilhetes do Usuario: " + sespaco + iqtdusr;
                    out.write(sbilusr.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    sespaco = "";
                    String svalor = "Total do Usuario: " + usrtotal;
                    iqtd = svalor.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String stotusr = "Total do Usuario: " + sespaco + usrtotal;
                    out.write(stotusr.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));


                }


                out.write(EscPosBase.nextLine(2));
                String stotal = String.format("%.2f", vlrtot);
                stotal = stotal.replace(".", ",");


                String sespaco = "";
                String sqtd = "Bilhetes Vendidos: " + iqtdbil;
                Integer iqtd = sqtd.length();
                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                    Integer iresto = (32 - iqtd);
                    while (sespaco.length() < iresto) {
                        sespaco = " " + sespaco;
                    }
                }
                String sbilvend = "Bilhetes Vendidos: " + sespaco + iqtdbil;
                out.write(sbilvend.getBytes(StandardCharsets.UTF_8));
                out.write(EscPosBase.nextLine());


                sespaco = "";
                String svalor = "Valor Total: " + stotal;
                iqtd = svalor.length();
                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                    Integer iresto = (32 - iqtd);
                    while (sespaco.length() < iresto) {
                        sespaco = " " + sespaco;
                    }
                }
                String stotger = "Valor Total: " + sespaco + stotal;
                out.write(stotger.getBytes(StandardCharsets.UTF_8));
                out.write(EscPosBase.nextLine(2));


                String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
                if (spvenda.equals("R") || (totalemb > 0)) { //rodiviaria calcular taxa de embarque
                    //vlremb=Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
                    //totalemb = iqtdbil*vlremb;
                    String staxa = String.format("%.2f", totalemb);
                    staxa = staxa.replace(".", ",");
                    sespaco = "";
                    String svlrtax = "Taxa de Embarque: " + staxa;
                    iqtd = svlrtax.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String staxemb = "Taxa de Embarque: " + sespaco + staxa;
                    out.write(staxemb.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));


                    ///Taxa de embarque por forma de pagamento
                    if (vlrembd > 0 || vlrembo > 0) {
                        if (vlrembd > 0) {//Dinheiro
                            String staxad = String.format("%.2f", vlrembd);
                            staxa = staxad.replace(".", ",");
                            sespaco = "";
                            String svlrtaxd = "TX em Dinheiro: " + staxad;
                            iqtd = svlrtaxd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            String staxembd = "TX em Dinheiro: " + sespaco + staxad;
                            out.write(staxembd.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(2));
                        }
                        if (vlrembo > 0) { //Vale transp ou Pix
                            String staxao = String.format("%.2f", vlrembo);
                            staxa = staxao.replace(".", ",");
                            sespaco = "";
                            String svlrtaxo = "TX em Vale Transp: " + staxao;
                            iqtd = svlrtaxo.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            String staxembo = "TX em Vale Transp: " + sespaco + staxao;
                            out.write(staxembo.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(2));

                        }
                    }

                    totliq = vlrtot - totalemb;
                    String stotliq = String.format("%.2f", totliq);
                    stotliq = stotliq.replace(".", ",");
                    sespaco = "";
                    String stotalliq = "Tarifa + Seguro: " + stotliq;
                    iqtd = stotalliq.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String staxseg = "Tarifa + Seguro: " + sespaco + stotliq;
                    out.write(staxseg.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));

                }
                //RESUMO POR FORMA DE PAGAMENTO
                if (vlrvale > 0) {
                    out.write(EscPosBase.nextLine(2));
                    String stotvale = String.format("%.2f", vlrvale);
                    stotvale = stotvale.replace(".", ",");
                    sespaco = "";
                    String stotalvale = "Vale Transporte: " + stotvale;
                    iqtd = stotalvale.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    stotalvale = "Vale Transporte: " + sespaco + stotvale;
                    out.write(stotalvale.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    if (vlrtot > vlrvale) {
                        double vlrdin = (vlrtot - vlrvale);
                        String stotdin = String.format("%.2f", vlrdin);
                        stotdin = stotdin.replace(".", ",");
                        sespaco = "";
                        String stotaldin = "Dinheiro: " + stotdin;
                        iqtd = stotaldin.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        stotaldin = "Dinheiro: " + sespaco + stotdin;
                        out.write(stotaldin.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));

                    }

                }


                if (iqtdcan > 0) { //mostrar quantidade cancelada
                    out.write(EscPosBase.nextLine(2));

                    String sespacocan = "";
                    String sqtdcan = "Bilhetes Cancelados: " + iqtdcan;
                    Integer iqtdc = sqtdcan.length();
                    if (iqtdc < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtdc);
                        while (sespacocan.length() < iresto) {
                            sespacocan = " " + sespacocan;
                        }
                    }
                    String sbilcan = "Bilhetes Cancelados: " + sespacocan + iqtdcan;
                    out.write(sbilcan.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));


                }

                if (iqtddig > 0) {
                    out.write(EscPosBase.nextLine(2));
                    String stotdig = String.format("%.2f", vlrdig);
                    stotdig = stotdig.replace(".", ",");


                    String sespacod = "";
                    String sqtdd = "Bilhetes em Aberto: " + iqtddig;
                    Integer iqtdd = sqtdd.length();
                    if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtdd);
                        while (sespacod.length() < iresto) {
                            sespacod = " " + sespacod;
                        }
                    }
                    String sbilabe = "Bilhetes em Aberto: " + sespacod + iqtddig;
                    out.write(sbilabe.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());
                    sespacod = "";
                    String svalord = "Valor em Aberto: " + stotdig;
                    iqtdd = svalord.length();
                    if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtdd);
                        while (sespacod.length() < iresto) {
                            sespacod = " " + sespacod;
                        }
                    }
                    String svalabe = "Valor em Aberto: " + sespacod + stotdig;
                    out.write(svalabe.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));

                }

                if (iqtdpen > 0) { //existe pendentes
                    String sespacop = "";
                    String sqtdp = "Pendentes de Transmissão: " + iqtdpen;
                    Integer iqtdp = sqtdp.length();
                    if (iqtdp < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtdp);
                        while (sespacop.length() < iresto) {
                            sespacop = " " + sespacop;
                        }
                    }
                    String sbilpen = "Bilhetes Pendentes: " + sespacop + iqtdpen;
                    out.write(sbilpen.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));

                }

                if (sespacos.equals("xx")) {
                    out.write(EscPosBase.nextLine(2));
                } else {
                    int iesp = Integer.parseInt(sespacos);
                    out.write(EscPosBase.nextLine(iesp));
                }


            } finally {
                // btnPrint.setEnabled(true);
                impressora.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @SuppressLint("MissingPermission")
    public void Listar_Vendas_Agrupado_BT() {
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
                    String sdia, smes, sano, sdatemi, svlremb;
                    String sviagem = "";
                    String snomeuser = "";
                    double vlrtot, vlrbil, vlrsub, vlrusr, vlrdig, vlrdia, vlremb, totalemb, totliq, nvlrpas, nvalor, nvalvale, vlrvale, vlrsubvale, vlrembd, vlrembo;
                    vlrtot = 0;
                    vlrsub = 0;
                    vlrusr = 0;
                    vlrdig = 0;
                    vlrdia = 0;
                    totalemb = 0;
                    vlremb = 0;
                    vlrembd = 0;
                    vlrembo = 0;
                    totliq = 0;
                    vlrvale = 0;
                    nvalvale = 0;
                    vlrsubvale = 0;
                    Integer iqtdbil = 0;
                    Integer iqtdsub = 0;
                    Integer iqtdusr = 0;
                    Integer iqtdcan = 0;
                    Integer iqtddig = 0;
                    Integer iqtdpen = 0;
                    Integer iqtddia = 0;
                    String sDatcaixa = "";

                    iniciarImpressora(impressora.getOutputStream());
                    OutputStream out = impressora.getOutputStream();

                    //Limpar Base temporaria
                    DB_TMP RELTMP = new DB_TMP(ConsultaActivity.this);
                    RELTMP.deletar_TMP();

                    //Dados a Empresa
                    DB_EMP dbemp = new DB_EMP(getApplicationContext());

                    CheckBox cklist = findViewById(R.id.ckbListar);
                    CheckBox ckfecha = findViewById(R.id.ckbFectur);
                    CheckBox ckbger = findViewById(R.id.ckbGeral);
                    String sgeral = "";
                    if (ckbger.isChecked()) {
                        sgeral = "S";
                    }

                    DB_BPE db = new DB_BPE(ConsultaActivity.this);
                    DB_BPE.BpeCursor cursor = db.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

                    for (int ibp = 0; ibp < cursor.getCount(); ibp++) {
                        cursor.moveToPosition(ibp);
                        String sdatven = cursor.getDatemi();
                        String sdatsai = sdatven.substring(0, (10));
                        String sagente = cursor.getAgente();
                        String sfecha = cursor.getTippas();
                        String stransf = cursor.getTransf();
                        if (!stransf.equals("S")) {
                            iqtdpen = iqtdpen + 1;
                        }
                        if (!sfecha.equals("S") || sgeral.equals("S")) { //se ainda nao fechou turno ou se for conferencia geral
                            String sSitbpe = cursor.getSitbpe();

                            String scancel = cursor.getRsv001(); //se marcou para cancelar
                            if ((sSitbpe.equals("BA") && !scancel.equals("S")) || (sSitbpe.equals("CT") && !scancel.equals("S"))) { //Autorizados e contingencia
                                String susr = cursor.getAgente();
                                String sdat = cursor.getDatemi();
                                String semissao = sdat.substring(0, 10);
                                String svia = cursor.getNomvia();
                                String vlrpas = cursor.getVlrpas();
                                String stippag = cursor.getPagmto();
                                vlrpas = vlrpas.replace(",", ".");
                                nvlrpas = Double.valueOf(vlrpas).doubleValue();
                                String scodigo = susr + semissao + svia;
                                DB_TMP.TmpCursor cursortmp = RELTMP.RetornarTmp(DB_TMP.TmpCursor.OrdenarPor.NomeCrescente);
                                String sOK = "";
                                for (int itmp = 0; itmp < cursortmp.getCount(); itmp++) {
                                    cursortmp.moveToPosition(itmp);

                                }
                                if (cursortmp.getCount() > 0) {
                                    sOK = "S";
                                }


                                String svai = "";
                                if (sOK.equals("S")) {
                                    for (int itmp = 0; itmp < cursortmp.getCount(); itmp++) {
                                        cursortmp.moveToPosition(itmp);
                                        String scodtmp = cursortmp.getCodigo();
                                        if (scodigo.equals(scodtmp)) {
                                            svai = "S";

                                        }
                                    }
                                }

                                if (svai.equals("S")) { //Achei combinacao de usuario,data,viagem
                                    String sval = RELTMP.Busca_Dados_Tmp(scodigo, "Vlrtot");
                                    String sqtd = RELTMP.Busca_Dados_Tmp(scodigo, "Qqtbil");
                                    sval = sval.replace(",", ".");
                                    nvalor = Double.valueOf(sval).doubleValue();

                                    String stot = String.format("%.2f", (nvalor + nvlrpas));
                                    String idTMP = RELTMP.Busca_Dados_Tmp(scodigo, "ID");
                                    RELTMP.Atualizar_Campo_Tmp(idTMP, "Vlrtot", stot);

                                    vlremb = Double.valueOf(cursor.getVlremb()).doubleValue();
                                    totalemb = totalemb + vlremb;
                                    //Atualizar Vale transporte
                                    //Log.i(TAG,"Taxa de embarque Encontrei DB: ");
                                    if (stippag.equals("05")) {
                                        //Log.i(TAG,sdat+" Forma: "+stippag+" = " + vlremb);
                                        String svale = RELTMP.Busca_Dados_Tmp(scodigo, "Vlrvale");
                                        svale = svale.replace(",", ".");
                                        nvalvale = Double.valueOf(svale).doubleValue();
                                        String stotvale = String.format("%.2f", (nvalvale + nvlrpas));
                                        RELTMP.Atualizar_Campo_Tmp(idTMP, "Vlrvale", stotvale);
                                        if (vlremb > 0) {//vale transporte
                                            vlrembo = vlrembo + vlremb;
                                                /*svlremb = RELTMP.Busca_Dados_Tmp(scodigo, "Vlrembo");
                                                svlremb = svlremb.replace(",", ".");
                                                String svalembo = "";
                                                if (!svlremb.equals("")) {
                                                    vlrembo = Double.valueOf(svlremb).doubleValue();
                                                    svalembo = String.format("%.2f", (vlrembo + vlremb));
                                                    vlrembo = vlrembo + vlremb;
                                                } else {
                                                    svalembo = String.format("%.2f", (vlremb));
                                                    vlrembo = vlremb;
                                                }
                                                RELTMP.Atualizar_Campo_Tmp(idTMP, "Vlrembo", svalembo);
                                                Log.i(TAG," Inserir Pagamento O: "+stippag+" = " + vlrembd + " === "+ svalembo);
                                                */
                                        }

                                    } else {//dinheiro
                                        //Log.i(TAG,sdat+" Forma: "+stippag+" = " + vlremb);
                                        if (vlremb > 0) {
                                            vlrembd = vlrembd + vlremb;
                                                /*svlremb = RELTMP.Busca_Dados_Tmp(scodigo, "Vlrembd");
                                                svlremb = svlremb.replace(",", ".");
                                                String svalembd = "";
                                                if (!svlremb.equals("")) {
                                                    vlrembd = Double.valueOf(svlremb).doubleValue();
                                                    svalembd = String.format("%.2f", (vlrembd + vlremb));
                                                    vlrembd = vlrembd + vlremb;
                                                }  else {
                                                    svalembd = String.format("%.2f", (vlremb));
                                                    vlrembd = vlremb;
                                                }
                                                RELTMP.Atualizar_Campo_Tmp(idTMP, "Vlrembd", svalembd);
                                                Log.i(TAG," Atualizar Pagamento D: "+stippag+" = " + svalembd + " === "+ vlrembo);
                                                */
                                        }
                                    }


                                    int iqtd = Integer.parseInt(sqtd);
                                    iqtd = (iqtd + 1);
                                    RELTMP.Atualizar_Campo_Tmp(idTMP, "Qqtbil", Integer.toString(iqtd));


                                } else {
                                    //Log.i(TAG,"Taxa de embarque Nao Encontrei DB: ");
                                    vlremb = Double.valueOf(cursor.getVlremb()).doubleValue();
                                    totalemb = totalemb + vlremb;
                                    //Criar Vale transporte
                                    Double nvale = 0.00;
                                    if (stippag.equals("05")) {
                                        nvale = nvlrpas;
                                        if (vlremb > 0) {//vale transporte
                                            vlrembo = vlrembo + vlremb;
                                            //Log.i(TAG,sdat+" Forma: "+stippag+" = " + vlrembo);
                                        }
                                    } else {
                                        if (vlremb > 0) {//vale transporte
                                            vlrembd = vlrembd + vlremb;
                                            //Log.i(TAG,sdat+" Forma: "+stippag+" = " + vlrembd);
                                        }
                                    }
                                    //Log.i(TAG," Inserir Pagamento: "+stippag+" = " + vlrembd + " === "+ vlrembo);
                                    RELTMP.InserirTmp(scodigo, svia, susr, semissao, "1", String.format("%.2f", (nvlrpas)), String.format("%.2f", (nvale)), String.format("%.2f", (vlrembd)), String.format("%.2f", (vlrembo)));
                                }


                            } else if (sSitbpe.equals("CA")) {
                                iqtdcan = iqtdcan + 1;
                            } else if (sSitbpe.equals("DG")) {
                                iqtddig = iqtddig + 1;
                                String sval = cursor.getVlrpas();
                                vlrbil = Double.valueOf(sval).doubleValue();
                                vlrdig = vlrdig + vlrbil;

                            }

                            if (sSitbpe.equals("CT") && scancel.equals("S")) {
                                iqtdcan = iqtdcan + 1;
                            }

                            if (sSitbpe.equals("BA") && scancel.equals("S")) {
                                iqtdcan = iqtdcan + 1;
                            }

                            if (ckfecha.isChecked()) { ///marcar como fechado
                                String snum = cursor.getNumbpe();
                                String sid = db.Busca_Dados_Bpe(snum, "ID");
                                db.Atualizar_Campo_Bpe(sid, "Tippas", "S");

                            }
                        }
                    }

                    ////Comeca imprimir aqui
                    String sespacos = dbemp.Busca_Dados_Emp(1, "Rsv003");


                    String sdathor = Funcoes_Android.getCurrentUTC();
                    sano = sdathor.substring(0, (4));
                    smes = sdathor.substring(5, (7));
                    sdia = sdathor.substring(8, (10));
                    sdatemi = sdia + "/" + smes + "/" + sano;
                    EditText edtuser = findViewById(R.id.edtUsrvenda);
                    String susuario = edtuser.getText().toString();


                    out.write(EscPosBase.getFontWNormal()); // Ativar Fonte em Negrito
                    out.write(EscPosBase.alignCenter()); //Centralizado


                    out.write((EscPosBase.getFontNormal()));
                    String sdatimp = "Data de Impressao: " + sdatemi;
                    out.write(sdatimp.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));
                    // out.write((EscPosBase.getFontNormal()));
                    //vlrembo = 0;
                    //vlrembd = 0;
                    DB_TMP.TmpCursor cursortmp = RELTMP.RetornarTmp(DB_TMP.TmpCursor.OrdenarPor.NomeCrescente);
                    String sUltdat = "";
                    for (int i = 0; i < cursortmp.getCount(); i++) {
                        cursortmp.moveToPosition(i);
                        String svia = cursortmp.getDescri();
                        String sqtdbil = cursortmp.getQtdbil();
                        String svaltot = cursortmp.getVlrtot();
                        String sDatatu = cursortmp.getDatemi();
                        String suser = cursortmp.getAgente();
                        String svalvale = cursortmp.getVlrvale();

                        //procurar taxa de embarque por forma de pagamento
                            /*String svlrembo = cursortmp.getVlrembo();
                            String svlrembd = cursortmp.getVlrembd();
                            svlrembo = svlrembo.replace(",", ".");
                            Double vbo = Double.valueOf(svlrembo).doubleValue();
                            vlrembo = vlrembo + vbo;//Vale transp.
                            svlrembd = svlrembd.replace(",", ".");
                            Double vbd = Double.valueOf(svlrembd).doubleValue();
                            vlrembd = vlrembd + vbd;//Dinheiro
                            Log.i(TAG,i+" Dinheiro: "+svlrembd+" Outros " + svlrembo);*/


                        if (!sDatatu.equals(sUltdat) && !sUltdat.equals("")) {
                            String subtot = String.format("%.2f", vlrdia);
                            subtot = subtot.replace(".", ",");


                            String sespaco = "";
                            String sqtd = "Bilhetes da Data: " + iqtddia;
                            Integer iqtd = sqtd.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            String sbildat = "Bilhetes da Data: " + sespaco + iqtddia;
                            out.write(sbildat.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine());

                            sespaco = "";
                            String svalor = "Total da Data: " + subtot;
                            iqtd = svalor.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            String stotdat = "Total da Data: " + sespaco + subtot;
                            out.write(stotdat.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(2));

                            iqtddia = 0;
                            vlrdia = 0;
                        }


                        if (!suser.equals(snomeuser)) {
                            if (iqtdusr > 0) { //subtotal do Usuario

                                String usrtotal = String.format("%.2f", vlrusr);
                                usrtotal = usrtotal.replace(".", ",");


                                String sespaco = "";
                                String sqtd = "Bilhetes do Usuario: " + iqtdusr;
                                Integer iqtd = sqtd.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                String sbilusr = "Bilhetes do Usuario: " + sespaco + iqtdusr;
                                out.write(sbilusr.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());


                                sespaco = "";
                                String svalor = "Total do Usuario: " + usrtotal;
                                iqtd = svalor.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                String stotusr = "Total do Usuario: " + sespaco + usrtotal;
                                out.write(stotusr.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine());


                                vlrusr = 0;
                                iqtdusr = 0;
                            }

                            out.write(EscPosBase.alignRight());
                            String snomeusr = "USUARIO: " + suser;
                            out.write(snomeusr.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine());

                        }


                        if (!sDatatu.equals(sUltdat)) {
                            String sdiav, smesv, sanov, sdatav;
                            sanov = sDatatu.substring(0, 4);
                            smesv = sDatatu.substring(5, 7);
                            sdiav = sDatatu.substring(8, 10);
                            sdatav = sdiav + "/" + smesv + "/" + sanov;


                            String sdatven = "Data da Venda: " + sdatav;
                            out.write(sdatven.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(2));

                        }


                        sUltdat = sDatatu;
                        snomeuser = suser;
                        svaltot = svaltot.replace(",", ".");
                        vlrsub = Double.valueOf(svaltot).doubleValue();
                        int iqtd = Integer.parseInt(sqtdbil);
                        iqtddia = iqtddia + iqtd;
                        iqtdbil = iqtdbil + iqtd;
                        iqtdusr = iqtdusr + iqtd;
                        vlrdia = vlrdia + vlrsub;
                        vlrtot = vlrtot + vlrsub;
                        vlrusr = vlrusr + vlrsub;
                        svalvale = svalvale.replace(",", ".");
                        vlrsubvale = Double.valueOf(svalvale).doubleValue();
                        vlrvale = vlrvale + vlrsubvale;


                        int posV = svia.indexOf("-");
                        Integer iqtdvia = svia.length();
                        svia = svia.substring((posV + 1), (iqtdvia));

                        String sespacov = "";
                        Integer iqtdd = svia.length();
                        if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdd);
                            while (sespacov.length() < iresto) {
                                sespacov = " " + sespacov;
                            }
                        }
                        String snomevia = svia + sespacov;
                        out.write(snomevia.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());

                        sespacov = "";
                        String sqtdd = "Bilhetes da Viagem: " + sqtdbil;
                        iqtdd = sqtdd.length();
                        if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdd);
                            while (sespacov.length() < iresto) {
                                sespacov = " " + sespacov;
                            }
                        }
                        String sbilvia = "Bilhetes da Viagem" + sespacov + sqtdbil;
                        out.write(sbilvia.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());

                        sespacov = "";
                        sqtdd = "Total da Viagem: " + svaltot;
                        iqtdd = sqtdd.length();
                        if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdd);
                            while (sespacov.length() < iresto) {
                                sespacov = " " + sespacov;
                            }
                        }
                        String stotvia = "Total da Viagem" + sespacov + svaltot;
                        out.write(stotvia.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                    }

                    String subtot = String.format("%.2f", vlrdia);
                    subtot = subtot.replace(".", ",");


                    String sespaco = "";
                    String sqtd = "Bilhetes da Data: " + iqtddia;
                    Integer iqtd = sqtd.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String sbildat = "Bilhetes da Data: " + sespaco + iqtddia;
                    out.write(sbildat.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    sespaco = "";
                    String svalor = "Total da Data: " + subtot;
                    iqtd = svalor.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String stotdat = "Total da Data: " + sespaco + subtot;
                    out.write(stotdat.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));

                    iqtddia = 0;
                    vlrdia = 0;


                    if (iqtdusr > 0) { //subtotal do Usuario
                        String usrtotal = String.format("%.2f", vlrusr);
                        usrtotal = usrtotal.replace(".", ",");


                        sespaco = "";
                        sqtd = "Bilhetes do Usuario: " + iqtdusr;
                        iqtd = sqtd.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        String sbilusr = "Bilhetes do Usuario: " + sespaco + iqtdusr;
                        out.write(sbilusr.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());

                        sespaco = "";
                        svalor = "Total do Usuario: " + usrtotal;
                        iqtd = svalor.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        String stotusr = "Total do Usuario: " + sespaco + usrtotal;
                        out.write(stotusr.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                    }


                    String stotal = String.format("%.2f", vlrtot);
                    stotal = stotal.replace(".", ",");


                    sespaco = "";
                    sqtd = "Bilhetes Vendidos: " + iqtdbil;
                    iqtd = sqtd.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String sbilven = "Bilhetes Vendidos: " + sespaco + iqtdbil;
                    out.write(sbilven.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine());

                    sespaco = "";
                    svalor = "Valor Total: " + stotal;
                    iqtd = svalor.length();
                    if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                        Integer iresto = (32 - iqtd);
                        while (sespaco.length() < iresto) {
                            sespaco = " " + sespaco;
                        }
                    }
                    String svaltot = "Valor Total: " + sespaco + stotal;
                    out.write(svaltot.getBytes(StandardCharsets.UTF_8));
                    out.write(EscPosBase.nextLine(2));


                    String spvenda = dbemp.Busca_Dados_Emp(1, "Pvenda");
                    if (totalemb > 0) { //rodiviaria calcular taxa de embarque
                        //vlremb=Double.valueOf(dbemp.Busca_Dados_Emp(1, "Rsv001")).doubleValue();
                        //totalemb = iqtdbil*vlremb;
                        String staxa = String.format("%.2f", totalemb);
                        staxa = staxa.replace(".", ",");
                        sespaco = "";
                        String svlrtax = "Taxa de Embarque: " + staxa;
                        iqtd = svlrtax.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        String staxemb = "Taxa de Embarque: " + sespaco + staxa;
                        out.write(staxemb.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));

                        ///Taxa de embarque por forma de pagamento
                        if (vlrembd > 0 || vlrembo > 0) {
                            Log.i(TAG, "Taxa de embarque: " + vlrembd + vlrembo);
                            if (vlrembd > 0) {//Dinheiro
                                String staxad = String.format("%.2f", vlrembd);
                                staxad = staxad.replace(".", ",");
                                sespaco = "";
                                String svlrtaxd = "TX em Dinheiro: " + staxad;
                                iqtd = svlrtaxd.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                String staxembd = "TX em Dinheiro: " + sespaco + staxad;
                                out.write(staxembd.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine(1));
                            }
                            if (vlrembo > 0) { //Vale transp ou Pix
                                String staxao = String.format("%.2f", vlrembo);
                                staxao = staxao.replace(".", ",");
                                sespaco = "";
                                String svlrtaxo = "TX em Vale Transp: " + staxao;
                                iqtd = svlrtaxo.length();
                                if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                    Integer iresto = (32 - iqtd);
                                    while (sespaco.length() < iresto) {
                                        sespaco = " " + sespaco;
                                    }
                                }
                                String staxembo = "TX em Vale Transp: " + sespaco + staxao;
                                out.write(staxembo.getBytes(StandardCharsets.UTF_8));
                                out.write(EscPosBase.nextLine(1));

                            }
                        }


                        totliq = vlrtot - totalemb;
                        String stotliq = String.format("%.2f", totliq);
                        stotliq = stotliq.replace(".", ",");
                        sespaco = "";
                        String stotalliq = "Tarifa + Seguro: " + stotliq;
                        iqtd = stotalliq.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        String staxseg = "Tarifa + Seguro: " + sespaco + stotliq;
                        out.write(staxseg.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                    }

                    //RESUMO POR FORMA DE PAGAMENTO
                    if (vlrvale > 0) {
                        out.write(EscPosBase.nextLine(2));
                        String stotvale = String.format("%.2f", vlrvale);
                        stotvale = stotvale.replace(".", ",");
                        sespaco = "";
                        String stotalvale = "Vale Transporte: " + stotvale;
                        iqtd = stotalvale.length();
                        if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtd);
                            while (sespaco.length() < iresto) {
                                sespaco = " " + sespaco;
                            }
                        }
                        stotalvale = "Vale Transporte: " + sespaco + stotvale;
                        out.write(stotalvale.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());
                        if (vlrtot > vlrvale) {
                            double vlrdin = (vlrtot - vlrvale);
                            String stotdin = String.format("%.2f", vlrdin);
                            stotdin = stotdin.replace(".", ",");
                            sespaco = "";
                            String stotaldin = "Dinheiro: " + stotdin;
                            iqtd = stotaldin.length();
                            if (iqtd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                                Integer iresto = (32 - iqtd);
                                while (sespaco.length() < iresto) {
                                    sespaco = " " + sespaco;
                                }
                            }
                            stotaldin = "Dinheiro: " + sespaco + stotdin;
                            out.write(stotaldin.getBytes(StandardCharsets.UTF_8));
                            out.write(EscPosBase.nextLine(2));

                        }

                    }


                    if (iqtdcan > 0) { //mostrar quantidade cancelada
                        out.write(EscPosBase.nextLine(2));

                        String sespacocan = "";
                        String sqtdcan = "Bilhetes Cancelados: " + iqtdcan;
                        Integer iqtdc = sqtdcan.length();
                        if (iqtdc < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdc);
                            while (sespacocan.length() < iresto) {
                                sespacocan = " " + sespacocan;
                            }
                        }
                        String sbilcan = "Bilhetes Cancelados: " + sespacocan + iqtdcan;
                        out.write(sbilcan.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));


                    }

                    if (iqtddig > 0) {
                        out.write(EscPosBase.nextLine(2));
                        String stotdig = String.format("%.2f", vlrdig);
                        stotdig = stotdig.replace(".", ",");


                        String sespacod = "";
                        String sqtdd = "Bilhetes em Aberto: " + iqtddig;
                        Integer iqtdd = sqtdd.length();
                        if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdd);
                            while (sespacod.length() < iresto) {
                                sespacod = " " + sespacod;
                            }
                        }
                        String sbilabe = "Bilhetes em Aberto: " + sespacod + iqtddig;
                        out.write(sbilabe.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine());

                        sespacod = "";
                        String svalord = "Valor em Aberto: " + stotdig;
                        iqtdd = svalord.length();
                        if (iqtdd < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdd);
                            while (sespacod.length() < iresto) {
                                sespacod = " " + sespacod;
                            }
                        }
                        String stotabe = "Valor em Aberto: " + sespacod + stotdig;
                        out.write(stotabe.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));

                    }

                    if (iqtdpen > 0) { //existe pendentes
                        String sespacop = "";
                        String sqtdp = "Pendentes de Transmissão: " + iqtdpen;
                        Integer iqtdp = sqtdp.length();
                        if (iqtdp < 32) {//se a quantidade de caracteres for menor que 32 vai completar
                            Integer iresto = (32 - iqtdp);
                            while (sespacop.length() < iresto) {
                                sespacop = " " + sespacop;
                            }
                        }
                        String stotpen = "Pendentes de Transmissao: " + sespacop + iqtdpen;
                        out.write(stotpen.getBytes(StandardCharsets.UTF_8));
                        out.write(EscPosBase.nextLine(2));
                    }

                    if (sespacos.equals("xx")) {
                        out.write(EscPosBase.nextLine(2));
                    } else {
                        int iesp = Integer.parseInt(sespacos);
                        out.write(EscPosBase.nextLine(iesp));
                    }


                    //Limpar Base temporaria
                    RELTMP.deletar_TMP();


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





}
