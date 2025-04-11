package Entite;

public class Personne {
    private String nom;

    public Personne(String nom){
        this.nom = nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
