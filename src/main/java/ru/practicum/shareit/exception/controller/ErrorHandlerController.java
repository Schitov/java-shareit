package ru.practicum.shareit.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.ErrorResponse;
import ru.practicum.shareit.exception.exceptions.ExistenceOfObjectException;
import ru.practicum.shareit.exception.exceptions.ValidException;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;


@RestControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final ValidException e) {
        if (e.getMessage().contains("Unknown state:")) {
            return new ErrorResponse(
                    e.getMessage()
            );
        } else if (e.getMessage().contains("validation.name.size.too_long}")) {
            return new ErrorResponse(
                    "Valid error", "Length of description must be less than 1000 characters"
            );
        } else {
            return new ErrorResponse(
                    "Valid error", e.getMessage()
            );
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExistenceOfObject(final ExistenceOfObjectException e) {
        return new ErrorResponse(
                "Existence of object error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExistenceOfUserId(final NoSuchElementException e) {
        return new ErrorResponse(
                "Existence of user error", e.getMessage()
        );
    }
    
}