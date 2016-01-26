package com.rn.myplaces.myplaces.com.rn.myplaces.database;

public class Notification {

    private int id;
    private String text;

    public Notification(String text){
        this.text = text;
    }

    public Notification(int id,String text){
        this.text = text;
        this.id = id;
    }
    public Notification(){}

    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

}
