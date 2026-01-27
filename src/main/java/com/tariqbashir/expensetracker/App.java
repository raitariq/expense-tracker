package com.tariqbashir.expensetracker;

import com.tariqbashir.expensetracker.cli.ConsolePrinter;
import com.tariqbashir.expensetracker.cli.ConsoleReader;
import com.tariqbashir.expensetracker.cli.Menu;
import com.tariqbashir.expensetracker.io.CsvExporter;
import com.tariqbashir.expensetracker.io.CsvImporter;
import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.repository.InMemoryTransactionRepository;
import com.tariqbashir.expensetracker.repository.TransactionRepository;
import com.tariqbashir.expensetracker.service.DuplicateService;
import com.tariqbashir.expensetracker.service.ReportService;
import com.tariqbashir.expensetracker.service.TransactionService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // default storage file
        Path dataFile = Paths.get("data", "transactions.csv");

        TransactionRepository repo = new InMemoryTransactionRepository();
        TransactionService transactionService = new TransactionService(repo);
        DuplicateService duplicateService = new DuplicateService(repo);
        ReportService reportService = new ReportService(repo);

        CsvImporter importer = new CsvImporter();
        CsvExporter exporter = new CsvExporter();

        // ---------- LOAD ON START ----------
        try {
            if (Files.exists(dataFile)) {
                List<Transaction> imported = importer.importFrom(dataFile);

                // Save imported into repository
                for (Transaction tx : imported) {
                    repo.save(tx);
                }

                System.out.println("Loaded transactions: " + imported.size());
            } else {
                System.out.println("No previous data found. Starting fresh.");
            }
        } catch (Exception e) {
            System.out.println("Warning: could not load data: " + e.getMessage());
        }

        // ---------- RUN MENU ----------
        ConsoleReader reader = new ConsoleReader(new Scanner(System.in));
        ConsolePrinter printer = new ConsolePrinter();
        Menu menu = new Menu(transactionService, duplicateService, reportService, reader, printer);

        // Auto-save when user exits (or CTRL+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.createDirectories(dataFile.getParent());
                List<Transaction> all = transactionService.getAllTransactions();
                exporter.exportTo(dataFile, all);
                System.out.println("\nSaved transactions: " + all.size());
            } catch (Exception e) {
                System.out.println("\nWarning: could not save data: " + e.getMessage());
            }
        }));

        menu.start();
    }
}
