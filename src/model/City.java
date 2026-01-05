package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe represente la colonie spatiale (la "ville").
 * 
 * La colonie contient:
 * - Des residences (ou vivent les habitants)
 * - Des centrales electriques (qui produisent l'energie)
 * 
 * Cette classe gere aussi les calculs globaux comme la demande totale,
 * la production totale, etc.
 */
public class City {
    
    // Le nom de la colonie
    private String name;
    
    // Liste de toutes les residences
    // ArrayList est comme un tableau qui peut grandir
    private ArrayList<Residence> residences;
    
    // Liste de toutes les centrales
    private ArrayList<PowerPlant> powerPlants;
    
    /**
     * Constructeur: cree une nouvelle colonie vide
     * 
     * @param name Le nom de la colonie
     */
    public City(String name) {
        this.name = name;
        
        // On cree des listes vides
        this.residences = new ArrayList<Residence>();
        this.powerPlants = new ArrayList<PowerPlant>();
    }
    
    /**
     * Ajoute une residence a la colonie
     * 
     * @param residence La residence a ajouter
     */
    public void addResidence(Residence residence) {
        residences.add(residence);
    }
    
    /**
     * Ajoute une centrale electrique a la colonie
     * 
     * @param powerPlant La centrale a ajouter
     */
    public void addPowerPlant(PowerPlant powerPlant) {
        powerPlants.add(powerPlant);
    }
    
    /**
     * Retire une centrale electrique de la colonie
     * 
     * @param powerPlant La centrale a retirer
     */
    public void removePowerPlant(PowerPlant powerPlant) {
        powerPlants.remove(powerPlant);
    }
    
    /**
     * Calcule la demande energetique totale de toutes les residences.
     * 
     * @return La demande totale en kW
     */
    public double getTotalEnergyDemand() {
        // On additionne le besoin de chaque residence
        double total = 0;
        
        // Boucle for classique: on parcourt chaque residence
        for (int i = 0; i < residences.size(); i++) {
            Residence residence = residences.get(i);
            total = total + residence.getEnergyNeed();
        }
        
        return total;
    }
    
    /**
     * Calcule la production energetique totale de toutes les centrales.
     * 
     * @return La production totale en kW
     */
    public double getTotalEnergyProduction() {
        // On additionne la production de chaque centrale
        double total = 0;
        
        for (int i = 0; i < powerPlants.size(); i++) {
            PowerPlant centrale = powerPlants.get(i);
            total = total + centrale.getProduction();
        }
        
        return total;
    }
    
    /**
     * Calcule le cout total d'entretien de toutes les centrales.
     * 
     * @return Le cout total en credits
     */
    public int getTotalMaintenanceCost() {
        int total = 0;
        
        for (int i = 0; i < powerPlants.size(); i++) {
            PowerPlant centrale = powerPlants.get(i);
            total = total + centrale.getMaintenanceCost();
        }
        
        return total;
    }
    
    /**
     * Calcule la population totale de la colonie.
     * 
     * @return Le nombre d'habitants
     */
    public int getTotalPopulation() {
        int total = 0;
        
        for (int i = 0; i < residences.size(); i++) {
            Residence residence = residences.get(i);
            total = total + residence.getInhabitants();
        }
        
        return total;
    }
    
    /**
     * Calcule le niveau de satisfaction moyen de la colonie.
     * 
     * @return La satisfaction moyenne (entre 0.0 et 1.0)
     */
    public double getAverageSatisfaction() {
        // Si pas de residences, on retourne 1.0 (100%)
        if (residences.size() == 0) {
            return 1.0;
        }
        
        // On fait la somme des satisfactions
        double somme = 0;
        for (int i = 0; i < residences.size(); i++) {
            Residence residence = residences.get(i);
            somme = somme + residence.getSatisfaction();
        }
        
        // On divise par le nombre de residences pour avoir la moyenne
        double moyenne = somme / residences.size();
        return moyenne;
    }
    
    /**
     * Distribue l'energie disponible aux residences.
     * Met a jour la satisfaction de chaque residence.
     * 
     * @return Le revenu total genere (en credits)
     */
    public double distributeEnergy() {
        double productionTotale = getTotalEnergyProduction();
        double demandeTotale = getTotalEnergyDemand();
        double revenuTotal = 0;
        
        // Si pas de demande, pas de revenus
        if (demandeTotale == 0) {
            return 0;
        }
        
        // On calcule comment distribuer l'energie
        if (productionTotale >= demandeTotale) {
            // BONNE NOUVELLE: On a assez d'energie pour tout le monde!
            // Chaque residence recoit ce qu'elle demande
            
            for (int i = 0; i < residences.size(); i++) {
                Residence residence = residences.get(i);
                
                // On leur donne toute l'energie qu'ils demandent
                double energieFournie = residence.getEnergyNeed();
                
                // On met a jour leur satisfaction
                residence.updateSatisfaction(energieFournie);
                
                // On calcule le revenu
                revenuTotal = revenuTotal + residence.calculateRevenue(energieFournie);
            }
        } else {
            // PROBLEME: On n'a pas assez d'energie!
            // On distribue proportionnellement (tout le monde recoit le meme %)
            
            double ratio = productionTotale / demandeTotale;
            // Ex: si on a 800 kW pour 1000 kW de demande, ratio = 0.8 (80%)
            
            for (int i = 0; i < residences.size(); i++) {
                Residence residence = residences.get(i);
                
                // On leur donne seulement une partie de ce qu'ils demandent
                double energieFournie = residence.getEnergyNeed() * ratio;
                
                // On met a jour leur satisfaction (ils ne seront pas contents!)
                residence.updateSatisfaction(energieFournie);
                
                // On calcule le revenu (moins que prevu)
                revenuTotal = revenuTotal + residence.calculateRevenue(energieFournie);
            }
        }
        
        return revenuTotal;
    }
    
    /**
     * Simule la croissance de la population dans chaque residence.
     */
    public void simulateGrowth() {
        for (int i = 0; i < residences.size(); i++) {
            Residence residence = residences.get(i);
            residence.simulateGrowth();
        }
    }
    
    // ========================================
    // GETTERS
    // ========================================
    
    /**
     * Retourne le nom de la colonie
     */
    public String getName() {
        return name;
    }
    
    /**
     * Retourne une copie de la liste des residences.
     * On fait une copie pour eviter que l'exterieur modifie notre liste.
     */
    public List<Residence> getResidences() {
        // On cree une nouvelle ArrayList avec les memes elements
        ArrayList<Residence> copie = new ArrayList<Residence>();
        for (int i = 0; i < residences.size(); i++) {
            copie.add(residences.get(i));
        }
        return copie;
    }
    
    /**
     * Retourne une copie de la liste des centrales.
     */
    public List<PowerPlant> getPowerPlants() {
        ArrayList<PowerPlant> copie = new ArrayList<PowerPlant>();
        for (int i = 0; i < powerPlants.size(); i++) {
            copie.add(powerPlants.get(i));
        }
        return copie;
    }
    
    /**
     * Retourne le nombre de residences
     */
    public int getResidenceCount() {
        return residences.size();
    }
    
    /**
     * Retourne le nombre de centrales
     */
    public int getPowerPlantCount() {
        return powerPlants.size();
    }
}
