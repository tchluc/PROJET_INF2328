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
 * Ecran de bienvenue du jeu.
 * 
 * Cet ecran s'affiche au lancement du jeu.
 * Il montre:
 * - Le titre du jeu
 * - Une description de l'univers (lore)
 * - Un bouton pour commencer a jouer
 */
public class WelcomeView {
    
    // Le conteneur principal (une VBox = boite verticale)
    private VBox root;
    
    // Le bouton pour commencer le jeu
    private Button boutonDemarrer;
    
    /**
     * Constructeur: cree la vue de bienvenue
     */
    public WelcomeView() {
        // On appelle la methode qui cree tous les elements visuels
        creerVue();
    }
    
    /**
     * Cree tous les elements de l'interface.
     */
    private void creerVue() {
        // On cree la boite verticale principale
        // 30 = espace entre les elements
        root = new VBox(30);
        
        // On centre tout au milieu
        root.setAlignment(Pos.CENTER);
        
        // On ajoute une marge de 50 pixels autour
        root.setPadding(new Insets(50));
        
        // On applique le style CSS
        root.getStyleClass().add("game-root");
        
        // === TITRE ===
        Text titre = new Text("⚡ COLONY POWER ⚡");
        titre.getStyleClass().add("title");
        
        // === SOUS-TITRE ===
        Text sousTitre = new Text("Gestionnaire d'Énergie Spatiale");
        sousTitre.getStyleClass().add("subtitle");
        
        // === DESCRIPTION (LORE) ===
        // On cree un label avec plusieurs lignes de texte
        String texteDescription = "Bienvenue, Commandant !\n\n"
            + "Vous êtes le nouveau Gestionnaire d'Énergie de la Colonie Nova-7,\n"
            + "une station spatiale isolée aux confins de la galaxie.\n\n"
            + "Votre mission : assurer la production et la distribution d'énergie\n"
            + "pour maintenir les systèmes vitaux et le moral des colons.\n\n"
            + "Chaque décision compte. Les ressources sont limitées.\n"
            + "Le bonheur de la population dépend de vous.\n\n"
            + "Bonne chance, Commandant. La colonie compte sur vous.";
        
        Label labelDescription = new Label(texteDescription);
        labelDescription.setWrapText(true);
        labelDescription.setMaxWidth(700);
        labelDescription.setTextAlignment(TextAlignment.CENTER);
        labelDescription.getStyleClass().add("label-secondary");
        labelDescription.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");
        
        // === BOUTON DEMARRER ===
        boutonDemarrer = new Button("▶ COMMENCER LA MISSION");
        boutonDemarrer.getStyleClass().add("button-primary");
        boutonDemarrer.setStyle("-fx-font-size: 18px; -fx-padding: 15px 40px;");
        
        // On ajoute tous les elements a la boite verticale
        root.getChildren().add(titre);
        root.getChildren().add(sousTitre);
        root.getChildren().add(labelDescription);
        root.getChildren().add(boutonDemarrer);
    }
    
    /**
     * Retourne le conteneur principal.
     */
    public VBox getRoot() {
        return root;
    }
    
    /**
     * Retourne le bouton demarrer pour pouvoir y attacher une action.
     */
    public Button getStartButton() {
        return boutonDemarrer;
    }
    
    /**
     * Cree et retourne une Scene contenant cette vue.
     * 
     * @return La Scene prete a etre affichee
     */
    public Scene createScene() {
        // On cree une scene de 1200x800 pixels
        Scene scene = new Scene(root, 1200, 800);
        
        // On charge le fichier CSS
        String cheminCSS = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(cheminCSS);
        
        return scene;
    }
}
