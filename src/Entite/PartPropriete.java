package Entite;

/**
 * Classe représentant une part de propriété d'une entité (personne ou organisation)
 * sur une autre entité (média ou organisation).
 * Elle contient des informations sur le propriétaire, la cible et le pourcentage de propriété.
 */
public class PartPropriete {
    /**
     * Propriétaire de la part de propriété (peut être une personne ou une organisation)
     */
    private Entite proprietaire;

    /**
     * Cible de la part de propriété (peut être un média ou une organisation)
     */
    private Entite cible;

    /**
     * Pourcentage de propriété (entre 0 et 100)
     */
    private double pourcentage;

    /**
     * Constructeur de la classe PartPropriete.
     * (vérifie si le propriétaire est une personne ou une organisation,
     * si la cible est un média ou une organisation,
     * et si le pourcentage est compris entre 0 et 100)
     * @param proprietaire Une Entite pouvant être une personne ou une organisation
     * @param cible Une Entite pouvant être un média ou une organisation
     * @param pourcentage Le pourcentage de propriété (entre 0 et 100)
     */
    public PartPropriete(Entite proprietaire, Entite cible, double pourcentage) {
        if(proprietaire instanceof Media){
            throw new IllegalArgumentException("Le propriétaire doit être une personne ou une organisation !");
        } else if(cible instanceof Personne){
            throw new IllegalArgumentException("Une entité ne peut pas être propriétaire d'une personne !");
        } else if(pourcentage < 0 || pourcentage > 100){
            throw new IllegalArgumentException("Le pourcentage doit être compris entre 0 et 10 !");
        }
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
