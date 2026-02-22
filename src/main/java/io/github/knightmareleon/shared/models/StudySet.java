package io.github.knightmareleon.shared.models;

import java.time.Instant;
import java.util.List;

public class StudySet {

    private final int id;
    private String title; 
    private String subject;
    private String imgpath;
    private Instant createdOn;
    private Instant lastTakeOn;
    private int totalTakes;
    private final List<Question> questions;

    public StudySet(
        String title, 
        String subject, 
        String imgpath, 
        List<Question> questions) {

            this.id = 0;
            this.title = title;
            this.subject = subject;
            this.imgpath = imgpath;
            this.totalTakes = 0;
            this.questions = questions;
    }

    public StudySet(
        int id,
        String title, 
        String subject, 
        String imgpath, 
        int totalTakes,
        List<Question> questions) {

            this.id = id;
            this.title = title;
            this.subject = subject;
            this.imgpath = imgpath;
            this.totalTakes = totalTakes;
            this.questions = questions;
    }

    public int id(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getSubject(){
        return this.subject;
    }

    public String getimgpath(){
        return this.imgpath;
    }

    public Instant getDateCreatedOn(){
        return this.createdOn;
    }

    public Instant getDataLastTakeOn(){
        return this.lastTakeOn;
    }

    public int totalTakes(){
        return this.totalTakes;
    }

    public List<Question> getQuestions(){
        return this.questions;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public void setImgpath(String imgpath){
        this.imgpath = imgpath;
    }

    public void setDateCreatedOn(){
        this.createdOn = Instant.now();
    }

    public void setDateLastCreatedOn(){
        this.lastTakeOn = Instant.now();
    }

    public void incrementTakes(){
        this.totalTakes++;
    }
}
