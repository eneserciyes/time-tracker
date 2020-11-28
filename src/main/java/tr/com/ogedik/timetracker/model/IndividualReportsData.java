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
public class IndividualReportsData {
    private List<IndividualReportEntry> data;

    public IndividualReportsData(){
        data = new ArrayList<>();
    }

    public void addEntry(IndividualReportEntry entry){
        data.add(entry);
    }
}
