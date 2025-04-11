package Util;

import Entite.Organisation;
import Entite.Media;
import Entite.Personne;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    public static List<Organisation> readOrganisations() {
        String path = "sources/organisations.tsv";
        List<Organisation> organisations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // on saute la premiere ligne
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length >= 2) {
                    String nom = parts[0];
                    String commentaire = parts[1];
                    if(commentaire == null || commentaire.equals("")){
                        organisations.add(new Organisation(nom));
                    } else {
                        organisations.add(new Organisation(nom, commentaire));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier organisations : " + e.getMessage());
        }
        return organisations;
    }

    public static List<Media> readMedias() {
        String path = "sources/medias.tsv";
        List<Media> medias = new ArrayList<>();

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
                    medias.add(new Media(nom,type,periodicite,echelle,estPayant,estDisparu));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier medias : " + e.getMessage());
        }
        return medias;
    }

    public static List<Personne> readPersonnes() {
        String path = "sources/personnes.tsv";
        List<Personne> personnes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // on saute la premiere ligne
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t", -1);
                if (parts.length >= 1) {
                    String nom = parts[0];
                    personnes.add(new Personne(nom));
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier personnes : " + e.getMessage());
        }
        return personnes;
    }


}
