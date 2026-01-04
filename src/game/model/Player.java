package game.model;

/**
 * Represents the player/manager in the game.
 * Manages resources and player statistics.
 */
public class Player {
    private double resources;
    private int totalDaysManaged;
    private double totalRevenue;
    private double totalExpenses;
    private int plantsBuilt;
    private int plantsUpgraded;
    
    /**
     * Create a new player with initial resources.
     */
    public Player() {
        this.resources = GameConfig.INITIAL_RESOURCES;
        this.totalDaysManaged = 0;
        this.totalRevenue = 0;
        this.totalExpenses = 0;
        this.plantsBuilt = 0;
        this.plantsUpgraded = 0;
    }
    
    /**
     * Spend resources if available.
     * @return true if the purchase was successful, false if not enough resources
     */
    public boolean spend(double amount) {
        if (amount <= resources) {
            resources -= amount;
            totalExpenses += amount;
            return true;
        }
        return false;
    }
    
    /**
     * Earn resources (from selling energy).
     */
    public void earn(double amount) {
        resources += amount;
        totalRevenue += amount;
    }
    
    /**
     * Check if player can afford an amount.
     */
    public boolean canAfford(double amount) {
        return resources >= amount;
    }
    
    /**
     * Increment the days managed counter.
     */
    public void addDayManaged() {
        totalDaysManaged++;
    }
    
    /**
     * Record a plant build.
     */
    public void recordPlantBuilt() {
        plantsBuilt++;
    }
    
    /**
     * Record a plant upgrade.
     */
    public void recordPlantUpgraded() {
        plantsUpgraded++;
    }
    
    // Getters
    public double getResources() { return resources; }
    public int getTotalDaysManaged() { return totalDaysManaged; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalExpenses() { return totalExpenses; }
    public int getPlantsBuilt() { return plantsBuilt; }
    public int getPlantsUpgraded() { return plantsUpgraded; }
    
    /**
     * Get profit (revenue - expenses).
     */
    public double getProfit() {
        return totalRevenue - totalExpenses;
    }
    
    /**
     * Get a summary of player statistics.
     */
    public String getStatsSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Statistiques du Joueur ===\n");
        sb.append(String.format("Ressources actuelles: %.0f€\n", resources));
        sb.append(String.format("Jours gérés: %d\n", totalDaysManaged));
        sb.append(String.format("Revenus totaux: %.0f€\n", totalRevenue));
        sb.append(String.format("Dépenses totales: %.0f€\n", totalExpenses));
        sb.append(String.format("Profit net: %.0f€\n", getProfit()));
        sb.append(String.format("Centrales construites: %d\n", plantsBuilt));
        sb.append(String.format("Améliorations effectuées: %d\n", plantsUpgraded));
        return sb.toString();
    }
}
