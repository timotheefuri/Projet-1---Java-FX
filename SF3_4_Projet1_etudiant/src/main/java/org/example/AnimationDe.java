package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.Random;


public class AnimationDe {
    double vitesseIni = 10;
    double constanteK = 1.5;
    double temps;
    double dt = 0.016;

    public Object boutonDe;
    double integrale;
    Random random = new Random();


    public void lancer(De de) {

        if (de.isGarde()) {
            return;
        }

        temps = 0;
        integrale = 0;

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(16), event -> {
            double w = vitesseIni*Math.pow(Math.E,-constanteK*temps);

            integrale += w*dt;

            if (integrale > 0.7) {
                int visage = random.nextInt(6)+1;
                de.afficherFace(visage);
                integrale = 0;
            }

            if (w < 0.2) {
                timeline.stop();
                de.lancer();
            }

            temps += dt;
        }));

        timeline.play();
    }

}
