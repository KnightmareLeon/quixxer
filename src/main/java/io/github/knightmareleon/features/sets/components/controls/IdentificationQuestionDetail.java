package io.github.knightmareleon.features.sets.components.controls;

import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.control.Label;

public class IdentificationQuestionDetail extends BaseQuestionDetail {

    public IdentificationQuestionDetail(Question question, ObservableDoubleValue widthProperty) {
        super(question, widthProperty);
    }

    @Override
    public void setChoices(Question question){
        int index = 0;
        for(Choice choice : question.getChoices()){
            this.choiceLabels.add(new Label(choice.getDescription()));
            if(choice.isAnswer()){
                this.choiceLabels.get(index).setStyle(
                    "-fx-text-fill: green !important"
                );
            }
            index++;
        }
    }
}
