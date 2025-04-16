package Module;

import Entite.*;
import Evenement.*;
import Observateur.IObservateur;
import Vigie.Alerte;
import Vigie.Vigie;

import java.util.*;

/**
 * Module qui suit les événements liés à une personne spécifique.
 */
public class ModuleSuiviPersonne implements IObservateur {
    /**
     * La personne suivie par le module
     */
    private final Personne personneASuivre;

    /**
     * Historique des événements liés à la personne
     */
    private final List<Evenement> historiqueEvenements;

    /**
     * Compteur de mentions de la personne suivie par média
     */
    private final Map<Media, Integer> compteurMentionsParMedia;

    /**
     * Instance de la Vigie pour envoyer des alertes
     */
    private final Vigie vigie;

    public ModuleSuiviPersonne(Personne personneASuivre) {
        this.personneASuivre = personneASuivre;
        this.historiqueEvenements = new ArrayList<>();
        this.compteurMentionsParMedia = new HashMap<>();
        this.vigie = Vigie.getInstance();
    }

    @Override
    public void notifier(IEvenement evenement) {
        if(evenement instanceof RachatPart){
            // on ne traite pas les rachats de parts
            return;
        }
        // Ce sera donc forcement une publication ou une diffusion
        Evenement event = (Evenement) evenement;
        Media media = event.getMedia();

        // Si la publication/diffusion mentionne la personne, on l'enregistre
        if (concernePersonne(event, personneASuivre)) {
            historiqueEvenements.add(event);
            compteurMentionsParMedia.put(media, compteurMentionsParMedia.getOrDefault(media, 0) + 1);
        }

        // Et on verifie si c'est un media détenu par la personne
        if (estDetenuParPersonne(media, personneASuivre)) {
            genererAlerteMediaDetenu(event);
        }
    }

    private boolean concernePersonne(Evenement evenement, Personne personne) {
        return evenement.getEntitesMentionnees().contains(personne);
    }

    /**
     * Vérifie si le média est détenu par la personne ou par une de ses organisations
     * @param media le média à vérifier
     * @param personne la personne à vérifier
     * @return true si le média est détenu par la personne ou par une de ses organisations, false sinon
     */
    private boolean estDetenuParPersonne(Media media, Personne personne) {
        // on verifie directement si la personne possede des parts du media
        for (PartPropriete part : personne.getPossessions()) {
            if (part.getCible().equals(media)) {
                return true;
            }

            // et on verifie si les organisations que detient la personne possede des parts du media
            if (part.getCible() instanceof Organisation) {
                Organisation org = (Organisation) part.getCible();
                for (PartPropriete partOrg : org.getPossessions()) {
                    if (partOrg.getCible().equals(media)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Génère une alerte si une publication/diffusion est effectuée par un média détenu par la personne suivie
     * @param evenement
     */
    private void genererAlerteMediaDetenu(Evenement evenement) {
        String message = "Le média " + evenement.getMedia().getNom() +
                " qui est détenu par " + personneASuivre.getNom() +
                " a publié une publication/diffusion : ''" + evenement.getTitre() + "''";

        Alerte alerte = new Alerte(
                message,
                "Nouvelle publication/diffusion d'un média détenu",
                evenement,
                "Module Suivi Personne: " + personneASuivre.getNom()
        );

        vigie.recevoirAlerte(alerte);
    }

    /**
     * Calcul le pourcentage de mentions par média
     * @return une map contenant le média et le pourcentage de mentions
     */
    public Map<Media, Double> calculerPourcentageMentionsParMedia() {
        Map<Media, Double> pourcentages = new HashMap<>();
        int totalMentions = compteurMentionsParMedia.values().stream().mapToInt(Integer::intValue).sum();

        if (totalMentions > 0) {
            compteurMentionsParMedia.forEach((media, mentions) -> {
                double pourcentage = (mentions * 100.0) / totalMentions;
                pourcentages.put(media, pourcentage);
            });
        }

        return pourcentages;
    }

    public List<Evenement> getHistoriqueEvenements() {
        return new ArrayList<>(historiqueEvenements);
    }

    public Map<Media, Integer> getCompteurMentionsParMedia() {
        return new HashMap<>(compteurMentionsParMedia);
    }

    public Personne getPersonneASuivre() {
        return personneASuivre;
    }
}