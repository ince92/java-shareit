package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
class BookingServiceImplIntegrationTest {

    private final EntityManager em;
    private final BookingService service;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    void createTest() {
        User user = userRepository.save(new User(1L, "name1", "email1@mail"));
        User booker = userRepository.save(new User(30L, "booker", "booker@mail"));
        Item item = itemRepository.save(new Item(1L, "username1", "description1", true,
                user, null));
        BookingRequestDto bookingDto = makeBookingDto(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                item.getId());
        service.create(bookingDto, booker.getId());

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.item.id = :itemId",
                Booking.class);
        Booking booking = query.setParameter("itemId", bookingDto.getItemId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getBooker(), equalTo(booker));
    }

    private BookingRequestDto makeBookingDto(LocalDateTime start, LocalDateTime end, Long itemId) {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setStart(start);
        dto.setEnd(end);
        dto.setItemId(itemId);

        return dto;
    }
}