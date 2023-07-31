package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        UserDto userDto = UserMapper.userToDto(item.getOwner());

        return ItemDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .requestId(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .request(itemDto.getRequestId())
                .description(itemDto.getDescription())
                .build();
    }

    public static ItemOwnerDto toItemDtoOwner(Item item) {
        UserDto userDto = UserMapper.userToDto(item.getOwner());

        return ItemOwnerDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .requestId(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static Item toItemOwner(ItemOwnerDto itemOwnerDto) {
        return Item.builder()
                .id(itemOwnerDto.getId())
                .available(itemOwnerDto.getAvailable())
                .name(itemOwnerDto.getName())
                .request(itemOwnerDto.getRequestId())
                .description(itemOwnerDto.getDescription())
                .build();
    }


}
