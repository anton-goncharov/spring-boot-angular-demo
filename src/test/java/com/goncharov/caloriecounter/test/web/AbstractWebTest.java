package com.goncharov.caloriecounter.test.web;

import com.goncharov.caloriecounter.CalorieCounterApplication;
import com.goncharov.caloriecounter.domain.User;
import com.goncharov.caloriecounter.security.CurrentUser;
import com.goncharov.caloriecounter.security.CustomAuthenticationProvider;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * @author Anton Goncharov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CalorieCounterApplication.class)
@WebAppConfiguration
@Transactional
public abstract class AbstractWebTest {

    private final Logger LOG = LoggerFactory.getLogger(AbstractWebTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    protected CsrfToken csrfToken;
    protected Map requestAttrs;


    @PostConstruct
    void postConstruct() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @PostConstruct
    void csrfSupport() {
        // init CSRF support
        HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfToken = httpSessionCsrfTokenRepository
                .generateToken(new MockHttpServletRequest());

        requestAttrs = new HashMap();
        requestAttrs.put("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN",
                csrfToken);
    }

    public static RequestPostProcessor credentials(User user) {
        CurrentUser currentUser = new CurrentUser(user, new String[]{"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"});
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentUser,user.getPassword());
        if (!user.isActive()) {
            token.setAuthenticated(false);
        }
        return SecurityMockMvcRequestPostProcessors.authentication(token);
    }

}
