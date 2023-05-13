package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .owner(item.getOwner())
                .id(item.getId())
                .available(item.isAvailable())
                .name(item.getName())
                .request(item.getRequest())
                .description(item.getDescription())
                .build();
    }

}
