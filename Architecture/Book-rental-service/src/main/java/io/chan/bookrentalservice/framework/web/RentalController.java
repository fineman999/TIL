package io.chan.bookrentalservice.framework.web;

import io.chan.bookrentalservice.application.usecase.*;
import io.chan.bookrentalservice.framework.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentItemUseCase rentItemUseCase;
    private final ReturnItemUserCase returnItemUserCase;
    private final CreateRentalCardUseCase createRentalCardUseCase;
    private final OverdueItemUseCase overdueItemUseCase;
    private final ClearOverdueItemUseCase clearOverdueItemUseCase;
    private final InQueryUseCase inQueryUseCase;

    @PostMapping
    public ResponseEntity<RentalCardOutputDTO> createRentalCard(
            @RequestBody UserInputDTO userInputDTO
    ) {
        final var rentalCard = createRentalCardUseCase.createRentalCard(userInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalCard);
    }

    @PostMapping("/rent")
    public ResponseEntity<RentalCardOutputDTO> rentItem(
            @RequestBody UserItemInputDTO userItemInputDTO
    ) {
        final var rentalCard = rentItemUseCase.rentItem(userItemInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalCard);
    }

    @PostMapping("/return")
    public ResponseEntity<RentalCardOutputDTO> returnItem(
            @RequestBody UserItemInputDTO userItemInputDTO
    ) {
        final var rentalCard = returnItemUserCase.returnItem(userItemInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalCard);
    }

    @PostMapping("/overdue")
    public ResponseEntity<RentalCardOutputDTO> overdueItem(
            @RequestBody UserItemInputDTO userItemInputDTO
    ) {
        final var rentalCard = overdueItemUseCase.overdueItem(userItemInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalCard);
    }

    @GetMapping("{memberId}")
    public ResponseEntity<RentalCardOutputDTO> getRentalCard(
            @PathVariable String memberId
    ) {
        final var rentalCard = inQueryUseCase.getRentalCard(new UserInputDTO(memberId,""));
        return ResponseEntity.status(HttpStatus.OK).body(rentalCard);
    }

    @GetMapping("{memberId}/rent")
    public ResponseEntity<List<RentItemOutputDTO>> getRentItems(
            @PathVariable String memberId
    ) {
        final var rentalItems = inQueryUseCase.getAllRentItem(new UserInputDTO(memberId,""));
        return ResponseEntity.status(HttpStatus.OK).body(rentalItems);
    }

    @GetMapping("{memberId}/return")
    public ResponseEntity<List<ReturnItemOutputDTO>> getReturnItems(
            @PathVariable String memberId
    ) {
        final var returnItems = inQueryUseCase.getAllReturnItem(new UserInputDTO(memberId,""));
        return ResponseEntity.status(HttpStatus.OK).body(returnItems);
    }

    @PostMapping("/clear-overdue")
    public ResponseEntity<RentalResultOutputDTO> clearOverdueItem(
            @RequestBody ClearOverdueInfoDto clearOverdueInfoDto
    ) {
        final var rentalCard = clearOverdueItemUseCase.clearOverdueItem(clearOverdueInfoDto);
        return ResponseEntity.status(HttpStatus.OK).body(rentalCard);
    }
}
