package com.example.usuario.bilhete1.Utils;

import android.app.PendingIntent;
import android.content.*;
import android.graphics.Bitmap;
import android.hardware.usb.*;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.usuario.bilhete1.Funcoes_Android;

import java.io.File;
import java.util.HashMap;

public class UsbEscPosPrinter {

    private static final String TAG = "UsbPrinter";
    private static final String ACTION_USB_PERMISSION = "com.example.usuario.bilhete1.USB_PERMISSION";

    private final Context appCtx;
    private final UsbManager usb;
    private UsbDevice pendingDevice; // device aguardando permissão
    private BroadcastReceiver receiver;

    public UsbEscPosPrinter(Context context) {
        this.appCtx = context.getApplicationContext();
        this.usb = (UsbManager) appCtx.getSystemService(Context.USB_SERVICE);
    }

    /** Chame este método após gerar o PDF. Ele cuida de tudo. */
    public void printPdfSilently(File pdfFile) {
        UsbDevice dev = findPrinterDevice();
        if (dev == null) {
            Toast.makeText(appCtx, "Impressora USB não encontrada", Toast.LENGTH_LONG).show();
            return;
        }

        if (usb.hasPermission(dev)) {
            // Já tem permissão → imprime
            doPrint(dev, pdfFile);
        } else {
            // Pede permissão e espera callback
            requestPermissionAndPrint(dev, pdfFile);
        }
    }

    /** Procura um UsbDevice com endpoint BULK OUT (mais comum para ESC/POS). */
    private UsbDevice findPrinterDevice() {
        HashMap<String, UsbDevice> list = usb.getDeviceList();
        for (UsbDevice d : list.values()) {
            for (int i = 0; i < d.getInterfaceCount(); i++) {
                UsbInterface inx = d.getInterface(i);
                for (int j = 0; j < inx.getEndpointCount(); j++) {
                    UsbEndpoint ep = inx.getEndpoint(j);
                    if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK &&
                            ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        Log.d(TAG, "Printer candidate: VID=" + d.getVendorId() + " PID=" + d.getProductId());
                        return d;
                    }
                }
            }
        }
        return null;
    }

    private void requestPermissionAndPrint(UsbDevice dev, File pdfFile) {
        // guarda device e registra receiver só para este ciclo
        this.pendingDevice = dev;
        Intent intent = new Intent(ACTION_USB_PERMISSION);
        PendingIntent permIntent = PendingIntent.getBroadcast(
                appCtx, 0, intent,
                Build.VERSION.SDK_INT >= 23 ? PendingIntent.FLAG_IMMUTABLE : 0
        );

        receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                if (!ACTION_USB_PERMISSION.equals(intent.getAction())) return;
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                boolean granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
                try {
                    if (granted && device != null && device.equals(pendingDevice)) {
                        doPrint(device, pdfFile);
                    } else {
                        Toast.makeText(appCtx, "Permissão USB negada", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    // limpamos o receiver
                    try { appCtx.unregisterReceiver(this); } catch (Exception ignore) {}
                    receiver = null;
                    pendingDevice = null;
                }
            }
        };
        ContextCompat.registerReceiver(appCtx, receiver, new IntentFilter(ACTION_USB_PERMISSION), ContextCompat.RECEIVER_EXPORTED);
        usb.requestPermission(dev, permIntent);
    }

    /** Abre conexão, envia o PDF como raster e fecha. */
    private void doPrint(UsbDevice dev, File pdfFile) {
        new Thread(() -> {
            UsbDeviceConnection conn = null;
            try {
                // Seleciona interface + endpoint OUT
                UsbInterface intf = null; UsbEndpoint epOut = null;
                for (int i = 0; i < dev.getInterfaceCount(); i++) {
                    UsbInterface inx = dev.getInterface(i);
                    for (int j = 0; j < inx.getEndpointCount(); j++) {
                        UsbEndpoint ep = inx.getEndpoint(j);
                        if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK &&
                                ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                            intf = inx; epOut = ep; break;
                        }
                    }
                    if (epOut != null) break;
                }
                if (intf == null || epOut == null) {
                    postToast("Endpoint OUT não encontrado");
                    return;
                }

                conn = usb.openDevice(dev);
                if (conn == null) { postToast("Falha ao abrir USB"); return; }

                if (!conn.claimInterface(intf, true)) {
                    postToast("Falha ao reivindicar interface USB");
                    return;
                }

                // 1) Renderiza PDF -> bitmap 384px; 2) Dither mono; 3) Envia ESC/POS
                // use suas funções utilitárias
                Bitmap bmp = Funcoes_Android.pdfToBitmap384(appCtx, pdfFile);
                Bitmap mono = Funcoes_Android.ditherToMono(bmp);

                // Reset ESC @
                Funcoes_Android.bulk(conn, epOut, new byte[]{0x1B, 0x40});

                // Raster GS v 0
               // Funcoes_Android.sendRaster(conn, epOut, mono);

                // Feed (3 linhas). Corte pode não existir em 58mm.
                Funcoes_Android.bulk(conn, epOut, new byte[]{0x0A, 0x0A, 0x0A});

                postToast("Impressão enviada (USB)");
            } catch (Exception e) {
                Log.e(TAG, "Erro ao imprimir USB", e);
                postToast("Erro USB: " + e.getMessage());
            } finally {
                try {
                    if (conn != null) {
                        try { conn.releaseInterface(dev.getInterface(0)); } catch (Exception ignore) {}
                        conn.close();
                    }
                } catch (Exception ignore) {}
            }
        }).start();
    }



    private void postToast(String msg) {
        android.os.Handler h = new android.os.Handler(appCtx.getMainLooper());
        h.post(() -> Toast.makeText(appCtx, msg, Toast.LENGTH_SHORT).show());
    }

    /** Chame no onDestroy da Activity, por segurança. */
    public void cleanup() {
        if (receiver != null) {
            try { appCtx.unregisterReceiver(receiver); } catch (Exception ignore) {}
            receiver = null;
        }
    }
}
