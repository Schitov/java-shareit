package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.ItemDaoStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserDaoStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    ItemDaoStorage itemDaoStorage;
    ItemRepository itemRepository;
    private UserRepository userRepository;
    private UserDaoStorage userDaoStorage;
    CommentRepository commentRepository;

    @Autowired
    ItemServiceImpl(ItemDaoStorage itemDaoStorage, UserDaoStorage userDaoStorage, UserRepository userRepository, ItemRepository itemRepository, CommentRepository commentRepository) {
        this.itemDaoStorage = itemDaoStorage;
        this.userDaoStorage = userDaoStorage;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ItemOwnerDto obtainItem(long id, long userId) {

        log.debug("Параметры, полученные в методе obtainItem: id - {}, userId - {}", id, userId);

        Item itemInit = itemRepository.findItemByIdWithComments(id).orElseThrow(() -> new ExistenceOfItemException("Item with id " + id + " not found"));
        if(userId != itemInit.getOwner().getId()) {
            return ItemMapper.toItemDtoOwner(itemInit);
        }

        Item item = itemRepository.findBookingsInItemById(id).orElseThrow(() -> new ExistenceOfItemException("Item with id " + id + " not found"));
        List<BookingCreateDto> bookingCreateDto = item.getBookings().stream().filter(b -> b.getStatus() == Status.APPROVED).map(BookingMapper::toBookingCreateDto).collect(Collectors.toList());
        ItemOwnerDto itemOwnerDto = ItemMapper.toItemDtoOwner(item, bookingCreateDto);

        return itemOwnerDto;
    }

    @Transactional
    public void deleteItem(long id) {
        itemRepository.deleteById(id);
    }

    public List<ItemOwnerDto> getItemsByOwnerId(Long ownerId) {

        log.debug("Параметр, полученный в методе getItemsByOwnerId: id - {}", ownerId);

        return itemRepository.findItemsWithBookings(itemRepository.findByOwnerId(ownerId, Sort.by("id")))
                .stream()
                .map(item -> ItemMapper.toItemDtoOwner(
                        item,
                        item.getBookings().stream().map(BookingMapper::toBookingCreateDto).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addCommentToItem(Long itemId, CommentDto commentDto, Long userId) {
        // Check if the item, user exists

        log.debug("Параметры, полученные в методе addCommentToItem: id - {}, userId - {}, текст комментария - {}"
                , itemId, userId, commentDto.getText());
        String text = commentDto.getText();
        if (text.isEmpty()) {
            throw new ValidException("Text musn't be empty");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ExistenceOfObjectException("Item not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ExistenceOfUserException("User not found."));


        boolean isPositive = item.getBookings().stream()
                .anyMatch(booking -> (booking.getBooker().getId() == userId) &
                        booking.getEnd().isBefore(LocalDateTime.now()));

        // Check if the user has rented the item
        if (!isPositive) {
            throw new UnauthorizedAddingCommentException("You are not authorized to add a comment for this item.");
        }

        Comment comment = ItemMapper.toNewComment(commentDto, user, item);

        return ItemMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long itemId, long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ExistenceOfItemException("Not found item"));
        if (item.getOwner().getId() != userId) {
            throw new UnauthorizedOperationException("Unauthorized access");
        }

        log.debug("Item with number: {}", item);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(item));
    }


    @Transactional
    public Item saveItem(ItemDto itemDto, long ownerId) {

        log.debug("Параметры, полученные в методе saveItem: id - {}, userId - {}"
                , itemDto.getId(), ownerId);

        // Check if the User with the specified ownerId exists in the database
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ExistenceOfUserException("User with id " + ownerId + " not found"));

        // Set the User as the owner of the new Item
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        // Save the new Item entity to the database

        return itemRepository.save(item);

    }

    public List<ItemDto> findByDescriptionContainingIgnoreCase(String searchText) {
        if(searchText.isEmpty()) {
            return new ArrayList<>();

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

        log.debug("Search text is: {searchText}", searchText);

        List<Item> items = itemRepository.findAll();

        String searchTextLower = searchText.toLowerCase();

        List<Item> result = new ArrayList<>();

        for (Item item : items) {
            String nameLower = item.getName().toLowerCase();
            String descriptionLower = item.getDescription().toLowerCase();

            if (nameLower.contains(searchTextLower) || descriptionLower.contains(searchTextLower)) {
                if(item.getAvailable() == Boolean.TRUE) {
                    result.add(item);
                }
            }
        }

        return result.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
