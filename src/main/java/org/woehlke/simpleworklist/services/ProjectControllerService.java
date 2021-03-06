package org.woehlke.simpleworklist.services;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.user.session.UserSessionBean;
import org.woehlke.simpleworklist.user.domain.account.UserAccount;

import java.util.Locale;

public interface ProjectControllerService {

    void addNewProjectToProjectIdForm(
        long projectId,
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    );

    void addNewProjectToProjectRootForm(
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    );

    String addNewProjectToProjectIdPersist(
        long projectId,
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale,
        Model model
    );

    String addNewProjectToProjectRootPersist(
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale,
        Model model
    );

    Project getProject(
        long projectId,
        UserAccount userAccount,
        UserSessionBean userSession
    );

    void moveTaskToTaskAndChangeTaskOrderInProject(Task sourceTask, Task destinationTask);
    void moveTaskToTaskAndChangeTaskOrderInProjectRoot(Task sourceTask, Task destinationTask);
}
