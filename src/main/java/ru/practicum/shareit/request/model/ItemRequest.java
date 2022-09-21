package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private long id;
    @Column(name = "request_description")
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "request_requester_id")
    private User requester;
    @Column(name = "request_create_date")
    private LocalDateTime created;
}
