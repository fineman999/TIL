package io.chan.springbatch.session12;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Customer(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate
) {

}
