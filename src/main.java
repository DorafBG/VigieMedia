import Entite.*;
import Evenement.*;
import Gestionnaire.*;
import Module.*;
import Outils.Parser;
import Vigie.*;
import Simulation.Simulateur;

import java.util.*;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        // On charge toutes les entités
        Map<String, Organisation> organisations = Parser.readOrganisations();
        Map<String, Personne> personnes = Parser.readPersonnes();
        Map<String, Media> medias = Parser.readMedias();

        // puis on charge les relations entre elles (et on met à jour leurs possessions)
        List<PartPropriete> relationsOrgMedia = Parser.updateAndReadRelations("sources/organisation-media.tsv", organisations, medias);
        List<PartPropriete> relationsOrgOrg = Parser.updateAndReadRelations("sources/organisation-organisation.tsv", organisations, organisations);
        List<PartPropriete> relationsPersMedia = Parser.updateAndReadRelations("sources/personne-media.tsv", personnes, medias);
        List<PartPropriete> relationsPersOrg = Parser.updateAndReadRelations("sources/personne-organisation.tsv", personnes, organisations);

        // On initialise le gestionnaire d'événements (lié à la vigie)
        GestionnaireEvenements gestionnaire = GestionnaireEvenements.getInstance();

        // On cree 2 modules de suivi
        Personne daniel = personnes.get("Daniel Křetínský");
        ModuleSuiviPersonne moduledaniel = new ModuleSuiviPersonne(daniel);
        Media leMondeMedia = medias.get("Arte");
        ModuleSuiviMedia moduleLeMondeMedia = new ModuleSuiviMedia(leMondeMedia, 5, 25.0,
                personnes, organisations);

        // le gestionnaire s'abonne aux modules
        gestionnaire.abonner(moduledaniel);
        gestionnaire.abonner(moduleLeMondeMedia);

        Simulateur.simulerEvenementsFichier(gestionnaire, medias, personnes, organisations, "sources/evenements.txt");

        // Menu utilisateur
        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;

        while (continuer) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Simuler une publication");
            System.out.println("2. Simuler une diffusion");
            System.out.println("3. Simuler un rachat de parts");
            System.out.println("4. Afficher les statistiques des modules");
            System.out.println("5. Afficher les alertes de la vigie");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            int choix = 0;
            try{
                choix = scanner.nextInt();
            } catch (Exception e){
                System.out.println("Erreur de saisie, veuillez entrer un nombre entier.");
                scanner.nextLine();
                continue;
            }

            scanner.nextLine();

            switch (choix) {
                case 1:
                    Simulateur.simulerPublication(scanner, gestionnaire, medias, personnes, organisations);
                    break;
                case 2:
                    Simulateur.simulerDiffusion(scanner, gestionnaire, medias, personnes, organisations);
                    break;
                case 3:
                    Simulateur.simulerRachatPart(scanner, gestionnaire, personnes, organisations, medias);
                    break;
                case 4:
                    afficherStatistiques(moduledaniel, moduleLeMondeMedia);
                    break;
                case 5:
                    afficherAlertes();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }

        scanner.close();
    }

    private static void afficherStatistiques(ModuleSuiviPersonne modulePersonne, ModuleSuiviMedia moduleMedia) {
        System.out.println("\n===== STATISTIQUES DES MODULES =====");

        // Statistiques du module de suivi de personne
        System.out.println("\n--- Module de suivi de " + modulePersonne.getPersonneASuivre().getNom() + " ---");
        System.out.println("Nombre d'événements liés : " + modulePersonne.getHistoriqueEvenements().size());

        System.out.println("\nMentions par média :");
        Map<Media, Integer> mentionsParMedia = modulePersonne.getCompteurMentionsParMedia();
        if (mentionsParMedia.isEmpty()) {
            System.out.println("Aucune mention dans les médias.");
        } else {
            for (Map.Entry<Media, Integer> entry : mentionsParMedia.entrySet()) {
                System.out.println("- " + entry.getKey().getNom() + ": " + entry.getValue() + " mentions");
            }
        }

        System.out.println("\nPourcentage des mentions par média :");
        Map<Media, Double> pourcentagesParMedia = modulePersonne.calculerPourcentageMentionsParMedia();
        if (pourcentagesParMedia.isEmpty()) {
            System.out.println("Aucune statistique disponible.");
        } else {
            for (Map.Entry<Media, Double> entry : pourcentagesParMedia.entrySet()) {
                System.out.println("- " + entry.getKey().getNom() + ": " + String.format("%.2f", entry.getValue()) + "%");
            }
        }

        // Statistiques du module de suivi de média
        System.out.println("\n--- Module de suivi de " + moduleMedia.getMediaASuivre().getNom() + " ---");

        System.out.println("\nMentions par entité :");
        Map<Entite, Integer> mentionsParEntite = moduleMedia.getCompteurMentionsParEntite();
        if (mentionsParEntite.isEmpty()) {
            System.out.println("Aucune mention d'entité.");
        } else {
            for (Map.Entry<Entite, Integer> entry : mentionsParEntite.entrySet()) {
                System.out.println("- " + entry.getKey().getNom() + ": " + entry.getValue() + " mentions");
            }
        }

        System.out.println("\nHistorique des rachats :");
        List<RachatPart> historiqueRachats = moduleMedia.getHistoriqueRachats();
        if (historiqueRachats.isEmpty()) {
            System.out.println("Aucun rachat de parts enregistré.");
        } else {
            for (RachatPart rachat : historiqueRachats) {
                System.out.println(rachat);
            }
        }

        System.out.println("\nPropriétaires actuels :");
        List<Entite> proprietaires = moduleMedia.getProprietairesActuels();
        if (proprietaires.isEmpty()) {
            System.out.println("Aucun propriétaire enregistré.");
        } else {
            for (Entite proprietaire : proprietaires) {
                if(proprietaire instanceof Personne p){
                    for(PartPropriete part : p.getPossessions()){
                        if(part.getCible().equals(moduleMedia.getMediaASuivre())){
                            System.out.println("- " + p.getNom() + " (" + part.getPourcentage() + "%)");
                        }
                    }
                } else {
                    for(PartPropriete part : ((Organisation) proprietaire).getPossessions()){
                        if(part.getCible().equals(moduleMedia.getMediaASuivre())){
                            System.out.println("- " + proprietaire.getNom() + " (" + part.getPourcentage() + "%)");
                        }
                    }
                }
            }
        }
    }

    private static void afficherAlertes() {
        Vigie vigie = Vigie.getInstance();
        List<Alerte> alertes = vigie.getAlertes();

        if (alertes.isEmpty()) {
            System.out.println("Aucune alerte à afficher.");
            return;
        }

        System.out.println("\n===== HISTORIQUE DES ALERTES =====");
        for (Alerte alerte : alertes) {
            System.out.println(alerte);
            System.out.println("--------------------------------");
        }
    }
}