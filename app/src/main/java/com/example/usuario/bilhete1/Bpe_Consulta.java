package com.example.usuario.bilhete1;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Bpe_Consulta extends BaseAdapter {
    //CRIANDO UM OBJETO LayoutInflater PARA FAZER LINK A NOSSA VIEW(activity_linha_consultar.xml)
    private static LayoutInflater layoutInflater = null;

    //CRIANDO UMA LISTA DE BILHETES
    List<BilhetesModel> listBPE =  new ArrayList<BilhetesModel>();


    //CIRANDO UM OBJETO DA NOSSA CLASSE QUE FAZ ACESSO AO BANCO DE DADOS
    DB_BPE  dbbpe;

    //CRIANDO UM OBJETO DA NOSSA ATIVIDADE QUE CONTEM A LISTA
    private ViaActivity viaActivity;

    //CONSTRUTOR QUE VAI RECEBER A NOSSA ATIVIDADE COMO PARAMETRO E A LISTA DE BILHETES QUE VAI RETORNAR
    //DA NOSSA BASE DE DADOS
    public Bpe_Consulta(ViaActivity viaActivity, List<BilhetesModel> listBPE ) {

        this.listBPE       =  listBPE;
        this.viaActivity  =  viaActivity;
        this.layoutInflater     = (LayoutInflater) this.viaActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbbpe   = new DB_BPE(viaActivity);
    }

    //RETORNA A QUANTIDADE DE REGISTROS DA LISTA
    @Override
    public int getCount(){

        return listBPE.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    //ESSE MÉTODO SETA OS VALORES DE UM ITEM DA NOSSA LISTA DE PESSOAS PARA UMA LINHA DO NOSSO LISVIEW
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        //CRIANDO UM OBJETO DO TIPO View PARA ACESSAR O NOSSO ARQUIVO DE LAYOUT activity_linha_consultar.xml
        final View viewLinhaLista = layoutInflater.inflate(R.layout.bilhetes_lista,null);

        //VINCULANDO OS CAMPOS DO ARQUIVO DE LAYOUT(activity_linha_consultar.xml) AOS OBJETOS DECLARADOS.

        //CAMPO QUE VAI MOSTRAR O NUMERO DO BILHETE
        final TextView txtNumbpe          = (TextView) viewLinhaLista.findViewById(R.id.txtNumbpe);

        //CAMPO QUE VAI MOSTRAR O VALOR DO BILHETE
        TextView txtValorbpe            = (TextView) viewLinhaLista.findViewById(R.id.txtValorbpe);

        //CAMPO QUE VAI MOSTRAR A VIAGEM
        TextView txtVia        = (TextView) viewLinhaLista.findViewById(R.id.txtVia);

        //CAMPO QUE VAI MOSTRAR ORIGEM
        TextView txtOri        = (TextView) viewLinhaLista.findViewById(R.id.txtOri);

        //Campo que vai mostrar o Destino
        TextView txtDes        = (TextView) viewLinhaLista.findViewById(R.id.txtDes);

        //Campo que vai mostrar a situacao do BP-e
        TextView txtStatus = (TextView) viewLinhaLista.findViewById(R.id.txtStatus);


        //CRIANDO O BOTÃO  EXCLUIR PARA DELETARMOS UM REGISTRO DO BANCO DE DADOS
        Button btnImprimir             = (Button)   viewLinhaLista.findViewById(R.id.btnImprimir);

        //CRIANDO O BOTÃO PARA EDITAR UM REGISTRO CADASTRADO
        Button   btnCopiar            = (Button)   viewLinhaLista.findViewById(R.id.btnCopiar);

        //Botao para Cancelar o Bilhete
        Button   btnCancelar            = (Button)   viewLinhaLista.findViewById(R.id.btnCancelar);


        //Botao para Excluir o Bilhete nao finalizado
        Button   btnExcluir            = (Button)   viewLinhaLista.findViewById(R.id.btnExcluir);

        //SETANDO O CÓDIGO NO CAMPO DA NOSSA VIEW
        txtNumbpe.setText(String.valueOf(listBPE.get(position).getNumbpe()));

        //SETANDO O VALOR NO CAMPO DA NOSSA VIEW
        txtValorbpe.setText(String.valueOf(listBPE.get(position).getVlrpas()));

        //SETANDO A VIAGEM NO CAMPO DA NOSSA VIEW
        txtVia.setText(listBPE.get(position).getNomvia());

        //SETANDO A ORIGEM NO CAMPO DA NOSSA VIEW
        txtOri.setText(listBPE.get(position).getTreori());

        //SETANDO O DESTINO NO CAMPO DA NOSSA VIEW
        txtDes.setText(listBPE.get(position).getTredes());

        String ssit = listBPE.get(position).getSitbpe();
        String scanc = listBPE.get(position).getRsv001();
        String sstatus = "";
        if (ssit.equals("DG")) {
            sstatus = "DIGITADO";
            btnImprimir.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnImprimir.setText("");
            btnCancelar.setText("");
        }
        if (ssit.equals("BA")) {
            if (scanc.equals("S")) {
                sstatus = "CANCELADO";
                txtStatus.setTextColor(Color.RED);
                btnImprimir.setEnabled(false);
                btnCancelar.setEnabled(false);
                btnImprimir.setText("");
                btnCancelar.setText("");
            } else {
                sstatus = "AUTORIZADO";
                txtStatus.setTextColor(Color.argb(255, 30, 196, 49));
            }
        }
        if (ssit.equals("CT")) {
            if (scanc.equals("S")) {
                sstatus = "CANCELADO";
                btnImprimir.setEnabled(false);
                btnCancelar.setEnabled(false);
                btnImprimir.setText("");
                btnCancelar.setText("");
            } else {
                sstatus = "CONTINGENCIA";
            }
            txtStatus.setTextColor(Color.RED);
        }
        if (ssit.equals("CA")) {
            sstatus = "CANCELADO";
            txtStatus.setTextColor(Color.RED);
            btnImprimir.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnImprimir.setText("");
            btnCancelar.setText("");
        }
        txtStatus.setText(sstatus);




        //CRIANDO EVENTO CLICK PARA O BOTÃO DE IMPRIMIR
        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String numbil = txtNumbpe.getText().toString();
                viaActivity.Chama_Reimpressao(numbil);

               //criar aqui opicao de imprimri

                //CHAMA O MÉTODO QUE ATUALIZA A LISTA COM OS REGISTROS QUE AINDA ESTÃO NA BASE
               // AtualizarLista();

            }
        });

        //CRIANDO EVENTO CLICK PARA O BOTÃO QUE FAZER UMA COPIA IDENTICA DO BILHETE
        // DO REGISTRO.
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numbil = txtNumbpe.getText().toString();
                viaActivity.Chama_Copia(numbil);


            }
        });

        //Evento Onclick do Botao cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numbil = txtNumbpe.getText().toString();
                String sdatemi = dbbpe.Busca_Dados_Bpe(numbil, "Datemi");
                DB_EMP dbemp = new DB_EMP(viaActivity);
                String sminutos = dbemp.Busca_Dados_Emp(1, "Mincan");
                String svalida = "";
                if (sminutos.equals("")) {
                    svalida = "S";
                } else {
                    int imin = Integer.parseInt(sminutos);
                    imin = (imin * 1000);
                    boolean bvalida = Funcoes_Android.Data_Hora_Cancel(sdatemi, imin);
                    if (bvalida ) {svalida = "S";}
                }
                if (svalida.equals("S")) { //data atende o limite para cancelamento ou nao definido
                    Log.i(TAG,"Tempo Valido: ");
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    String smodimp = dbemp.Busca_Dados_Emp(1, "Modimp");
                    if (bluetoothAdapter.isEnabled() || !smodimp.equals("03")) {//se estiver ligado ou se for outro modelo de impressora
                        //Solicitar o Motivo
                        String smotivo = viaActivity.infMotivo(v, numbil);
                    } else {
                        //Bluetooth desativado
                        viaActivity.infMensagens("Bluetooth está Desativado\n\n" + "Ative antes de Prosseguir", "");
                    }
                } else { //data nao atende o limite
                    viaActivity.infMensagens("Tempo excedido para cancelaemnto.", "");
                }

            }
        });

        //CRIANDO EVENTO CLICK PARA O BOTÃO DE EXCLUIR
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numbil = txtNumbpe.getText().toString();
                viaActivity.deleta_Bilhete(numbil);

            }
        });




        return viewLinhaLista;
    }





    //ATUALIZA A LISTTA DEPOIS DE EXCLUIR UM REGISTRO
    public void AtualizarLista(){

        this.listBPE.clear();
        //this.listBPE = listBPE.SelecionarTodos();
        this.notifyDataSetChanged();
    }
}
