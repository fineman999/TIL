package io.chan.queuingsystemforjava.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.member.Member;
import io.chan.queuingsystemforjava.domain.member.MemberRole;
import io.chan.queuingsystemforjava.support.controller.DocsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@Import({
        BaseControllerTest.RestDocsConfig.class,
        DocsController.class
})
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseControllerTest {

    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected RestDocumentationResultHandler restDocs;

    protected MockMvc mockMvc;

    @TestConfiguration
    public static class RestDocsConfig {

        @Bean
        public RestDocumentationResultHandler write() {
            return MockMvcRestDocumentation.document(
                    "{class-name}/{method-name}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()));
        }
    }

    @BeforeEach
    void setUp(
            WebApplicationContext applicationContext,
            RestDocumentationContextProvider documentationContextProvider) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(applicationContext)
                        .alwaysDo(print())
                        .alwaysDo(restDocs)
                        .apply(springSecurity())
                        .apply(
                                MockMvcRestDocumentation.documentationConfiguration(
                                        documentationContextProvider))
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .build();

        Member admin = Member.create("admin@admin.com", "password", MemberRole.ADMIN);
//        this.adminBearerToken = "Bearer " + jwtProvider.createAccessToken(admin);

        Member user = Member.create("user@user.com", "password", MemberRole.USER);
//        this.userBearerToken = "Bearer " + jwtProvider.createAccessToken(user);
    }
}
