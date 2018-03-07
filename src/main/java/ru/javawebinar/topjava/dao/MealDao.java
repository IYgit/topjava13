package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by iy on 07.03.18.
 */
public interface MealDao {
    public void addMeal(Meal meal);

    public void updateMeal(Meal book);

    public void removeMeal(int id);

    public Meal getMealById(int id);

    public List<Meal> listMeals();
}
