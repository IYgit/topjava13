package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;

import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    void delete(int id);

    Meal get(int id);

    Collection<Meal> getAll(User user);

    List<Meal> getFilteredList(SearchFilter filter);
}
