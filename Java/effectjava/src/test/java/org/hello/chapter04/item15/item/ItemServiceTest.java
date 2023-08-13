package org.hello.chapter04.item15.item;

import org.assertj.core.api.Assertions;
import org.hello.chapter04.item15.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    MemberService memberService;

    @Test
    void test() {
        ItemService itemService = new ItemService(memberService);
        assertNotNull(itemService);
        assertNotNull(itemService.memberService);
        itemService.onSale = true;
        itemService.saleRate = 10;
    }

}