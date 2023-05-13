package ru.practicum.shareit.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    T get(long id);
}
