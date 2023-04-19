package com.example.demo.beanfind;

import com.example.demo.AppConfig;
import com.example.demo.member.MemberService;
import com.example.demo.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertInstanceOf(MemberServiceImpl.class, memberService);
    }
    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertInstanceOf(MemberServiceImpl.class, memberService);
    }
    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByClassImpl() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertInstanceOf(MemberServiceImpl.class, memberService);
    }
    @Test
    @DisplayName("빈으로 조회X")
    void findBeanByNameX() {
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("notMemberService", MemberServiceImpl.class));
    }
}
