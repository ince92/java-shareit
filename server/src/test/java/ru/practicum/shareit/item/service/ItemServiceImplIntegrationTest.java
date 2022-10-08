package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {

    private final EntityManager em;
    private final ItemRequestService service;
    private final UserRepository userRepository;

    @Test
    void findItemById() {

        User user = userRepository.save(new User(1L, "name1", "email1@mail"));
        ItemRequestDto requestDto = makeItemDto("description", user.getId(), LocalDateTime.now());
        service.create(requestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select r from ItemRequest r where r.description = " +
                ":description and r.requester.id = :userId", ItemRequest.class);
        ItemRequest request = query.setParameter("description", requestDto.getDescription())
                .setParameter("userId", user.getId()).getSingleResult();

        assertThat(request.getId(), notNullValue());
        assertThat(request.getDescription(), equalTo(requestDto.getDescription()));
        assertThat(request.getRequester(), equalTo(user));
    }

    private ItemRequestDto makeItemDto(String description, Long requesterId, LocalDateTime created) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(description);
        dto.setRequesterId(requesterId);
        dto.setCreated(created);

        return dto;
    }
}