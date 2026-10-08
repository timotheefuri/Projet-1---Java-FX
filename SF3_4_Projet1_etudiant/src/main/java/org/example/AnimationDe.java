package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

/**
 * Animation du lancer d'un dé
 *
 * Équation:dw/dt = -k·w
 * (la vitesse w diminue proportionnellement à elle-même)
 * Solution analytique:w(t) = w0·e^(-kt)
 *
 * Chaque fois qu'elle dépasse
 * seuil, la face affichée change. Les changements sont donc rapides au début (w grand)
 * puis de plus en plus espacés, jusqu'à l'arrêt quand w(t) < vm.
 *
 * Durée de l'animation : T = ln(w0 / vm) / k = ln(30 / 0,2) / 6 = 0,84 s
 */
public class AnimationDe {
    private static final double vi = 30;      // w0
    private static final double k = 6;       // k
    private static final double vm = 0.2;     // en dessous, le dé s'arrête
    private static final double seuil = 0.7;           // distance entre deux changements de face
    private static final double dt = 0.016;            // pas de temps (16 ms)
    private final Random random = new Random();
    public void lancer(De de,Runnable aLaFin) {
        double[] temps = {0};
        double[] integrale = {0};
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double w = vi * Math.exp(-k * temps[0]); // w(t) = w(t) = w0·e^(-kt)
            integrale[0] += w * dt;                                     // ∫ω dt cumulée

            if (integrale[0] > seuil) {
                de.afficherFace(random.nextInt(6) + 1);
                integrale[0] = 0;
            }
            if (w < vm) {
                timeline.stop();
                de.afficherFace(de.getValeur()); // face finale = vraie valeur
                aLaFin.run();
            }
            temps[0] += dt;
        }));

        timeline.play();
    }
}