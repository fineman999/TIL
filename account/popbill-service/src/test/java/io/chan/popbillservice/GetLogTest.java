package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import com.popbill.api.taxinvoice.TaxinvoiceLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GetLogTest {
  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("로그 확인")
  @Test
  void getLogs() throws PopbillException {
    /**
     * 세금계산서의 상태에 대한 변경이력을 확인합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#GetLogs
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    TaxinvoiceLog[] taxinvoiceLogs =
        taxinvoiceService.getLogs(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    for (TaxinvoiceLog taxinvoiceLog : taxinvoiceLogs) {
      log.info("로그 : {}", taxinvoiceLog.toString());
    }
    // 1. 임시저장 되었습니다. - 나
    // 2. 발행 되었습니다. - 나
    // 3. 공급받는자가 미등록 상태입니다. - 시스템
    //4. 발행자가 국세청전송을 요청하였습니다. - 나
    // 5. 국세청 전송이 대기중입니다. - 시스템
    // 6. 국세청 전송이 진행중입니다. - 시스템
    // 7. 국세청 전송이 접수되었습니다. - 시스템
    // 8. 국세청 전송이 완료되었습니다. - 시스템
  }
}
