package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long ownerId, Sort sort);

    boolean existsByOwnerEmail(Email email);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Item> findByDescriptionContainingIgnoreCase(String searchText);

    @Query("SELECT item FROM Item item " +
            "LEFT JOIN FETCH item.comments c " +
            "WHERE item.id = :itemId")
    Optional<Item> findItemByIdWithComments(long itemId);

    @Query(" SELECT DISTINCT item FROM Item item " +
            " LEFT JOIN FETCH item.comments c " +
            " LEFT JOIN FETCH c.author " +
            " WHERE item.owner = :itemId")
    List<Item> findItemsByOwnerIdWithComments(int itemId);

    @Query("SELECT DISTINCT i FROM Item i " +
            "LEFT JOIN FETCH i.bookings " +
            "LEFT JOIN FETCH i.owner " +
            "WHERE i.id = :itemId")
    Optional<Item> findBookingsInItemById(Long itemId);

    @Query(" SELECT DISTINCT item FROM Item item " +
            " LEFT JOIN FETCH item.bookings book " +
            " LEFT JOIN FETCH book.booker " +
            " WHERE item IN ?1")
    List<Item> findItemsWithBookings(List<Item> items);
}
