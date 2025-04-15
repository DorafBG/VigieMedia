package Entite;

/**
 * Classe représentant une part de propriété d'une entité (personne ou organisation)
 * sur une autre entité (média ou organisation).
 * Elle contient des informations sur le propriétaire, la cible et le pourcentage de propriété.
 */
public class PartPropriete {
    private Entite proprietaire; //personne ou organisation
    private Entite cible; //media ou organisation
    private double pourcentage;

    public PartPropriete(Entite proprietaire, Entite cible, double pourcentage) {
        this.proprietaire = proprietaire;
        this.cible = cible;
        this.pourcentage = pourcentage;
    }

    // getters et setters
    public Entite getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Entite proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Entite getCible() {
        return cible;
    }

    public void setCible(Entite cible) {
        this.cible = cible;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    @Override
    public String toString() {
        return proprietaire.getNom() + " détient " + pourcentage + "% de " + cible.toString();
    }
}
