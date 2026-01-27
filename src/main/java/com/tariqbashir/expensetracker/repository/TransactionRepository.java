package com.tariqbashir.expensetracker.repository;
import com.tariqbashir.expensetracker.model.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    List<Transaction> findAll();

    boolean deleteById(UUID id);

    Transaction update(Transaction transaction);

    void deleteAll();
}
