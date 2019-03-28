package org.woehlke.simpleworklist.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by tw on 14.02.16.
 */
@Controller
public class PagesController extends AbstractController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public final String home() {
        return "redirect:/tasks/inbox";
    }

}
