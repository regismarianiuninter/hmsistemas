package com.example.usuario.bilhete1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.usuario.bilhete1.Utils.PrintfBlueListActivity;

public class ParametrosActivity extends AppCompatActivity {

    private static String Nome_user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        Intent Newintent = getIntent();
        Bundle bundle = Newintent.getExtras();
        String user = bundle.getString("USUARIO");
        Nome_user = user;


        TextView txtnomeemp = findViewById(R.id.txtEmpresa);
        EditText edtpvenda = findViewById(R.id.edtPvenda);
        EditText edtemb = findViewById(R.id.edtTxEmb);
        EditText edtimp = findViewById(R.id.edtImpressora);
        EditText edtid = findViewById(R.id.edtIDimp);
        DB_EMP dbemp = new DB_EMP(ParametrosActivity.this);
        txtnomeemp.setText(dbemp.Busca_Dados_Emp(1, "Descri"));
        edtpvenda.setText(dbemp.Busca_Dados_Emp(1, "Pvenda"));
        edtemb.setText(dbemp.Busca_Dados_Emp(1, "Rsv001"));
        edtimp.setText(dbemp.Busca_Dados_Emp(1, "Nomimp"));
        edtid.setText(dbemp.Busca_Dados_Emp(1, "Codimp"));
        Button btnsalvar = findViewById(R.id.btnSalvar);
        edtpvenda.setEnabled(false);
        edtemb.setEnabled(false);
        btnsalvar.setEnabled(false);
        //Verificar usuario logado
        String susuario = Nome_user;
        if (susuario.equals("HMINFO") ||susuario.equals("CAIXA")){
            edtpvenda.setEnabled(true);
            edtemb.setEnabled(true);
            btnsalvar.setEnabled(true);
        }
        btnsalvar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtpvenda = findViewById(R.id.edtPvenda);
                EditText edtemb = findViewById(R.id.edtTxEmb);
                String spvenda = edtpvenda.getText().toString();
                String semb = edtemb.getText().toString();
                DB_EMP dbemp = new DB_EMP(ParametrosActivity.this);
                dbemp.Atualizar_Campo_Emp("1", "Pvenda", spvenda);
                dbemp.Atualizar_Campo_Emp("1", "Rsv001", semb);

                Toast.makeText(ParametrosActivity.this, "Alterações salvas com sucesso.", Toast.LENGTH_LONG).show();



            }
        });

        Button btnimp = findViewById(R.id.btnMudaimp);
        btnimp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_EMP dbemp = new DB_EMP(ParametrosActivity.this);
                dbemp.Atualizar_Campo_Emp("1", "Nomimp", "");
                dbemp.Atualizar_Campo_Emp("1", "Codimp", "");
                edtimp.setText("");
                edtid.setText("");
                Intent myIntent = new Intent(ParametrosActivity.this, PrintfBlueListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("USUARIO", Nome_user);
                myIntent.putExtras(bundle);
                resultadoImpressora.launch(myIntent);
            }
        });
    }

    ActivityResultLauncher<Intent> resultadoImpressora = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null) {
                EditText edtimp = findViewById(R.id.edtImpressora);
                EditText edtid = findViewById(R.id.edtIDimp);
                DB_EMP dbemp = new DB_EMP(ParametrosActivity.this);
                edtimp.setText(dbemp.Busca_Dados_Emp(1, "Nomimp"));
                edtid.setText(dbemp.Busca_Dados_Emp(1, "Codimp"));
            }
        }
    });



}
