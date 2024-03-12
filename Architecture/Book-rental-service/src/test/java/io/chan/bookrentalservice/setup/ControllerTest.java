package io.chan.bookrentalservice.setup;

import io.chan.bookrentalservice.application.usecase.*;
import io.chan.bookrentalservice.framework.web.RentalController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest(
        RentalController.class
)
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {
    protected MockMvcRequestSpecification restDocs;

    @MockBean
    protected ClearOverdueItemUseCase clearOverdueItemUseCase;

    @MockBean
    protected CreateRentalCardUseCase createRentalCardUseCase;

    @MockBean
    protected InQueryUseCase inQueryUseCase;

    @MockBean
    protected OverdueItemUseCase overdueItemUseCase;

    @MockBean
    protected RentItemUseCase rentItemUseCase;

    @MockBean
    protected ReturnItemUserCase returnItemUserCase;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        restDocs = RestAssuredMockMvc.given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .build())
                .log().all();
    }
}
