package hello.testtheory;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;


class AccountTest {

    @Test
    void create() {
        // given
        String username = "foobar";
        String autoToken = UUID.randomUUID().toString();
        // when
        Account account = Account.create(username, autoToken);
        // then
        assertThat(account.getUsername()).isEqualTo("foobar");
        assertThat(account.getAutoToken()).isEqualTo(autoToken);


    }

}