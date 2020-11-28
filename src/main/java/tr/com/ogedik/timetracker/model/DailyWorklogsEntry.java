package tr.com.ogedik.timetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWorklogsEntry {
    String label;
    String backgroundColor;
    List<Double> data;
}
