package tr.com.ogedik.timetracker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.com.ogedik.commons.constants.Headers;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.rest.AbstractController;
import tr.com.ogedik.commons.rest.response.AbstractResponse;
import tr.com.ogedik.timetracker.model.JTTWorklog;
import tr.com.ogedik.timetracker.services.WorklogService;
import tr.com.ogedik.timetracker.services.WorklogServiceImpl;
import tr.com.ogedik.timetracker.services.integration.TimeTrackerIntegrationService;

import javax.validation.Valid;

/*
 * @author enes.erciyes
 * */

@RequestMapping(Services.Path.TRACKER)
@Controller
public class TimeTrackerController extends AbstractController {

  private final WorklogService worklogService;

  private static final Logger logger = LogManager.getLogger(TimeTrackerController.class);

  public TimeTrackerController(WorklogServiceImpl worklogService) {
    this.worklogService = worklogService;
  }

  @GetMapping(Services.Path.WORKLOGS)
  public AbstractResponse getWorklogs(
      @RequestHeader(value = Headers.AUTH_USER) String authenticatedUsername,
      @RequestParam String startDate,
      @RequestParam String endDate) {
    logger.info(
        "worklogs for {} between {} and {} requested", authenticatedUsername, startDate, endDate);
    return AbstractResponse.build(
        worklogService.retrieveWorklogs(authenticatedUsername, startDate, endDate));
  }

  @PostMapping(Services.Path.WORKLOGS)
  public AbstractResponse createWorklog(@Valid @RequestBody JTTWorklog worklog) {
    logger.info("Worklog creation for {} requested", worklog.getIssueKey());
    return AbstractResponse.build(worklogService.createWorklog(worklog));
  }
}
