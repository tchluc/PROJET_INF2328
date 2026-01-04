package game.model;

/**
 * Represents a residence in the city.
 * Each residence has a level that determines its energy needs and purchasing power.
 */
public class Residence {
    private static int nextId = 1;
    
    private final int id;
    private int level;
    private double energyDemand;      // kWh per day
    private double purchasingPower;   // Money per kWh
    private double satisfaction;       // 0-100
    private boolean powered;           // Whether it received enough energy
    
    /**
     * Create a new residence with a random level.
     */
    public Residence() {
        this(GameConfig.randomIntInRange(GameConfig.MIN_RESIDENCE_LEVEL, GameConfig.MAX_RESIDENCE_LEVEL));
    }
    
    /**
     * Create a new residence with a specific level.
     */
    public Residence(int level) {
        this.id = nextId++;
        this.level = level;
        this.satisfaction = 50.0;
        this.powered = true;
        recalculateCharacteristics();
    }
    
    /**
     * Recalculate energy demand and purchasing power based on level.
     * Uses random values within ranges for variability.
     */
    private void recalculateCharacteristics() {
        this.energyDemand = GameConfig.getEnergyDemandForLevel(level);
        this.purchasingPower = GameConfig.getPurchasingPowerForLevel(level);
    }
    
    /**
     * Upgrade the residence to the next level (if possible).
     */
    public boolean upgrade() {
        if (level < GameConfig.MAX_RESIDENCE_LEVEL) {
            level++;
            recalculateCharacteristics();
            return true;
        }
        return false;
    }
    
    /**
     * Update satisfaction based on power supply.
     */
    public void updateSatisfaction(boolean receivedPower, double coverageRatio) {
        this.powered = receivedPower;
        
        if (receivedPower && coverageRatio >= 1.0) {
            // Fully powered - increase satisfaction
            satisfaction = Math.min(100, satisfaction + 3);
        } else if (receivedPower && coverageRatio >= 0.7) {
            // Mostly powered - slight increase
            satisfaction = Math.min(100, satisfaction + 1);
        } else if (receivedPower && coverageRatio >= 0.5) {
            // Partially powered - stable
            satisfaction = Math.max(0, satisfaction - 1);
        } else if (coverageRatio >= 0.3) {
            // Low coverage - decrease
            satisfaction = Math.max(0, satisfaction - 3);
        } else {
            // Very low coverage - significant decrease
            satisfaction = Math.max(0, satisfaction - 5);
        }
    }
    
    /**
     * Calculate the payment for received energy.
     */
    public double calculatePayment(double energyReceived) {
        return energyReceived * purchasingPower;
    }
    
    // Getters
    public int getId() { return id; }
    public int getLevel() { return level; }
    public double getEnergyDemand() { return energyDemand; }
    public double getPurchasingPower() { return purchasingPower; }
    public double getSatisfaction() { return satisfaction; }
    public boolean isPowered() { return powered; }
    
    /**
     * Get a display name for the residence level.
     */
    public String getLevelName() {
        return switch (level) {
            case 1 -> "Petit Appartement";
            case 2 -> "Appartement Moyen";
            case 3 -> "Grande Maison";
            case 4 -> "Villa";
            case 5 -> "Manoir";
            default -> "Résidence Niveau " + level;
        };
    }
    
    @Override
    public String toString() {
        return String.format("Résidence #%d [%s] - Demande: %.1f kWh/jour, Pouvoir d'achat: %.2f€/kWh, Satisfaction: %.0f%%",
                id, getLevelName(), energyDemand, purchasingPower, satisfaction);
    }
}
