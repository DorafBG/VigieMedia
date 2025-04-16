package Observateur;

import Evenement.IEvenement;

/**
 * Interface pour le gestionnaire d'événements
 */
public interface IGestionnaireEvenement {
    void abonner(IObservateur observateur);
    void desabonner(IObservateur observateur);
    void notifierObservateurs(IEvenement evenement);
}