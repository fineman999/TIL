package io.chan.springbatch.session08.db.cursor;

import java.time.LocalDateTime;

/**
 * JPA 사용시 어노테이션을 등록하자.
 * @param id
 * @param name
 * @param email
 * @param phone
 * @param address
 * @param createdDate
 * @param updatedDate
 */
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
