package org.woehlke.simpleworklist.services;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.domain.context.Context;
import org.woehlke.simpleworklist.domain.task.Task;
import org.woehlke.simpleworklist.domain.task.TaskState;
import org.woehlke.simpleworklist.user.session.UserSessionBean;

import java.util.Locale;

public interface TaskStateControllerService {

    String getTaskStatePage(
        TaskState taskState,
        Context context,
        Pageable pageRequest,
        UserSessionBean userSession,
        Locale locale,
        Model model
    );

    void moveTaskToTaskAndChangeTaskOrderInTaskstate(Task sourceTask, Task destinationTask);
}
