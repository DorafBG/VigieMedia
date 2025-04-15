package Entite;

public class PartPropriete {
    // sert pour les classes qui lient des entites entre elles :
    // organisation-media.tsv
    // organisation-organisation.tsv
    // personne-media.tsv
    // personne-organisation.tsv
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
