package Observateur;
import Evenement.IEvenement;

/**
 * Interface pour les observateurs (modules spécialisés)
 */
public interface IObservateur {
    void notifier(IEvenement evenement);
}