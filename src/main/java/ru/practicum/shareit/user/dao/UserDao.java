package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();
    User create(User user);
    void update(User user,long id);
    void remove(long id);
    Optional<User> findUserById(long id);
}
