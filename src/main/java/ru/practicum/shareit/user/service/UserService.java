package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto obtainUser(long id);

    public List obtainAllUsers();

    public void deleteUser(long id);

    public UserDto updateUser(UserDto userDto, long id);

    public UserDto saveUser(UserDto userDto);
}
