package ru.javawebinar.topjava.web.user;

import ru.javawebinar.topjava.model.User;

import java.util.List;

/**
 * Created by iy on 12.03.18.
 */
public interface UserController {
    List<User> getAll();

    User get(int id);

    User create(User user);

    void delete(int id);

    void update(User user, int id);

    User getByMail(String email);
}
