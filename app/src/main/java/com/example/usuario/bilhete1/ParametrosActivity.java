package com.example.usuario.bilhete1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParametrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);

        TextView txtnomeemp = findViewById(R.id.txtEmpresa);
        EditText edtpvenda = findViewById(R.id.edtPvenda);
        EditText edtemb = findViewById(R.id.edtTxEmb);
        DB_EMP dbemp = new DB_EMP(ParametrosActivity.this);
        txtnomeemp.setText(dbemp.Busca_Dados_Emp(1, "Descri"));
        edtpvenda.setText(dbemp.Busca_Dados_Emp(1, "Pvenda"));
        edtemb.setText(dbemp.Busca_Dados_Emp(1, "Rsv001"));

        Button ntnsalvar = findViewById(R.id.btnSalvar);
        ntnsalvar.setOnClickListener(new View.OnClickListener(){
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
    }



}
