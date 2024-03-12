package io.chan.bookrentalservice.framework.web;

import io.chan.bookrentalservice.framework.web.dto.RentalCardOutputDTO;
import io.chan.bookrentalservice.framework.web.dto.UserInputDTO;
import io.chan.bookrentalservice.setup.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

class RentalControllerTest extends ControllerTest {

    @Test
    void createRentalCard() {
        var userInputDTO = new UserInputDTO("id", "홍길동");
        var rentalCardOutputDTO = new RentalCardOutputDTO(
                "uuid-1",
                "id",
                "홍길동",
                "RENT_AVAILABLE",
                0L,
                0L,
                0L,
                0L
        );

        doReturn(rentalCardOutputDTO)
                .when(createRentalCardUseCase)
                .createRentalCard(any());
        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(userInputDTO)
                .when().post("/api/v1/rentals")
                .then().log().all()
                .apply(document("rental/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void rentItem() {
    }

    @Test
    void returnItem() {
    }

    @Test
    void overdueItem() {
    }

    @Test
    void getRentalCard() {
    }

    @Test
    void getRentItems() {
    }

    @Test
    void getReturnItems() {
    }

    @Test
    void clearOverdueItem() {
    }
}