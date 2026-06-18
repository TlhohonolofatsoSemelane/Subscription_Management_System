package com.subtrack.client.util;

import com.subtrack.client.dto.SubscriptionRowDto;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PdfExportUtil {

    public static File exportSubscriptions(List<SubscriptionRowDto> rows, File file, String username) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
        Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD);

        document.add(new Paragraph("SubTrack - Subscription Report", titleFont));
        document.add(new Paragraph("User: " + username, normalFont));
        document.add(new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), normalFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.2f, 3.0f, 2.0f, 2.0f, 2.5f});

        addHeaderCell(table, "ID", headerFont);
        addHeaderCell(table, "Name", headerFont);
        addHeaderCell(table, "Amount", headerFont);
        addHeaderCell(table, "Cycle", headerFont);
        addHeaderCell(table, "Next Payment", headerFont);

        if (rows != null) {
            for (SubscriptionRowDto r : rows) {
                table.addCell(new Phrase(String.valueOf(r.getId()), normalFont));
                table.addCell(new Phrase(safe(r.getName()), normalFont));
                table.addCell(new Phrase(String.valueOf(r.getAmount()), normalFont));
                table.addCell(new Phrase(safe(r.getBillingCycle()), normalFont));
                table.addCell(new Phrase(safe(r.getNextPaymentDate()), normalFont));

            }
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total records: " + (rows == null ? 0 : rows.size()), normalFont));

        document.close();
        return file;
    }

    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        table.addCell(cell);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
