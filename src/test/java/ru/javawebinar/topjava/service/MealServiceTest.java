package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.IllegalUserAccessException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Before // before all test methods
    public void setUp() throws Exception {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_1, USER_ID_1);
        assertMatch(meal, MEAL_1);
    }

    @Test(expected = IllegalUserAccessException.class)
    public void getForeignMeal(){
        service.get(MEAL_ID_1, USER_ID_2);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(MEAL_ID_1, USER_ID_1);
        service.get(MEAL_ID_1, USER_ID_1); // expected NotFoundException
    }

    @Test(expected = IllegalUserAccessException.class)
    public void deleteForeignMeal(){
        service.delete(MEAL_ID_1, USER_ID_2);
    }

    @Test
    public void getBetweenDates() {
        LocalDate from = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2015-05-30", LocalDate::from);
        LocalDate to = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2015-05-30", LocalDate::from);
        List<Meal> meals = service.getBetweenDates(from, to, USER_ID_1);
        assertMatch(meals, MEAL_2);

        from = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2015-05-30", LocalDate::from);
        to = DateTimeFormatter.ofPattern("yyyy-MM-dd").parse("2015-05-31", LocalDate::from);
        meals = service.getBetweenDates(from, to, USER_ID_1);
        assertMatch(meals, MEAL_1, MEAL_2);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime from = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse("2015-05-30 13:00", LocalDateTime::from);
        LocalDateTime to = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse("2015-05-31 00:00", LocalDateTime::from);
        List<Meal> meals = service.getBetweenDateTimes(from, to, USER_ID_1);
        assertMatch(meals, MEAL_2);

        from = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse("2015-05-30 13:00", LocalDateTime::from);
        to = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").parse("2015-05-31 10:00", LocalDateTime::from);
        meals = service.getBetweenDateTimes(from, to, USER_ID_1);
        assertMatch(meals, MEAL_1, MEAL_2);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID_1);
        assertMatch(all, MEAL_1, MEAL_2);
    }

    @Test
    public void update() {
        MEAL_1.setCalories(5);
        MEAL_1.setDescription("new_description");
        MEAL_1.setDateTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .parse("2015-10-10 15:00", LocalDateTime::from));
        service.update(MEAL_1, USER_ID_1);
        assertMatch(service.get(MEAL_ID_1, USER_ID_1), MEAL_1);
    }

    @Test(expected = IllegalUserAccessException.class)
    public void updateForeignMeal(){
        service.update(MEAL_1, USER_ID_2);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .parse("2015-10-10 15:00", LocalDateTime::from), "description", 1800);
        service.create(newMeal, USER_ID_1);
        // important order
        assertMatch(service.getAll(USER_ID_1), newMeal, MEAL_1, MEAL_2);
    }
}