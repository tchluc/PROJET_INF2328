package model;

/**
 * Cette classe represente l'etat global du jeu.
 * 
 * Elle contient:
 * - Les ressources (credits) du joueur
 * - Le niveau de bonheur de la population
 * - Le numero du cycle actuel
 * - La reference vers la colonie (ville)
 * - L'etat game over (fin de partie)
 */
public class GameState {
    
    // ========================================
    // CONSTANTES DU JEU
    // ========================================
    
    // Nombre de credits au debut du jeu
    public static final int INITIAL_RESOURCES = 500;
    
    // Seuil minimum de bonheur (30%)
    // Si le bonheur descend en dessous, c'est game over
    public static final double MIN_HAPPINESS_THRESHOLD = 0.3;
    
    // De combien le bonheur diminue naturellement par cycle
    public static final double HAPPINESS_DECAY_RATE = 0.02;
    
    // ========================================
    // ATTRIBUTS (ETAT DU JEU)
    // ========================================
    
    private int resources;          // Credits disponibles
    private double happiness;       // Bonheur (0.0 a 1.0)
    private int currentCycle;       // Numero du cycle actuel
    private City city;              // La colonie
    private boolean gameOver;       // Est-ce que le jeu est fini?
    private String gameOverReason;  // Pourquoi le jeu est fini
    
    /**
     * Constructeur: cree un nouvel etat de jeu
     * 
     * @param cityName Le nom de la colonie
     */
    public GameState(String cityName) {
        // On initialise avec les valeurs de depart
        this.resources = INITIAL_RESOURCES;  // 500 credits
        this.happiness = 0.8;                // 80% de bonheur
        this.currentCycle = 0;               // On commence au cycle 0
        this.city = new City(cityName);      // On cree la colonie
        this.gameOver = false;               // Le jeu n'est pas fini
        this.gameOverReason = "";            // Pas de raison pour l'instant
    }
    
    /**
     * Ajoute des credits aux ressources du joueur
     * 
     * @param amount Le montant a ajouter (peut etre negatif)
     */
    public void addResources(int amount) {
        this.resources = this.resources + amount;
    }
    
    /**
     * Depense des ressources (retire des credits)
     * 
     * @param amount Le montant a depenser
     * @return true si on avait assez de credits, false sinon
     */
    public boolean spendResources(int amount) {
        // On verifie si on a assez
        if (resources >= amount) {
            // On depense
            resources = resources - amount;
            return true;
        } else {
            // Pas assez de credits
            return false;
        }
    }
    
    /**
     * Definit le niveau de bonheur.
     * Le bonheur est toujours entre 0 et 1.
     * Si le bonheur tombe trop bas, c'est game over!
     * 
     * @param happiness Le nouveau niveau de bonheur
     */
    public void setHappiness(double happiness) {
        // On s'assure que c'est entre 0 et 1
        if (happiness < 0.0) {
            this.happiness = 0.0;
        } else if (happiness > 1.0) {
            this.happiness = 1.0;
        } else {
            this.happiness = happiness;
        }
        
        // On verifie si le bonheur est trop bas
        if (this.happiness < MIN_HAPPINESS_THRESHOLD) {
            // Game Over!
            gameOver = true;
            gameOverReason = "Le bonheur de la population est tombé trop bas. Le conseil colonial vous a démis de vos fonctions.";
        }
    }
    
    /**
     * Ajuste le bonheur (ajoute ou retire une valeur)
     * 
     * @param delta Le changement (+0.05 pour ajouter, -0.05 pour retirer)
     */
    public void adjustHappiness(double delta) {
        // On utilise setHappiness pour gerer les limites
        setHappiness(happiness + delta);
    }
    
    /**
     * Passe au cycle suivant
     */
    public void nextCycle() {
        currentCycle = currentCycle + 1;
    }
    
    /**
     * Verifie si le joueur a assez de credits
     * 
     * @param amount Le montant requis
     * @return true si on a assez
     */
    public boolean hasEnoughResources(int amount) {
        if (resources >= amount) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Termine le jeu avec une raison specifique
     * 
     * @param reason La raison de la fin du jeu
     */
    public void endGame(String reason) {
        this.gameOver = true;
        this.gameOverReason = reason;
    }
    
    /**
     * Retourne un message decrivant le niveau de bonheur
     * 
     * @return Un mot descriptif
     */
    public String getHappinessStatus() {
        // On utilise des if/else pour determiner le message
        if (happiness >= 0.8) {
            return "Excellente";
        } else if (happiness >= 0.6) {
            return "Bonne";
        } else if (happiness >= 0.4) {
            return "Moyenne";
        } else if (happiness >= 0.3) {
            return "Faible";
        } else {
            return "Critique";
        }
    }
    
    /**
     * Calcule l'equilibre energetique (production - demande)
     * 
     * @return La difference en kW (positif = excedent, negatif = deficit)
     */
    public double getEnergyBalance() {
        double production = city.getTotalEnergyProduction();
        double demande = city.getTotalEnergyDemand();
        return production - demande;
    }
    
    /**
     * Calcule le ratio production/demande
     * 
     * @return Le ratio (1.0 = equilibre parfait, < 1.0 = deficit)
     */
    public double getEnergyRatio() {
        double demande = city.getTotalEnergyDemand();
        
        // On evite la division par zero
        if (demande == 0) {
            return 1.0;
        }
        
        double production = city.getTotalEnergyProduction();
        return production / demande;
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    public int getResources() {
        return resources;
    }
    
    public double getHappiness() {
        return happiness;
    }
    
    public int getCurrentCycle() {
        return currentCycle;
    }
    
    public City getCity() {
        return city;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public String getGameOverReason() {
        return gameOverReason;
    }
}
