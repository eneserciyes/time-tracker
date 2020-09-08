package tr.com.ogedik.timetracker.services;

import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.abstraction.AbstractService;
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
public class WorklogParsingServiceImpl extends AbstractService implements WorklogParsingService {

  private final TimeTrackerIntegrationService timeTrackerIntegrationService;

  public WorklogParsingServiceImpl(TimeTrackerIntegrationService timeTrackerIntegrationService) {
    this.timeTrackerIntegrationService = timeTrackerIntegrationService;
  }

  @Override
  public WorklogContainer retrieveWorklogs(
      String authenticatedUsername, String startDate, String endDate) {
    JQLSearchResult searchResult =
        timeTrackerIntegrationService.getJQLSearchResult(authenticatedUsername, startDate, endDate);

    WorklogContainer worklogContainer = new WorklogContainer();

    searchResult
        .getIssues()
        .forEach(
            issue -> {
              String issueKey = issue.getKey();
              String issueSummary = issue.getFields().getSummary();
              worklogContainer.addWorklogs(
                  issue.getFields().getWorklog().getWorklogs().stream()
                      .filter(
                          worklogRecord -> DateUtils.isBetween(worklogRecord, startDate, endDate))
                      .map(
                          worklogRecord ->
                              new JTTWorklog(
                                  worklogRecord.getAuthor(),
                                  issueKey,
                                  issueSummary,
                                  worklogRecord.getComment(),
                                  DateUtils.convertWorklogDateString(worklogRecord.getStarted()),
                                  worklogRecord.getTimeSpent()))
                      .collect(Collectors.toList()));
            });
    return worklogContainer;
  }
}
