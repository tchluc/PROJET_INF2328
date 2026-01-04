package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Moteur du jeu - Gère la logique principale et la simulation.
 * Responsable de l'initialisation, du déroulement des cycles et des événements.
 */
public class GameEngine {
    private static final Random random = new Random();
    
    private GameState gameState;
    private List<String> eventLog; // Log des événements récents
    
    // Événements aléatoires possibles
    private static final String[] POSITIVE_EVENTS = {
        "Découverte d'un gisement de cristaux énergétiques ! +100 crédits",
        "Les ingénieurs ont optimisé le réseau. Production +5% ce cycle.",
        "Arrivée d'une navette avec des provisions. Moral en hausse !",
        "Une innovation technique réduit les coûts d'entretien de 20% ce cycle."
    };
    
    private static final String[] NEGATIVE_EVENTS = {
        "Tempête solaire détectée. Production réduite de 10% ce cycle.",
        "Panne dans un secteur résidentiel. Satisfaction en baisse.",
        "Hausse des prix des matériaux de construction.",
        "Vague de chaleur inhabituelle. Demande énergétique +15%."
    };
    
    /**
     * Constructeur du moteur de jeu
     * @param colonyName Nom de la colonie
     */
    public GameEngine(String colonyName) {
        this.gameState = new GameState(colonyName);
        this.eventLog = new ArrayList<>();
        initializeGame();
    }
    
    /**
     * Initialise le jeu avec quelques résidences de départ
     */
    private void initializeGame() {
        City city = gameState.getCity();
        
        // Créer 5 résidences initiales de différents niveaux
        city.addResidence(new Residence(1, 1));
        city.addResidence(new Residence(2, 1));
        city.addResidence(new Residence(3, 2));
        city.addResidence(new Residence(4, 2));
        city.addResidence(new Residence(5, 3));
        
        // Une petite centrale solaire pour commencer
        city.addPowerPlant(new PowerPlant(PowerPlantType.SOLAR, 1));
        
        addLog("Bienvenue sur la colonie " + city.getName() + " !");
        addLog("Vous commencez avec " + gameState.getResources() + " crédits.");
        addLog(String.format("Population initiale: %d habitants dans %d résidences.",
                city.getTotalPopulation(), city.getResidenceCount()));
    }
    
    /**
     * Construit une nouvelle centrale électrique
     * @param type Type de centrale à construire
     * @return true si la construction a réussi
     */
    public boolean buildPowerPlant(PowerPlantType type) {
        int cost = PowerPlant.getBuildCost(type);
        
        if (!gameState.hasEnoughResources(cost)) {
            addLog("Ressources insuffisantes pour construire une centrale " + type.getName() + 
                   ". Coût: " + cost + " crédits.");
            return false;
        }
        
        gameState.spendResources(cost);
        PowerPlant newPlant = new PowerPlant(type);
        gameState.getCity().addPowerPlant(newPlant);
        
        addLog(String.format("Centrale %s construite ! Production: %.0f kW, Coût: %d crédits.",
                type.getName(), newPlant.getProduction(), cost));
        
        return true;
    }
    
    /**
     * Améliore une centrale existante
     * @param powerPlant Centrale à améliorer
     * @return true si l'amélioration a réussi
     */
    public boolean upgradePowerPlant(PowerPlant powerPlant) {
        if (!powerPlant.canUpgrade()) {
            addLog("Cette centrale est déjà au niveau maximum !");
            return false;
        }
        
        int cost = powerPlant.getUpgradeCost();
        
        if (!gameState.hasEnoughResources(cost)) {
            addLog("Ressources insuffisantes pour améliorer cette centrale. Coût: " + cost + " crédits.");
            return false;
        }
        
        int oldLevel = powerPlant.getLevel();
        double oldProduction = powerPlant.getProduction();
        
        gameState.spendResources(cost);
        powerPlant.upgrade();
        
        addLog(String.format("Centrale %s améliorée: Niv.%d → Niv.%d. Production: %.0f → %.0f kW.",
                powerPlant.getType().getName(), oldLevel, powerPlant.getLevel(), 
                oldProduction, powerPlant.getProduction()));
        
        return true;
    }
    
    /**
     * Exécute un cycle de jeu complet
     */
    public void processCycle() {
        gameState.nextCycle();
        addLog("\n=== CYCLE " + gameState.getCurrentCycle() + " ===");
        
        City city = gameState.getCity();
        
        // 1. Distribution de l'énergie et calcul des revenus
        double revenue = city.distributeEnergy();
        
        // 2. Calcul des coûts d'entretien
        int maintenanceCost = city.getTotalMaintenanceCost();
        
        // 3. Bilan financier
        int netIncome = (int) revenue - maintenanceCost;
        gameState.addResources(netIncome);
        
        addLog(String.format("Revenus de vente: %.0f crédits | Entretien: %d crédits | Net: %d crédits",
                revenue, maintenanceCost, netIncome));
        
        // 4. Mise à jour du bonheur basé sur la satisfaction moyenne
        double avgSatisfaction = city.getAverageSatisfaction();
        double newHappiness = avgSatisfaction * 0.7 + gameState.getHappiness() * 0.3; // Moyenne pondérée
        
        // Décroissance naturelle si production insuffisante
        double energyRatio = gameState.getEnergyRatio();
        if (energyRatio < 0.8) {
            newHappiness -= GameState.HAPPINESS_DECAY_RATE;
            addLog("⚠ Production insuffisante ! Le moral baisse...");
        }
        
        gameState.setHappiness(newHappiness);
        
        // 5. Croissance de la population
        city.simulateGrowth();
        
        // 6. Événements aléatoires (20% de chance)
        if (random.nextDouble() < 0.2) {
            triggerRandomEvent();
        }
        
        // 7. Afficher le statut
        addLog(String.format("Énergie: %.0f/%.0f kW | Bonheur: %.0f%% (%s)",
                city.getTotalEnergyProduction(), city.getTotalEnergyDemand(),
                gameState.getHappiness() * 100, gameState.getHappinessStatus()));
        
        // 8. Vérifier ressources négatives (game over)
        if (gameState.getResources() < 0 && netIncome < 0) {
            // Si déficit prolongé
            gameState.endGame("Vous êtes en faillite ! Les créanciers ont repris le contrôle de la colonie.");
        }
        
        // 9. Possibilité d'ajouter une nouvelle résidence si bonne performance
        if (gameState.getCurrentCycle() % 5 == 0 && avgSatisfaction > 0.7) {
            int newId = city.getResidenceCount() + 1;
            int level = 1 + random.nextInt(3);
            city.addResidence(new Residence(newId, level));
            addLog("✨ Nouvelle résidence construite (Niv." + level + ") ! La colonie s'agrandit.");
        }
    }
    
    /**
     * Déclenche un événement aléatoire
     */
    private void triggerRandomEvent() {
        boolean positive = random.nextBoolean();
        
        if (positive) {
            String event = POSITIVE_EVENTS[random.nextInt(POSITIVE_EVENTS.length)];
            addLog("📰 " + event);
            
            // Appliquer les effets (simplifié)
            if (event.contains("crédits")) {
                gameState.addResources(100);
            }
            if (event.contains("Moral")) {
                gameState.adjustHappiness(0.05);
            }
        } else {
            String event = NEGATIVE_EVENTS[random.nextInt(NEGATIVE_EVENTS.length)];
            addLog("📰 " + event);
            
            // Appliquer les effets
            if (event.contains("Satisfaction")) {
                gameState.adjustHappiness(-0.05);
            }
        }
    }
    
    /**
     * Ajoute un message au log
     * @param message Message à ajouter
     */
    public void addLog(String message) {
        eventLog.add(message);
        // Garder seulement les 50 derniers messages
        if (eventLog.size() > 50) {
            eventLog.remove(0);
        }
    }
    
    /**
     * Obtient les logs récents
     * @param count Nombre de logs à récupérer
     * @return Liste des logs
     */
    public List<String> getRecentLogs(int count) {
        int start = Math.max(0, eventLog.size() - count);
        return new ArrayList<>(eventLog.subList(start, eventLog.size()));
    }
    
    /**
     * Réinitialise le jeu
     * @param colonyName Nom de la nouvelle colonie
     */
    public void resetGame(String colonyName) {
        this.gameState = new GameState(colonyName);
        this.eventLog.clear();
        initializeGame();
    }
    
    // Getters
    public GameState getGameState() {
        return gameState;
    }
    
    public List<String> getAllLogs() {
        return new ArrayList<>(eventLog);
    }
}
