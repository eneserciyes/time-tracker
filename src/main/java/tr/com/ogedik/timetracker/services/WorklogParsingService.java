package tr.com.ogedik.timetracker.services;

import tr.com.ogedik.timetracker.model.WorklogContainer;

public interface WorklogParsingService {
    WorklogContainer retrieveWorklogs(String authenticatedUsername, String startDate, String endDate);
}
