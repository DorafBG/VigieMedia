package Simulation;

import Entite.*;
import Evenement.*;
import Gestionnaire.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Classe Simulateur qui permet de simuler la publication d'un événement, la diffusion d'un événement
 * (ne sert qu'à des fins de tests)
 */
public class Simulateur {
    /**
     * Méthode pour simuler la publication d'un événement
     * @param scanner
     * @param gestionnaire
     * @param medias
     * @param personnes
     * @param organisations
     */
    public static void simulerPublication(Scanner scanner, GestionnaireEvenements gestionnaire,
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

    /**
     * Méthode pour simuler la diffusion d'un événement
     * @param scanner
     * @param gestionnaire
     * @param medias
     * @param personnes
     * @param organisations
     */
    public static void simulerDiffusion(Scanner scanner, GestionnaireEvenements gestionnaire,
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

    /**
     * Méthode pour simuler le rachat d'une part
     * @param scanner
     * @param gestionnaire
     * @param personnes
     * @param organisations
     * @param medias
     */
    public static void simulerRachatPart(Scanner scanner, GestionnaireEvenements gestionnaire,
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


    /**
     * Méthode pour lire et simuler des événements depuis un fichier
     * @param gestionnaire Le gestionnaire d'événements
     * @param medias La liste des médias disponibles
     * @param personnes La liste des personnes disponibles
     * @param organisations La liste des organisations disponibles
     * @param fichier Le chemin vers le fichier d'événements
     */
    public static void simulerEvenementsFichier(GestionnaireEvenements gestionnaire,
                                                Map<String, Media> medias,
                                                Map<String, Personne> personnes,
                                                Map<String, Organisation> organisations,
                                                String fichier) {
        System.out.println("\n===== SIMULATION DES ÉVÉNEMENTS DEPUIS FICHIER =====");

        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            int compteur = 0;

            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty() || ligne.startsWith("#")) { // on ignore les lignes vides ou commentaires
                    continue;
                }

                // on traite la ligne et on cree l'evenement
                Evenement evenement = creerEvenementDepuisLigne(ligne, medias, personnes, organisations);

                // si l'evenement a bien ete cree, on le publie (1 seconde d'intervalle entre chaque evenement)
                if (evenement != null) {
                    gestionnaire.publierEvenement(evenement);
                    compteur++;

                    System.out.println("Événement simulé (" + compteur + "): ");
                    System.out.println(evenement);
                    System.out.println("---------------------------------");

                    // Attendre 1 seconde avant le prochain événement
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Simulation interrompue.");
                        break;
                    }
                }
            }

            System.out.println("Fin de lecture du fichier d'événements. " + compteur + " événements simulés.");

        } catch (FileNotFoundException e) {
            System.out.println("Erreur: Le fichier " + fichier + " n'a pas été trouvé.");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier: " + e.getMessage());
        }
    }

    /**
     * Méthode pour créer un événement à partir d'une ligne du fichier
     * Format attendu:
     * TYPE;MEDIA;TITRE;DESCRIPTION;TYPE_CONTENU;AUTEUR/ANIMATEUR;[DUREE];ENTITES_MENTIONNEES
     */
    private static Evenement creerEvenementDepuisLigne(String ligne,
                                                       Map<String, Media> medias,
                                                       Map<String, Personne> personnes,
                                                       Map<String, Organisation> organisations) {
        String[] parties = ligne.split(";"); // ; est le séparateur

        if (parties.length < 6) {
            System.out.println("Format de ligne invalide: " + ligne);
            return null;
        }

        String type = parties[0].trim().toUpperCase();
        String nomMedia = parties[1].trim();
        String titre = parties[2].trim();
        String description = parties[3].trim();
        String typeContenuStr = parties[4].trim().toUpperCase();
        String auteurAnimateur = parties[5].trim();

        // on verifie si le media existe
        Media media = medias.get(nomMedia);
        if (media == null) {
            System.out.println("Média non trouvé: " + nomMedia);
            return null;
        }

        // on verifie si le type de contenu est valide
        TypeContenu typeContenu;
        try {
            typeContenu = TypeContenu.valueOf(typeContenuStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Type de contenu invalide: " + typeContenuStr);
            return null;
        }

        Evenement evenement = null;

        // On créé l'événement selon son type
        try {
            if (type.equals("PUBLICATION")) {
                evenement = new Publication(media, titre, description, typeContenu, auteurAnimateur);
            } else if (type.equals("DIFFUSION")) {
                if (parties.length < 7) {
                    System.out.println("Format de diffusion invalide, durée manquante: " + ligne);
                    return null;
                }

                double duree;
                try {
                    duree = Double.parseDouble(parties[6].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Durée de diffusion invalide: " + parties[6]);
                    return null;
                }

                evenement = new Diffusion(media, titre, description, typeContenu, duree, auteurAnimateur);
            } else {
                System.out.println("Type d'événement non reconnu: " + type);
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur lors de la création de l'événement: " + e.getMessage());
            return null;
        }

        // Lecture des entités mentionnées
        int indexEntites = type.equals("PUBLICATION") ? 6 : 7;
        if (parties.length > indexEntites) {
            String entitesMentionnees = parties[indexEntites].trim();
            if (!entitesMentionnees.isEmpty()) {
                String[] entites = entitesMentionnees.split("\\|");

                for (String entite : entites) {
                    String[] infoEntite = entite.split(":");
                    if (infoEntite.length != 2) {
                        System.out.println("Format d'entité mentionnée invalide: " + entite);
                        continue;
                    }

                    String typeEntite = infoEntite[0].trim().toUpperCase();
                    String nomEntite = infoEntite[1].trim();

                    switch (typeEntite) {
                        case "PERSONNE":
                            Personne personne = personnes.get(nomEntite);
                            if (personne != null) {
                                evenement.ajouterEntiteMentionnee(personne);
                            } else {
                                System.out.println("Personne non trouvée: " + nomEntite);
                            }
                            break;
                        case "ORGANISATION":
                            Organisation organisation = organisations.get(nomEntite);
                            if (organisation != null) {
                                evenement.ajouterEntiteMentionnee(organisation);
                            } else {
                                System.out.println("Organisation non trouvée: " + nomEntite);
                            }
                            break;
                        case "MEDIA":
                            Media mediaEntite = medias.get(nomEntite);
                            if (mediaEntite != null) {
                                evenement.ajouterEntiteMentionnee(mediaEntite);
                            } else {
                                System.out.println("Média non trouvé: " + nomEntite);
                            }
                            break;
                        default:
                            System.out.println("Type d'entité non reconnu: " + typeEntite);
                    }
                }
            }
        }

        return evenement;
    }
}
