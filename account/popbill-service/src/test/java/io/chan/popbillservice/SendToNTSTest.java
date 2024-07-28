package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SendToNTSTest {

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired
    private PopbillConfig popbillConfig;

    @DisplayName("국세청 전송")
    @Test
    void sendToNTS() throws PopbillException {
        /**
         * 공급자가 "발행완료" 상태의 전자세금계산서를 국세청에 즉시 전송하며, 함수 호출 후 최대 30분 이내에 전송 처리가 완료됩니다.
         * - 국세청 즉시전송을 호출하지 않은 세금계산서는 발행일 기준 다음 영업일 오후 3시에 팝빌 시스템에서 일괄적으로 국세청으로 전송합니다.
         * - https://developers.popbill.com/reference/taxinvoice/java/api/issue#SendToNTS
         */

        // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
        MgtKeyType mgtKeyType = MgtKeyType.SELL;

        // 세금계산서 문서번호
        String mgtKey = "20240728-BOOT003";
    Response response = taxinvoiceService.sendToNTS(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    log.info("국세청 전송 결과 : {}", response.toString());
    // 한번 전송되면 - 국세청 전송중 이거나, 전송완료된 (세금)계산서입니다.
    }
}
