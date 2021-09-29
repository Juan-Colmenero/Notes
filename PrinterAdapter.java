package com.shopravis.ravisrunner.Adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import com.shopravis.ravisrunner.Model.ExpressItemModel;
import com.shopravis.ravisrunner.PDFFrameGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrinterAdapter extends PrintDocumentAdapter {
    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalPages;
    public ArrayList<String> tableLabels;
    public ArrayList<Integer> columnWidths;
    private final ArrayList<ExpressItemModel> expressItemModelArrayList;
    private final int ITEMS_PER_PAGE = 30;
    private final int itemCount;
    private final String customerPhone;
    private final String timestamp;

    public PrinterAdapter(Context context, ArrayList<ExpressItemModel> expressItemModelArrayList, String customerPhone, String timestamp, int itemCount) {
        this.context = context;
        this.expressItemModelArrayList = expressItemModelArrayList;
        this.customerPhone = customerPhone;
        this.timestamp = timestamp;
        this.itemCount = itemCount;

        totalPages = generateNumberOfPages();
    }

    private Integer generateNumberOfPages() {
        return (int) Math.ceil(expressItemModelArrayList.size() / 30.0);
    }

    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        pagenumber++; // Make sure page numbers start at 1

        drawRavisHeader(page);
        drawCustomerHeader(page);
        drawTableLabels(page);
        drawItems(page, pagenumber);
    }

    public void drawRavisHeader(PdfDocument.Page page){
        PDFFrameGenerator pdfFrameGenerator = new PDFFrameGenerator(page, 32, 32, 14);

        pdfFrameGenerator.drawCanvasText("Company Name", 0, 32);
        pdfFrameGenerator.drawCanvasText("Company Name 2", 0, 75);
        pdfFrameGenerator.drawCanvasText("Street Address", 0, 95);
        pdfFrameGenerator.drawCanvasText("Dallas, TX", 0, 115);
        pdfFrameGenerator.drawCanvasText("Phone Number", 0, 135);
    }

    public void drawCustomerHeader(PdfDocument.Page page){
        PDFFrameGenerator pdfFrameGenerator = new PDFFrameGenerator(page, 412, 32, 14);

        pdfFrameGenerator.drawCanvasText("Customer Information", 0, 75);
        pdfFrameGenerator.drawCanvasText("Last 4: " + customerPhone, 0, 95);
        pdfFrameGenerator.drawCanvasText("# of Items: " + itemCount, 0, 115);
        pdfFrameGenerator.drawCanvasText(timestamp, 0, 135);
    }

    public void drawTableLabels(PdfDocument.Page page){
        PDFFrameGenerator pdfFrameGenerator = new PDFFrameGenerator(page, 16, 200, 14);

        generateTableLabelsArray();
        generateColumnWidthsArray();

        int startXCordinate = 3;
        for(int tearOffIndex = 0; tearOffIndex < tableLabels.size(); tearOffIndex++){
            pdfFrameGenerator.drawCanvasText(tableLabels.get(tearOffIndex), startXCordinate, 0);

            startXCordinate += columnWidths.get(tearOffIndex) + 6;
        }
    }

    public void drawItems(PdfDocument.Page page, int pageNumber){
        PDFFrameGenerator pdfFrameGenerator = new PDFFrameGenerator(page, 16, 215, 10);

        int startingYCordinate = 0;
        int previousPage  = pageNumber - 1;
        int lastIndex = getLastIndex(pageNumber);
        int currentIndex = previousPage * ITEMS_PER_PAGE;

        for(int itemIndex = currentIndex; itemIndex < lastIndex - 1; itemIndex++){
            ExpressItemModel itemModel = expressItemModelArrayList.get(itemIndex);
            int startingXCordinate =  3;

            for(int tearOffIndex = 0; tearOffIndex < tableLabels.size(); tearOffIndex++){
                pdfFrameGenerator.drawCanvasText(getItemList(itemModel).get(tearOffIndex), startingXCordinate, startingYCordinate);

                startingXCordinate += columnWidths.get(tearOffIndex) + 6;
            }
            startingYCordinate += 15;
        }
    }

    public List<String> getItemList (ExpressItemModel expressItemModel){
        List<String> itemList = new ArrayList<String>();
        itemList.add(String.valueOf(expressItemModel.getPart_number()));
        itemList.add(String.valueOf(expressItemModel.getPackSize()));
        itemList.add(String.valueOf(expressItemModel.getName()));
        itemList.add(String.valueOf(expressItemModel.getSize1()));
        itemList.add(String.valueOf(expressItemModel.getSize2()));
        itemList.add(String.valueOf(expressItemModel.getSize3()));
        itemList.add(String.valueOf(expressItemModel.getQuantity()));
        itemList.add(String.valueOf(Math.round(expressItemModel.getPrice() * 100.0) / 100.0));
        itemList.add(String.valueOf(expressItemModel.getUnit_type()));
        itemList.add(String.valueOf(
                Math.round((expressItemModel.getPrice() * expressItemModel.getQuantity()) * 100.0) / 100.0)
        );
        return itemList;
    }

    public int getLastIndex(int pageNumber){
        if (getItemsListLastIndex(pageNumber, ITEMS_PER_PAGE) < getPagesLastIndex(pageNumber, ITEMS_PER_PAGE)){
            return expressItemModelArrayList.size();
        }else{
            return pageNumber * ITEMS_PER_PAGE;
        }
    }

    public int getItemsListLastIndex(int pageNumber, int ITEMS_PER_PAGE){
        int previousPage = pageNumber - 1;

        return Math.abs((previousPage * ITEMS_PER_PAGE) - expressItemModelArrayList.size()) ;
    }


    public int getPagesLastIndex(int pageNumber, int ITEMS_PER_PAGE){
        return (pageNumber * ITEMS_PER_PAGE);
    }

    public void generateTableLabelsArray(){
        tableLabels = new ArrayList<>();

        tableLabels.add("Item#");
        tableLabels.add("Units");
        tableLabels.add("Description");
        tableLabels.add("Size1");
        tableLabels.add("Size2");
        tableLabels.add("Size3");
        tableLabels.add("Qty");
        tableLabels.add("Price");
        tableLabels.add("Unit");
        tableLabels.add("Total");
    }

    public void generateColumnWidthsArray(){
        columnWidths = new ArrayList<>();

        columnWidths.add(40);
        columnWidths.add(30);
        columnWidths.add(200);
        columnWidths.add(32);
        columnWidths.add(32);
        columnWidths.add(32);
        columnWidths.add(47);
        columnWidths.add(47);
        columnWidths.add(30);
        columnWidths.add(57);
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight = 792;
        pageWidth = 612;

        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalPages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalPages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }

    }

    @Override
    public void onWrite(final PageRange[] pageRanges,
                        final ParcelFileDescriptor destination,
                        final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {

        for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
            if (pageInRange(pageRanges, pageIndex))
            {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, pageIndex).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, pageIndex);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pageRanges);

    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int pageIndex = 0; pageIndex<pageRanges.length; pageIndex++) {
            if ((page >= pageRanges[pageIndex].getStart()) &&
                    (page <= pageRanges[pageIndex].getEnd()))
                return true;
        }
        return false;
    }
}
