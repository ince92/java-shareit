package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long id;
    @Column(name = "BOOKING_START_DATE")
    private LocalDateTime start;
    @Column(name = "BOOKING_END_DATE")
    private LocalDateTime end;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BOOKING_ITEM_ID")
    private Item item;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BOOKING_BOOKER_ID")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "BOOKING_STATUS")
    private BookingStatus status;
}
