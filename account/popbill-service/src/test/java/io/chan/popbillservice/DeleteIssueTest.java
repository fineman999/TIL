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
public class DeleteIssueTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("세금계산서 삭제")
  @Test
  void deleteIssue() throws PopbillException {
    /**
     * 삭제 가능한 상태의 세금계산서를 삭제합니다. - 삭제 가능한 상태: "임시저장", "발행취소", "역발행거부", "역발행취소", "전송실패" - 삭제처리된 세금계산서의
     * 문서번호는 재사용이 가능합니다. - https://developers.popbill.com/reference/taxinvoice/java/api/issue#Delete
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    Response response = taxinvoiceService.delete(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    log.info("삭제 결과 : {}", response.toString());
    Assertions.assertThat(response.getCode()).isEqualTo(1);
    Assertions.assertThat(response.getMessage()).isEqualTo("삭제 완료");
  }
}
