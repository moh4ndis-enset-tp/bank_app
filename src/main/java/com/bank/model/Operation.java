package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class Operation {
    private static final AtomicInteger SEQ = new AtomicInteger(1);

    private final int id;
    private final LocalDateTime date;
    private final OperationType type;
    private final BigDecimal montant;

    public Operation(OperationType type, BigDecimal montant) {
        this.id = SEQ.getAndIncrement();
        this.date = LocalDateTime.now();
        this.type = type;
        this.montant = montant;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public OperationType getType() {
        return type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String signe = (type == OperationType.VERSEMENT) ? "+" : "-";
        return "[" + id + "] " + date.format(fmt) + " " + type + " " + signe + montant + " EUR";
    }
}