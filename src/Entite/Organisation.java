package Entite;

import java.util.ArrayList;
import java.util.List;

public class Organisation extends Entite{
    protected List<PartPropriete> possessions = new ArrayList<>();
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

    public void ajouterPart(PartPropriete part) {
        possessions.add(part);
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
