package Evenement;

import Entite.Media;

/**
 * Classe représentant une diffusion produite par un média
 */
public class Publication extends Evenement {
    private String auteur;

    public Publication(Media media, String titre, String description, TypeContenu typePublication, String auteur) {
        super(media, titre, description, typePublication);
        if(typePublication == TypeContenu.REPORTAGE){
            throw new IllegalArgumentException("Le type de contenu " + typePublication + " n'est pas compatible avec une Publication.");
        }
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }

    @Override
    public String toString() {
        String ligne = "+-----------------------------------------------------------------------------------------------+\n";

        String ligne1 = typeContenu + " \"" + getTitre() + "\" par " + auteur +
                " publié le " + getDate().getDayOfMonth() + " " +
                getDate().getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) +
                " " + getDate().getYear() + " (à " + getDate().getHour() + ":" + getDate().getMinute() + ") dans " + getMedia().getNom();

        // sert a completer la ligne pour qu'elle remplisse le rectangle qui entoure le texte
        ligne1 = String.format("| %-84s |\n", ligne1);
        String ligne2 = String.format("| Description : %-79s |\n", getDescription());
        String entites = getEntitesMentionnees().toString();
        String ligne3 = String.format("| On y mentionne les entités suivantes : %-54s |\n", entites);

        return ligne + ligne1 + ligne2 + ligne3 + ligne;
    }

}