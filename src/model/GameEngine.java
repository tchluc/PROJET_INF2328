package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Moteur du jeu - Gere la logique principale et la simulation.
 * 
 * Cette classe est le "cerveau" du jeu. Elle gere:
 * - L'initialisation du jeu
 * - La construction et amelioration des centrales
 * - Le deroulement des cycles
 * - Les evenements aleatoires
 * - Le journal des evenements (log)
 */
public class GameEngine {
    
    // Generateur de nombres aleatoires
    private Random random;
    
    // L'etat du jeu (ressources, bonheur, ville, etc.)
    private GameState gameState;
    
    // Liste des messages du journal
    private ArrayList<String> eventLog;
    
    // ========================================
    // EVENEMENTS ALEATOIRES
    // ========================================
    
    // Evenements positifs (bonnes nouvelles)
    private String[] evenementsPositifs = {
        "Découverte d'un gisement de cristaux énergétiques ! +100 crédits",
        "Les ingénieurs ont optimisé le réseau. Production +5% ce cycle.",
        "Arrivée d'une navette avec des provisions. Moral en hausse !",
        "Une innovation technique réduit les coûts d'entretien de 20% ce cycle."
    };
    
    // Evenements negatifs (mauvaises nouvelles)
    private String[] evenementsNegatifs = {
        "Tempête solaire détectée. Production réduite de 10% ce cycle.",
        "Panne dans un secteur résidentiel. Satisfaction en baisse.",
        "Hausse des prix des matériaux de construction.",
        "Vague de chaleur inhabituelle. Demande énergétique +15%."
    };
    
    /**
     * Constructeur du moteur de jeu
     * 
     * @param colonyName Le nom de la colonie
     */
    public GameEngine(String colonyName) {
        // On initialise les composants
        this.random = new Random();
        this.gameState = new GameState(colonyName);
        this.eventLog = new ArrayList<String>();
        
        // On initialise le jeu (residences de depart, etc.)
        initialiserJeu();
    }
    
    /**
     * Initialise le jeu avec quelques residences et une centrale de depart.
     */
    private void initialiserJeu() {
        // On recupere la reference vers la ville
        City city = gameState.getCity();
        
        // On cree 5 residences de depart avec differents niveaux
        // Les residences sont numerotees de 1 a 5
        
        Residence residence1 = new Residence(1, 1);  // Residence 1, niveau 1
        city.addResidence(residence1);
        
        Residence residence2 = new Residence(2, 1);  // Residence 2, niveau 1
        city.addResidence(residence2);
        
        Residence residence3 = new Residence(3, 2);  // Residence 3, niveau 2
        city.addResidence(residence3);
        
        Residence residence4 = new Residence(4, 2);  // Residence 4, niveau 2
        city.addResidence(residence4);
        
        Residence residence5 = new Residence(5, 3);  // Residence 5, niveau 3
        city.addResidence(residence5);
        
        // On cree une petite centrale solaire pour commencer
        PowerPlant centraleDeDepart = new PowerPlant(PowerPlantType.SOLAR, 1);
        city.addPowerPlant(centraleDeDepart);
        
        // On ajoute des messages de bienvenue dans le journal
        addLog("Bienvenue sur la colonie " + city.getName() + " !");
        addLog("Vous commencez avec " + gameState.getResources() + " crédits.");
        addLog("Population initiale: " + city.getTotalPopulation() + " habitants dans " + city.getResidenceCount() + " résidences.");
    }
    
    /**
     * Construit une nouvelle centrale electrique.
     * 
     * @param type Le type de centrale a construire
     * @return true si la construction a reussi, false sinon
     */
    public boolean buildPowerPlant(PowerPlantType type) {
        // On recupere le cout de construction
        int cout = PowerPlant.getBuildCost(type);
        
        // On verifie si on a assez de credits
        if (gameState.hasEnoughResources(cout) == false) {
            // Pas assez de credits!
            addLog("Ressources insuffisantes pour construire une centrale " + type.getName() + ". Coût: " + cout + " crédits.");
            return false;
        }
        
        // On depense les credits
        gameState.spendResources(cout);
        
        // On cree la nouvelle centrale
        PowerPlant nouvelleCentrale = new PowerPlant(type);
        
        // On l'ajoute a la ville
        gameState.getCity().addPowerPlant(nouvelleCentrale);
        
        // On ajoute un message dans le journal
        addLog("Centrale " + type.getName() + " construite ! Production: " + Math.round(nouvelleCentrale.getProduction()) + " kW, Coût: " + cout + " crédits.");
        
        return true;
    }
    
    /**
     * Ameliore une centrale existante.
     * 
     * @param powerPlant La centrale a ameliorer
     * @return true si l'amelioration a reussi, false sinon
     */
    public boolean upgradePowerPlant(PowerPlant powerPlant) {
        // On verifie si on peut ameliorer
        if (powerPlant.canUpgrade() == false) {
            addLog("Cette centrale est déjà au niveau maximum !");
            return false;
        }
        
        // On recupere le cout
        int cout = powerPlant.getUpgradeCost();
        
        // On verifie les credits
        if (gameState.hasEnoughResources(cout) == false) {
            addLog("Ressources insuffisantes pour améliorer cette centrale. Coût: " + cout + " crédits.");
            return false;
        }
        
        // On memorise les anciennes valeurs pour le message
        int ancienNiveau = powerPlant.getLevel();
        double ancienneProduction = powerPlant.getProduction();
        
        // On depense et on ameliore
        gameState.spendResources(cout);
        powerPlant.upgrade();
        
        // Message de confirmation
        addLog("Centrale " + powerPlant.getType().getName() + " améliorée: Niv." + ancienNiveau + " → Niv." + powerPlant.getLevel() + ". Production: " + Math.round(ancienneProduction) + " → " + Math.round(powerPlant.getProduction()) + " kW.");
        
        return true;
    }
    
    /**
     * Execute un cycle de jeu complet.
     * C'est la methode principale appelee quand le joueur clique sur "Cycle Suivant"
     */
    public void processCycle() {
        // On passe au cycle suivant
        gameState.nextCycle();
        addLog("\n=== CYCLE " + gameState.getCurrentCycle() + " ===");
        
        City city = gameState.getCity();
        
        // ========================================
        // ETAPE 1: Distribution de l'energie
        // ========================================
        double revenus = city.distributeEnergy();
        
        // ========================================
        // ETAPE 2: Calcul des couts d'entretien
        // ========================================
        int coutEntretien = city.getTotalMaintenanceCost();
        
        // ========================================
        // ETAPE 3: Bilan financier
        // ========================================
        int bilanNet = (int) revenus - coutEntretien;
        gameState.addResources(bilanNet);
        
        // Message dans le journal
        addLog("Revenus de vente: " + Math.round(revenus) + " crédits | Entretien: " + coutEntretien + " crédits | Net: " + bilanNet + " crédits");
        
        // ========================================
        // ETAPE 4: Mise a jour du bonheur
        // ========================================
        double satisfactionMoyenne = city.getAverageSatisfaction();
        
        // Le nouveau bonheur est une moyenne ponderee:
        // 70% de la satisfaction actuelle + 30% de l'ancien bonheur
        double nouveauBonheur = satisfactionMoyenne * 0.7 + gameState.getHappiness() * 0.3;
        
        // Si la production est insuffisante, le bonheur baisse
        double ratioEnergie = gameState.getEnergyRatio();
        if (ratioEnergie < 0.8) {
            nouveauBonheur = nouveauBonheur - GameState.HAPPINESS_DECAY_RATE;
            addLog("⚠ Production insuffisante ! Le moral baisse...");
        }
        
        gameState.setHappiness(nouveauBonheur);
        
        // ========================================
        // ETAPE 5: Croissance de la population
        // ========================================
        city.simulateGrowth();
        
        // ========================================
        // ETAPE 6: Evenements aleatoires (20% de chance)
        // ========================================
        double hasard = random.nextDouble();
        if (hasard < 0.2) {
            declencherEvenementAleatoire();
        }
        
        // ========================================
        // ETAPE 7: Afficher le statut
        // ========================================
        addLog("Énergie: " + Math.round(city.getTotalEnergyProduction()) + "/" + Math.round(city.getTotalEnergyDemand()) + " kW | Bonheur: " + Math.round(gameState.getHappiness() * 100) + "% (" + gameState.getHappinessStatus() + ")");
        
        // ========================================
        // ETAPE 8: Verifier la faillite
        // ========================================
        if (gameState.getResources() < 0 && bilanNet < 0) {
            // Le joueur est en deficit prolonge
            gameState.endGame("Vous êtes en faillite ! Les créanciers ont repris le contrôle de la colonie.");
        }
        
        // ========================================
        // ETAPE 9: Nouvelle residence tous les 5 cycles
        // ========================================
        // Si les habitants sont contents, on peut avoir une nouvelle residence
        int cycleActuel = gameState.getCurrentCycle();
        if (cycleActuel % 5 == 0 && satisfactionMoyenne > 0.7) {
            // On ajoute une nouvelle residence
            int nouvelId = city.getResidenceCount() + 1;
            int niveauAleatoire = 1 + random.nextInt(3);  // Niveau entre 1 et 3
            
            Residence nouvelleResidence = new Residence(nouvelId, niveauAleatoire);
            city.addResidence(nouvelleResidence);
            
            addLog("✨ Nouvelle résidence construite (Niv." + niveauAleatoire + ") ! La colonie s'agrandit.");
        }
    }
    
    /**
     * Declenche un evenement aleatoire (positif ou negatif)
     */
    private void declencherEvenementAleatoire() {
        // 50% de chance d'evenement positif, 50% negatif
        boolean estPositif = random.nextBoolean();
        
        if (estPositif) {
            // On choisit un evenement positif au hasard
            int index = random.nextInt(evenementsPositifs.length);
            String evenement = evenementsPositifs[index];
            
            addLog("📰 " + evenement);
            
            // On applique les effets
            // On cherche si l'evenement donne des credits
            if (evenement.contains("crédits")) {
                gameState.addResources(100);
            }
            // On cherche si l'evenement augmente le moral
            if (evenement.contains("Moral")) {
                gameState.adjustHappiness(0.05);
            }
        } else {
            // On choisit un evenement negatif au hasard
            int index = random.nextInt(evenementsNegatifs.length);
            String evenement = evenementsNegatifs[index];
            
            addLog("📰 " + evenement);
            
            // On applique les effets
            if (evenement.contains("Satisfaction")) {
                gameState.adjustHappiness(-0.05);
            }
        }
    }
    
    /**
     * Ajoute un message au journal des evenements.
     * 
     * @param message Le message a ajouter
     */
    public void addLog(String message) {
        eventLog.add(message);
        
        // On garde seulement les 50 derniers messages
        if (eventLog.size() > 50) {
            // On retire le premier message (le plus ancien)
            eventLog.remove(0);
        }
    }
    
    /**
     * Recupere les derniers messages du journal.
     * 
     * @param count Nombre de messages a recuperer
     * @return Liste des messages
     */
    public List<String> getRecentLogs(int count) {
        // On calcule l'index de depart
        int taille = eventLog.size();
        int debut;
        
        if (taille <= count) {
            debut = 0;  // On prend tout
        } else {
            debut = taille - count;  // On prend les derniers
        }
        
        // On cree une nouvelle liste avec les messages selectionnes
        ArrayList<String> resultat = new ArrayList<String>();
        for (int i = debut; i < taille; i++) {
            resultat.add(eventLog.get(i));
        }
        
        return resultat;
    }
    
    /**
     * Reinitialise le jeu pour une nouvelle partie.
     * 
     * @param colonyName Le nom de la nouvelle colonie
     */
    public void resetGame(String colonyName) {
        // On cree un nouvel etat de jeu
        this.gameState = new GameState(colonyName);
        
        // On vide le journal
        this.eventLog.clear();
        
        // On reinitialise le jeu
        initialiserJeu();
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    public GameState getGameState() {
        return gameState;
    }
    
    public List<String> getAllLogs() {
        // On retourne une copie de la liste
        ArrayList<String> copie = new ArrayList<String>();
        for (int i = 0; i < eventLog.size(); i++) {
            copie.add(eventLog.get(i));
        }
        return copie;
    }
}
