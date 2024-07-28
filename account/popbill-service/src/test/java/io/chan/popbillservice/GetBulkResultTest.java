package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.BulkTaxinvoiceResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GetBulkResultTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("1. 대량 발행 접수 결과 확인")
  @Test
  void getBulkResult() throws PopbillException {
    /**
     * 접수시 기재한 SubmitID를 사용하여 세금계산서 접수결과를 확인합니다. - 개별 세금계산서 처리상태는 접수상태(txState)가 완료(2) 시 반환됩니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/issue#GetBulkResult
     */

    // 대량 발행 접수시 기재한 제출아이디
    String SubmitID = "20230102-BOOT-BULK";
    BulkTaxinvoiceResult bulkResult =
        taxinvoiceService.getBulkResult(popbillConfig.getCorpNum(), SubmitID);

    log.info("접수아이디 : {}", bulkResult.getSubmitID());
    log.info("접수상태 : {}", bulkResult.getCode());
    log.info("접수상태 메시지 : {}", bulkResult.getMessage());

  }
}
