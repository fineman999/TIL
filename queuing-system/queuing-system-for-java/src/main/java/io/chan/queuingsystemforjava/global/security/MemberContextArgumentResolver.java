package io.chan.queuingsystemforjava.global.security;

import io.chan.queuingsystemforjava.common.ErrorCode;
import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.common.TicketingException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class MemberContextArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean hasLongParameterType = parameter.getParameterType().equals(MemberContext.class);
        return hasParameterAnnotation && hasLongParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        checkAuthenticated(authentication);
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        return authenticationToken.getPrincipal();
    }

    private void checkAuthenticated(Authentication authentication) {
        if (authentication != null) {
            return;
        }
        throw new TicketingException(ErrorCode.UNAUTHORIZED);
    }
}
