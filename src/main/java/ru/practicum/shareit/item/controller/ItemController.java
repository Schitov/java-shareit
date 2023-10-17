package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.exceptions.ValidException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemServiceImpl itemService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto obtainItem(@PathVariable long itemId,
                                   @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        log.debug("Параметр, полученный в методе obtainItem: {}", itemId);
        return itemService.obtainItem(itemId, userId);
    }

    @GetMapping()
    public List<ItemOwnerDto> obtainAllItems(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.debug("Параметр, полученный в методе obtainAllItems: {}", userId);
        return itemService.getItemsByOwnerId(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(long itemId) {
        log.debug("Параметр, полученный в методе deleteItem: {}", itemId);
        itemService.deleteItem(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@PathVariable Long itemId,
                                       @RequestBody CommentDto commentDto,
                                       @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.debug("For item {} of owner {} will be added comment: {}", itemId, userId, commentDto);
        return itemService.addCommentToItem(itemId, commentDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
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

        try {
            return itemService.saveItem(itemDto, userId);
        } catch (ConstraintViolationException ex) {
            throw new ValidException(ex.getMessage());
        }
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestParam String text) {
        log.debug("Параметр, полученный в методе searchItemsByText: {}", text);
        return itemService.findByDescriptionContainingIgnoreCase(text);
    }

}
