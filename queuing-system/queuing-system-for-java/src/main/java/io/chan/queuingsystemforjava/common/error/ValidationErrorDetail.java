package io.chan.queuingsystemforjava.common.error;

import org.springframework.validation.BindingResult;

import java.util.List;


public record ValidationErrorDetail(String field, String value, String reason) {
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
