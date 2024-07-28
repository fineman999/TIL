package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CancelIssueTest {
  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("세금계산서 취소")
  @Test
  void cancelIssue() throws PopbillException {
    /**
     * 국세청 전송 이전 "발행완료" 상태의 세금계산서를 "발행취소"하고 국세청 전송 대상에서 제외합니다. - 삭제(Delete API) 함수를 호출하여 "발행취소" 상태의
     * 전자세금계산서를 삭제하면, 문서번호 재사용이 가능합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/issue#CancelIssue
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    // 메모
    String memo = "발행취소 메모";

    Response response =
        taxinvoiceService.cancelIssue(popbillConfig.getCorpNum(), mgtKeyType, mgtKey, memo);

    log.info("발행취소 결과 : {}", response.toString());
    Assertions.assertThat(response.getCode()).isEqualTo(1);
    Assertions.assertThat(response.getMessage()).isEqualTo("발행취소 완료");
  }
}
