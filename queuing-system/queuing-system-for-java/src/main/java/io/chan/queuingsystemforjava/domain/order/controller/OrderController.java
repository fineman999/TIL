package io.chan.queuingsystemforjava.domain.order.controller;

import io.chan.queuingsystemforjava.common.LoginMember;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.order.dto.*;
import io.chan.queuingsystemforjava.domain.order.service.OrderCreationService;
import io.chan.queuingsystemforjava.domain.order.service.OrderQueryService;
import io.chan.queuingsystemforjava.domain.waitingsystem.aop.Waiting;
import io.chan.queuingsystemforjava.global.security.MemberContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
  private final OrderCreationService orderCreationService;
  private final OrderQueryService orderQueryService;

  @Waiting
  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(
      @Valid @RequestBody OrderRequest request, @LoginMember MemberContext memberContext) {
    Member member = memberContext.getUser();
    OrderResponse response = orderCreationService.createOrder(request, member);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<OrderListResponse>> getOrders(
      @LoginMember MemberContext memberContext,
      @ModelAttribute OrderSearchRequest searchRequest,
      Pageable pageable) {
    Member member = memberContext.getUser();
    Page<OrderListResponse> orders = orderQueryService.getOrders(member, searchRequest, pageable);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDetailResponse> getOrderDetail(
      @LoginMember MemberContext memberContext, @PathVariable String orderId) {
    Member member = memberContext.getUser();
    OrderDetailResponse order = orderQueryService.getOrderDetail(member, orderId);
    return ResponseEntity.ok(order);
  }
}
