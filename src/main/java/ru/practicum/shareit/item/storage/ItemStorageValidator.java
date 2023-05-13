package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.*;
import java.util.Set;

@Service
@Slf4j
class ItemStorageValidator {

    private final Validator validator;

    public ItemStorageValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }


    void validateInputWithInjectedValidator(ItemDto item) {
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(item);

        for (ConstraintViolation<ItemDto> violation : violations) {
            log.error(violation.getMessage());
        }
    }
}