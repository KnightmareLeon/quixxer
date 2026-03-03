package io.github.knightmareleon.features.sets.components.controls;

import java.util.List;

import io.github.knightmareleon.shared.models.Question;
import javafx.scene.control.Label;

public class IdentificationQuestionDetail extends BaseQuestionDetail {

    public IdentificationQuestionDetail(Question question) {
        super(question);
    }
    
    @Override
    public void setChoices(Question question){
        List<String> questionChoices = question.getChoices();
        List<Integer> answers = question.getAnswerIndices();
        int answerIndex = 0;
        for(int i = 0; i < questionChoices.size(); i++){
            this.choiceLabels.add(new Label(questionChoices.get(i)));
            if(answers.get(answerIndex) == i){
                this.choiceLabels.get(i).setStyle(
                    "-fx-text-fill: green !important"
                );
                if(answerIndex < answers.size() - 1){
                    answerIndex++;
                }
            }
        }
    }
}
