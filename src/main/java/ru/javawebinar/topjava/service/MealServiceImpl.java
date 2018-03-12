package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

public class MealServiceImpl implements MealService {
    private MealRepository repository;


    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Meal> getAll(User user) {
        return (List<Meal>) repository.getAll(user);
    }

    @Override
    public List<Meal> getFilteredList(User user, SearchFilter filter) {
        Predicate<Meal> predicate = meal -> filter.getFromDate() == null ? true : meal.getDate().isAfter(filter.getFromDate());
        predicate.and(meal -> filter.getToDate() == null ? true : meal.getDate().isBefore(filter.getToDate()));
        predicate.and(meal -> filter.getFromTime() == null ? true : meal.getTime().isAfter(filter.getFromTime()));
        predicate.and(meal -> filter.getToTime() == null ? true : meal.getTime().isBefore(filter.getToTime()));
        boolean isSearchKey = filter.getSearchKey() != null && !filter.getSearchKey().isEmpty();
        if (isSearchKey){
            String regex = ".*" + filter.getSearchKey() + ".*";
            predicate.and(meal -> meal.getDate().toString().matches(regex));
            predicate.and(meal -> meal.getTime().toString().matches(regex));
            predicate.and(meal -> meal.getDescription().matches(regex));
            predicate.and(meal -> String.valueOf(meal.getCalories()).matches(regex));
        }

        return getAll(user).stream().filter(meal -> predicate.test(meal)).collect(Collectors.toList());
    }

    @Override
    public Meal get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public void update(Meal meal, int id) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }
}