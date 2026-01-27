package com.tariqbashir.expensetracker.service;

import com.tariqbashir.expensetracker.model.Category;
import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    private final TransactionRepository repository;

    public ReportService(TransactionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    // ---------------- MONTHLY REPORT ----------------

    public MonthlyReport generateMonthlyReport(YearMonth month) {
        Objects.requireNonNull(month, "month must not be null");

        List<Transaction> monthly = repository.findAll().stream()
                .filter(t -> YearMonth.from(t.getDate()).equals(month))
                .sorted(Comparator.comparing(Transaction::getDate))
                .collect(Collectors.toList());


        BigDecimal total = sum(monthly);

        Map<Category, BigDecimal> totalsByCategory = monthly.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        List<Transaction> top5Expenses = monthly.stream()
                .sorted(Comparator.comparing(Transaction::getAmount).reversed())
                .limit(5)
                .collect(Collectors.toList());


        return new MonthlyReport(month, monthly.size(), total, totalsByCategory, top5Expenses);
    }

    // ---------------- OVERALL REPORT ----------------

    public OverallReport generateOverallReport() {
        List<Transaction> all = repository.findAll();

        BigDecimal total = sum(all);

        Map<Category, BigDecimal> totalsByCategory = all.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        () -> new EnumMap<>(Category.class),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        Map<YearMonth, BigDecimal> totalsByMonth = all.stream()
                .collect(Collectors.groupingBy(
                        t -> YearMonth.from(t.getDate()),
                        TreeMap::new, // sorted by month
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        List<Map.Entry<Category, BigDecimal>> top5Categories = totalsByCategory.entrySet().stream()
                .sorted(Map.Entry.<Category, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());


        return new OverallReport(all.size(), total, totalsByCategory, totalsByMonth, top5Categories);
    }

    // ---------------- helpers ----------------

    private BigDecimal sum(List<Transaction> txs) {
        return txs.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------------- report DTOs (simple containers) ----------------

    public static class MonthlyReport {
        private final YearMonth month;
        private final int transactionCount;
        private final BigDecimal total;
        private final Map<Category, BigDecimal> totalsByCategory;
        private final List<Transaction> top5Expenses;

        public MonthlyReport(YearMonth month,
                             int transactionCount,
                             BigDecimal total,
                             Map<Category, BigDecimal> totalsByCategory,
                             List<Transaction> top5Expenses) {
            this.month = month;
            this.transactionCount = transactionCount;
            this.total = total;
            this.totalsByCategory = totalsByCategory;
            this.top5Expenses = top5Expenses;
        }

        public YearMonth getMonth() { return month; }
        public int getTransactionCount() { return transactionCount; }
        public BigDecimal getTotal() { return total; }
        public Map<Category, BigDecimal> getTotalsByCategory() { return totalsByCategory; }
        public List<Transaction> getTop5Expenses() { return top5Expenses; }
    }

    public static class OverallReport {
        private final int transactionCount;
        private final BigDecimal total;
        private final Map<Category, BigDecimal> totalsByCategory;
        private final Map<YearMonth, BigDecimal> totalsByMonth;
        private final List<Map.Entry<Category, BigDecimal>> top5Categories;

        public OverallReport(int transactionCount,
                             BigDecimal total,
                             Map<Category, BigDecimal> totalsByCategory,
                             Map<YearMonth, BigDecimal> totalsByMonth,
                             List<Map.Entry<Category, BigDecimal>> top5Categories) {
            this.transactionCount = transactionCount;
            this.total = total;
            this.totalsByCategory = totalsByCategory;
            this.totalsByMonth = totalsByMonth;
            this.top5Categories = top5Categories;
        }

        public int getTransactionCount() { return transactionCount; }
        public BigDecimal getTotal() { return total; }
        public Map<Category, BigDecimal> getTotalsByCategory() { return totalsByCategory; }
        public Map<YearMonth, BigDecimal> getTotalsByMonth() { return totalsByMonth; }
        public List<Map.Entry<Category, BigDecimal>> getTop5Categories() { return top5Categories; }
    }
}
