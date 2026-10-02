package org.example;

/**
 * Énumération CategorieCombinaison - Définit les types de combinaisons possibles au Yumzee
 * 
 * Cette énumération encapsule les six combinaisons de scoring du jeu Yahtzee/Yumzee.
 * Chaque combinaison possède un nom d'affichage et un index unique pour faciliter
 * la gestion des boutons dans l'interface utilisateur.
 * 
 * Les combinaisons sont ordonnées par complexité croissante :
 * - BRELAN : 3 dés identiques (le plus fréquent)
 * - CARRE : 4 dés identiques 
 * - FULL : 3 identiques + 2 identiques
 * - PETITE_SUITE : 4 dés consécutifs
 * - GRANDE_SUITE : 5 dés consécutifs
 * - YUMZEE : 5 dés identiques (le plus rare)
 * 
 
 */
public enum CategorieCombinaison {
    
    /** 3 dés de même valeur - Score = somme de tous les dés */
    BRELAN("Brelan", 0),
    
    /** 4 dés de même valeur - Score = somme de tous les dés */
    CARRE("Carré", 1),
    
    /** 3 dés identiques + 2 dés identiques - Score = 25 points fixes */
    FULL("Full", 2),
    
    /** 4 dés avec valeurs consécutives - Score = 30 points fixes */
    PETITE_SUITE("Petite suite", 3),
    
    /** 5 dés avec valeurs consécutives - Score = 40 points fixes */
    GRANDE_SUITE("Grande suite", 4),
    
    /** 5 dés de même valeur - Score = 50 points fixes */
    YUMZEE("Yumzee", 5);

    /** Nom d'affichage de la combinaison dans l'interface utilisateur */
    private final String nom;
    
    /** Index unique pour associer la combinaison aux boutons de l'interface (0-5) */
    private final int index;

    /**
     * Constructeur privé de l'énumération
     * @param nom Nom d'affichage de la combinaison
     * @param index Index unique (0-5) pour la gestion des boutons
     */
    CategorieCombinaison(String nom, int index) {
        this.nom = nom;
        this.index = index;
    }

    /**
     * Retourne l'index unique de cette combinaison
     * Utilisé pour associer les boutons dans l'interface (tableau boutonsCombinaison[])
     * 
     * @return L'index de la combinaison (0 pour BRELAN, 1 pour CARRE, etc.)
     */
    public int getIndex() {
        return index;
    }

    /**
     * Retourne le nom d'affichage de cette combinaison
     * Utilisé pour les labels dans l'interface utilisateur
     * 
     * @return Le nom lisible de la combinaison ("Brelan", "Carré", etc.)
     */
    public String getNom() {
        return nom;
    }

    /**
     * Convertit un index en instance de CategorieCombinaison
     * 
     * Méthode utilitaire pour retrouver quelle combinaison correspond à un bouton cliqué.
     * Utilisée dans les gestionnaires d'événements pour identifier la combinaison choisie.
     * 
     * @param index L'index à convertir (doit être entre 0 et 5 inclus)
     * @return L'instance de CategorieCombinaison correspondante
     * @throws IllegalArgumentException si l'index est invalide (< 0 ou > 5)
     * 
     * Exemple d'utilisation :
     * - fromIndex(0) → BRELAN
     * - fromIndex(3) → PETITE_SUITE
     * - fromIndex(7) → IllegalArgumentException
     */
    public static CategorieCombinaison fromIndex(int index) {
        for (CategorieCombinaison cp : values()) {
            if (cp.getIndex() == index) {
                return cp;
            }
        }
        throw new IllegalArgumentException("Index invalide: " + index);
    }
}