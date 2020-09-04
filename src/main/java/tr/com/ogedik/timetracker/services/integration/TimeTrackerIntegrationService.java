package tr.com.ogedik.timetracker.services.integration;

import tr.com.ogedik.commons.rest.response.model.JQLSearchResult;

public interface TimeTrackerIntegrationService {

    public JQLSearchResult getJQLSearchResult(String username, String startDate, String endDate);
}
