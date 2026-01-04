package model;

import java.util.Random;

/**
 * Classe représentant une résidence dans la colonie spatiale.
 * Chaque résidence a un niveau qui détermine ses besoins en énergie et son pouvoir d'achat.
 */
public class Residence {
    private static final Random random = new Random();
    
    // Attributs de la résidence
    private int id;
    private int level; // Niveau de la résidence (1 à 3)
    private int inhabitants; // Nombre d'habitants
    private double energyNeed; // Besoin énergétique par cycle (kW)
    private double purchasingPower; // Pouvoir d'achat électrique (crédits par kW)
    private double satisfaction; // Niveau de satisfaction (0.0 à 1.0)
    
    // Plages de valeurs selon le niveau
    private static final int[][] ENERGY_RANGES = {
        {50, 100},   // Niveau 1: 50-100 kW
        {100, 200},  // Niveau 2: 100-200 kW
        {200, 350}   // Niveau 3: 200-350 kW
    };
    
    private static final double[][] PURCHASE_POWER_RANGES = {
        {0.8, 1.2},   // Niveau 1: 0.8-1.2 crédits/kW
        {1.5, 2.0},   // Niveau 2: 1.5-2.0 crédits/kW
        {2.5, 3.5}    // Niveau 3: 2.5-3.5 crédits/kW
    };
    
    private static final int[][] INHABITANTS_RANGES = {
        {10, 30},    // Niveau 1
        {30, 60},    // Niveau 2
        {60, 100}    // Niveau 3
    };
    
    /**
     * Constructeur d'une résidence
     * @param id Identifiant unique
     * @param level Niveau de la résidence (1-3)
     */
    public Residence(int id, int level) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Le niveau doit être entre 1 et 3");
        }
        
        this.id = id;
        this.level = level;
        
        // Initialisation avec des valeurs aléatoires dans les plages
        int[] energyRange = ENERGY_RANGES[level - 1];
        this.energyNeed = energyRange[0] + random.nextDouble() * (energyRange[1] - energyRange[0]);
        
        double[] powerRange = PURCHASE_POWER_RANGES[level - 1];
        this.purchasingPower = powerRange[0] + random.nextDouble() * (powerRange[1] - powerRange[0]);
        
        int[] inhabitantsRange = INHABITANTS_RANGES[level - 1];
        this.inhabitants = inhabitantsRange[0] + random.nextInt(inhabitantsRange[1] - inhabitantsRange[0] + 1);
        
        // Satisfaction initiale élevée
        this.satisfaction = 0.8 + random.nextDouble() * 0.2; // 0.8 à 1.0
    }
    
    /**
     * Met à jour la satisfaction en fonction de l'énergie reçue
     * @param energyReceived Énergie effectivement reçue
     */
    public void updateSatisfaction(double energyReceived) {
        double ratio = energyReceived / energyNeed;
        
        if (ratio >= 1.0) {
            // Énergie suffisante: satisfaction augmente légèrement
            satisfaction = Math.min(1.0, satisfaction + 0.05);
        } else if (ratio >= 0.8) {
            // Légèrement insuffisant: satisfaction stable
            satisfaction = Math.max(0.0, satisfaction - 0.02);
        } else if (ratio >= 0.5) {
            // Insuffisant: satisfaction diminue
            satisfaction = Math.max(0.0, satisfaction - 0.1);
        } else {
            // Très insuffisant: satisfaction diminue fortement
            satisfaction = Math.max(0.0, satisfaction - 0.2);
        }
    }
    
    /**
     * Calcule le revenu généré par cette résidence
     * @param energyProvided Énergie fournie à la résidence
     * @return Montant en crédits
     */
    public double calculateRevenue(double energyProvided) {
        // Le revenu est basé sur l'énergie effectivement fournie
        double actualEnergy = Math.min(energyProvided, energyNeed);
        return actualEnergy * purchasingPower;
    }
    
    /**
     * Simule une croissance de la population (peut augmenter les besoins)
     */
    public void simulateGrowth() {
        // Probabilité de croissance dépend de la satisfaction
        if (random.nextDouble() < satisfaction * 0.1) {
            // Augmentation des besoins de 2 à 5%
            double growthFactor = 1.02 + random.nextDouble() * 0.03;
            energyNeed *= growthFactor;
            inhabitants = (int) (inhabitants * growthFactor);
        }
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getInhabitants() {
        return inhabitants;
    }
    
    public double getEnergyNeed() {
        return energyNeed;
    }
    
    public double getPurchasingPower() {
        return purchasingPower;
    }
    
    public double getSatisfaction() {
        return satisfaction;
    }
    
    @Override
    public String toString() {
        return String.format("Résidence #%d (Niv.%d) - %d hab. - Besoin: %.1f kW - Satisfaction: %.0f%%",
                id, level, inhabitants, energyNeed, satisfaction * 100);
    }
}
