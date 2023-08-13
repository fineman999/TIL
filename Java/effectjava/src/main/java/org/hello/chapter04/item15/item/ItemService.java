package org.hello.chapter04.item15.item;

import org.hello.chapter04.item15.member.MemberService;

public class ItemService {
    MemberService memberService;
    boolean onSale;
    protected int saleRate;

    public ItemService(MemberService memberService) {
        if (memberService == null) {
            throw new IllegalArgumentException("memberService must not be null");
        }
        this.memberService = memberService;
    }
}
