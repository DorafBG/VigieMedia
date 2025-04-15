package Entite;

import java.util.*;

/**
 * Classe représentant une personne qui possède des parts de propriété.
 */
public class Personne extends Entite{
    protected List<PartPropriete> possessions = new ArrayList<>();

    public Personne(String nom){
        super(nom);
    }

    /**
     * Fonction pour ajouter une part de propriété à la personne
     * @param part la part de propriété à ajouter
     */
    public void ajouterPart(PartPropriete part) {
        possessions.add(part);
    }

    /**
     * Fonction pour supprimer une part de propriété à la personne
     * @param part la part de propriété à supprimer
     */
    public void supprimerPart(PartPropriete part) {
        possessions.remove(part);
    }


    /**
     * @return la liste des parts de propriété de la personne
     */
    public List<PartPropriete> getPossessions() {
        return possessions;
    }
}
