package tr.com.ogedik.timetracker.model;

import lombok.*;
import tr.com.ogedik.commons.model.BusinessObject;
import tr.com.ogedik.commons.model.JiraUser;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamReportsIssue implements BusinessObject {
    private String key;
    private String summary;
    private JiraUser assignee;
}
