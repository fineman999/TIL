package io.chan.springbatch.session08.db.paging;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;

    @OneToOne(mappedBy = "customer")
    private Address address;
}
