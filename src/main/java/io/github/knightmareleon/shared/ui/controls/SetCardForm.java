package io.github.knightmareleon.shared.ui.controls;

import org.kordamp.ikonli.javafx.FontIcon;

import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class SetCardForm extends HBox{
    
    @FXML private FontIcon img;
    @FXML private Label titleLabel;
    @FXML private Label subjectLabel;
    @FXML private Label questionLabel;
    @FXML private IconButton actionsButton;

    private final String iconLiteral;
    private String title;
    private String subject;
    private int totalQuestions;

    @SuppressWarnings("LeakingThisInConstructor")
    public SetCardForm(String iconLiteral, String title, String subject, int totalQuestions) {

        this.iconLiteral = iconLiteral;
        this.title = title;
        this.subject = subject;
        this.totalQuestions = totalQuestions;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("SetCardForm.fxml")
        );
        
        ControllerRootSetter.set(this, loader);
    }

    @FXML
    public void initialize(){
        this.img.setIconLiteral(this.iconLiteral);
        this.img.getStyleClass().add("icon-base-color");
        this.img.setIconSize(100);
        this.titleLabel.setText(this.title);
        this.subjectLabel.setText(this.subject);
        this.questionLabel.setText(this.totalQuestions + " Questions");

        this.actionsButton.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
        });

        this.actionsButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
        });
    }
}
