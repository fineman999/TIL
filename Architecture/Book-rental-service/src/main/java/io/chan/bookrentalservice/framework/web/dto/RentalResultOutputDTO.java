package io.chan.bookrentalservice.framework.web.dto;

public record RentalResultOutputDTO(
    String userId,
    String username,
    int rentedCount,
    int totalLateFee
) {
}
