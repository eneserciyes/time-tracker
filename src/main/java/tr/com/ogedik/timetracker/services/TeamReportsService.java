package tr.com.ogedik.timetracker.services;

import tr.com.ogedik.timetracker.model.TeamReportsIssuesData;

public interface TeamReportsService {
    TeamReportsIssuesData getIssuesDataBySprintCode(String sprintCode);
}
