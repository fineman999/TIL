package org.example.structural_patterns.adapter._02_after;

import org.example.structural_patterns.adapter._01_before.Account;
import org.example.structural_patterns.adapter._01_before.security.UserDetails;

public class AccountUserDetails implements UserDetails {
    private Account account;

    public AccountUserDetails(Account account) {
        this.account = account;
    }


    @Override
    public String getUsername() {
        return account.name();
    }

    @Override
    public String getPassword() {
        return account.password();
    }
}
