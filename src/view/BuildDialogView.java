package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PowerPlant;
import model.PowerPlantType;

/**
 * Dialogue pour construire une nouvelle centrale electrique.
 * 
 * Cette fenetre permet au joueur de:
 * - Voir tous les types de centrales disponibles
 * - Voir le cout et la production de chaque type
 * - Choisir le type a construire
 * 
 * La classe herite de Dialog<PowerPlantType> ce qui signifie
 * qu'elle retourne un PowerPlantType quand l'utilisateur fait son choix.
 */
public class BuildDialogView extends Dialog<PowerPlantType> {
    
    // Groupe de boutons radio (un seul peut etre selectionne)
    private ToggleGroup groupeBoutons;
    
    // Labels pour afficher les infos du type selectionne
    private Label labelCout;
    private Label labelProduction;
    
    /**
     * Constructeur du dialogue
     * 
     * @param creditsDuJoueur Le nombre de credits actuels du joueur
     */
    public BuildDialogView(int creditsDuJoueur) {
        // On configure le titre et l'en-tete du dialogue
        setTitle("Construire une Centrale");
        setHeaderText("Sélectionnez le type de centrale à construire");
        
        // On recupere le panneau du dialogue pour le personnaliser
        DialogPane panneauDialogue = getDialogPane();
        panneauDialogue.getStyleClass().add("dialog-pane");
        
        // On cree le contenu du dialogue
        VBox contenu = new VBox(15);
        contenu.setPadding(new Insets(20));
        
        // On cree le groupe de boutons radio
        groupeBoutons = new ToggleGroup();
        
        // On cree un bouton radio pour chaque type de centrale
        // PowerPlantType.values() retourne tous les types possibles
        PowerPlantType[] tousLesTypes = PowerPlantType.values();
        
        for (int i = 0; i < tousLesTypes.length; i++) {
            PowerPlantType type = tousLesTypes[i];
            RadioButton bouton = creerBoutonCentrale(type);
            groupeBoutons.getToggles().add(bouton);
            contenu.getChildren().add(bouton);
        }
        
        // On selectionne le premier bouton par defaut
        if (groupeBoutons.getToggles().size() > 0) {
            Toggle premierBouton = groupeBoutons.getToggles().get(0);
            premierBouton.setSelected(true);
        }
        
        // === PANNEAU D'INFORMATION ===
        VBox panneauInfo = new VBox(10);
        panneauInfo.setPadding(new Insets(15));
        panneauInfo.getStyleClass().add("panel");
        
        labelCout = new Label();
        labelCout.getStyleClass().add("label-stat");
        
        labelProduction = new Label();
        labelProduction.getStyleClass().add("label-stat");
        
        Label labelCredits = new Label("Ressources disponibles: " + creditsDuJoueur + " crédits");
        labelCredits.setStyle("-fx-text-fill: #00d4ff;");
        
        panneauInfo.getChildren().add(labelCout);
        panneauInfo.getChildren().add(labelProduction);
        panneauInfo.getChildren().add(labelCredits);
        
        contenu.getChildren().add(panneauInfo);
        
        // On ecoute les changements de selection
        // Quand l'utilisateur change de bouton, on met a jour les infos
        groupeBoutons.selectedToggleProperty().addListener(
            (observable, ancienneValeur, nouvelleValeur) -> {
                if (nouvelleValeur != null) {
                    mettreAJourInfos();
                }
            }
        );
        
        // On affiche les infos du premier type selectionne
        mettreAJourInfos();
        
        // On ajoute le contenu au dialogue
        panneauDialogue.setContent(contenu);
        
        // === BOUTONS DU DIALOGUE ===
        // On cree un bouton "Construire"
        ButtonType boutonConstruire = new ButtonType("Construire", ButtonBar.ButtonData.OK_DONE);
        panneauDialogue.getButtonTypes().add(boutonConstruire);
        panneauDialogue.getButtonTypes().add(ButtonType.CANCEL);
        
        // On configure ce que le dialogue retourne
        // Si l'utilisateur clique sur "Construire", on retourne le type selectionne
        // Sinon, on retourne null
        setResultConverter(typeBouton -> {
            if (typeBouton == boutonConstruire) {
                Toggle selection = groupeBoutons.getSelectedToggle();
                if (selection != null) {
                    // getUserData() retourne le type qu'on a stocke dans le bouton
                    return (PowerPlantType) selection.getUserData();
                }
            }
            return null;
        });
        
        // On charge le fichier CSS
        try {
            String cheminCSS = getClass().getResource("styles.css").toExternalForm();
            panneauDialogue.getStylesheets().add(cheminCSS);
        } catch (Exception e) {
            System.err.println("Impossible de charger styles.css: " + e.getMessage());
        }
    }
    
    /**
     * Cree un bouton radio pour un type de centrale.
     * 
     * @param type Le type de centrale
     * @return Le bouton radio configure
     */
    private RadioButton creerBoutonCentrale(PowerPlantType type) {
        RadioButton bouton = new RadioButton();
        
        // On stocke le type dans le bouton pour le recuperer plus tard
        bouton.setUserData(type);
        
        // On cree un conteneur horizontal pour l'icone et les details
        HBox conteneur = new HBox(15);
        conteneur.setAlignment(Pos.CENTER_LEFT);
        conteneur.setPadding(new Insets(10));
        conteneur.getStyleClass().add("panel");
        
        // Icone de la centrale
        Label labelIcone = new Label(type.getIcon());
        labelIcone.getStyleClass().add("icon-label");
        
        // Details de la centrale
        VBox details = new VBox(5);
        
        Label labelNom = new Label(type.getName());
        labelNom.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // On recupere le cout et la production
        int cout = PowerPlant.getBuildCost(type);
        double production = PowerPlant.getBaseProduction(type);
        
        Label labelInfo = new Label("Coût: " + cout + " cr | Production: " + Math.round(production) + " kW");
        labelInfo.getStyleClass().add("label-secondary");
        
        details.getChildren().add(labelNom);
        details.getChildren().add(labelInfo);
        
        conteneur.getChildren().add(labelIcone);
        conteneur.getChildren().add(details);
        
        // On met le conteneur comme graphique du bouton radio
        bouton.setGraphic(conteneur);
        bouton.getStyleClass().add("radio-button");
        
        return bouton;
    }
    
    /**
     * Met a jour les informations affichees selon le type selectionne.
     */
    private void mettreAJourInfos() {
        Toggle selection = groupeBoutons.getSelectedToggle();
        
        if (selection != null) {
            // On recupere le type stocke dans le bouton
            PowerPlantType type = (PowerPlantType) selection.getUserData();
            
            // On recupere les valeurs
            int cout = PowerPlant.getBuildCost(type);
            double production = PowerPlant.getBaseProduction(type);
            
            // On met a jour les labels
            labelCout.setText("💰 Coût de construction: " + cout + " crédits");
            labelProduction.setText("⚡ Production: " + Math.round(production) + " kW");
        }
    }
}
