package tr.com.ogedik.timetracker.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.com.ogedik.commons.constants.Headers;
import tr.com.ogedik.commons.constants.Services;
import tr.com.ogedik.commons.rest.AbstractController;
import tr.com.ogedik.commons.rest.response.AbstractResponse;
import tr.com.ogedik.commons.rest.response.model.JTTWorklog;
import tr.com.ogedik.timetracker.services.DataRetrievalService;
import tr.com.ogedik.timetracker.services.WorklogService;

import javax.validation.Valid;

/*
 * @author enes.erciyes
 * */

@RequestMapping(Services.Path.TRACKER)
@Controller
public class TimeTrackerController extends AbstractController {

  @Autowired private WorklogService worklogService;
  @Autowired private DataRetrievalService dataRetrievalService;

  private static final Logger logger = LogManager.getLogger(TimeTrackerController.class);

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

  @PostMapping(Services.Path.WORKLOGS)
  public AbstractResponse createWorklog(@Valid @RequestBody JTTWorklog worklog) {
    logger.info("Worklog creation for {} requested", worklog.getIssueKey());
    return AbstractResponse.build(worklogService.createWorklog(worklog));
  }

  @PutMapping(Services.Path.WORKLOGS)
  public AbstractResponse updateWorklog(@Valid @RequestBody JTTWorklog worklog) {
    return AbstractResponse.build(worklogService.updateWorklog(worklog));
  }

  @GetMapping(Services.Path.ISSUES_IN_SPRINT)
  public AbstractResponse getIssuesInSprint(@RequestParam String sprintCode) {
    return AbstractResponse.build(dataRetrievalService.getTeamReportsData(sprintCode));
  }

  @GetMapping(Services.Path.BOARDS)
  public AbstractResponse getAllBoards() {
    return AbstractResponse.build(dataRetrievalService.getAllBoards());
  }

  @GetMapping(Services.Path.SPRINTS)
  public AbstractResponse getSprintsInABoard(@RequestParam String boardId) {
    return AbstractResponse.build(dataRetrievalService.getSprintsInABoard(boardId));
  }

  @GetMapping(Services.Path.ISSUES)
  public AbstractResponse getRecentIssues() {
    return AbstractResponse.build(dataRetrievalService.getRecentIssues());
  }

  @GetMapping(Services.Path.INDIVIDUAL_REPORT)
  public AbstractResponse getIndividualReports(
          @RequestHeader(value = Headers.AUTH_USER) String authenticatedUsername,
          @RequestParam String startDate,
          @RequestParam String endDate){

    return AbstractResponse.build(dataRetrievalService.getIndividualReportsData(
            worklogService.retrieveWorklogs(authenticatedUsername, startDate, endDate, "false"),
            startDate,
            endDate
    ));
  }
}
