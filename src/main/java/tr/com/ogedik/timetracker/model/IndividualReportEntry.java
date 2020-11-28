package tr.com.ogedik.timetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IndividualReportEntry {
    String username;
    int logPercentage;
    float loggedHours;
}
