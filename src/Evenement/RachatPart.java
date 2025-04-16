package Evenement;

import Entite.*;
import java.time.LocalDateTime;

/**
 * Classe représentant un événement de rachat de parts
 * (GestionnaireRachat verifiera si cet événement est valide)
 */
public class RachatPart implements IEvenement {
    private LocalDateTime date;
    /**
     * Personne ou Organisation qui achete la part
     */
    private Entite acheteur;

    /**
     * Personne ou Organisation qui vend la part
     */
    private Entite vendeur;

    /**
     * Media ou Organisation dont les parts sont vendues
     */
    private Entite cible;

    /**
     * Pourcentage de la part
     */
    private double pourcentage;

    /**
     * Description de l'événement
     */
    private String description;

    public RachatPart(Entite acheteur, Entite vendeur, Entite cible, double pourcentage) {
        this.date = LocalDateTime.now();
        this.acheteur = acheteur;
        this.vendeur = vendeur;
        this.cible = cible;
        this.pourcentage = pourcentage;
        this.description = acheteur.getNom() + " rachète " + pourcentage + "% de " + cible.getNom() + " à " + vendeur.getNom();
    }

    public Entite getAcheteur() {
        return acheteur;
    }

    public Entite getVendeur() {
        return vendeur;
    }

    public Entite getCible() {
        return cible;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Rachat de parts ["
                + date.getDayOfMonth() + " "
                + date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " "
                + date.getYear()
                + "] : " +
                acheteur.getNom() + " rachète " + pourcentage + "% de " + cible.getNom() + " à " + vendeur.getNom();
    }
}