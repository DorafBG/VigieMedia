package Module;

import Entite.*;
import Evenement.*;
import Observateur.IObservateur;
import Vigie.Alerte;
import Vigie.Vigie;

import java.util.*;

/**
 * Module qui suit les mentions d'un média spécifique
 * et génère des alertes en cas de surreprésentation d'entités ou de nouveaux propriétaires.
 */
public class ModuleSuiviMedia implements IObservateur {

    /**
     * Le media suivi par le module
     */
    private final Media mediaASuivre;

    /**
     * Une liste de paire <Entite, Entier> permettant de connaitre
     * le nombre de mentions d'une entité dans le média
     */
    private final Map<Entite, Integer> compteurMentionsParEntite;

    /**
     * Historique des rachats de parts du média
     */
    private final List<RachatPart> historiqueRachats;

    /**
     * Liste des propriétaires (complets ou partiels) actuels du média
     */
    private final List<Entite> proprietairesActuels;

    /**
     * Seuil de mentions pour commencer à calculer le pourcentage de mentions d'une certaine entité
     */
    private final int seuilNombreMentions;

    /**
     * Seuil de pourcentage de mentions pour déclencher une alerte
     */
    private final double seuilPourcentageMentions;

    /**
     * Instance de la Vigie pour envoyer des alertes
     */
    private final Vigie vigie;

    public ModuleSuiviMedia(Media mediaASuivre, int seuilNombreMentions, double seuilPourcentageMentions) {
        this.mediaASuivre = mediaASuivre;
        this.compteurMentionsParEntite = new HashMap<>();
        this.historiqueRachats = new ArrayList<>();
        this.proprietairesActuels = new ArrayList<>();
        this.seuilNombreMentions = seuilNombreMentions;
        this.seuilPourcentageMentions = seuilPourcentageMentions;
        this.vigie = Vigie.getInstance();

        // Initialiser la liste des propriétaires actuels
        initialiserProprietaires();
    }

    private void initialiserProprietaires() {
        // Cette méthode devrait parcourir toutes les entités et ajouter
        // celles qui possèdent une part du média suivi
    }

    /**
     * Méthode pour notifier les événements concernant le média suivi.
     *
     * @param evenement L'événement à notifier.
     */
    @Override
    public void notifier(IEvenement evenement) {
        // Traitement des publications/diffusions concernant le média
        if (evenement instanceof Publication || evenement instanceof Diffusion) {
            Evenement event = (Evenement) evenement;
            if (event.getMedia().equals(mediaASuivre)) {
                traiterEvenementPublication(event);
            }
        }
        // Traitement des rachats de parts concernant le média
        else if (evenement instanceof RachatPart) {
            RachatPart rachat = (RachatPart) evenement;
            if (rachat.getCible().equals(mediaASuivre)) {
                traiterEvenementRachat(rachat);
            }
        }
    }

    private void traiterEvenementPublication(Evenement evenement) {
        // Mettre à jour le compteur pour chaque entité mentionnée
        for (Entite entite : evenement.getEntitesMentionnees()) {
            int nombreMentions = compteurMentionsParEntite.getOrDefault(entite, 0) + 1;
            compteurMentionsParEntite.put(entite, nombreMentions);

            // Vérifier si le seuil est dépassé
            if (nombreMentions >= seuilNombreMentions) {
                double pourcentage = calculerPourcentageMentions(entite);
                if (pourcentage >= seuilPourcentageMentions) {
                    genererAlerteSurrepresentationEntite(entite, pourcentage, evenement);
                }
            }
        }
    }

    /**
     * Methode pour traiter un événement de rachat de parts.
     * Envoie une alerte si un nouveau propriétaire est détecté.
     * @param rachat un rachat qui concerne le média suivi
     */
    private void traiterEvenementRachat(RachatPart rachat) {
        historiqueRachats.add(rachat);

        // on verifie si c'est un nouveau proprietaire
        Entite acheteur = rachat.getAcheteur();
        if (!proprietairesActuels.contains(acheteur)) {
            proprietairesActuels.add(acheteur);
            genererAlerteNouveauProprietaire(acheteur, rachat);
        }
    }

    /**
     * Méthode pour calculer le pourcentage de mentions d'une entité
     *
     * @param entite L'entité pour laquelle calculer le pourcentage
     * @return Le pourcentage de mentions de l'entité
     */
    private double calculerPourcentageMentions(Entite entite) {
        int totalMentions = compteurMentionsParEntite.values().stream().mapToInt(Integer::intValue).sum();
        int mentionsEntite = compteurMentionsParEntite.getOrDefault(entite, 0);

        if (totalMentions == 0) return 0.0;

        return (mentionsEntite * 100.0) / totalMentions;
    }

    /**
     * Envoie une alerte à la Vigie pour la prévenir qu'une certaine entité a dépasse le seuil pourcentage
     * de mentions.
     * @param entite L'entité mentionnée
     * @param pourcentage Le pourcentage de mentions de l'entité
     * @param evenement L'événement qui a déclenché l'alerte
     */
    private void genererAlerteSurrepresentationEntite(Entite entite, double pourcentage, Evenement evenement) {
        String message = "L'entité " + entite.getNom() +
                " représente " + String.format("%.2f", pourcentage) +
                "% des mentions dans le média " + mediaASuivre.getNom() +
                " (seuil: " + seuilPourcentageMentions + "%)";

        Alerte alerte = new Alerte(
                message,
                "Surreprésentation d'entité",
                evenement,
                "Module Suivi Média: " + mediaASuivre.getNom()
        );

        vigie.recevoirAlerte(alerte);
    }

    /**
     * Envoie une alerte à la Vigie pour la prévenir qu'un nouveau propriétaire a été détecté
     * pour le média suivi.
     * @param proprietaire Le nouveau propriétaire
     * @param rachat Le rachat qui a eu lieu
     */
    private void genererAlerteNouveauProprietaire(Entite proprietaire, RachatPart rachat) {
        String message = "Nouveau propriétaire détecté pour " + mediaASuivre.getNom() +
                " : " + proprietaire.getNom() +
                " a acquis " + rachat.getPourcentage() +
                "% des parts";

        Alerte alerte = new Alerte(
                message,
                "Nouveau propriétaire",
                rachat,
                "Module Suivi Média: " + mediaASuivre.getNom()
        );

        vigie.recevoirAlerte(alerte);
    }

    public Map<Entite, Integer> getCompteurMentionsParEntite() {
        return compteurMentionsParEntite;
    }

    public List<RachatPart> getHistoriqueRachats() {
        return historiqueRachats;
    }

    public List<Entite> getProprietairesActuels() {
        return proprietairesActuels;
    }

    public Media getMediaASuivre() {
        return mediaASuivre;
    }
}