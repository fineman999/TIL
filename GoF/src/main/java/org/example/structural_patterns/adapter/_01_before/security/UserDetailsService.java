package org.example.structural_patterns.adapter._01_before.security;

public interface UserDetailsService {
    UserDetails loadUser(String username);
}
