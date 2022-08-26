package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto user) {
        validateEmail(user.getEmail(), 0);
        return UserMapper.toUserDto(userDao.create(UserMapper.toUser(user)));
    }

    @Override
    public UserDto update(UserDto user, long id) {
        User updatedUser = userDao.findUserById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        validateEmail(user.getEmail(), id);
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        userDao.update(updatedUser, id);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        userDao.remove(id);
    }

    @Override
    public UserDto findUserById(long id) {
        return UserMapper.toUserDto(userDao.findUserById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!")));
    }

    private void validateEmail(String email, long userid) {
        List<User> user = userDao.findAll().stream().filter(s -> s.getId() != userid && s.getEmail().equals(email))
                .collect(Collectors.toList());
        if (!user.isEmpty()) {
            throw new EmailException("Дублирование имейла!");
        }
    }
}
