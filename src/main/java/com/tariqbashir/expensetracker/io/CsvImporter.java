package com.tariqbashir.expensetracker.io;

import com.tariqbashir.expensetracker.exception.CsvParseException;
import com.tariqbashir.expensetracker.model.Category;
import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.util.CsvUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CsvImporter {

    public List<Transaction> importFrom(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new CsvParseException("File not found: " + path);
            }

            List<String> lines = Files.readAllLines(path);
            List<Transaction> result = new ArrayList<>();

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;

                // Skip header if present
                if (i == 0 && line.toLowerCase(Locale.ROOT).startsWith("id,")) {
                    continue;
                }

                List<String> cols = CsvUtils.splitCsvLine(line);

                if (cols.size() < 5) {
                    throw new CsvParseException("Invalid CSV row (need at least 5 columns) at line " + (i + 1));
                }

                UUID id = parseId(cols.get(0));
                LocalDate date = LocalDate.parse(cols.get(1));
                String merchant = cols.get(2);
                BigDecimal amount = new BigDecimal(cols.get(3));
                Category category = Category.valueOf(cols.get(4).trim().toUpperCase(Locale.ROOT));
                String notes = (cols.size() >= 6) ? cols.get(5) : "";

                Transaction tx = new Transaction(id, date, merchant, amount, category, notes);
                result.add(tx);
            }

            return result;

        } catch (CsvParseException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvParseException("Failed to import CSV: " + e.getMessage(), e);
        }
    }

    private UUID parseId(String s) {
        if (s == null) return UUID.randomUUID();
        String v = s.trim();
        if (v.isEmpty()) return UUID.randomUUID();
        return UUID.fromString(v);
    }
}
