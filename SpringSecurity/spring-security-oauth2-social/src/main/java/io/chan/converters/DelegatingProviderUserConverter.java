package io.chan.converters;

import io.chan.enums.ProviderEnum;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.social.NaverUser;
import io.chan.util.OAuth2Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.Function;

@Component
public class DelegatingProviderUserConverter implements Function<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser apply(ProviderUserRequest providerUserRequest) {
        Assert.notNull(providerUserRequest, "providerUserRequest cannot be null");
        providerUserRequest.oAuth2User().getAttribute("response");
        return ProviderEnum.newProviderUser(
                OAuth2Utils.getAttributes(providerUserRequest.oAuth2User(), NaverUser.USER_NAME_ATTRIBUTE_NAME, "id"),
                providerUserRequest.clientRegistration().getRegistrationId(),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration()
        );
    }
}
