package org.woehlke.simpleworklist.project;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.woehlke.simpleworklist.breadcrumb.Breadcrumb;
import org.woehlke.simpleworklist.breadcrumb.BreadcrumbService;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.context.ContextService;
import org.woehlke.simpleworklist.user.UserSessionBean;
import org.woehlke.simpleworklist.user.account.UserAccount;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class ProjectControllerServiceImpl implements ProjectControllerService {

    private final ProjectService projectService;
    private final ContextService contextService;
    private final BreadcrumbService breadcrumbService;

    @Autowired
    public ProjectControllerServiceImpl(ProjectService projectService, ContextService contextService, BreadcrumbService breadcrumbService) {
        this.projectService = projectService;
        this.contextService = contextService;
        this.breadcrumbService = breadcrumbService;
    }

    public void addNewProject(
        long projectId,
        UserSessionBean userSession,
        Context context,
        Locale locale,
        Model model
    ) {
        log.info("private addNewProject projectId="+projectId);
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        Project thisProject = null;
        Project project = null;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            project = Project.newRootProjectFactory(userAccount);
            if(userSession.getContextId() == 0L){
                model.addAttribute("mustChooseArea", true);
                project.setContext(userAccount.getDefaultContext());
            } else {
                project.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId);
            project = Project.newProjectFactory(thisProject);
        }
        Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
        model.addAttribute("breadcrumb", breadcrumb);
        model.addAttribute("thisProject", thisProject);
        model.addAttribute("project", project);
    }

    public String addNewProjectPersist(
        long projectId,
        UserSessionBean userSession,
        Project project,
        Context context,
        BindingResult result,
        Locale locale, Model model,
        String template
    ){
        log.info("private addNewProjectPersist projectId="+projectId+" "+project.toString());
        UserAccount userAccount = context.getUserAccount();
        userSession.setLastProjectId(projectId);
        model.addAttribute("userSession",userSession);
        if(result.hasErrors()){
            Project thisProject = null;
            if (projectId == 0) {
                thisProject = new Project();
                thisProject.setId(0L);
            } else {
                thisProject = projectService.findByProjectId(projectId);
            }
            Breadcrumb breadcrumb = breadcrumbService.getBreadcrumbForShowOneProject(thisProject,locale);
            model.addAttribute("breadcrumb", breadcrumb);
            model.addAttribute("thisProject", thisProject);
            model.addAttribute("project", project);
            return template;
        } else {
            if (projectId == 0) {
                if(userSession.getContextId()>0) {
                    project.setContext(context);
                }
                project = projectService.saveAndFlush(project);
                projectId = project.getId();
            } else {
                Project thisProject = projectService.findByProjectId(projectId);
                List<Project> children = thisProject.getChildren();
                children.add(project);
                thisProject.setChildren(children);
                project.setParent(thisProject);
                project = projectService.saveAndFlush(project);
                projectId = project.getId();
                log.info("project:     "+ project.toString());
                log.info("thisProject: "+ thisProject.toString());
            }
            return "redirect:/project/" + projectId;
        }
    }

    public Project getProject(long projectId, UserAccount userAccount, UserSessionBean userSession){
        Project thisProject;
        if (projectId == 0) {
            thisProject = new Project();
            thisProject.setId(0L);
            if(userSession.getContextId() == 0L){
                thisProject.setContext(userAccount.getDefaultContext());
            } else {
                Context context = contextService.findByIdAndUserAccount(userSession.getContextId(), userAccount);
                thisProject.setContext(context);
            }
        } else {
            thisProject = projectService.findByProjectId(projectId);
        }
        return thisProject;
    }
}