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
@Table(name = "bookings")
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;
    @Column(name = "booking_start_date")
    private LocalDateTime start;
    @Column(name = "booking_end_date")
    private LocalDateTime end;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "booking_item_id")
    private Item item;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "booking_booker_id")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus status;
}
