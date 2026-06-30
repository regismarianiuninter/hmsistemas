package com.example.usuario.bilhete1.Utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public final class QrUtils {
    private QrUtils() {}
    public static Bitmap makeQr(String payload, int sizePx) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bm = writer.encode(payload == null ? "" : payload, BarcodeFormat.QR_CODE, sizePx, sizePx);
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        for (int x = 0; x < bm.getWidth(); x++) {
            for (int y = 0; y < bm.getHeight(); y++) {
                bmp.setPixel(x, y, bm.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }
}
