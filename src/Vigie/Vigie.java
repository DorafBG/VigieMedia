package Vigie;

import Entite.Entite;
import Observateur.IObservateur;
import Evenement.Evenement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de la Vigie qui va pouvoir suivre des modules pour surveiller les mentions
 * dans la presse de personnes / groupes qui peuvent posséder certains titres de presse.
 * Cette classe est un Singleton, elle ne pourra donc pas être instanciée plusieurs fois.
 */
public class Vigie {
    /**
     * Instance unique de la Vigie
     */
    private static Vigie instance;
    private final List<Alerte> alertes;

    private Vigie() {
        alertes = new ArrayList<>();
    }

    /**
     * Permet de récupérer l'instance de la Vigie. Si elle n'existe pas, elle sera créée
     * (patron de conception Singleton)
     * @return l'instance unique de la Vigie
     */
    public static Vigie getInstance() {
        if (instance == null) {
            instance = new Vigie();
        }
        return instance;
    }

    /**
     * Reçoit des alertes des modules spécialisés
     * @param alerte L'alerte reçue
     */
    public void recevoirAlerte(Alerte alerte) {
        alertes.add(alerte);
        publierAlerte(alerte);
    }

    /**
     * Publie une alerte dans la console
     * @param alerte L'alerte à publier
     */
    private void publierAlerte(Alerte alerte) {
        System.out.println("\n******* ALERTE VIGIE *******");
        System.out.println(alerte);
        System.out.println("***************************\n");
    }

    /**
     * Retourne l'historique des alertes
     * @return La liste des alertes
     */
    public List<Alerte> getAlertes() {
        return new ArrayList<>(alertes);
    }

}