package io.chan.springcoresecurity.security.manager;

import io.chan.springcoresecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class OpenPolicyAgentAuthorizationManager  implements AuthorizationManager<HttpServletRequest> {

    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);

    private final Log logger = LogFactory.getLog(getClass());

    private final List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;



    @Override
    public void verify(Supplier<Authentication> authentication, HttpServletRequest object) {
        AuthorizationDecision decision = check(authentication, object);
        if (decision != null && !decision.isGranted()) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Authorizing %s", request));
        }
        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request);
            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace(LogMessage.format("Checking authorization on %s using %s", request, manager));
                }
                return manager.check(authentication,
                        new RequestAuthorizationContext(request, matchResult.getVariables()));
            }
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.of(() -> "Denying request since did not find matching RequestMatcher"));
        }
        return DENY;
    }
}