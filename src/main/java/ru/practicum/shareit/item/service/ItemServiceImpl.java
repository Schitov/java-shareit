package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ExistenceOfUserException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDaoStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDaoStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    ItemDaoStorage itemDaoStorage;
    private UserDaoStorage userDaoStorage;

    @Autowired
    ItemServiceImpl(ItemDaoStorage itemDaoStorage, UserDaoStorage userDaoStorage) {
        this.itemDaoStorage = itemDaoStorage;
        this.userDaoStorage = userDaoStorage;
    }

    public Item obtainItem(long id) {
        return itemDaoStorage.get(id);
    }

    public List<Item> obtainAllItems(long userId) {

        return itemDaoStorage.getAllItems(userId)
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    public void deleteItem(long id) {
        itemDaoStorage.delete(id);
    }

    public Item updateItem(ItemDto itemDto, long itemId, long userId) {

        // Преобразуем DTO item в item
        Item item = ItemMapper.toItem(itemDto);

        // Получаем объект item по его идентификатору из хранилища (storage)
        Optional<Item> itemToChange = Optional.ofNullable(itemDaoStorage.get(itemId));

        // Оборачиваем опционально значение описания, доступности и названия товара
        Optional<String> description = Optional.ofNullable(item.getDescription());
        Optional<Boolean> available = Optional.ofNullable(item.getAvailable());
        Optional<String> name = Optional.ofNullable(item.getName());

        log.debug("Item to change: {}", itemToChange);

        // Проверяем, существует ли item, который нужно изменить
        if (itemToChange.isPresent()) {
            // Проверяем, что владелец item соответствует пользователю с заданным userId
            if (!itemToChange.get().getOwner().equals(userDaoStorage.get(userId))) {
                throw new ExistenceOfUserException(String.format("User with number {} doesn't exist",
                        itemDto.getOwner()));
            }
        }

        // Если опциональное значение описания присутствует, устанавливаем новое значение описания item
        description.ifPresent(d -> itemToChange.get().setDescription(item.getDescription()));
        // Если опциональное значение доступности присутствует, устанавливаем новое значение доступности item
        available.ifPresent(aBoolean -> itemToChange.get().setAvailable(aBoolean));
        // Если опциональное значение названия присутствует, устанавливаем новое значение названия товара
        name.ifPresent(s -> itemToChange.get().setName(s));

        return itemDaoStorage.update(itemToChange, itemId);

    }

    public Item saveItem(ItemDto itemDto, long userId) {

        // Получаем пользователя с заданным userId из userDaoStorage и оборачиваем его в Optional
        Optional<User> user = Optional.ofNullable(userDaoStorage.get(userId));

        // Проверяем, пустой ли Optional (пользователь не существует)
        if (user.isEmpty()) {
            // Генерируем исключение, указывая, что пользователь не существует
            throw new ExistenceOfUserException(String.format("User with id %d doesn't exist",
                    userId));
        }
        // Преобразуем ItemDto в объект Item с помощью ItemMapper
        Item item = ItemMapper.toItem(itemDto);

        // Устанавливаем владельца для товара, используя полученного пользователя
        item.setOwner(user.get());
        // Генерируем новый идентификатор для товара с помощью itemDaoStorage
        item.setId(itemDaoStorage.generatorId());

        // Сохраняем товар в itemDaoStorage и получаем сохраненный товар
        Item itemToReturn = itemDaoStorage.saveItem(item);

        log.debug("Saved item from ItemStorage: {}", itemToReturn);

        return itemToReturn;
    }

    public List<Item> searchItemsByText(String text) {

        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemDaoStorage.search(text)
                .stream()
                .filter(itemDto -> (itemDto.getDescription().toLowerCase().contains(text.toLowerCase())
                        || itemDto.getName().toLowerCase().contains(text.toLowerCase()))
                        && itemDto.getAvailable())
                .collect(Collectors.toList());
    }


}
