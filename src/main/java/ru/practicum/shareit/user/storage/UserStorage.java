package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    public UserDto saveUserDto(UserDto userDto);

    public List getAllUsers();

    public void delete(long userId);

    public UserDto update(Object o, long id);
}
