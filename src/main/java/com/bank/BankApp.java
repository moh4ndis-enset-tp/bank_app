package com.bank;

import com.bank.model.Client;
import com.bank.model.Compte;
import com.bank.model.Operation;
import com.bank.model.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankApp {
    private final Map<Integer, Client> clients = new HashMap<>();
    private final Map<Integer, Compte> comptes = new HashMap<>();
    private int clientSeq = 1;
    private int compteSeq = 1;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BankApp app = new BankApp();
        app.run();
    }

    private void run() {
        System.out.println("=== Gestion de comptes bancaires ===");
        boolean exit = false;
        while (!exit) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> creerClient();
                case "2" -> creerCompte();
                case "3" -> effectuerOperation();
                case "4" -> afficherDetailCompte();
                case "5" -> listerClientsEtComptes();
                case "0" -> {
                    exit = true;
                    System.out.println("Au revoir.");
                }
                default -> System.out.println("Choix invalide. Réessayez.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1 - Créer un client");
        System.out.println("2 - Créer un compte pour un client");
        System.out.println("3 - Réaliser une opération sur un compte");
        System.out.println("4 - Afficher le détail d'un compte");
        System.out.println("5 - Lister clients et comptes");
        System.out.println("0 - Quitter");
        System.out.print("Choix: ");
    }

    private void creerClient() {
        System.out.print("Nom: ");
        String nom = scanner.nextLine().trim();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine().trim();

        Client client = new Client(clientSeq++, nom, prenom);
        clients.put(client.getId(), client);
        System.out.println("Client créé: " + client);
    }

    private void creerCompte() {
        System.out.print("ID du client (liste disponible avec l'option 5) : ");
        String in = scanner.nextLine().trim();
        int clientId;
        try {
            clientId = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
            return;
        }
        Client client = clients.get(clientId);
        if (client == null) {
            System.out.println("Client introuvable. Créez le d'abord (option 1).");
            return;
        }
        String numero = "ACC" + String.format("%06d", compteSeq);
        Compte compte = new Compte(compteSeq, numero, client);
        comptes.put(compte.getId(), compte);
        compteSeq++;
        System.out.println("Compte créé: " + compte);
    }

    private void effectuerOperation() {
        System.out.print("ID du compte: ");
        String in = scanner.nextLine().trim();
        int compteId;
        try {
            compteId = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
            return;
        }
        Compte compte = comptes.get(compteId);
        if (compte == null) {
            System.out.println("Compte introuvable.");
            return;
        }
        System.out.print("Type (1=VERSEMENT, 2=RETRAIT): ");
        String tp = scanner.nextLine().trim();
        OperationType type;
        if ("1".equals(tp)) type = OperationType.VERSEMENT;
        else if ("2".equals(tp)) type = OperationType.RETRAIT;
        else {
            System.out.println("Type invalide.");
            return;
        }
        System.out.print("Montant (EUR) : ");
        String mstr = scanner.nextLine().trim();
        BigDecimal montant;
        try {
            montant = new BigDecimal(mstr).setScale(2, RoundingMode.HALF_EVEN);
            if (montant.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Le montant doit être positif.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Montant invalide.");
            return;
        }

        try {
            Operation op = new Operation(type, montant);
            compte.ajouterOperation(op);
            System.out.println("Opération effectuée: " + op);
            System.out.println("Nouveau solde: " + compte.getSolde() + " EUR");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }
    }

    private void afficherDetailCompte() {
        System.out.print("ID du compte: ");
        String in = scanner.nextLine().trim();
        int compteId;
        try {
            compteId = Integer.parseInt(in);
        } catch (NumberFormatException e) {
            System.out.println("ID invalide.");
            return;
        }
        Compte compte = comptes.get(compteId);
        if (compte == null) {
            System.out.println("Compte introuvable.");
            return;
        }
        System.out.println("\n--- Détail du compte ---");
        System.out.println("ID: " + compte.getId());
        System.out.println("Numéro: " + compte.getNumero());
        System.out.println("Client: " + compte.getClient());
        System.out.println("Solde courant: " + compte.getSolde() + " EUR");
        System.out.println("Opérations:");
        if (compte.getOperations().isEmpty()) {
            System.out.println("  (aucune opération)");
        } else {
            compte.getOperations().forEach(op -> System.out.println("  " + op));
        }
    }

    private void listerClientsEtComptes() {
        System.out.println("\nClients:");
        if (clients.isEmpty()) {
            System.out.println("  (aucun client)");
        } else {
            clients.values().forEach(c -> System.out.println("  " + c));
        }
        System.out.println("\nComptes:");
        if (comptes.isEmpty()) {
            System.out.println("  (aucun compte)");
        } else {
            comptes.values().forEach(c -> System.out.println("  " + c));
        }
    }
}