package io.chan.springngrindertest.config;

import jakarta.annotation.Nonnull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PageKeyGenerator implements KeyGenerator {
    public static final String PREFIX = "Pageable::";

    @Nonnull
    @Override
    public Object generate(
            @Nonnull final Object target,
            @Nonnull final Method method,
            @Nonnull final Object... params
    ) {
        return Arrays.stream(params)
                .filter(Pageable.class::isInstance)
                .findFirst()
                .map(Pageable.class::cast)
                .map(pageable-> PREFIX + pageable.getPageNumber() + "_" + pageable.getPageSize())
                .orElse(SimpleKeyGenerator.generateKey(params).toString());
    }
}
