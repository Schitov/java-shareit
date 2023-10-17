package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDaoStorage;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserDaoStorage userDaoStorage;
    UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserDaoStorage userDaoStorage, UserRepository userRepository) {
        this.userDaoStorage = userDaoStorage;
        this.userRepository = userRepository;
    }


    @Override
    public UserDto obtainUser(long id) {
        return UserMapper.userToDto(userRepository.findById(id)
                .orElseThrow(() -> new ExistenceOfObjectException("User with id " + id + " not found")));
    }

    @Override
    public List<User> obtainAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(UserDto userDto, long id) {
        User userToUpdate = userRepository
                .findById(id)
                .orElseThrow(() -> new ExistenceOfObjectException(String.format("Not found user with id ", id)));

        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userToUpdate.setEmail(userDto.getEmail());
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    public User saveUser(UserDto userDto) {
        return userRepository.save(UserMapper.dtoToUser(userDto));
    }
}
