package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User {
    private final Member member;

    private MemberContext(Member user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.member = user;
    }

    public static MemberContext createAuth(Member user, Collection<? extends GrantedAuthority> authorities) {
        return new MemberContext(user, authorities);
    }


    public Member getUser() {
        return member;
    }
}
