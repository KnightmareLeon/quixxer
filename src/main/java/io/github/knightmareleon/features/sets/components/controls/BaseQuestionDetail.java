package io.github.knightmareleon.features.sets.components.controls;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.constants.StandardStyleClass;
import io.github.knightmareleon.shared.models.Question;
import io.github.knightmareleon.shared.ui.controls.IconButton;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public abstract class BaseQuestionDetail extends VBox {
    
    private final HBox questionHeaderContainer = new HBox();
    private final Label questionHeaderLabel = new Label("Question");
    private final Label choicesHeaderLabel = new Label("Choices");

    private final IconButton deleteButton = new IconButton("bi-trash-fill", "icon-base-color" );
    private final Text questionLabel = new Text();
    protected final List<Label> choiceLabels = new ArrayList<>();

    public BaseQuestionDetail(Question question, ObservableDoubleValue widthProperty){
        this.questionHeaderLabel.getStyleClass().add(StandardStyleClass.STANDARD_HEADER_FONT);
        this.choicesHeaderLabel.getStyleClass().add(StandardStyleClass.STANDARD_HEADER_FONT);
        this.questionHeaderLabel.setStyle("-fx-font-weight: bolder !important");
        this.choicesHeaderLabel.setStyle("-fx-font-weight: bolder !important");

        this.questionLabel.setText(question.getDescription());
        this.questionLabel.getStyleClass().add(
            StandardStyleClass.STANDARD_FONT
        );
        this.questionLabel.wrappingWidthProperty().bind(widthProperty);
        this.questionLabel.setFill(Color.WHITE);
        this.questionLabel.setTextAlignment(TextAlignment.JUSTIFY);

        this.initChoices(question);
        for(Label choiceLabel: this.choiceLabels){
            choiceLabel.getStyleClass().add(StandardStyleClass.STANDARD_FONT);
            choiceLabel.getStyleClass().addAll(
                StandardStyleClass.STANDARD_FONT,
                StandardStyleClass.COMPONENT_BG_NO_HOVER
            );
            choiceLabel.setPadding(new Insets(6));
            choiceLabel.setMaxWidth(Double.MAX_VALUE);
            choiceLabel.setBorder(new Border(
            new BorderStroke(Color.TRANSPARENT, 
                BorderStrokeStyle.NONE, 
                new CornerRadii(15), 
                BorderWidths.DEFAULT)));
            choiceLabel.setStyle("-fx-background-radius: 15;" + choiceLabel.getStyle());
        }

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        space.setMaxWidth(Double.MAX_VALUE);

        this.deleteButton.setMinHeight(48);
        this.deleteButton.setMinWidth(48);
        this.deleteButton.getStyleClass().add(
            StandardStyleClass.COMPONENT_BG
        );

        this.questionHeaderContainer.getChildren().addAll(
            this.questionHeaderLabel,
            space,
            this.deleteButton  
        );
        this.getChildren().addAll(
            this.questionHeaderContainer,
            this.questionLabel,
            this.choicesHeaderLabel);
        this.getChildren().addAll(
            this.choiceLabels
        );

        this.setBorder(new Border(
            new BorderStroke(Color.WHITE, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(15), 
                BorderWidths.DEFAULT)));
        this.setPadding(new Insets(24));
        this.setFillWidth(true);
        this.setSpacing(24);
        this.setMaxWidth(Double.MAX_VALUE);
    }

    private void initChoices(Question question){
        this.setChoices(question);
    }

    public abstract void setChoices(Question question);
}