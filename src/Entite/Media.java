package Entite;

/**
 * Classe représentant un média.
 */
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

    // getters et setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPeriodicite() {
        return periodicite;
    }
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }
    public String getEchelle() {
        return echelle;
    }
    public void setEchelle(String echelle) {
        this.echelle = echelle;
    }
    public boolean isEstPayant() {
        return estPayant;
    }
    public void setEstPayant(boolean estPayant) {
        this.estPayant = estPayant;
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
