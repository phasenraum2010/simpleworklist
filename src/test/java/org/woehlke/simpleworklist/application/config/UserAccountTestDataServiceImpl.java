package org.woehlke.simpleworklist.application.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.woehlke.simpleworklist.application.ApplicationProperties;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.context.ContextService;
import org.woehlke.simpleworklist.domain.context.NewContextForm;
import org.woehlke.simpleworklist.common.language.Language;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.user.services.UserAccountService;

import java.util.Date;


@Slf4j
@Getter
@Service
public class UserAccountTestDataServiceImpl implements UserAccountTestDataService {

    private final String[] emails = { "test01@test.de", "test02@test.de", "test03@test.de" };
    private final String[] passwords = { "test01pwd", "test02pwd", "test03pwd"};
    private final String[] fullnames = { "test01 Name", "test02 Name", "test03 Name"};

    private final String username_email = "undefined@test.de";
    private final String password = "ASDFG";
    private final String full_name = "UNDEFINED_NAME";

    private final UserAccount[] testUser;
    private final NewContextForm[] newContext;

    private final UserAccountService userAccountService;
    private final ContextService contextService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public UserAccountTestDataServiceImpl(
        UserAccountService userAccountService,
        ContextService contextService,
        ApplicationProperties applicationProperties
    ) {
        this.userAccountService = userAccountService;
        this.contextService = contextService;
        this.applicationProperties = applicationProperties;
        Date lastLoginTimestamp = new Date();
        testUser = new UserAccount[emails.length];
        newContext = new NewContextForm[emails.length];
        for (int i = 0; i < testUser.length; i++) {
            testUser[i] = new UserAccount();
            testUser[i].setUserEmail(emails[i]);
            testUser[i].setUserPassword(passwords[i]);
            testUser[i].setUserFullname(fullnames[i]);
            testUser[i].setDefaultLanguage(Language.EN);
            testUser[i].setLastLoginTimestamp(lastLoginTimestamp);
            newContext[i] = new NewContextForm("testDe_"+i,"testEn_"+i);
        }
    }

    public void setUp() {
        for (int i = 0; i < testUser.length; i++) {
            UserAccount a = userAccountService.findByUserEmail(testUser[i].getUserEmail());
            if (a == null) {
                UserAccount persisted = userAccountService.saveAndFlush(testUser[i]);
                testUser[i] = persisted;
                NewContextForm newContextPrivate = new NewContextForm("privat"+i,"private"+i);
                NewContextForm newContextWork = new NewContextForm("arbeit"+i,"work"+i);
                Context persistedContextPrivate = contextService.createNewContext(newContextPrivate, testUser[i]);
                Context persistedContextWork = contextService.createNewContext(newContextWork, testUser[i]);
                testUser[i].setDefaultContext(persistedContextPrivate);
                persisted = userAccountService.saveAndFlush(testUser[i]);
            }
       }
    }

    @Override
    public UserAccount getFirstUserAccount() {
        return testUser[0];
    }
}
