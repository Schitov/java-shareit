package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserDaoStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserDaoStorage userDaoStorage;

    @Autowired
    UserServiceImpl(UserDaoStorage userDaoStorage) {
        this.userDaoStorage = userDaoStorage;
    }

    @Override
    public UserDto obtainUser(long id) {
        return userDaoStorage.get(id);
    }

    @Override
    public List obtainAllUsers() {
        return userDaoStorage.getAllUsers();
    }

    @Override
    public void deleteUser(long id) {
        userDaoStorage.delete(id);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        log.info("Updated user account №{} information from UserServiceImpl: {}", id, userDto);
        return userDaoStorage.update(userDto, id);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return userDaoStorage.saveUserDto(userDto);
    }
}
