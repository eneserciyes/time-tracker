package tr.com.ogedik.timetracker.services;

import tr.com.ogedik.commons.rest.response.model.JTTWorklog;
import tr.com.ogedik.commons.rest.response.model.WorklogContainer;

public interface WorklogService {
  WorklogContainer retrieveWorklogs(
      String authenticatedUsername, String startDate, String endDate, String isUserOnly);

  Boolean createWorklog(JTTWorklog worklog);

  Boolean updateWorklog(JTTWorklog worklog);
}
