package Entite;

/**
 * Classe abstraite représentant une entité,
 * pouvant soit être une personne, un média ou une organisation.
 */
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
