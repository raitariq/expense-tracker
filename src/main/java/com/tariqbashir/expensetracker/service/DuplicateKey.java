package com.tariqbashir.expensetracker.service;

import com.tariqbashir.expensetracker.model.Transaction;

import java.util.Locale;
import java.util.Objects;

public final class DuplicateKey {

    private final String key;

    private DuplicateKey(String key) {
        this.key = key;
    }

    public static DuplicateKey from(Transaction t) {
        String date = t.getDate().toString();
        String merchant = t.getMerchant().trim().toLowerCase(Locale.ROOT);
        String amount = t.getAmount().stripTrailingZeros().toPlainString();

        return new DuplicateKey(date + "|" + merchant + "|" + amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DuplicateKey)) return false;
        DuplicateKey that = (DuplicateKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
