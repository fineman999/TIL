package io.chan.popbillservice;

import com.popbill.api.BulkResponse;
import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.Taxinvoice;
import com.popbill.api.taxinvoice.TaxinvoiceAddContact;
import com.popbill.api.taxinvoice.TaxinvoiceDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class PopbillBulkRegistTest {

  @Autowired private TaxinvoiceService taxinvoiceService;

  @Autowired private PopbillConfig popbillConfig;

  @DisplayName("1. 대량 발행 접수")
  @Test
  void bulkRegist() throws PopbillException {
    /**
     * 최대 100건의 세금계산서 발행을 한번의 요청으로 접수합니다. - 세금계산서 발행을 위해서 공급자의 인증서가 팝빌 인증서버에 사전등록 되어야 합니다. └ 위수탁발행의
     * 경우, 수탁자의 인증서 등록이 필요합니다. -
     * https://developers.popbill.com/reference/taxinvoice/java/api/issue#BulkSubmit
     */

    // 제출아이디, 대량 발행 접수를 구별하는 식별키
    // └ 최대 36자리 영문, 숫자, '-' 조합으로 구성
    String SubmitID = "20230102-BOOT-BULK";

    // 지연발행 강제여부
    // 발행마감일이 지난 세금계산서를 발행하는 경우, 가산세가 부과될 수 있습니다.
    // 가산세가 부과되더라도 발행을 해야하는 경우에는 forceIssue의 값을 true로 선언하여 API를 호출하시면 됩니다.
    Boolean ForceIssue = false;

    // 최대 100건
    List<Taxinvoice> bulkTx = new ArrayList<Taxinvoice>();

    for (int i = 0; i < 100; i++) {

      // 세금계산서 정보 객체
      Taxinvoice taxinvoice = new Taxinvoice();

      // 작성일자, 날짜형식(yyyyMMdd)
      taxinvoice.setWriteDate("20220218");

      // 과금방향, [정과금, 역과금] 중 선택기재
      // - 정과금 = 공급자 과금 , 역과금 = 공급받는자 과금
      // -'역과금'은 역발행 세금계산서 발행 시에만 이용가능
      taxinvoice.setChargeDirection("정과금");

      // 발행유형, [정발행, 역발행, 위수탁] 중 기재
      taxinvoice.setIssueType("정발행");

      // [영수, 청구, 없음] 중 기재
      taxinvoice.setPurposeType("영수");

      // 과세형태, [과세, 영세, 면세] 중 기재
      taxinvoice.setTaxType("과세");

      /**********************************************************************
       * 공급자 정보
       *********************************************************************/

      // 공급자 사업자번호
      taxinvoice.setInvoicerCorpNum(popbillConfig.getCorpNum());

      // 공급자 종사업장 식별번호, 필요시 기재. 형식은 숫자 4자리.
      taxinvoice.setInvoicerTaxRegID("");

      // 공급자 상호
      taxinvoice.setInvoicerCorpName("공급자 상호");

      // 공급자 문서번호, 1~24자리 (숫자, 영문, '-', '_') 조합으로 사업자 별로 중복되지 않도록 구성
      taxinvoice.setInvoicerMgtKey(SubmitID + "-" + String.valueOf(i + 1));

      // 공급자 대표자 성명
      taxinvoice.setInvoicerCEOName("공급자 대표자 성명");

      // 공급자 주소
      taxinvoice.setInvoicerAddr("공급자 주소");

      // 공급자 종목
      taxinvoice.setInvoicerBizClass("공급자 종목");

      // 공급자 업태
      taxinvoice.setInvoicerBizType("공급자 업태,업태2");

      // 공급자 담당자 성명
      taxinvoice.setInvoicerContactName("공급자 담당자 성명");

      // 공급자 담당자 메일주소
      taxinvoice.setInvoicerEmail("");

      // 공급자 담당자 연락처
      taxinvoice.setInvoicerTEL("070-7070-0707");

      // 공급자 담당자 휴대폰번호
      taxinvoice.setInvoicerHP("010-000-2222");

      // 발행 안내 문자 전송여부 (true / false 중 택 1)
      // └ true = 전송 , false = 미전송
      // └ 공급받는자 (주)담당자 휴대폰번호 {invoiceeHP1} 값으로 문자 전송
      // - 전송 시 포인트 차감되며, 전송실패시 환불처리
      taxinvoice.setInvoicerSMSSendYN(false);

      /**********************************************************************
       * 공급받는자 정보
       *********************************************************************/

      // 공급받는자 구분, [사업자, 개인, 외국인] 중 기재
      taxinvoice.setInvoiceeType("사업자");

      // 공급받는자 사업자번호
      // - {invoiceeType}이 "사업자" 인 경우, 사업자번호 (하이픈 ('-') 제외 10자리)
      // - {invoiceeType}이 "개인" 인 경우, 주민등록번호 (하이픈 ('-') 제외 13자리)
      // - {invoiceeType}이 "외국인" 인 경우, "9999999999999" (하이픈 ('-') 제외 13자리)
      taxinvoice.setInvoiceeCorpNum("8888888888");

      // 공급받는자 상호
      taxinvoice.setInvoiceeCorpName("공급받는자 상호");

      // [역발행시 필수] 공급받는자 문서번호, 1~24자리 (숫자, 영문, '-', '_') 조합으로 사업자 별로 중복되지 않도록 구성
      taxinvoice.setInvoiceeMgtKey("");

      // 공급받는자 대표자 성명
      taxinvoice.setInvoiceeCEOName("공급받는자 대표자 성명");

      // 공급받는자 주소
      taxinvoice.setInvoiceeAddr("공급받는자 주소");

      // 공급받는자 종목
      taxinvoice.setInvoiceeBizClass("공급받는자 업종");

      // 공급받는자 업태
      taxinvoice.setInvoiceeBizType("공급받는자 업태");

      // 공급받는자 담당자 성명
      taxinvoice.setInvoiceeContactName1("공급받는자 담당자 성명");

      // 공급받는자 담당자 메일주소
      // 팝빌 테스트 환경에서 테스트하는 경우에도 안내 메일이 전송되므로,
      // 실제 거래처의 메일주소가 기재되지 않도록 주의
      taxinvoice.setInvoiceeEmail1("");

      // 공급받는자 담당자 연락처
      taxinvoice.setInvoiceeTEL1("070-111-222");

      // 공급받는자 담당자 휴대폰번호
      taxinvoice.setInvoiceeHP1("010-111-222");

      // 역발행 안내 문자 전송여부 (true / false 중 택 1)
      // └ true = 전송 , false = 미전송
      // └ 공급자 담당자 휴대폰번호 {invoicerHP} 값으로 문자 전송
      // - 전송 시 포인트 차감되며, 전송실패시 환불처리
      taxinvoice.setInvoiceeSMSSendYN(false);

      /**********************************************************************
       * 세금계산서 기재정보
       *********************************************************************/

      // 공급가액 합계
      taxinvoice.setSupplyCostTotal("100000");

      // 세액 합계
      taxinvoice.setTaxTotal("10000");

      // 합계금액, 공급가액 + 세액
      taxinvoice.setTotalAmount("110000");

      // 일련번호
      taxinvoice.setSerialNum("123");

      // 현금
      taxinvoice.setCash("");

      // 수표
      taxinvoice.setChkBill("");

      // 어음
      taxinvoice.setNote("");

      // 외상미수금
      taxinvoice.setCredit("");

      // 비고
      // {invoiceeType}이 "외국인" 이면 remark1 필수
      // - 외국인 등록번호 또는 여권번호 입력
      taxinvoice.setRemark1("비고1");
      taxinvoice.setRemark2("비고2");
      taxinvoice.setRemark3("비고3");

      // 책번호 '권' 항목, 최대값 32767
      taxinvoice.setKwon((short) 1);

      // 책번호 '호' 항목, 최대값 32767
      taxinvoice.setHo((short) 1);

      // 사업자등록증 이미지 첨부여부 (true / false 중 택 1)
      // └ true = 첨부 , false = 미첨부(기본값)
      // - 팝빌 사이트 또는 인감 및 첨부문서 등록 팝업 URL (GetSealURL API) 함수를 이용하여 등록
      taxinvoice.setBusinessLicenseYN(false);

      // 통장사본 이미지 첨부여부 (true / false 중 택 1)
      // └ true = 첨부 , false = 미첨부(기본값)
      // - 팝빌 사이트 또는 인감 및 첨부문서 등록 팝업 URL (GetSealURL API) 함수를 이용하여 등록
      taxinvoice.setBankBookYN(false);

      /**********************************************************************
       * 수정세금계산서 정보 (수정세금계산서 작성시 기재)
       * - 수정세금계산서 작성방법 안내 [https://developers.popbill.com/guide/taxinvoice/java/introduction/modified-taxinvoice]
       *********************************************************************/
      // 수정사유코드, 수정사유에 따라 1~6 중 선택기재.
      taxinvoice.setModifyCode(null);

      // 수정세금계산서 작성시 원본세금계산서 국세청 승인번호 기재
      taxinvoice.setOrgNTSConfirmNum(null);

      /**********************************************************************
       * 상세항목(품목) 정보
       *********************************************************************/

      taxinvoice.setDetailList(new ArrayList<TaxinvoiceDetail>());

      // 상세항목 객체
      TaxinvoiceDetail detail = new TaxinvoiceDetail();

      detail.setSerialNum((short) 1); // 일련번호, 1부터 순차기재
      detail.setPurchaseDT("20230102"); // 거래일자
      detail.setItemName("품목명"); // 품목명
      detail.setSpec("규격"); // 규격
      detail.setQty("1"); // 수량
      detail.setUnitCost("50000"); // 단가
      detail.setSupplyCost("50000"); // 공급가액
      detail.setTax("5000"); // 세액
      detail.setRemark("품목비고"); // 비고

      taxinvoice.getDetailList().add(detail);

      detail = new TaxinvoiceDetail();

      detail.setSerialNum((short) 2); // 일련번호, 1부터 순차기재
      detail.setPurchaseDT("20230102"); // 거래일자
      detail.setItemName("품목명2"); // 품목명
      detail.setSpec("규격"); // 규격
      detail.setQty("1"); // 수량
      detail.setUnitCost("50000"); // 단가
      detail.setSupplyCost("50000"); // 공급가액
      detail.setTax("5000"); // 세액
      detail.setRemark("품목비고2"); // 비고

      taxinvoice.getDetailList().add(detail);

      /**********************************************************************
       * 추가담당자 정보
       * - 세금계산서 발행 안내 메일을 수신받을 공급받는자 담당자가 다수인 경우
       * - 담당자 정보를 추가하여 발행 안내메일을 다수에게 전송할 수 있습니다. (최대 5명)
       *********************************************************************/

      taxinvoice.setAddContactList(new ArrayList<TaxinvoiceAddContact>());

      TaxinvoiceAddContact addContact = new TaxinvoiceAddContact();

      addContact.setSerialNum(1); // 일련번호 (1부터 순차적으로 입력 (최대 5))
      addContact.setContactName("추가 담당자 성명"); // 담당자 성명
      addContact.setEmail(""); // 이메일

      taxinvoice.getAddContactList().add(addContact);

      bulkTx.add(taxinvoice);
    }

    BulkResponse response =
        taxinvoiceService.bulkSubmit(popbillConfig.getCorpNum(), SubmitID, bulkTx, ForceIssue);

    System.out.println("응답 메시지 : " + response.getMessage());
    System.out.println("응답 코드 : " + response.getCode());
    System.out.println("접수아이디 : " + response.getReceiptID());
    /**
     * 응답 메시지 : 접수 완료
     * 응답 코드 : 1
     * 접수아이디 : 0240728-12f5b0bd00fe4106bd35c36a7651
     */
  }
}
