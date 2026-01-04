package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la colonie spatiale avec ses résidences et centrales.
 * Gère l'ensemble des bâtiments et calcule les statistiques globales.
 */
public class City {
    private String name;
    private List<Residence> residences;
    private List<PowerPlant> powerPlants;
    
    /**
     * Constructeur de la ville
     * @param name Nom de la colonie
     */
    public City(String name) {
        this.name = name;
        this.residences = new ArrayList<>();
        this.powerPlants = new ArrayList<>();
    }
    
    /**
     * Ajoute une résidence à la colonie
     * @param residence Résidence à ajouter
     */
    public void addResidence(Residence residence) {
        residences.add(residence);
    }
    
    /**
     * Ajoute une centrale électrique
     * @param powerPlant Centrale à ajouter
     */
    public void addPowerPlant(PowerPlant powerPlant) {
        powerPlants.add(powerPlant);
    }
    
    /**
     * Retire une centrale électrique
     * @param powerPlant Centrale à retirer
     */
    public void removePowerPlant(PowerPlant powerPlant) {
        powerPlants.remove(powerPlant);
    }
    
    /**
     * Calcule la demande énergétique totale de toutes les résidences
     * @return Demande totale en kW
     */
    public double getTotalEnergyDemand() {
        return residences.stream()
                .mapToDouble(Residence::getEnergyNeed)
                .sum();
    }
    
    /**
     * Calcule la production énergétique totale de toutes les centrales
     * @return Production totale en kW
     */
    public double getTotalEnergyProduction() {
        return powerPlants.stream()
                .mapToDouble(PowerPlant::getProduction)
                .sum();
    }
    
    /**
     * Calcule le coût total d'entretien de toutes les centrales
     * @return Coût total en crédits
     */
    public int getTotalMaintenanceCost() {
        return powerPlants.stream()
                .mapToInt(PowerPlant::getMaintenanceCost)
                .sum();
    }
    
    /**
     * Calcule la population totale de la colonie
     * @return Nombre d'habitants
     */
    public int getTotalPopulation() {
        return residences.stream()
                .mapToInt(Residence::getInhabitants)
                .sum();
    }
    
    /**
     * Calcule le niveau de satisfaction moyen
     * @return Satisfaction moyenne (0.0 à 1.0)
     */
    public double getAverageSatisfaction() {
        if (residences.isEmpty()) {
            return 1.0;
        }
        
        return residences.stream()
                .mapToDouble(Residence::getSatisfaction)
                .average()
                .orElse(1.0);
    }
    
    /**
     * Distribue l'énergie disponible aux résidences et met à jour leur satisfaction
     * @return Revenu total généré
     */
    public double distributeEnergy() {
        double totalProduction = getTotalEnergyProduction();
        double totalDemand = getTotalEnergyDemand();
        double totalRevenue = 0.0;
        
        if (totalDemand == 0) {
            return 0.0;
        }
        
        // Si production suffisante, tout le monde reçoit ce qu'il demande
        if (totalProduction >= totalDemand) {
            for (Residence residence : residences) {
                double energyProvided = residence.getEnergyNeed();
                residence.updateSatisfaction(energyProvided);
                totalRevenue += residence.calculateRevenue(energyProvided);
            }
        } else {
            // Sinon, on distribue proportionnellement
            double ratio = totalProduction / totalDemand;
            
            for (Residence residence : residences) {
                double energyProvided = residence.getEnergyNeed() * ratio;
                residence.updateSatisfaction(energyProvided);
                totalRevenue += residence.calculateRevenue(energyProvided);
            }
        }
        
        return totalRevenue;
    }
    
    /**
     * Simule la croissance de la population dans les résidences
     */
    public void simulateGrowth() {
        for (Residence residence : residences) {
            residence.simulateGrowth();
        }
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public List<Residence> getResidences() {
        return new ArrayList<>(residences); // Copie défensive
    }
    
    public List<PowerPlant> getPowerPlants() {
        return new ArrayList<>(powerPlants); // Copie défensive
    }
    
    public int getResidenceCount() {
        return residences.size();
    }
    
    public int getPowerPlantCount() {
        return powerPlants.size();
    }
}
