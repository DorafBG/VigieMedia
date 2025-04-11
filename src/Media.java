public class Media {
    private String nom;
    private String type;
    private String periodicite;
    private String echelle;
    private boolean estPayant;
    private boolean estDisparu;

    public Media(String nom, String type, String periodicite, String echelle, boolean estPayant, boolean estDisparu){
        this.nom = nom;
        this.type = type;
        this.periodicite = periodicite;
        this.echelle = echelle;
        this.estPayant = estPayant;
        this.estDisparu = estDisparu;
    }
}
