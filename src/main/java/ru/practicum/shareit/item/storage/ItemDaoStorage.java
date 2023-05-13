package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.dao.Dao;
import ru.practicum.shareit.exception.exceptions.ExistenceOfUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserDaoStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemDaoStorage implements Dao, ItemStorage {

    private static long id = 1;
    private UserDaoStorage userDaoStorage;
    private final ItemStorageValidator itemStorageValidator;
    HashMap<Long, ItemDto> items = new HashMap<>();

    @Autowired
    ItemDaoStorage(UserDaoStorage userDaoStorage, ItemStorageValidator itemStorageValidator) {
        this.userDaoStorage = userDaoStorage;
        this.itemStorageValidator = itemStorageValidator;
    }

    @Override
    public ItemDto get(long id) {
        ItemDto itemDto = items.get(id);
        log.info("Item from ItemStorage: {}", itemDto);
        return items.get(id);
    }

    @Override
    public List getAllItems(long userId) {
        log.info("All recorded items: {}", items.values());
        List<ItemDto> itemsOfUser = items.values()
                .stream()
                .filter(itemDto -> itemDto.getOwner().getId() == userId)
                .collect(Collectors.toList());
        log.debug("Output of all items owned by user: {}", itemsOfUser);
        return itemsOfUser;
    }

    @Override
    public ItemDto saveItemDto(ItemDto item, long userId) {
        Optional<UserDto> userDto = Optional.ofNullable(userDaoStorage.get(userId));

        if (userDto.isEmpty()) {
            throw new ExistenceOfUserException(String.format("User with id %d doesn't exist",
                    userId));
        }

        item.setOwner(userDto.get());
        item.setId(generatorId());

        items.put(item.getId(), item);

        log.info("Saved item from ItemStorage: {}", item);
        return item;
    }

    @Override
    public ItemDto update(ItemDto itemDto, long itemId, long userId) {

        Optional<ItemDto> itemDtoToChange = Optional.ofNullable(items.get(itemId));

        itemStorageValidator.validateInputWithInjectedValidator(itemDto);

        Optional<String> description = Optional.ofNullable(itemDto.getDescription());
        Optional<Boolean> available = Optional.ofNullable(itemDto.getAvailable());
        Optional<String> name = Optional.ofNullable(itemDto.getName());


        log.debug("Item to change: {}", itemDtoToChange);

        if (itemDtoToChange.isPresent()) {
            if (!itemDtoToChange.get().getOwner().equals(userDaoStorage.get(userId))) {
                throw new ExistenceOfUserException(String.format("User with number {} doesn't exist",
                        itemDto.getOwner()));
            }
        }

        description.ifPresent(d -> itemDtoToChange.get().setDescription(itemDto.getDescription()));
        available.ifPresent(aBoolean -> itemDtoToChange.get().setAvailable(aBoolean));
        name.ifPresent(s -> itemDtoToChange.get().setName(s));

        items.replace(itemId, itemDtoToChange.get());

        log.info("Updated item from ItemStorage: {}", itemDtoToChange.get());

        return items.get(itemId);
    }

    @Override
    public ItemDto delete(Object o) {
        ItemDto itemDto = (ItemDto) o;
        items.remove(itemDto.getId());

        log.info("Deleted item from ItemStorage: {}", itemDto);

        return itemDto;
    }

    public List<ItemDto> search(String text) {

        log.info("Text to search item from ItemStorage: {}", text);

        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        return items.values()
                .stream()
                .filter(itemDto -> (itemDto.getDescription().toLowerCase().contains(text.toLowerCase())
                        || itemDto.getName().toLowerCase().contains(text.toLowerCase()))
                        && itemDto.getAvailable())
                .collect(Collectors.toList());
    }

    public static long generatorId() {
        return id++;
    }

}
