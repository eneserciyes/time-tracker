package tr.com.ogedik.timetracker.services.integration;

import tr.com.ogedik.commons.rest.request.model.CreateWorklogRequest;
import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;

public interface TimeTrackerIntegrationService {

    JQLSearchResult getJQLSearchResult(String username, String startDate, String endDate);
    Boolean createWorklog(CreateWorklogRequest worklog);
}
