package com.bank.model;

public class Client {
    private final int id;
    private final String nom;
    private final String prenom;

    public Client(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "Client{id=" + id + ", nom='" + nom + "', prenom='" + prenom + "'}";
    }
}