package io.chan.bookrentalservice.domain.model;

import io.chan.bookrentalservice.feature.ItemFixture;
import io.chan.bookrentalservice.feature.RentalCardFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 테스트 시나리오 구상해 보기
 * 도서샘플 1,2 생성
 *
 * 1. 도서 1 ,2 대여
 * 2. 도서 1 반납
 * 3. 도서2 강제연체 처리 대여정지됨
 * 4. 도서2 반납 연체료 계산됨
 * 5. 정지해제처리 포인트로 연체료 삭감
 */
class RentalTest {

    @DisplayName("테스트 시나리오")
    @Test
    void domainTest() {
        // given
        var item1 = ItemFixture.create(1L, "노인과 바다");
        var item2 = ItemFixture.create(2L, "죄와 벌");
        final RentalCard rentalCard = RentalCardFixture.createRentalCard();

        String expected = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[]
                총 연체료: 0
                대여 가능 여부: RENT_AVAILABLE
                대여 목록
                -[]
                반납 목록
                -[]""";
        assertThat(rentalCard.toString()).hasToString(expected);

        // item1 대여하기
        RentalCardFixture.rent(rentalCard, item1);
        String expected2 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[false]
                총 연체료: 0
                대여 가능 여부: RENT_AVAILABLE
                대여 목록
                -[노인과 바다]
                반납 목록
                -[]""";
        assertThat(rentalCard.toString()).hasToString(expected2);

        //item2 대여하기
        RentalCardFixture.rent(rentalCard, item2);
        String expected3 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[false, false]
                총 연체료: 0
                대여 가능 여부: RENT_AVAILABLE
                대여 목록
                -[노인과 바다, 죄와 벌]
                반납 목록
                -[]""";
        assertThat(rentalCard.toString()).hasToString(expected3);

        // item1 반납하기
        RentalCardFixture.returnItem(rentalCard, item1);
        String expected4 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[false]
                총 연체료: 0
                대여 가능 여부: RENT_AVAILABLE
                대여 목록
                -[죄와 벌]
                반납 목록
                -[노인과 바다]""";
        assertThat(rentalCard.toString()).hasToString(expected4);

        // item2 강제 연체
        RentalCardFixture.overdueItem(rentalCard, item2);
        String expected5 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[true]
                총 연체료: 0
                대여 가능 여부: RENT_UNAVAILABLE
                대여 목록
                -[죄와 벌]
                반납 목록
                -[노인과 바다]""";
        assertThat(rentalCard.toString()).hasToString(expected5);

        // 연체된 item2 반납
        RentalCardFixture.returnItemAfter16Days(rentalCard, item2);
        String expected6 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[]
                총 연체료: 20
                대여 가능 여부: RENT_UNAVAILABLE
                대여 목록
                -[]
                반납 목록
                -[노인과 바다, 죄와 벌]""";
        assertThat(rentalCard.toString()).hasToString(expected6);

        // 포인트 20을 사용해서 대여 가능하게 변환
        RentalCardFixture.makeAvailable(rentalCard, 20L);
        String expected7 = """
                도서 카드: 홍길동님의 대여 목록
                대여 도서 연체 상태:[]
                총 연체료: 0
                대여 가능 여부: RENT_AVAILABLE
                대여 목록
                -[]
                반납 목록
                -[노인과 바다, 죄와 벌]""";
        assertThat(rentalCard.toString()).hasToString(expected7);
    }
}
