package io.github.knightmareleon.features.sets;

import io.github.knightmareleon.features.sets.components.SetsNavigator;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class SetsController{

    @FXML private StackPane mainContainer;

    private SetsNavigator navigator;

    private final AppContext context;

    public SetsController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        this.navigator = new SetsNavigator(this.mainContainer, this.context);

        navigator.show("main");

        System.out.println("Sets loaded");
    }

    public SetsNavigator getNavigator(){
        return this.navigator;
    }
}