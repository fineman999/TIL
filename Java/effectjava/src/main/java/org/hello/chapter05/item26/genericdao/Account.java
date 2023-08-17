package org.hello.chapter05.item26.genericdao;

public class Account implements Entity {
    private Long id;

    private String name;
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
