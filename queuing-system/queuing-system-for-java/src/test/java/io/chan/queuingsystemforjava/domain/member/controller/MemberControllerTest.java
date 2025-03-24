package io.chan.queuingsystemforjava.domain.member.controller;

import io.chan.queuingsystemforjava.domain.member.dto.request.CreateMemberRequest;
import io.chan.queuingsystemforjava.domain.member.dto.response.CreateMemberResponse;
import io.chan.queuingsystemforjava.support.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends BaseControllerTest {
    @Test
    @DisplayName("회원 생성 API 호출")
    void createMember() throws Exception {
        // given
        CreateMemberRequest request = new CreateMemberRequest("eamil@email.com", "password");

        given(memberService.saveMember(any())).willReturn(new CreateMemberResponse(1L));

        // when
        ResultActions result =
                mockMvc.perform(
                        post("/api/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(request)));

        // then
        result.andExpect(status().isCreated())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("email")
                                                .type(JsonFieldType.STRING)
                                                .description("이메일"),
                                        fieldWithPath("password")
                                                .type(JsonFieldType.STRING)
                                                .description("비밀번호")),
                                responseFields(
                                        fieldWithPath("memberId")
                                                .type(JsonFieldType.NUMBER)
                                                .description("회원ID"))));
    }
}