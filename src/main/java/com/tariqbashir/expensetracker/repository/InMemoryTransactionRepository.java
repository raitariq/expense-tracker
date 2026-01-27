package com.tariqbashir.expensetracker.repository;

import com.tariqbashir.expensetracker.model.Transaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionRepository implements TransactionRepository {

    // Map is perfect for edit/delete by id (fast lookup)
    private final Map<UUID, Transaction> store = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction must not be null");
        Objects.requireNonNull(transaction.getId(), "transaction id must not be null");
        store.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Transaction> findAll() {
        // Return a stable snapshot list
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return store.remove(id) != null;
    }

    @Override
    public Transaction update(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction must not be null");
        Objects.requireNonNull(transaction.getId(), "transaction id must not be null");

        UUID id = transaction.getId();
        if (!store.containsKey(id)) {
            throw new NoSuchElementException("Transaction not found: " + id);
        }

        store.put(id, transaction);
        return transaction;
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
