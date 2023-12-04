package io.chan.springsecurityoauth2social.converters;

import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.social.NaverUser;
import io.chan.springsecurityoauth2social.util.OAuth2Utils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser>{
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
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
