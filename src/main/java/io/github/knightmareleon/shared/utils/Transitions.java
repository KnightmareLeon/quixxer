package io.github.knightmareleon.shared.utils;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Transitions {
    public static void standardFadeTransition(Node node){
        Transitions.standardFadeTransition(node, 0.5);
    }

    public static void standardFadeTransition(Node node, double duration){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), node);
        fadeTransition.setFromValue(0.3);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();
    }
}
