package io.chan.springbatchtestservice.batch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Product {
    @Id
    private Long id;
    private String name;
    private int price;
    private String type;
}
