package org.example.structural_patterns.adapter._01_before;

public class AccountService {

    public Account findAccountByUsername(String username) {
        return new Account("id", "password", "name", "email");
    }

    public void notify(Account account) {
        System.out.println("notify " + account.email());
    }

    public void updateAccount(Account account) {
        System.out.println("update " + account.id());
    }
}
