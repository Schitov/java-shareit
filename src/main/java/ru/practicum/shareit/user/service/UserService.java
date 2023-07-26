package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional obtainUser(long id);

    List<User> obtainAllUsers();

    void deleteUser(long id);

    User updateUser(UserDto userDto, long id);

    User saveUser(UserDto userDto);
}
