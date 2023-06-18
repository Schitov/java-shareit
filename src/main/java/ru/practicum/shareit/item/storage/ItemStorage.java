package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item saveItem(Item item);

    List<Item> getAllItems(long userId);

    List<Item> search(String text);

    void delete(long itemId);

    Item update(Optional<Item> itemToChange, long itemId);

    Item get(long id);

}
