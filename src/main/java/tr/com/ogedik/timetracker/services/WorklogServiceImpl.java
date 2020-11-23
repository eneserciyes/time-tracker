package tr.com.ogedik.timetracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.rest.request.model.CreateUpdateWorklogRequest;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.commons.rest.response.model.JTTWorklog;
import tr.com.ogedik.commons.rest.response.model.WorklogContainer;
import tr.com.ogedik.commons.util.DateUtils;
import tr.com.ogedik.scrumier.proxy.clients.IntegrationProxy;

import java.util.stream.Collectors;

/*
 * @author enes.erciyes
 */
@Service
public class WorklogServiceImpl implements WorklogService {

  @Autowired private IntegrationProxy integrationProxy;

  @Override
  public WorklogContainer retrieveWorklogs(
      String authenticatedUsername, String startDate, String endDate, String isUserOnly) {
    JQLSearchResult searchResult =
        integrationProxy.getIssuesWithWorklogs(authenticatedUsername, startDate, endDate);

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
    return integrationProxy.createNewWorklog(convertWorklogToRequest(worklog));
  }

  @Override
  public Boolean updateWorklog(JTTWorklog worklog) {
    return integrationProxy.updateWorklog(convertWorklogToRequest(worklog));
  }

  private CreateUpdateWorklogRequest convertWorklogToRequest(JTTWorklog worklog) {
    return CreateUpdateWorklogRequest.builder()
        .id(worklog.getId())
        .issueKey(worklog.getIssueKey())
        .comment(worklog.getWorklogExplanation())
        .started(worklog.getStarted())
        .timeSpentSeconds(worklog.getTimeSpentSeconds())
        .build();
  }
}
