package model;

/**
 * Classe représentant l'état global du jeu.
 * Contient les ressources, le bonheur, le cycle actuel et la référence à la ville.
 */
public class GameState {
    // Constantes de jeu
    public static final int INITIAL_RESOURCES = 500; // Crédits de départ
    public static final double MIN_HAPPINESS_THRESHOLD = 0.3; // Seuil de bonheur minimum (30%)
    public static final double HAPPINESS_DECAY_RATE = 0.02; // Décroissance naturelle par cycle
    
    // État du jeu
    private int resources; // Crédits disponibles
    private double happiness; // Niveau de bonheur global (0.0 à 1.0)
    private int currentCycle; // Numéro du cycle actuel
    private City city; // Référence à la ville
    private boolean gameOver; // Indique si le jeu est terminé
    private String gameOverReason; // Raison de la fin du jeu
    
    /**
     * Constructeur de l'état du jeu
     * @param cityName Nom de la colonie
     */
    public GameState(String cityName) {
        this.resources = INITIAL_RESOURCES;
        this.happiness = 0.8; // 80% de bonheur initial
        this.currentCycle = 0;
        this.city = new City(cityName);
        this.gameOver = false;
        this.gameOverReason = "";
    }
    
    /**
     * Ajoute des ressources
     * @param amount Montant à ajouter
     */
    public void addResources(int amount) {
        this.resources += amount;
    }
    
    /**
     * Déduit des ressources
     * @param amount Montant à déduire
     * @return true si les ressources étaient suffisantes
     */
    public boolean spendResources(int amount) {
        if (resources >= amount) {
            resources -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Définit le niveau de bonheur
     * @param happiness Nouveau niveau (0.0 à 1.0)
     */
    public void setHappiness(double happiness) {
        this.happiness = Math.max(0.0, Math.min(1.0, happiness));
        
        // Vérifier si le bonheur est trop bas
        if (this.happiness < MIN_HAPPINESS_THRESHOLD) {
            gameOver = true;
            gameOverReason = "Le bonheur de la population est tombé trop bas. Le conseil colonial vous a démis de vos fonctions.";
        }
    }
    
    /**
     * Ajuste le bonheur (ajoute ou retire)
     * @param delta Changement du bonheur
     */
    public void adjustHappiness(double delta) {
        setHappiness(happiness + delta);
    }
    
    /**
     * Passe au cycle suivant
     */
    public void nextCycle() {
        currentCycle++;
    }
    
    /**
     * Vérifie si le joueur a assez de ressources
     * @param amount Montant requis
     * @return true si suffisant
     */
    public boolean hasEnoughResources(int amount) {
        return resources >= amount;
    }
    
    /**
     * Termine le jeu avec une raison spécifique
     * @param reason Raison de la fin du jeu
     */
    public void endGame(String reason) {
        this.gameOver = true;
        this.gameOverReason = reason;
    }
    
    /**
     * Obtient un message de statut du bonheur
     * @return Message descriptif
     */
    public String getHappinessStatus() {
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
     * Calcule l'équilibre énergétique (production - demande)
     * @return Différence en kW
     */
    public double getEnergyBalance() {
        return city.getTotalEnergyProduction() - city.getTotalEnergyDemand();
    }
    
    /**
     * Obtient le ratio production/demande
     * @return Ratio (1.0 = parfait équilibre)
     */
    public double getEnergyRatio() {
        double demand = city.getTotalEnergyDemand();
        if (demand == 0) {
            return 1.0;
        }
        return city.getTotalEnergyProduction() / demand;
    }
    
    // Getters
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
