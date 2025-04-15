package Vigie;

public class Vigie {
    private static Vigie instance;

    private Vigie() {
    }

    // Patron de conception Singleton pour s'assurer qu'il n'y a qu'un seul Vigie qui existe
    public static Vigie getInstance() {
        if (instance == null) {
            instance = new Vigie();
        }
        return instance;
    }
}
