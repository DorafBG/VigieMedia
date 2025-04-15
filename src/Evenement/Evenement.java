package Evenement;

import Entite.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe abstraite utilisée pour représenter un évènement produit par un média
 * (Publication ou Diffusion)
 */
public abstract class Evenement implements IEvenement {
    protected LocalDateTime date;
    protected Media media;
    protected String titre;
    protected String description;
    protected TypeContenu typeContenu;
    protected List<Entite> entitesMentionnees;

    /**
     * Constructeur pour créer un événement.
     * Si le type d'événement ne peut pas être publié sur le média spécifié,
     * une exception sera levée.
     * @param media le média qui publie l'événement
     * @param titre le titre de l'événement
     * @param description la description de l'événement
     * @param typeContenu le type de l'événement parmi celles qui existent
     */
    public Evenement(Media media, String titre, String description, TypeContenu typeContenu) {
        this.date = LocalDateTime.now();
        this.media = media;
        this.titre = titre;
        this.description = description;
        this.entitesMentionnees = new ArrayList<>();

        if(!typeContenu.estCompatibleAvec(media.getType())){
            throw new IllegalArgumentException("Le type de contenu " + typeContenu + " n'est pas compatible avec le média " + media.getNom());
        } else {
            this.typeContenu = typeContenu;
        }

    }

    /**
     * Permet d'ajouter une entité mentionnée dans l'événement
     * @param entite l'entité mentionnée
     */
    public void ajouterEntiteMentionnee(Entite entite) {
        entitesMentionnees.add(entite);
    }

    /**
     * Permet d'ajouter des entités mentionnées dans l'événement en même temps
     * @param entites toutes les entités mentionnées
     */
    public void ajouterEntitesMentionnees(List<Entite> entites) {
        entitesMentionnees.addAll(entites);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Media getMedia() {
        return media;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public List<Entite> getEntitesMentionnees() {
        return entitesMentionnees;
    }

    public boolean contientEntite(Entite entite) {
        return entitesMentionnees.contains(entite);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName())
                .append(" - ")
                .append(titre)
                .append(" (")
                .append(date)
                .append(") dans ")
                .append(media.getNom())
                .append("\nMentionne: ");

        for (Entite entite : entitesMentionnees) {
            sb.append(entite.getNom()).append(", ");
        }

        if (!entitesMentionnees.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb.toString();
    }
}