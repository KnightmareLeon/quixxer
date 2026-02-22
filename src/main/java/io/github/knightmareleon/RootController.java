package io.github.knightmareleon;

import io.github.knightmareleon.shared.infrastructure.AppContext;
import io.github.knightmareleon.shared.infrastructure.navigator.RootTabNavigator;
import io.github.knightmareleon.shared.ui.header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class RootController {

    @FXML
    private StackPane contentArea;

    @FXML
    private HeaderController headerController;

    private RootTabNavigator navigator;
    private final AppContext context;

    public RootController(AppContext context){
        this.context = context;
    }
    @FXML
    public void initialize() {

        this.navigator = new RootTabNavigator(this.contentArea, this.context);

        headerController.setOnTabSelected(tabId -> {
            navigator.show(tabId);
        });

        navigator.show("dashboard");
    }
}
