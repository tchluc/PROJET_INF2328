package game.ui;

import game.engine.GameEngine;
import game.engine.GameEngine.SimulationResult;
import game.model.*;
import game.model.GameConfig.PlantType;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the energy management game.
 */
public class ConsoleUI {
    private final Scanner scanner;
    private GameEngine engine;
    private boolean running;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    /**
     * Start the game.
     */
    public void start() {
        printWelcome();
        setupGame();
        
        while (running && !engine.isGameOver()) {
            printMainMenu();
            handleMainMenuChoice();
        }
        
        if (engine.isGameOver()) {
            printGameOver();
        }
        
        printGoodbye();
    }
    
    /**
     * Print welcome message and lore.
     */
    private void printWelcome() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║           ⚡ ÉLECTRICITÉ CITY - GESTION D'ÉNERGIE ⚡              ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                  ║");
        System.out.println("║  Bienvenue, nouveau gestionnaire d'énergie!                      ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  L'année est 2025. La petite ville d'Énergieville vient de       ║");
        System.out.println("║  vous confier la responsabilité de sa production électrique.    ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  Le maire compte sur vous pour:                                  ║");
        System.out.println("║    • Assurer un approvisionnement stable en électricité         ║");
        System.out.println("║    • Maintenir le bonheur des habitants au-dessus de 20%        ║");
        System.out.println("║    • Gérer vos ressources financières avec sagesse              ║");
        System.out.println("║    • Développer un mix énergétique durable                      ║");
        System.out.println("║                                                                  ║");
        System.out.println("║  Attention: si les habitants sont trop mécontents, le maire     ║");
        System.out.println("║  n'hésitera pas à vous retirer vos fonctions!                   ║");
        System.out.println("║                                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println("\nAppuyez sur Entrée pour commencer...");
        scanner.nextLine();
    }
    
    /**
     * Set up a new game.
     */
    private void setupGame() {
        clearScreen();
        System.out.println("=== Configuration de la partie ===\n");
        
        System.out.print("Entrez le nom de votre ville (défaut: Énergieville): ");
        String cityName = scanner.nextLine().trim();
        if (cityName.isEmpty()) {
            cityName = "Énergieville";
        }
        
        System.out.print("Nombre initial de résidences [10-50] (défaut: 20): ");
        String residencesInput = scanner.nextLine().trim();
        int initialResidences = 20;
        try {
            if (!residencesInput.isEmpty()) {
                initialResidences = Integer.parseInt(residencesInput);
                initialResidences = Math.max(10, Math.min(50, initialResidences));
            }
        } catch (NumberFormatException e) {
            System.out.println("Valeur invalide, utilisation de 20 résidences.");
        }
        
        engine = new GameEngine(cityName);
        engine.initializeGame(initialResidences);
        
        System.out.println("\n✅ Ville créée avec " + initialResidences + " résidences!");
        System.out.println("💰 Vous disposez de " + String.format("%.0f", engine.getPlayer().getResources()) + "€ de ressources initiales.");
        System.out.println("\nAppuyez sur Entrée pour commencer votre gestion...");
        scanner.nextLine();
    }
    
    /**
     * Print the main menu.
     */
    private void printMainMenu() {
        clearScreen();
        printStatusBar();
        
        System.out.println("\n=== MENU PRINCIPAL ===\n");
        System.out.println("1. 📊 Voir le tableau de bord complet");
        System.out.println("2. 🏭 Construire une centrale électrique");
        System.out.println("3. ⬆️  Améliorer une centrale existante");
        System.out.println("4. 🔧 Gérer les centrales (activer/désactiver/vendre)");
        System.out.println("5. 🏠 Voir les résidences");
        System.out.println("6. ⏭️  Avancer d'un jour");
        System.out.println("7. ⏩ Avancer d'une semaine (7 jours)");
        System.out.println("8. 📅 Avancer d'un mois (30 jours)");
        System.out.println("9. 📈 Statistiques du joueur");
        System.out.println("0. 🚪 Quitter le jeu");
        System.out.print("\nVotre choix: ");
    }
    
    /**
     * Print a compact status bar.
     */
    private void printStatusBar() {
        City city = engine.getCity();
        Player player = engine.getPlayer();
        
        double production = city.getTotalEnergyProduction();
        double demand = city.getTotalEnergyDemand();
        double coverage = demand > 0 ? (production / demand * 100) : 100;
        
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ 📅 %s | 💰 %.0f€ | ⚡ %.0f/%.0f kWh (%.0f%%) | 😊 %.0f%% │%n",
                engine.getCurrentDateString(),
                player.getResources(),
                production, demand, coverage,
                city.getAverageHappiness());
        System.out.println("└─────────────────────────────────────────────────────────────────┘");
        
        // Show active events
        List<GameEvent> events = engine.getActiveEvents();
        if (!events.isEmpty()) {
            System.out.println("\n📢 Événements actifs:");
            for (GameEvent event : events) {
                System.out.println("   " + event);
            }
        }
    }
    
    /**
     * Handle main menu choice.
     */
    private void handleMainMenuChoice() {
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> showDashboard();
            case "2" -> buildPowerPlant();
            case "3" -> upgradePowerPlant();
            case "4" -> managePowerPlants();
            case "5" -> showResidences();
            case "6" -> advanceTime(1);
            case "7" -> advanceTime(7);
            case "8" -> advanceTime(30);
            case "9" -> showPlayerStats();
            case "0" -> confirmQuit();
            default -> {
                System.out.println("❌ Choix invalide. Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Show detailed dashboard.
     */
    private void showDashboard() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📊 TABLEAU DE BORD                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        City city = engine.getCity();
        System.out.println(city.getStatusSummary());
        
        System.out.println("\n--- Centrales Électriques ---");
        List<PowerPlant> plants = city.getPowerPlants();
        if (plants.isEmpty()) {
            System.out.println("Aucune centrale construite.");
        } else {
            for (PowerPlant plant : plants) {
                System.out.println("  " + plant);
            }
        }
        
        System.out.println("\n--- Modificateurs Actifs ---");
        System.out.println(engine.getModifiersString());
        
        pressEnterToContinue();
    }
    
    /**
     * Build a new power plant.
     */
    private void buildPowerPlant() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🏭 CONSTRUIRE UNE CENTRALE                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        System.out.printf("💰 Ressources disponibles: %.0f€%n%n", engine.getPlayer().getResources());
        
        System.out.println("Types de centrales disponibles:\n");
        PlantType[] types = PlantType.values();
        for (int i = 0; i < types.length; i++) {
            PlantType type = types[i];
            double cost = PowerPlant.getBuildCost(type, 1);
            double production = GameConfig.PLANT_PRODUCTION_BASE[type.ordinal()][0];
            double pollution = GameConfig.PLANT_POLLUTION[type.ordinal()][0];
            
            System.out.printf("%d. %s %s (Niveau 1)%n", i + 1, type.getIcon(), type.getName());
            System.out.printf("   💰 Coût: %.0f€ | ⚡ Production: %.0f kWh/jour | 🏭 Pollution: %.0f%n%n",
                    cost, production, pollution);
        }
        
        System.out.println("0. ↩️  Retour au menu principal\n");
        System.out.print("Choisissez un type de centrale: ");
        
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("0")) return;
        
        try {
            int typeIndex = Integer.parseInt(choice) - 1;
            if (typeIndex >= 0 && typeIndex < types.length) {
                PlantType selectedType = types[typeIndex];
                
                // Choose level
                System.out.println("\nNiveaux disponibles:");
                for (int level = 1; level <= 5; level++) {
                    double cost = PowerPlant.getBuildCost(selectedType, level);
                    double production = GameConfig.PLANT_PRODUCTION_BASE[selectedType.ordinal()][level - 1];
                    String affordable = engine.getPlayer().canAfford(cost) ? "✅" : "❌";
                    System.out.printf("%d. Niveau %d - Coût: %.0f€, Production: %.0f kWh/jour %s%n",
                            level, level, cost, production, affordable);
                }
                
                System.out.print("\nChoisissez un niveau (1-5): ");
                String levelChoice = scanner.nextLine().trim();
                int level = Integer.parseInt(levelChoice);
                
                if (level >= 1 && level <= 5) {
                    if (engine.buildPowerPlant(selectedType, level)) {
                        System.out.printf("%n✅ %s %s de niveau %d construite avec succès!%n",
                                selectedType.getIcon(), selectedType.getName(), level);
                    } else {
                        System.out.println("\n❌ Ressources insuffisantes!");
                    }
                } else {
                    System.out.println("\n❌ Niveau invalide!");
                }
            } else {
                System.out.println("\n❌ Type invalide!");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Choix invalide!");
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Upgrade an existing power plant.
     */
    private void upgradePowerPlant() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ⬆️  AMÉLIORER UNE CENTRALE                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        System.out.printf("💰 Ressources disponibles: %.0f€%n%n", engine.getPlayer().getResources());
        
        List<PowerPlant> plants = engine.getCity().getPowerPlants();
        
        if (plants.isEmpty()) {
            System.out.println("Aucune centrale à améliorer.");
            pressEnterToContinue();
            return;
        }
        
        System.out.println("Centrales pouvant être améliorées:\n");
        int count = 0;
        for (PowerPlant plant : plants) {
            if (plant.canUpgrade()) {
                count++;
                double upgradeCost = plant.getUpgradeCost();
                String affordable = engine.getPlayer().canAfford(upgradeCost) ? "✅" : "❌";
                System.out.printf("ID %d: %s - Coût amélioration vers niveau %d: %.0f€ %s%n",
                        plant.getId(), plant, plant.getLevel() + 1, upgradeCost, affordable);
            }
        }
        
        if (count == 0) {
            System.out.println("Toutes les centrales sont au niveau maximum!");
            pressEnterToContinue();
            return;
        }
        
        System.out.print("\nEntrez l'ID de la centrale à améliorer (0 pour annuler): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("0")) return;
        
        try {
            int plantId = Integer.parseInt(choice);
            if (engine.upgradePowerPlant(plantId)) {
                System.out.println("\n✅ Centrale améliorée avec succès!");
            } else {
                System.out.println("\n❌ Impossible d'améliorer cette centrale (ressources insuffisantes ou niveau max).");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ ID invalide!");
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Manage power plants (toggle, sell).
     */
    private void managePowerPlants() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🔧 GÉRER LES CENTRALES                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        List<PowerPlant> plants = engine.getCity().getPowerPlants();
        
        if (plants.isEmpty()) {
            System.out.println("Aucune centrale à gérer.");
            pressEnterToContinue();
            return;
        }
        
        System.out.println("Vos centrales:\n");
        for (PowerPlant plant : plants) {
            double sellValue = PowerPlant.getBuildCost(plant.getType(), plant.getLevel()) * 0.4;
            System.out.printf("ID %d: %s (Valeur de revente: %.0f€)%n", 
                    plant.getId(), plant, sellValue);
        }
        
        System.out.println("\nActions:");
        System.out.println("1. Activer/Désactiver une centrale");
        System.out.println("2. Vendre une centrale");
        System.out.println("0. Retour");
        System.out.print("\nVotre choix: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1" -> {
                System.out.print("ID de la centrale à basculer: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    if (engine.togglePowerPlant(id)) {
                        PowerPlant plant = engine.getCity().getPowerPlantById(id);
                        System.out.println("\n✅ Centrale " + (plant.isOperational() ? "activée" : "désactivée") + "!");
                    } else {
                        System.out.println("\n❌ Centrale non trouvée!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n❌ ID invalide!");
                }
            }
            case "2" -> {
                System.out.print("ID de la centrale à vendre: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    PowerPlant plant = engine.getCity().getPowerPlantById(id);
                    if (plant != null) {
                        double sellValue = PowerPlant.getBuildCost(plant.getType(), plant.getLevel()) * 0.4;
                        System.out.printf("Êtes-vous sûr de vendre cette centrale pour %.0f€? (o/n): ", sellValue);
                        String confirm = scanner.nextLine().trim().toLowerCase();
                        if (confirm.equals("o") || confirm.equals("oui")) {
                            engine.sellPowerPlant(id);
                            System.out.println("\n✅ Centrale vendue!");
                        } else {
                            System.out.println("\nVente annulée.");
                        }
                    } else {
                        System.out.println("\n❌ Centrale non trouvée!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\n❌ ID invalide!");
                }
            }
            case "0" -> { return; }
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Show residences.
     */
    private void showResidences() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🏠 RÉSIDENCES DE LA VILLE                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        List<Residence> residences = engine.getCity().getResidences();
        
        System.out.printf("Total: %d résidences%n%n", residences.size());
        
        // Group by level
        int[] levelCounts = new int[6];
        double[] levelDemand = new double[6];
        double[] levelSatisfaction = new double[6];
        
        for (Residence r : residences) {
            levelCounts[r.getLevel()]++;
            levelDemand[r.getLevel()] += r.getEnergyDemand();
            levelSatisfaction[r.getLevel()] += r.getSatisfaction();
        }
        
        System.out.println("Répartition par niveau:\n");
        for (int level = 1; level <= 5; level++) {
            if (levelCounts[level] > 0) {
                double avgSat = levelSatisfaction[level] / levelCounts[level];
                System.out.printf("Niveau %d (%s): %d résidences, Demande totale: %.0f kWh, Satisfaction moyenne: %.0f%%%n",
                        level, getLevelName(level), levelCounts[level], levelDemand[level], avgSat);
            }
        }
        
        System.out.println("\n--- Liste détaillée (premiers 20) ---\n");
        int shown = 0;
        for (Residence r : residences) {
            if (shown >= 20) {
                System.out.println("... et " + (residences.size() - 20) + " autres résidences.");
                break;
            }
            System.out.println("  " + r);
            shown++;
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Get level name for residence.
     */
    private String getLevelName(int level) {
        return switch (level) {
            case 1 -> "Petit Apt";
            case 2 -> "Apt Moyen";
            case 3 -> "Grande Maison";
            case 4 -> "Villa";
            case 5 -> "Manoir";
            default -> "Niveau " + level;
        };
    }
    
    /**
     * Advance time.
     */
    private void advanceTime(int days) {
        clearScreen();
        System.out.println("⏳ Simulation de " + days + " jour(s)...\n");
        
        SimulationResult result = engine.simulateDays(days);
        
        System.out.printf("📊 Résultats de la période:%n");
        System.out.printf("   💵 Revenus: %.0f€%n", result.getRevenue());
        System.out.printf("   💸 Dépenses: %.0f€%n", result.getExpenses());
        System.out.printf("   📈 Profit net: %.0f€%n", result.getProfit());
        
        if (!result.getMessages().isEmpty()) {
            System.out.println("\n📢 Événements durant cette période:");
            for (String msg : result.getMessages()) {
                System.out.println("   " + msg);
            }
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Show player statistics.
     */
    private void showPlayerStats() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    📈 STATISTIQUES DU JOUEUR                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        System.out.println(engine.getPlayer().getStatsSummary());
        
        pressEnterToContinue();
    }
    
    /**
     * Confirm quit.
     */
    private void confirmQuit() {
        System.out.print("\nÊtes-vous sûr de vouloir quitter? (o/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("o") || confirm.equals("oui")) {
            running = false;
        }
    }
    
    /**
     * Print game over screen.
     */
    private void printGameOver() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         ❌ GAME OVER ❌                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Raison: " + engine.getGameOverReason());
        System.out.println("\n" + engine.getPlayer().getStatsSummary());
        
        pressEnterToContinue();
    }
    
    /**
     * Print goodbye message.
     */
    private void printGoodbye() {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║           Merci d'avoir joué à Électricité City!                 ║");
        System.out.println("║                                                                  ║");
        System.out.println("║           À bientôt pour de nouvelles aventures                  ║");
        System.out.println("║              dans le monde de l'énergie! ⚡                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝\n");
    }
    
    /**
     * Clear screen using ANSI escape codes.
     * Falls back to printing blank lines if ANSI not supported.
     */
    private void clearScreen() {
        // Try ANSI escape codes first (works on most terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
        // Also print some newlines as fallback for terminals that don't support ANSI
        System.out.println("\n".repeat(3));
    }
    
    /**
     * Wait for user to press Enter.
     */
    private void pressEnterToContinue() {
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }
}
