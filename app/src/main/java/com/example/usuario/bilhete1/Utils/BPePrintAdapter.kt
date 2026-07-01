package com.example.usuario.bilhete1.Utils;

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

/**
 * Adapter simples que rende uma única página a partir de um @Composable.
 * Para conteúdo multipágina, fracionar em múltiplas páginas chamando startPage/finishPage.
 */
class BPePrintAdapter(
    private val context: Context,
    private val title: String,
    private val data: PrintData,
    private val content: @Composable (PrintData) -> Unit
) : PrintDocumentAdapter() {

    private var pdfDocument: PdfDocument? = null
    private var pageWidth = 0
    private var pageHeight = 0
    private lateinit var composeView: ComposeView

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        pdfDocument = PdfDocument()
        // Tamanho em pontos (1/72in) aproximado usando mils
        pageWidth  = ((newAttributes.mediaSize?.widthMils ?: 21000) / 1000f * 72).toInt()
        pageHeight = ((newAttributes.mediaSize?.heightMils ?: 29700) / 1000f * 72).toInt()

        composeView = ComposeView(context).apply {
            setContent { content(data) }
            // Mede com largura da página; altura wrap
            measure(
                View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            layout(0, 0, pageWidth, measuredHeight)
        }

        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        callback.onLayoutFinished(
            PrintDocumentInfo.Builder("$title.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(1) // ajuste para multipágina
                .build(),
            true
        )
    }

    override fun onWrite(
        pages: Array<android.print.PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        val doc = pdfDocument ?: PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = doc.startPage(pageInfo)

        // Desenha o composeView no canvas (escala se necessário)
        val canvas = page.canvas
        val scale = if (composeView.height > pageHeight) pageHeight / composeView.height.toFloat() else 1f
        canvas.save()
        canvas.scale(scale, scale)
        composeView.draw(canvas)
        canvas.restore()

        doc.finishPage(page)
        try {
            doc.writeTo(java.io.FileOutputStream(destination.fileDescriptor))
            callback.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))
        } catch (e: Exception) {
            callback.onWriteFailed(e.message)
        } finally {
            doc.close()
        }
    }
}

fun Context.printBPe(title: String, data: PrintData) {
    val pm = getSystemService(Context.PRINT_SERVICE) as PrintManager
    pm.print(title, BPePrintAdapter(this, title, data) { BPeDoc(it) },
        PrintAttributes.Builder()
            .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .build()
    )
}
