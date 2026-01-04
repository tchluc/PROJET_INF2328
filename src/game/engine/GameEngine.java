package game.engine;

import game.model.*;
import game.model.GameConfig.PlantType;
import game.model.GameEvent.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game engine that manages the simulation.
 */
public class GameEngine {
    private final City city;
    private final Player player;
    private final List<GameEvent> activeEvents;
    
    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private boolean gameOver;
    private String gameOverReason;
    
    // Temporary modifiers from events
    private double solarModifier = 1.0;
    private double windModifier = 1.0;
    private double costModifier = 1.0;
    private double demandModifier = 1.0;
    
    /**
     * Create a new game engine.
     */
    public GameEngine(String cityName) {
        this.city = new City(cityName);
        this.player = new Player();
        this.activeEvents = new ArrayList<>();
        this.currentDay = 1;
        this.currentMonth = 1;
        this.currentYear = 2025;
        this.gameOver = false;
    }
    
    /**
     * Initialize the game with starting conditions.
     */
    public void initializeGame(int initialResidences) {
        city.initializeCity(initialResidences);
    }
    
    /**
     * Simulate one day.
     */
    public SimulationResult simulateDay() {
        if (gameOver) {
            return new SimulationResult(false, "La partie est terminée!", 0, 0);
        }
        
        SimulationResult result = new SimulationResult(true, "", 0, 0);
        
        // Apply weather effects
        city.applyWeatherEffects();
        applyEventEffects();
        
        // Calculate production (with per-plant-type modifiers) and demand
        double production = calculateTotalProductionWithModifiers();
        double demand = city.getTotalEnergyDemand() * demandModifier;
        
        // Distribute energy and calculate revenue
        double revenue = city.distributeEnergyWithProduction(production, demand);
        double operatingCosts = city.getTotalOperatingCosts() * costModifier;
        
        // Update player resources
        player.earn(revenue);
        player.spend(operatingCosts);
        player.addDayManaged();
        
        // Process events
        processEvents();
        
        // Generate new random events
        if (GameConfig.randomChance(GameConfig.EVENT_PROBABILITY)) {
            GameEvent newEvent = GameEvent.generateRandomEvent();
            activeEvents.add(newEvent);
            result.addMessage("📢 Nouvel événement: " + newEvent.getType().getName());
            
            // Handle one-time event effects when they start
            handleNewEventEffect(newEvent, result);
        }
        
        // Update date
        currentDay++;
        if (currentDay > GameConfig.DAYS_PER_MONTH) {
            currentDay = 1;
            currentMonth++;
            
            // Monthly city growth
            city.simulateGrowth();
            result.addMessage("📈 La ville a grandi! Nouvelles résidences ajoutées.");
            
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
                result.addMessage("🎉 Bonne année " + currentYear + "!");
            }
        }
        
        // Check game over conditions
        checkGameOverConditions();
        
        result.setRevenue(revenue);
        result.setExpenses(operatingCosts);
        result.setSuccess(!gameOver);
        
        if (gameOver) {
            result.addMessage("❌ GAME OVER: " + gameOverReason);
        }
        
        return result;
    }
    
    /**
     * Simulate multiple days.
     */
    public SimulationResult simulateDays(int days) {
        SimulationResult combinedResult = new SimulationResult(true, "", 0, 0);
        
        for (int i = 0; i < days && !gameOver; i++) {
            SimulationResult dayResult = simulateDay();
            combinedResult.addRevenue(dayResult.getRevenue());
            combinedResult.addExpenses(dayResult.getExpenses());
            for (String message : dayResult.getMessages()) {
                combinedResult.addMessage(message);
            }
        }
        
        combinedResult.setSuccess(!gameOver);
        return combinedResult;
    }
    
    /**
     * Build a new power plant.
     */
    public boolean buildPowerPlant(PlantType type, int level) {
        double cost = PowerPlant.getBuildCost(type, level);
        
        if (player.canAfford(cost)) {
            player.spend(cost);
            PowerPlant plant = new PowerPlant(type, level);
            city.addPowerPlant(plant);
            player.recordPlantBuilt();
            return true;
        }
        return false;
    }
    
    /**
     * Upgrade an existing power plant.
     */
    public boolean upgradePowerPlant(int plantId) {
        PowerPlant plant = city.getPowerPlantById(plantId);
        
        if (plant == null || !plant.canUpgrade()) {
            return false;
        }
        
        double cost = plant.getUpgradeCost();
        
        if (player.canAfford(cost)) {
            player.spend(cost);
            plant.upgrade();
            player.recordPlantUpgraded();
            return true;
        }
        return false;
    }
    
    /**
     * Toggle power plant operational status.
     */
    public boolean togglePowerPlant(int plantId) {
        PowerPlant plant = city.getPowerPlantById(plantId);
        if (plant != null) {
            plant.setOperational(!plant.isOperational());
            return true;
        }
        return false;
    }
    
    /**
     * Sell a power plant (recover some value).
     */
    public boolean sellPowerPlant(int plantId) {
        PowerPlant plant = city.getPowerPlantById(plantId);
        if (plant != null) {
            // Recover 40% of original build cost
            double sellValue = PowerPlant.getBuildCost(plant.getType(), plant.getLevel()) * 0.4;
            player.earn(sellValue);
            city.removePowerPlant(plantId);
            return true;
        }
        return false;
    }
    
    /**
     * Process active events (decrease duration, remove expired).
     */
    private void processEvents() {
        activeEvents.removeIf(event -> !event.tick());
        updateEventModifiers();
    }
    
    /**
     * Update modifiers based on active events.
     */
    private void updateEventModifiers() {
        // Reset modifiers
        solarModifier = 1.0;
        windModifier = 1.0;
        costModifier = 1.0;
        demandModifier = 1.0;
        
        for (GameEvent event : activeEvents) {
            switch (event.getType()) {
                case SUNNY_WEEK -> solarModifier *= 1.5;
                case STRONG_WINDS -> windModifier *= 1.4;
                case CLOUDY_WEATHER -> solarModifier *= 0.6;
                case CALM_WEATHER -> windModifier *= 0.5;
                case PRICE_INCREASE -> costModifier *= 1.2;
                case HEATWAVE -> demandModifier *= 1.3;
                case COLDSNAP -> demandModifier *= 1.4;
                case EFFICIENCY_BOOST -> {
                    solarModifier *= 1.15;
                    windModifier *= 1.15;
                }
                // GOVERNMENT_SUBSIDY is handled as a one-time effect in handleNewEventEffect
                default -> {}
            }
        }
    }
    
    /**
     * Handle one-time effects when a new event starts.
     */
    private void handleNewEventEffect(GameEvent event, SimulationResult result) {
        switch (event.getType()) {
            case GOVERNMENT_SUBSIDY -> {
                player.earn(500);
                result.addMessage("💰 Vous recevez 500€ de subvention!");
            }
            default -> {}
        }
    }
    
    /**
     * Apply event effects to production.
     */
    private void applyEventEffects() {
        for (PowerPlant plant : city.getPowerPlants()) {
            switch (plant.getType()) {
                case SOLAR, WIND -> plant.applyWeatherEffect();
                default -> {} // Other plants don't have weather effects
            }
        }
    }
    
    /**
     * Get production modifier for a specific plant type based on active events.
     */
    public double getProductionModifierForType(PlantType type) {
        return switch (type) {
            case SOLAR -> solarModifier;
            case WIND -> windModifier;
            default -> 1.0; // Coal, Nuclear, Hydro are not affected by weather events
        };
    }
    
    /**
     * Calculate total production with per-plant-type modifiers.
     */
    private double calculateTotalProductionWithModifiers() {
        double totalProduction = 0;
        for (PowerPlant plant : city.getPowerPlants()) {
            if (plant.isOperational()) {
                double modifier = getProductionModifierForType(plant.getType());
                totalProduction += plant.getActualProduction() * modifier;
            }
        }
        return totalProduction;
    }
    
    /**
     * Check game over conditions.
     */
    private void checkGameOverConditions() {
        // Check happiness threshold
        double happiness = city.getAverageHappiness();
        if (happiness < GameConfig.MIN_HAPPINESS_THRESHOLD) {
            gameOver = true;
            gameOverReason = "Le bonheur des habitants est tombé en dessous du seuil critique! " +
                    "Le maire vous retire la gestion de l'électricité.";
        }
        
        // Check bankruptcy
        if (player.getResources() < -1000) { // Allow small debt
            gameOver = true;
            gameOverReason = "Vous êtes en faillite! Impossible de continuer.";
        }
    }
    
    // Getters
    public City getCity() { return city; }
    public Player getPlayer() { return player; }
    public List<GameEvent> getActiveEvents() { return new ArrayList<>(activeEvents); }
    public int getCurrentDay() { return currentDay; }
    public int getCurrentMonth() { return currentMonth; }
    public int getCurrentYear() { return currentYear; }
    public boolean isGameOver() { return gameOver; }
    public String getGameOverReason() { return gameOverReason; }
    
    /**
     * Get formatted current date.
     */
    public String getCurrentDateString() {
        return String.format("%02d/%02d/%d", currentDay, currentMonth, currentYear);
    }
    
    /**
     * Get current modifiers for display.
     */
    public String getModifiersString() {
        StringBuilder sb = new StringBuilder();
        if (solarModifier != 1.0) sb.append(String.format("Solaire: %.0f%%, ", solarModifier * 100));
        if (windModifier != 1.0) sb.append(String.format("Éolien: %.0f%%, ", windModifier * 100));
        if (costModifier != 1.0) sb.append(String.format("Coûts: %.0f%%, ", costModifier * 100));
        if (demandModifier != 1.0) sb.append(String.format("Demande: %.0f%%, ", demandModifier * 100));
        return sb.length() > 0 ? sb.toString() : "Aucun modificateur actif";
    }
    
    /**
     * Result class for simulation operations.
     */
    public static class SimulationResult {
        private boolean success;
        private final List<String> messages;
        private double revenue;
        private double expenses;
        
        public SimulationResult(boolean success, String message, double revenue, double expenses) {
            this.success = success;
            this.messages = new ArrayList<>();
            if (!message.isEmpty()) {
                this.messages.add(message);
            }
            this.revenue = revenue;
            this.expenses = expenses;
        }
        
        public void addMessage(String message) { messages.add(message); }
        public void setSuccess(boolean success) { this.success = success; }
        public void setRevenue(double revenue) { this.revenue = revenue; }
        public void setExpenses(double expenses) { this.expenses = expenses; }
        public void addRevenue(double amount) { this.revenue += amount; }
        public void addExpenses(double amount) { this.expenses += amount; }
        
        public boolean isSuccess() { return success; }
        public List<String> getMessages() { return messages; }
        public double getRevenue() { return revenue; }
        public double getExpenses() { return expenses; }
        public double getProfit() { return revenue - expenses; }
    }
}
