package org.woehlke.simpleworklist.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.woehlke.simpleworklist.services.SearchService;

import javax.inject.Inject;

/**
 * Created by tw on 14.02.16.
 */
@Component
public class ApplicationStartUpListener implements
        ApplicationListener<ContextStartedEvent>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartUpListener.class);

    @Inject
    private SearchService searchService;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        LOGGER.info("----------------------------------------------------");
        LOGGER.info("onApplicationEvent and resetSearchIndex "+event.toString());
        LOGGER.info("----------------------------------------------------");
        searchService.resetSearchIndex();
        LOGGER.info("----------------------------------------------------");
    }


}