package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemStorage {

    public ItemDto saveItemDto(ItemDto item, long userId);

    public List getAllItems(long userId);

    public List<ItemDto> search(String text);

    public ItemDto delete(Object o);

    ItemDto update(ItemDto itemDto, long itemId, long userId);

}
