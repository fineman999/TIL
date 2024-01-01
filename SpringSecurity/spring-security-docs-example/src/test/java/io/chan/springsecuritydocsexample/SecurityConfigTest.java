package io.chan.springsecuritydocsexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void loginOnSecondLoginThenFirstSessionTerminated() throws Exception {
        MvcResult mvcResult = this.mvc.perform(formLogin())
                .andExpect(authenticated())
                .andReturn();

        MockHttpSession firstLoginSession = (MockHttpSession) mvcResult.getRequest().getSession();

        this.mvc.perform(get("/").session(firstLoginSession))
                .andExpect(authenticated());

        this.mvc.perform(formLogin()).andExpect(authenticated());

        // first session is terminated by second login
        this.mvc.perform(get("/").session(firstLoginSession))
                .andExpect(unauthenticated());
    }

    @WithMockUser(authorities="USER")
    @Test
    void endpointWhenUserAuthorityThenAuthorized() throws Exception {
        this.mvc.perform(get("/endpoint"))
                .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    void endpointWhenNotUserAuthorityThenForbidden() throws Exception {
        this.mvc.perform(get("/endpoint"))
                .andExpect(status().isForbidden());
    }

    @Test
    void anyWhenUnauthenticatedThenUnauthorized() throws Exception {
        this.mvc.perform(get("/test"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "chan")
    @Test
    void resourceWhenNameChanThenAuthorized() throws Exception {
        this.mvc.perform(get("/resource/chan"))
                .andExpect(status().isOk());
    }
}