package io.chan.springbatch.session08.db.paging;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String location;
    private int zipCode;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
