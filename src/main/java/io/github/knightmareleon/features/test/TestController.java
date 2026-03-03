package io.github.knightmareleon.features.test;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class TestController {

    @FXML private StackPane mainContainer;
    private TestNavigator navigator;
    private final AppContext context;

    public TestController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        this.navigator = new TestNavigator(this.mainContainer, this.context);

        navigator.show("main");

        System.out.println("Tests loaded");
    }
}