package io.chan.queuingsystemforjava.domain.order.dto;

import io.chan.queuingsystemforjava.domain.order.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record OrderSearchRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate,
    
    String orderName,
    OrderStatus status
) {}
