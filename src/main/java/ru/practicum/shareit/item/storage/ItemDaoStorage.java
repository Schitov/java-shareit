package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemDaoStorage implements ItemStorage {

    private long id = 1;
    HashMap<Long, Item> items = new HashMap<>();

    @Override
    public Item get(long id) {
        Item item = items.get(id);
        log.debug("Item from ItemStorage: {}", item);
        return item;
    }

    @Override
    public Item saveItem(Item item) {
        items.put(item.getId(), item);

        return items.get(item.getId());
    }

    @Override
    public List<Item> getAllItems(long userId) {
        log.debug("All recorded items: {}", items.values());
        return new ArrayList<>(items.values());
    }

    @Override
    public Item update(Optional<Item> itemToChange, long itemId) {

        items.replace(itemId, itemToChange.get());

        log.debug("Updated item from ItemStorage: {}", itemToChange.get());

        return items.get(itemId);
    }

    @Override
    public void delete(long itemId) {
        items.remove(itemId);
    }

    public List<Item> search(String text) {

        log.debug("Text to search item from ItemStorage: {}", text);

        return new ArrayList<>(items.values());
    }

    public long generatorId() {
        return id++;
    }

}
