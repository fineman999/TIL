package org.example.structural_patterns.adapter;

import org.example.structural_patterns.adapter._01_before.AccountService;
import org.example.structural_patterns.adapter._01_before.security.LoginHandler;
import org.example.structural_patterns.adapter._02_after.AccountUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdapterPatternsTest {

    private LoginHandler loginHandler;


    @BeforeEach
    void setUp() {
        AccountService accountService = new AccountService();
        AccountUserDetailsService userDetailsService = new AccountUserDetailsService(accountService);
        loginHandler = new LoginHandler(userDetailsService);

    }

    @Test
    @DisplayName("어댑터 패턴 테스트")
    void name() {
        loginHandler.login("user1", "pass1");


    }
}