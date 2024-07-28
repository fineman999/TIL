package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import com.popbill.api.taxinvoice.Taxinvoice;
import com.popbill.api.taxinvoice.TaxinvoiceInfo;
import com.popbill.api.taxinvoice.TaxinvoiceXML;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GetInfoTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("세금계산서 정보 확인")
  @Test
  void getInfo() throws PopbillException {
    /**
     * 세금계산서 1건의 상태 및 요약정보를 확인합니다. - 리턴값 'TaxinvoiceInfo'의 변수 'stateCode'를 통해 세금계산서의 상태코드를 확인합니다. -
     * 세금계산서 상태코드 [https://developers.popbill.com/reference/taxinvoice/java/response-code] -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#GetInfo
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    TaxinvoiceInfo taxinvoiceInfo =
        taxinvoiceService.getInfo(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    log.info("세금계산서 정보 : {}", taxinvoiceInfo.toString());
  }

  @DisplayName("다수건의 세금계산서 정보 확인")
  @Test
  void getInfoList() throws PopbillException {
    /**
     * 다수건의 세금계산서 상태 및 요약 정보를 확인합니다. (1회 호출 시 최대 1,000건 확인 가능) - 리턴값 'TaxinvoiceInfo'의 변수
     * 'stateCode'를 통해 세금계산서의 상태코드를 확인합니다. - 세금계산서 상태코드
     * [https://developers.popbill.com/reference/taxinvoice/java/response-code] -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#GetInfos
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호 배열 (최대 1000건)
    String[] MgtKeyList = new String[] {"20230102-BOOT001", "20230102-BOOT002"};
    TaxinvoiceInfo[] taxinvoiceInfos =
        taxinvoiceService.getInfos(popbillConfig.getCorpNum(), mgtKeyType, MgtKeyList);

    // 없으면 에러가나지않고 값들이 null로 리턴됨
    log.info("세금계산서 정보 : {}", taxinvoiceInfos.toString());
  }

  @DisplayName("세금계산서 상세정보 확인")
  @Test
  void getDetailInfo() throws PopbillException {
    /**
     * 세금계산서 1건의 상세정보를 확인합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#GetDetailInfo
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    Taxinvoice taxinvoice =
        taxinvoiceService.getDetailInfo(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    log.info("세금계산서 상세정보 : {}", taxinvoice.toString());

  }

  @DisplayName("세금계산서 XML 확인")
    @Test
    void getXML() throws PopbillException {
        /**
         * 세금계산서 1건의 상세정보를 확인합니다. - https://developers.popbill.com/reference/taxinvoice/java/api/info#GetXML
         */

        // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
        MgtKeyType mgtKeyType = MgtKeyType.SELL;

        // 세금계산서 문서번호
        String mgtKey = "20240728-BOOT003";

      final TaxinvoiceXML xml = taxinvoiceService.getXML(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

        log.info("세금계산서 XML : {}", xml.toString());
    }
}
