package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item create(Item item);

    Optional<Item> findItemById(long id);

    void update(Item item, long id);

    List<Item> findAll();
}
