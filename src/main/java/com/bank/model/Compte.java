package com.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Compte {
    private final int id;
    private final String numero;
    private final Client client;
    private final List<Operation> operations = new ArrayList<>();

    // solde stocké pour lecture rapide (toujours maintenir l'invariant via méthodes contrôlées)
    private BigDecimal solde = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

    public Compte(int id, String numero, Client client) {
        this.id = id;
        this.numero = numero;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Client getClient() {
        return client;
    }

    public synchronized BigDecimal getSolde() {
        return solde;
    }

    public synchronized List<Operation> getOperations() {
        return Collections.unmodifiableList(new ArrayList<>(operations));
    }

    /**
     * Ajoute une opération et met à jour le solde de façon atomique.
     * Lance IllegalArgumentException ou IllegalStateException si règles métier non respectées.
     */
    public synchronized void ajouterOperation(Operation op) {
        if (op == null) {
            throw new IllegalArgumentException("Operation ne peut pas être null");
        }
        if (op.getMontant() == null || op.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant doit être positif");
        }

        if (op.getType() == OperationType.RETRAIT) {
            if (solde.compareTo(op.getMontant()) < 0) {
                throw new IllegalStateException("Solde insuffisant pour le retrait");
            }
            solde = solde.subtract(op.getMontant());
        } else {
            solde = solde.add(op.getMontant());
        }

        // Ajouter après avoir modifié le solde (ou avant selon votre politique). Ici on ajoute après mise à jour.
        operations.add(op);
    }

    /**
     * Réconciliation : recalcule le solde à partir de l'historique et met à jour le solde stocké.
     * Utile pour vérification / audit.
     */
    public synchronized BigDecimal recomputeSoldeFromOperations() {
        BigDecimal computed = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);
        for (Operation op : operations) {
            if (op.getType() == OperationType.VERSEMENT) {
                computed = computed.add(op.getMontant());
            } else {
                computed = computed.subtract(op.getMontant());
            }
        }
        this.solde = computed;
        return computed;
    }

    @Override
    public String toString() {
        return "Compte{id=" + id + ", numero='" + numero + "', client=" +
                client.getNom() + " " + client.getPrenom() +
                ", solde=" + solde + " EUR}";
    }
}