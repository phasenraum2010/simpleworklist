package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.woehlke.simpleworklist.entities.Task;
import org.woehlke.simpleworklist.entities.UserAccount;
import org.woehlke.simpleworklist.entities.enumerations.TaskState;
import org.woehlke.simpleworklist.services.TaskStateService;
import org.woehlke.simpleworklist.services.TaskService;

import javax.inject.Inject;

/**
 * Created by tw on 21.02.16.
 */
@Controller
public class TaskStateController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStateController.class);

    @Inject
    private TaskStateService taskStateService;

    @Inject
    private TaskService taskService;

    @RequestMapping(value = "/tasks/inbox", method = RequestMethod.GET)
    public final String inbox(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getInbox(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Inbox");
        return "tasks/inbox";
    }

    @RequestMapping(value = "/tasks/today", method = RequestMethod.GET)
    public final String today(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getToday(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Today");
        return "tasks/today";
    }

    @RequestMapping(value = "/tasks/next", method = RequestMethod.GET)
    public final String next(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getNext(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Next");
        return "tasks/next";
    }

    @RequestMapping(value = "/tasks/waiting", method = RequestMethod.GET)
    public final String waiting(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getWaiting(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Waiting");
        return "tasks/waiting";
    }

    @RequestMapping(value = "/tasks/scheduled", method = RequestMethod.GET)
    public final String scheduled(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getScheduled(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Scheduled");
        return "tasks/scheduled";
    }

    @RequestMapping(value = "/tasks/someday", method = RequestMethod.GET)
    public final String someday(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getSomeday(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Someday");
        return "tasks/someday";
    }

    @RequestMapping(value = "/tasks/completed", method = RequestMethod.GET)
    public final String completed(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getCompleted(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Completed Tasks");
        return "tasks/completed";
    }

    @RequestMapping(value = "/tasks/trash", method = RequestMethod.GET)
    public final String trash(@RequestParam(defaultValue = "1", required = false) int page, Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        Pageable request =
                new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "lastChangeTimestamp");
        Page<Task> taskPage = taskStateService.getTrash(thisUser, request);
        int current = taskPage.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, taskPage.getTotalPages());
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("dataList", taskPage.getContent());
        model.addAttribute("totalPages", taskPage.getTotalPages());
        model.addAttribute("focustype", "Trash");
        return "tasks/trash";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/inbox", method = RequestMethod.GET)
    public final String moveTaskToInbox(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to inbox");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId,thisUser);
        if(task!=null){
            task.setTaskState(TaskState.INBOX);
            task=taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped "+taskId+" to inbox: "+task.toString());
        }
        return "redirect:/tasks/inbox";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/today", method = RequestMethod.GET)
    public final String moveTaskToToday(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to today");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            task.setTaskState(TaskState.TODAY);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to today: " + task.toString());
        }
        return "redirect:/tasks/today";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/next", method = RequestMethod.GET)
    public final String moveTaskToNext(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to next");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        task.setTaskState(TaskState.NEXT);
        task=taskService.saveAndFlush(task, thisUser);
        LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        return "redirect:/tasks/next";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/waiting", method = RequestMethod.GET)
    public final String moveTaskToWaiting(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to waiting");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null){
            task.setTaskState(TaskState.WAITING);
            task=taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped "+taskId+" to next: "+task.toString());
        }
        return "redirect:/tasks/waiting";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/someday", method = RequestMethod.GET)
    public final String moveTaskToSomeday(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to someday");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            task.setTaskState(TaskState.SOMEDAY);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to someday: " + task.toString());
        }
        return "redirect:/tasks/someday";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/completed", method = RequestMethod.GET)
    public final String moveTaskToCompleted(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to completed");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            task.setTaskState(TaskState.COMPLETED);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to completed: " + task.toString());
        }
        return "redirect:/tasks/completed";
    }

    @RequestMapping(value = "/tasks/move/{taskId}/to/trash", method = RequestMethod.GET)
    public final String moveTaskToTrash(@PathVariable long taskId, Model model) {
        LOGGER.info("dragged and dropped "+taskId+" to trash");
        UserAccount thisUser = userService.retrieveCurrentUser();
        Task task = taskService.findOne(taskId, thisUser);
        if(task!=null) {
            task.setTaskState(TaskState.TRASHED);
            task = taskService.saveAndFlush(task, thisUser);
            LOGGER.info("dragged and dropped " + taskId + " to trash: " + task.toString());
        }
        return "redirect:/tasks/trash";
    }

    @RequestMapping(value = "/tasks/completed/deleteall", method = RequestMethod.GET)
    public final String deleteallCompleted(Model model) {
        UserAccount thisUser = userService.retrieveCurrentUser();
        taskStateService.deleteAllCompleted(thisUser);
        return "redirect:/tasks/trash";
    }
}