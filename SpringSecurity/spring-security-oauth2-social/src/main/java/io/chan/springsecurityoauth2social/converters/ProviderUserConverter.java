package io.chan.springsecurityoauth2social.converters;

@FunctionalInterface
public interface ProviderUserConverter<T, R> {
    R converter(T t);
}
