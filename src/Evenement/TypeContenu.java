package Evenement;

/**
 * Enumeration permettant de représenter tous les types de publications/diffusions
 */
public enum TypeContenu {
    ARTICLE,
    REPORTAGE,
    INTERVIEW;

    TypeContenu() {
    }

    /**
     * Permet d'obtenir tous les types de médias compatibles avec ce type de contenu
     * @return une liste des médias compatibles
     */
    public String[] getTypesMediaCompatibles() {
        switch (this) {
            case ARTICLE:
                return new String[]{"Presse (généraliste  politique  économique)", "Site"};
            case REPORTAGE:
                return new String[]{"Radio", "Télévision"};
            case INTERVIEW:
                return new String[]{"Radio", "Télévision", "Presse (généraliste  politique  économique)", "Site"};
            default:
                return new String[]{};
        }
    }

    /**
     * Vérifie si le type de contenu est compatible avec le type de média donné
     * @param typeMedia le type de media à vérifier
     * @return true si le type de média est compatible, false sinon
     */
    public boolean estCompatibleAvec(String typeMedia){
        String[] typesCompatibles = getTypesMediaCompatibles();
        for (String type : typesCompatibles) {
            if (type.equals(typeMedia)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        switch (this) {
            case ARTICLE:
                return "Article";
            case REPORTAGE:
                return "Reportage";
            case INTERVIEW:
                return "Interview";
            default:
                return super.toString();
        }
    }
}