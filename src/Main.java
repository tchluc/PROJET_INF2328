import view.GameApplication;

/**
 * Point d'entree principal du jeu Colony Power.
 * 
 * Cette classe contient la methode main() qui est le point de depart
 * du programme Java. Elle lance l'application JavaFX.
 * 
 * Pour lancer le jeu, il suffit d'executer cette classe.
 */
public class Main {
    
    /**
     * Methode principale - point d'entree du programme.
     * 
     * @param args Les arguments passes en ligne de commande (non utilises ici)
     */
    public static void main(String[] args) {
        // On appelle la methode main() de GameApplication
        // qui va demarrer l'interface graphique JavaFX
        GameApplication.main(args);
    }
}
