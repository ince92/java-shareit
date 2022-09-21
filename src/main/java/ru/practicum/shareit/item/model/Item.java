package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_description")
    private String description;
    @Column(name = "item_is_available")
    private Boolean available;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_owner_id")
    private User owner;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_request_id")
    private ItemRequest request;
}
