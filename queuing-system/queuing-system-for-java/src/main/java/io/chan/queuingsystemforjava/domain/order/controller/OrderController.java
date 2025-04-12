package io.chan.queuingsystemforjava.domain.order.controller;

import io.chan.queuingsystemforjava.common.ItemResult;
import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.dto.OrderRequest;
import io.chan.queuingsystemforjava.domain.order.dto.OrderResponse;
import io.chan.queuingsystemforjava.domain.order.service.OrderService;
import io.chan.queuingsystemforjava.domain.waitingsystem.aop.Waiting;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Waiting
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @Valid @RequestBody OrderRequest request,
        @LoginMember MemberContext memberContext
    ) {
        Member member = memberContext.getUser();
        OrderResponse response = orderService.createOrder(request, member);
        return ResponseEntity.ok(response);
    }
}