package controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import model.*;
import view.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur du jeu - Gère l'interaction entre le Model et la View.
 * Traite les actions de l'utilisateur et met à jour l'interface.
 */
public class GameController {
    private GameEngine gameEngine;
    private MainGameView mainGameView;
    private GameApplication application;
    
    public GameController(GameApplication application) {
        this.application = application;
        this.gameEngine = new GameEngine("Nova-7");
        this.mainGameView = new MainGameView();
        
        setupEventHandlers();
        updateView();
    }
    
    /**
     * Configure les gestionnaires d'événements pour tous les boutons
     */
    private void setupEventHandlers() {
        // Bouton construire centrale
        mainGameView.getBuildButton().setOnAction(e -> handleBuildPowerPlant());
        
        // Bouton améliorer centrale
        mainGameView.getUpgradeButton().setOnAction(e -> handleUpgradePowerPlant());
        
        // Bouton voir détails
        mainGameView.getDetailsButton().setOnAction(e -> handleShowDetails());
        
        // Bouton cycle suivant
        mainGameView.getNextCycleButton().setOnAction(e -> handleNextCycle());
    }
    
    /**
     * Gère la construction d'une nouvelle centrale
     */
    private void handleBuildPowerPlant() {
        GameState gameState = gameEngine.getGameState();
        
        // Ouvrir le dialogue de construction
        BuildDialogView dialog = new BuildDialogView(gameState.getResources());
        Optional<PowerPlantType> result = dialog.showAndWait();
        
        result.ifPresent(type -> {
            boolean success = gameEngine.buildPowerPlant(type);
            
            if (!success) {
                showAlert("Construction Impossible", 
                         "Ressources insuffisantes pour construire une centrale " + type.getName() + ".",
                         Alert.AlertType.WARNING);
            }
            
            updateView();
        });
    }
    
    /**
     * Gère l'amélioration d'une centrale existante
     */
    private void handleUpgradePowerPlant() {
        GameState gameState = gameEngine.getGameState();
        City city = gameState.getCity();
        List<PowerPlant> powerPlants = city.getPowerPlants();
        
        if (powerPlants.isEmpty()) {
            showAlert("Aucune Centrale", 
                     "Il n'y a aucune centrale à améliorer. Construisez-en une d'abord !",
                     Alert.AlertType.INFORMATION);
            return;
        }
        
        // Créer une liste de choix
        List<String> choices = new ArrayList<>();
        for (PowerPlant plant : powerPlants) {
            String choice = String.format("%s #%d (Niv.%d) - %.0f kW", 
                    plant.getType().getIcon(),
                    plant.getId(),
                    plant.getLevel(),
                    plant.getProduction());
            
            if (!plant.canUpgrade()) {
                choice += " [MAX]";
            } else {
                choice += String.format(" [Coût amélioration: %d cr]", plant.getUpgradeCost());
            }
            
            choices.add(choice);
        }
        
        // Afficher le dialogue de choix
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Améliorer une Centrale");
        dialog.setHeaderText("Sélectionnez la centrale à améliorer:");
        dialog.setContentText("Centrale:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> {
            int index = choices.indexOf(choice);
            PowerPlant selectedPlant = powerPlants.get(index);
            
            boolean success = gameEngine.upgradePowerPlant(selectedPlant);
            
            if (!success && !selectedPlant.canUpgrade()) {
                showAlert("Amélioration Impossible", 
                         "Cette centrale est déjà au niveau maximum !",
                         Alert.AlertType.INFORMATION);
            }
            
            updateView();
        });
    }
    
    /**
     * Affiche les détails du jeu
     */
    private void handleShowDetails() {
        GameState gameState = gameEngine.getGameState();
        City city = gameState.getCity();
        
        String details = String.format(
            "STATISTIQUES DÉTAILLÉES\n\n" +
            "Colonie: %s\n" +
            "Cycle: %d\n" +
            "Ressources: %d crédits\n" +
            "Bonheur: %.0f%% (%s)\n\n" +
            "Population: %d habitants\n" +
            "Résidences: %d\n" +
            "Centrales: %d\n\n" +
            "Production totale: %.0f kW\n" +
            "Demande totale: %.0f kW\n" +
            "Balance: %.0f kW\n" +
            "Ratio: %.1f%%\n\n" +
            "Coût d'entretien: %d crédits/cycle",
            city.getName(),
            gameState.getCurrentCycle(),
            gameState.getResources(),
            gameState.getHappiness() * 100,
            gameState.getHappinessStatus(),
            city.getTotalPopulation(),
            city.getResidenceCount(),
            city.getPowerPlantCount(),
            city.getTotalEnergyProduction(),
            city.getTotalEnergyDemand(),
            gameState.getEnergyBalance(),
            gameState.getEnergyRatio() * 100,
            city.getTotalMaintenanceCost()
        );
        
        showAlert("Détails de la Colonie", details, Alert.AlertType.INFORMATION);
    }
    
    /**
     * Passe au cycle suivant
     */
    private void handleNextCycle() {
        // Traiter le cycle
        gameEngine.processCycle();
        
        // Mettre à jour l'affichage
        updateView();
        
        // Vérifier game over
        if (gameEngine.getGameState().isGameOver()) {
            handleGameOver();
        }
    }
    
    /**
     * Gère la fin du jeu
     */
    private void handleGameOver() {
        Platform.runLater(() -> {
            GameState gameState = gameEngine.getGameState();
            City city = gameState.getCity();
            
            GameOverView gameOverView = new GameOverView();
            gameOverView.setGameOverInfo(
                gameState.getGameOverReason(),
                gameState.getCurrentCycle(),
                gameState.getResources(),
                gameState.getHappiness(),
                city.getTotalPopulation()
            );
            
            // Gérer les boutons
            gameOverView.getNewGameButton().setOnAction(e -> {
                restartGame();
            });
            
            gameOverView.getQuitButton().setOnAction(e -> {
                Platform.exit();
            });
            
            // Afficher la vue Game Over
            application.showGameOver(gameOverView);
        });
    }
    
    /**
     * Redémarre une nouvelle partie
     */
    private void restartGame() {
        gameEngine.resetGame("Nova-7");
        mainGameView.clearLogs();
        updateView();
        application.showMainGame(this);
    }
    
    /**
     * Met à jour toute la vue avec l'état actuel
     */
    private void updateView() {
        Platform.runLater(() -> {
            mainGameView.updateDisplay(gameEngine.getGameState());
            
            // Ajouter les nouveaux logs
            List<String> recentLogs = gameEngine.getRecentLogs(10);
            if (!recentLogs.isEmpty()) {
                mainGameView.addLogs(recentLogs);
            }
        });
    }
    
    /**
     * Affiche une alerte
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public MainGameView getMainGameView() {
        return mainGameView;
    }
    
    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
