package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        UserDto userDto = UserMapper.userToDto(item.getOwner());

        return ItemDto.builder()
                .owner(userDto)
                .id(item.getId())
                .available(item.getAvailable())
                .name(item.getName())
                .request(item.getRequest())
                .description(item.getDescription())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {

        return Item.builder()
                .id(itemDto.getId())
                .available(itemDto.getAvailable())
                .name(itemDto.getName())
                .request(itemDto.getRequest())
                .description(itemDto.getDescription())
                .build();
    }


}
