package io.github.knightmareleon.features.dashboard;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.utils.Transitions;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

    private final DashboardService dbService;

    @FXML private VBox mainContainer;
    @FXML private Label totalSetsLabel;

    public DashboardController(AppContext context){
        this.dbService = context.getDBService();

    }

    @FXML
    public void initialize() {
        System.out.println("Dashboard loaded");
        this.totalSetsLabel.setText("" + dbService.getTotalSets());
        Transitions.standardFadeTransition(this.mainContainer);
    }
}