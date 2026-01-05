package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.City;
import model.GameState;
import model.PowerPlant;
import model.Residence;

import java.util.List;

/**
 * Vue principale du jeu.
 * 
 * Cette vue contient l'interface complete du jeu:
 * - En haut: barre d'informations (credits, bonheur, cycle)
 * - A gauche: boutons de controle
 * - Au centre: affichage de l'energie et des batiments
 * - En bas: journal des evenements
 * 
 * On utilise un BorderPane qui divise l'ecran en 5 zones:
 * TOP, LEFT, CENTER, RIGHT, BOTTOM
 */
public class MainGameView {

    // Le conteneur principal (BorderPane)
    private BorderPane root;

    // === ELEMENTS DE LA BARRE DU HAUT ===
    private Label labelResources; // Affiche les credits
    private Label labelBonheur; // Affiche le bonheur
    private Label labelCycle; // Affiche le numero du cycle
    private Label labelNomColonie; // Affiche le nom de la colonie
    private ProgressBar barreBonheur; // Barre de progression du bonheur

    // === BOUTONS DE CONTROLE (GAUCHE) ===
    private Button boutonConstruire; // Construire une centrale
    private Button boutonAmeliorer; // Ameliorer une centrale
    private Button boutonDetails; // Voir les details
    private Button boutonCycleSuivant; // Passer au cycle suivant

    // === ELEMENTS DU CENTRE ===
    private VBox panneauCentral;
    private ProgressBar barreProduction; // Barre de la production
    private ProgressBar barreDemande; // Barre de la demande
    private Label labelProduction; // Texte de la production
    private Label labelDemande; // Texte de la demande
    private Label labelBalance; // Balance energie
    private ListView<String> listeCentrales; // Liste des centrales
    private ListView<String> listeResidences; // Liste des residences

    // === ZONE DE LOG (BAS) ===
    private TextArea zoneLog;

    /**
     * Constructeur: cree la vue principale
     */
    public MainGameView() {
        creerVue();
    }

    /**
     * Cree tous les elements de l'interface.
     */
    private void creerVue() {
        // On cree le conteneur BorderPane
        root = new BorderPane();
        root.getStyleClass().add("game-root");

        // On cree chaque section
        creerBarreHaut();
        creerPanneauGauche();
        creerPanneauCentral();
        creerPanneauBas();
    }

    /**
     * Cree la barre superieure avec les informations du jeu.
     */
    private void creerBarreHaut() {
        // On cree une boite horizontale
        HBox barreHaut = new HBox(25);
        barreHaut.setPadding(new Insets(12, 15, 12, 15));
        barreHaut.setAlignment(Pos.CENTER_LEFT);
        barreHaut.getStyleClass().add("info-bar");

        // Nom de la colonie
        labelNomColonie = new Label("Colonie Nova-7");
        labelNomColonie.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Numero du cycle
        labelCycle = new Label("Cycle: 0");
        labelCycle.getStyleClass().add("label-stat");

        // Credits
        labelResources = new Label("💰 Crédits: 0");
        labelResources.getStyleClass().add("label-stat");

        // Bonheur (label + barre de progression)
        VBox boiteBonheur = new VBox(5);

        labelBonheur = new Label("Bonheur: 0%");
        labelBonheur.getStyleClass().add("label-stat");

        barreBonheur = new ProgressBar(0);
        barreBonheur.setPrefWidth(120);
        barreBonheur.getStyleClass().add("progress-bar");

        boiteBonheur.getChildren().add(labelBonheur);
        boiteBonheur.getChildren().add(barreBonheur);

        // Espace flexible pour pousser les elements a gauche
        Region espaceur = new Region();
        HBox.setHgrow(espaceur, Priority.ALWAYS);

        // On ajoute tous les elements a la barre
        barreHaut.getChildren().add(labelNomColonie);
        barreHaut.getChildren().add(labelCycle);
        barreHaut.getChildren().add(labelResources);
        barreHaut.getChildren().add(boiteBonheur);
        barreHaut.getChildren().add(espaceur);

        // On place la barre en haut du BorderPane
        root.setTop(barreHaut);
    }

    /**
     * Cree le panneau gauche avec les boutons de controle.
     */
    private void creerPanneauGauche() {
        // On cree une boite verticale
        VBox panneauGauche = new VBox(12);
        panneauGauche.setPadding(new Insets(15));
        panneauGauche.setPrefWidth(200);
        panneauGauche.getStyleClass().add("panel");

        // Titre du panneau
        Label labelTitre = new Label("CONTRÔLES");
        labelTitre.getStyleClass().add("panel-header");

        // Bouton Construire
        boutonConstruire = new Button("Construire");
        boutonConstruire.setMaxWidth(Double.MAX_VALUE); // Prend toute la largeur
        boutonConstruire.getStyleClass().add("button");

        // Bouton Ameliorer
        boutonAmeliorer = new Button("Améliorer");
        boutonAmeliorer.setMaxWidth(Double.MAX_VALUE);
        boutonAmeliorer.getStyleClass().add("button");

        // Bouton Details
        boutonDetails = new Button("Détails");
        boutonDetails.setMaxWidth(Double.MAX_VALUE);
        boutonDetails.getStyleClass().add("button");

        // Separateur
        Separator separateur = new Separator();

        // Bouton Cycle Suivant (plus gros et plus visible)
        boutonCycleSuivant = new Button("CYCLE SUIVANT");
        boutonCycleSuivant.setMaxWidth(Double.MAX_VALUE);
        boutonCycleSuivant.getStyleClass().add("button");
        boutonCycleSuivant.getStyleClass().add("button-primary");
        boutonCycleSuivant.setStyle("-fx-font-weight: bold;");

        // On ajoute tous les elements
        panneauGauche.getChildren().add(labelTitre);
        panneauGauche.getChildren().add(boutonConstruire);
        panneauGauche.getChildren().add(boutonAmeliorer);
        panneauGauche.getChildren().add(boutonDetails);
        panneauGauche.getChildren().add(separateur);
        panneauGauche.getChildren().add(boutonCycleSuivant);

        // On place le panneau a gauche
        root.setLeft(panneauGauche);
    }

    /**
     * Cree le panneau central avec l'affichage de l'energie et des batiments.
     */
    private void creerPanneauCentral() {
        panneauCentral = new VBox(15);
        panneauCentral.setPadding(new Insets(15));

        // Section Energie
        VBox sectionEnergie = creerSectionEnergie();

        // Section Batiments (centrales et residences)
        HBox sectionBatiments = creerSectionBatiments();

        panneauCentral.getChildren().add(sectionEnergie);
        panneauCentral.getChildren().add(sectionBatiments);

        root.setCenter(panneauCentral);
    }

    /**
     * Cree la section affichant la production et la demande d'energie.
     */
    private VBox creerSectionEnergie() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(15));
        section.getStyleClass().add("panel");

        // Titre
        Label titre = new Label("ÉNERGIE");
        titre.getStyleClass().add("panel-header");

        // Ligne Production
        HBox ligneProduction = new HBox(10);
        ligneProduction.setAlignment(Pos.CENTER_LEFT);

        labelProduction = new Label("Production: 0 kW");
        labelProduction.setPrefWidth(150);

        barreProduction = new ProgressBar(0);
        barreProduction.setPrefWidth(500);
        barreProduction.getStyleClass().add("progress-bar");
        barreProduction.getStyleClass().add("progress-bar-success");

        ligneProduction.getChildren().add(labelProduction);
        ligneProduction.getChildren().add(barreProduction);

        // Ligne Demande
        HBox ligneDemande = new HBox(10);
        ligneDemande.setAlignment(Pos.CENTER_LEFT);

        labelDemande = new Label("Demande: 0 kW");
        labelDemande.setPrefWidth(150);

        barreDemande = new ProgressBar(0);
        barreDemande.setPrefWidth(500);
        barreDemande.getStyleClass().add("progress-bar");
        barreDemande.getStyleClass().add("progress-bar-warning");

        ligneDemande.getChildren().add(labelDemande);
        ligneDemande.getChildren().add(barreDemande);

        // Balance
        labelBalance = new Label("Balance: 0 kW");
        labelBalance.getStyleClass().add("label-stat");
        labelBalance.setStyle("-fx-font-size: 14px;");

        // On assemble
        section.getChildren().add(titre);
        section.getChildren().add(ligneProduction);
        section.getChildren().add(ligneDemande);
        section.getChildren().add(labelBalance);

        return section;
    }

    /**
     * Cree la section affichant les centrales et les residences.
     */
    private HBox creerSectionBatiments() {
        HBox section = new HBox(20);
        section.setAlignment(Pos.TOP_CENTER);

        // === COLONNE CENTRALES ===
        VBox boiteCentrales = new VBox(8);
        boiteCentrales.setPadding(new Insets(12));
        boiteCentrales.getStyleClass().add("panel");
        boiteCentrales.setPrefWidth(450);

        Label titreCentrales = new Label("CENTRALES");
        titreCentrales.getStyleClass().add("panel-header");

        listeCentrales = new ListView<String>();
        listeCentrales.setPrefHeight(180);
        listeCentrales.getStyleClass().add("list-view");

        boiteCentrales.getChildren().add(titreCentrales);
        boiteCentrales.getChildren().add(listeCentrales);

        // === COLONNE RESIDENCES ===
        VBox boiteResidences = new VBox(8);
        boiteResidences.setPadding(new Insets(12));
        boiteResidences.getStyleClass().add("panel");
        boiteResidences.setPrefWidth(450);

        Label titreResidences = new Label("RÉSIDENCES");
        titreResidences.getStyleClass().add("panel-header");

        listeResidences = new ListView<String>();
        listeResidences.setPrefHeight(180);
        listeResidences.getStyleClass().add("list-view");

        boiteResidences.getChildren().add(titreResidences);
        boiteResidences.getChildren().add(listeResidences);

        // On assemble
        section.getChildren().add(boiteCentrales);
        section.getChildren().add(boiteResidences);

        return section;
    }

    /**
     * Cree le panneau du bas avec le journal des evenements.
     */
    private void creerPanneauBas() {
        VBox panneauBas = new VBox(8);
        panneauBas.setPadding(new Insets(12, 15, 12, 15));
        panneauBas.setPrefHeight(150);

        // Titre
        Label titre = new Label("JOURNAL");
        titre.getStyleClass().add("panel-header");

        // Zone de texte pour le log
        zoneLog = new TextArea();
        zoneLog.setEditable(false); // On ne peut pas modifier le texte
        zoneLog.setWrapText(true); // Retour a la ligne automatique
        zoneLog.getStyleClass().add("log-area");
        VBox.setVgrow(zoneLog, Priority.ALWAYS); // Prend tout l'espace disponible

        panneauBas.getChildren().add(titre);
        panneauBas.getChildren().add(zoneLog);

        root.setBottom(panneauBas);
    }

    /**
     * Met a jour l'affichage avec l'etat du jeu actuel.
     * 
     * @param gameState L'etat du jeu
     */
    public void updateDisplay(GameState gameState) {
        City city = gameState.getCity();

        // === MISE A JOUR DE LA BARRE DU HAUT ===
        labelNomColonie.setText(city.getName());
        labelCycle.setText("Cycle: " + gameState.getCurrentCycle());
        labelResources.setText("Crédits: " + gameState.getResources());

        double bonheur = gameState.getHappiness();
        labelBonheur.setText("Bonheur: " + Math.round(bonheur * 100) + "% (" + gameState.getHappinessStatus() + ")");
        barreBonheur.setProgress(bonheur);

        // On change la couleur de la barre selon le niveau de bonheur
        barreBonheur.getStyleClass().remove("progress-bar-success");
        barreBonheur.getStyleClass().remove("progress-bar-warning");
        barreBonheur.getStyleClass().remove("progress-bar-danger");

        if (bonheur >= 0.7) {
            barreBonheur.getStyleClass().add("progress-bar-success");
        } else if (bonheur >= 0.4) {
            barreBonheur.getStyleClass().add("progress-bar-warning");
        } else {
            barreBonheur.getStyleClass().add("progress-bar-danger");
        }

        // === MISE A JOUR DE LA SECTION ENERGIE ===
        double production = city.getTotalEnergyProduction();
        double demande = city.getTotalEnergyDemand();

        labelProduction.setText("Production: " + Math.round(production) + " kW");
        labelDemande.setText("Demande: " + Math.round(demande) + " kW");

        // On calcule le maximum pour les barres de progression
        double maximum = production;
        if (demande > maximum) {
            maximum = demande;
        }

        // On met a jour les barres
        if (maximum > 0) {
            barreProduction.setProgress(production / maximum);
            barreDemande.setProgress(demande / maximum);
        } else {
            barreProduction.setProgress(0);
            barreDemande.setProgress(0);
        }

        // Balance
        double balance = production - demande;
        String texteBalance;
        String styleBalance;

        if (balance >= 0) {
            texteBalance = "Excédent: +" + Math.round(balance) + " kW";
            styleBalance = "-fx-text-fill: #00ff00; -fx-font-weight: bold;"; // Vert
        } else {
            texteBalance = "Déficit: " + Math.round(balance) + " kW";
            styleBalance = "-fx-text-fill: #ff0000; -fx-font-weight: bold;"; // Rouge
        }
        labelBalance.setText(texteBalance);
        labelBalance.setStyle("-fx-font-size: 14px; " + styleBalance);

        // === MISE A JOUR DES LISTES ===

        // Liste des centrales
        listeCentrales.getItems().clear();
        List<PowerPlant> centrales = city.getPowerPlants();
        for (int i = 0; i < centrales.size(); i++) {
            PowerPlant centrale = centrales.get(i);
            String texte = centrale.getType().getIcon() + " " + centrale.toString();
            listeCentrales.getItems().add(texte);
        }

        // Liste des residences
        listeResidences.getItems().clear();
        List<Residence> residences = city.getResidences();
        for (int i = 0; i < residences.size(); i++) {
            Residence residence = residences.get(i);
            listeResidences.getItems().add(residence.toString());
        }
    }

    /**
     * Ajoute des messages au journal.
     * 
     * @param logs Liste des messages a ajouter
     */
    public void addLogs(List<String> logs) {
        for (int i = 0; i < logs.size(); i++) {
            String message = logs.get(i);
            zoneLog.appendText(message + "\n");
        }
        // On fait defiler vers le bas
        zoneLog.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * Efface le contenu du journal.
     */
    public void clearLogs() {
        zoneLog.clear();
    }

    // ========================================
    // GETTERS POUR LES BOUTONS
    // ========================================

    public Button getBuildButton() {
        return boutonConstruire;
    }

    public Button getUpgradeButton() {
        return boutonAmeliorer;
    }

    public Button getDetailsButton() {
        return boutonDetails;
    }

    public Button getNextCycleButton() {
        return boutonCycleSuivant;
    }

    public ListView<String> getPowerPlantsList() {
        return listeCentrales;
    }

    public BorderPane getRoot() {
        return root;
    }

    /**
     * Cree et retourne une Scene contenant cette vue.
     */
    public Scene createScene() {
        Scene scene = new Scene(root, 1400, 850);
        String cheminCSS = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(cheminCSS);
        return scene;
    }
}
