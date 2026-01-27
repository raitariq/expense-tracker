package com.tariqbashir.expensetracker.service;

import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.repository.TransactionRepository;

import java.util.*;

public class DuplicateService {

    private final TransactionRepository repository;

    public DuplicateService(TransactionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public List<Transaction> findDuplicates() {
        List<Transaction> all = repository.findAll();

        Set<String> seen = new HashSet<>();
        List<Transaction> duplicates = new ArrayList<>();

        for (Transaction tx : all) {
            String key = buildKey(tx);
            if (!seen.add(key)) {
                duplicates.add(tx);
            }
        }
        return duplicates;
    }

    public int removeDuplicates() {
        List<Transaction> all = repository.findAll();

        Set<String> seen = new HashSet<>();
        int removed = 0;

        for (Transaction tx : all) {
            String key = buildKey(tx);
            if (!seen.add(key)) {
                if (repository.deleteById(tx.getId())) {
                    removed++;
                }
            }
        }
        return removed;
    }

    private String buildKey(Transaction tx) {
        String date = tx.getDate() == null ? "" : tx.getDate().toString();
        String merchant = tx.getMerchant() == null ? "" : tx.getMerchant().trim().toLowerCase(Locale.ROOT);
        String amount = tx.getAmount() == null ? "" : tx.getAmount().stripTrailingZeros().toPlainString();
        return date + "|" + merchant + "|" + amount;
    }
}
