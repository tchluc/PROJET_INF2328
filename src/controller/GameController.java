package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import model.*;
import view.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controleur du jeu - Fait le lien entre le Modele et la Vue.
 * 
 * Cette classe gere les interactions de l'utilisateur:
 * - Quand le joueur clique sur un bouton, c'est ici qu'on reagit
 * - On met a jour l'interface apres chaque action
 * 
 * C'est le "chef d'orchestre" de l'application.
 */
public class GameController {
    
    // Le moteur de jeu (logique du jeu)
    private GameEngine gameEngine;
    
    // La vue principale (interface graphique)
    private MainGameView mainGameView;
    
    // L'application (pour changer d'ecran)
    private GameApplication application;
    
    /**
     * Constructeur du controleur
     * 
     * @param application L'application principale
     */
    public GameController(GameApplication application) {
        // On sauvegarde la reference vers l'application
        this.application = application;
        
        // On cree le moteur de jeu avec le nom de la colonie
        this.gameEngine = new GameEngine("Nova-7");
        
        // On cree la vue principale
        this.mainGameView = new MainGameView();
        
        // On configure les boutons pour qu'ils reagissent aux clics
        configurerBoutons();
        
        // On affiche l'etat initial
        mettreAJourVue();
    }
    
    /**
     * Configure les boutons pour qu'ils appellent les bonnes methodes quand on clique.
     */
    private void configurerBoutons() {
        // Bouton "Construire Centrale"
        // Quand on clique, on appelle la methode gererConstructionCentrale()
        mainGameView.getBuildButton().setOnAction(e -> gererConstructionCentrale());
        
        // Bouton "Ameliorer Centrale"
        mainGameView.getUpgradeButton().setOnAction(e -> gererAmeliorationCentrale());
        
        // Bouton "Voir Details"
        mainGameView.getDetailsButton().setOnAction(e -> afficherDetails());
        
        // Bouton "Cycle Suivant"
        mainGameView.getNextCycleButton().setOnAction(e -> passerCycleSuivant());
    }
    
    /**
     * Gere la construction d'une nouvelle centrale.
     * Cette methode est appelee quand le joueur clique sur "Construire Centrale"
     */
    private void gererConstructionCentrale() {
        // On recupere l'etat du jeu
        GameState gameState = gameEngine.getGameState();
        
        // On ouvre la fenetre de dialogue pour choisir le type de centrale
        BuildDialogView dialog = new BuildDialogView(gameState.getResources());
        
        // showAndWait() attend que l'utilisateur fasse un choix
        java.util.Optional<PowerPlantType> resultat = dialog.showAndWait();
        
        // On verifie si l'utilisateur a choisi quelque chose
        if (resultat.isPresent()) {
            // Il a choisi un type de centrale
            PowerPlantType typeChoisi = resultat.get();
            
            // On essaye de construire
            boolean reussite = gameEngine.buildPowerPlant(typeChoisi);
            
            // Si ca n'a pas marche, on affiche un message d'erreur
            if (reussite == false) {
                afficherAlerte("Construction Impossible", 
                    "Ressources insuffisantes pour construire une centrale " + typeChoisi.getName() + ".",
                    Alert.AlertType.WARNING);
            }
            
            // On met a jour l'affichage
            mettreAJourVue();
        }
        // Si resultat n'est pas present, l'utilisateur a annule -> on ne fait rien
    }
    
    /**
     * Gere l'amelioration d'une centrale existante.
     */
    private void gererAmeliorationCentrale() {
        GameState gameState = gameEngine.getGameState();
        City city = gameState.getCity();
        
        // On recupere la liste des centrales
        List<PowerPlant> centrales = city.getPowerPlants();
        
        // S'il n'y a pas de centrale, on affiche un message
        if (centrales.size() == 0) {
            afficherAlerte("Aucune Centrale", 
                "Il n'y a aucune centrale à améliorer. Construisez-en une d'abord !",
                Alert.AlertType.INFORMATION);
            return;  // On sort de la methode
        }
        
        // On cree une liste de choix pour l'utilisateur
        ArrayList<String> choix = new ArrayList<String>();
        
        for (int i = 0; i < centrales.size(); i++) {
            PowerPlant centrale = centrales.get(i);
            
            // On cree une description de la centrale
            String description = centrale.getType().getIcon() + " #" + centrale.getId() 
                + " (Niv." + centrale.getLevel() + ")"
                + " - " + Math.round(centrale.getProduction()) + " kW";
            
            // On ajoute l'info sur l'amelioration
            if (centrale.canUpgrade() == false) {
                description = description + " [MAX]";
            } else {
                description = description + " [Coût amélioration: " + centrale.getUpgradeCost() + " cr]";
            }
            
            choix.add(description);
        }
        
        // On cree une fenetre de dialogue avec la liste de choix
        ChoiceDialog<String> dialog = new ChoiceDialog<String>(choix.get(0), choix);
        dialog.setTitle("Améliorer une Centrale");
        dialog.setHeaderText("Sélectionnez la centrale à améliorer:");
        dialog.setContentText("Centrale:");
        
        // On attend le choix de l'utilisateur
        java.util.Optional<String> resultat = dialog.showAndWait();
        
        if (resultat.isPresent()) {
            // L'utilisateur a choisi une centrale
            String choixUtilisateur = resultat.get();
            
            // On trouve l'index de ce choix dans la liste
            int index = -1;
            for (int i = 0; i < choix.size(); i++) {
                if (choix.get(i).equals(choixUtilisateur)) {
                    index = i;
                    break;
                }
            }
            
            // On recupere la centrale correspondante
            if (index >= 0) {
                PowerPlant centraleChoisie = centrales.get(index);
                
                // On essaye d'ameliorer
                boolean reussite = gameEngine.upgradePowerPlant(centraleChoisie);
                
                if (reussite == false && centraleChoisie.canUpgrade() == false) {
                    afficherAlerte("Amélioration Impossible", 
                        "Cette centrale est déjà au niveau maximum !",
                        Alert.AlertType.INFORMATION);
                }
            }
            
            // On met a jour l'affichage
            mettreAJourVue();
        }
    }
    
    /**
     * Affiche les details du jeu dans une fenetre.
     */
    private void afficherDetails() {
        GameState gameState = gameEngine.getGameState();
        City city = gameState.getCity();
        
        // On construit le texte des details
        String details = "STATISTIQUES DÉTAILLÉES\n\n"
            + "Colonie: " + city.getName() + "\n"
            + "Cycle: " + gameState.getCurrentCycle() + "\n"
            + "Ressources: " + gameState.getResources() + " crédits\n"
            + "Bonheur: " + Math.round(gameState.getHappiness() * 100) + "% (" + gameState.getHappinessStatus() + ")\n\n"
            + "Population: " + city.getTotalPopulation() + " habitants\n"
            + "Résidences: " + city.getResidenceCount() + "\n"
            + "Centrales: " + city.getPowerPlantCount() + "\n\n"
            + "Production totale: " + Math.round(city.getTotalEnergyProduction()) + " kW\n"
            + "Demande totale: " + Math.round(city.getTotalEnergyDemand()) + " kW\n"
            + "Balance: " + Math.round(gameState.getEnergyBalance()) + " kW\n"
            + "Ratio: " + Math.round(gameState.getEnergyRatio() * 100) + "%\n\n"
            + "Coût d'entretien: " + city.getTotalMaintenanceCost() + " crédits/cycle";
        
        // On affiche les details
        afficherAlerte("Détails de la Colonie", details, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Passe au cycle suivant du jeu.
     */
    private void passerCycleSuivant() {
        // On execute un cycle de jeu
        gameEngine.processCycle();
        
        // On met a jour l'affichage
        mettreAJourVue();
        
        // On verifie si le jeu est termine
        if (gameEngine.getGameState().isGameOver()) {
            gererFinDePartie();
        }
    }
    
    /**
     * Gere la fin de partie (game over).
     */
    private void gererFinDePartie() {
        // Platform.runLater() permet d'executer du code sur le thread JavaFX
        // C'est necessaire pour modifier l'interface graphique
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GameState gameState = gameEngine.getGameState();
                City city = gameState.getCity();
                
                // On cree la vue de fin de partie
                GameOverView gameOverView = new GameOverView();
                
                // On lui donne les informations
                gameOverView.setGameOverInfo(
                    gameState.getGameOverReason(),
                    gameState.getCurrentCycle(),
                    gameState.getResources(),
                    gameState.getHappiness(),
                    city.getTotalPopulation()
                );
                
                // On configure le bouton "Nouvelle Mission"
                gameOverView.getNewGameButton().setOnAction(e -> recommencerPartie());
                
                // On configure le bouton "Quitter"
                gameOverView.getQuitButton().setOnAction(e -> Platform.exit());
                
                // On affiche l'ecran de fin de partie
                application.showGameOver(gameOverView);
            }
        });
    }
    
    /**
     * Recommence une nouvelle partie.
     */
    private void recommencerPartie() {
        // On reinitialise le moteur de jeu
        gameEngine.resetGame("Nova-7");
        
        // On efface le journal
        mainGameView.clearLogs();
        
        // On met a jour l'affichage
        mettreAJourVue();
        
        // On revient a l'ecran de jeu principal
        application.showMainGame(this);
    }
    
    /**
     * Met a jour toute l'interface avec l'etat actuel du jeu.
     */
    private void mettreAJourVue() {
        // Platform.runLater() pour s'assurer qu'on est sur le thread JavaFX
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // On met a jour l'affichage principal
                mainGameView.updateDisplay(gameEngine.getGameState());
                
                // On ajoute les nouveaux messages du journal
                List<String> logs = gameEngine.getRecentLogs(10);
                if (logs.size() > 0) {
                    mainGameView.addLogs(logs);
                }
            }
        });
    }
    
    /**
     * Affiche une fenetre d'alerte avec un message.
     * 
     * @param titre Le titre de la fenetre
     * @param contenu Le message a afficher
     * @param type Le type d'alerte (INFORMATION, WARNING, ERROR)
     */
    private void afficherAlerte(String titre, String contenu, Alert.AlertType type) {
        // On cree une alerte
        Alert alerte = new Alert(type);
        
        // On configure l'alerte
        alerte.setTitle(titre);
        alerte.setHeaderText(null);  // Pas d'en-tete
        alerte.setContentText(contenu);
        
        // On affiche et on attend que l'utilisateur ferme
        alerte.showAndWait();
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    public MainGameView getMainGameView() {
        return mainGameView;
    }
    
    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
