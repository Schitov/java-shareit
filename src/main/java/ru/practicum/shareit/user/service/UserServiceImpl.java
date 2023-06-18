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

import java.util.*;
import java.util.stream.Collectors;

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
    public User obtainUser(long id) {
        return userDaoStorage.get(id);
    }

    @Override
    public List<User> obtainAllUsers() {

        return new ArrayList<>(userDaoStorage.getAllUsers());
    }

    @Override
    public void deleteUser(long id) {
        userDaoStorage.delete(id);
    }

    @Override
    public User updateUser(UserDto userDto, long id) {
        log.info("Updated user account №{} information from UserServiceImpl: {}", id, userDto);
        User user = UserMapper.dtoToUser(userDto); // Преобразуем полученный DTO user объект в user

        Optional<String> email = Optional.ofNullable(user.getEmail()); // Для проверки email на существование -->
        // делаем обертку Optional
        Optional<String> name = Optional.ofNullable(user.getName()); // Для проверки name на существование -->
        // делаем обертку Optional
        user.setId(id); // Для полученного пользователя устанавливаем id

        User userActual = userDaoStorage.getUsers().get(id); //Получаем пользователя, который хранится в Storage по id

        log.debug("User account №{} from to update information UserServiceImpl: {}", id, user);

        if (email.isPresent()) { // проверяем email у updated user на существование
            if (!email.get().equals(userActual.getEmail())) { // если email у updated user есть и его нужно обновить
                if (checkExistenceEmail(user.getEmail())) { // то проверяем, что больше никого нет с таким email
                    throw new ExistenceOfObjectException(String.format("User with email %s is already existed",
                            user.getEmail()));
                }
            }
        }


        email.ifPresent(userActual::setEmail); // Для установки email проверяем его существование
        name.ifPresent(userActual::setName); // Для установки name проверяем его существование


        return userDaoStorage.update(userActual, id);
    }

    @Override
    public User saveUser(UserDto userDto) {
        User user = UserMapper.dtoToUser(userDto);
        if (checkExistenceEmail(user.getEmail())) {
            throw new ExistenceOfObjectException(String.format("User with email %s is already existed",
                    user.getEmail()));
        }
        user.setId(userDaoStorage.generatorId());
        log.info("Saved user account information from UserStorage: {}", user);

        return userDaoStorage.saveUser(user);
    }

    public boolean checkExistenceEmail(String email) {

        log.debug("email {}", email);

        Set<String> emails = userDaoStorage.getAllUsers()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
        if (emails.contains(email)) {
            return true;
        }
        return false;


//        if (email != null) {
//            return userDaoStorage.getAllUsers()
//                    .stream()
//                    .anyMatch(userDtoUsers -> userDtoUsers.getEmail().contains(email));
//        } else {
//            return false;
//        }
    }
}
