package io.github.knightmareleon.features.test;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;

public class TestController {

    private final AppContext context;

    public TestController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        System.out.println("Test loaded");
    }
}