package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("sve {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            log.info("sve {}", meal);
            return meal;
        }

        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll for userId={}", userId);
        List<Meal> meals = repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(meal -> meal.getDateTime()))
                .collect(Collectors.toList());

        return meals.isEmpty() ? null : meals;
    }

    @Override
    public List<Meal> getFilteredList(int userId, SearchFilter filter) {
        log.info("getFilteredList for userId {} with filter {}", userId, filter);
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

        return getAll(userId).stream().filter(meal -> predicate.test(meal)).collect(Collectors.toList());
    }
}

