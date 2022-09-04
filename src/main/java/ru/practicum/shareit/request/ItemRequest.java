package ru.practicum.shareit.request;

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
@Table(name = "REQUESTS")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private long id;
    @Column(name = "REQUEST_DESCRIPTION")
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "REQUEST_REQUESTER_ID")
    private User requester;
    @Column(name = "REQUEST_CREATE_DATE")
    private LocalDateTime created;
}
