package Entite;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une organisation qui possède des parts de propriété.
 */
public class Organisation extends Entite{
    protected List<PartPropriete> possessions = new ArrayList<>(); // toutes les proprietes avec le pourcentage associe
    private String commentaire;

    public Organisation(String nom, String commentaire){
        super(nom);
        this.commentaire = commentaire;
    }
    public Organisation(String nom){
        super(nom);
        this.commentaire = "";
    }

    public String getNom() {
        return nom;
    }
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * Fonction pour ajouter une part de propriété à l'organisation
     * @param part la part de propriété à ajouter
     */
    public void ajouterPart(PartPropriete part) {
        possessions.add(part);
    }

    /**
     * Fonction pour ajouter une part de propriété à la personne
     * @param part la part de propriété à ajouter
     */
    public void supprimerPart(PartPropriete part) {
        possessions.remove(part);
    }

    public List<PartPropriete> getPossessions() {
        return possessions;
    }

    @Override
    public String toString() {
        String result = nom;
        if(!commentaire.isEmpty()){
            result += " (" + commentaire + ")";
        }
        return result;
    }

}
