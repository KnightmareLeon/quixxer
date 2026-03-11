package io.github.knightmareleon.features.test;

import io.github.knightmareleon.features.test.components.TestNavigator;
import io.github.knightmareleon.features.test.components.constants.TestPageURL;
import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class TestController {

    @FXML private VBox mainContainer;
    private TestNavigator navigator;
    private final AppContext context;

    public TestController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        this.navigator = new TestNavigator(this.mainContainer, this.context);

        navigator.show(TestPageURL.MAIN);

        System.out.println("Tests loaded");
    }
}