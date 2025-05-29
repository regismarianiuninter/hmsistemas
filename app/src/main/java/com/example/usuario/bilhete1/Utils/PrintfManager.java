package com.example.usuario.bilhete1.Utils; // Corrigido de "Manaer" para "Manager"

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.usuario.bilhete1.DB_EMP;
import com.example.usuario.bilhete1.EscPosBase;
import com.example.usuario.bilhete1.R;
import com.example.usuario.bilhete1.Utils.Mode;
import com.example.usuario.bilhete1.ViaActivity;
import com.sunmi.utils.ThreadPoolManager;


import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PrintfManager {
    private static final String TAG = "PrintfManager";
    public static final int WIDTH_PIXEL = 384; // 58 mm, 203 dpi
    private static PrintfManager instance;
    private final Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private final List<BluetoothChangLister> bluetoothChangListers = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private boolean isHasPrinter = false;
    private boolean isConnecting = false;
    private String connectedDeviceName;
    private String connectedDeviceAddress;
    private ConnectSuccess connectSuccess;
    private String sgabre = "S";

    public interface BluetoothChangLister {
        void chang(String name, String address);
    }

    public interface ConnectSuccess {
        void success();
    }

    private PrintfManager(Context context) {
        this.context = context.getApplicationContext();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static synchronized PrintfManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrintfManager(context);
        }
        return instance;
    }

    public void setConnectSuccess(ConnectSuccess connectSuccess) {
        this.connectSuccess = connectSuccess;
    }

    public void addBluetoothChangLister(BluetoothChangLister lister) {
        bluetoothChangListers.add(lister);
    }

    public void removeBluetoothChangLister(BluetoothChangLister lister) {
        if (lister != null) {
            bluetoothChangListers.remove(lister);
        }
    }

    public boolean isConnect() {
        return isHasPrinter && bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    public boolean isCONNECTING() {
        return isConnecting;
    }

    @SuppressLint("MissingPermission")
    public void openPrinter(BluetoothDevice device) {
        executor.execute(() -> {
            isConnecting = true;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //SharedPreferencesManager.saveBluetoothName(context, device.getName());
            //SharedPreferencesManager.saveBluetoothAddress(context, device.getAddress());
            SharedPreferencesManager.saveBluetoothName(context, "DTS2500");
            SharedPreferencesManager.saveBluetoothAddress(context, "DC:0D:30:40:3D:C4");
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                isHasPrinter = true;
                connectedDeviceName = device.getName();
                connectedDeviceAddress = device.getAddress();
                handler.post(() -> {
                    //Toast.makeText(context, "Conexão bem-sucedida", Toast.LENGTH_SHORT).show();
                    for (BluetoothChangLister lister : bluetoothChangListers) {
                        lister.chang(connectedDeviceName, connectedDeviceAddress);
                    }
                    if (connectSuccess != null) {
                        connectSuccess.success();
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Falha na conexão", e);
                disConnect("Falha na conexão");
            } finally {
                isConnecting = false;
            }
        });
    }

    public void disConnect(String message) {
        executor.execute(() -> {
            isHasPrinter = false;
            try {
                if (outputStream != null) outputStream.close();
                if (bluetoothSocket != null) bluetoothSocket.close();
                handler.post(() -> {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    for (BluetoothChangLister lister : bluetoothChangListers) {
                        lister.chang(null, null);
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Erro ao fechar conexão", e);
            }
        });
    }

    public void defaultConnection(Context context) {
        String address = SharedPreferencesManager.getBluetoothAddress(context);
        if (address == null || bluetoothAdapter == null) return;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            if (device.getAddress().equals(address)) {
                openPrinter(device);
                return;
            }
        }
    }

    public void printf_50(String companyName, String operator, String remark, List<Mode> modeList) {
        if (!isConnect()) {
            handler.post(() -> Toast.makeText(context, "Impressora não conectada", Toast.LENGTH_SHORT).show());
            return;
        }
        executor.execute(() -> {
            try {
                handler.post(() -> Toast.makeText(context, "Imprimindo...", Toast.LENGTH_SHORT).show());
                // Inicializar impressora
                outputStream.write(new byte[]{0x1B, 0x40});
                // Imprimir logotipo (se disponível)
                // Nota: Comentei a impressão de imagem para simplificar; descomente se necessário
                /*
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
                int left = getCenterLeft(48, bitmap);
                byte[] bytes = bitmap2PrinterBytes(bitmap, left);
                outputStream.write(bytes);
                */
                // Imprimir dados
                printTwoColumn(context.getString(R.string.company_name), companyName);
                printfWrap();
                printTwoColumn(context.getString(R.string.operator), operator);
                printfWrap();
                printTwoColumn(context.getString(R.string.time), Util.stampToDate(System.currentTimeMillis()));
                printfWrap();
                printPlusLine_50();
                outputStream.write(context.getString(R.string.type).getBytes(Charset.forName("UTF-8")));
                printTabSpace(6);
                outputStream.write(context.getString(R.string.number).getBytes(Charset.forName("UTF-8")));
                printTabSpace(4);
                outputStream.write(context.getString(R.string.price).getBytes(Charset.forName("UTF-8")));
                printfWrap();
                for (Mode mode : modeList) {
                    String name = mode.getName();
                    outputStream.write(name.getBytes(Charset.forName("UTF-8")));
                    int supplementNumber = name.getBytes().length - context.getString(R.string.type).getBytes().length;
                    printTabSpace(6 - supplementNumber);
                    String number = String.valueOf(mode.getNumber());
                    outputStream.write(number.getBytes(Charset.forName("UTF-8")));
                    supplementNumber = number.getBytes().length - context.getString(R.string.number).getBytes().length;
                    printTabSpace(4 - supplementNumber);
                    String totalPrice = String.valueOf(mode.getPrice() * mode.getNumber());
                    outputStream.write(totalPrice.getBytes(Charset.forName("UTF-8")));
                    printfWrap();
                }
                printPlusLine_50();
                outputStream.write(context.getString(R.string.remarks).getBytes(Charset.forName("UTF-8")));
                printfWrap();
                outputStream.write(remark.getBytes(Charset.forName("UTF-8")));
                printfWrap(4);
                outputStream.write(new byte[]{0x1D, 0x56, 0x00}); // Cortar papel
                outputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Erro ao imprimir", e);
                handler.post(() -> Toast.makeText(context, "Erro ao imprimir", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void printf_Texto(String scomando, String position, String fontsize, String style, String text, int lines) {
        if (!isConnect()) {
            handler.post(() -> Toast.makeText(context, "Impressora não conectada", Toast.LENGTH_SHORT).show());
            return;
        }
        executor.execute(() -> {
              try {
                  // Inicializar impressora
                  if (scomando.equals("A")) {//Inicializa impressora
                     // outputStream.write(new byte[]{0x1B, 0x40});
                  }
                  if (position.equals("L")) {
                      outputStream.write(EscPosBase.alignLeft());
                  }//lado esquerdo
                  if (position.equals("C")) {
                      outputStream.write(EscPosBase.alignCenter());
                  }//Centralizado
                  if (position.equals("R")) {
                      outputStream.write(EscPosBase.alignRight());
                  }//lado direito
                  if (fontsize.equals("S")) {
                      outputStream.write((EscPosBase.getFontsmall()));
                  }//tamanho reduzido
                  if (fontsize.equals("N")) {
                      outputStream.write((EscPosBase.getFontNormal()));
                  }//tamanho normal
                  if (fontsize.equals("G")) {
                      outputStream.write((EscPosBase.getFontTall()));
                  }//tamanho grande
                  if (style.equals("B")) {
                      outputStream.write(EscPosBase.getFontWBold());
                  } //Fonte em Negrito
                  if (style.equals("N")) {
                      outputStream.write(EscPosBase.getFontWNormal());
                  } //Desativar Fonte em Negrito

                  outputStream.write(text.getBytes(Charset.forName("UTF-8")));

                  printfWrap(lines); //Quantidade de Linhas em Branco
                  if (scomando.equals("F")) {//Finaliza impressora
                      outputStream.write(new byte[]{0x1D, 0x56, 0x00}); // Cortar papel
                  }

                  outputStream.flush();
              } catch(Exception e){
                  e.printStackTrace();
                  System.out.println("Erro Imp01: "+e.toString());
              }

            });
    }

    public void printf_QRcode(String scomando, String position, byte[] text) {
        if (!isConnect()) {
            handler.post(() -> Toast.makeText(context, "Impressora não conectada", Toast.LENGTH_SHORT).show());
            return;
        }
        executor.execute(() -> {
                try {
                    //handler.post(() -> Toast.makeText(context, "Imprimindo...", Toast.LENGTH_SHORT).show());
                    // Inicializar impressora
                    if (scomando.equals("A")) {//Inicializa impressora
                       // outputStream.write(new byte[]{0x1B, 0x40});
                        outputStream.write(EscPosBase.alignCenter());
                        outputStream.write(text);
                        printfWrap(2); //Quantidade de Linhas em Branco

                        //Finaliza impressora
                       // outputStream.write(new byte[]{0x1D, 0x56, 0x00}); // Cortar papel
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("Erro Imp02: " + e.toString());
                }
            });
    }





    public void printf_bold_size(String text, int size, boolean isBold) {
        if (!isConnect()) return;
        try {
            outputStream.write(new byte[]{0x1B, 0x40}); // Inicializar
            outputStream.write(isBold ? boldOn() : boldOff()); // Negrito
            outputStream.write(new byte[]{0x1D, 0x21, (byte) ((size > 0 && size <= 8) ? (size - 1) : 0)}); // Tamanho
            outputStream.write(text.getBytes(Charset.forName("UTF-8")));
            outputStream.write(new byte[]{0x0A, 0x1B, 0x45, 0x00, 0x1D, 0x21, 0x00}); // Nova linha, reset
            printfWrap(2);
            outputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Erro ao imprimir texto em negrito", e);
        }
    }

    public void printBufferedText(List<PrintCommand> commands) {
        if (!isConnect()) {
            handler.post(() -> Toast.makeText(context, "Impressora não conectada", Toast.LENGTH_SHORT).show());
            return;
        }
        sgabre = "S";
        executor.execute(() -> {
            try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                String sabre = sgabre;
                if (sabre.equals("S")) {
                    //buffer.write(new byte[]{0x1B, 0x40}); // Inicializar impressora
                    sgabre = "";
                }
                for (PrintCommand cmd : commands) {
                   // buffer.write(new byte[]{ 0x12 }); //Desativar texto condensado
                    if (cmd.position.equals("L")) {
                        buffer.write(EscPosBase.alignLeft());
                    } else if (cmd.position.equals("C")) {
                        buffer.write(EscPosBase.alignCenter());
                    } else if (cmd.position.equals("R")) {
                        buffer.write(EscPosBase.alignRight());
                    }
                    if (cmd.fontsize.equals("S")) {
                       // buffer.write(new byte[]{0x1B, 0x0F });//Ativar texto condensado
                        buffer.write(EscPosBase.condensado());
                    } else if (cmd.fontsize.equals("N")) {
                        buffer.write(EscPosBase.getFontNormall());
                    } else if (cmd.fontsize.equals("G")) {
                        buffer.write(EscPosBase.getFontLarge());
                    }
                    if (cmd.style.equals("B")) {
                        buffer.write(EscPosBase.getFontWBold());
                    } else if (cmd.style.equals("N")) {
                        buffer.write(EscPosBase.getFontWNormal());
                    }
                    buffer.write(cmd.text.getBytes(Charset.forName("UTF-8")));
                    buffer.write(EscPosBase.pordefecto());
                    for (int i = 0; i < cmd.lines; i++) {
                        buffer.write(new byte[]{0x0A});
                    }
                }
                //buffer.write(new byte[]{0x1D, 0x56, 0x00}); // Cortar papel
                outputStream.write(buffer.toByteArray());
                outputStream.flush();

            } catch (IOException e) {
                Log.e(TAG, "Erro ao imprimir buffer", e);
                handler.post(() -> Toast.makeText(context, "Erro ao imprimir", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void printf_80(String companyName, String operator, String remark, List<Mode> modeList) {
        // AR-2500 é 58 mm, então redireciona para printf_50
        printf_50(companyName, operator, remark, modeList);
    }

    public static int getCenterLeft(int paperWidth, Bitmap bitmap) {
        int width = bitmap.getWidth();
        float bitmapPaperWidth = width / 8f; // 1 mm = 8 px
        return (int) (paperWidth / 2f - bitmapPaperWidth / 2);
    }

    public static byte[] bitmap2PrinterBytes(Bitmap bitmap, int left) {
        // Implementação simplificada; considere usar uma biblioteca como escpos-coffee para imagens
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] imgbuf = new byte[(width / 8 + left + 4) * height];
        byte[] bitbuf = new byte[width / 8];
        int s = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 8; x++) {
                int value = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int pixel = bitmap.getPixel(x * 8 + bit, y);
                    int gray = (int) (0.299 * ((pixel >> 16) & 0xFF) +
                            0.587 * ((pixel >> 8) & 0xFF) +
                            0.114 * (pixel & 0xFF));
                    value |= (gray <= 190 ? 1 : 0) << (7 - bit);
                }
                bitbuf[x] = (byte) value;
            }
            imgbuf[s++] = 0x16; // ESC v
            imgbuf[s++] = (byte) (width / 8 + left);
            for (int i = 0; i < left; i++) imgbuf[s++] = 0;
            for (int i = 0; i < width / 8; i++) imgbuf[s++] = bitbuf[i];
            imgbuf[s++] = 0x15; // ESC u
            imgbuf[s++] = 1;
        }
        return imgbuf;
    }

    private void printTabSpace(int length) throws IOException {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < length; i++) {
            space.append(" ");
        }
        outputStream.write(space.toString().getBytes(Charset.forName("UTF-8")));
    }

    private void printTwoColumn(String title, String content) throws IOException {
        byte[] buffer = new byte[100];
        int pos = 0;
        byte[] titleBytes = title.getBytes(Charset.forName("UTF-8"));
        System.arraycopy(titleBytes, 0, buffer, pos, titleBytes.length);
        pos += titleBytes.length;
        byte[] location = setLocation(getOffset(content));
        System.arraycopy(location, 0, buffer, pos, location.length);
        pos += location.length;
        byte[] contentBytes = content.getBytes(Charset.forName("UTF-8"));
        System.arraycopy(contentBytes, 0, buffer, pos, contentBytes.length);
        outputStream.write(buffer, 0, pos + contentBytes.length);
    }

    private void printfWrap() throws IOException {
        printfWrap(1);
    }

    private void printfWrap(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            outputStream.write(new byte[]{0x0A});
        }
    }

    private byte[] setLocation(int offset) {
        byte[] bs = new byte[4];
        bs[0] = 0x1B; // ESC
        bs[1] = 0x24; // $
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    private int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        for (char c : str.toCharArray()) {
            pixLength += (c > 127) ? 24 : 12; // Aproximação para UTF-8
        }
        return pixLength;
    }

    private void printPlusLine_50() throws IOException {
        outputStream.write("- - - - - - - - - - - - - - - -\n".getBytes(Charset.forName("UTF-8")));
    }

    private void printPlusLine_80() throws IOException {
        printPlusLine_50(); // AR-2500 usa 58 mm
    }

    public static byte[] boldOn() {
        return new byte[]{0x1B, 0x45, 0x01};
    }

    public static byte[] boldOff() {
        return new byte[]{0x1B, 0x45, 0x00};
    }

    public void changBlueName(String name) {
        // AR-2500 pode não suportar comandos AT; comentei por segurança
        handler.post(() -> Toast.makeText(context, "Alteração de nome não suportada", Toast.LENGTH_SHORT).show());
    }

    public static class PrintCommand {
        String position, fontsize, style, text;
        int lines;

        public PrintCommand(String position, String fontsize, String style, String text, int lines) {
            this.position = position;
            this.fontsize = fontsize;
            this.style = style;
            this.text = text;
            this.lines = lines;
        }
    }


}

