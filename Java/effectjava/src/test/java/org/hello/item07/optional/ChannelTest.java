package org.hello.item07.optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ChannelTest {


    @Test
    void test() {
        Channel channel = new Channel();
        Optional<MemberShip> memberShip = channel.defaultMemberShip();
        memberShip.ifPresent(MemberShip::hello);
    }

    @Test
    @DisplayName("NoSuchElementException")
    void test2() {
        Channel channel = new Channel();
        Optional<MemberShip> optional = channel.defaultMemberShip();
        assertThatThrownBy(optional::get)
                .isInstanceOf(NoSuchElementException.class);
    }
}