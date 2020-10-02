package tr.com.ogedik.timetracker.services.integration;

import tr.com.ogedik.commons.rest.request.model.CreateWorklogRequest;
import tr.com.ogedik.commons.rest.response.BoardsResponse;
import tr.com.ogedik.commons.rest.response.SprintResponse;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;

public interface TimeTrackerIntegrationService {

    JQLSearchResult getWorklogSearchResult(String username, String startDate, String endDate);
    Boolean createWorklog(CreateWorklogRequest worklog);
    JQLSearchResult getIssuesInASprintSearchResult(String sprintCode, String fields);
    BoardsResponse getAllBoards();
    SprintResponse getSprintsInABoard(String boardId);
}
