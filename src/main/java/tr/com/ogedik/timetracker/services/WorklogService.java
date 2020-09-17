package tr.com.ogedik.timetracker.services;

import tr.com.ogedik.timetracker.model.JTTWorklog;
import tr.com.ogedik.timetracker.model.WorklogContainer;

public interface WorklogService {
    WorklogContainer retrieveWorklogs(String authenticatedUsername, String startDate, String endDate);
    Boolean createWorklog(JTTWorklog worklog);
}
