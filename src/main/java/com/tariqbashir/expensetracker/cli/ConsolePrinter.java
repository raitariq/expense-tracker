package com.tariqbashir.expensetracker.cli;

import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.service.ReportService;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConsolePrinter {

    private final NumberFormat money = NumberFormat.getCurrencyInstance(Locale.UK);

    public void printTransactions(List<Transaction> txs) {
        if (txs.isEmpty()) {
            System.out.println("(no transactions)");
            return;
        }
        for (Transaction t : txs) {
            System.out.println(formatTransaction(t));
        }
    }

    public String formatTransaction(Transaction t) {
        return String.format(
                "%s | %s | %s | %s | %s | %s",
                t.getId(),
                t.getDate(),
                money.format(t.getAmount()),
                t.getCategory(),
                t.getMerchant(),
                (t.getNotes() == null || t.getNotes().trim().isEmpty())
                        ? "-"
                        : t.getNotes()
        );
    }


    public void printMonthlyReport(ReportService.MonthlyReport r) {
        System.out.println("===== MONTHLY REPORT: " + r.getMonth() + " =====");
        System.out.println("Transactions: " + r.getTransactionCount());
        System.out.println("Total: " + money.format(r.getTotal()));

        System.out.println("\n-- Totals by Category --");
        r.getTotalsByCategory().forEach((cat, amt) ->
                System.out.println(cat + " : " + money.format(amt))
        );

        System.out.println("\n-- Top 5 Expenses --");
        printTransactions(r.getTop5Expenses());
        System.out.println("====================================");
    }

    public void printOverallReport(ReportService.OverallReport r) {
        System.out.println("===== OVERALL REPORT =====");
        System.out.println("Transactions: " + r.getTransactionCount());
        System.out.println("Total: " + money.format(r.getTotal()));

        System.out.println("\n-- Totals by Category --");
        r.getTotalsByCategory().forEach((cat, amt) ->
                System.out.println(cat + " : " + money.format(amt))
        );

        System.out.println("\n-- Totals by Month --");
        for (Map.Entry<YearMonth, BigDecimal> e : r.getTotalsByMonth().entrySet()) {
            System.out.println(e.getKey() + " : " + money.format(e.getValue()));
        }

        System.out.println("\n-- Top 5 Categories --");
        r.getTop5Categories().forEach(e ->
                System.out.println(e.getKey() + " : " + money.format(e.getValue()))
        );

        System.out.println("=========================");
    }
}
