package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ExistenceOfItemException;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.exception.exceptions.ExistenceOfUserException;
import ru.practicum.shareit.exception.exceptions.UnauthorizedOperationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.ItemDaoStorage;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserDaoStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
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

    public ItemOwnerDto obtainItem(long id, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ExistenceOfUserException("User with id " + userId + " not found"));
        UserDto userDto = UserMapper.userToDto(user);



        Item item = itemRepository.findById(id).orElseThrow(() -> new ExistenceOfItemException("Item with id " + id + " not found"));
        ItemOwnerDto itemOwnerDto = ItemMapper.toItemDtoOwner(item);
        itemOwnerDto.setOwnerId(userDto.getId());
        return itemOwnerDto;
    }

//    public List<Item> obtainAllItems(long userId) {
//
//        return itemDaoStorage.getAllItems(userId)
//                .stream()
//                .filter(item -> item.getOwner().getId() == userId)
//                .collect(Collectors.toList());
//    }

    public void deleteItem(long id) {
        itemRepository.deleteById(id);
    }

    public List<ItemOwnerDto> getItemsByOwnerId(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDtoOwner)
                .collect(Collectors.toList());
    }


    public Comment addCommentToItem(Long itemId, CommentDto commentDto, Long userId) {
        // Check if the item exists
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ExistenceOfObjectException("Item not found."));

        // Check if the user has rented the item
        if (item.getOwner().getId() != userId) {
            throw new UnauthorizedOperationException("You are not authorized to add a comment for this item.");
        }

        Comment comment = CommentMapper.toComment(commentDto);

        return commentRepository.save(comment);
    }

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

    public Item saveItem(ItemDto itemDto, long ownerId) {

        // Check if the User with the specified ownerId exists in the database
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ExistenceOfUserException("User with id " + ownerId + " not found"));

        // Set the User as the owner of the new Item
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        // Save the new Item entity to the database

        return itemRepository.save(item);

    }

//    public List<Item> searchItemsByText(String searchText) {
//        searchText = searchText.toLowerCase();
//        return itemRepository.findByDescriptionContainingIgnoreCase(searchText);
//    }

    public List<ItemDto> findByDescriptionContainingIgnoreCase(String searchText) {
        if(searchText.isEmpty()) {
            return new ArrayList<>();
        }

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

//    public List<Item> searchItemsByText(String text) {
//
//        if (text.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return itemDaoStorage.search(text)
//                .stream()
//                .filter(itemDto -> (itemDto.getDescription().toLowerCase().contains(text.toLowerCase())
//                        || itemDto.getName().toLowerCase().contains(text.toLowerCase()))
//                        && itemDto.getAvailable())
//                .collect(Collectors.toList());
//    }


}
