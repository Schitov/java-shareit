package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemDaoStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    ItemDaoStorage itemDaoStorage;

    @Autowired
    ItemServiceImpl(ItemDaoStorage itemDaoStorage) {
        this.itemDaoStorage = itemDaoStorage;
    }

    public ItemDto obtainItem(long id) {
        return itemDaoStorage.get(id);
    }

    public List obtainAllItems(long userId) {
        return itemDaoStorage.getAllItems(userId);
    }

    public void deleteItem(long id) {
        itemDaoStorage.delete(id);
    }

    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        return itemDaoStorage.update(itemDto, itemId, userId);
    }

    public ItemDto saveItem(ItemDto itemDto, long userId) {
        return itemDaoStorage.saveItemDto(itemDto, userId);
    }

    public List<ItemDto> searchItemsByText(String text) {
        return itemDaoStorage.search(text);
    }

}
