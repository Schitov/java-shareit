package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemDaoStorage;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemServiceImpl itemService;

    @Autowired
    ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemDto obtainItem(@PathVariable long itemId) {
        return itemService.obtainItem(itemId);
    }

    @GetMapping()
    public List obtainAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.obtainAllItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(long itemId) {
        itemService.deleteItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @PostMapping()
    public ItemDto saveItem(@Valid @RequestBody ItemDto itemDto,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.saveItem(itemDto, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestParam String text) {
        return itemService.searchItemsByText(text);
    }

}
