package game.model;

import java.util.ArrayList;
import java.util.List;
import game.model.GameConfig.PlantType;

/**
 * Represents the city with all its residences and power plants.
 * Manages the overall state of the energy system.
 */
public class City {
    private final String name;
    private final List<Residence> residences;
    private final List<PowerPlant> powerPlants;
    private double pollution;
    
    /**
     * Create a new city with a name.
     */
    public City(String name) {
        this.name = name;
        this.residences = new ArrayList<>();
        this.powerPlants = new ArrayList<>();
        this.pollution = 0;
    }
    
    /**
     * Initialize the city with some starting residences.
     */
    public void initializeCity(int initialResidences) {
        for (int i = 0; i < initialResidences; i++) {
            // Distribute levels: more low-level residences than high-level
            int level;
            double roll = GameConfig.randomInRange(0, 1);
            if (roll < 0.4) level = 1;      // 40% level 1
            else if (roll < 0.7) level = 2;  // 30% level 2
            else if (roll < 0.85) level = 3; // 15% level 3
            else if (roll < 0.95) level = 4; // 10% level 4
            else level = 5;                   // 5% level 5
            
            residences.add(new Residence(level));
        }
    }
    
    /**
     * Add a new residence to the city.
     */
    public void addResidence(Residence residence) {
        residences.add(residence);
    }
    
    /**
     * Add a new power plant to the city.
     */
    public void addPowerPlant(PowerPlant plant) {
        powerPlants.add(plant);
    }
    
    /**
     * Remove a power plant from the city.
     */
    public boolean removePowerPlant(int plantId) {
        return powerPlants.removeIf(p -> p.getId() == plantId);
    }
    
    /**
     * Get total energy demand from all residences.
     */
    public double getTotalEnergyDemand() {
        return residences.stream()
                .mapToDouble(Residence::getEnergyDemand)
                .sum();
    }
    
    /**
     * Get total energy production from all operational power plants.
     */
    public double getTotalEnergyProduction() {
        return powerPlants.stream()
                .filter(PowerPlant::isOperational)
                .mapToDouble(PowerPlant::getActualProduction)
                .sum();
    }
    
    /**
     * Get total operating costs for all power plants.
     */
    public double getTotalOperatingCosts() {
        return powerPlants.stream()
                .mapToDouble(PowerPlant::getOperatingCost)
                .sum();
    }
    
    /**
     * Get total pollution from all power plants.
     */
    public double getTotalPollution() {
        return powerPlants.stream()
                .mapToDouble(PowerPlant::getPollution)
                .sum();
    }
    
    /**
     * Calculate average satisfaction of all residences.
     */
    public double getAverageHappiness() {
        if (residences.isEmpty()) return 50.0;
        return residences.stream()
                .mapToDouble(Residence::getSatisfaction)
                .average()
                .orElse(50.0);
    }
    
    /**
     * Distribute energy to residences and calculate revenue.
     * Returns the total revenue from energy sales.
     */
    public double distributeEnergy() {
        double totalProduction = getTotalEnergyProduction();
        double totalDemand = getTotalEnergyDemand();
        double revenue = 0;
        
        if (totalDemand == 0) {
            return 0;
        }
        
        double coverageRatio = totalProduction / totalDemand;
        
        // Distribute energy proportionally to each residence
        for (Residence residence : residences) {
            double demand = residence.getEnergyDemand();
            double energyReceived;
            
            if (coverageRatio >= 1.0) {
                // Enough energy for everyone
                energyReceived = demand;
                residence.updateSatisfaction(true, 1.0);
            } else {
                // Not enough energy - distribute proportionally
                energyReceived = demand * coverageRatio;
                residence.updateSatisfaction(coverageRatio >= 0.5, coverageRatio);
            }
            
            revenue += residence.calculatePayment(energyReceived);
        }
        
        // Update pollution
        pollution = Math.min(100, pollution + getTotalPollution() * 0.1);
        
        // Pollution affects happiness
        if (pollution > 50) {
            for (Residence residence : residences) {
                // Extra happiness penalty from pollution
                residence.updateSatisfaction(residence.isPowered(), 
                        residence.isPowered() ? (1.0 - (pollution - 50) / 100.0) : 0);
            }
        }
        
        return revenue;
    }
    
    /**
     * Apply weather effects to all power plants.
     */
    public void applyWeatherEffects() {
        for (PowerPlant plant : powerPlants) {
            plant.applyWeatherEffect();
        }
    }
    
    /**
     * Simulate city growth (new residences).
     */
    public void simulateGrowth() {
        int newResidences = GameConfig.randomIntInRange(GameConfig.CITY_GROWTH_MIN, GameConfig.CITY_GROWTH_MAX);
        for (int i = 0; i < newResidences; i++) {
            // New residences are typically lower level
            int level = GameConfig.randomIntInRange(1, 3);
            residences.add(new Residence(level));
        }
        
        // Occasionally upgrade existing residences
        for (Residence residence : residences) {
            if (GameConfig.randomChance(0.05)) { // 5% chance to upgrade
                residence.upgrade();
            }
        }
        
        // Natural pollution decay
        pollution = Math.max(0, pollution - 2);
    }
    
    /**
     * Get power plant by ID.
     */
    public PowerPlant getPowerPlantById(int id) {
        return powerPlants.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    // Getters
    public String getName() { return name; }
    public List<Residence> getResidences() { return new ArrayList<>(residences); }
    public List<PowerPlant> getPowerPlants() { return new ArrayList<>(powerPlants); }
    public int getResidenceCount() { return residences.size(); }
    public int getPowerPlantCount() { return powerPlants.size(); }
    public double getPollution() { return pollution; }
    
    /**
     * Get count of power plants by type.
     */
    public int getPowerPlantCountByType(PlantType type) {
        return (int) powerPlants.stream()
                .filter(p -> p.getType() == type)
                .count();
    }
    
    /**
     * Get a summary of the city status.
     */
    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Ville de %s ===\n", name));
        sb.append(String.format("Population: %d résidences\n", residences.size()));
        sb.append(String.format("Centrales électriques: %d\n", powerPlants.size()));
        sb.append(String.format("Demande totale: %.0f kWh/jour\n", getTotalEnergyDemand()));
        sb.append(String.format("Production totale: %.0f kWh/jour\n", getTotalEnergyProduction()));
        double coverage = getTotalEnergyDemand() > 0 ? 
                (getTotalEnergyProduction() / getTotalEnergyDemand() * 100) : 0;
        sb.append(String.format("Couverture énergétique: %.1f%%\n", coverage));
        sb.append(String.format("Bonheur moyen: %.1f%%\n", getAverageHappiness()));
        sb.append(String.format("Pollution: %.1f%%\n", pollution));
        sb.append(String.format("Coûts opérationnels: %.0f€/jour\n", getTotalOperatingCosts()));
        return sb.toString();
    }
}
