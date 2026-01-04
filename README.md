# Colony Power - Gestionnaire d'Énergie Spatiale ⚡

Jeu de simulation de gestion d'énergie développé en Java avec JavaFX et architecture MVC.

## Description

Vous êtes le Gestionnaire d'Énergie de la Colonie Nova-7, une station spatiale isolée. Votre mission est d'assurer la production et distribution d'énergie pour maintenir les systèmes vitaux et le moral des colons.

## Architecture

Le projet suit une architecture **MVC (Modèle - Vue - Contrôleur)** :

### Model (`src/model/`)
- **Residence.java** : Résidences avec niveaux et besoins énergétiques variables
- **PowerPlant.java** : Centrales électriques (Solaire, Éolienne, Charbon, Nucléaire, Fusion)
- **PowerPlantType.java** : Types de centrales disponibles
- **City.java** : Gestion de la colonie (résidences + centrales)
- **GameState.java** : État global du jeu (ressources, bonheur, cycles)
- **GameEngine.java** : Logique du jeu et simulation

### View (`src/view/`)
- **GameApplication.java** : Application JavaFX principale
- **WelcomeView.java** : Écran de bienvenue
- **MainGameView.java** : Interface de jeu principale
- **BuildDialogView.java** : Dialogue de construction
- **GameOverView.java** : Écran de fin de partie
- **styles.css** : Thème spatial sombre

### Controller (`src/controller/`)
- **GameController.java** : Gère les interactions utilisateur et met à jour la vue

## Compilation et Exécution

### Prérequis
- Java 11 ou supérieur
- JavaFX SDK

### Avec IntelliJ IDEA (Recommandé)
1. Ouvrir le projet dans IntelliJ IDEA
2. Configurer le SDK Java (File → Project Structure → Project SDK)
3. Ajouter JavaFX aux librairies du projet si nécessaire
4. Exécuter `Main.java`

### En ligne de commande

#### Compilation
```bash
javac --module-path $PATH_TO_FX --add-modules javafx.controls -d bin src/**/*.java
```

#### Exécution
```bash
java --module-path $PATH_TO_FX --add-modules javafx.controls -cp bin Main
```

Remplacez `$PATH_TO_FX` par le chemin vers votre installation JavaFX.

## Gameplay

### Objectif
Maintenir un système énergétique fonctionnel et un niveau de bonheur supérieur à 30%.

### Mécaniques
- **Construire** des centrales électriques (coûte des crédits)
- **Améliorer** les centrales existantes (3 niveaux max)
- **Gérer** l'équilibre entre production et demande
- **Surveiller** le bonheur de la population
- **Réagir** aux événements aléatoires

### Ressources
- Démarrage : 500 crédits
- Revenus : Vente d'électricité aux résidences
- Dépenses : Construction, améliorations, entretien

### Fin de partie
- Bonheur < 30% : Le conseil vous démet de vos fonctions
- Faillite : Déficit prolongé de ressources

## Fonctionnalités

✅ **Valeurs aléatoires** : Chaque résidence a des besoins et pouvoir d'achat variables
✅ **Événements aléatoires** : Tempêtes solaires, découvertes, pannes...
✅ **Croissance** : La population peut augmenter si satisfaite
✅ **Équilibrage** : Production insuffisante → Bonheur diminue
✅ **Interface moderne** : Thème spatial sombre avec JavaFX

## Auteur

Projet développé dans le cadre du cours INF2328
