package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemOwnerDto obtainItem(long id, long userId);

    List<ItemOwnerDto> getItemsByOwnerId(long ownerId);

    void deleteItem(long id);

    ItemDto updateItem(ItemDto item, long itemId, long userId);

    Item saveItem(ItemDto itemDto, long userId);

    CommentDto addCommentToItem(Long itemId, CommentDto commentDto, Long userId);

    List<ItemDto> findByDescriptionContainingIgnoreCase(String searchText);

}
