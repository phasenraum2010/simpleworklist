package org.woehlke.simpleworklist.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.project.Project;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskRepository;
import org.woehlke.simpleworklist.domain.task.TaskService;
import org.woehlke.simpleworklist.domain.task.TaskState;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository ) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean projectHasNoTasks(Project project) {
        log.debug("projectHasNoTasks");
        //TODO: #244 change List<Task> to Page<Task>
        return taskRepository.findByProject(project).isEmpty();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findbyTaskstate(
        @NotNull TaskState taskState,
        @NotNull Context context,
        @NotNull Pageable request
    ) {
        if(taskState == TaskState.FOCUS){
            return taskRepository.findByFocusAndContext(true,context,request);
        }else {
            return taskRepository.findByTaskStateAndContext(taskState, context, request);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByProject(
        @NotNull Project thisProject, @NotNull Pageable request
    ) {
        log.debug("findByProject: ");
        log.debug("---------------------------------");
        log.debug("thisProject: "+thisProject);
        log.debug("---------------------------------");
        return taskRepository.findByProject(thisProject,request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Page<Task> findByRootProject(@NotNull Context context, @NotNull Pageable request) {
        log.debug("findByRootProject: ");
        return taskRepository.findByProjectIsNullAndContext(context, request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Task findOne(@Min(1L) long taskId) {
        log.debug("findOne: ");
        if(taskRepository.existsById(taskId)) {
            return taskRepository.getOne(taskId);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaTaskstate(@NotNull Task task) {
        log.debug("updatedViaTaskstate");
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProject(@NotNull Task task) {
        log.debug("updatedViaProject");
        long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(), task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task updatedViaProjectRoot(@NotNull Task task) {
        log.debug("updatedViaProject");
        //long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        //task.setOrderIdProject(++maxOrderIdProject);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted Task: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToInbox(@NotNull Task task) {
        log.debug("addToInbox");
        task.setUuid(UUID.randomUUID().toString());
        task.setRootProject();
        task.unsetFocus();
        task.setTaskState(TaskState.INBOX);
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToProject(@NotNull Task task) {
        log.debug("addToProject");
        task.setUuid(UUID.randomUUID().toString());
        task.unsetFocus();
        long maxOrderIdProject = this.getMaxOrderIdProject(task.getProject(),task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task addToRootProject(@NotNull Task task) {
        log.debug("addToRootProject");
        task.setUuid(UUID.randomUUID().toString());
        task.setRootProject();
        task.unsetFocus();
        task.moveToInbox();
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(task.getTaskState(),task.getContext());
        task.setOrderIdTaskState(++maxOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("persisted: " + task.outProject());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToRootProject(@NotNull Task task) {
        task.moveTaskToRootProject();
        long maxOrderIdProject = this.getMaxOrderIdProjectRoot(task.getContext());
        task.setOrderIdProject(++maxOrderIdProject);
        return taskRepository.saveAndFlush(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToAnotherProject(@NotNull Task task, @NotNull Project project) {
        boolean okContext = task.hasSameContextAs(project);
        if(okContext) {
            task.moveTaskToAnotherProject(project);
            long maxOrderIdProject = this.getMaxOrderIdProject(
                task.getProject(),
                task.getContext()
            );
            task.setOrderIdProject(++maxOrderIdProject);
            taskRepository.saveAndFlush(task);
        }
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void moveAllCompletedToTrash(@NotNull Context context) {
        long maxOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            context
        );
        long newOrderIdTaskState = maxOrderIdTaskState;
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> taskListCompleted = taskRepository.findByTaskStateAndContextOrderByOrderIdTaskStateAsc(
            TaskState.COMPLETED,
            context
        );
        for (Task task : taskListCompleted) {
            newOrderIdTaskState++;
            task.setOrderIdTaskState(newOrderIdTaskState);
            task.moveToTrash();
        }
        taskRepository.saveAll(taskListCompleted);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void emptyTrash(@NotNull Context context) {
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> taskList = taskRepository.findByTaskStateAndContext(
            TaskState.TRASH,
            context
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> taskListChanged = new ArrayList<>(taskList.size());
        for(Task task: taskList){
            task.emptyTrash();
            taskListChanged.add(task);
        }
        taskRepository.saveAll(taskListChanged);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdTaskState(@NotNull TaskState taskState, @NotNull Context context) {
        Task task = taskRepository.findTopByTaskStateAndContextOrderByOrderIdTaskStateDesc(
            taskState,
            context
        );
        return (task==null) ? 0 : task.getOrderIdTaskState();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProject(@NotNull Project project, @NotNull Context context) {
        Task task = taskRepository.findTopByProjectAndContextOrderByOrderIdProjectDesc(
            project,
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public long getMaxOrderIdProjectRoot(@NotNull Context context) {
        Task task = taskRepository.findTopByProjectIsNullAndContextOrderByOrderIdProjectDesc(
            context
        );
        return (task==null) ? 0 : task.getOrderIdProject();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        final long lowerOrderIdTaskState = destinationTask.getOrderIdTaskState();
        final long higherOrderIdTaskState = sourceTask.getOrderIdTaskState();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
            lowerOrderIdTaskState,
            higherOrderIdTaskState,
            taskState,
            context
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByTaskState();
            log.debug(task.outTaskstate());
            tasksMoved.add(task);
        }
        destinationTask.moveUpByTaskState();
        log.debug(destinationTask.outTaskstate());
        tasksMoved.add(destinationTask);
        sourceTask.setOrderIdTaskState( lowerOrderIdTaskState );
        log.debug(sourceTask.outTaskstate());
        tasksMoved.add(sourceTask);
        taskRepository.saveAll(tasksMoved);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByTaskState(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" moveTasks DOWN By TaskState: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.debug("-------------------------------------------------------------------------------");
        TaskState taskState = sourceTask.getTaskState();
        Context context = sourceTask.getContext();
        long lowerOrderIdTaskState = sourceTask.getOrderIdTaskState();
        long higherOrderIdTaskState = destinationTask.getOrderIdTaskState();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdTaskStateBetweenLowerTaskAndHigherTask(
            lowerOrderIdTaskState,
            higherOrderIdTaskState,
            taskState,
            context
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByTaskState();
            log.debug(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdTaskState(higherOrderIdTaskState);
        destinationTask.moveDownByTaskState();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" DONE: moveTasks DOWN By TaskState("+taskState.name()+"): "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.debug("-------------------------------------------------------------------------------");
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByProjectRoot(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.debug("-------------------------------------------------------------------------------");
        Context context = sourceTask.getContext();
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            context
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByProject();
            log.debug(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(lowerOrderIdProject);
        destinationTask.moveUpByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" DONE: moveTasks UP By ProjectRoot: "+sourceTask.getId() +" -> "+ destinationTask.getId());
        log.debug("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByProjectRoot(@NotNull Task sourceTask, @NotNull Task destinationTask) {
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" START moveTasks UP By Project Root");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");
        Context context = sourceTask.getContext();
        final long lowerOrderIdProject = sourceTask.getOrderIdProject();
        final long higherOrderIdProject = destinationTask.getOrderIdProject();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectRootBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            context
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByProject();
            log.debug(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(higherOrderIdProject);
        destinationTask.moveDownByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" DONE moveTasks UP By Project Root");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksUpByProject(@NotNull Task sourceTask, @NotNull Task destinationTask ) {
        Project project = sourceTask.getProject();
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" START moveTasks UP By Project("+project.out()+"):");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");
        long lowerOrderIdProject = destinationTask.getOrderIdProject();
        long higherOrderIdProject = sourceTask.getOrderIdProject();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveUpByProject();
            log.debug(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(lowerOrderIdProject);
        destinationTask.moveUpByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" DONE moveTasks UP By Project("+project.out()+"):");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void moveTasksDownByProject(@NotNull Task sourceTask, @NotNull Task destinationTask) {
        Project project = sourceTask.getProject();
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" START moveTasks DOWN By Project("+project.out()+"):");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");
        final long lowerOrderIdProject = sourceTask.getOrderIdProject();
        final long higherOrderIdProject = destinationTask.getOrderIdProject();
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasks = taskRepository.getTasksByOrderIdProjectBetweenLowerTaskAndHigherTask(
            lowerOrderIdProject,
            higherOrderIdProject,
            project
        );
        //TODO: #244 change List<Task> to Page<Task>
        List<Task> tasksMoved = new ArrayList<>(tasks.size()+2);
        for(Task task:tasks){
            task.moveDownByProject();
            log.debug(task.outProject());
            tasksMoved.add(task);
        }
        sourceTask.setOrderIdProject(higherOrderIdProject);
        destinationTask.moveDownByProject();
        tasksMoved.add(sourceTask);
        tasksMoved.add(destinationTask);
        taskRepository.saveAll(tasksMoved);
        log.debug("-------------------------------------------------------------------------------");
        log.debug(" DONE smoveTasks DOWN By Project("+project.out()+"):");
        log.debug(" "+sourceTask.outProject() +" -> "+ destinationTask.outProject());
        log.debug("-------------------------------------------------------------------------------");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToInbox(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.INBOX,
            task.getContext()
        );
        task.moveToInbox();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to inbox: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToToday(@NotNull Task task) {
        Date now = new Date();
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TODAY,
            task.getContext()
        );
        task.moveToToday();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to today: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToNext(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.NEXT,
            task.getContext()
        );
        task.moveToNext();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to next: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToWaiting(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.WAITING,
            task.getContext()
        );
        task.moveToWaiting();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to next: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToSomeday(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.SOMEDAY,
            task.getContext()
        );
        task.moveToSomeday();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to someday: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToFocus(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.FOCUS,
            task.getContext()
        );
        task.moveToFocus();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to focus: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToCompleted(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.COMPLETED,
            task.getContext()
        );
        task.moveToCompletedTasks();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to completed: " + task.outTaskstate());
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Task moveTaskToTrash(@NotNull Task task) {
        long newOrderIdTaskState = this.getMaxOrderIdTaskState(
            TaskState.TRASH,
            task.getContext()
        );
        task.moveToTrash();
        task.setOrderIdTaskState(++newOrderIdTaskState);
        task = taskRepository.saveAndFlush(task);
        log.debug("moved to trash: " + task.outTaskstate());
        return task;
    }
}
