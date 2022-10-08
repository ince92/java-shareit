package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    User user1;
    User user2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(new User(1L, "name1", "email1@mail"));
        user2 = userRepository.save(new User(2L, "name2", "email2@mail"));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void findByIdTest() {
        final Optional<User> itemById = userRepository.findById(user1.getId());
        assertEquals(user1.getId(), itemById.get().getId());
        assertEquals(user1.getName(), itemById.get().getName());
        assertEquals(user1.getEmail(), itemById.get().getEmail());
    }

}