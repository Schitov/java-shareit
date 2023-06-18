package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemServiceImpl itemService;
    private final static String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public Item obtainItem(@PathVariable long itemId) {
        log.debug("Параметр, полученный в методе obtainItem: {}", itemId);
        return itemService.obtainItem(itemId);
    }

    @GetMapping()
    public List<Item> obtainAllItems(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.debug("Параметр, полученный в методе obtainAllItems: {}", userId);
        return itemService.obtainAllItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(long itemId) {
        log.debug("Параметр, полученный в методе deleteItem: {}", itemId);
        itemService.deleteItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto,
                           @PathVariable long itemId,
                           @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.debug("Параметры, полученные в методе updateItem: itemDto - {}, itemId - {}, userId - {}",
                itemDto, itemId, userId);
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @PostMapping()
    public Item saveItem(@Valid @RequestBody ItemDto itemDto,
                         @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.debug("Параметры, полученные в методе saveItem: itemDto - {}, serId - {}",
                itemDto, userId);
        return itemService.saveItem(itemDto, userId);
    }

    @GetMapping("/search")
    public List<Item> searchItemsByText(@RequestParam String text) {
        log.debug("Параметр, полученный в методе searchItemsByText: {}", text);
        return itemService.searchItemsByText(text);
    }

}
