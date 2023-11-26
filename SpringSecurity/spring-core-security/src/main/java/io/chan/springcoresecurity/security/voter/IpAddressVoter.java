package io.chan.springcoresecurity.security.voter;

import io.chan.springcoresecurity.domain.entity.AccessIp;
import io.chan.springcoresecurity.security.service.SecurityResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class IpAddressVoter implements AccessDecisionVoter {

    private final SecurityResourceService securityResourceService;

    /**
     * IpAddressVoter 클래스는 AccessDecisionVoter 인터페이스를 구현하고 있습니다.
     * 이 클래스는 투표를 진행하는 클래스입니다.
     * 투표를 진행하는 메서드는 vote() 메서드입니다.
     * 이 메서드는 Authentication 객체와 요청 정보를 받아서 투표를 진행합니다.
     * Authentication 객체는 인증된 사용자 정보를 담고 있습니다.
     * 요청 정보는 HttpServletRequest 객체를 사용하면 되지만, Spring Security에서는 Authentication 객체의 getDetails() 메서드를 통해서 요청 정보를 받아올 수 있습니다.
     * getDetails() 메서드는 Object 타입으로 리턴되기 때문에 WebAuthenticationDetails 타입으로 캐스팅을 해줘야 합니다.
     * WebAuthenticationDetails 객체는 Spring Security에서 인증 과정에서 생성되는 객체입니다.
     * 이 객체는 인증 과정에서 생성되는 객체이기 때문에 인증 과정에서 생성되지 않은 경우에는 사용할 수 없습니다.
     * 따라서, 인증 과정에서 생성되지 않은 경우에는 HttpServletRequest 객체를 사용해야 합니다.
     * 이 메서드는 투표 결과를 리턴합니다.
     * 투표 결과는 ACCESS_GRANTED, ACCESS_ABSTAIN, ACCESS_DENIED 중 하나를 리턴합니다.
     * 투표 결과는 ACCESS_ABSTAIN을 리턴하면 다른 Voter에게 투표를 위임합니다.
     * 예외 처리를 위해서 AccessDeniedException 예외가 발생합니다.
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        for (AccessIp accessIp : securityResourceService.getAccessIps()) {
            if (accessIp.isIpMatched(remoteAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        throw new AccessDeniedException("Invalid IpAddress");
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }
}
