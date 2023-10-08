package ru.practicum.shareit.exception.exceptions;

public class UnauthorizedAddingCommentException extends RuntimeException {
    public UnauthorizedAddingCommentException(String message) {
        super(message);
    }
}
