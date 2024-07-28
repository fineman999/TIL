package io.chan.popbillservice.info;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceCertificate;
import com.popbill.api.TaxinvoiceService;
import io.chan.popbillservice.PopbillConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CertificationInfoTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("등록된 공인인증서 정보 확인")
  @Test
  void getCertificateInfo() throws PopbillException {
    /**
     * 팝빌 인증서버에 등록된 공동인증서의 정보를 확인합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/cert#GetTaxCertInfo
     */
    TaxinvoiceCertificate taxinvoiceCertificate =
        taxinvoiceService.getTaxCertInfo(popbillConfig.getCorpNum());

    log.info("등록된 공인인증서 정보 확인 : {}", taxinvoiceCertificate.toString());
  }
}
