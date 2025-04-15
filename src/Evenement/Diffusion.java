package Evenement;

import Entite.Media;

/**
 * Classe représentant une diffusion produite par un média
 */
public class Diffusion extends Evenement {
    private Double duree; // duree en minutes
    private String presentateur;

    public Diffusion(Media media, String titre, String description, TypeContenu typeDiffusion, Double duree, String presentateur) {
        super(media, titre, description, typeDiffusion);
        this.duree = duree;
        this.presentateur = presentateur;
    }

    public Double getDuree() {
        return duree;
    }

    public String getPresentateur() {
        return presentateur;
    }

    @Override
    public String toString() {
        return typeContenu + " \"" + getTitre() + "\" présenté par " + presentateur +
                " (" + getDate() + ", durée: " + duree + " minutes) sur " + getMedia().getNom() + "\n" +
                "Description: " + getDescription() + "\n" +
                "Entités mentionnées: " + getEntitesMentionnees();
    }
}