package io.chan.springsecuritydocsexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankServiceTest {

    @Autowired
    BankService bankService;

    @WithMockUser(roles="ADMIN")
    @Test
    void readAccountWithAdminRoleThenInvokes() {
        this.bankService.readAccount("12345678");
        // ... assertions
    }

    @WithMockUser(roles="WRONG")
    @Test
    void readAccountWithWrongRoleThenAccessDenied() {
        assertThatExceptionOfType(AccessDeniedException.class).isThrownBy(
                () -> this.bankService.readAccount("12345678"));
    }


}