package com.tariqbashir.expensetracker.io;

import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.util.CsvUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvExporter {

    public void exportTo(Path path, List<Transaction> transactions) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,date,merchant,amount,category,notes");

            for (Transaction t : transactions) {
                String row =
                        t.getId() + "," +
                                t.getDate() + "," +
                                CsvUtils.escape(t.getMerchant()) + "," +
                                t.getAmount() + "," +
                                t.getCategory() + "," +
                                CsvUtils.escape(t.getNotes());

                lines.add(row);
            }

            Files.write(path, lines);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export CSV: " + e.getMessage(), e);
        }
    }
}
