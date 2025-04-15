package Vigie;

/**
 * Classe de la Vigie qui va pouvoir suivre des modules pour surveiller les mentions
 * ans la presse de personnes / groupes qui peuvent posséder certains titres de presse.
 * Cette classe est un Singleton, elle ne pourra donc pas être instanciée plusieurs fois.
 */
public class Vigie {
    private static Vigie instance;

    private Vigie() {
    }

    /**
     * Permet de récupérer l'instance de la Vigie. Si elle n'existe pas, elle sera créée
     * (patron de conception Singleton)
     * @return
     */
    public static Vigie getInstance() {
        if (instance == null) {
            instance = new Vigie();
        }
        return instance;
    }
}
