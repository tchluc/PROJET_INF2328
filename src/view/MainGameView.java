package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import model.*;

/**
 * Vue principale du jeu.
 * Layout BorderPane avec panneaux d'information, contrôles et visualisation.
 */
public class MainGameView {
    private BorderPane root;
    
    // Top: Barre d'informations
    private Label resourcesLabel;
    private Label happinessLabel;
    private Label cycleLabel;
    private Label colonyNameLabel;
    private ProgressBar happinessBar;
    
    // Left: Panel de contrôle
    private Button buildButton;
    private Button upgradeButton;
    private Button detailsButton;
    private Button nextCycleButton;
    
    // Center: Zone de visualisation
    private VBox centerPanel;
    private ProgressBar productionBar;
    private ProgressBar demandBar;
    private Label productionLabel;
    private Label demandLabel;
    private Label balanceLabel;
    private ListView<String> powerPlantsList;
    private ListView<String> residencesList;
    
    // Bottom: Zone de logs
    private TextArea logArea;
    
    public MainGameView() {
        createView();
    }
    
    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("game-root");
        
        createTopBar();
        createLeftPanel();
        createCenterPanel();
        createBottomPanel();
    }
    
    /**
     * Crée la barre supérieure d'informations
     */
    private void createTopBar() {
        HBox topBar = new HBox(30);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("info-bar");
        
        // Nom de la colonie
        colonyNameLabel = new Label("Colonie Nova-7");
        colonyNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #00d4ff;");
        
        // Cycle
        cycleLabel = new Label("Cycle: 0");
        cycleLabel.getStyleClass().add("label-stat");
        
        // Ressources
        resourcesLabel = new Label("💰 Crédits: 0");
        resourcesLabel.getStyleClass().add("label-stat");
        
        // Bonheur avec barre de progression
        VBox happinessBox = new VBox(5);
        happinessLabel = new Label("😊 Bonheur: 0%");
        happinessLabel.getStyleClass().add("label-stat");
        
        happinessBar = new ProgressBar(0);
        happinessBar.setPrefWidth(150);
        happinessBar.getStyleClass().add("progress-bar");
        
        happinessBox.getChildren().addAll(happinessLabel, happinessBar);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        topBar.getChildren().addAll(colonyNameLabel, cycleLabel, resourcesLabel, happinessBox, spacer);
        
        root.setTop(topBar);
    }
    
    /**
     * Crée le panneau gauche de contrôle
     */
    private void createLeftPanel() {
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(250);
        leftPanel.getStyleClass().add("panel");
        
        Label controlLabel = new Label("🎮 CONTRÔLES");
        controlLabel.getStyleClass().add("panel-header");
        
        buildButton = new Button("🏗️ Construire Centrale");
        buildButton.setMaxWidth(Double.MAX_VALUE);
        buildButton.getStyleClass().add("button");
        
        upgradeButton = new Button("⬆️ Améliorer Centrale");
        upgradeButton.setMaxWidth(Double.MAX_VALUE);
        upgradeButton.getStyleClass().add("button");
        
        detailsButton = new Button("📊 Voir Détails");
        detailsButton.setMaxWidth(Double.MAX_VALUE);
        detailsButton.getStyleClass().add("button");
        
        Separator separator = new Separator();
        
        nextCycleButton = new Button("⏩ CYCLE SUIVANT");
        nextCycleButton.setMaxWidth(Double.MAX_VALUE);
        nextCycleButton.getStyleClass().addAll("button", "button-primary");
        nextCycleButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        leftPanel.getChildren().addAll(
            controlLabel,
            buildButton,
            upgradeButton,
            detailsButton,
            separator,
            nextCycleButton
        );
        
        root.setLeft(leftPanel);
    }
    
    /**
     * Crée le panneau central de visualisation
     */
    private void createCenterPanel() {
        centerPanel = new VBox(20);
        centerPanel.setPadding(new Insets(20));
        
        // Section Production/Demande
        VBox energySection = createEnergySection();
        
        // Section Centrales et Résidences
        HBox buildingsSection = createBuildingsSection();
        
        centerPanel.getChildren().addAll(energySection, buildingsSection);
        
        root.setCenter(centerPanel);
    }
    
    /**
     * Crée la section affichant production et demande
     */
    private VBox createEnergySection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.getStyleClass().add("panel");
        
        Label header = new Label("⚡ ÉNERGIE");
        header.getStyleClass().add("panel-header");
        
        // Production
        HBox prodBox = new HBox(10);
        prodBox.setAlignment(Pos.CENTER_LEFT);
        productionLabel = new Label("Production: 0 kW");
        productionLabel.setPrefWidth(200);
        productionBar = new ProgressBar(0);
        productionBar.setPrefWidth(400);
        productionBar.getStyleClass().addAll("progress-bar", "progress-bar-success");
        prodBox.getChildren().addAll(productionLabel, productionBar);
        
        // Demande
        HBox demandBox = new HBox(10);
        demandBox.setAlignment(Pos.CENTER_LEFT);
        demandLabel = new Label("Demande: 0 kW");
        demandLabel.setPrefWidth(200);
        demandBar = new ProgressBar(0);
        demandBar.setPrefWidth(400);
        demandBar.getStyleClass().addAll("progress-bar", "progress-bar-warning");
        demandBox.getChildren().addAll(demandLabel, demandBar);
        
        // Balance
        balanceLabel = new Label("Balance: 0 kW");
        balanceLabel.getStyleClass().add("label-stat");
        balanceLabel.setStyle("-fx-font-size: 18px;");
        
        section.getChildren().addAll(header, prodBox, demandBox, balanceLabel);
        
        return section;
    }
    
    /**
     * Crée la section affichant les bâtiments
     */
    private HBox createBuildingsSection() {
        HBox section = new HBox(20);
        section.setAlignment(Pos.TOP_CENTER);
        
        // Centrales
        VBox plantsBox = new VBox(10);
        plantsBox.setPadding(new Insets(15));
        plantsBox.getStyleClass().add("panel");
        plantsBox.setPrefWidth(400);
        
        Label plantsHeader = new Label("🏭 CENTRALES ÉLECTRIQUES");
        plantsHeader.getStyleClass().add("panel-header");
        
        powerPlantsList = new ListView<>();
        powerPlantsList.setPrefHeight(200);
        powerPlantsList.getStyleClass().add("list-view");
        
        plantsBox.getChildren().addAll(plantsHeader, powerPlantsList);
        
        // Résidences
        VBox residencesBox = new VBox(10);
        residencesBox.setPadding(new Insets(15));
        residencesBox.getStyleClass().add("panel");
        residencesBox.setPrefWidth(400);
        
        Label residencesHeader = new Label("🏘️ RÉSIDENCES");
        residencesHeader.getStyleClass().add("panel-header");
        
        residencesList = new ListView<>();
        residencesList.setPrefHeight(200);
        residencesList.getStyleClass().add("list-view");
        
        residencesBox.getChildren().addAll(residencesHeader, residencesList);
        
        section.getChildren().addAll(plantsBox, residencesBox);
        
        return section;
    }
    
    /**
     * Crée le panneau inférieur de logs
     */
    private void createBottomPanel() {
        VBox bottomPanel = new VBox(10);
        bottomPanel.setPadding(new Insets(15, 20, 15, 20));
        bottomPanel.setPrefHeight(180);
        
        Label logHeader = new Label("📜 JOURNAL DE BORD");
        logHeader.getStyleClass().add("panel-header");
        
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.getStyleClass().add("log-area");
        VBox.setVgrow(logArea, Priority.ALWAYS);
        
        bottomPanel.getChildren().addAll(logHeader, logArea);
        
        root.setBottom(bottomPanel);
    }
    
    /**
     * Met à jour l'affichage avec l'état du jeu actuel
     */
    public void updateDisplay(GameState gameState) {
        City city = gameState.getCity();
        
        // Top bar
        colonyNameLabel.setText("🚀 " + city.getName());
        cycleLabel.setText("Cycle: " + gameState.getCurrentCycle());
        resourcesLabel.setText("💰 Crédits: " + gameState.getResources());
        
        double happiness = gameState.getHappiness();
        happinessLabel.setText(String.format("😊 Bonheur: %.0f%% (%s)", 
                happiness * 100, gameState.getHappinessStatus()));
        happinessBar.setProgress(happiness);
        
        // Changer la couleur de la barre selon le niveau
        happinessBar.getStyleClass().removeAll("progress-bar-success", "progress-bar-warning", "progress-bar-danger");
        if (happiness >= 0.7) {
            happinessBar.getStyleClass().add("progress-bar-success");
        } else if (happiness >= 0.4) {
            happinessBar.getStyleClass().add("progress-bar-warning");
        } else {
            happinessBar.getStyleClass().add("progress-bar-danger");
        }
        
        // Production et demande
        double production = city.getTotalEnergyProduction();
        double demand = city.getTotalEnergyDemand();
        double maxValue = Math.max(production, demand);
        
        productionLabel.setText(String.format("Production: %.0f kW", production));
        demandLabel.setText(String.format("Demande: %.0f kW", demand));
        
        if (maxValue > 0) {
            productionBar.setProgress(production / maxValue);
            demandBar.setProgress(demand / maxValue);
        } else {
            productionBar.setProgress(0);
            demandBar.setProgress(0);
        }
        
        // Balance
        double balance = production - demand;
        String balanceText;
        String balanceStyle;
        
        if (balance >= 0) {
            balanceText = String.format("✅ Excédent: +%.0f kW", balance);
            balanceStyle = "-fx-text-fill: #10b981;";
        } else {
            balanceText = String.format("❌ Déficit: %.0f kW", balance);
            balanceStyle = "-fx-text-fill: #ef4444;";
        }
        balanceLabel.setText(balanceText);
        balanceLabel.setStyle("-fx-font-size: 18px; " + balanceStyle);
        
        // Listes de bâtiments
        powerPlantsList.getItems().clear();
        for (PowerPlant plant : city.getPowerPlants()) {
            powerPlantsList.getItems().add(plant.getType().getIcon() + " " + plant.toString());
        }
        
        residencesList.getItems().clear();
        for (Residence residence : city.getResidences()) {
            residencesList.getItems().add(residence.toString());
        }
    }
    
    /**
     * Ajoute des messages au log
     */
    public void addLogs(java.util.List<String> logs) {
        for (String log : logs) {
            logArea.appendText(log + "\n");
        }
        // Auto-scroll vers le bas
        logArea.setScrollTop(Double.MAX_VALUE);
    }
    
    /**
     * Efface le log
     */
    public void clearLogs() {
        logArea.clear();
    }
    
    // Getters pour les boutons
    public Button getBuildButton() {
        return buildButton;
    }
    
    public Button getUpgradeButton() {
        return upgradeButton;
    }
    
    public Button getDetailsButton() {
        return detailsButton;
    }
    
    public Button getNextCycleButton() {
        return nextCycleButton;
    }
    
    public ListView<String> getPowerPlantsList() {
        return powerPlantsList;
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    public Scene createScene() {
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        return scene;
    }
}
