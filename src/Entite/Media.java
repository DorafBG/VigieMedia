package Entite;

public class Media extends Entite {
    private String type;
    private String periodicite;
    private String echelle;
    private boolean estPayant;
    private boolean estDisparu;

    public Media(String nom, String type, String periodicite, String echelle, boolean estPayant, boolean estDisparu){
        super(nom);
        this.type = type;
        this.periodicite = periodicite;
        this.echelle = echelle;
        this.estPayant = estPayant;
        this.estDisparu = estDisparu;
    }

    @Override
    public String toString() {
        String result = nom + " (" + type + ")";

        if (!periodicite.isEmpty()) {
            result += " - " + periodicite;
        }
        if (!echelle.isEmpty()) {
            result += " [" + echelle + "]";
        }
        result += estPayant ? " (Payant)" : " (Gratuit)";
        result += estDisparu ? " - Disparu" : " - Actif";

        return result;
    }



}
