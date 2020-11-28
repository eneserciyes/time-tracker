package tr.com.ogedik.timetracker.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.ogedik.commons.constants.Configs;
import tr.com.ogedik.commons.constants.IssueFields;
import tr.com.ogedik.commons.expection.ErrorException;
import tr.com.ogedik.commons.expection.constants.CommonErrorType;
import tr.com.ogedik.commons.rest.response.BoardsResponse;
import tr.com.ogedik.commons.rest.response.SprintResponse;
import tr.com.ogedik.commons.rest.response.model.*;
import tr.com.ogedik.commons.util.DateUtils;
import tr.com.ogedik.scrumier.proxy.clients.IntegrationProxy;
import tr.com.ogedik.timetracker.TimeTrackerUtil;
import tr.com.ogedik.timetracker.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        .data(getWorklogDoughnutChartData(searchResult, sprintCode))
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

  @Override
  public IndividualReportsData getIndividualReportsData(WorklogContainer container, String startDate, String endDate) {

    Map<String, Float> authorLoggedMap = new HashMap<>();
    Map<String, String> authorKeyNameMap = new HashMap<>();
    for (JTTWorklog worklog : container.getWorklogs()){
      String authorKey = worklog.getAuthor().getKey();
      authorKeyNameMap.putIfAbsent(authorKey, worklog.getAuthor().getDisplayName());

      float worklogHours = (float)worklog.getTimeSpentSeconds()/3600;

      if(authorLoggedMap.containsKey(authorKey)){
        float currentLoggedHours = authorLoggedMap.get(authorKey);
        authorLoggedMap.put(authorKey, currentLoggedHours + worklogHours);
      } else {
        authorLoggedMap.putIfAbsent(authorKey, worklogHours);
      }
    }

    int intervalWorkingHours = DateUtils.getWorkingDaysBetweenTwoDates(DateUtils.convertTimelessDateString(startDate),
            DateUtils.convertTimelessDateString(endDate)) * Configs.WORKING_HOURS;

    IndividualReportsData individualReportsData = new IndividualReportsData();
    for(Map.Entry<String, Float> entry: authorLoggedMap.entrySet()){
      individualReportsData.addEntry(new IndividualReportEntry(
              authorKeyNameMap.get(entry.getKey()),
              (int)((entry.getValue()/intervalWorkingHours)*100)
              ,entry.getValue())
      );
    }
    return individualReportsData;
  }


  private List<TeamReportsIssue> getIssuesInASprint(JQLSearchResult searchResult) {

    return searchResult.getIssues().stream()
        .map(TimeTrackerUtil::convertIssueToTeamReportsIssue)
        .collect(Collectors.toList());
  }

  private WorklogDoughnutChartData getWorklogDoughnutChartData(
      JQLSearchResult searchResult, String sprintCode) {
    if (searchResult.getIssues() == null || searchResult.getIssues().isEmpty()) return null;
    Sprint selectedSprint = integrationProxy.getSprint(sprintCode);
    int sprintDurationInDays =
        DateUtils.getWorkingDaysBetweenTwoDates(
            DateUtils.convertTimelessDateString(selectedSprint.getStartDate().substring(0, 10)),
            DateUtils.convertTimelessDateString(selectedSprint.getEndDate().substring(0, 10)));

    List<String> issueKeyList =
        searchResult.getIssues().stream().map(Issue::getKey).collect(Collectors.toList());

    List<Double> totalWorkSpent =
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
                .map(e-> (double)e/3600)
            .collect(Collectors.toList());
    double notLoggedTimeInSeconds =
        sprintDurationInDays * 8  - totalWorkSpent.stream().reduce(0.0, Double::sum);
    if (notLoggedTimeInSeconds > 0) {
      issueKeyList.add("Unlogged Time");
      totalWorkSpent.add(notLoggedTimeInSeconds);
    }
    return WorklogDoughnutChartData.builder()
        .issueLabels(issueKeyList)
        .totalTimeSpentByIssue(totalWorkSpent)
        .build();
  }
}
