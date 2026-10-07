package org.example;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.sql.Time;
import java.util.Random;


public class AnimationDe {
    double vitesseIni;
    double constante;
    double dt = 0.016;
    public Object boutonDe;

    public AnimationDe(Button boutonDe) {
        this.boutonDe = boutonDe;
    }

    public void lancer() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(dt,))



        timeline.play();
    }

}
