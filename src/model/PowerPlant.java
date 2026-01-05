package model;

/**
 * Cette classe represente une centrale electrique.
 * 
 * Une centrale produit de l'electricite pour alimenter les residences.
 * Il existe plusieurs types de centrales (solaire, eolienne, charbon, etc.)
 * et chaque centrale peut etre amelioree jusqu'au niveau 3.
 */
public class PowerPlant {
    
    // Compteur pour generer des identifiants uniques
    // C'est une variable "static" donc partagee par toutes les centrales
    private static int prochainId = 1;
    
    // Les attributs de la centrale
    private int id;                // Identifiant unique
    private PowerPlantType type;   // Type de centrale (solaire, eolienne, etc.)
    private int level;             // Niveau actuel (1, 2 ou 3)
    private double production;     // Production en kW
    private int buildCost;         // Cout de construction
    private int upgradeCost;       // Cout pour ameliorer
    private int maintenanceCost;   // Cout d'entretien par cycle
    
    /**
     * Constructeur principal: cree une centrale d'un type et niveau donnes
     * 
     * @param type Le type de centrale (SOLAR, WIND, COAL, NUCLEAR, FUSION)
     * @param level Le niveau initial (1, 2 ou 3)
     */
    public PowerPlant(PowerPlantType type, int level) {
        // On verifie que le niveau est valide
        if (level < 1) {
            level = 1;
        }
        if (level > 3) {
            level = 3;
        }
        
        // On attribue un identifiant unique
        this.id = prochainId;
        prochainId = prochainId + 1;  // On incremente pour la prochaine centrale
        
        // On enregistre le type et le niveau
        this.type = type;
        this.level = level;
        
        // On calcule les statistiques selon le type et le niveau
        calculerStatistiques();
    }
    
    /**
     * Constructeur simplifie: cree une centrale de niveau 1
     * 
     * @param type Le type de centrale
     */
    public PowerPlant(PowerPlantType type) {
        // On appelle l'autre constructeur avec niveau = 1
        this(type, 1);
    }
    
    /**
     * Calcule les statistiques (production, couts) selon le type et niveau.
     * Cette methode est appelee apres la creation ou une amelioration.
     */
    private void calculerStatistiques() {
        // On utilise des if/else pour determiner les valeurs
        // selon le type de centrale
        
        // === CENTRALE SOLAIRE ===
        if (type == PowerPlantType.SOLAR) {
            if (level == 1) {
                production = 80;
                buildCost = 100;
                upgradeCost = 80;
                maintenanceCost = 5;
            } else if (level == 2) {
                production = 150;
                buildCost = 180;
                upgradeCost = 120;
                maintenanceCost = 8;
            } else {  // level == 3
                production = 250;
                buildCost = 300;
                upgradeCost = 0;  // 0 = niveau maximum atteint
                maintenanceCost = 12;
            }
        }
        
        // === CENTRALE EOLIENNE ===
        else if (type == PowerPlantType.WIND) {
            if (level == 1) {
                production = 100;
                buildCost = 120;
                upgradeCost = 100;
                maintenanceCost = 7;
            } else if (level == 2) {
                production = 180;
                buildCost = 200;
                upgradeCost = 150;
                maintenanceCost = 10;
            } else {
                production = 300;
                buildCost = 350;
                upgradeCost = 0;
                maintenanceCost = 15;
            }
        }
        
        // === CENTRALE AU CHARBON ===
        else if (type == PowerPlantType.COAL) {
            if (level == 1) {
                production = 150;
                buildCost = 150;
                upgradeCost = 120;
                maintenanceCost = 15;
            } else if (level == 2) {
                production = 280;
                buildCost = 250;
                upgradeCost = 180;
                maintenanceCost = 25;
            } else {
                production = 450;
                buildCost = 400;
                upgradeCost = 0;
                maintenanceCost = 40;
            }
        }
        
        // === CENTRALE NUCLEAIRE ===
        else if (type == PowerPlantType.NUCLEAR) {
            if (level == 1) {
                production = 200;
                buildCost = 300;
                upgradeCost = 250;
                maintenanceCost = 20;
            } else if (level == 2) {
                production = 400;
                buildCost = 500;
                upgradeCost = 400;
                maintenanceCost = 35;
            } else {
                production = 700;
                buildCost = 800;
                upgradeCost = 0;
                maintenanceCost = 60;
            }
        }
        
        // === CENTRALE A FUSION ===
        else {  // type == PowerPlantType.FUSION
            if (level == 1) {
                production = 300;
                buildCost = 500;
                upgradeCost = 400;
                maintenanceCost = 30;
            } else if (level == 2) {
                production = 600;
                buildCost = 900;
                upgradeCost = 700;
                maintenanceCost = 50;
            } else {
                production = 1000;
                buildCost = 1500;
                upgradeCost = 0;
                maintenanceCost = 80;
            }
        }
    }
    
    /**
     * Ameliore la centrale au niveau suivant.
     * 
     * @return true si l'amelioration a reussi, false si deja au niveau max
     */
    public boolean upgrade() {
        // On verifie si on peut ameliorer
        if (level >= 3) {
            // On est deja au niveau maximum
            return false;
        }
        
        // On passe au niveau suivant
        level = level + 1;
        
        // On recalcule les statistiques
        calculerStatistiques();
        
        return true;
    }
    
    /**
     * Verifie si la centrale peut etre amelioree.
     * 
     * @return true si le niveau est inferieur a 3
     */
    public boolean canUpgrade() {
        if (level < 3) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Methode statique pour obtenir le cout de construction
     * d'une centrale de niveau 1 pour un type donne.
     * 
     * @param type Le type de centrale
     * @return Le cout en credits
     */
    public static int getBuildCost(PowerPlantType type) {
        // On retourne le cout selon le type
        if (type == PowerPlantType.SOLAR) {
            return 100;
        } else if (type == PowerPlantType.WIND) {
            return 120;
        } else if (type == PowerPlantType.COAL) {
            return 150;
        } else if (type == PowerPlantType.NUCLEAR) {
            return 300;
        } else {  // FUSION
            return 500;
        }
    }
    
    /**
     * Methode statique pour obtenir la production de base
     * d'une centrale de niveau 1 pour un type donne.
     * 
     * @param type Le type de centrale
     * @return La production en kW
     */
    public static double getBaseProduction(PowerPlantType type) {
        if (type == PowerPlantType.SOLAR) {
            return 80;
        } else if (type == PowerPlantType.WIND) {
            return 100;
        } else if (type == PowerPlantType.COAL) {
            return 150;
        } else if (type == PowerPlantType.NUCLEAR) {
            return 200;
        } else {  // FUSION
            return 300;
        }
    }
    
    // ========================================
    // GETTERS: methodes pour lire les valeurs
    // ========================================
    
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
    
    /**
     * Retourne une description textuelle de la centrale.
     */
    @Override
    public String toString() {
        String description = "Centrale " + type.getName() + " #" + id 
            + " (Niv." + level + ")"
            + " - Production: " + Math.round(production) + " kW"
            + " - Entretien: " + maintenanceCost + " cr/cycle";
        return description;
    }
}
