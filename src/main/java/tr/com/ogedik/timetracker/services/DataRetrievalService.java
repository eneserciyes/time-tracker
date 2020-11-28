package tr.com.ogedik.timetracker.services;

import tr.com.ogedik.commons.rest.response.BoardsResponse;
import tr.com.ogedik.commons.rest.response.SprintResponse;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;
import tr.com.ogedik.commons.rest.response.model.WorklogContainer;
import tr.com.ogedik.timetracker.model.IndividualReportsData;
import tr.com.ogedik.timetracker.model.TeamReportsIssuesData;

public interface DataRetrievalService {
  TeamReportsIssuesData getTeamReportsData(String sprintCode);

  BoardsResponse getAllBoards();

  SprintResponse getSprintsInABoard(String boardId);

  JQLSearchResult getRecentIssues();

  IndividualReportsData getIndividualReportsData(WorklogContainer aTrue, String startDate, String endDate);
}
