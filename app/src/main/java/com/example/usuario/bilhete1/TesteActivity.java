package com.example.usuario.bilhete1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.usuario.bilhete1.Utils.PrintfBlueListActivity;
import com.example.usuario.bilhete1.Utils.PrintfManager;
import com.example.usuario.bilhete1.Utils.Mode;
import com.example.usuario.bilhete1.Utils.MyContextWrapper;
import com.example.usuario.bilhete1.Utils.Util;
import com.example.usuario.bilhete1.R;

import java.util.ArrayList;
import java.util.List;

public class TesteActivity extends AppCompatActivity {
    private static final String TAG = "TesteActivity";

    private TextView tv_main_bluetooth;
    private Button btn_main_test_80, btn_main_test_58, btn_main_test_bold;
    private RadioGroup rg_main_test;
    private RadioButton rb_main_test_bold, rb_main_test_no_bold;
    private EditText et_main_test_size;
    private List<Mode> listData;
    private PrintfManager printfManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teste_activity);
        context = this;
        initView();
        initData();
        setLister();
    }

    private void setLister() {
        btn_main_test_bold.setOnClickListener(v -> {
            String s = et_main_test_size.getText().toString();
            int size = 1;
            if (Util.isNumeric(s)) {
                size = Integer.parseInt(s);
            }
            if (printfManager == null) {
                Log.e(TAG, "PrintfManager é null");
                Util.ToastText(context, "Erro: Impressora não inicializada");
                return;
            }
            printfManager.printf_bold_size("Welcome to use\n", size, rb_main_test_bold.isChecked());
        });

        if (printfManager != null) {
            printfManager.addBluetoothChangLister((name, address) -> {
                runOnUiThread(() -> tv_main_bluetooth.setText(name));
            });
        } else {
            Log.e(TAG, "Não foi possível adicionar BluetoothChangLister: PrintfManager é null");
        }

        btn_main_test_58.setOnClickListener(v -> {
            if (printfManager == null) {
                Log.e(TAG, "PrintfManager é null");
                Util.ToastText(context, "Erro: Impressora não inicializada");
                return;
            }
            if (printfManager.isConnect()) {
                printfManager.printf_50(
                        context.getString(R.string.TEST),
                        context.getString(R.string.godown_keeper),
                        context.getString(R.string.send_printer),
                        listData
                );
            } else {
                Log.d(TAG, "Impressora não conectada, abrindo PrintfBlueListActivity");
                PrintfBlueListActivity.startActivity(TesteActivity.this);
            }
        });

        // Adicionar listener para btn_main_test_80 (estava sem implementação)
        btn_main_test_80.setOnClickListener(v -> {
            if (printfManager == null) {
                Log.e(TAG, "PrintfManager é null");
                Util.ToastText(context, "Erro: Impressora não inicializada");
                return;
            }
            if (printfManager.isConnect()) {
                // Implementar teste para 80mm, se necessário
                Util.ToastText(context, "Teste de impressão 80mm não implementado");
            } else {
                Log.d(TAG, "Impressora não conectada, abrindo PrintfBlueListActivity");
                PrintfBlueListActivity.startActivity(TesteActivity.this);
            }
        });
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
            printfManager.defaultConnection(TesteActivity.this);
        } catch (Exception e) {
            Log.e(TAG, "Erro em initData: " + e.getMessage());
            Util.ToastText(context, "Erro ao inicializar dados");
        }
    }

    private void initView() {
        btn_main_test_80 = findViewById(R.id.btn_main_test_80);
        btn_main_test_58 = findViewById(R.id.btn_main_test_58);
        tv_main_bluetooth = findViewById(R.id.tv_main_bluetooth);
        et_main_test_size = findViewById(R.id.et_main_test_size);
        btn_main_test_bold = findViewById(R.id.btn_main_test_bold);
        rg_main_test = findViewById(R.id.rg_main_test);
        rb_main_test_bold = findViewById(R.id.rb_main_test_bold);
        rb_main_test_no_bold = findViewById(R.id.rb_main_test_no_bold);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = MyContextWrapper.wrap(newBase);
        super.attachBaseContext(context);
    }
}