package org.woehlke.simpleworklist.breadcrumb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.project.Project;
import org.woehlke.simpleworklist.task.Task;
import org.woehlke.simpleworklist.task.TaskState;

import java.util.Locale;
import java.util.Stack;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class BreadcrumbServiceImpl implements BreadcrumbService {

    private final MessageSource messageSource;

    @Autowired
    public BreadcrumbServiceImpl(MessageSource messageSource) {
        this.messageSource=messageSource;
    }

    @Override
    public Breadcrumb getBreadcrumbForShowRootProject(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProjectRoot();
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForShowOneProject(Project thisProject, Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProjectRoot();
            if (thisProject.getId() > 0) {
                Stack<Project> stack = new Stack<>();
                Project breadcrumbProject = thisProject;
                while (breadcrumbProject != null) {
                    stack.push(breadcrumbProject);
                    breadcrumbProject = breadcrumbProject.getParent();
                }
                while (!stack.empty()) {
                    breadcrumb.addProject(stack.pop());
                }
            }
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskstate(TaskState taskstate, Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code = taskstate.getCode();
        String name = messageSource.getMessage(code,null,locale);
        breadcrumb.addTaskstate(name,taskstate.getUrl());
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInTaskstate(String taskstate, Task task, Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addTaskstate(taskstate);
        breadcrumb.addTask(task);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskstateAll(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="layout.page.all";
        String name= messageSource.getMessage(code,null,locale);
        String url="/taskstate/all";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForTaskInProject(Project thisProject, Task task, Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        breadcrumb.addProject(thisProject);
        breadcrumb.addTask(task);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserProfileAndMenu(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangeName(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.name";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/name";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangePassword(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.password";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/password";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContexts(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.contexts";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/contexts";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextAdd(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.add.context";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/context/add";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextEdit(Locale locale, Context context) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.edit.context";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/context/edit/"+context.getId();
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserContextDelete(Locale locale, Context context) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.delete.context";
        name= messageSource.getMessage(code,null,locale);
        url="/context/delete/"+context.getId();
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForUserChangeLanguage(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.profile.change.language";
        name= messageSource.getMessage(code,null,locale);
        url="/user/selfservice/language";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForMessagesBetweenCurrentAndOtherUser(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.user.user2user.messages";
        name= messageSource.getMessage(code,null,locale);
        url="/search";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }

    @Override
    public Breadcrumb getBreadcrumbForSearchResults(Locale locale) {
        Breadcrumb breadcrumb = new Breadcrumb(locale);
        String code="pages.user.profile";
        String name= messageSource.getMessage(code,null,locale);
        String url="/user/selfservice/profile";
        breadcrumb.addPage(name,url);
        code="pages.search";
        name= messageSource.getMessage(code,null,locale);
        url="/search";
        breadcrumb.addPage(name,url);
        return breadcrumb;
    }


}