package com.tariqbashir.expensetracker.service;

import com.tariqbashir.expensetracker.exception.InvalidTransactionException;
import com.tariqbashir.expensetracker.model.Category;
import com.tariqbashir.expensetracker.model.Transaction;
import com.tariqbashir.expensetracker.repository.TransactionRepository;
import com.tariqbashir.expensetracker.util.MoneyUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    // ---------- CREATE ----------
    public Transaction addTransaction(LocalDate date,
                                      String merchant,
                                      BigDecimal amount,
                                      Category category,
                                      String notes) {

        validate(date, merchant, amount, category);

        UUID id = UUID.randomUUID();
        Transaction tx = new Transaction(id, date, merchant.trim(), amount, category, normalizeNotes(notes));
        return repository.save(tx);
    }

    // ---------- READ ----------
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public Transaction getTransactionById(UUID id) {
        return repository.findById(requireId(id))
                .orElseThrow(() -> new NoSuchElementException("Transaction not found: " + id));
    }

    // ---------- UPDATE ----------
    public Transaction editTransaction(UUID id,
                                       LocalDate newDate,
                                       String newMerchant,
                                       BigDecimal newAmount,
                                       Category newCategory,
                                       String newNotes) {

        requireId(id);

        Transaction existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found: " + id));

        validate(newDate, newMerchant, newAmount, newCategory);

        existing.setDate(newDate);
        existing.setMerchant(newMerchant.trim());
        existing.setAmount(newAmount);
        existing.setCategory(newCategory);
        existing.setNotes(normalizeNotes(newNotes));

        return repository.update(existing);
    }

    // ---------- DELETE ----------
    public boolean deleteTransaction(UUID id) {
        return repository.deleteById(requireId(id));
    }

    // ---------- VALIDATION ----------
    private void validate(LocalDate date,
                          String merchant,
                          BigDecimal amount,
                          Category category) {

        if (date == null) {
            throw new InvalidTransactionException("Date must not be null");
        }
        if (merchant == null || merchant.trim().isEmpty()) {
            throw new InvalidTransactionException("Merchant must not be empty");
        }
        if (!MoneyUtils.isPositive(amount)) {
            throw new InvalidTransactionException("Amount must be positive");
        }

        if (category == null) {
            throw new InvalidTransactionException("Category must not be null");
        }
    }

    private UUID requireId(UUID id) {
        return Objects.requireNonNull(id, "id must not be null");
    }

    private String normalizeNotes(String notes) {
        if (notes == null) return "";
        return notes.trim();
    }
    public int importTransactions(List<Transaction> transactions) {
        int count = 0;
        for (Transaction tx : transactions) {
            // reuse your validation
            // (validate requires category/date/amount/merchant)
            // notes can be empty
            validate(tx.getDate(), tx.getMerchant(), tx.getAmount(), tx.getCategory());

            // save directly (keeps CSV id)
            repository.save(tx);
            count++;
        }
        return count;
    }

}
