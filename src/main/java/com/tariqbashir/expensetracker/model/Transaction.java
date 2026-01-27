package com.tariqbashir.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Transaction {

    private UUID id;
    private LocalDate date;
    private String merchant;
    private BigDecimal amount;
    private Category category;
    private String notes;

    // constructor
    public Transaction(UUID id,
                       LocalDate date,
                       String merchant,
                       BigDecimal amount,
                       Category category,
                       String notes) {

        this.id = id;
        this.date = date;
        this.merchant = merchant;
        this.amount = amount;
        this.category = category;
        this.notes = notes;
    }

    // getters
    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getMerchant() {
        return merchant;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    // setters (needed for edit)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
