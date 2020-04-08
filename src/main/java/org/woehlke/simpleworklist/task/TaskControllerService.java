package org.woehlke.simpleworklist.task;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.simpleworklist.context.Context;
import org.woehlke.simpleworklist.taskstate.TaskState;
import org.woehlke.simpleworklist.user.UserSessionBean;

import java.util.Locale;

public interface TaskControllerService {

    String getTaskStatePage(
        TaskState taskState,
        Context context,
        Pageable pageRequest,
        UserSessionBean userSession,
        Locale locale,
        Model model
    );

}
