package org.example;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;
import java.util.Random;
public class De {
    private static final Random HASARD = new Random();
    private static final int TAILLE = 64;
    private static final Image[] CACHE_IMAGES = new Image[7];
    private int valeur;
    private boolean garde;
    private ImageView vueImageDe;
    private boolean interactif = true;
    public De() {
        valeur = 1;
        garde = false;
        vueImageDe = new ImageView(chargerImage(1));
        vueImageDe.setFitWidth(TAILLE);
        vueImageDe.setFitHeight(TAILLE);
        vueImageDe.setCursor(Cursor.HAND);
        vueImageDe.setOnMouseClicked(e -> {
            if (interactif) {
                flip();
            }
        });
    }
    public static Image chargerImage(int face) {
        if (CACHE_IMAGES[face] == null) {
            CACHE_IMAGES[face] = new Image(
                    Objects.requireNonNull(De.class.getResourceAsStream("/" + face + ".png"),
                            "Image introuvable : /" + face + ".png"));
        }
        return CACHE_IMAGES[face];
    }
    public int getValeur() {
        return valeur;
    }
    public boolean isGarde() {
        return garde;
    }

    public ImageView getAffichage() {
        return vueImageDe;
    }

    public void lancer() {
        if (!garde) {
            valeur = HASARD.nextInt(6) + 1;
            mettreAJourAffichage();
        }
    }
    private void mettreAJourAffichage() {
        vueImageDe.setImage(chargerImage(valeur));

        if (garde) {
            vueImageDe.setStyle("-fx-effect: dropshadow(gaussian, red, 15, 0.5, 0, 0);");
        } else {
            vueImageDe.setStyle(null);
        }
    }
    public void flip() {
        garde = !garde;
        mettreAJourAffichage();
    }
    public void liberer() {
        garde = false;
        mettreAJourAffichage();
    }
    public void afficherFace(int face) {
        vueImageDe.setImage(chargerImage(face));
    }
    public void terminerAnimation() {
        mettreAJourAffichage();
    }
    public void setInteractif(boolean interactif) {
        this.interactif = interactif;
    }
    public void setValeurSimulee(int valeur) {
        this.valeur = valeur;
    }
}
