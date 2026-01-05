package view;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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

        // === TITRE AVEC EFFET DE LUEUR ===
        Text titre = new Text("⚡ COLONY POWER ⚡");
        titre.getStyleClass().add("title");

        // Effet de lueur sur le titre
        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.rgb(0, 212, 255, 0.8));
        glowEffect.setRadius(25);
        glowEffect.setSpread(0.6);
        titre.setEffect(glowEffect);

        // === SOUS-TITRE ===
        Text sousTitre = new Text("Gestionnaire d'Énergie Spatiale");
        sousTitre.getStyleClass().add("subtitle");

        // === DESCRIPTION (LORE) ===
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
        labelDescription.setMaxWidth(750);
        labelDescription.setTextAlignment(TextAlignment.CENTER);
        labelDescription.getStyleClass().add("label-secondary");
        labelDescription.setStyle("-fx-font-size: 17px; -fx-line-spacing: 6px;");

        // === BOUTON DEMARRER AVEC EFFET PULSE ===
        boutonDemarrer = new Button("▶ COMMENCER LA MISSION");
        boutonDemarrer.getStyleClass().add("button-primary");
        boutonDemarrer.setStyle("-fx-font-size: 20px; -fx-padding: 18px 45px;");

        // On ajoute tous les elements a la boite verticale
        root.getChildren().add(titre);
        root.getChildren().add(sousTitre);
        root.getChildren().add(labelDescription);
        root.getChildren().add(boutonDemarrer);

        // === ANIMATIONS D'APPARITION ===
        animerEntree(titre, 0);
        animerEntree(sousTitre, 200);
        animerEntree(labelDescription, 400);
        animerBouton(boutonDemarrer, 600);
    }

    /**
     * Anime l'apparition d'un élément avec un effet de fade-in et translation
     */
    private void animerEntree(javafx.scene.Node element, int delaiMs) {
        // Rendre l'élément invisible au départ
        element.setOpacity(0);
        element.setTranslateY(20);

        // Animation de fade-in
        FadeTransition fade = new FadeTransition(Duration.millis(800), element);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delaiMs));

        // Animation de translation (monte vers le haut)
        TranslateTransition translate = new TranslateTransition(Duration.millis(800), element);
        translate.setFromY(20);
        translate.setToY(0);
        translate.setDelay(Duration.millis(delaiMs));

        // Lancer les animations
        fade.play();
        translate.play();
    }

    /**
     * Anime le bouton avec un effet de pulse
     */
    private void animerBouton(Button bouton, int delaiMs) {
        // D'abord l'animation d'apparition
        animerEntree(bouton, delaiMs);

        // Ensuite l'effet de pulse au survol
        bouton.setOnMouseEntered(e -> {
            ScaleTransition pulse = new ScaleTransition(Duration.millis(150), bouton);
            pulse.setToX(1.05);
            pulse.setToY(1.05);
            pulse.play();
        });

        bouton.setOnMouseExited(e -> {
            ScaleTransition depulse = new ScaleTransition(Duration.millis(150), bouton);
            depulse.setToX(1.0);
            depulse.setToY(1.0);
            depulse.play();
        });
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
