# <u>VigieMedia</u>
## Projet Java - 3ème année école d'ingénieurs en informatique

## Consignes :

**Projet : Vigie.Vigie des médias**

-----
**Contexte**

- Les médias français sont (notamment) la propriété de groupes industriels ou d'individus
- Un groupe industriel peut être pour part la propriété d'un autre groupe industriel ou d'un individu
- Il est important de pouvoir déterminer qui possède et/ou contrôle un média

exemple : Télérama est notamment possédé à :

- 15% par Prisa (Personne morale)
- 19,5% par Xavier Niel (Personne physique)
- 21% par Madison Cox (Personne physique)
- 7,7% par Matthieu Pigasse (Personne physique)
- 4,7% par Daniel Křetínský (Personne physique)



scénario du projet

On souhaite développer une application qui permet de surveiller les mentions dans la presse de personnes / groupes qui peuvent posséder certains titres de presse. On suppose que différents traitements séparés peuvent être réalisés sur ces mentions.
On s'intéressera aussi aux changements dans les part de propriété des titres de presse.

-----
**Jeu de données : Médias français, qui possède quoi ?**

[source des données](https://github.com/mdiplo/Medias_francais)

-----
**Objectifs du projet**

- Modéliser des entités ayant possiblement des liens entre elles et pratiquer les notions vues dans le module (encapsulation, gestion des erreurs, etc.)
- Manipuler des données ouvertes
- Représenter des collections et opérer des traitements (simples) dessus
- Mettre en place un système de transmission de messages (programmation événementielle)
-----
**Cahier des charges**


Application à développer

- modélisation des liens capitalistiques de la presse française
    - utilisation du jeu de données fourni
    - simplification : liste des entités stable
- navigation dans les données
    - visualisation simple sous forme d'interface en mode console
    - choix entre un affichage par ordre lexicographique ou par nombre décroissant de médias / organisations possédés
    - *en option* : développement d'une interface graphique
- simulation d'événements (commandes de l'interface)
    - rachat de parts de propriétés
    - publication/diffusion d'un article (presse écrite), d'un reportage (radio / télévision), d'une interview (radio / télévision / presse écrite)
- traitement d'événements par des modules spécialisés
    - s'intéressent à une liste de personnes / organisations / médias
    - suivent les rachats de parts / mentions
    - peuvent émettre des alertes transmises à des modules "vigies"
-----

**Simulation d'événements**

Rachats de parts de propriété

Les parts de propriétés d'un média / d'une organisation peuvent être rachetées : si le rachat est *cohérent*, redistribue les propriétés et/ou part de propriétés.

Simplification : la liste des entités ne peut pas évoluer.


Publications/diffusions

Publication/diffusion d'un article (presse écrite), d'un reportage (radio / télévision), d'une interview (radio / télévision / presse écrite). Une publication / diffusion est d'un certain type et se fait sur un média particulier. Elle est associée à un ensemble de personnes / organisations / médias qui y sont mentionnées (limitées à celles modélisées).

Simplifications :

- la date de la publication est par défaut celle de la survenue de l'événement
- on les simule par saisie dans l'interface

En option :

- génération aléatoire de publications / diffusions
- lecture / simulation d'une liste prédéfinie de publications / diffusions (ex. dans un fichier, dans une méthode du code)
-----

**Traitement d'événements par des modules spécialisés**

Modules spécialisés

- les modules spécialisés écoutent certains types d'événements, qui portent sur une liste de personnes / organisations / médias : rachats de parts et/ou mentions
- les modules réalisent des traitements qui peuvent leur être spécifiques
- certaines configurations détectées par ces modules peuvent être transmis à un unique module "vigie"


Développement (au minimum) de deux modules spécialisés

1. module de suivi d'une personne particulière
    - maintient un historique des publications / diffusions qui la concerne
    - calcule le pourcentage de mentions par média
    - lance une alerte à la vigie si une publication / diffusion est faite par un média détenu (même partiellement) par la personne
1. module de suivi d'un média particulier
   - maintient une liste du nombre de mentions par personne / organisation / média
   - garde un historique de l'évolution des rachats de parts et des différents propriétaires
   - lance une alerte à la vigie si (au-delà d'un certain nombre de mentions) un pourcentage de mentions pour une personne / une organisation / un média dépasse un seuil, ou si une nouvelle personne / organisation acquiert des parts pour un média pour lequel elle n'en avait pas précédemment (i.e. nouveau propriétaire)
-----

**Vigie.Vigie des médias**

Module vigie

La vigie des médias reçoit certains types d'alertes produites par des modules spécialisés auxquels elle est abonnée. Elle fait une historisation des alertes et les publie.

Simplification : la publication se fait par simple émission dans la console.

-----

**Propagation / traitement des événements / alertes**

Application simple

- Pour simplifier, ce projet ne comporte aucune partie devant être réalisée sous forme d'application distribuée
- L'application prendra la forme d'une unique application où la propagation et le traitement des événements / alertes sera fait en suivant le patron de conception Observateur ([voir par exemple](https://www.baeldung.com/java-observer-pattern)) : un générateur d'événement maintient une liste d'abonnés à des types d'événements auxquels les messages pertinents sont envoyés
-----
**Aspects pratiques**

Contraintes sur la réalisation et le rendu

- Travail à réaliser individuellement
    - parties imposées, parties en option
    - date de rendu à fixer avec vos délégué.es
- Réalisation en langage Java
    - mise en application des principes vus lors du module
    - implémentation d'une interaction simple en mode console
    - gestion appropriée des erreurs
    - documentation du code (JavaDoc)
- Composition du rendu
    - fichier archive avec l'ensemble des sources et une version exécutable de l'application et/ou lien sur système public de gestion de code
    - rapport (environ 5p., format PDF) : description des réalisations, modélisation (diagramme de classe), difficultés rencontrées, informations techniques pour l'exécution du projet
