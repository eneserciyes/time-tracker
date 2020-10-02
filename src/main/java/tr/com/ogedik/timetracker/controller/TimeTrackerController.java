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
import tr.com.ogedik.timetracker.services.TeamReportsService;
import tr.com.ogedik.timetracker.services.WorklogService;
import tr.com.ogedik.timetracker.services.WorklogServiceImpl;

import javax.validation.Valid;

/*
 * @author enes.erciyes
 * */

@RequestMapping(Services.Path.TRACKER)
@Controller
public class TimeTrackerController extends AbstractController {

  private final WorklogService worklogService;
  private final TeamReportsService teamReportsService;

  private static final Logger logger = LogManager.getLogger(TimeTrackerController.class);

  public TimeTrackerController(
      WorklogServiceImpl worklogService, TeamReportsService teamReportsService) {
    this.worklogService = worklogService;
    this.teamReportsService = teamReportsService;
  }

  @GetMapping(Services.Path.WORKLOGS)
  public AbstractResponse getWorklogs(
      @RequestHeader(value = Headers.AUTH_USER) String authenticatedUsername,
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam String isUserOnly) {
    logger.info(
        "worklogs for {} between {} and {} requested", authenticatedUsername, startDate, endDate);
    return AbstractResponse.build(
        worklogService.retrieveWorklogs(authenticatedUsername, startDate, endDate, isUserOnly));
  }

  @GetMapping(Services.Path.ISSUES_IN_SPRINT)
  public AbstractResponse getIssuesInSprint(@RequestParam String sprintCode) {
    return AbstractResponse.build(teamReportsService.getIssuesDataBySprintCode(sprintCode));
  }

  @GetMapping(Services.Path.BOARDS)
  public AbstractResponse getAllBoards() {
    return AbstractResponse.build(teamReportsService.getAllBoards());
  }

  @GetMapping(Services.Path.SPRINTS)
  public AbstractResponse getSprintsInABoard(@RequestParam String boardId) {
    return AbstractResponse.build(teamReportsService.getSprintsInABoard(boardId));
  }

  @PostMapping(Services.Path.WORKLOGS)
  public AbstractResponse createWorklog(@Valid @RequestBody JTTWorklog worklog) {
    logger.info("Worklog creation for {} requested", worklog.getIssueKey());
    return AbstractResponse.build(worklogService.createWorklog(worklog));
  }
}
