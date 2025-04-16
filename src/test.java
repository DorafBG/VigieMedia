import Entite.*;
import Evenement.*;
import Gestionnaire.*;

import java.util.List;
import java.util.Map;

import static Outils.Parser.*;

public class test {
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

        System.out.println("Test d'une Publication :");
        Publication pub = new Publication(medias.values().iterator().next(), "Journal", "Quotidien", TypeContenu.INTERVIEW, "Thomas Pequet");
        System.out.println(pub);

        System.out.println("Test d'une Diffusion :");
        Diffusion dif = new Diffusion(medias.get("CNews"), "Mario Kart", "Le nouveau Mario Kart", TypeContenu.REPORTAGE, 90.0, "Julien Choiu");
        System.out.println(dif);

        System.out.println("\n--- TEST D'UN RACHAT ---");

        Organisation acheteur = ogs.get("Vivendi");
        for(PartPropriete PP : acheteur.getPossessions()){
            System.out.println(PP);
        }
        Organisation vendeur = ogs.get("Groupe Canal+");
        for(PartPropriete PP : vendeur.getPossessions()){
            System.out.println(PP);
        }
        Media cible = medias.get("CNews");

        if (cible != null) {
            RachatPart rachat = new RachatPart(acheteur, vendeur, cible, 10.0); // Vivendi rachete 10% de CNews a Canal+
            GestionnaireRachat.traiterRachat(rachat);
        } else {
            System.out.println("Erreur : entités introuvables pour effectuer le rachat.");
        }
        for(PartPropriete PP : acheteur.getPossessions()){
            System.out.println(PP);
        }
        for(PartPropriete PP : vendeur.getPossessions()){
            System.out.println(PP);
        }

        // On peut aussi essayer
        // Daniel Křetínský a 5% de TF1
        // On parle de lui dans LCI -> lance une alerte (car TF1 a 100% de LCI)


    }
}
