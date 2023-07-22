package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    User saveUser(User user);

    List<User> getAllUsers();

    void delete(long userId);

    User update(User user, String email, long id);

    User get(long id);

    HashMap<Long, User> getUsers();
}
