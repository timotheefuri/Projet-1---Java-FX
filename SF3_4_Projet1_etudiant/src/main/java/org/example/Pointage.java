package org.example;

/**
 * Classe Pointage - Gère le système de calcul et de suivi des points pour le jeu Yumzee
 *
 * Cette classe est responsable de :
 * - Calculer les scores selon les règles du Yahtzee/Yumzee
 * - Maintenir le total des points accumulés durant la partie
 * - Fournir des méthodes utilitaires pour analyser les combinaisons de dés
 *
 * Règles de scoring :
 * - Brelan (3 identiques) : Somme de tous les dés
 * - Carré (4 identiques) : Somme de tous les dés
 * - Full (3+2 identiques) : 25 points fixes
 * - Petite Suite (4 consécutifs) : 30 points fixes
 * - Grande Suite (5 consécutifs) : 40 points fixes
 * - Yumzee (5 identiques) : 50 points fixes
 *

 */
public class Pointage {

    /** Score total accumulé durant la partie */
    private Integer totalPoints;

    /**
     * Constructeur - Initialise le score total à zéro
     */
    public Pointage() {
        totalPoints = 0;
    }

    /**
     * Retourne le score total actuel
     * @return Le nombre total de points accumulés
     */
    public Integer getTotalPoints() {
        return totalPoints;
    }

    /**
     * Définit directement le score total (utilisé pour remise à zéro)
     * @param points Le nouveau score total
     */
    public void setTotalPoints(int points) {
        totalPoints = points;
    }

    /**
     * Ajoute des points au score total existant
     * @param points Le nombre de points à ajouter (peut être 0)
     */
    public void ajoutePoints(int points) {
        totalPoints += points;
    }

    /**
     * Calcule la somme de tous les dés (utilisé pour Brelan et Carré)
     * @param des Tableau des 5 dés
     * @return La somme totale des valeurs des dés
     */
    private int sommeDes(De[] des) {
        int total = 0;

        for (int i = 0; i < des.length; i++) {
            total += des[i].getValeur();
        }

        return total;
    }

    /**
     * Vérifie si les dés contiennent une suite de longueur donnée
     * Une suite est une séquence de valeurs consécutives (ex: 1-2-3-4 ou 2-3-4-5-6)
     *
     * @param compteVals Tableau de comptage des occurrences [0, nb_de_1, nb_de_2, ..., nb_de_6]
     * @param longueur Longueur de suite recherchée (4 pour petite suite, 5 pour grande suite)
     * @return true si une suite de la longueur demandée existe, false sinon
     */
    private boolean contientSuite(int[] compteVals, int longueur) {
        int suite = 0;

        // Parcourt les valeurs 1 à 6 pour détecter une séquence
        for (int i = 1; i <= 6; i++) {
            if (compteVals[i] > 0) {
                suite++; // Valeur présente, continue la séquence
                if (suite >= longueur)
                    return true; // Suite trouvée
            } else {
                suite = 0; // Valeur absente, remet le compteur à zéro
            }
        }

        return false;
    }

    /**
     * Méthode principale pour calculer le score d'une combinaison donnée
     *
     * @param categorie Le type de combinaison à évaluer (BRELAN, CARRE, etc.)
     * @param des Tableau des 5 dés avec leurs valeurs actuelles
     * @return Le nombre de points obtenus (0 si la combinaison n'est pas réalisée)
     */
    public int calculerScore(CategorieCombinaison categorie, De[] des) {

        // Créer un tableau pour compter les occurrences de chaque valeur
        // Index 0 non utilisé, index 1-6 pour les valeurs des dés
        int[] compteValeurs = new int[7];

        // Compter combien de fois chaque valeur apparaît
        for (int i = 0; i < des.length; i++) {
            compteValeurs[des[i].getValeur()]++;
        }

        // Évaluer selon le type de combinaison demandé
        switch (categorie) {
            case BRELAN:
                // 3 dés identiques ou plus → somme de tous les dés
                return contientNbFaces(compteValeurs, 3) ? sommeDes(des) : 0;

            case CARRE:
                // 4 dés identiques ou plus → somme de tous les dés
                return contientNbFaces(compteValeurs, 4) ? sommeDes(des) : 0;

            case FULL:
                // 3 identiques ET 2 identiques → 25 points fixes
                return (contientNbFaces(compteValeurs, 3) && contientNbFaces(compteValeurs, 2)) ? 25 : 0;

            case PETITE_SUITE:
                // 4 valeurs consécutives → 30 points fixes
                return contientSuite(compteValeurs, 4) ? 30 : 0;

            case GRANDE_SUITE:
                // 5 valeurs consécutives → 40 points fixes
                return contientSuite(compteValeurs, 5) ? 40 : 0;

            case YUMZEE:
                // 5 dés identiques → 50 points fixes
                return contientNbFaces(compteValeurs, 5) ? 50 : 0;

            default:
                return 0;
        }
    }

    /**
     * Vérifie si au moins une valeur de dé apparaît un nombre minimum de fois
     * Utilisé pour détecter les brelans, carrés et Yumzee
     *
     * @param compteVals Tableau de comptage des occurrences de chaque valeur
     * @param compte Nombre minimum d'occurrences recherché
     * @return true si au moins une valeur apparaît le nombre de fois demandé
     *
     * Exemples :
     * - contientNbFaces([0,3,1,1,0,0,0], 3) → true (trois 1)
     * - contientNbFaces([0,2,2,1,0,0,0], 3) → false (pas de triplet)
     */
    //hello tim
    private boolean contientNbFaces(int[] compteVals, int compte) {

        for (int i = 0; i < compteVals.length; i++) {
            if (compteVals[i] >= compte) {
                return true;
            }
        }

        return false;
    }
}