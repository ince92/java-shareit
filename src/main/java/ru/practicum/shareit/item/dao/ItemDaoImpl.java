package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {
    private long id;
    private final HashMap<Long, Item> itemMap = new HashMap<>();

    @Override
    public Item create(Item newItem) {
        newItem.setId(++id);
        itemMap.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public Optional<Item> findItemById(long id) {
        return Optional.ofNullable(itemMap.get(id));
    }

    @Override
    public void update(Item item, long id) {
        itemMap.put(id, item);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemMap.values());
    }
}
