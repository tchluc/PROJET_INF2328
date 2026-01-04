package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Écran de fin de partie (Game Over).
 * Affiche les statistiques finales et la raison de la défaite.
 */
public class GameOverView {
    private VBox root;
    private Button newGameButton;
    private Button quitButton;
    private Label statsLabel;
    private Label reasonLabel;
    
    public GameOverView() {
        createView();
    }
    
    private void createView() {
        root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("game-root");
        
        // Titre
        Text title = new Text("⚠ MISSION TERMINÉE ⚠");
        title.getStyleClass().add("title");
        title.setStyle("-fx-fill: #ef4444;");
        
        // Raison
        reasonLabel = new Label();
        reasonLabel.setWrapText(true);
        reasonLabel.setMaxWidth(700);
        reasonLabel.setTextAlignment(TextAlignment.CENTER);
        reasonLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #f59e0b;");
        
        // Statistiques
        statsLabel = new Label();
        statsLabel.setWrapText(true);
        statsLabel.setMaxWidth(700);
        statsLabel.setTextAlignment(TextAlignment.CENTER);
        statsLabel.getStyleClass().add("label-secondary");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");
        
        // Boutons
        newGameButton = new Button("🔄 NOUVELLE MISSION");
        newGameButton.getStyleClass().add("button-primary");
        newGameButton.setStyle("-fx-font-size: 16px; -fx-padding: 12px 30px;");
        
        quitButton = new Button("❌ QUITTER");
        quitButton.getStyleClass().add("button-danger");
        quitButton.setStyle("-fx-font-size: 16px; -fx-padding: 12px 30px;");
        
        root.getChildren().addAll(title, reasonLabel, statsLabel, newGameButton, quitButton);
    }
    
    public void setGameOverInfo(String reason, int cycles, int finalResources, double finalHappiness, int population) {
        reasonLabel.setText(reason);
        
        String stats = String.format(
            "RAPPORT FINAL\n\n" +
            "Cycles survécus: %d\n" +
            "Ressources finales: %d crédits\n" +
            "Bonheur final: %.0f%%\n" +
            "Population: %d habitants",
            cycles, finalResources, finalHappiness * 100, population
        );
        statsLabel.setText(stats);
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public Button getNewGameButton() {
        return newGameButton;
    }
    
    public Button getQuitButton() {
        return quitButton;
    }
}
