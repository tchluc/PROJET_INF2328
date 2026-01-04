package view;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application principale JavaFX.
 * Gère les scènes (écran de bienvenue, jeu principal, game over) et la fenêtre.
 */
public class GameApplication extends Application {
    private Stage primaryStage;
    private GameController gameController;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Configuration de la fenêtre principale
        primaryStage.setTitle("⚡ Colony Power - Gestionnaire d'Énergie Spatiale");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(800);
        
        // Afficher l'écran de bienvenue
        showWelcomeScreen();
        
        primaryStage.show();
    }
    
    /**
     * Affiche l'écran de bienvenue
     */
    private void showWelcomeScreen() {
        WelcomeView welcomeView = new WelcomeView();
        
        // Gérer le bouton démarrer
        welcomeView.getStartButton().setOnAction(e -> {
            startGame();
        });
        
        Scene scene = welcomeView.createScene();
        primaryStage.setScene(scene);
    }
    
    /**
     * Démarre le jeu principal
     */
    private void startGame() {
        gameController = new GameController(this);
        showMainGame(gameController);
    }
    
    /**
     * Affiche l'écran de jeu principal
     */
    public void showMainGame(GameController controller) {
        this.gameController = controller;
        Scene scene = controller.getMainGameView().createScene();
        primaryStage.setScene(scene);
    }
    
    /**
     * Affiche l'écran de game over
     */
    public void showGameOver(GameOverView gameOverView) {
        Scene scene = new Scene(gameOverView.getRoot(), 1200, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Impossible de charger styles.css: " + e.getMessage());
        }
        primaryStage.setScene(scene);
    }
    
    /**
     * Point d'entrée de l'application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
