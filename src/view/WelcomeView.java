package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Écran de bienvenue du jeu.
 * Affiche le titre, le lore et permet de démarrer la partie.
 */
public class WelcomeView {
    private VBox root;
    private Button startButton;
    
    public WelcomeView() {
        createView();
    }
    
    private void createView() {
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.getStyleClass().add("game-root");
        
        // Titre
        Text title = new Text("⚡ COLONY POWER ⚡");
        title.getStyleClass().add("title");
        
        Text subtitle = new Text("Gestionnaire d'Énergie Spatiale");
        subtitle.getStyleClass().add("subtitle");
        
        // Lore
        Label loreLabel = new Label(
            "Bienvenue, Commandant !\n\n" +
            "Vous êtes le nouveau Gestionnaire d'Énergie de la Colonie Nova-7,\n" +
            "une station spatiale isolée aux confins de la galaxie.\n\n" +
            "Votre mission : assurer la production et la distribution d'énergie\n" +
            "pour maintenir les systèmes vitaux et le moral des colons.\n\n" +
            "Chaque décision compte. Les ressources sont limitées.\n" +
            "Le bonheur de la population dépend de vous.\n\n" +
            "Bonne chance, Commandant. La colonie compte sur vous."
        );
        loreLabel.setWrapText(true);
        loreLabel.setMaxWidth(700);
        loreLabel.setTextAlignment(TextAlignment.CENTER);
        loreLabel.getStyleClass().add("label-secondary");
        loreLabel.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");
        
        // Bouton démarrer
        startButton = new Button("▶ COMMENCER LA MISSION");
        startButton.getStyleClass().add("button-primary");
        startButton.setStyle("-fx-font-size: 18px; -fx-padding: 15px 40px;");
        
        root.getChildren().addAll(title, subtitle, loreLabel, startButton);
    }
    
    public VBox getRoot() {
        return root;
    }
    
    public Button getStartButton() {
        return startButton;
    }
    
    public Scene createScene() {
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return scene;
    }
}
