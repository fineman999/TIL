package io.chan.converters;

import io.chan.enums.ProviderEnum;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.users.FormUser;
import io.chan.springsecurityoauth2social.model.users.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.function.Function;

@Component
public class DelegatingProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {

        if (Objects.nonNull(providerUserRequest.user())) {
            User user = providerUserRequest.user();
            return FormUser.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .authorities(user.getAuthorities())
                    .provider("form")
                    .build();
        }

        Assert.notNull(providerUserRequest, "providerUserRequest cannot be null");

        return ProviderEnum.newProviderUser(
                providerUserRequest.clientRegistration().getRegistrationId(),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
