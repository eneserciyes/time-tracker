package tr.com.ogedik.timetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import tr.com.ogedik.commons.model.JiraUser;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorklogContainer{
    //private JiraUser user;
    //private Date startDate;
    //private Date endDate;

    private List<JTTWorklog> worklogs;


    public void addWorklogs(List<JTTWorklog> newWorklogs){
        if (Objects.isNull(worklogs)) {
            worklogs = newWorklogs;
        } else {
            worklogs.addAll(newWorklogs);
        }
    }
}
