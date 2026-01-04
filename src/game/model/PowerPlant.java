package game.model;

import game.model.GameConfig.PlantType;

/**
 * Represents a power plant in the city.
 * Power plants produce energy and have operating costs.
 */
public class PowerPlant {
    private static int nextId = 1;
    
    private final int id;
    private final PlantType type;
    private int level;
    private boolean operational;
    private double productionEfficiency; // 0.0 to 1.5 (weather/maintenance effects)
    
    /**
     * Create a new power plant of the given type at level 1.
     */
    public PowerPlant(PlantType type) {
        this(type, 1);
    }
    
    /**
     * Create a new power plant of the given type at a specific level.
     */
    public PowerPlant(PlantType type, int level) {
        this.id = nextId++;
        this.type = type;
        this.level = level;
        this.operational = true;
        this.productionEfficiency = 1.0;
    }
    
    /**
     * Get the base production capacity (kWh per day).
     */
    public double getBaseProduction() {
        return GameConfig.PLANT_PRODUCTION_BASE[type.ordinal()][level - 1];
    }
    
    /**
     * Get the actual production considering efficiency and weather.
     */
    public double getActualProduction() {
        if (!operational) return 0;
        return getBaseProduction() * productionEfficiency;
    }
    
    /**
     * Get the daily operating cost.
     */
    public double getOperatingCost() {
        if (!operational) return 0;
        return GameConfig.PLANT_OPERATING_COSTS[type.ordinal()][level - 1];
    }
    
    /**
     * Get the pollution produced per day.
     */
    public double getPollution() {
        if (!operational) return 0;
        return GameConfig.PLANT_POLLUTION[type.ordinal()][level - 1];
    }
    
    /**
     * Get the cost to upgrade to the next level.
     */
    public double getUpgradeCost() {
        if (level >= 5) return -1; // Cannot upgrade
        return GameConfig.PLANT_UPGRADE_COSTS[type.ordinal()][level - 1];
    }
    
    /**
     * Check if the plant can be upgraded.
     */
    public boolean canUpgrade() {
        return level < 5;
    }
    
    /**
     * Upgrade the plant to the next level.
     */
    public boolean upgrade() {
        if (canUpgrade()) {
            level++;
            return true;
        }
        return false;
    }
    
    /**
     * Apply weather effects to production efficiency.
     */
    public void applyWeatherEffect() {
        switch (type) {
            case SOLAR -> productionEfficiency = GameConfig.getSolarWeatherEffect();
            case WIND -> productionEfficiency = GameConfig.getWindWeatherEffect();
            default -> productionEfficiency = GameConfig.randomInRange(0.9, 1.1);
        }
    }
    
    /**
     * Toggle operational status.
     */
    public void setOperational(boolean operational) {
        this.operational = operational;
    }
    
    /**
     * Perform maintenance (reset efficiency to optimal).
     */
    public void performMaintenance() {
        productionEfficiency = 1.0;
    }
    
    // Getters
    public int getId() { return id; }
    public PlantType getType() { return type; }
    public int getLevel() { return level; }
    public boolean isOperational() { return operational; }
    public double getProductionEfficiency() { return productionEfficiency; }
    
    /**
     * Get the build cost for a specific plant type and level.
     */
    public static double getBuildCost(PlantType type, int level) {
        return GameConfig.PLANT_BUILD_COSTS[type.ordinal()][level - 1];
    }
    
    @Override
    public String toString() {
        String status = operational ? "Opérationnelle" : "Arrêtée";
        return String.format("%s %s #%d [Niveau %d] - Production: %.0f kWh/jour (%.0f%% eff.), Coût: %.0f€/jour - %s",
                type.getIcon(), type.getName(), id, level, 
                getActualProduction(), productionEfficiency * 100,
                getOperatingCost(), status);
    }
}
