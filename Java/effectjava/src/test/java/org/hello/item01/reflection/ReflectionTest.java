package org.hello.item01.reflection;

import org.hello.item01.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    @DisplayName("Class 문자열로 해당 클래스를 가져올수 있는가?")
    void test() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("org.hello.item01.ChineseHelloService");
        Constructor<?> constructor = aClass.getConstructor();
        HelloService helloService = (HelloService) constructor.newInstance();
        assertThat(helloService.hello("")).isEqualTo("你好 ");
    }

    @Test
    @DisplayName("만약 클래스의 메소드를 가져오고 싶으면?")
    void test2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("org.hello.item01.ChineseHelloService");
        String invoke = (String) aClass.getDeclaredMethod("hello", String.class).invoke(aClass.getConstructor().newInstance(), "홍길동");
        assertThat(invoke).isEqualTo("你好 홍길동");
    }

}
