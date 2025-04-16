package Gestionnaire;

import Entite.*;
import Evenement.RachatPart;
import java.util.List;

/**
 * Cette classe est responsable de la gestion des rachats de parts entre
 * entités
 */
public class GestionnaireRachat {

    /**
     * Permet de traiter un rachat de parts entre deux entites
     *
     * @param rachat L'objet RachatPart qui contient des info sur le rachat
     * @return true si le rachat a été effectué avec succès, false sinon
     */
    public static boolean traiterRachat(RachatPart rachat) {
        Entite acheteur = rachat.getAcheteur();
        Entite vendeur = rachat.getVendeur();
        Entite cible = rachat.getCible();
        double pourcentage = rachat.getPourcentage();

        // On verifie que le vendeur possede bien la cible avec un pourcentage suffisant
        PartPropriete partVendeur = trouverPartPropriete(vendeur, cible);

        if (partVendeur == null || partVendeur.getPourcentage() < pourcentage) {
            System.out.println("Erreur: " + vendeur.getNom() + " ne possède pas assez de parts de " + cible.getNom());
            return false;
        }

        // On met a jour celle du vendeur apres le rachat
        double nouveauPourcentageVendeur = partVendeur.getPourcentage() - pourcentage;
        if (nouveauPourcentageVendeur <= 0) {
            // si le pourcentage du vendeur est nul ou negatif, on supprime la part
            if (vendeur instanceof Organisation og) {
                og.supprimerPart(partVendeur);
            } else if (vendeur instanceof Personne p) {
                p.supprimerPart(partVendeur);
            }
        } else {
            // sinon on met juste a jour le pourcentage
            partVendeur.setPourcentage(nouveauPourcentageVendeur);
        }

        // On recupere la part de l'acheteur
        PartPropriete partAcheteur = trouverPartPropriete(acheteur, cible);
        if(partAcheteur == null || partAcheteur.getPourcentage() == 100.0){
            // si l'acheteur a deja 100% de la cible, on ne fait rien
            System.out.println("Erreur: " + acheteur.getNom() + " possède déjà 100% de " + cible.getNom());
            return false;
        }

        // On met a jour le nouveau pourcentage
        double nouveauPourcentageAcheteur = partAcheteur.getPourcentage() + pourcentage;
        partAcheteur.setPourcentage(nouveauPourcentageAcheteur);

        System.out.println("Rachat effectué: " + acheteur.getNom() + " a racheté " + pourcentage +
                "% de " + cible.getNom() + " à " + vendeur.getNom());
        return true;
    }

    /**
     * Trouve la part de propriété entre une propriété et une cible.
     * Si elle n'existe pas, un lien est créé avec un pourcentage de 0%.
     *
     * @param proprietaire L'entité propriétaire (Organisation ou Personne)
     * @param cible        L'entité cible (Media ou Organisation)
     * @return La part de propriété en question (null si le proprietaire n'est pas une Organisation ou une Personne)
     */
    private static PartPropriete trouverPartPropriete(Entite proprietaire, Entite cible) {
        List<PartPropriete> possessions;

        if (proprietaire instanceof Organisation) {
            possessions = ((Organisation) proprietaire).getPossessions();
        } else if (proprietaire instanceof Personne) {
            possessions = ((Personne) proprietaire).getPossessions();
        } else {
            return null;
        }

        // on recherche parmi les possessions du proprietaire, si la relation existe
        for (PartPropriete part : possessions) {
            if (part.getCible().equals(cible)) {
                return part;
            }
        }

        // si la relation nexiste pas, on la cree avec un pourcentage de 0%
        PartPropriete nouvellePart = new PartPropriete(proprietaire, cible, 0.0);
        if (proprietaire instanceof Organisation og) {
            og.ajouterPart(nouvellePart);
        } else {
            Personne p = (Personne) proprietaire;
            p.ajouterPart(nouvellePart);
        }

        return nouvellePart;
    }

}