package org.woehlke.simpleworklist.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.woehlke.simpleworklist.application.LoginSuccessHandler;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.services.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tw on 19.02.16.
 */
@Component("myLoginSuccessHandler")
public class LoginSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler implements LoginSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSuccessHandlerImpl.class);

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        UserAccount user = userAccountService.retrieveCurrentUser();
        userAccountService.updateLastLoginTimestamp(user);
        Locale locale;
        switch(user.getDefaultLanguage()){
            case DE: locale = Locale.GERMAN; break;
            default: locale = Locale.ENGLISH; break;
        }
        localeResolver.setLocale(request,response,locale);
        LOGGER.info("successful logged in "+user.getUserEmail());
    }
}
