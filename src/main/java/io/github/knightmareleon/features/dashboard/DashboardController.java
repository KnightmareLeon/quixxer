package io.github.knightmareleon.features.dashboard;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import javafx.fxml.FXML;

public class DashboardController {

    private final AppContext context;

    public DashboardController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        System.out.println("Dashboard loaded");
    }
}