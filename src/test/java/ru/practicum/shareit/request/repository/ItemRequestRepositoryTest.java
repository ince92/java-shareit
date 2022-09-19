package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;

    User user1;
    User user2;
    User user3;
    ItemRequest request1;
    ItemRequest request2;

    @BeforeEach
    void setup() {
        user1 = userRepository.save(new User(1L, "name1", "email1@mail"));
        user2 = userRepository.save(new User(2L, "name2", "email2@mail"));
        user3 = userRepository.save(new User(3L, "name3", "email3@mail"));
        request1 = itemRequestRepository.save(new ItemRequest(1L, "requestDescription", user2,
                LocalDateTime.now()));
        request2 = itemRequestRepository.save(new ItemRequest(2L, "requestDescriptionNew", user3,
                LocalDateTime.now()));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    void findItemRequestByRequesterIdTest() {
        final List<ItemRequest> requestList = itemRequestRepository.findItemRequestByRequesterId(user3.getId());
        assertEquals(1, requestList.size());
        assertEquals(request2.getId(), requestList.get(0).getId());
        assertEquals(request2.getDescription(), requestList.get(0).getDescription());
        assertEquals(request2.getRequester(), requestList.get(0).getRequester());
        assertEquals(request2.getCreated(), requestList.get(0).getCreated());
        assertEquals(user3.getId(), requestList.get(0).getRequester().getId());
        assertEquals(user3.getName(), requestList.get(0).getRequester().getName());
    }

    @Test
    void findAllItemRequestTest() {
        final List<ItemRequest> requestList = itemRequestRepository.findAllItemRequest(user2.getId(), Pageable.unpaged());
        assertEquals(1, requestList.size());
        assertEquals(request2.getId(), requestList.get(0).getId());
        assertEquals(request2.getDescription(), requestList.get(0).getDescription());
        assertEquals(request2.getRequester(), requestList.get(0).getRequester());
        assertEquals(request2.getCreated(), requestList.get(0).getCreated());
        assertEquals(user3.getId(), requestList.get(0).getRequester().getId());
        assertEquals(user3.getName(), requestList.get(0).getRequester().getName());
    }

}