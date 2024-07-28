package io.chan.popbillservice;

import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.MgtKeyType;
import com.popbill.api.taxinvoice.TISearchResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SearchTest {

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired
    private PopbillConfig popbillConfig;

    @DisplayName("세금계산서 검색 - 상태")
    @Test
    void search() throws PopbillException {
        /**
         * 검색조건에 해당하는 세금계산서를 조회합니다. (조회기간 단위 : 최대 6개월)
         * - https://developers.popbill.com/reference/taxinvoice/java/api/info#Search
         */

        // 세금계산서 유형 (SELL-매출, BUY-매입, TRUSTEE-위수탁)
        MgtKeyType mgtKeyType = MgtKeyType.SELL;

        // 일자 유형 ("R" , "W" , "I" 중 택 1)
        // └ R = 등록일자 , W = 작성일자 , I = 발행일자
        String DType = "W";

        // 시작일자, 날짜형식(yyyyMMdd)
        String SDate = "20240302";

        // 종료일자, 날짜형식(yyyyMMdd)
        String EDate = "20240831";

        // 상태코드 배열 (2,3번째 자리에 와일드카드(*) 사용 가능)
        // - 미입력시 전체조회
        String[] State = {"3**", "6**"};

        // 문서유형 배열 ("N" , "M" 중 선택, 다중 선택 가능)
        // - N = 일반세금계산서 , M = 수정세금계산서
        // - 미입력시 전체조회
        String[] Type = {"N", "M"};

        // 과세형태 배열 ("T" , "N" , "Z" 중 선택, 다중 선택 가능)
        // - T = 과세 , N = 면세 , Z = 영세
        // - 미입력시 전체조회
        String[] TaxType = {"T", "N", "Z"};

        // 발행형태 배열 ("N" , "R" , "T" 중 선택, 다중 선택 가능)
        // - N = 정발행 , R = 역발행 , T = 위수탁발행
        // - 미입력시 전체조회
        String[] IssueType = {"N", "R", "T"};

        // 등록유형 배열 ("P" , "H" 중 선택, 다중 선택 가능)
        // - P = 팝빌 , H = 홈택스 또는 외부ASP
        // - 미입력시 전체조회
        String[] RegType = {"P", "H"};

        // 공급받는자 휴폐업상태 배열 ("N" , "0" , "1" , "2" , "3" , "4" 중 선택, 다중 선택 가능)
        // - N = 미확인 , 0 = 미등록 , 1 = 사업 , 2 = 폐업 , 3 = 휴업 , 4 = 확인실패
        // - 미입력시 전체조회
        String[] CloseDownState = {"N", "0", "1", "2", "3"};

        // 지연발행 여부 (null , true , false 중 택 1)
        // - null = 전체조회 , true = 지연발행 , false = 정상발행
        Boolean LateOnly = null;

        // 종사업장번호의 주체 ("S" , "B" , "T" 중 택 1)
        // └ S = 공급자 , B = 공급받는자 , T = 수탁자
        // - 미입력시 전체조회
        String TaxRegIDType = "";

        // 종사업장번호
        // 다수기재시 콤마(",")로 구분하여 구성 ex ) "0001,0002"
        // - 미입력시 전체조회
        String TaxRegID = "";

        // 종사업장번호 유무
        // - null = 전체 , 0 = 없음, 1 = 있음
        String TaxRegIDYN = null;

        // 거래처 상호 / 사업자번호 (사업자) / 주민등록번호 (개인) / "9999999999999" (외국인) 중 검색하고자 하는 정보 입력
        // - 사업자번호 / 주민등록번호는 하이픈('-')을 제외한 숫자만 입력
        // - 미입력시 전체조회
        String QString = "";

        // 문서번호 또는 국세청 승인번호 조회
        // - 미입력시 전체조회
        String MgtKey = "";

        // 페이지 번호
        int Page = 1;

        // 페이지당 목록개수
        int PerPage = 20;

        // 정렬방향, A-오름차순, D-내림차순
        String Order = "D";

        // 연동문서 여부 (null , "0" , "1" 중 택 1)
        // - null = 전체조회 , 0 = 일반문서 , 1 = 연동문서
        // 일반문서 - 세금계산서 작성 시 API가 아닌 팝빌 사이트를 통해 등록한 문서
        // 연동문서 - 세금계산서 작성 시 API를 통해 등록한 문서
        String InterOPYN = null;

    TISearchResult searchResult =
        taxinvoiceService.Search(
            popbillConfig.getCorpNum(),
            mgtKeyType,
            DType,
            SDate,
            EDate,
            State,
            Type,
            TaxType,
            IssueType,
            LateOnly,
            TaxRegIDType,
            TaxRegID,
            TaxRegIDYN,
            QString,
            Page,
            PerPage,
            Order,
            InterOPYN,
            RegType,
            CloseDownState,
            MgtKey,
            popbillConfig.getUserId());
    log.info("검색 결과 : {}", searchResult.toString());
    }
}
