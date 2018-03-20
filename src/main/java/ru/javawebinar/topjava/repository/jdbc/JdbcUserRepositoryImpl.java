package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.List;
/**We have bean DataSource which supplies connection to DB.
 * DataSource is passing to JdbcTemplate and NamedParameterJdbcTemplate. */

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {
    // mapping db table fields to User fields and vice versa (need getters and setters in User)
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    // unnamed bean is used to pass @Nullable Object... args in sql query
    private final JdbcTemplate jdbcTemplate;
    // named bean is used to pass String parameters in sql query
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    // for executing inserting to DB
    private final SimpleJdbcInsert insertUser;

    @Autowired // beans are defined in spring-db.xml
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource) // insert object
                .withTableName("users") // work with 'users' table
                .usingGeneratedKeyColumns("id"); // us 'id' in 'where condition' (sql quarry) (== return type 'id')

        // work with default constructor and setters (mast to be in User class)
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        // mapping User fields to parameters (String -> Object)
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map); // insert 'user' into table
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update( // work with map parameter name
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map) == 0) {
            return null;
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        // unnamed template: work with @Nullable Object... args
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        // mapping table fields to user fields using ROW_MAPPER (ROW_MAPPER is needed to convert table fields to User object)
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id); // unnamed template: work with @Nullable Object... args
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        // mapping table fields to user fields using ROW_MAPPER
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override // mapping table fields to user fields using ROW_MAPPER
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }
}
