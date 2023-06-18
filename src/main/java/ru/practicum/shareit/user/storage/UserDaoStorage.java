package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
public class UserDaoStorage implements UserStorage {

    public long id = 1;
    public static final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User get(long id) {
        User user = users.get(id);
        log.debug("User account information from UserStorage: {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> selectedUsers = new ArrayList<>(users.values());
        log.debug("Users account information from UserStorage: {}", selectedUsers);
        return selectedUsers;
    }

    @Override
    public User saveUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, long id) {

        log.debug("Updated user account information from UserStorage: {}", user);
        users.put(id, user);

        return users.get(id);
    }

    @Override
    public void delete(long userId) {
        log.debug("Deleted user account information from UserStorage: {}", userId);
        users.remove(userId);
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public long generatorId() {
        return id++;
    }

}
