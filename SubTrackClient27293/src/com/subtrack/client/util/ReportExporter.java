package com.subtrack.client.util;

import com.subtrack.client.dto.SubscriptionRowDto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReportExporter {

    private ReportExporter() {
        // utility class
    }

    public static void exportCsv(List<SubscriptionRowDto> rows, File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            // header
            pw.println("ID,Name,Amount,BillingCycle,NextPaymentDate");

            if (rows == null) return;

            for (SubscriptionRowDto r : rows) {
                pw.println(
                        r.getId() + "," +
                        esc(r.getName()) + "," +
                        r.getAmount() + "," +
                        esc(r.getBillingCycle()) + "," +
                        esc(r.getNextPaymentDate())
                );
            }
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        String v = s.replace("\"", "\"\"");
        return "\"" + v + "\"";
    }
}
