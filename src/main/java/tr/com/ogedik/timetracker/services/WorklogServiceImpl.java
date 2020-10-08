package tr.com.ogedik.timetracker.services;

import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.abstraction.AbstractService;
import tr.com.ogedik.commons.rest.request.model.CreateWorklogRequest;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.commons.util.DateUtils;
import tr.com.ogedik.timetracker.model.JTTWorklog;
import tr.com.ogedik.timetracker.model.WorklogContainer;
import tr.com.ogedik.timetracker.services.integration.TimeTrackerIntegrationService;

import java.util.stream.Collectors;

/*
 * @author enes.erciyes
 */
@Service
public class WorklogServiceImpl extends AbstractService implements WorklogService {
  private final TimeTrackerIntegrationService timeTrackerIntegrationService;

  public WorklogServiceImpl(TimeTrackerIntegrationService timeTrackerIntegrationService) {
    this.timeTrackerIntegrationService = timeTrackerIntegrationService;
  }

  @Override
  public WorklogContainer retrieveWorklogs(
      String authenticatedUsername, String startDate, String endDate, String isUserOnly) {
    JQLSearchResult searchResult =
        timeTrackerIntegrationService.getWorklogSearchResult(
            authenticatedUsername, startDate, endDate);

    WorklogContainer worklogContainer = new WorklogContainer();

    searchResult
        .getIssues()
        .forEach(
            issue -> {
              String issueKey = issue.getKey();
              String issueSummary = issue.getFields().getSummary();
              // TODO: Check by user key
              worklogContainer.addWorklogs(
                  issue.getFields().getWorklog().getWorklogs().stream()
                      .filter(
                          worklogRecord ->
                              (DateUtils.isBetween(worklogRecord, startDate, endDate)
                                  && (isUserOnly.equals("false")
                                      || worklogRecord
                                          .getAuthor()
                                          .getName()
                                          .equals(authenticatedUsername))))
                      .map(
                          worklogRecord ->
                              new JTTWorklog(
                                  worklogRecord.getId(),
                                  worklogRecord.getAuthor(),
                                  issueKey,
                                  issueSummary,
                                  worklogRecord.getComment(),
                                  DateUtils.convertWorklogDateString(worklogRecord.getStarted()),
                                  worklogRecord.getTimeSpent(),
                                  worklogRecord.getTimeSpentSeconds()))
                      .collect(Collectors.toList()));
            });
    return worklogContainer;
  }

  @Override
  public Boolean createWorklog(JTTWorklog worklog) {
    CreateWorklogRequest createWorklogRequest =
        CreateWorklogRequest.builder()
            .issueKey(worklog.getIssueKey())
            .comment(worklog.getWorklogExplanation())
            .started(worklog.getStarted())
            .timeSpentSeconds(worklog.getTimeSpentSeconds())
            .build();
    return timeTrackerIntegrationService.createWorklog(createWorklogRequest);
  }
}
