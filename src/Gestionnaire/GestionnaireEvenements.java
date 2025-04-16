package Gestionnaire;

import Evenement.Evenement;
import Evenement.IEvenement;
import Observateur.IGestionnaireEvenement;
import Observateur.IObservateur;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe gèrera tous les modules spécialisés (observateurs) à laquelle elle est abonnée.
 * Cette classe sera un Singleton (comme la Vigie)
 */
public class GestionnaireEvenements implements IGestionnaireEvenement {
    private static GestionnaireEvenements instance;
    private final List<IObservateur> observateurs;
    private final List<IEvenement> historique;

    private GestionnaireEvenements() {
        observateurs = new ArrayList<>();
        historique = new ArrayList<>();
    }

    public static GestionnaireEvenements getInstance() {
        if (instance == null) {
            instance = new GestionnaireEvenements();
        }
        return instance;
    }

    /**
     * Méthode pour ajouter un observateur à la liste des observateurs
     * @param observateur l'observateur à ajouter
     */
    @Override
    public void abonner(IObservateur observateur) {
        if (!observateurs.contains(observateur)) {
            observateurs.add(observateur);
        }
    }

    /**
     * Méthode pour supprimer un observateur de la liste des observateurs
     * @param observateur l'observateur à supprimer
     */
    @Override
    public void desabonner(IObservateur observateur) {
        observateurs.remove(observateur);
    }

    /**
     * Méthode pour notifier tous les observateurs auxquels le Gestionnaire est abonné d'un événement
     * @param evenement l'événement à notifier
     */
    @Override
    public void notifierObservateurs(IEvenement evenement) {
        historique.add(evenement);
        for (IObservateur observateur : observateurs) {
            observateur.notifier(evenement);
        }
    }

    /**
     * Méthode pour publier un événement et notifier les observateurs
     * @param evenement l'événement à publier
     */
    public void publierEvenement(IEvenement evenement) {
        notifierObservateurs(evenement);
    }

    /**
     * Méthode pour récupérer l'historique des événements
     * @return la liste des événements
     */
    public List<IEvenement> getHistorique() {
        return historique;
    }
}