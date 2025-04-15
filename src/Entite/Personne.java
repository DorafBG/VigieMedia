package Entite;

import java.util.*;

public class Personne extends Entite{
    protected List<PartPropriete> possessions = new ArrayList<>();

    public Personne(String nom){
        super(nom);
    }

    public void ajouterPart(PartPropriete part) {
        possessions.add(part);
    }

    public List<PartPropriete> getPossessions() {
        return possessions;
    }
}
