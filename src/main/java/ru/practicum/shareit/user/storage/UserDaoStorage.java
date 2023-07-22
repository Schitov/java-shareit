package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
@Slf4j
public class UserDaoStorage implements UserStorage {

    public long id = 1;
    public static final HashMap<Long, User> users = new HashMap<>();
    public static final HashSet<String> emails = new HashSet<>();

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
        String email = user.getEmail();
        emails.add(email);

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user, String email, long id) {

        updateEmail(user, email);

        log.debug("Updated user account information from UserStorage: {}", user);
        users.put(id, user);

        return users.get(id);
    }

    @Override
    public void delete(long userId) {
        log.debug("Deleted user account information from UserStorage: {}", userId);
        String email = users.get(userId).getEmail();
        emails.remove(email);

        users.remove(userId);
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public HashSet<String> getEmails() {
        return emails;
    }

    public void updateEmail(User user, String oldEmail) {
        String email = user.getEmail();
        emails.remove(oldEmail);
        emails.add(email);
    }

    public long generatorId() {
        return id++;
    }

}
