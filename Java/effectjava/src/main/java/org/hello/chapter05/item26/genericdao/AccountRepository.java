package org.hello.chapter05.item26.genericdao;

import java.util.Optional;
import java.util.Set;

public class AccountRepository {
    private Set<Account> accounts;

    public AccountRepository(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Optional<Account> findById(Long id) {
        return accounts.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public void add(Account account) {
        accounts.add(account);
    }
}
