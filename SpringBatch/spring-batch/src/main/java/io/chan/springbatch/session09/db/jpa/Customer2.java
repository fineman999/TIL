package io.chan.springbatch.session09.db.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table
public class Customer2 {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String birthDate;
}
