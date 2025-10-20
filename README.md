```markdown
# Gestion de comptes bancaires (Console Java)

Structure
- Projet Maven minimal.
- Package principal : com.bank
- Modèle dans com.bank.model

Compilation & exécution (depuis la racine du projet)
1. mvn compile
2. mvn exec:java -Dexec.mainClass="com.bank.BankApp"

(ou compiler/runer avec votre IDE préféré)

Description brève
- Compte possède un solde (attribut) et une liste d'opérations.
- L'ajout d'une opération met à jour le solde de façon atomique (synchronisée).
- Méthode de réconciliation fournie pour recalculer le solde à partir de l'historique.

Améliorations possibles
- Persistance (JPA / fichiers JSON)
- Tests unitaires
- Gestion d'utilisateurs et rôles
```