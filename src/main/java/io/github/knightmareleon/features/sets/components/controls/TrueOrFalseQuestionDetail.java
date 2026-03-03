package io.github.knightmareleon.features.sets.components.controls;

import io.github.knightmareleon.shared.models.Question;
import javafx.scene.control.Label;

public class TrueOrFalseQuestionDetail extends BaseQuestionDetail {

    public TrueOrFalseQuestionDetail(Question question) {
        super(question);
    }


    @Override
    public void setChoices(Question question) {
        this.choiceLabels.add(new Label("True"));
        this.choiceLabels.add(new Label("False"));
        this.choiceLabels.get(question.getAnswerIndices().get(0) == 1 ? 0 : 1).setStyle(
            "-fx-text-fill: green !important"
        );
    }
    
}
