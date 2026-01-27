package com.tariqbashir.expensetracker.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtils {

    private DateUtils() {}

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM");

    // Parse yyyy-MM-dd safely
    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use yyyy-MM-dd");
        }
    }

    // Parse yyyy-MM safely
    public static YearMonth parseMonth(String input) {
        try {
            return YearMonth.parse(input.trim(), MONTH_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid month format. Use yyyy-MM");
        }
    }

    public static String format(LocalDate date) {
        return DATE_FORMAT.format(date);
    }

    public static String format(YearMonth month) {
        return MONTH_FORMAT.format(month);
    }

    public static LocalDate startOfMonth(YearMonth month) {
        return month.atDay(1);
    }

    public static LocalDate endOfMonth(YearMonth month) {
        return month.atEndOfMonth();
    }
}
