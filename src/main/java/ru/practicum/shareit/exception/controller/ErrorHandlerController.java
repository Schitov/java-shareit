package ru.practicum.shareit.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.ErrorResponse;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.exception.exceptions.ExistenceOfUserException;
import ru.practicum.shareit.exception.exceptions.ValidException;


@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final ValidException e) {
        return new ErrorResponse(
                "Valid error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleExistenceOfObjects(final ExistenceOfObjectException e) {
        return new ErrorResponse(
                "Existence error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExistenceOfObjects(final RuntimeException e) {
        return new ErrorResponse(
                "Other error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExistenceOfUser(final ExistenceOfUserException e) {
        return new ErrorResponse(
                "Existence of user error", e.getMessage()
        );
    }
}