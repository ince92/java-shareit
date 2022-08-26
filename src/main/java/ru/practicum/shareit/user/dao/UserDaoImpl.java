package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private long id;
    private final HashMap<Long, User> userMap = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User create(User newUser) {
        newUser.setId(++id);
        userMap.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void update(User user, long id) {
        userMap.put(id, user);
    }

    @Override
    public void remove(long id) {
        userMap.remove(id);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }
}
