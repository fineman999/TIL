package io.chan.popbillservice;

import com.popbill.api.IssueResponse;
import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class IssueTest {
    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired
    private PopbillConfig popbillConfig;

    @DisplayName("발행")
    @Test
    void issue() throws PopbillException {
        /**
         * "임시저장" 또는 "(역)발행대기" 상태의 세금계산서를 발행(전자서명)하며, "발행완료" 상태로 처리합니다.
         * - 세금계산서 국세청 전송정책 [https://developers.popbill.com/guide/taxinvoice/java/introduction/policy-of-send-to-nts]
         * - "발행완료" 된 전자세금계산서는 국세청 전송 이전에 발행취소(CancelIssue API) 함수로 국세청 신고 대상에서 제외할 수 있습니다.
         * - 세금계산서 발행을 위해서 공급자의 인증서가 팝빌 인증서버에 사전등록 되어야 합니다.
         *   └ 위수탁발행의 경우, 수탁자의 인증서 등록이 필요합니다.
         * - 세금계산서 발행 시 공급받는자에게 발행 메일이 발송됩니다.
         * - https://developers.popbill.com/reference/taxinvoice/java/api/issue#Issue
         */

        // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
        MgtKeyType mgtKeyType = MgtKeyType.SELL;

        // 세금계산서 문서번호
        String mgtKey = "20240728-BOOT003";

        // 메모
        String memo = "발행 메모";

        // 지연발행 강제여부 (true / false 중 택 1)
        // └ true = 가능 , false = 불가능
        // - 미입력 시 기본값 false 처리
        // - 발행마감일이 지난 세금계산서를 발행하는 경우, 가산세가 부과될 수 있습니다.
        // - 가산세가 부과되더라도 발행을 해야하는 경우에는 forceIssue의 값을 true로 선언하여 발행(Issue API)를 호출하시면 됩니다.
        Boolean forceIssue = false;

    IssueResponse response =
        taxinvoiceService.issue(
            popbillConfig.getCorpNum(), mgtKeyType, mgtKey, memo, forceIssue, popbillConfig.getUserId());

    log.info("발행 결과 : {}", response.toString());

    }
}
