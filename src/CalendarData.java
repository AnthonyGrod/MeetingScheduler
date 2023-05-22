import java.util.List;

public class CalendarData {
    private WorkingHours working_hours;
    private List<Meeting> planned_meeting;

    public WorkingHours getWorking_hours() {
        return working_hours;
    }

    public List<Meeting> getPlanned_meeting() {
        return planned_meeting;
    }

    public void setPlanned_meeting(List<Meeting> planned_meeting) {
        this.planned_meeting = planned_meeting;
    }

    public void setWorking_hours(WorkingHours working_hours) {
        this.working_hours = working_hours;
    }
}