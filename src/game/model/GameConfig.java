package game.model;

import java.util.Random;

/**
 * Configuration class for the energy management game.
 * Contains all game constants and random value generators.
 */
public class GameConfig {
    private static final Random random = new Random();
    
    // Initial player resources
    public static final double INITIAL_RESOURCES = 5000.0;
    
    // Happiness thresholds
    public static final double MIN_HAPPINESS_THRESHOLD = 20.0; // Below this = game over
    public static final double INITIAL_HAPPINESS = 50.0;
    public static final double MAX_HAPPINESS = 100.0;
    
    // Time configuration
    public static final int DAYS_PER_MONTH = 30;
    
    // Residence levels and their characteristics (min, max ranges)
    public static final int MIN_RESIDENCE_LEVEL = 1;
    public static final int MAX_RESIDENCE_LEVEL = 5;
    
    // Energy demand per residence level (kWh per day) - ranges
    public static final double[] ENERGY_DEMAND_MIN = {0, 10, 25, 50, 100, 200}; // Index 0 unused, 1-5 for levels
    public static final double[] ENERGY_DEMAND_MAX = {0, 20, 40, 80, 150, 300};
    
    // Purchasing power per residence level (money per kWh) - ranges
    public static final double[] PURCHASING_POWER_MIN = {0, 0.05, 0.08, 0.12, 0.18, 0.25};
    public static final double[] PURCHASING_POWER_MAX = {0, 0.10, 0.15, 0.20, 0.30, 0.40};
    
    // Power plant types
    public enum PlantType {
        SOLAR("Centrale Solaire", "☀️"),
        WIND("Éolienne", "🌬️"),
        COAL("Centrale à Charbon", "🏭"),
        NUCLEAR("Centrale Nucléaire", "⚛️"),
        HYDRO("Centrale Hydraulique", "💧");
        
        private final String name;
        private final String icon;
        
        PlantType(String name, String icon) {
            this.name = name;
            this.icon = icon;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
    }
    
    // Power plant costs per level (to build)
    public static final double[][] PLANT_BUILD_COSTS = {
        // Level 1, 2, 3, 4, 5
        {500, 1200, 2500, 5000, 10000},   // SOLAR
        {600, 1400, 3000, 6000, 12000},   // WIND
        {800, 1800, 4000, 8000, 16000},   // COAL
        {2000, 5000, 12000, 25000, 50000}, // NUCLEAR
        {1000, 2500, 5500, 11000, 22000}  // HYDRO
    };
    
    // Power plant upgrade costs (from level n to n+1)
    public static final double[][] PLANT_UPGRADE_COSTS = {
        // Level 1->2, 2->3, 3->4, 4->5
        {800, 1500, 3000, 6000},   // SOLAR
        {900, 1800, 3500, 7000},   // WIND
        {1200, 2400, 5000, 10000}, // COAL
        {3500, 8000, 15000, 30000}, // NUCLEAR
        {1500, 3500, 7000, 14000}  // HYDRO
    };
    
    // Power production per level (kWh per day) - base values, can vary
    public static final double[][] PLANT_PRODUCTION_BASE = {
        // Level 1, 2, 3, 4, 5
        {50, 120, 280, 600, 1200},    // SOLAR
        {60, 140, 320, 700, 1400},    // WIND
        {100, 250, 550, 1200, 2500},  // COAL
        {200, 500, 1200, 2500, 5000}, // NUCLEAR
        {80, 200, 450, 950, 2000}     // HYDRO
    };
    
    // Operating costs per day per level
    public static final double[][] PLANT_OPERATING_COSTS = {
        // Level 1, 2, 3, 4, 5
        {2, 5, 12, 25, 50},    // SOLAR
        {3, 7, 15, 30, 60},    // WIND
        {8, 20, 45, 95, 190},  // COAL
        {15, 40, 95, 200, 400}, // NUCLEAR
        {5, 12, 28, 58, 120}   // HYDRO
    };
    
    // Environmental impact (pollution) per level
    public static final double[][] PLANT_POLLUTION = {
        // Level 1, 2, 3, 4, 5
        {0, 0, 0, 0, 0},       // SOLAR - Clean
        {0, 0, 0, 0, 0},       // WIND - Clean
        {5, 12, 28, 60, 120},  // COAL - Polluting
        {1, 2, 5, 10, 20},     // NUCLEAR - Low pollution, but risk
        {0, 0, 0, 0, 0}        // HYDRO - Clean
    };
    
    // Weather effects on production (multiplier range)
    public static final double SOLAR_WEATHER_MIN = 0.3;
    public static final double SOLAR_WEATHER_MAX = 1.2;
    public static final double WIND_WEATHER_MIN = 0.2;
    public static final double WIND_WEATHER_MAX = 1.5;
    
    // City growth rate (new residences per month)
    public static final int CITY_GROWTH_MIN = 0;
    public static final int CITY_GROWTH_MAX = 2;
    
    // Random event probabilities
    public static final double EVENT_PROBABILITY = 0.15; // 15% chance per cycle
    
    /**
     * Get a random value within a range.
     */
    public static double randomInRange(double min, double max) {
        return min + random.nextDouble() * (max - min);
    }
    
    /**
     * Get a random integer within a range (inclusive).
     */
    public static int randomIntInRange(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
    
    /**
     * Get a random boolean with given probability.
     */
    public static boolean randomChance(double probability) {
        return random.nextDouble() < probability;
    }
    
    /**
     * Get energy demand for a residence level (randomized within range).
     */
    public static double getEnergyDemandForLevel(int level) {
        if (level < MIN_RESIDENCE_LEVEL || level > MAX_RESIDENCE_LEVEL) {
            throw new IllegalArgumentException("Invalid residence level: " + level);
        }
        return randomInRange(ENERGY_DEMAND_MIN[level], ENERGY_DEMAND_MAX[level]);
    }
    
    /**
     * Get purchasing power for a residence level (randomized within range).
     */
    public static double getPurchasingPowerForLevel(int level) {
        if (level < MIN_RESIDENCE_LEVEL || level > MAX_RESIDENCE_LEVEL) {
            throw new IllegalArgumentException("Invalid residence level: " + level);
        }
        return randomInRange(PURCHASING_POWER_MIN[level], PURCHASING_POWER_MAX[level]);
    }
    
    /**
     * Get weather effect multiplier for solar plants.
     */
    public static double getSolarWeatherEffect() {
        return randomInRange(SOLAR_WEATHER_MIN, SOLAR_WEATHER_MAX);
    }
    
    /**
     * Get weather effect multiplier for wind plants.
     */
    public static double getWindWeatherEffect() {
        return randomInRange(WIND_WEATHER_MIN, WIND_WEATHER_MAX);
    }
}
