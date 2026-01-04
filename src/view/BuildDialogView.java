package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PowerPlantType;

/**
 * Dialogue modal pour construire une nouvelle centrale électrique.
 * Permet de sélectionner le type et affiche les coûts.
 */
public class BuildDialogView extends Dialog<PowerPlantType> {
    private ToggleGroup typeGroup;
    private Label costLabel;
    private Label productionLabel;
    
    public BuildDialogView(int currentResources) {
        setTitle("Construire une Centrale");
        setHeaderText("Sélectionnez le type de centrale à construire");
        
        // Style du dialogue
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStyleClass().add("dialog-pane");
        
        // Contenu
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        typeGroup = new ToggleGroup();
        
        // Créer un RadioButton pour chaque type de centrale
        for (PowerPlantType type : PowerPlantType.values()) {
            RadioButton rb = createPowerPlantOption(type);
            typeGroup.getToggles().add(rb);
            content.getChildren().add(rb);
        }
        
        // Sélectionner le premier par défaut
        if (!typeGroup.getToggles().isEmpty()) {
            typeGroup.getToggles().get(0).setSelected(true);
        }
        
        // Informations sur le choix sélectionné
        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(15));
        infoBox.getStyleClass().add("panel");
        
        costLabel = new Label();
        costLabel.getStyleClass().add("label-stat");
        
        productionLabel = new Label();
        productionLabel.getStyleClass().add("label-stat");
        
        Label resourcesLabel = new Label("Ressources disponibles: " + currentResources + " crédits");
        resourcesLabel.setStyle("-fx-text-fill: #00d4ff;");
        
        infoBox.getChildren().addAll(costLabel, productionLabel, resourcesLabel);
        
        content.getChildren().add(infoBox);
        
        // Mettre à jour les infos quand la sélection change
        typeGroup.selectedToggleProperty().addListener((obs, old, newToggle) -> {
            if (newToggle != null) {
                updateInfo();
            }
        });
        
        updateInfo(); // Affichage initial
        
        dialogPane.setContent(content);
        
        // Boutons
        ButtonType buildButtonType = new ButtonType("Construire", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(buildButtonType, ButtonType.CANCEL);
        
        // Convertir le résultat
        setResultConverter(buttonType -> {
            if (buttonType == buildButtonType && typeGroup.getSelectedToggle() != null) {
                return (PowerPlantType) typeGroup.getSelectedToggle().getUserData();
            }
            return null;
        });
        
        // Charger le style
        try {
            dialogPane.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Impossible de charger styles.css: " + e.getMessage());
        }
    }
    
    private RadioButton createPowerPlantOption(PowerPlantType type) {
        RadioButton rb = new RadioButton();
        rb.setUserData(type);
        
        // Créer un HBox pour l'icône et les détails
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10));
        container.getStyleClass().add("panel");
        
        Label iconLabel = new Label(type.getIcon());
        iconLabel.getStyleClass().add("icon-label");
        
        VBox details = new VBox(5);
        Label nameLabel = new Label(type.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        int cost = model.PowerPlant.getBuildCost(type);
        double production = model.PowerPlant.getBaseProduction(type);
        
        Label costInfoLabel = new Label(String.format("Coût: %d cr | Production: %.0f kW", cost, production));
        costInfoLabel.getStyleClass().add("label-secondary");
        
        details.getChildren().addAll(nameLabel, costInfoLabel);
        
        container.getChildren().addAll(iconLabel, details);
        
        rb.setGraphic(container);
        rb.getStyleClass().add("radio-button");
        
        return rb;
    }
    
    private void updateInfo() {
        Toggle selected = typeGroup.getSelectedToggle();
        if (selected != null) {
            PowerPlantType type = (PowerPlantType) selected.getUserData();
            int cost = model.PowerPlant.getBuildCost(type);
            double production = model.PowerPlant.getBaseProduction(type);
            
            costLabel.setText("💰 Coût de construction: " + cost + " crédits");
            productionLabel.setText("⚡ Production: " + String.format("%.0f", production) + " kW");
        }
    }
}
