import Entite.*;
import Evenement.*;
import Gestionnaire.*;
import Module.*;
import Outils.Parser;
import Vigie.*;

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
        Personne daniel = personnes.get("Xavier Niel");
        ModuleSuiviPersonne moduledaniel = new ModuleSuiviPersonne(daniel);

        Media leMondeMedia = medias.get("Arte");
        ModuleSuiviMedia moduleLeMondeMedia = new ModuleSuiviMedia(leMondeMedia, 5, 25.0,
                personnes, organisations);

        // le gestionnaire s'abonne aux modules
        gestionnaire.abonner(moduledaniel);
        gestionnaire.abonner(moduleLeMondeMedia);

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

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    simulerPublication(scanner, gestionnaire, medias, personnes, organisations);
                    break;
                case 2:
                    simulerDiffusion(scanner, gestionnaire, medias, personnes, organisations);
                    break;
                case 3:
                    simulerRachatPart(scanner, gestionnaire, personnes, organisations, medias);
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

    private static void simulerPublication(Scanner scanner, GestionnaireEvenements gestionnaire,
                                           Map<String, Media> medias, Map<String, Personne> personnes,
                                           Map<String, Organisation> organisations) {
        System.out.println("\n===== SIMULER UNE PUBLICATION =====");

        // Choix du média
        System.out.println("Médias disponibles :");
        for (Media media : medias.values()) {
            System.out.println("- " + media.getNom());
        }
        System.out.print("Nom du média qui publie : ");
        String nomMedia = scanner.nextLine();
        Media media = medias.get(nomMedia);
        if (media == null) {
            System.out.println("Ce média n'existe pas.");
            return;
        }

        // Saisie des infos de la publication dans la console
        System.out.print("Titre de la publication : ");
        String titre = scanner.nextLine();
        System.out.print("Description de la publication : ");
        String source = scanner.nextLine();
        System.out.println("Types de contenu disponibles :");
        for (TypeContenu type : TypeContenu.values()) {
            if(type != TypeContenu.INTERVIEW){ // une publication ne peut pas etre une interview
                System.out.println("- " + type);
            }
        }
        System.out.print("Type de contenu : ");
        String typeString = scanner.nextLine().toUpperCase();
        TypeContenu typeContenu;
        try {
            typeContenu = TypeContenu.valueOf(typeString);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de contenu invalide.");
            return;
        }

        System.out.print("Auteur de la publication : ");
        String auteur = scanner.nextLine();

        // On essaye de créer la publication
        // (peut lancer une exception si le type de contenu n'est pas compatible avec le média)
        Publication publication;
        try{
            publication = new Publication(media, titre, source, typeContenu, auteur);
        } catch (IllegalArgumentException e){
            System.out.println("Erreur lors de la création de la publication : " + e.getMessage());
            return;
        }

        // ON ajoute les entités mentionnées dans la publication
        boolean ajouterEntites = true;
        while (ajouterEntites) {
            System.out.println("\nAjouter une entité mentionnée :");
            System.out.println("1. Personne");
            System.out.println("2. Organisation");
            System.out.println("3. Media");
            System.out.println("0. Terminer");
            System.out.print("Votre choix : ");

            int choixEntite = scanner.nextInt();
            scanner.nextLine();

            switch (choixEntite) {
                case 1:
                    System.out.println("Personnes disponibles :");
                    for (Personne p : personnes.values()) {
                        System.out.println("- " + p.getNom());
                    }
                    System.out.print("Nom de la personne : ");
                    String nomPersonne = scanner.nextLine();
                    Personne personne = personnes.get(nomPersonne);
                    if (personne != null) {
                        publication.ajouterEntiteMentionnee(personne);
                        System.out.println(nomPersonne + " est désormais mentionné(e) dans la publication.");
                    } else {
                        System.out.println("Cette personne n'existe pas.");
                    }
                    break;
                case 2:
                    System.out.println("Organisations disponibles :");
                    for (Organisation org : organisations.values()) {
                        System.out.println("- " + org.getNom());
                    }
                    System.out.print("Nom de l'organisation : ");
                    String nomOrg = scanner.nextLine();
                    Organisation org = organisations.get(nomOrg);
                    if (org != null) {
                        publication.ajouterEntiteMentionnee(org);
                        System.out.println(nomOrg + " est désormais mentionnée dans la publication.");
                    } else {
                        System.out.println("Cette organisation n'existe pas.");
                    }
                    break;
                case 3:
                    System.out.println("Médias disponibles :");
                    for (Media m : medias.values()) {
                        System.out.println("- " + m.getNom());
                    }
                    System.out.print("Nom du média : ");
                    String nomMed = scanner.nextLine();
                    Media med = medias.get(nomMed);
                    if (med != null) {
                        publication.ajouterEntiteMentionnee(med);
                        System.out.println(nomMed + " est désormais mentionné à la publication.");
                    } else {
                        System.out.println("Ce média n'existe pas.");
                    }
                    break;
                case 0:
                    ajouterEntites = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }

        // on publie la publication
        gestionnaire.publierEvenement(publication);
        System.out.println("Publication simulée avec succès :");
        System.out.println(publication);
    }

    private static void simulerDiffusion(Scanner scanner, GestionnaireEvenements gestionnaire,
                                         Map<String, Media> medias, Map<String, Personne> personnes,
                                         Map<String, Organisation> organisations) {
        System.out.println("\n===== SIMULER UNE DIFFUSION =====");

        // Choix du média
        System.out.println("Médias disponibles :");
        for (Media m : medias.values()) {
            System.out.println("- " + m.getNom());
        }
        System.out.print("Nom du média qui diffuse : ");
        String nomMedia = scanner.nextLine();
        Media media = medias.get(nomMedia);
        if (media == null) {
            System.out.println("Ce média n'existe pas.");
            return;
        }

        // saisie des info de la diffusion dans la console
        System.out.print("Titre de la diffusion : ");
        String titre = scanner.nextLine();
        System.out.print("Description de la diffusion : ");
        String description = scanner.nextLine();
        System.out.println("Types de contenu disponibles :");
        for (TypeContenu type : TypeContenu.values()) {
            if(type != TypeContenu.ARTICLE){ // une diffusion ne peut pas etre un article
                System.out.println("- " + type);
            }
        }
        System.out.print("Type de contenu : ");
        String typeStr = scanner.nextLine().toUpperCase();
        TypeContenu typeContenu;
        try {
            typeContenu = TypeContenu.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de contenu invalide.");
            return;
        }

        System.out.print("Durée de la diffusion (en minutes) : ");
        double duree = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Animateur de la diffusion : ");
        String animateur = scanner.nextLine();

        // Créeation de la diffusion
        // (peut lancer une exception si le type de contenu n'est pas compatible avec le média)
        Diffusion diffusion;
        try{
            diffusion = new Diffusion(media, titre, description, typeContenu, duree, animateur);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la création de la diffusion : " + e.getMessage());
            return;
        }

        // On ajoute les entites mentionnées dans la diffusion
        boolean ajouterEntites = true;
        while (ajouterEntites) {
            System.out.println("\nAjouter une entité mentionnée :");
            System.out.println("1. Personne");
            System.out.println("2. Organisation");
            System.out.println("3. Media");
            System.out.println("0. Terminer");
            System.out.print("Votre choix : ");

            int choixEntite = scanner.nextInt();
            scanner.nextLine();

            switch (choixEntite) {
                case 1:
                    System.out.println("Personnes disponibles :");
                    for (Personne p : personnes.values()) {
                        System.out.println("- " + p.getNom());
                    }
                    System.out.print("Nom de la personne : ");
                    String nomPersonne = scanner.nextLine();
                    Personne personne = personnes.get(nomPersonne);
                    if (personne != null) {
                        diffusion.ajouterEntiteMentionnee(personne);
                        System.out.println(nomPersonne + " est désormais mentionné(e) dans la diffusion.");
                    } else {
                        System.out.println("Cette personne n'existe pas.");
                    }
                    break;
                case 2:
                    System.out.println("Organisations disponibles :");
                    for (Organisation org : organisations.values()) {
                        System.out.println("- " + org.getNom());
                    }
                    System.out.print("Nom de l'organisation : ");
                    String nomOrg = scanner.nextLine();
                    Organisation org = organisations.get(nomOrg);
                    if (org != null) {
                        diffusion.ajouterEntiteMentionnee(org);
                        System.out.println(nomOrg + " est désormais mentionnée dans cette diffusion.");
                    } else {
                        System.out.println("Cette organisation n'existe pas.");
                    }
                    break;
                case 3:
                    System.out.println("Médias disponibles :");
                    for (Media m : medias.values()) {
                        System.out.println("- " + m.getNom());
                    }
                    System.out.print("Nom du média : ");
                    String nomMed = scanner.nextLine();
                    Media med = medias.get(nomMed);
                    if (med != null) {
                        diffusion.ajouterEntiteMentionnee(med);
                        System.out.println(nomMed + " est désormais mentionné dans la diffusion.");
                    } else {
                        System.out.println("Ce média n'existe pas.");
                    }
                    break;
                case 0:
                    ajouterEntites = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }

        // on publie la diffusion
        gestionnaire.publierEvenement(diffusion);
        System.out.println("Diffusion simulée avec succès :");
        System.out.println(diffusion);
    }

    private static void simulerRachatPart(Scanner scanner, GestionnaireEvenements gestionnaire,
                                          Map<String, Personne> personnes, Map<String, Organisation> organisations,
                                          Map<String, Media> medias) {
        System.out.println("\n===== SIMULER UN RACHAT DE PARTS =====");

        // Choix de l'acheteur
        System.out.println("Choisir l'acheteur :");
        System.out.println("1. Une personne");
        System.out.println("2. Une organisation");
        System.out.print("Votre choix : ");
        int choixAcheteur = scanner.nextInt();
        scanner.nextLine();

        Entite acheteur = null;
        if (choixAcheteur == 1) {
            System.out.println("Personnes disponibles :");
            for (Personne p : personnes.values()) {
                System.out.println("- " + p.getNom());
            }
            System.out.print("Nom de la personne acheteur : ");
            String nomPersonne = scanner.nextLine();
            acheteur = personnes.get(nomPersonne);
        } else if (choixAcheteur == 2) {
            System.out.println("Organisations disponibles :");
            for (Organisation org : organisations.values()) {
                System.out.println("- " + org.getNom());
            }
            System.out.print("Nom de l'organisation acheteur : ");
            String nomOrg = scanner.nextLine();
            acheteur = organisations.get(nomOrg);
        }

        if (acheteur == null) {
            System.out.println("Acheteur invalide.");
            return;
        }

        // Choix du vendeur
        System.out.println("Choisir le vendeur :");
        System.out.println("1. Une personne");
        System.out.println("2. Une organisation");
        System.out.print("Votre choix : ");
        int choixVendeur = scanner.nextInt();
        scanner.nextLine();

        Entite vendeur = null;
        if (choixVendeur == 1) {
            System.out.println("Personnes disponibles :");
            for (Personne p : personnes.values()) {
                System.out.println("- " + p.getNom());
            }
            System.out.print("Nom de la personne vendeur : ");
            String nomPersonne = scanner.nextLine();
            vendeur = personnes.get(nomPersonne);
        } else if (choixVendeur == 2) {
            System.out.println("Organisations disponibles :");
            for (Organisation org : organisations.values()) {
                System.out.println("- " + org.getNom());
            }
            System.out.print("Nom de l'organisation vendeur : ");
            String nomOrg = scanner.nextLine();
            vendeur = organisations.get(nomOrg);
        }

        if (vendeur == null) {
            System.out.println("Vendeur invalide.");
            return;
        }

        // Choix de la cible
        System.out.println("Choisir la cible :");
        System.out.println("1. Une organisation");
        System.out.println("2. Un média");
        System.out.print("Votre choix : ");
        int choixCible = scanner.nextInt();
        scanner.nextLine();

        Entite cible = null;
        if (choixCible == 1) {
            System.out.println("Organisations disponibles :");
            for (Organisation org : organisations.values()) {
                System.out.println("- " + org.getNom());
            }
            System.out.print("Nom de l'organisation cible : ");
            String nomOrg = scanner.nextLine();
            cible = organisations.get(nomOrg);
        } else if (choixCible == 2) {
            System.out.println("Médias disponibles :");
            for (Media m : medias.values()) {
                System.out.println("- " + m.getNom());
            }
            System.out.print("Nom du média cible : ");
            String nomMedia = scanner.nextLine();
            cible = medias.get(nomMedia);
        }

        if (cible == null) {
            System.out.println("Cible invalide.");
            return;
        }

        // Pourcentage à racheter
        System.out.print("Pourcentage à racheter (entre 0 et 100) : ");
        double pourcentage = scanner.nextDouble();
        scanner.nextLine();

        if (pourcentage <= 0 || pourcentage > 100) {
            System.out.println("Pourcentage invalide.");
            return;
        }

        // On créé le rachat et on le traite
        RachatPart rachat = new RachatPart(acheteur, vendeur, cible, pourcentage);
        boolean succes = GestionnaireRachat.traiterRachat(rachat);

        if (succes) {
            // si le rachat a eu lieu, on publie l'evenement
            gestionnaire.publierEvenement(rachat);
            System.out.println("Rachat de parts simulé avec succès :");
            System.out.println(rachat);
        } else {
            System.out.println("Le rachat de parts a échoué.");
        }
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