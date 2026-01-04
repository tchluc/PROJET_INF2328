package model;

/**
 * Classe représentant une centrale électrique dans la colonie.
 * Chaque centrale a un type, un niveau, et produit de l'énergie.
 */
public class PowerPlant {
    private static int nextId = 1;
    
    private int id;
    private PowerPlantType type;
    private int level; // Niveau de 1 à 3
    private double production; // Production en kW
    private int buildCost; // Coût de construction
    private int upgradeCost; // Coût d'amélioration au niveau suivant
    private int maintenanceCost; // Coût d'entretien par cycle
    
    // Matrices de statistiques par type et niveau
    // Format: [type][level-1] = valeur
    private static final double[][] BASE_PRODUCTION = {
        {80, 150, 250},    // Solaire
        {100, 180, 300},   // Éolienne
        {150, 280, 450},   // Charbon
        {200, 400, 700},   // Nucléaire
        {300, 600, 1000}   // Fusion
    };
    
    private static final int[][] BUILD_COSTS = {
        {100, 180, 300},   // Solaire
        {120, 200, 350},   // Éolienne
        {150, 250, 400},   // Charbon
        {300, 500, 800},   // Nucléaire
        {500, 900, 1500}   // Fusion
    };
    
    private static final int[][] UPGRADE_COSTS = {
        {80, 120, 0},      // Solaire (0 = max level)
        {100, 150, 0},     // Éolienne
        {120, 180, 0},     // Charbon
        {250, 400, 0},     // Nucléaire
        {400, 700, 0}      // Fusion
    };
    
    private static final int[][] MAINTENANCE_COSTS = {
        {5, 8, 12},        // Solaire (faible entretien)
        {7, 10, 15},       // Éolienne
        {15, 25, 40},      // Charbon (élevé)
        {20, 35, 60},      // Nucléaire (très élevé)
        {30, 50, 80}       // Fusion (extrême)
    };
    
    /**
     * Constructeur d'une centrale électrique
     * @param type Type de centrale
     * @param level Niveau initial (1-3)
     */
    public PowerPlant(PowerPlantType type, int level) {
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Le niveau doit être entre 1 et 3");
        }
        
        this.id = nextId++;
        this.type = type;
        this.level = level;
        
        // Initialisation des statistiques
        updateStats();
    }
    
    /**
     * Constructeur pour une nouvelle centrale (niveau 1)
     * @param type Type de centrale
     */
    public PowerPlant(PowerPlantType type) {
        this(type, 1);
    }
    
    /**
     * Met à jour les statistiques selon le type et le niveau
     */
    private void updateStats() {
        int typeIndex = type.ordinal();
        int levelIndex = level - 1;
        
        this.production = BASE_PRODUCTION[typeIndex][levelIndex];
        this.buildCost = BUILD_COSTS[typeIndex][levelIndex];
        this.upgradeCost = UPGRADE_COSTS[typeIndex][levelIndex];
        this.maintenanceCost = MAINTENANCE_COSTS[typeIndex][levelIndex];
    }
    
    /**
     * Améliore la centrale au niveau suivant
     * @return true si l'amélioration est possible, false si déjà au max
     */
    public boolean upgrade() {
        if (level >= 3) {
            return false; // Déjà au niveau maximum
        }
        
        level++;
        updateStats();
        return true;
    }
    
    /**
     * Vérifie si la centrale peut être améliorée
     * @return true si améliorable
     */
    public boolean canUpgrade() {
        return level < 3;
    }
    
    /**
     * Obtient le coût de construction initial pour ce type au niveau 1
     * @param type Type de centrale
     * @return Coût en crédits
     */
    public static int getBuildCost(PowerPlantType type) {
        return BUILD_COSTS[type.ordinal()][0];
    }
    
    /**
     * Obtient la production d'une centrale de ce type au niveau 1
     * @param type Type de centrale
     * @return Production en kW
     */
    public static double getBaseProduction(PowerPlantType type) {
        return BASE_PRODUCTION[type.ordinal()][0];
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public PowerPlantType getType() {
        return type;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getProduction() {
        return production;
    }
    
    public int getBuildCost() {
        return buildCost;
    }
    
    public int getUpgradeCost() {
        return upgradeCost;
    }
    
    public int getMaintenanceCost() {
        return maintenanceCost;
    }
    
    @Override
    public String toString() {
        return String.format("Centrale %s #%d (Niv.%d) - Production: %.0f kW - Entretien: %d cr/cycle",
                type.getName(), id, level, production, maintenanceCost);
    }
}
