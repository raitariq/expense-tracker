package com.tariqbashir.expensetracker.cli;

import com.tariqbashir.expensetracker.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleReader {

    private final Scanner scanner;

    public ConsoleReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public UUID readUuid(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return UUID.fromString(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid UUID format. Example: 550e8400-e29b-41d4-a716-446655440000");
            }
        }
    }

    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid date. Use format YYYY-MM-DD (example: 2026-01-26).");
            }
        }
    }

    public YearMonth readYearMonth(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                // input like 2026-01
                return YearMonth.parse(s);
            } catch (Exception e) {
                System.out.println("Invalid month. Use format YYYY-MM (example: 2026-01).");
            }
        }
    }

    public BigDecimal readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Invalid amount. Example: 25.50");
            }
        }
    }

    public Category readCategory(String prompt) {
        while (true) {
            System.out.println(prompt);
            for (Category c : Category.values()) {
                System.out.println(" - " + c.name());
            }
            System.out.print("Enter category: ");
            String s = scanner.nextLine().trim();
            try {
                return Category.valueOf(s.toUpperCase(Locale.ROOT));
            } catch (Exception e) {
                System.out.println("Invalid category. Try again.");
            }
        }
    }
}
