package org.example.structural_patterns.adapter._02_after;

import org.example.structural_patterns.adapter._01_before.Account;
import org.example.structural_patterns.adapter._01_before.AccountService;
import org.example.structural_patterns.adapter._01_before.security.UserDetails;
import org.example.structural_patterns.adapter._01_before.security.UserDetailsService;

public class AccountUserDetailsService implements UserDetailsService {
    AccountService accountService;

    public AccountUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUser(String username) {
        Account accountByUsername = accountService.findAccountByUsername(username);

        return new AccountUserDetails(accountByUsername);
    }

}
