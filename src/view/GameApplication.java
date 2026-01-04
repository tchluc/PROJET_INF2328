package view;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application principale JavaFX.
 * 
 * Cette classe est le point d'entree de l'interface graphique.
 * Elle gere la fenetre principale et les differents ecrans du jeu:
 * - Ecran de bienvenue
 * - Ecran de jeu principal
 * - Ecran de fin de partie (game over)
 */
public class GameApplication extends Application {
    
    // La fenetre principale de l'application
    private Stage fenetrePrincipale;
    
    // Le controleur du jeu (gere la logique)
    private GameController gameController;
    
    /**
     * Methode start() appelee automatiquement par JavaFX au lancement.
     * 
     * @param primaryStage La fenetre principale fournie par JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        // On sauvegarde la reference vers la fenetre
        this.fenetrePrincipale = primaryStage;
        
        // On configure la fenetre
        fenetrePrincipale.setTitle("⚡ Colony Power - Gestionnaire d'Énergie Spatiale");
        fenetrePrincipale.setResizable(true);
        fenetrePrincipale.setMinWidth(1200);
        fenetrePrincipale.setMinHeight(800);
        
        // On affiche l'ecran de bienvenue
        afficherEcranBienvenue();
        
        // On rend la fenetre visible
        fenetrePrincipale.show();
    }
    
    /**
     * Affiche l'ecran de bienvenue.
     */
    private void afficherEcranBienvenue() {
        // On cree la vue de bienvenue
        WelcomeView welcomeView = new WelcomeView();
        
        // On configure le bouton "Demarrer" pour lancer le jeu
        welcomeView.getStartButton().setOnAction(e -> lancerJeu());
        
        // On cree la scene et on l'affiche
        Scene scene = welcomeView.createScene();
        fenetrePrincipale.setScene(scene);
    }
    
    /**
     * Lance le jeu principal.
     */
    private void lancerJeu() {
        // On cree le controleur (qui cree aussi le moteur de jeu et la vue)
        gameController = new GameController(this);
        
        // On affiche l'ecran de jeu
        showMainGame(gameController);
    }
    
    /**
     * Affiche l'ecran de jeu principal.
     * 
     * @param controller Le controleur du jeu
     */
    public void showMainGame(GameController controller) {
        // On sauvegarde le controleur
        this.gameController = controller;
        
        // On recupere la scene depuis la vue du controleur
        Scene scene = controller.getMainGameView().createScene();
        
        // On affiche la scene
        fenetrePrincipale.setScene(scene);
    }
    
    /**
     * Affiche l'ecran de fin de partie (game over).
     * 
     * @param gameOverView La vue de fin de partie
     */
    public void showGameOver(GameOverView gameOverView) {
        // On cree une nouvelle scene avec la vue game over
        Scene scene = new Scene(gameOverView.getRoot(), 1200, 800);
        
        // On essaye de charger le fichier CSS
        try {
            String cheminCSS = getClass().getResource("styles.css").toExternalForm();
            scene.getStylesheets().add(cheminCSS);
        } catch (Exception e) {
            // Si le fichier CSS n'est pas trouve, on affiche un message
            System.err.println("Impossible de charger styles.css: " + e.getMessage());
        }
        
        // On affiche la scene
        fenetrePrincipale.setScene(scene);
    }
    
    /**
     * Point d'entree principal de l'application.
     * 
     * @param args Les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        // launch() est une methode de Application qui demarre JavaFX
        launch(args);
    }
}
