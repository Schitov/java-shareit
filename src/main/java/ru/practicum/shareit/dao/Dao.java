package ru.practicum.shareit.dao;

public interface Dao<T> {
    T get(long id);
}
