package io.chan.bookrentalservice.application;

import io.chan.bookrentalservice.application.usecase.RentItemUseCase;
import io.chan.bookrentalservice.domain.model.vo.RentStatus;
import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserItemInputDTO;
import io.chan.bookrentalservice.setup.ApplicationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RentItemUseCaseTest extends ApplicationTest {

    @Autowired
    private RentItemUseCase rentItemUseCase;


    @DisplayName("아이템(책)을 대여한다.")
    @Test
    void rentItem() {

        UserItemInputDTO userItemInputDTO = new UserItemInputDTO("id-test", "", 1L, "자바의 정석");
        final RentalCardOutputDTO rentalCardOutputDTO = rentItemUseCase.rentItem(userItemInputDTO);

        Assertions.assertAll(
                () -> assertThat(rentalCardOutputDTO).isNotNull(),
                () -> assertThat(rentalCardOutputDTO.memberId()).isEqualTo("id-test"),
                () -> assertThat(rentalCardOutputDTO.memberName()).isEqualTo("황천길"),
                () -> assertThat(rentalCardOutputDTO.rentStatus()).isEqualTo(RentStatus.RENT_AVAILABLE.name())
        );


    }
}