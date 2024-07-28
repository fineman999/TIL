package io.chan.popbillservice.info;

import com.popbill.api.ChargeInfo;
import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import io.chan.popbillservice.PopbillConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class ChargeInfoTest {

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired
    private PopbillConfig popbillConfig;

    @DisplayName("과금정보 확인")
    @Test
    void chargeInfo() throws PopbillException {
    /**
     * 팝빌 전자세금계산서 API 서비스 과금정보를 확인합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/point#GetChargeInfo
     */
        ChargeInfo chrgInfo = taxinvoiceService.getChargeInfo(popbillConfig.getCorpNum());

        log.info("과금정보 확인 : {}", chrgInfo.toString());
        assertThat(chrgInfo.getUnitCost()).isEqualTo("100");
        assertThat(chrgInfo.getChargeMethod()).isEqualTo("파트너");
        assertThat(chrgInfo.getRateSystem()).isEqualTo("종량제");
    }

    @DisplayName("포인트 단가")
    @Test
    void unitCost() throws PopbillException {
        /**
         * 팝빌 전자세금계산서 API 서비스 과금정보를 확인합니다. -
         * https://developers.popbill.com/reference/taxinvoice/java/api/point#GetUnitCost
         */
        final float unitCost = taxinvoiceService.getUnitCost(popbillConfig.getCorpNum());

        log.info("포인트 단가 : {}", unitCost);
        assertThat(unitCost).isEqualTo(100.0);
    }

}
