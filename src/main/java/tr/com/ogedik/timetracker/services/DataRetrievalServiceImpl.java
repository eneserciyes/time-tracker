package tr.com.ogedik.timetracker.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.constants.IssueFields;
import tr.com.ogedik.commons.expection.ErrorException;
import tr.com.ogedik.commons.expection.constants.CommonErrorType;
import tr.com.ogedik.commons.rest.response.BoardsResponse;
import tr.com.ogedik.commons.rest.response.SprintResponse;
import tr.com.ogedik.commons.rest.response.model.Issue;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.commons.rest.response.model.Sprint;
import tr.com.ogedik.commons.rest.response.model.WorklogRecord;
import tr.com.ogedik.commons.util.DateUtils;
import tr.com.ogedik.scrumier.proxy.clients.IntegrationProxy;
import tr.com.ogedik.timetracker.TimeTrackerUtil;
import tr.com.ogedik.timetracker.model.TeamReportsIssue;
import tr.com.ogedik.timetracker.model.TeamReportsIssuesData;
import tr.com.ogedik.timetracker.model.WorklogDoughnutChartData;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @author enes.erciyes
 */
@Service
public class DataRetrievalServiceImpl implements DataRetrievalService {

  @Autowired IntegrationProxy integrationProxy;

  @Override
  public TeamReportsIssuesData getTeamReportsData(String sprintCode) {
    if (!StringUtils.isNumeric(sprintCode)) throw new ErrorException(CommonErrorType.INVALID_INPUT);

    JQLSearchResult searchResult =
        integrationProxy.getIssuesInASprint(
            sprintCode,
            String.join(
                ",",
                IssueFields.WORKLOG,
                IssueFields.SPRINT,
                IssueFields.ASSIGNEE,
                IssueFields.SUMMARY));
    return TeamReportsIssuesData.builder()
        .issues(getIssuesInASprint(searchResult))
        .data(getWorklogDoughnutChartData(searchResult))
        .build();
  }

  @Override
  public BoardsResponse getAllBoards() {
    return integrationProxy.getAllBoards();
  }

  @Override
  public SprintResponse getSprintsInABoard(String boardId) {
    return integrationProxy.getSprintsInABoard(boardId);
  }

  @Override
  public JQLSearchResult getRecentIssues() {
    return integrationProxy.getRecentIssues();
  }

  private List<TeamReportsIssue> getIssuesInASprint(JQLSearchResult searchResult) {

    return searchResult.getIssues().stream()
        .map(TimeTrackerUtil::convertIssueToTeamReportsIssue)
        .collect(Collectors.toList());
  }

  private WorklogDoughnutChartData getWorklogDoughnutChartData(JQLSearchResult searchResult) {
    if (searchResult.getIssues() == null || searchResult.getIssues().isEmpty()) return null;
    Sprint selectedSprint = searchResult.getIssues().get(0).getFields().getSprint();

    List<String> issueKeyList =
        searchResult.getIssues().stream().map(Issue::getKey).collect(Collectors.toList());

    List<Integer> totalWorkSpent =
        searchResult.getIssues().stream()
            .map(
                issue ->
                    issue.getFields().getWorklog().getWorklogs().stream()
                        .filter(
                            worklogRecord ->
                                DateUtils.isBetween(
                                    worklogRecord,
                                    selectedSprint.getStartDate(),
                                    selectedSprint.getEndDate()))
                        .map(WorklogRecord::getTimeSpentSeconds)
                        .reduce(0, Integer::sum))
            .collect(Collectors.toList());

    return WorklogDoughnutChartData.builder()
        .issueLabels(issueKeyList)
        .totalTimeSpentByIssue(totalWorkSpent)
        .build();
  }
}
