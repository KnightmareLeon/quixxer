package io.github.knightmareleon.shared.ui.controls;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;

public class IconToggleButton extends ToggleButton {

    private final FontIcon icon = new FontIcon();

    private final String iconLiteral;
    private final String iconStyleClass;

    @SuppressWarnings("LeakingThisInConstructor")
    public IconToggleButton(@NamedArg("iconLiteral") String iconLiteral, @NamedArg("iconStyleClass") String iconStyleClass) {
        this.iconLiteral = iconLiteral;
        this.iconStyleClass = iconStyleClass;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("IconToggleButton.fxml")
        );

        ControllerRootSetter.set(this, loader);

    }

    @FXML
    public void initialize() {
        this.icon.setIconLiteral(this.iconLiteral);
        this.icon.getStyleClass().add(this.iconStyleClass);
        this.setGraphic(this.icon);
    }
}
