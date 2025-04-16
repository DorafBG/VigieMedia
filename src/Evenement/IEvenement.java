package Evenement;

import java.time.LocalDateTime;

/**
 * Interface utilisée pour représenter tous les événements du projet
 * (Publication, Diffusion, RachatPart...)
 */
public interface IEvenement {
    /**
     * Permet d'obtenir la date de l'événement
     * @return la date de l'événément
     */
    LocalDateTime getDate();

    /**
     * Permet d'obtenir la description de l'événement
     * @return la description de l'événément
     */
    String getDescription();

    /**
     * Permet d'obtenir toutes les informations de l'événement sous forme textuel
     * @return la forme textuelle de l'événement
     */
    String toString();
}