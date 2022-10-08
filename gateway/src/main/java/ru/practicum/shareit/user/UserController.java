package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping()
    public ResponseEntity<Object> findAll() {
        log.info("Get user list");
        return userClient.findAll();
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserDto user) {
        log.info("Creating user - {}", user.getName());
        return userClient.create(user);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") long id, @Validated({Update.class})
    @RequestBody UserDto user) {
        log.info("Updating user- {}", user.getName());
        return userClient.update(user, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserByID(@PathVariable("id") long id) {
        return userClient.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userClient.deleteUser(id);
    }


}
