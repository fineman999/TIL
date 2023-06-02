package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("ALL")
@Slf4j
public class ProxyCastingText {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적  프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberService = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시를 구현 클래스로 캐스팅 실패, ClassCastException 예외 발생
        assertThatThrownBy(() -> {
            MemberServiceImpl proxy = (MemberServiceImpl) proxyFactory.getProxy();
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB  프록시

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberService = (MemberService) proxyFactory.getProxy();

        // CGLIB 동적 프록시를 구현 클래스로 캐스팅 성공
        MemberServiceImpl proxy = (MemberServiceImpl) proxyFactory.getProxy();

    }


}
