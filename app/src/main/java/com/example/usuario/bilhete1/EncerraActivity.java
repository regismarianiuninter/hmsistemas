package com.example.usuario.bilhete1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class EncerraActivity extends AppCompatActivity {

    private Button botao;

    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encerra);

        EditText edtdatenc = findViewById(R.id.edtDatenc);
        String sdata = Funcoes_Android.getCurrentUTC();
        String sano = sdata.substring(0, (4)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
        String smes = sdata.substring(5, (7));
        String sdia = sdata.substring(8, (10));
        String datvenda = sdia+"/"+smes+"/"+sano;
        edtdatenc.setText(datvenda);
        edtdatenc.setEnabled(false);


        Button btnencerrar = findViewById(R.id.btnEncerrar);
        btnencerrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                EditText edtdatenc = findViewById(R.id.edtDatenc);
                String sdatenc = edtdatenc.getText().toString();
                String sdia, smes, sano;

                DB_BPE dbbpe = new DB_BPE(EncerraActivity.this);
                DB_BPE.BpeCursor cursor = dbbpe.RetornarBpe(DB_BPE.BpeCursor.OrdenarPor.NomeCrescente);

                for (int iE = 0; iE < cursor.getCount(); iE++) {
                    cursor.moveToPosition(iE);
                    String sdatemi = cursor.getDatemi();
                    sdia = sdatemi.substring(8, (10)); //Posicao inicial(considerando 0), posicao final(desconsiderando 0)
                    smes = sdatemi.substring(5, (7));
                    sano = sdatemi.substring(0, (4));
                    sdatemi = sdia+"/"+smes+"/"+sano;
                    boolean bconfere = Funcoes_Android.Verifica_Datas(sdatenc, sdatemi);
                    if (bconfere) { //se a data esta dentro do periodo
                        String stransf = cursor.getTransf();
                        String ssit = cursor.getSitbpe();
                        if (stransf.equals("S") || ssit.equals("DG")) { //Ja transferiu para o servidor
                            String schavebpe = cursor.getChvbpe();
                            if (!schavebpe.equals("")) { //Existe Chave
                                String snome1 = schavebpe + "-procBPe.xml";
                                String snome2 = schavebpe + "-bpe.xml";

                                FileOutputStream fos;
                                try {
                                    File sdCard = getExternalFilesDir("Download");
                                    File dir = new File(sdCard.getAbsolutePath() );
                                    dir.mkdirs();
                                    File file = new File(dir, snome1);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    ///
                                    File file2 = new File(dir, snome2);
                                    if (file2.exists()) {
                                        file2.delete();
                                    }

                                } catch (Exception e) {
                                }

                                String sid = Long.toString(cursor.getID());
                                dbbpe.delete_item_Bpe(sid);
                            }
                        }
                    }

                }
                Toast.makeText(EncerraActivity.this,"Processo Finalizado" , Toast.LENGTH_SHORT).show();
            }
        });

        Button btncalend = findViewById(R.id.btnCalendar);
        btncalend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String scal = selCalendar();
                showDialog(DATE_DIALOG_ID);
            }
        });


    }



    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendario = Calendar.getInstance();


        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes,
                        dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    String sdia = String.valueOf(dayOfMonth);
                    String smes = String.valueOf(monthOfYear+1);
                    String sano = String.valueOf(year);
                    String data = ("00" + sdia).substring(sdia.length())+"/"+("00" + smes).substring(smes.length())+"/"+sano;
                    //String data = String.valueOf(dayOfMonth) + " /"
                      //      + String.valueOf(monthOfYear+1) + " /" + String.valueOf(year);
                    EditText edtdata = findViewById(R.id.edtDatenc);
                    edtdata.setText(data);
                    //Toast.makeText(EncerraActivity.this,"DATA = " + data, Toast.LENGTH_SHORT).show();
                }
            };

}
