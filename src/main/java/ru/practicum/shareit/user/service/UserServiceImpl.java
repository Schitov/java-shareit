package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.exception.exceptions.ExistenceOfUserException;
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
                .orElseThrow(() -> new ExistenceOfUserException("User with id " + id + " not found")));
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
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new ExistenceOfObjectException(String.format("Not found user with id ", id)));

        if (userDto.getName() != null) {
            userToUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userToUpdate.setEmail(userDto.getEmail());
        }

        return userRepository.save(userToUpdate);
    }

//    public void updateUserColumns(User userToUpdate, User newUser) {
//
//    }
//
//    @Override
//    public User updateUser(UserDto userDto, long id) {
//        log.info("Updated user account №{} information from UserServiceImpl: {}", id, userDto);
//        HashSet<String> emails = userDaoStorage.getEmails();
//        User user = UserMapper.dtoToUser(userDto); // Преобразуем полученный DTO user объект в user
//
//        Optional<String> email = Optional.ofNullable(user.getEmail()); // Для проверки email на существование -->
//        // делаем обертку Optional
//        Optional<String> name = Optional.ofNullable(user.getName()); // Для проверки name на существование -->
//        // делаем обертку Optional
//        user.setId(id); // Для полученного пользователя устанавливаем id
//
//        User userActual = userDaoStorage.getUsers().get(id);//Получаем пользователя, который хранится в Storage по id
//        String actualEmail = userActual.getEmail();
//
//        log.debug("User account №{} from to update information UserServiceImpl: {}", id, user);
//
//        if (email.isPresent()) { // проверяем email у updated user на существование
//            if (!email.get().equals(actualEmail)) { // если email у updated user есть и его нужно обновить
//                if (emails.contains(email.get())) { // то проверяем, что больше никого нет с таким email
//                    throw new ExistenceOfObjectException(String.format("User with email %s is already existed",
//                            user.getEmail()));
//                }
//            }
//        }
//
//        email.ifPresent(userActual::setEmail); // Для установки email проверяем его существование
//        name.ifPresent(userActual::setName); // Для установки name проверяем его существование
//
//
//        return userDaoStorage.update(userActual, actualEmail, id);
//    }

    @Override
    public User saveUser(UserDto userDto) {
        return userRepository.save(UserMapper.dtoToUser(userDto));
    }
}
