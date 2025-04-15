import Entite.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Util.Parser.*;

public class main {
    public static void main(String[] args) {
        Map<String, ? extends  Organisation> ogs = readOrganisations();
        Map<String, ? extends Entite> personnes = readPersonnes();
        Map<String, ? extends Entite> medias = readMedias();

        List<PartPropriete> relationsOrgMedia = lireRelations("sources/organisation-media.tsv", ogs, medias);
        List<PartPropriete> relationsOrgOrg = lireRelations("sources/organisation-organisation.tsv", ogs, ogs);

        // print
        //System.out.println("Relations Organisation-Media :");
        //for (PartPropriete relation : relationsOrgMedia) {
        //    System.out.println(relation);
        //}
        System.out.println("\nRelations Organisation-Organisation :");
        for (PartPropriete relation : relationsOrgOrg) {
            System.out.println(relation);
        }

        // relations de l'organisation Bayard :
        System.out.println("\nRelations de l'organisation Bayard :");
        for (Organisation og : ogs.values()) {
            if (og.getNom().equals("Bayard")) {
                List<PartPropriete> possessions = og.getPossessions();
                for(PartPropriete possession : possessions) {
                    System.out.println(possession);
                }
            }
        }

    }
}
