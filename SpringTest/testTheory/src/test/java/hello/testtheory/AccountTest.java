package hello.testtheory;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;


class AccountTest {

    @Test
    void create() {
        // given
        String username = "foobar";
        String superSecretId = "f000aa01-0451-4000-b000-000000000000";
        String autoToken = UUID.fromString(superSecretId).toString();
        // when
        Account account = Account.create(username, autoToken);
        // then
        assertThat(account.getUsername()).isEqualTo("foobar");
        assertThat(account.getAutoToken()).isEqualTo("f000aa01-0451-4000-b000-000000000000");

    }

}