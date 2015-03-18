package com.gmail.slybarrack.practicelog.app;

/**
 * Created by natalie on 6/11/14.
 */
public class MyLog {

    public static long chronometerTime;

    private int id;
    private String date;
    private String notes;
    private String timer;

    public MyLog(){}

    public MyLog(String date, String notes, String timer){
        super();
        this.date = date;
        this.notes = notes;
        this.timer = timer;
       }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "Log [id= " + id + " date= " + date + " notes= " + notes + " timer = " + timer + "]";
    }
}
