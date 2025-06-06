package io.chan.queuingsystemforjava.domain.waitingsystem.controller;

import io.chan.queuingsystemforjava.domain.waitingsystem.service.WaitingSystem;
import io.chan.queuingsystemforjava.global.security.AuthenticationToken;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitingController.class)
class WaitingControllerTest extends BaseControllerTest {

  @MockitoBean private WaitingSystem waitingSystem;

  public static final String PERFORMANCE_ID = "performanceId";

  @MockitoBean private SecurityContext securityContext;

  @MockitoBean private AuthenticationToken authenticationToken;

  @BeforeEach
  void setUp() {
    Mockito.when(authenticationToken.getPrincipal()).thenReturn(userMemberContext);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authenticationToken);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  @DisplayName("남은 대기 순번 조회 API 호출 시")
  void getRemainingCount() throws Exception {
    // given
    given(waitingSystem.pollRemainingCountAndTriggerEvents(anyString(), anyLong())).willReturn(1L);

    // when
    ResultActions result =
        mockMvc.perform(
            get("/api/v1/performances/{performanceId}/wait", 1)
                .header(HttpHeaders.AUTHORIZATION, userBearerToken)
                .header(PERFORMANCE_ID, 1));

    // then
    result
        .andExpect(status().isOk())
        .andDo(
            restDocs.document(
                pathParameters(parameterWithName("performanceId").description("공연 ID")),
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                    headerWithName(PERFORMANCE_ID).description("공연 ID")),
                responseFields(
                    fieldWithPath("remainingCount")
                        .type(JsonFieldType.NUMBER)
                        .description("남은 대기 순번"))));
  }
}
