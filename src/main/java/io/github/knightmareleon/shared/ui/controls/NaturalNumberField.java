package io.github.knightmareleon.shared.ui.controls;

import java.util.function.UnaryOperator;

import javafx.beans.NamedArg;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.IntegerStringConverter;

public class NaturalNumberField extends TextField {

    private int maxNumber;
    public NaturalNumberField(@NamedArg("maxNumber") int maxNumber) {
        this.maxNumber = maxNumber;
        UnaryOperator<Change> filter = change -> {
            String newText = change.getControlNewText();

            if (!newText.matches("^[1-9][0-9]*$")) {
                return null;
            }

            try {
                long value = Long.parseLong(newText);

                if (value > this.maxNumber ) {
                    change.setRange(0, change.getControlText().length());
                    change.setText(String.valueOf(this.maxNumber ));
                }

            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        };

        this.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 1, filter));
    }

    public NaturalNumberField() {
        this(Integer.MAX_VALUE);
    }

    public void setMaxNumber(int maxNumber){
        if(maxNumber < 1) return;
        this.maxNumber = maxNumber > Integer.MAX_VALUE ? Integer.MAX_VALUE : maxNumber;
    }

    public void setTextToMax(){
        this.setText(""  + this.maxNumber);
    }
}
