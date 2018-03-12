package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

/**
 * Created by iy on 12.03.18.
 */
public class AbstractMealController implements MealController {
    private static final Logger log = getLogger(AbstractMealController.class);
    private MealService mealService;

    @Override
    public List<Meal> getAll(User user) {
        log.info("getAll");
        return mealService.getAll(user);
    }

    @Override
    public List<Meal> getFilteredList(User user, SearchFilter filter) {
        log.info("getFilteredList");

        return mealService.getFilteredList(user, filter);
    }

    @Override
    public Meal get(int id) {
        log.info("get {}", id);
        return mealService.get(id);
    }

    @Override
    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return mealService.create(meal);
    }

    @Override
    public void delete(int id) {
        log.info("delete {}", id);
        mealService.delete(id);
    }

    @Override
    public void update(Meal meal, int id) {
        log.info("update {} with id = {}", meal, id);
        assureIdConsistent(meal, id);
        mealService.update(meal, id);
    }
}
