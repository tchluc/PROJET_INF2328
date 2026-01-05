package model;

/**
 * Cette enumeration definit les differents types de centrales electriques.
 * 
 * Une enumeration (enum) est une liste de valeurs fixes.
 * Ici, on definit 5 types de centrales possibles.
 * Chaque type a un nom affichable et une icone (emoji).
 */
public enum PowerPlantType {
    
    // On definit les 5 types de centrales
    // Format: NOM_INTERNE("Nom affiche", "icone")
    SOLAR("Solaire", "☀️"),
    WIND("Éolienne", "🌪️"),
    COAL("Charbon", "⚫"),
    NUCLEAR("Nucléaire", "⚛️"),
    FUSION("Fusion", "✨");  // Technologie avancee de la colonie spatiale
    
    // Les attributs de chaque type
    private String name;  // Le nom a afficher
    private String icon;  // L'icone emoji
    
    /**
     * Constructeur de l'enum (appele automatiquement pour chaque valeur)
     * 
     * @param name Le nom a afficher
     * @param icon L'icone emoji
     */
    PowerPlantType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }
    
    /**
     * Retourne le nom affichable du type de centrale
     */
    public String getName() {
        return name;
    }
    
    /**
     * Retourne l'icone emoji du type de centrale
     */
    public String getIcon() {
        return icon;
    }
    
    /**
     * Retourne le nom quand on convertit en String
     */
    @Override
    public String toString() {
        return name;
    }
}
