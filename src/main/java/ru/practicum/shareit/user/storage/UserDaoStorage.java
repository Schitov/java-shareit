package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;

@Repository
@Slf4j
public class UserDaoStorage implements Dao, UserStorage {

    public static long id = 1;
    public static HashMap<Long, UserDto> users = new HashMap<>();

    @Override
    public UserDto get(long id) {
        UserDto userDto = users.get(id);
        log.info("User account information from UserStorage: {}", userDto);
        return userDto;
    }

    @Override
    public List getAllUsers() {
        List<UserDto> selectedUsers = new ArrayList<>(users.values());
        log.info("Users account information from UserStorage: {}", selectedUsers);
        return selectedUsers;
    }

    @Override
    public UserDto saveUserDto(UserDto userDto) {
        if (checkExistenceEmail(userDto.getEmail())) {
            throw new ExistenceOfObjectException(String.format("User with email %s is already existed",
                    userDto.getEmail()));
        }
        userDto.setId(generatorId());
        log.info("Saved user account information from UserStorage: {}", userDto);
        users.put(userDto.getId(), userDto);
        return userDto;
    }

    @Override
    public UserDto update(Object o, long id) {

        UserDto userDto = (UserDto) o;

        Optional<String> email = Optional.ofNullable(userDto.getEmail());
        Optional<String> name = Optional.ofNullable(userDto.getName());

        UserDto userDtoActual = users.get(id);
        userDto.setId(id);

        if (email.isPresent()) {
            if (!email.get().equals(userDtoActual.getEmail())) {
                if (checkExistenceEmail(userDto.getEmail())) {
                    throw new ExistenceOfObjectException(String.format("User with email %s is already existed",
                            userDto.getEmail()));
                }
            }
        }


        if (email.isPresent()) {
            userDtoActual.setEmail(email.get());
        }
        if (name.isPresent()) {
            userDtoActual.setName(name.get());
        }

        log.info("Updated user account information from UserStorage: {}", userDto);
        users.put(id, userDtoActual);

        return userDtoActual;
    }

    @Override
    public void delete(long userId) {
        log.info("Deleted user account information from UserStorage: {}", userId);
        users.remove(userId);
    }

    public static boolean checkExistenceEmail(String email) {
        if (email != null) {
            return users.values()
                    .stream()
                    .filter(userDtoUsers -> userDtoUsers.getEmail().contains(email))
                    .findFirst()
                    .isPresent();
        } else {
            return false;
        }
    }

    public static UserDto checkExistenceUser(long userId) {
        Optional<UserDto> userDto = Optional.ofNullable(users.get(userId));
        if (userDto.isPresent()) {
            return userDto.get();
        }
        return userDto.get();
    }

    public static long generatorId() {
        return id++;
    }

}
