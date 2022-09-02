package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;
    @Column(name = "ITEM_NAME")
    private String name;
    @Column(name = "ITEM_DESCRIPTION")
    private String description;
    @Column(name = "ITEM_IS_AVAILABLE")
    private Boolean available;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ITEM_OWNER_ID")
    private User owner;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ITEM_REQUEST_ID")
    private ItemRequest request;
}
