package io.github.knightmareleon.features.sets;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SetsController{

    @FXML private StackPane mainContainer;

    private SetsNavigator navigator;

    @FXML
    public void initialize() {
        navigator = new SetsNavigator(mainContainer);

        navigator.show("main");

        System.out.println("Sets loaded");
    }

    public SetsNavigator getNavigator(){
        return this.navigator;
    }
}