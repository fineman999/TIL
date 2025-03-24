package io.chan.queuingsystemforjava.common.error;

import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.List;

@Data
public class ValidationErrorDetail {
    private final String field;
    private final String value;
    private final String reason;

    public static List<ValidationErrorDetail> of(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(
                        error ->
                                new ValidationErrorDetail(
                                        error.getField(),
                                        error.getRejectedValue() != null
                                                ? error.getRejectedValue().toString()
                                                : null,
                                        error.getDefaultMessage()))
                .toList();
    }
}
