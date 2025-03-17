package io.chan.queuingsystemforjava.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.domain.member.service.JwtProvider;
import io.chan.queuingsystemforjava.global.config.CorsConfig;
import io.chan.queuingsystemforjava.global.config.SecurityConfig;
import io.chan.queuingsystemforjava.global.config.WebConfig;
import io.chan.queuingsystemforjava.global.security.JwtAuthenticationFilter;
import io.chan.queuingsystemforjava.support.controller.DocsController;
import io.chan.queuingsystemforjava.support.mock.MockCustomUserDetailsService;
import io.chan.queuingsystemforjava.support.mock.MockJwtProviderImpl;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(DocsController.class)
@Import({
        BaseControllerTest.RestDocsConfig.class,
        WebConfig.class,
})
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseControllerTest {

  @Autowired
  protected ObjectMapper objectMapper;
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
  void setUp(WebApplicationContext applicationContext, RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .alwaysDo(print())
            .alwaysDo(restDocs)
//            .apply(springSecurity())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .build();
  }
}