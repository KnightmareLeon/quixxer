package io.github.knightmareleon.shared.ui.controls;

import io.github.knightmareleon.shared.utils.ControllerRootSetter;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Paginator extends HBox{
    
    @FXML private Button firstPageButton;
    @FXML private Button prevPageButton;
    @FXML private NaturalNumberField pageField;
    @FXML private Label totalPageLabel;
    @FXML private Button nextPageButton;
    @FXML private Button lastPageButton;

    private int totalPages;
    @SuppressWarnings("LeakingThisInConstructor")
    public Paginator(){

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Paginator.fxml")
        );
        ControllerRootSetter.set(this, loader);
        
    }

    public void setFirstPageButtonAction(EventHandler<ActionEvent> eh){
        this.firstPageButton.setOnAction(eh);
    }

    public void setPrevPageButtonAction(EventHandler<ActionEvent> eh){
        this.prevPageButton.setOnAction(eh);
    }

    public void setNextPageButtonAction(EventHandler<ActionEvent> eh){
        this.nextPageButton.setOnAction(eh);
    }

    public void setLastPageButtonAction(EventHandler<ActionEvent> eh){
        this.lastPageButton.setOnAction(eh);
    }

    public void addPageFieldListener(ChangeListener<? super String> listener){
        this.pageField.textProperty().addListener(listener);
    }

    public void init(int totalPages){
        this.totalPages = totalPages;
        this.totalPageLabel.setText("/ " + this.totalPages);
        this.pageField.setMaxNumber(this.totalPages);
        this.firstPageButton.addEventHandler(ActionEvent.ACTION, this::onFirstPageButtonClicked);
        this.prevPageButton.addEventHandler(ActionEvent.ACTION, this::onPrevPageButtonClicked);
        this.nextPageButton.addEventHandler(ActionEvent.ACTION, this::onNextPageButtonClicked);
        this.lastPageButton.addEventHandler(ActionEvent.ACTION, this::onLastPageButtonClicked);
        this.pageField.textProperty().addListener((obs, oldVal, newVal) -> {
            int currentPage = Integer.parseInt(newVal);
            this.disablePrevButtons(currentPage);
            this.disableNextButtons(currentPage);
        });

        this.disablePrevButtons(Integer.parseInt(this.pageField.getText()));
        this.disableNextButtons(Integer.parseInt(this.pageField.getText()));
    }

    public void setTotalPages(int totalPages){
        this.totalPages = totalPages;
        this.totalPageLabel.setText("/ " + this.totalPages);
        this.pageField.setMaxNumber(this.totalPages);
    }

    private void onFirstPageButtonClicked(ActionEvent e){
        if(e.getEventType() == ActionEvent.ACTION){
            this.pageField.setText("1");
        }
    }

    private void onPrevPageButtonClicked(ActionEvent e){
        if(e.getEventType() == ActionEvent.ACTION){
            int currentPage = Integer.parseInt(this.pageField.getText());
            this.pageField.setText((currentPage - 1) + "");
        }
    }

    private void onNextPageButtonClicked(ActionEvent e){
        if(e.getEventType() == ActionEvent.ACTION){
            int currentPage = Integer.parseInt(this.pageField.getText());
            this.pageField.setText((currentPage + 1) + "");
        }
    }

    private void onLastPageButtonClicked(ActionEvent e){
        if(e.getEventType() == ActionEvent.ACTION){
            this.pageField.setText(this.totalPages + "");
        }
    }

    private void disablePrevButtons(int currentPage){
        if(currentPage == 1){
            this.firstPageButton.setDisable(true);
            this.prevPageButton.setDisable(true);
        } else {
            if(this.firstPageButton.isDisabled() || this.prevPageButton.isDisabled()){
                this.firstPageButton.setDisable(false);
                this.prevPageButton.setDisable(false);
            }
        }
    }

    private void disableNextButtons(int currentPage){
        if(currentPage == this.totalPages){
                this.nextPageButton.setDisable(true);
                this.lastPageButton.setDisable(true);
        } else {
            if(this.nextPageButton.isDisabled() || this.lastPageButton.isDisabled()){
                this.nextPageButton.setDisable(false);
                this.lastPageButton.setDisable(false);
            }
        }
    }
}
