package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class PopbillCheckMgtKeyTests {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("1. 문서 번호 사용 여부 확인 테스트")
  @Test
  void checkMgtKeyInUse() throws PopbillException {
    /**
     * 파트너가 세금계산서 관리 목적으로 할당하는 문서번호의 사용여부를 확인합니다. - 이미 사용 중인 문서번호는 중복 사용이 불가하고, 세금계산서가 삭제된 경우에만
     * 문서번호의 재사용이 가능합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#CheckMgtKeyInUse
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType keyType = MgtKeyType.SELL;

    // 세금계산서 문서번호, 1~24자리 (숫자, 영문, '-', '_') 조합으로 사업자 별로 중복되지 않도록 구성
    String mgtKey = "20230102-BOOT001";

    boolean IsUse = taxinvoiceService.checkMgtKeyInUse(popbillConfig.getCorpNum(), keyType, mgtKey);

    log.info("사용여부 확인 결과 : {}", IsUse);
    assertThat(IsUse).isFalse();
  }


}
