package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Override
    public ItemDto create(ItemDto item, long userId) {
        User owner = userDao.findUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item newItem = ItemMapper.toItem(item, owner);
        return ItemMapper.toItemDto(itemDao.create(newItem));
    }

    @Override
    public ItemDto update(ItemDto item, long itemId, long userId) {
        User owner = userDao.findUserById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с таким id не найден!"));
        Item updatedItem = itemDao.findItemById(itemId).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!"));
        if (updatedItem.getOwner().getId() != owner.getId()) {
            throw new NotFoundException("Пользователь не может обновить чужой предмет!");
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        itemDao.update(updatedItem, itemId);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto findItemById(long id) {
        return ItemMapper.toItemDto(itemDao.findItemById(id).orElseThrow(() ->
                new NotFoundException("Предмет с таким id не найден!")));
    }

    @Override
    public List<ItemDto> findOwnersItems(long userId) {
        return itemDao.findAll().stream().filter(s -> s.getOwner() != null && s.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findAvailableItems(String text) {
        return itemDao.findAll().stream().filter(s -> s.getAvailable() && (s.getName().toLowerCase().contains(text)
                        || s.getDescription().toLowerCase().contains(text)))
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
