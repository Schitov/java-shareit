package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    UserServiceImpl userService;

    @Autowired
    UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User obtainUser(@PathVariable long userId) {
        log.debug("Параметр, полученный в методе obtainUser: {}", userId);
        return userService.obtainUser(userId);
    }

    @GetMapping()
    public List<User> obtainAllItems() {
        return userService.obtainAllUsers();
    }

    @DeleteMapping("/{userId}")
    public void deleteItem(@PathVariable long userId) {
        log.debug("Параметр, полученный в методе deleteItem: {}", userId);
        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody UserDto userDto,
                           @PathVariable long userId) {
        log.debug("Параметры полученные в методе updateUser: userDto - {}, userId - {}", userDto, userId);
        return userService.updateUser(userDto, userId);
    }

    @PostMapping()
    public User saveUser(@Valid @RequestBody UserDto userDto) {
        log.debug("Body, полученное в методе saveUser: {}", userDto);
        return userService.saveUser(userDto);
    }

}
