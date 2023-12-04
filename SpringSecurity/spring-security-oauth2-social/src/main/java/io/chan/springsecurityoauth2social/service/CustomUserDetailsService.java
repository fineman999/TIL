package io.chan.springsecurityoauth2social.service;

import io.chan.certification.SelfCertification;
import io.chan.converters.ProviderUserRequest;
import io.chan.springsecurityoauth2social.model.PrincipalUser;
import io.chan.springsecurityoauth2social.model.ProviderUser;
import io.chan.springsecurityoauth2social.model.users.User;
import io.chan.springsecurityoauth2social.repository.UserRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Form 로그인을 위한 UserDetailsService 구현체
 */
@Service
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserService userService, UserRepository userRepository, Function<ProviderUserRequest, ProviderUser> providerUserConverter, SelfCertification certification) {
        super(userService, userRepository, providerUserConverter, certification);
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> User.builder()
                        .id("1")
                        .username(username)
                        .password("{noop}1234")
                        .authorities(AuthorityUtils.createAuthorityList("ROLE_USER"))
                        .email("user@hho.com")
                        .build());
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(user);
        ProviderUser providerUser = providerUser(providerUserRequest);

        selfCertificate(providerUser);

        return new PrincipalUser(providerUser);
    }
}
