package game.model;

/**
 * Represents a random event that can occur during the game.
 */
public class GameEvent {
    
    public enum EventType {
        // Positive events
        SUNNY_WEEK("Semaine Ensoleillée", "Le soleil brille intensément cette semaine! Production solaire +50%", true),
        STRONG_WINDS("Vents Forts", "Des vents soutenus augmentent la production éolienne! +40%", true),
        GOVERNMENT_SUBSIDY("Subvention Gouvernementale", "Le gouvernement accorde une aide aux énergies propres!", true),
        EFFICIENCY_BOOST("Amélioration Technique", "Une avancée technique améliore l'efficacité de vos centrales!", true),
        
        // Negative events
        CLOUDY_WEATHER("Temps Nuageux", "Le ciel est couvert... Production solaire réduite de 40%", false),
        CALM_WEATHER("Temps Calme", "Pas de vent aujourd'hui... Production éolienne réduite de 50%", false),
        EQUIPMENT_FAILURE("Panne d'Équipement", "Une de vos centrales nécessite des réparations urgentes!", false),
        PRICE_INCREASE("Hausse des Prix", "Le coût des matières premières augmente! Coûts opérationnels +20%", false),
        HEATWAVE("Canicule", "Une vague de chaleur augmente la demande d'énergie de 30%!", false),
        COLDSNAP("Vague de Froid", "Une vague de froid augmente la demande d'énergie de 40%!", false);
        
        private final String name;
        private final String description;
        private final boolean positive;
        
        EventType(String name, String description, boolean positive) {
            this.name = name;
            this.description = description;
            this.positive = positive;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public boolean isPositive() { return positive; }
    }
    
    private final EventType type;
    private final int duration; // in days
    private int remainingDays;
    
    /**
     * Create a new game event.
     */
    public GameEvent(EventType type, int duration) {
        this.type = type;
        this.duration = duration;
        this.remainingDays = duration;
    }
    
    /**
     * Generate a random event.
     */
    public static GameEvent generateRandomEvent() {
        EventType[] types = EventType.values();
        EventType randomType = types[GameConfig.randomIntInRange(0, types.length - 1)];
        int duration = GameConfig.randomIntInRange(1, 7); // 1-7 days
        return new GameEvent(randomType, duration);
    }
    
    /**
     * Decrease remaining days.
     * @return true if the event is still active
     */
    public boolean tick() {
        remainingDays--;
        return remainingDays > 0;
    }
    
    /**
     * Check if the event is still active.
     */
    public boolean isActive() {
        return remainingDays > 0;
    }
    
    // Getters
    public EventType getType() { return type; }
    public int getDuration() { return duration; }
    public int getRemainingDays() { return remainingDays; }
    
    @Override
    public String toString() {
        String icon = type.isPositive() ? "🌟" : "⚠️";
        return String.format("%s %s: %s (%d jours restants)", 
                icon, type.getName(), type.getDescription(), remainingDays);
    }
}
