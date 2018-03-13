package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;

import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    boolean delete(int id);

    Meal get(int id);

    Collection<Meal> getAll(int userId);

    List<Meal> getFilteredList(int userId, SearchFilter filter);
}
