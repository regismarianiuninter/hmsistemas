package com.example.usuario.bilhete1.Utils;

// PdfFilePrintAdapter.java
import android.content.Context;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.*;
import java.io.*;

public class PdfFilePrintAdapter extends android.print.PrintDocumentAdapter {
    private final Context ctx;
    private final Uri uri;

    public PdfFilePrintAdapter(Context ctx, Uri uri) {
        this.ctx = ctx;
        this.uri = uri;
    }

    @Override public void onLayout(android.print.PrintAttributes oldA, android.print.PrintAttributes newA,
                                   android.os.CancellationSignal cs, LayoutResultCallback cb, android.os.Bundle extras) {
        if (cs.isCanceled()) { cb.onLayoutCancelled(); return; }
        android.print.PrintDocumentInfo info = new android.print.PrintDocumentInfo.Builder("ticket.pdf")
                .setContentType(android.print.PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(android.print.PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build();
        cb.onLayoutFinished(info, true);
    }

    @Override public void onWrite(android.print.PageRange[] pages, android.os.ParcelFileDescriptor dest,
                                  android.os.CancellationSignal cs, WriteResultCallback cb) {
        try (java.io.InputStream in = ctx.getContentResolver().openInputStream(uri);
             java.io.OutputStream out = new java.io.FileOutputStream(dest.getFileDescriptor())) {
            byte[] buf = new byte[8192]; int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            cb.onWriteFinished(new android.print.PageRange[]{android.print.PageRange.ALL_PAGES});
        } catch (Exception e) {
            cb.onWriteFailed(e.getMessage());
        }
    }
}

