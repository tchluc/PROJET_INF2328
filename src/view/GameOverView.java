package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Ecran de fin de partie (Game Over).
 * 
 * Cet ecran s'affiche quand le joueur a perdu.
 * Il montre:
 * - La raison de la defaite
 * - Les statistiques finales
 * - Un bouton pour rejouer
 * - Un bouton pour quitter
 */
public class GameOverView {
    
    // Le conteneur principal
    private VBox root;
    
    // Les boutons
    private Button boutonNouvellePartie;
    private Button boutonQuitter;
    
    // Les labels pour afficher les informations
    private Label labelStatistiques;
    private Label labelRaison;
    
    /**
     * Constructeur: cree la vue de fin de partie
     */
    public GameOverView() {
        creerVue();
    }
    
    /**
     * Cree tous les elements de l'interface.
     */
    private void creerVue() {
        // On cree la boite verticale principale
        root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("game-root");
        
        // === TITRE "MISSION TERMINEE" ===
        Text titre = new Text("⚠ MISSION TERMINÉE ⚠");
        titre.getStyleClass().add("title");
        titre.setStyle("-fx-fill: #ef4444;");  // Rouge
        
        // === RAISON DE LA DEFAITE ===
        labelRaison = new Label();
        labelRaison.setWrapText(true);
        labelRaison.setMaxWidth(700);
        labelRaison.setTextAlignment(TextAlignment.CENTER);
        labelRaison.setStyle("-fx-font-size: 18px; -fx-text-fill: #f59e0b;");  // Orange
        
        // === STATISTIQUES ===
        labelStatistiques = new Label();
        labelStatistiques.setWrapText(true);
        labelStatistiques.setMaxWidth(700);
        labelStatistiques.setTextAlignment(TextAlignment.CENTER);
        labelStatistiques.getStyleClass().add("label-secondary");
        labelStatistiques.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");
        
        // === BOUTON NOUVELLE PARTIE ===
        boutonNouvellePartie = new Button("🔄 NOUVELLE MISSION");
        boutonNouvellePartie.getStyleClass().add("button-primary");
        boutonNouvellePartie.setStyle("-fx-font-size: 16px; -fx-padding: 12px 30px;");
        
        // === BOUTON QUITTER ===
        boutonQuitter = new Button("❌ QUITTER");
        boutonQuitter.getStyleClass().add("button-danger");
        boutonQuitter.setStyle("-fx-font-size: 16px; -fx-padding: 12px 30px;");
        
        // On ajoute tous les elements
        root.getChildren().add(titre);
        root.getChildren().add(labelRaison);
        root.getChildren().add(labelStatistiques);
        root.getChildren().add(boutonNouvellePartie);
        root.getChildren().add(boutonQuitter);
    }
    
    /**
     * Definit les informations a afficher sur l'ecran de fin de partie.
     * 
     * @param reason La raison de la defaite
     * @param cycles Le nombre de cycles survecu
     * @param finalResources Les credits restants
     * @param finalHappiness Le niveau de bonheur final (0.0 a 1.0)
     * @param population La population finale
     */
    public void setGameOverInfo(String reason, int cycles, int finalResources, double finalHappiness, int population) {
        // On affiche la raison
        labelRaison.setText(reason);
        
        // On construit le texte des statistiques
        String stats = "RAPPORT FINAL\n\n"
            + "Cycles survécus: " + cycles + "\n"
            + "Ressources finales: " + finalResources + " crédits\n"
            + "Bonheur final: " + Math.round(finalHappiness * 100) + "%\n"
            + "Population: " + population + " habitants";
        
        labelStatistiques.setText(stats);
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    public VBox getRoot() {
        return root;
    }
    
    public Button getNewGameButton() {
        return boutonNouvellePartie;
    }
    
    public Button getQuitButton() {
        return boutonQuitter;
    }
}
