package org.woehlke.simpleworklist.user.selfservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.woehlke.simpleworklist.domain.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.common.domain.AbstractController;
import org.woehlke.simpleworklist.domain.context.NewContextForm;
import org.woehlke.simpleworklist.common.language.UserChangeLanguageForm;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.common.language.Language;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;
import org.woehlke.simpleworklist.user.services.UserAccountAccessService;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Fert on 14.03.2016.
 */
@Slf4j
@Controller
@RequestMapping(path = "/user/selfservice")
public class UserSelfserviceController extends AbstractController {

    private final UserAccountAccessService userAccountAccessService;

    @Autowired
    public UserSelfserviceController(UserAccountAccessService userAccountAccessService) {
        this.userAccountAccessService = userAccountAccessService;
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String userProfileAndMenu(
        @PageableDefault(
            sort = "userFullname",
            direction = Sort.Direction.DESC) Pageable request,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userProfileAndMenu");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        Page<UserAccount> users = userAccountService.findAll(request);
        for(UserAccount u:users){
            log.debug(u.getUserFullname()+": "+u.getUserEmail());
        }
        Map<Long,Integer> usersToNewMessages = userAccountService.getNewIncomingMessagesForEachOtherUser(user);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserProfileAndMenu(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("usersToNewMessages", usersToNewMessages);
        model.addAttribute("users", users);
        model.addAttribute("thisUser", user);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/profile";
    }

    @RequestMapping(path = "/name", method = RequestMethod.GET)
    public String userNameForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userNameForm");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        UserChangeNameForm bean = new UserChangeNameForm(user.getUserFullname());
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangeName(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("username", bean);
        model.addAttribute("thisUser", user);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/name";
    }

    @RequestMapping(path = "/name", method = RequestMethod.POST)
    public String userNameStore(
        @Valid UserChangeNameForm username,
        BindingResult result,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userNameStore");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        if(result.hasErrors()) {
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangeName(locale,userSession);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("username", username);
            model.addAttribute("thisUser", user);
            model.addAttribute("userSession", userSession);
            return "user/selfservice/name";
        } else {
            user.setUserFullname(username.getUserFullname());
            userAccountService.saveAndFlush(user);
            model.addAttribute("userSession", userSession);
            return "redirect:/user/selfservice/profile";
        }
    }

    @RequestMapping(path = "/password", method = RequestMethod.GET)
    public String userPasswordForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userPasswordForm");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        UserChangePasswordForm userChangePasswordForm = new UserChangePasswordForm();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangePassword(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisUser", user);
        model.addAttribute("userChangePasswordForm", userChangePasswordForm);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/password";
    }

    @RequestMapping(path = "/password", method = RequestMethod.POST)
    public String userPasswordStore(
        @Valid UserChangePasswordForm userChangePasswordForm,
        BindingResult result,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ) {
        log.debug("userPasswordStore");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        log.debug("---------------------------------------------------------");
        log.debug("userPasswordStore");
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangePassword(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisUser", user);
        model.addAttribute("userChangePasswordForm", userChangePasswordForm);
        if(result.hasErrors()) {
            log.debug("result has Errors");
            for(ObjectError error : result.getAllErrors()){
                log.debug(error.toString());
            }
            model.addAttribute("userSession", userSession);
            return "user/selfservice/password";
        } else {
            if(! userChangePasswordForm.passwordsAreTheSame()){
                log.debug("passwords Are Not The Same");
                String objectName = "userChangePasswordForm";
                String field = "userPassword";
                String defaultMessage = "Passwords aren't the same.";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                for(ObjectError error : result.getAllErrors()){
                    log.debug(error.toString());
                }
                model.addAttribute("userSession", userSession);
                return "user/selfservice/password";
            }
            if(!userAccountAccessService.confirmUserByLoginAndPassword(
                user.getUserEmail(), userChangePasswordForm.getOldUserPassword())
            ){
                log.debug("old Password is wrong");
                String objectName = "userChangePasswordForm";
                String field = "oldUserPassword";
                String defaultMessage = "Password is incorrect";
                FieldError e = new FieldError(objectName, field, defaultMessage);
                result.addError(e);
                for(ObjectError error : result.getAllErrors()){
                    log.debug(error.toString());
                }
                model.addAttribute("userSession", userSession);
                return "user/selfservice/password";
            }
            log.debug("OK");
            userAccountAccessService.changeUsersPassword(userChangePasswordForm,user);
            model.addAttribute("userSession", userSession);
            return "redirect:/user/selfservice/profile";
        }
    }

    @RequestMapping(path = "/contexts", method = RequestMethod.GET)
    public String userContextsForm(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userContextsForm");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        UserChangeDefaultContextForm bean = new UserChangeDefaultContextForm();
        bean.setId(user.getId());
        bean.setDefaultContext(user.getDefaultContext());
        model.addAttribute("thisUser", bean);
        List<Context> contexts = contextService.getAllForUser(user);
        model.addAttribute("contexts", contexts);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContexts(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/context/all";
    }

    @RequestMapping(path = "/contexts", method = RequestMethod.POST)
    public String userContextsSave(
            @Valid @ModelAttribute("thisUser") UserChangeDefaultContextForm thisUser,
            BindingResult result,
            @ModelAttribute("userSession") UserSessionBean userSession,
            Locale locale,
            Model model
    ){
        log.debug("userContextsSave");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        List<Context> contexts = contextService.getAllForUser(user);
        model.addAttribute("contexts", contexts);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContexts(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        if(result.hasErrors()){
            log.debug("userContextsSave: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                log.debug(error.toString());
            }
            model.addAttribute("userSession", userSession);
            return "user/selfservice/context/all";
        } else {
            if(user.getId() == thisUser.getId()){
                user.setDefaultContext(thisUser.getDefaultContext());
                userAccountService.saveAndFlush(user);
                userSession.setLastContextId(thisUser.getDefaultContext().getId());
                model.addAttribute("userSession", userSession);
            }
            model.addAttribute("userSession", userSession);
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(path = "/context/add", method = RequestMethod.GET)
    public String userNewContextGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userNewContextGet");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        model.addAttribute("thisUser", user);
        NewContextForm newContext = new NewContextForm();
        model.addAttribute("newContext", newContext);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContextAdd(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/context/add";
    }

    @RequestMapping(path = "/context/add", method = RequestMethod.POST)
    public String userNewContextPost(
        @Valid NewContextForm newContext,
        BindingResult result,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userNewContextPost");
        Context context = super.getContext(userSession);
        UserAccount user = context.getUserAccount();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContextAdd(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisUser", user);
        if(result.hasErrors()){
            log.debug("userNewAreaStore: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                log.debug(error.toString());
            }
            model.addAttribute("userSession", userSession);
            return "user/selfservice/context/add";
        } else {
            contextService.createNewContext(newContext,user);
            model.addAttribute("userSession", userSession);
            return "redirect:/user/selfservice/contexts";
        }
    }

    @RequestMapping(path = "/context/{id}/edit", method = RequestMethod.GET)
    public String userContextEditGet(
        @PathVariable("id") Context context,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userContextEditGet");
        Context contextFromSession = super.getContext(userSession);
        UserAccount user = contextFromSession.getUserAccount();
        model.addAttribute("thisUser", user);
        NewContextForm editContext = new NewContextForm();
        editContext.setNameDe(context.getNameDe());
        editContext.setNameEn(context.getNameEn());
        model.addAttribute("editContext", editContext);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContextEdit(locale, context,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/context/edit";
    }

    @RequestMapping(path = "/context/{id}/edit", method = RequestMethod.POST)
    public String userContextEditPost(
        @Valid NewContextForm editContext,
        @PathVariable("id") Context context,
        BindingResult result,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userContextEditPost");
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContextEdit(locale, context,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        if(result.hasErrors()){
            log.debug("userContextEditPost: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                log.debug(error.toString());
            }
            model.addAttribute("userSession", userSession);
            return "user/selfservice/context/edit";
        } else {
            context.setNameDe(editContext.getNameDe());
            context.setNameEn(editContext.getNameEn());
            contextService.updateContext(context);
            model.addAttribute("userSession", userSession);
            return "redirect:/user/selfservice/contexts";
        }
    }

    //TODO: is in session active? -> display message in frontend
    //TODO: has projects or tasks? -> display message in frontend
    @RequestMapping(path = "/context/{id}/delete", method = RequestMethod.GET)
    public String userDeleteContextGet(
        @PathVariable("id") Context context,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userDeleteContextGet");
        UserAccount thisUser = context.getUserAccount();
        model.addAttribute("userSession",userSession);
        model.addAttribute("thisUser", thisUser);
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserContextDelete(locale,context,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        if(userSession.getLastContextId() == context.getId()){
            log.debug("context is active in session: "+ context);
        } else {
            if(thisUser.getDefaultContext().getId() == context.getId()){
                log.debug("context is default context of this user: "+ context);
            } else {
                if(contextService.contextHasItems(context)){
                    log.debug("context has items: "+ context);
                } else {
                    boolean deleted = contextService.delete(context);
                    if(!deleted){
                        log.debug("context not deleted: "+ context);
                    }
                }
            }
        }
        model.addAttribute("userSession", userSession);
        return "redirect:/user/selfservice/contexts";
    }

    @RequestMapping(path = "/language", method = RequestMethod.GET)
    public String userLanguageGet(
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userLanguageGet");
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        model.addAttribute("thisUser", user);
        model.addAttribute("languages", Language.list());
        model.addAttribute("userChangeLanguageForm",new UserChangeLanguageForm(user.getDefaultLanguage()));
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangeLanguage(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("userSession", userSession);
        return "user/selfservice/language";
    }

    @RequestMapping(path = "/language", method = RequestMethod.POST)
    public String userLanguagePost(
        @Valid UserChangeLanguageForm userChangeLanguageForm,
        BindingResult result,
        @ModelAttribute("userSession") UserSessionBean userSession,
        Locale locale,
        Model model
    ){
        log.debug("userLanguagePost");
        UserAccount user = userAccountLoginSuccessService.retrieveCurrentUser();
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForUserChangeLanguage(locale,userSession);
        model.addAttribute("breadcrumb", breadcrumb);
        if(result.hasErrors()){
            log.debug("userLanguagePost: result has Errors");
            for(ObjectError error : result.getAllErrors()){
                log.debug(error.toString());
            }
            model.addAttribute("userSession", userSession);
            return "user/selfservice/language";
        } else {
            user.setDefaultLanguage(userChangeLanguageForm.getDefaultLanguage());
            userAccountService.saveAndFlush(user);
            String returnUrl;
            switch (userChangeLanguageForm.getDefaultLanguage()){
                case DE: returnUrl="redirect:/user/selfservice/profile?lang=de"; break;
                default: returnUrl="redirect:/user/selfservice/profile?lang=en"; break;
            }
            model.addAttribute("userSession", userSession);
            return returnUrl;
        }
    }

}
