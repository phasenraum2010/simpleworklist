package org.woehlke.simpleworklist.control;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.entities.RegistrationProcess;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.model.LoginFormBean;
import org.woehlke.simpleworklist.model.RegisterFormBean;
import org.woehlke.simpleworklist.model.UserAccountFormBean;
import org.woehlke.simpleworklist.services.RegistrationProcessService;
import org.woehlke.simpleworklist.services.UserService;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private UserService userService;

    @Inject
    private RegistrationProcessService registrationProcessService;

    /**
     * Login Formular. If User is not logged in, this page will be displayed for
     * all page-URLs which need login.
     * @param model
     * @return Login Screen.
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginFormular(Model model){
		LoginFormBean loginFormBean = new LoginFormBean();
		model.addAttribute("loginFormBean",loginFormBean);
		return "user/loginForm";
	}

    /**
     * Perform login.
     * @param loginFormBean
     * @param result
     * @param model
     * @return Shows Root Category after successful login or login form with error messages.
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPerform(@Valid LoginFormBean loginFormBean,
			BindingResult result, Model model){
		boolean authorized = userService.authorize(loginFormBean);
		if(!result.hasErrors() && authorized){
			return "redirect:/";
		} else {
			String objectName="loginFormBean";
			String field="userEmail";
			String defaultMessage="Email or Password wrong.";
			FieldError e = new FieldError(objectName, field, defaultMessage);
			result.addError(e);
			return "user/loginForm";
		}
	}

    /**
     * @param model
     * @return List of all registered users.
     */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getRegisteredUsers(Model model){
		List<UserAccount> users = userService.findAll();
		model.addAttribute("users",users);
		return "user/users";
	}

    /**
     * Register as new user by entering the email-address which is
     * unique and the login identifier.
     * @param model
     * @return Formular for entering Email-Address for Registration
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerNewUserRequestForm(Model model){
        RegisterFormBean registerFormBean = new RegisterFormBean();
        model.addAttribute("registerFormBean",registerFormBean);
        return "user/registerForm";
    }

    /**
     * Register new User: Store the Request and send Email for Verification.
     * @param registerFormBean
     * @param result
     * @param model
     * @return info page at success or return to form with error messages.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerNewUserRequestStoreAndSendEmailForVerification(@Valid RegisterFormBean registerFormBean,
                                 BindingResult result, Model model){
        if(result.hasErrors()){
            return "user/registerForm";
        } else {
            //TODO: this here is wrong, or not?
            registrationProcessService.checkIfResponseIsInTime(registerFormBean.getEmail());
            if(userService.isEmailAvailable(registerFormBean.getEmail())){
                if(registrationProcessService.isRetryAndMaximumNumberOfRetries(registerFormBean.getEmail())){
                    String objectName="registerFormBean";
                    String field="email";
                    String defaultMessage="Maximum Number of Retries reached.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                    return "user/registerForm";
                } else {
                    registrationProcessService.sendEmailForVerification(registerFormBean.getEmail());
                    return "user/registerSentMail";
                }
            } else {
                String objectName="registerFormBean";
                String field="email";
                String defaultMessage="Email is already in use.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/registerForm";
            }
        }
    }

    /**
     * Register as new user: The URL in the Verification Email clicked by User.
     * @param confirmId
     * @param model
     * @return Formular for Entering Account Data or Error Messages.
     */
    @RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.GET)
    public String registerNewUserCheckResponseAndRegistrationForm(@PathVariable String confirmId,Model model){
        logger.info("GET /confirm/"+confirmId);
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if(o!=null){
            registrationProcessService.registratorClickedInEmail(o);
            UserAccountFormBean userAccountFormBean = new UserAccountFormBean();
            userAccountFormBean.setUserEmail(o.getEmail());
            model.addAttribute("userAccountFormBean",userAccountFormBean);
            return "user/registerConfirmed";
        } else {
            return "user/registerNotConfirmed";
        }
    }

    /**
     * Saving Account Data from Formular and forward to login page.
     * @param userAccountFormBean
     * @param result
     * @param confirmId
     * @param model
     * @return login page at success or page with error messages.
     */
    @RequestMapping(value = "/confirm/{confirmId}", method = RequestMethod.POST)
    public String registerNewUserRegistrationStore(@PathVariable String confirmId,
                                     @Valid UserAccountFormBean userAccountFormBean,
                                     BindingResult result, Model model){
        logger.info("POST /confirm/"+confirmId+" : "+userAccountFormBean.toString());
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if(o!=null){
            boolean passwordsMatch = userAccountFormBean.passwordsAreTheSame();
            if(!result.hasErrors() && passwordsMatch){
                userService.createUser(userAccountFormBean,o);
                registrationProcessService.userCreated(o);
                return "user/registerDone";
            } else {
               if(!passwordsMatch){
                   String objectName="userAccountFormBean";
                   String field="userPassword";
                   String defaultMessage="Passwords aren't the same.";
                   FieldError e = new FieldError(objectName, field, defaultMessage);
                   result.addError(e);
               }
               return "user/registerConfirmed";
            }
        } else {
            return "user/registerNotConfirmed";
        }
    }

    /**
     * Visitor who might be Registered, but not yet logged in, clicks
     * on 'password forgotten' at login formular.
     * @param model
     * @return a Formular for entering the email-adress.
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String passwordForgottenForm(Model model){
        RegisterFormBean registerFormBean = new RegisterFormBean();
        model.addAttribute("registerFormBean",registerFormBean);
        return "user/resetPasswordForm";
    }

    /**
     * If email-address exists, send email with Link for password-Reset.
     * @param registerFormBean
     * @param result
     * @param model
     * @return info page if without errors or formular again displaying error messages.
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String passwordForgottenPost(@Valid RegisterFormBean registerFormBean,
                                        BindingResult result, Model model){
        if(result.hasErrors()) {
            logger.info("----------------------");
            logger.info(registerFormBean.toString());
            logger.info(result.toString());
            logger.info(model.toString());
            logger.info("----------------------");
            return "user/resetPasswordForm";
        }  else {
            logger.info(registerFormBean.toString());
            logger.info(result.toString());
            logger.info(model.toString());
            if(userService.findByUserEmail(registerFormBean.getEmail())==null){
                String objectName="registerFormBean";
                String field="email";
                String defaultMessage="This Email is not registered.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                return "user/resetPasswordForm";
            } else {
                registrationProcessService.sendPasswordResetTo(registerFormBean.getEmail());
                return "user/resetPasswordSentMail";
            }

        }
    }

    /**
     * User clicked on Link in Email for Password-Recovery.
     * @param confirmId
     * @param model
     * @return a Formular for entering the new Password.
     */
    @RequestMapping(value = "/passwordResetConfirm/{confirmId}", method = RequestMethod.GET)
    public String enterNewPasswordFormular(@PathVariable String confirmId,Model model){
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        if(o!=null){
            registrationProcessService.usersPasswordChangeClickedInEmail(o);
            UserAccount ua = userService.findByUserEmail(o.getEmail());
            UserAccountFormBean userAccountFormBean = new UserAccountFormBean();
            userAccountFormBean.setUserEmail(o.getEmail());
            userAccountFormBean.setUserFullname(ua.getUserFullname());
            model.addAttribute("userAccountFormBean",userAccountFormBean);
            return "user/resetRasswordConfirmed";
        } else {
            return "user/resetPasswordNotConfirmed";
        }
    }

    /**
     * Save new Password.
     * @param userAccountFormBean
     * @param result
     * @param confirmId
     * @param model
     * @return Info Page for success or back to formular with error messages.
     */
    @RequestMapping(value = "/passwordResetConfirm/{confirmId}", method = RequestMethod.POST)
    public String enterNewPasswordPost(@Valid UserAccountFormBean userAccountFormBean, BindingResult result,
                                       @PathVariable String confirmId, Model model){
        RegistrationProcess o = registrationProcessService.findByToken(confirmId);
        boolean passwordsMatch = userAccountFormBean.passwordsAreTheSame();
        if(o!=null){
            if(!result.hasErrors() && passwordsMatch){
                userService.changeUsersPassword(userAccountFormBean, o);
                registrationProcessService.usersPasswordChanged(o);
                return "redirect:/login";
            } else {
                if(!passwordsMatch){
                    String objectName="userAccountFormBean";
                    String field="userPassword";
                    String defaultMessage="Passwords aren't the same.";
                    FieldError e = new FieldError(objectName, field, defaultMessage);
                    result.addError(e);
                }
                return "user/resetRasswordConfirmed";
            }
        } else {
            return "user/resetPasswordNotConfirmed";
        }
    }

}
