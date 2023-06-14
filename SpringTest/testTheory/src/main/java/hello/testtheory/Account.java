package hello.testtheory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Account {

    private final String username;
    private final String autoToken;

    public static Account create(String username, String autoToken) {
        return Account.builder()
                .username(username)
                .autoToken(autoToken)
                .build();
    }
}
