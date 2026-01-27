package com.tariqbashir.expensetracker.util;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {

    private CsvUtils() {}

    // Splits a CSV line handling quotes:  a,"b,c",d  -> [a, b,c, d]
    public static List<String> splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                inQuotes = !inQuotes;
            } else if (ch == ',' && !inQuotes) {
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        out.add(cur.toString().trim());
        return out;
    }

    public static String escape(String value) {
        if (value == null) return "";
        String v = value;
        boolean mustQuote = v.contains(",") || v.contains("\"") || v.contains("\n");
        v = v.replace("\"", "\"\"");
        return mustQuote ? "\"" + v + "\"" : v;
    }
}
