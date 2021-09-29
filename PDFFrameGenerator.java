package com.shopravis.ravisrunner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

public class PDFFrameGenerator {
    public Canvas canvas;
    public PdfDocument.Page page;
    public int xMargin;
    public int yMargin;
    public int textSize;

    public PDFFrameGenerator(PdfDocument.Page page, int xMargin, int yMargin, int textSize){
        canvas = page.getCanvas();

        this.page = page;
        this.xMargin = xMargin;
        this.yMargin = yMargin;
        this.textSize = textSize;
    }

    public void drawCanvasText(String canvasText, int xCordinate, int yCordinate){
        Canvas canvas = page.getCanvas();
        canvas.drawText(canvasText, xMargin + xCordinate, yMargin + yCordinate, getPaint());
    }


    public Paint getPaint(){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);

        return paint;
    }
}
