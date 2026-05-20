package io.github.knightmareleon.features.dashboard;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DashboardController {

    private final AppContext context;
    @FXML private VBox mainContainer;

    public DashboardController(AppContext context){
        this.context = context;
    }

    @FXML
    public void initialize() {
        System.out.println("Dashboard loaded");
        Transitions.standardFadeTransition(this.mainContainer);
    }
}