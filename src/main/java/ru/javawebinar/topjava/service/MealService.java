package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

public interface MealService {
    List<Meal> getAll(int userId);

    List<Meal> getFilteredList(int userId, SearchFilter filter);

    Meal get(int id) throws NotFoundException;

    Meal create(Meal meal);

    void delete(int id) throws NotFoundException;

    void update(Meal meal, int id);

}