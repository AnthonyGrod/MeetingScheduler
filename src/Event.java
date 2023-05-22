import java.util.ArrayList;

public abstract class Event {

    public Event() {
        this.start = null;
        this.end = null;
    }

    protected String start;
    protected String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public static int compareTime(String time1, String time2) {
        // Parse the time strings into integers for comparison
        int hour1 = Integer.parseInt(time1.split(":")[0]);
        int minute1 = Integer.parseInt(time1.split(":")[1]);

        int hour2 = Integer.parseInt(time2.split(":")[0]);
        int minute2 = Integer.parseInt(time2.split(":")[1]);

        // Compare the hours
        if (hour1 < hour2) {
            return -1;
        } else if (hour1 > hour2) {
            return 1;
        }

        // If the hours are the same, compare the minutes
        if (minute1 < minute2) {
            return -1;
        } else if (minute1 > minute2) {
            return 1;
        }

        // If both hours and minutes are the same, return 0 for equality
        return 0;
    }

    public static String addTime(String time1, String time2) {
        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");

        int hours1 = Integer.parseInt(parts1[0]);
        int minutes1 = Integer.parseInt(parts1[1]);

        int hours2 = Integer.parseInt(parts2[0]);
        int minutes2 = Integer.parseInt(parts2[1]);

        int totalMinutes = minutes1 + minutes2;
        int additionalHours = totalMinutes / 60;
        int minutesResult = totalMinutes % 60;
        int hoursResult = hours1 + hours2 + additionalHours;

        if (hoursResult >= 24) {
            hoursResult %= 24;
        }

        String result = String.format("%02d:%02d", hoursResult, minutesResult);
        return result;
    }

    public static boolean areEventsOverlaping(Event e1, Event e2) {
        String laterStart = (compareTime(e1.getStart(), e2.getStart()) == 1 ? e1.getStart() : e2.getStart());
        String earlierEnd = (compareTime(e1.getEnd(), e2.getEnd()) == -1 ? e1.getEnd() : e2.getEnd());

        if (compareTime(laterStart, earlierEnd) != -1) {
            return false;
        }
        return true;
    }

    // Caution: next event is next meeting's start hour or end of working dat
    public static boolean isMeetingPossible(String start, String duration, String nextEvent) {
        if (compareTime(addTime(start, duration), nextEvent) != 1) {
            return true;
        }
        return false;
    }

    public static String getNext(ArrayList<ArrayList<Meeting>> meetings, ArrayList<WorkingHours> workingHours) {
        if (meetings.get(0).isEmpty() || meetings.get(1).isEmpty()) {
            if (meetings.get(0).isEmpty() && meetings.get(1).isEmpty()) {
                return (compareTime(workingHours.get(0).getEnd(), workingHours.get(1).getEnd()) == -1
                    ? workingHours.get(0).getEnd() : workingHours.get(1).getEnd());
            } else if (meetings.get(0).isEmpty()) {
                meetings.get(1).remove(0);
                return (compareTime(workingHours.get(0).getEnd(), meetings.get(1).get(0).getStart()) == -1
                        ? workingHours.get(0).getEnd() : meetings.get(1).get(0).getStart());
            } else {
                meetings.get(1).remove(1);
                return (compareTime(workingHours.get(1).getEnd(), meetings.get(0).get(0).getStart()) == -1
                        ? workingHours.get(1).getEnd() : meetings.get(0).get(0).getStart());
            }
        }
        else {
            int res = compareTime(meetings.get(0).get(0).getStart(), meetings.get(1).get(0).getStart());
            String ret = compareTime(meetings.get(0).get(0).getStart(), meetings.get(1).get(0).getStart()) == -1
                    ? meetings.get(0).get(0).getStart() : meetings.get(1).get(0).getStart();
            if (res == 0) {
                meetings.get(0).remove(0);
                meetings.get(1).remove(0);
            } else if (res == -1) {
                meetings.get(0).remove(0);
            } else {
                meetings.get(1).remove(0);
            }
            return ret;
        }
    }


    public static boolean isTimeInsideEvent(String iterator, ArrayList<ArrayList<Meeting>> meetings) {
        if (meetings.get(0).isEmpty()) {
            if (meetings.get(1).isEmpty()) {
                return false;
            } else {
                return (compareTime(iterator, meetings.get(1).get(0).getStart()) != -1);
            }
        } else if (meetings.get(1).isEmpty()) {
            return (compareTime(iterator, meetings.get(0).get(0).getStart()) != -1);
        } else {
            return (compareTime(iterator, meetings.get(0).get(0).getStart()) != -1
                    || compareTime(iterator, meetings.get(1).get(0).getStart()) != -1);
        }
    }

    public static String getEndOfOngoingEvent(String iterator, ArrayList<ArrayList<Meeting>> meetings, String duration, ArrayList<WorkingHours> workingHours) {
        if (meetings.get(0).isEmpty()) {
            if (meetings.get(1).isEmpty()) {
                return workingHours.get(0).getEnd();
            }
            return meetings.get(1).get(0).getEnd();
        } else if (meetings.get(1).isEmpty()) {
            return meetings.get(0).get(0).getEnd();
        } else {
            return (compareTime(meetings.get(1).get(0).getEnd(), meetings.get(0).get(0).getEnd()) == -1
                    ? meetings.get(1).get(0).getEnd() : meetings.get(0).get(0).getEnd());
        }
    }
}