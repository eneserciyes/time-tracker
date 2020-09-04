package tr.com.ogedik.timetracker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tr.com.ogedik.commons.constants.Headers;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.rest.AbstractController;
import tr.com.ogedik.commons.rest.response.AbstractResponse;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.timetracker.services.WorklogParsingServiceImpl;
import tr.com.ogedik.timetracker.services.integration.TimeTrackerIntegrationService;

/*
 * @author enes.erciyes
 * */

@RequestMapping(Services.Path.TRACKER)
@Controller
public class TimeTrackerController extends AbstractController {

    private final TimeTrackerIntegrationService timeTrackerIntegrationService;
    private final WorklogParsingServiceImpl worklogParsingServiceImpl;

    private static final Logger logger = LogManager.getLogger(TimeTrackerController.class);

    public TimeTrackerController(TimeTrackerIntegrationService timeTrackerIntegrationService, WorklogParsingServiceImpl worklogParsingServiceImpl) {
        this.timeTrackerIntegrationService = timeTrackerIntegrationService;
        this.worklogParsingServiceImpl = worklogParsingServiceImpl;
    }

    @GetMapping(Services.Path.WORKLOGS)
    public AbstractResponse getWorklogs(@RequestHeader(value = Headers.AUTH_USER) String authenticatedUsername,
                                        @RequestParam String startDate, @RequestParam String endDate) {
        logger.info("worklogs for {} between {} and {} requested", authenticatedUsername, startDate, endDate);
        return AbstractResponse.build(worklogParsingServiceImpl.retrieveWorklogs(authenticatedUsername, startDate, endDate));
    }


}
