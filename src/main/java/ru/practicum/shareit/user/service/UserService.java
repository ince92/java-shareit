package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto create(UserDto user);
    UserDto update(UserDto user, long id);
    void deleteUser(long id);
    UserDto findUserById(long id);

}
