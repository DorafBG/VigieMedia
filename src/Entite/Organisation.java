package Entite;

public class Organisation {
    private String nom;
    private String commentaire;

    public Organisation(String nom, String commentaire){
        this.nom = nom;
        this.commentaire = commentaire;
    }
    public Organisation(String nom){
        this.nom = nom;
        this.commentaire = "";
    }

    public String getNom() {
        return nom;
    }
    public String getCommentaire() {
        return commentaire;
    }
    @Override
    public String toString() {
        String result = nom;
        if(!commentaire.equals("")){
            result += " (" + commentaire + ")";
        }
        return result;
    }

}
