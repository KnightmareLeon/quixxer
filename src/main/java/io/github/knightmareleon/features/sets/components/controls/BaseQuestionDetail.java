package io.github.knightmareleon.features.sets.components.controls;

import java.util.ArrayList;
import java.util.List;

import io.github.knightmareleon.shared.models.Question;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class BaseQuestionDetail extends VBox {
    
    private final Label questionHeaderLabel = new Label("Question");
    private final Label choicesHeaderLabel = new Label("Choices");

    private final Label questionLabel = new Label();
    protected final List<Label> choiceLabels = new ArrayList<>();


    public BaseQuestionDetail(Question question){
        this.questionHeaderLabel.getStyleClass().add("standard-header-font");
        this.choicesHeaderLabel.getStyleClass().add("standard-header-font");
        this.questionHeaderLabel.setStyle("-fx-font-weight: bolder !important");
        this.choicesHeaderLabel.setStyle("-fx-font-weight: bolder !important");

        this.questionLabel.setText(question.getDescription());
        this.questionLabel.getStyleClass().addAll("standard-font","set-standard-bg-no-hover");
        this.questionLabel.setPadding(new Insets(6));
        this.questionLabel.setMaxWidth(Double.MAX_VALUE);
        this.initChoices(question);
        for(Label choiceLabel: this.choiceLabels){
            choiceLabel.getStyleClass().add("standard-font");
            choiceLabel.getStyleClass().addAll("standard-font","set-standard-bg-no-hover");
            choiceLabel.setPadding(new Insets(6));
            choiceLabel.setMaxWidth(Double.MAX_VALUE);
        }
        this.getChildren().addAll(
            this.questionHeaderLabel,
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