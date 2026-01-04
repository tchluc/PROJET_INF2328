package model;

import java.util.Random;

/**
 * Cette classe represente une residence (un batiment ou vivent des habitants).
 * 
 * Chaque residence a:
 * - Un identifiant unique (id)
 * - Un niveau (1, 2 ou 3) - plus le niveau est haut, plus il y a d'habitants
 * - Un besoin en energie (en kW) - combien d'electricite la residence demande
 * - Un pouvoir d'achat - combien les habitants payent pour l'electricite
 * - Une satisfaction - si les habitants sont contents (de 0.0 a 1.0)
 */
public class Residence {
    
    // On utilise Random pour generer des nombres aleatoires
    private Random random;
    
    // Les attributs (caracteristiques) de la residence
    private int id;                // Numero unique de la residence
    private int level;             // Niveau: 1, 2 ou 3
    private int inhabitants;       // Nombre d'habitants
    private double energyNeed;     // Besoin en electricite (kW)
    private double purchasingPower; // Combien ils payent par kW
    private double satisfaction;   // Niveau de satisfaction (0.0 a 1.0)
    
    /**
     * Constructeur: cree une nouvelle residence
     * 
     * @param id L'identifiant unique de la residence
     * @param level Le niveau (doit etre 1, 2 ou 3)
     */
    public Residence(int id, int level) {
        // On cree notre generateur de nombres aleatoires
        this.random = new Random();
        
        // On verifie que le niveau est valide
        if (level < 1) {
            level = 1;  // Si trop bas, on met 1
        }
        if (level > 3) {
            level = 3;  // Si trop haut, on met 3
        }
        
        // On enregistre l'id et le niveau
        this.id = id;
        this.level = level;
        
        // On calcule les valeurs en fonction du niveau
        // On utilise des conditions if/else simples
        
        // Calcul du besoin en energie
        if (level == 1) {
            // Niveau 1: entre 50 et 100 kW
            int minimum = 50;
            int maximum = 100;
            this.energyNeed = minimum + random.nextDouble() * (maximum - minimum);
        } else if (level == 2) {
            // Niveau 2: entre 100 et 200 kW
            int minimum = 100;
            int maximum = 200;
            this.energyNeed = minimum + random.nextDouble() * (maximum - minimum);
        } else {
            // Niveau 3: entre 200 et 350 kW
            int minimum = 200;
            int maximum = 350;
            this.energyNeed = minimum + random.nextDouble() * (maximum - minimum);
        }
        
        // Calcul du pouvoir d'achat (combien ils payent par kW)
        if (level == 1) {
            // Niveau 1: entre 0.8 et 1.2 credits par kW
            double min = 0.8;
            double max = 1.2;
            this.purchasingPower = min + random.nextDouble() * (max - min);
        } else if (level == 2) {
            // Niveau 2: entre 1.5 et 2.0 credits par kW
            double min = 1.5;
            double max = 2.0;
            this.purchasingPower = min + random.nextDouble() * (max - min);
        } else {
            // Niveau 3: entre 2.5 et 3.5 credits par kW
            double min = 2.5;
            double max = 3.5;
            this.purchasingPower = min + random.nextDouble() * (max - min);
        }
        
        // Calcul du nombre d'habitants
        if (level == 1) {
            // Niveau 1: entre 10 et 30 habitants
            int min = 10;
            int max = 30;
            this.inhabitants = min + random.nextInt(max - min + 1);
        } else if (level == 2) {
            // Niveau 2: entre 30 et 60 habitants
            int min = 30;
            int max = 60;
            this.inhabitants = min + random.nextInt(max - min + 1);
        } else {
            // Niveau 3: entre 60 et 100 habitants
            int min = 60;
            int max = 100;
            this.inhabitants = min + random.nextInt(max - min + 1);
        }
        
        // Satisfaction initiale: entre 80% et 100%
        this.satisfaction = 0.8 + random.nextDouble() * 0.2;
    }
    
    /**
     * Met a jour la satisfaction en fonction de l'energie recue.
     * Si on recoit assez d'energie, les habitants sont contents.
     * Si on n'en recoit pas assez, ils sont mecontents.
     * 
     * @param energyReceived L'energie effectivement recue (en kW)
     */
    public void updateSatisfaction(double energyReceived) {
        // On calcule le ratio: energie recue / energie demandee
        double ratio = energyReceived / energyNeed;
        
        // On ajuste la satisfaction selon le ratio
        if (ratio >= 1.0) {
            // On a recu assez d'energie: satisfaction augmente de 5%
            satisfaction = satisfaction + 0.05;
            // Mais on ne depasse pas 1.0 (100%)
            if (satisfaction > 1.0) {
                satisfaction = 1.0;
            }
        } else if (ratio >= 0.8) {
            // On a recu presque assez: satisfaction baisse un peu
            satisfaction = satisfaction - 0.02;
            // On ne descend pas en dessous de 0
            if (satisfaction < 0.0) {
                satisfaction = 0.0;
            }
        } else if (ratio >= 0.5) {
            // On n'a pas recu assez: satisfaction baisse de 10%
            satisfaction = satisfaction - 0.1;
            if (satisfaction < 0.0) {
                satisfaction = 0.0;
            }
        } else {
            // On a recu tres peu: satisfaction baisse beaucoup (20%)
            satisfaction = satisfaction - 0.2;
            if (satisfaction < 0.0) {
                satisfaction = 0.0;
            }
        }
    }
    
    /**
     * Calcule combien d'argent cette residence va payer.
     * Le revenu depend de l'energie fournie et du pouvoir d'achat.
     * 
     * @param energyProvided L'energie qu'on leur donne
     * @return Le montant en credits
     */
    public double calculateRevenue(double energyProvided) {
        // On ne peut pas facturer plus que ce qu'ils demandent
        double energieAFacturer;
        if (energyProvided > energyNeed) {
            energieAFacturer = energyNeed;
        } else {
            energieAFacturer = energyProvided;
        }
        
        // Le revenu = energie * prix par kW
        double revenu = energieAFacturer * purchasingPower;
        return revenu;
    }
    
    /**
     * Simule une croissance de la population.
     * Si les habitants sont contents, la population peut augmenter.
     */
    public void simulateGrowth() {
        // On tire un nombre aleatoire entre 0 et 1
        double hasard = random.nextDouble();
        
        // Chance de croissance = satisfaction * 10%
        // Ex: si satisfaction = 0.8, alors chance = 8%
        double chanceDeCroissance = satisfaction * 0.1;
        
        // Si le hasard est inferieur a la chance, on grandit
        if (hasard < chanceDeCroissance) {
            // Facteur de croissance entre 2% et 5%
            double facteur = 1.02 + random.nextDouble() * 0.03;
            
            // On augmente le besoin en energie
            energyNeed = energyNeed * facteur;
            
            // On augmente le nombre d'habitants
            inhabitants = (int) (inhabitants * facteur);
        }
    }
    
    // ========================================
    // GETTERS: methodes pour lire les valeurs
    // ========================================
    
    /**
     * Retourne l'identifiant de la residence
     */
    public int getId() {
        return id;
    }
    
    /**
     * Retourne le niveau de la residence (1, 2 ou 3)
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Retourne le nombre d'habitants
     */
    public int getInhabitants() {
        return inhabitants;
    }
    
    /**
     * Retourne le besoin en energie (en kW)
     */
    public double getEnergyNeed() {
        return energyNeed;
    }
    
    /**
     * Retourne le pouvoir d'achat (credits par kW)
     */
    public double getPurchasingPower() {
        return purchasingPower;
    }
    
    /**
     * Retourne la satisfaction (entre 0.0 et 1.0)
     */
    public double getSatisfaction() {
        return satisfaction;
    }
    
    /**
     * Retourne une description textuelle de la residence.
     * Cette methode est appelee automatiquement quand on fait System.out.println(residence)
     */
    @Override
    public String toString() {
        // On cree une chaine de caracteres avec les infos importantes
        String description = "Résidence #" + id 
            + " (Niv." + level + ")"
            + " - " + inhabitants + " hab."
            + " - Besoin: " + Math.round(energyNeed) + " kW"
            + " - Satisfaction: " + Math.round(satisfaction * 100) + "%";
        return description;
    }
}
