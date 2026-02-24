package io.github.knightmareleon.features.sets.components.controls;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SetCardForm extends HBox{
    
    @FXML private FontIcon img;
    @FXML private Label titleLabel;
    @FXML private Label subjectLabel;
    @FXML private Label questionLabel;

    private String iconLiteral;
    private String title;
    private String subject;
    private int totalQuestions;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetCardForm(String iconLiteral, String title, String subject, int totalQuestions) {

        this.iconLiteral = iconLiteral;
        this.title = title;
        this.subject = subject;
        this.totalQuestions = totalQuestions;

        FXMLLoader loader;
        loader = new FXMLLoader(
                getClass().getResource("SetCardForm.fxml")
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
    public void initialize(){
        this.img.setIconLiteral(this.iconLiteral);
        this.img.getStyleClass().add("icon-base-color");
        this.img.setIconSize(100);
        this.titleLabel.setText(this.title);
        this.subjectLabel.setText(this.subject);
        this.questionLabel.setText(this.totalQuestions + " Questions");
    }
}
