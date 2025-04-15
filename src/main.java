import Entite.*;

import java.util.List;
import java.util.Map;

import static Util.Parser.*;

public class main {
    public static void main(String[] args) {
        Map<String, Organisation> ogs = readOrganisations();
        Map<String, Personne> personnes = readPersonnes();
        Map<String, Media> medias = readMedias();

        List<PartPropriete> relationsOrgMedia = updateAndReadRelations("sources/organisation-media.tsv", ogs, medias);
        List<PartPropriete> relationsOrgOrg = updateAndReadRelations("sources/organisation-organisation.tsv", ogs, ogs);
        List<PartPropriete> relationsPersMedia = updateAndReadRelations("sources/personne-media.tsv", personnes, medias);
        List<PartPropriete> relationsPersOrg = updateAndReadRelations("sources/personne-organisation.tsv", personnes, ogs);

        //System.out.println("Relations Organisation-Media :");
        //for (PartPropriete relation : relationsOrgMedia) {
        //    System.out.println(relation);
        //}
        System.out.println("\nRelations Organisation-Organisation :");
        for (PartPropriete relation : relationsOrgOrg) {
            System.out.println(relation);
        }

        // relations de la personne Vincent Bolloré et toutes ses possessions
        System.out.println("\nRelations de la personne :");
        for (Personne p : personnes.values()) {
            if (p.getNom().equals("Vincent Bolloré")) {
                List<PartPropriete> possessions = p.getPossessions();
                for(PartPropriete possession : possessions) {
                    System.out.println("Possessions de " + possession.getCible() + " :");
                    Organisation bollore = (Organisation) possession.getCible();
                    for(PartPropriete po2 : bollore.getPossessions()){
                        System.out.println(po2);
                    }
                }
            }
        }

    }
}
