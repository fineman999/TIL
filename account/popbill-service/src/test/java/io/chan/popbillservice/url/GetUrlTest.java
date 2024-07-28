package io.chan.popbillservice.url;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import io.chan.popbillservice.PopbillConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class GetUrlTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @Test
  @DisplayName("전자세금명세서 문서함 메뉴")
  void getUrl() throws PopbillException {
    /**
     * 로그인 상태로 팝빌 사이트의 전자세금계산서 문서함 메뉴에 접근할 수 있는 페이지의 팝업 URL을 반환합니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며,
     * 시간을 초과한 후에는 해당 URL을 통한 페이지 접근이 불가합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/info#GetURL
     */

    // TBOX : 임시문서함 , SBOX : 매출문서함 , PBOX : 매입문서함 ,
    // SWBOX : 매출발행 대기함 , PWBOX : 매입발행 대기함 , WRITE : 정발행 작성
    String TOGO = "SBOX";

    String url =
        taxinvoiceService.getURL(popbillConfig.getCorpNum(), popbillConfig.getUserId(), TOGO);

    log.info("URL : {}", url);
  }

  @Test
  @DisplayName("전자세금계산서 상세 URL- 사이트로 감")
  void getPopup() throws PopbillException {
    /**
     * 세금계산서 1건의 상세 정보 페이지의 팝업 URL을 반환합니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며, 시간을 초과한 후에는 해당 URL을 통한
     * 페이지 접근이 불가합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/view#GetPopUpURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    String url =
        taxinvoiceService.getPopUpURL(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, popbillConfig.getUserId());

    log.info("URL : {}", url);
  }

  @DisplayName("전자세금계산서 상세 페이지 URL-이거 하나만 보여줌")
  @Test
  void getViewUrl() throws PopbillException {
    /**
     * 세금계산서 1건의 상세정보 페이지(사이트 상단, 좌측 메뉴 및 버튼 제외)의 팝업 URL을 반환합니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며, 시간을
     * 초과한 후에는 해당 URL을 통한 페이지 접근이 불가합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/view#GetViewURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    String url =
        taxinvoiceService.getViewURL(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, popbillConfig.getUserId());
    log.info("URL : {}", url);
  }

  @DisplayName("세금계산서 인쇄 팝업 URL")
  @Test
  void getPrintUrl() throws PopbillException {
    /**
     * 세금계산서 1건을 인쇄하기 위한 페이지의 팝업 URL을 반환하며, 페이지내에서 인쇄 설정값을 "공급자" / "공급받는자" / "공급자+공급받는자"용 중 하나로 지정할
     * 수 있습니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며, 시간을 초과한 후에는 해당 URL을 통한 페이지 접근이 불가합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/view#GetPrintURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    String url =
        taxinvoiceService.getPrintURL(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, popbillConfig.getUserId());

    log.info("URL : {}", url);
  }

  @DisplayName("공급받는자만 세금계산서 인쇄 팝업 URL")
  @Test
  void getEPrintURL() throws PopbillException {
    /**
     * "공급받는자" 용 세금계산서 1건을 인쇄하기 위한 페이지의 팝업 URL을 반환합니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며, 시간을 초과한 후에는
     * 해당 URL을 통한 페이지 접근이 불가합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/view#GetEPrintURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    String url =
        taxinvoiceService.getEPrintURL(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, popbillConfig.getUserId());

    log.info("URL : {}", url);
  }

  @DisplayName("세금계산서 다량 인쇄 팝업 URL")
  @Test
  void getMassPrintURL() throws PopbillException {
    /**
     * 다수건의 세금계산서 인쇄팝업 URL을 반환합니다. - 반환되는 URL은 보안 정책상 30초 동안 유효하며, 시간을 초과한 후에는 해당 URL을 통한 페이지 접근이
     * 불가합니다. - https://developers.popbill.com/reference/taxinvoice/java/api/view#GetMassPrintURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호 배열 (최대 100건)
    String[] MgtKeyList = new String[] {"20230102-001", "20230102-002", "20230102-003"};

    String url =
        taxinvoiceService.getMassPrintURL(
            popbillConfig.getCorpNum(), mgtKeyType, MgtKeyList, popbillConfig.getUserId());

    log.info("URL : {}", url);
  }

  @Test
  @DisplayName("전자세금계산서 메일 링크 URL")
  void getMailUrl() throws PopbillException {
    /**
     * 전자세금계산서 안내메일의 상세보기 링크 URL을 반환합니다. - 함수 호출로 반환 받은 URL에는 유효시간이 없습니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/view#GetMailURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";
    String url = taxinvoiceService.getMailURL(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);
    log.info("URL : {}", url);
  }

  @Test
  @DisplayName("전자세금계산서 PDF 다운로드 URL")
  void getPDFURL() throws PopbillException {
    /**
     * 전자세금계산서 PDF 파일을 다운 받을 수 있는 URL을 반환합니다. - 반환되는 URL은 보안정책상 30초의 유효시간을 갖으며, 유효시간 이후 호출시 정상적으로
     * 페이지가 호출되지 않습니다. - https://developers.popbill.com/reference/taxinvoice/java/api/view#GetPDFURL
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    String url = taxinvoiceService.getPDFURL(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    log.info("URL : {}", url);
  }
}
