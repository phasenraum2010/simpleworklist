package org.woehlke.simpleworklist.test.testdata;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.woehlke.simpleworklist.application.config.UserAccountTestDataService;
import org.woehlke.simpleworklist.common.testdata.TestDataService;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;

import java.net.URL;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestDataServiceTest {

    @Autowired
    ServletWebServerApplicationContext server;

    @LocalServerPort
    int port;

    protected URL base;

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Autowired
    private TestDataService testDataService;

    @Autowired
    private UserAccountTestDataService userAccountTestDataService;

    @BeforeEach
    public void setUp() throws Exception {
        log.info(" @BeforeEach setUp()");
        this.base = new URL("http://localhost:" + port + "/");
        this.mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void createTestCategoryTreeForUserAccountTest(){
        log.info("createTestCategoryTreeForUserAccountTest");
        userAccountTestDataService.setUp();
        UserAccount userAccount = userAccountTestDataService.getFirstUserAccount();
        //TODO: #128
        testDataService.createTestCategoryTreeForUserAccount(userAccount);
    }
}
