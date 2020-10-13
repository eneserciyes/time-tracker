package tr.com.ogedik.timetracker;

import tr.com.ogedik.commons.rest.response.model.Issue;
import tr.com.ogedik.timetracker.model.TeamReportsIssue;

/*
 * @author enes.erciyes
 */
public class TimeTrackerUtil {

    public static TeamReportsIssue convertIssueToTeamReportsIssue(Issue issue){
        return TeamReportsIssue.builder().key(issue.getKey())
                .summary(issue.getFields().getSummary())
                .assignee(issue.getFields().getAssignee()).build();
    }


}
