package org.woehlke.simpleworklist.control.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.entities.UserAccount;
import org.woehlke.simpleworklist.model.services.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.woehlke.simpleworklist.model.services.UserAccountLoginSuccessService;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    private final TestService testService;

    private final UserAccountLoginSuccessService userAccountLoginSuccessService;

    @Autowired
    public TestController(TestService testService, UserAccountLoginSuccessService userAccountLoginSuccessService) {
        this.testService = testService;
        this.userAccountLoginSuccessService = userAccountLoginSuccessService;
    }

    @RequestMapping(value = "/helper/project/createTree", method = RequestMethod.GET)
    public String createTestCategoryTree() {
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        testService.createTestCategoryTreeForUserAccount(user);
        return "redirect:/";
    }

}