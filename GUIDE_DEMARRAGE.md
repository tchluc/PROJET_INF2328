# 🚀 Guide de Démarrage Rapide - Colony Power

## Installation et Configuration

### 1. Vérifier Java et JavaFX

Le projet nécessite **Java 11+** et **JavaFX**.

#### Vérifier Java
```bash
java -version
```

Si Java n'est pas installé :
```bash
sudo apt install openjdk-21-jdk
```

#### JavaFX avec IntelliJ IDEA
JavaFX est généralement inclus dans IntelliJ IDEA. Si ce n'est pas le cas :
1. File → Project Structure → Libraries
2. Click "+" → New Project Library → Java
3. Naviguez vers votre installation JavaFX (ou téléchargez depuis https://openjfx.io/)

### 2. Ouvrir dans IntelliJ IDEA

1. **Ouvrir IntelliJ IDEA**
2. **Open** → Sélectionnez le dossier `PROJET_INF2328`
3. Attendez l'indexation du projet

### 3. Configurer le JDK

1. **File → Project Structure → Project**
2. **Project SDK** : Sélectionnez Java 11 ou supérieur
3. **Project language level** : 11 ou supérieur
4. **Apply** et **OK**

### 4. Exécuter le Jeu

#### Méthode 1 : Via IntelliJ (Recommandée)
1. Ouvrez `src/Main.java`
2. Clic droit sur le fichier → **Run 'Main.main()'**
3. La fenêtre du jeu devrait s'ouvrir ! 🎮

#### Méthode 2 : Terminal avec Maven (si configuré)
```bash
mvn clean javafx:run
```

#### Méthode 3 : Ligne de commande directe
```bash
# Compilation
javac --module-path $PATH_TO_FX --add-modules javafx.controls -d bin src/**/*.java

# Exécution
java --module-path $PATH_TO_FX --add-modules javafx.controls -cp bin Main
```

## 🎮 Comment Jouer

### Premier Lancement
1. **Écran de Bienvenue** : Lisez le briefing de mission
2. **Cliquez sur "COMMENCER LA MISSION"**

### Interface de Jeu

#### Panneau Supérieur
- **Crédits** : Vos ressources (commencez avec 500)
- **Bonheur** : Ne doit pas descendre sous 30% !
- **Cycle** : Numéro du tour actuel

#### Panneau de Contrôle (Gauche)
- **🏗️ Construire Centrale** : Ouvre le menu de construction
- **⬆️ Améliorer Centrale** : Améliore une centrale existante
- **📊 Voir Détails** : Statistiques complètes
- **⏩ CYCLE SUIVANT** : Passe au tour suivant

#### Zone Centrale
- **Barres d'Énergie** : Production vs Demande
- **Liste des Centrales** : Vos installations
- **Liste des Résidences** : Population de la colonie

#### Journal de Bord (Bas)
- Événements du jeu
- Résultats des cycles
- Alertes importantes

### Stratégie de Base

1. **Début de Partie**
   - Construisez 1-2 centrales solaires/éoliennes
   - Visez à couvrir la demande énergétique

2. **Cycle de Jeu**
   - Production > Demande = Bonheur augmente ✅
   - Production < Demande = Bonheur diminue ⚠️

3. **Gestion des Ressources**
   - Revenus : Vente d'électricité aux résidences
   - Dépenses : Construction + Entretien
   - Gardez une réserve pour les imprévus !

4. **Événements Aléatoires**
   - Tempêtes solaires (réduisent production)
   - Découvertes (bonus de crédits)
   - Pannes (satisfaction baisse)

## ⚠️ Dépannage

### Erreur : "Cannot find JavaFX"
**Solution** : Configurez JavaFX dans IntelliJ
1. File → Project Structure → Libraries
2. Ajoutez le SDK JavaFX

### Erreur : "Module not found"
**Solution** : Configurez le module-path
1. Run → Edit Configurations
2. VM Options : `--module-path $PATH_TO_FX --add-modules javafx.controls`

### Le jeu ne se lance pas
**Vérifiez** :
- Java 11+ installé : `java -version`
- Projet ouvert dans IntelliJ
- JDK configuré dans Project Structure

## 📁 Structure du Code

```
src/
├── Main.java              # Point d'entrée
├── model/                 # Logique du jeu (MODEL)
├── view/                  # Interface (VIEW)
└── controller/            # Interactions (CONTROLLER)
```

## 🎯 Objectifs du Jeu

### Survie
- Maintenez le bonheur au-dessus de 30%
- Ne tombez pas en faillite

### Optimisation
- Maximisez le nombre de cycles
- Gérez une population croissante
- Équilibrez production et coûts

## 🆘 Besoin d'Aide ?

Consultez le fichier `README.md` pour plus de détails sur :
- Architecture MVC
- Caractéristiques des centrales
- Mécaniques de jeu détaillées

Bon courage, Commandant ! La colonie compte sur vous ! 🚀
