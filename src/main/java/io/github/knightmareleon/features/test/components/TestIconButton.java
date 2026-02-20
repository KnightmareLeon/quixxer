package io.github.knightmareleon.features.test.components;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TestIconButton extends Button{

    @FXML private FontIcon icon = new FontIcon();
    @FXML private HBox hbox = new HBox();
    @FXML private VBox vbox = new VBox();
    @FXML private Label header = new Label();
    @FXML private Label desc = new Label();

    private final String iconLiteral;
    private final String headerText;
    private final String descText;

    public TestIconButton(@NamedArg("iconLiteral") String iconLiteral, @NamedArg("headerText") String headerText, @NamedArg("descText") String descText) {

        this.iconLiteral = iconLiteral;
        this.headerText = headerText;
        this.descText = descText;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("TestIconButton.fxml")
        );
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        this.icon.setIconLiteral(this.iconLiteral);
        this.icon.getStyleClass().add("test-icon-base");

        this.header.setText(this.headerText);
        this.header.getStyleClass().add("standard-header-font");

        this.desc.setText(this.descText);
        this.desc.getStyleClass().add("standard-font");

        this.hbox.getChildren().addAll(this.icon);
        this.vbox.getChildren().addAll(this.header, this.desc);
        this.hbox.getChildren().addAll(this.vbox);
        this.hbox.setSpacing(24);
        this.vbox.setSpacing(12);

        this.setPadding(new Insets(24));
        this.setGraphic(this.hbox);
    }
}