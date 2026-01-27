package com.tariqbashir.expensetracker.cli;

import com.tariqbashir.expensetracker.model.Category;
import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.service.DuplicateService;
import com.tariqbashir.expensetracker.service.ReportService;
import com.tariqbashir.expensetracker.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import com.tariqbashir.expensetracker.io.CsvExporter;
import com.tariqbashir.expensetracker.io.CsvImporter;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Menu {

    private final CsvImporter csvImporter = new CsvImporter();
    private final CsvExporter csvExporter = new CsvExporter();


    private final TransactionService transactionService;
    private final DuplicateService duplicateService;
    private final ReportService reportService;

    private final ConsoleReader reader;
    private final ConsolePrinter printer;

    public Menu(TransactionService transactionService,
                DuplicateService duplicateService,
                ReportService reportService,
                ConsoleReader reader,
                ConsolePrinter printer) {

        this.transactionService = transactionService;
        this.duplicateService = duplicateService;
        this.reportService = reportService;
        this.reader = reader;
        this.printer = printer;
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = reader.readInt("Choose: ");

            try {
                switch (choice) {
                    case 1:
                        add();
                        break;
                    case 2:
                        edit();
                        break;
                    case 3:
                        delete();
                        break;
                    case 4:
                        list();
                        break;
                    case 5:
                        removeDuplicates();
                        break;
                    case 6:
                        monthlyReport();
                        break;
                    case 7:
                        overallReport();
                        break;
                    case 8:
                        importCsv();
                        break;
                    case 9:
                        exportCsv();
                        break;
                    case 0:
                        System.out.println("Bye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("===== Expense Tracker =====");
        System.out.println("1) Add transaction");
        System.out.println("2) Edit transaction");
        System.out.println("3) Delete transaction");
        System.out.println("4) List transactions");
        System.out.println("5) Remove duplicates");
        System.out.println("6) Monthly report");
        System.out.println("7) Overall report");
        System.out.println("0) Exit");
    }

    private void add() {
        LocalDate date = reader.readDate("Date (YYYY-MM-DD): ");
        String merchant = reader.readString("Merchant: ");
        BigDecimal amount = reader.readAmount("Amount: ");
        Category category = reader.readCategory("Category options:");
        String notes = reader.readString("Notes (optional): ");

        Transaction t = transactionService.addTransaction(
                date, merchant, amount, category, notes);

        System.out.println("Added:");
        System.out.println(printer.formatTransaction(t));
    }

    private void edit() {
        UUID id = reader.readUuid("Transaction ID to edit: ");

        LocalDate date = reader.readDate("New date (YYYY-MM-DD): ");
        String merchant = reader.readString("New merchant: ");
        BigDecimal amount = reader.readAmount("New amount: ");
        Category category = reader.readCategory("New category options:");
        String notes = reader.readString("New notes (optional): ");

        Transaction updated = transactionService.editTransaction(
                id, date, merchant, amount, category, notes);

        System.out.println("Updated:");
        System.out.println(printer.formatTransaction(updated));
    }

    private void delete() {
        UUID id = reader.readUuid("Transaction ID to delete: ");
        boolean ok = transactionService.deleteTransaction(id);
        System.out.println(ok ? "Deleted." : "Transaction not found.");
    }

    private void list() {
        List<Transaction> all = transactionService.getAllTransactions();
        printer.printTransactions(all);
    }

    private void removeDuplicates() {
        int removed = duplicateService.removeDuplicates();
        System.out.println("Removed duplicates: " + removed);
    }

    private void monthlyReport() {
        YearMonth month = reader.readYearMonth("Month (YYYY-MM): ");
        ReportService.MonthlyReport report =
                reportService.generateMonthlyReport(month);
        printer.printMonthlyReport(report);
    }

    private void overallReport() {
        ReportService.OverallReport report =
                reportService.generateOverallReport();
        printer.printOverallReport(report);
    }
    private void importCsv() {
        String pathStr = reader.readString("CSV path to import: ").trim();
        Path path = Paths.get(pathStr);

        List<Transaction> imported = csvImporter.importFrom(path);
        int added = transactionService.importTransactions(imported);

        System.out.println("Imported transactions: " + added);
    }

    private void exportCsv() {
        String pathStr = reader.readString("CSV path to export to: ").trim();
        Path path = Paths.get(pathStr);

        List<Transaction> all = transactionService.getAllTransactions();
        csvExporter.exportTo(path, all);

        System.out.println("Exported transactions: " + all.size());
    }

}
