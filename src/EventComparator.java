import java.util.Comparator;

public class EventComparator implements Comparator<Meeting> {
    @Override
    public int compare(Meeting meeting1, Meeting meeting2) {
        String time1 = meeting1.getStart();
        String time2 = meeting2.getStart();

        return Event.compareTime(time1, time2);
    }
}