package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .owner(item.getOwner())
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .request(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .owner(itemDto.getOwner())
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .request(itemDto.getRequest())
                .description(itemDto.getDescription())
                .build();
    }


}
