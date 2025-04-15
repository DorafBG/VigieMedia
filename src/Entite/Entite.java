package Entite;

public abstract class Entite {
    protected String nom;

    public Entite(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}
