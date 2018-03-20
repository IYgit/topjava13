package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.AuthorizedUser.id;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {
    // need getters and setters in Meal class
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(dataSource).withTableName("meals").usingGeneratedKeyColumns("id");
        // work with default constructor and setters (mast to be in Meal class)
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        ValidationUtil.checkUserId(id(), userId);

        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("user_id", userId)
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());

        if (meal.isNew()){
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("UPDATE meals SET datetime=:dateTime," +
                "description=:description, calories=:calories WHERE id=:id", map) == 0) {
            return null;
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        ValidationUtil.checkUserId(id(), userId);
        // update() returns number of row
        return jdbcTemplate.update("DELETE FROM meals WHERE id = ?", id) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        ValidationUtil.checkUserId(id(), userId);
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals where id = ?", ROW_MAPPER, id);

        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        ValidationUtil.checkUserId(id(), userId);

        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals", ROW_MAPPER);
        meals.sort((m1, m2) -> {
            int result = m2.getDate().compareTo(m1.getDate());
            return result != 0 ? result : m1.getTime().compareTo(m2.getTime());
        });

        return meals;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDate, endDate))
                .collect(Collectors.toList());
    }
}
