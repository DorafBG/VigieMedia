package Evenement;

import Entite.*;
import java.time.LocalDateTime;

public class RachatPart implements IEvenement {
    private LocalDateTime date;
    private Entite acheteur; //personne ou organisation qui achete la part
    private Entite vendeur; // personne ou organisation qui vend la part
    private Entite cible;    // media ou organisation dont les parts sont vendues
    private double pourcentage; // pourcentage de la part
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
        return "Rachat de parts - " + date.toString() + "\n" +
                acheteur.getNom() + " rachète " + pourcentage + "% de " + cible.getNom() + " à " + vendeur.getNom();
    }
}