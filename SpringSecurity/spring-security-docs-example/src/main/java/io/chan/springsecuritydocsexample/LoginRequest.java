package io.chan.springsecuritydocsexample;

public record LoginRequest (
        String username,
        String password
){
}
