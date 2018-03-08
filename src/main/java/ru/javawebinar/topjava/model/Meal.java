package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Meal {
    public final static AtomicInteger ID = new AtomicInteger(-1);
    private  LocalDateTime dateTime;
    private  int id;
    private  String description;
    private  int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = ID.addAndGet(1);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public static void resetId(){
        ID.set(-1);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
