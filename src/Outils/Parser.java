package Outils;

import Entite.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitaire permettant de faire la relation entre l'application et les fichiers sources.
 */
public class Parser {

    /**
     * Permet de parser les organisations à partir des fichiers sources
     * @return une Map avec toutes les organisations en valeurs et leurs noms en clé (utile pour la recherche)
     */
    public static Map<String, Organisation> readOrganisations() {
        String path = "sources/organisations.tsv";
        Map<String, Organisation> organisations = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // on saute la premiere ligne
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length >= 2) {
                    String nom = parts[0];
                    String commentaire = parts[1];
                    if(commentaire == null || commentaire.isEmpty()){
                        organisations.put(nom, new Organisation(nom));
                    } else {
                        organisations.put(nom, new Organisation(nom, commentaire));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier organisations : " + e.getMessage());
        }
        return organisations;
    }

    /**
     * Permet de parser les médias à partir des fichiers sources
     * @return une Map avec tous les médias en valeurs et leurs noms en clé (utile pour la recherche)
     */
    public static Map<String, Media> readMedias() {
        String path = "sources/medias.tsv";
        Map<String, Media> medias = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // on saute la premiere ligne
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length >= 6) {
                    String nom = parts[0];
                    String type = parts[1];
                    String periodicite = parts[2];
                    String echelle = parts[3];
                    String prix = parts[4];
                    String disparu = parts[5];

                    boolean estPayant = false; // gratuit de base
                    boolean estDisparu = false; // n'est pas disparu de base

                    if(periodicite == null || periodicite.isEmpty()){
                        periodicite = "";
                    }
                    if(echelle == null || echelle.isEmpty()){
                        echelle = "";
                    }
                    if(prix.equals("Payant")){
                        estPayant = true;
                    }
                    if(disparu.equals("checked")){
                        estDisparu = true;
                    }
                    medias.put(nom, new Media(nom,type,periodicite,echelle,estPayant,estDisparu));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier medias : " + e.getMessage());
        }
        return medias;
    }

    /**
     * Permet de parser les personnes à partir des fichiers sources
     * @return une Map avec toutes les personnes en valeurs et leurs noms en clé (utile pour la recherche)
     */
    public static Map<String, Personne> readPersonnes() {
        String path = "sources/personnes.tsv";
        Map<String, Personne> personnes = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // on saute la premiere ligne
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length >= 1) {
                    String nom = parts[0];
                    personnes.put(nom, new Personne(nom));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier personnes : " + e.getMessage());
        }
        return personnes;
    }

    /**
     * Permet de lire les relations entre les entités et de mettre à jour les possessions (attributs)
     * de chaque entité origine (Organisation ou Personne)
     * @param path chemin du fichier de relation (en format .tsv)
     * @param originesConnues la Map de toutes les Entite (Organisation ou Personne) origine qui existent
     * @param ciblesConnues la Map de toutes les Entite (Organisation ou Media) cibles qui existent
     * @return
     */
    public static List<PartPropriete> updateAndReadRelations(String path,
                                                             Map<String, ? extends Entite> originesConnues,
                                                             Map<String, ? extends Entite> ciblesConnues) {
        List<PartPropriete> relations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);

                if (parts.length >= 5) {
                    String origineNom = parts[1];
                    String qualificatif = parts[2];
                    String pourcentageString = parts[3].replace("%", "");
                    String cibleNom = parts[4];

                    Entite origine = originesConnues.get(origineNom);
                    Entite cible = ciblesConnues.get(cibleNom);

                    if (origine != null && cible != null) {
                        double pourcentage = 0;
                        if(qualificatif.equals("contrôle")){
                            pourcentage = 100;
                        } else if(qualificatif.equals("inférieur à")){
                            pourcentage = 49;
                        } else if(qualificatif.equals("participe")){
                            pourcentage = 5;
                        } else {
                            pourcentage = Double.parseDouble(pourcentageString);
                        }

                        PartPropriete part = new PartPropriete(origine, cible, pourcentage);
                        relations.add(part);

                        // On met a jour les "possessions" de l'origine
                        if (origine instanceof Organisation org) {
                            org.ajouterPart(part);
                        } else if (origine instanceof Personne pers) {
                            pers.ajouterPart(part);
                        }
                    } else {
                        System.out.println("Origine ou cible introuvable : " + origineNom + " -> " + cibleNom);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + path + " : " + e.getMessage());
        }

        return relations;
    }



}
