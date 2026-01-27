package com.tariqbashir.expensetracker.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtils {

    private MoneyUtils() {}

    public static final int SCALE = 2;

    // Ensures proper money scale
    public static BigDecimal normalize(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO;
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    // Safe addition
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return normalize(a.add(b));
    }

    // Safe subtraction
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return normalize(a.subtract(b));
    }

    // Format currency nicely
    public static String format(BigDecimal amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(normalize(amount));
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}
