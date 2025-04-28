package com.example.learn_1;

public class Task {
    private int id;
    private String title;
    private boolean isCompleted;

    public Task(String title, boolean isCompleted) {

        this.title = title;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
