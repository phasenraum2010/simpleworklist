package org.woehlke.simpleworklist.user.domain.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.user.domain.account.UserAccountForm;
import org.woehlke.simpleworklist.user.services.UserAccountService;
import org.woehlke.simpleworklist.user.services.UserRegistrationService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(path = "/user/register")
public class UserRegistrationController {

    private final UserAccountService userAccountService;
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserAccountService userAccountService, UserRegistrationService userRegistrationService) {
        this.userAccountService = userAccountService;
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * Register as new user by entering the email-address which is
     * unique and the login identifier.
     *
     * @param model Model
     * @return Formular for entering Email-Address for Registration
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public final String registerGet(Model model) {
        log.debug("registerGet");
        UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
        model.addAttribute("userRegistrationForm", userRegistrationForm);
        return "user/register/registerForm";
    }

    /**
     * Register new User: Store the Request and send Email for Verification.
     *
     * @param userRegistrationForm UserRegistrationForm
     * @param result BindingResult
     * @param model Model
     * @return info page at success or return to form with error messages.
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public final String registerPost(
            @Valid UserRegistrationForm userRegistrationForm,
            BindingResult result,
            Model model
    ) {
        log.debug("registerPost");
        if (result.hasErrors()) {
            return "user/register/registerForm";
        } else {
            userRegistrationService.registrationCheckIfResponseIsInTime(userRegistrationForm.getEmail());
            if (userAccountService.isEmailAvailable(userRegistrationForm.getEmail())) {
                if (userRegistrationService.registrationIsRetryAndMaximumNumberOfRetries(userRegistrationForm.getEmail())) {
                    String objectName = "userRegistrationForm";
                    String field = "email";
                    String defaultMessage = "Maximum Number of Retries reached.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                    return "user/register/registerForm";
                } else {
                    userRegistrationService.registrationSendEmailTo(userRegistrationForm.getEmail());
                    return "user/register/registerConfirmSentMail";
                }
            } else {
                String objectName = "userRegistrationForm";
                String field = "email";
                String defaultMessage = "Email is already in use.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/register/registerForm";
            }
        }
    }

    /**
     * Register as new user: The URL in the Verification Email clicked by User.
     *
     * @param confirmId String
     * @param model Model
     * @return Formular for Entering Account Task or Error Messages.
     */
    @RequestMapping(path = "/confirm/{confirmId}", method = RequestMethod.GET)
    public final String registerConfirmGet(
        @PathVariable String confirmId,
        Model model
    ) {
        log.debug("registerConfirmGet");
        log.debug("GET /confirm/" + confirmId);
        UserRegistration o = userRegistrationService.findByToken(confirmId);
        if (o != null) {
            userRegistrationService.registrationClickedInEmail(o);
            UserAccountForm userAccountForm = new UserAccountForm();
            userAccountForm.setUserEmail(o.getEmail());
            model.addAttribute("userAccountForm", userAccountForm);
            return "user/register/registerConfirmForm";
        } else {
            return "user/register/registerConfirmFailed";
        }
    }

    /**
     * Saving Account Task from Formular and forward to login page.
     *
     * @param userAccountForm UserAccountForm
     * @param result BindingResult
     * @param confirmId String
     * @param model Model
     * @return login page at success or page with error messages.
     */
    @RequestMapping(path = "/confirm/{confirmId}", method = RequestMethod.POST)
    public final String registerConfirmPost(
        @PathVariable String confirmId,
        @Valid UserAccountForm userAccountForm,
        BindingResult result,
        Model model
    ) {
        log.debug("registerConfirmPost");
        log.debug("POST /confirm/" + confirmId + " : " + userAccountForm.toString());
        userRegistrationService.registrationCheckIfResponseIsInTime(userAccountForm.getUserEmail());
        UserRegistration oUserRegistration = userRegistrationService.findByToken(confirmId);
        if (oUserRegistration != null) {
            boolean passwordsMatch = userAccountForm.passwordsAreTheSame();
            if (!result.hasErrors() && passwordsMatch) {
                userAccountService.createUser(userAccountForm);
                userRegistrationService.registrationUserCreated(oUserRegistration);
                return "user/register/registerConfirmFinished";
            } else {
                if (!passwordsMatch) {
                    String objectName = "userAccountForm";
                    String field = "userPassword";
                    String defaultMessage = "Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/register/registerConfirmForm";
            }
        } else {
            return "user/register/registerConfirmFailed";
        }
    }
}
