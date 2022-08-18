package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<UserDto> findAll() {
        List<UserDto> users = userService.findAll();
        log.info("Количество пользователей - {}", users.size());
        return users;
    }

    @PostMapping()
    public UserDto create(@Validated({Create.class}) @RequestBody UserDto user) {
        UserDto newUser = userService.create(user);
        log.info("Добавлен пользователь - {}", newUser.getName());
        return newUser;
    }

    @PatchMapping(value = "/{id}")
    public UserDto update(@PathVariable("id") long id, @Validated({Update.class}) @RequestBody UserDto user) {
        UserDto updatedUser = userService.update(user, id);
        log.info("Обновлен пользователь- {}", updatedUser.getName());
        return updatedUser;
    }

    @GetMapping("/{id}")
    public UserDto findUserByID(@PathVariable("id") long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }


}
