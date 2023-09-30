package org.example.structural_patterns.adapter;

import org.example.structural_patterns.adapter._01_before.AccountService;
import org.example.structural_patterns.adapter._01_before.security.LoginHandler;
import org.example.structural_patterns.adapter._02_after.AccountUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.*;

class AdapterPatternsTest {

    private LoginHandler loginHandler;


    @BeforeEach
    void setUp() {
        AccountService accountService = new AccountService();
        AccountUserDetailsService userDetailsService = new AccountUserDetailsService(accountService);
        loginHandler = new LoginHandler(userDetailsService);

    }

    @Test
    @DisplayName("어댑터 패턴 테스트")
    void name() {
        loginHandler.login("user1", "pass1");


    }

    @Test
    @DisplayName("Arrays 클래스의 asList 메서드 테스트")
    void name2() {
        // Arrays 클래스의 asList 메서드는 List 인터페이스를 구현한 ArrayList 인스턴스를 반환한다.
        // 이때 반환된 ArrayList 인스턴스는 Arrays 클래스의 private static class ArrayList<E> 에서 정의된 메서드를 사용한다.
        // 이렇게 어댑터 패턴을 사용하면 기존의 클래스를 수정하지 않고도 새로운 인터페이스를 구현할 수 있다.
        List<String> strings = Arrays.asList("a", "b", "c");

        Enumeration<String> enumeration = Collections.enumeration(strings);
        ArrayList<String> list = Collections.list(enumeration);


    }

    @Test
    @DisplayName("HandlerAdapter 인터페이스 테스트")
    void test() {
        HandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
    }
}