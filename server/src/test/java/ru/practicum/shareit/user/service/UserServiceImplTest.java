package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    UserService userService;

    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void findAllTest() {
        User user = new User(1L, "name1", "email1@mail");
        User user1 = new User(2L, "name1", "email1@mail");
        List<User> userList = List.of(user, user1);
        when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> userDtoList = userService.findAll();

        assertEquals(userList.size(), userDtoList.size());
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
        assertEquals(userList.get(0).getName(), userDtoList.get(0).getName());
        assertEquals(userList.get(0).getEmail(), userDtoList.get(0).getEmail());
    }

    @Test
    void findUserByIdTest() {
        User user = new User(1L, "name1", "email1@mail");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto userDto = userService.findUserById(user.getId());

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void createTest() {
        UserDto user = new UserDto(1L, "name1", "email1@mail");
        when(userRepository.save(any())).thenReturn(UserMapper.toUser(user));
        UserDto userDto = userService.create(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void updateTest() {
        UserDto userDto = new UserDto(1L, "UpdatedName1", "email1@mail");
        User user = new User(1L, "name1", "email1@mail");

        when(userRepository.save(any())).thenReturn(UserMapper.toUser(userDto));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserDto updatedUserDto = userService.update(userDto, userDto.getId());

        assertEquals(user.getId(), updatedUserDto.getId());
        assertEquals(user.getName(), updatedUserDto.getName());
        assertEquals(user.getEmail(), updatedUserDto.getEmail());
    }
}