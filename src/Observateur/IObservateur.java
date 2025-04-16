package Observateur;
import Evenement.IEvenement;

/**
 * Interface pour les observateurs (modules spécialisés)
 */
public interface IObservateur {
    /**
     * Méthode pour notifier l'observateur d'un événement
     * @param evenement l'événement à notifier
     */
    void notifier(IEvenement evenement);
}