package org.woehlke.simpleworklist.testdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.user.account.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.user.login.UserAccountLoginSuccessService;

@Slf4j
@Controller
@RequestMapping(path = "/testdata")
public class TestDataController {

    private final TestDataService testDataService;
    private final UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    public TestDataController(
        TestDataService testDataService,
        UserAccountLoginSuccessService userAccountLoginSuccessService
    ) {
        this.testDataService = testDataService;
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
    }

    @RequestMapping(path = "/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        testDataService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
    }

}
