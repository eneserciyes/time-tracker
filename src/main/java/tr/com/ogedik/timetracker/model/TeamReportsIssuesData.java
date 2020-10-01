package tr.com.ogedik.timetracker.model;

import lombok.*;
import tr.com.ogedik.commons.model.BusinessObject;

import java.util.List;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamReportsIssuesData implements BusinessObject {
    List<TeamReportsIssue> issues;
    WorklogDoughnutChartData data;
}
