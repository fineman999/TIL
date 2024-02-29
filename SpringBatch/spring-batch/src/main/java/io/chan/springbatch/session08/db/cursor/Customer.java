package io.chan.springbatch.session08.db.cursor;

import java.time.LocalDateTime;

public record Customer (
        Long id,
        String name,
        String email,
        String phone,
        String address,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
){
}
