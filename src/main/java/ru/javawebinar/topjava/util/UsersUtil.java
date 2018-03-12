package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by iy on 12.03.18.
 */
public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(0, "user_0", "email_0@gmail.com", "password_0", Role.ROLE_USER),
            new User(1, "user_1", "email_1@gmail.com", "password_1", Role.ROLE_USER),
            new User(2, "user_2", "email_2@gmail.com", "password_2", Role.ROLE_USER),
            new User(3, "user_3", "email_3@gmail.com", "password_3", Role.ROLE_USER),
            new User(4, "user_4", "email_4@gmail.com", "password_4", Role.ROLE_USER)
    );

    public static List<User> getUSERS() {
        return USERS;
    }
}
