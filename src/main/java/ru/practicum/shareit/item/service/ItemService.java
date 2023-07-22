package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item obtainItem(long id);

    List<Item> obtainAllItems(long userId);

    void deleteItem(long id);

    Item updateItem(ItemDto itemDto, long itemId, long userId);

    Item saveItem(ItemDto itemDto, long userId);

    List<Item> searchItemsByText(String text);

}
