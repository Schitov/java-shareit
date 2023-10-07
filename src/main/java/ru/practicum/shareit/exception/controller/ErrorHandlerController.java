package ru.practicum.shareit.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.*;

import java.util.NoSuchElementException;


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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingException(final BookingException e) {
        return new ErrorResponse(
                "You can't booking your thing", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateIntersection(final DateIntersectionException e) {
        return new ErrorResponse(
                "End date must be after start date", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExistenceDate(final ExistenceDateException e) {
        return new ErrorResponse(
                "Date must be existed", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedState(final UnsupportedStateException e) {
        return new ErrorResponse(
                e.getMessage()
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBlockedException(final BlockedException e) {
        return new ErrorResponse(
                "Item is already boocked", e.getMessage()
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExistenceOfItem(final ExistenceOfItemException e) {
        return new ErrorResponse(
                "Existence of item error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExistenceOfUserId(final NoSuchElementException e) {
        return new ErrorResponse(
                "Existence of user error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthorizationOfUser(final UnauthorizedOperationException e) {
        return new ErrorResponse(
                "Authorization of user error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleExistenceOfBooking(final ExistenceOfBookingException e) {
        return new ErrorResponse(
                "Booking is not existed", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAddingComment(final UnauthorizedAddingCommentException e) {
        return new ErrorResponse(
                "Authorization of user error", e.getMessage()
        );
    }

}