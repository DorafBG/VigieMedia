package Evenement;

import Entite.Media;

/**
 * Classe représentant une diffusion produite par un média
 */
public class Publication extends Evenement {
    private String auteur;

    public Publication(Media media, String titre, String description, TypeContenu typePublication, String auteur) {
        super(media, titre, description, typePublication);
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    @Override
    public String toString() {
        return typeContenu + " \"" + getTitre() + "\" par " + auteur +
                " (" + getDate() + ") dans " + getMedia().getNom() + "\n" +
                "Description: " + getDescription() + "\n" +
                "Entités mentionnées: " + getEntitesMentionnees();
    }
}