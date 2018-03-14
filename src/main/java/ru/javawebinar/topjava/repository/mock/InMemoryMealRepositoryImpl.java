package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.SearchFilter;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.nio.file.DirectoryStream;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .collect(Collectors.toList());

        return meals.isEmpty() ? null : meals;
    }

    @Override
    public List<Meal> getFilteredList(int userId, SearchFilter filter) {
        log.info("getFilteredList for userId {} with filter {}", userId, filter);
        Predicate<Meal> predicate = getMealPredicate(filter);

        return getAll(userId).stream().filter(meal -> predicate.test(meal)).collect(Collectors.toList());
    }

    private Predicate<Meal> getMealPredicate(SearchFilter filter) {
        Predicate<Meal> predicate = meal -> filter.getFromDate() == null ? true : meal.getDate().compareTo(filter.getFromDate()) >= 0;
        predicate = predicate.and(meal -> filter.getToDate() == null ? true : meal.getDate().compareTo(filter.getToDate()) <= 0);
        predicate = predicate.and(meal -> filter.getFromTime() == null ? true : meal.getTime().compareTo(filter.getFromTime()) >= 0);
        predicate = predicate.and(meal -> filter.getToTime() == null ? true : meal.getTime().compareTo(filter.getToTime()) <= 0);

        boolean isSearchKey = filter.getSearchKey() != null && !filter.getSearchKey().isEmpty();
        Predicate<Meal> regexPredicate = null;
        if (isSearchKey){
            String regex = ".*" + filter.getSearchKey() + ".*";
            regexPredicate = meal -> meal.getDate().toString().matches(regex);
            regexPredicate = regexPredicate.or(meal -> meal.getTime().toString().matches(regex));
            regexPredicate = regexPredicate.or(meal -> meal.getDescription().matches(regex));
            regexPredicate = regexPredicate.or(meal -> String.valueOf(meal.getCalories()).matches(regex));
        }

        if (regexPredicate != null)
            predicate = predicate.and(regexPredicate);

        return predicate;
    }
}

