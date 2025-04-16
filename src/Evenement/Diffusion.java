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
        if(typeDiffusion == TypeContenu.ARTICLE){
            throw new IllegalArgumentException("Le type de contenu " + typeDiffusion + " n'est pas compatible avec une Diffusion.");
        }
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
        String ligne = "+-----------------------------------------------------------------------------------------------+\n";

        String ligne1 = typeContenu + " \"" + getTitre() + "\" présenté par " + presentateur +
                " publié le " + getDate().getDayOfMonth() + " " +
                getDate().getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) +
                " " + getDate().getYear() + " (à " + getDate().getHour() + ":" + getDate().getMinute() + ") dans " + getMedia().getNom();

        // sert a completer la ligne pour qu'elle remplisse le rectangle qui entoure le texte
        ligne1 = String.format("| %-84s |\n", ligne1);
        String ligne2 = String.format("| Durée de la diffusion : " + getDuree() + " minutes %-56s |\n", "");
        String ligne3 = String.format("| Description : %-79s |\n", getDescription());
        String entites = getEntitesMentionnees().toString();
        String ligne4 = String.format("| On y mentionne les entités suivantes : %-54s |\n", entites);

        return ligne + ligne1 + ligne2 + ligne3 + ligne4 + ligne;
    }
}