package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto obtainItem(long id);

    public List obtainAllItems(long userId);

    public void deleteItem(long id);

    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    public ItemDto saveItem(ItemDto itemDto, long userId);

    public List<ItemDto> searchItemsByText(String text);

}
