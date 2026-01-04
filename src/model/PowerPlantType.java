package model;

/**
 * Énumération des types de centrales électriques disponibles dans le jeu.
 * Chaque type a des caractéristiques différentes (coût, production, impact environnemental).
 */
public enum PowerPlantType {
    SOLAR("Solaire", "☀️"),
    WIND("Éolienne", "🌪️"),
    COAL("Charbon", "⚫"),
    NUCLEAR("Nucléaire", "⚛️"),
    FUSION("Fusion", "✨"); // Technologie avancée pour colonie spatiale
    
    private final String name;
    private final String icon;
    
    PowerPlantType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
    
    public String getName() {
        return name;
    }
    
    public String getIcon() {
        return icon;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
