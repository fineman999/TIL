package io.chan.popbillservice.send;

import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import io.chan.popbillservice.PopbillConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class SendEmailTest {

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired
    private PopbillConfig popbillConfig;

    @DisplayName("이메일 전송")
    @Test
    void sendEmail() throws PopbillException {
        /**
         * 세금계산서와 관련된 안내 메일을 재전송 합니다.
         * - https://developers.popbill.com/reference/taxinvoice/java/api/etc#SendEmail
         */

        // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
        MgtKeyType mgtKeyType = MgtKeyType.SELL;

        // 세금계산서 문서번호
        String mgtKey = "20240728-BOOT003";

        // 수신메일주소
        String receiverMail = "chpark@witchcompay.io";
    Response response =
        taxinvoiceService.sendEmail(popbillConfig.getCorpNum(), mgtKeyType, mgtKey, receiverMail);

        log.info("이메일 전송 결과 : {}", response.toString());
        }
}
