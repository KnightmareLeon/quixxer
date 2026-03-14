package io.github.knightmareleon.features.sets.components.controls;

import java.util.List;

import io.github.knightmareleon.shared.models.Choice;
import io.github.knightmareleon.shared.models.Question;
import javafx.scene.control.Label;

public class EnumerationQuestionDetail extends BaseQuestionDetail {

    public EnumerationQuestionDetail(Question question) {
        super(question);
    }
    
    @Override
    public void setChoices(Question question){
        List<Choice> questionChoices = question.getChoices();
        for(int i = 0; i < questionChoices.size(); i++){
            this.choiceLabels.add(new Label(questionChoices.get(i).getDescription()));
        }
    }
}