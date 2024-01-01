package io.chan.springsecuritydocsexample;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @PreAuthorize("hasRole('ADMIN')")
    public String readAccount(String number) {
        return "admin";
    }
}

