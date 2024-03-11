package io.chan.bookrentalservice.application;

import io.chan.bookrentalservice.application.outputport.RentalCardOutputPort;
import io.chan.bookrentalservice.application.usecase.CreateRentalCardUseCase;
import io.chan.bookrentalservice.domain.model.RentalCard;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;
import io.chan.bookrentalservice.setup.ApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class CreateRentalCardUseCaseTest extends ApplicationTest {

    @Autowired
    private RentalCardOutputPort rentalCardOutputPort;

    @Autowired
    private CreateRentalCardUseCase createRentalCardUseCase;

    @DisplayName("대출 가능한 카드를 생성한다.")
    @Test
    void createRentalCard() {
        var userInputDTO = new UserInputDTO("id", "홍길동");
        var rentalCard = createRentalCardUseCase.createRentalCard(userInputDTO);
        final Optional<RentalCard> savedRentalCard = rentalCardOutputPort.loadRentalCard(rentalCard.memberId());
        Assertions.assertAll(
                () -> assertThat(savedRentalCard).isNotNull(),
                () -> assertThat(savedRentalCard.get().getMember().getId()).isEqualTo(rentalCard.memberId())
            );
    }
}