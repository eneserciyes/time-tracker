package tr.com.ogedik.timetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.com.ogedik.commons.model.JiraUser;

import java.util.Date;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JTTWorklog {
      private JiraUser author;
      private String issueKey;
      private String issueSummary;
      private String worklogExplanation;
      private Date started;
      private String timeSpent;
}
