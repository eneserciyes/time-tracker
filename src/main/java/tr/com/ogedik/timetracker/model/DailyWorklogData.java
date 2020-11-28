package tr.com.ogedik.timetracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
 * @author enes.erciyes
 */
@Getter
@Setter
public class DailyWorklogData {
    private List<String> labels;
    private List<DailyWorklogsEntry> datasets;

    public DailyWorklogData(List<String> labels){
        this.labels = labels;
        datasets = new ArrayList<>();
    }

    public void addEntry(DailyWorklogsEntry entry){
        datasets.add(entry);
    }

}
