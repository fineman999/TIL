package io.chan.popbillservice.attach;

import com.popbill.api.AttachedFile;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import io.chan.popbillservice.PopbillConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class AttachmentTest {
  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("첨부파일 등록 URL")
  @Test
  void getURL() throws PopbillException {
    /**
     * 세금계산서에 첨부파일을 등록할 수 있는 URL을 반환합니다. -
     * https://developers.popbill.com/taxinvoice/java/api#GetAttachFile
     */
    String url =
        taxinvoiceService.getSealURL(popbillConfig.getCorpNum(), popbillConfig.getUserId());

    log.info("첨부파일 등록 URL : {}", url);
  }

  @DisplayName("첨부파일 등록")
  @Test
  void attachFile() throws PopbillException {
    /**
     * "임시저장" 상태의 세금계산서에 1개의 파일을 첨부합니다. (최대 5개) -
     * https://developers.popbill.com/reference/taxinvoice/java/api/etc#AttachFile
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    // 첨부파일 표시명
    String displayName = "첨부파일.jpg";

    // 첨부할 파일의 InputStream. 예제는 resource에 테스트파일을 참조함.
    // FileInputStream으로 처리하는 것을 권함.
    InputStream stream = getClass().getClassLoader().getResourceAsStream("static/test.png");
    Response response =
        taxinvoiceService.attachFile(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, displayName, stream);

    log.info("첨부파일 등록 결과 : {}", response);
    assertThat(response.getCode()).isEqualTo(1);
    assertThat(response.getMessage()).isEqualTo("첨부 완료");
  }

  @DisplayName("첨부파일 목록")
  @Test
  void getFiles() throws PopbillException {
    /**
     * 세금계산서에 첨부된 파일목록을 확인합니다. - 응답항목 중 파일아이디(AttachedFile) 항목은 파일삭제(DeleteFile API) 함수 호출 시 이용할 수
     * 있습니다. - https://developers.popbill.com/reference/taxinvoice/java/api/etc#GetFiles
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    AttachedFile[] attachedFiles =
        taxinvoiceService.getFiles(popbillConfig.getCorpNum(), mgtKeyType, mgtKey);

    for (AttachedFile attachedFile : attachedFiles) {
      log.info("파일아이디 : {}", attachedFile.getAttachedFile());
      log.info("첨부파일명 : {}", attachedFile.getDisplayName());
    }
  }

  @DisplayName("첨부파일 삭제")
  @Test
  void deleteFile() throws PopbillException {
    /**
     * "임시저장" 상태의 세금계산서에 첨부된 1개의 파일을 삭제합니다. - 파일을 식별하는 파일아이디는 첨부파일 목록 확인(GetFiles API) 함수의 응답항목 중
     * 파일아이디(AttachedFile) 값을 통해 확인할 수 있습니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/etc#DeleteFile
     */

    // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
    MgtKeyType mgtKeyType = MgtKeyType.SELL;

    // 세금계산서 문서번호
    String mgtKey = "20240728-BOOT003";

    // 파일아이디, getFiles()로 확인한 AttachedFile의 attachedFile 참조.
    String FileID = "8F2870AF-5987-42E4-8C32-AA2C9F0143FA.PBF";

    Response response =
        taxinvoiceService.deleteFile(popbillConfig.getCorpNum(), mgtKeyType, mgtKey, FileID);

    log.info("첨부파일 삭제 결과 : {}", response);
    assertThat(response.getCode()).isEqualTo(1);
    assertThat(response.getMessage()).isEqualTo("첨부삭제 완료");
  }
}
