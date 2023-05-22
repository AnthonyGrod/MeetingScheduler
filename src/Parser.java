import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class Parser {
//  For testing purposes
    public static String json1 = "{\"working_hours\":{\"start\":\"09:00\",\"end\":\"19:55\"},\"planned_meeting\":[{\"start\":\"09:00\",\"end\":\"10:30\"},{\"start\":\"12:00\",\"end\":\"13:00\"},{\"start\":\"16:00\",\"end\":\"18:00\"}]}";
    public static String json2 = "{\"working_hours\":{\"start\":\"10:00\",\"end\":\"18:30\"},\"planned_meeting\":[{\"start\":\"10:00\",\"end\":\"11:30\"},{\"start\":\"12:30\",\"end\":\"14:30\"},{\"start\":\"14:30\",\"end\":\"15:00\"},{\"start\":\"16:00\",\"end\":\"17:00\"}]}";
    public static String meetingDurationTest = "00:30";



    public static void main(String[] args) {
//      For testing purposes
        boolean test = false;
        String[] jsons = new String[2];
        jsons[0] = json1;
        jsons[1] = json2;
        String meetingDuration = meetingDurationTest;
        if (!test) {
            if (args.length < 3) {
                System.out.println("Usage: <meeting duration> <schedule1> <schedule2>");
                return;
            }
            meetingDuration = args[0];
            jsons[0] = args[1];
            jsons[1] = args[2];

        }

        Gson gson = new Gson();

        ArrayList<WorkingHours> workingHours = new ArrayList<>();
        ArrayList<ArrayList<Meeting>> plannedMeetings = new ArrayList<>();
        ArrayList<String> starts = new ArrayList<>();
        ArrayList<String> ends = new ArrayList<>();

        EventComparator comparator = new EventComparator();

        for (int i = 0; i < 2; i++) {
            CalendarData calendarData = gson.fromJson(jsons[i], CalendarData.class);

            // Access the parsed data
            workingHours.add(calendarData.getWorking_hours());
            plannedMeetings.add((ArrayList<Meeting>) calendarData.getPlanned_meeting());

            // Sort meetings by their starting hour
            Collections.sort(plannedMeetings.get(i), comparator);

            // Access individual properties
            starts.add(workingHours.get(i).getStart());
            ends.add(workingHours.get(i).getEnd());
        }

        WorkingHours commonWorkingHours = WorkingHours.getCommonHours(workingHours.get(0), workingHours.get(1));
        if (!commonWorkingHours.areMeetingsPossible()) {
            System.out.println("No meetings are possible");
            return;
        }

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String firstIt = commonWorkingHours.getStart();

        ArrayList<String> lastEnds = new ArrayList<>();
        ArrayList<String> endMeeting = new ArrayList<>();
        lastEnds.add(plannedMeetings.get(0).get(plannedMeetings.get(0).size() - 1).getEnd());
        lastEnds.add(plannedMeetings.get(1).get(plannedMeetings.get(1).size() - 1).getEnd());
        String laterEnd = (Event.compareTime(lastEnds.get(0), lastEnds.get(1)) == 1 ? lastEnds.get(0) : lastEnds.get(1));
        if (Event.compareTime(Event.addTime(commonWorkingHours.getEnd(), meetingDuration), laterEnd) != -1) {
            endMeeting.add(laterEnd);
            endMeeting.add(commonWorkingHours.getEnd());
        }

        // Remove meetings that are earlier than common start
        for (int i = 0; i < 2; i++) {
            int size = plannedMeetings.get(i).size();
            for (int j = 0; j < size; j++) {
                if (Event.compareTime(commonWorkingHours.getStart(), plannedMeetings.get(i).get(0).getStart()) == 1) {
                    plannedMeetings.get(i).remove(0);
                }
            }
        }

        while (Event.compareTime(Event.addTime(firstIt, meetingDuration), commonWorkingHours.getEnd()) != 1) {

            if (Event.isTimeInsideEvent(firstIt, plannedMeetings)) {
                firstIt = Event.getEndOfOngoingEvent(plannedMeetings, workingHours, true);
                continue;
            }
            String nextEvent = Event.getNext(plannedMeetings, workingHours, false);
            if (Event.compareTime(Event.addTime(firstIt, meetingDuration), nextEvent) == 1) {
                // If either worker will be busy in next 'meetingDuration' minutes, we increase both iterators to match the end of certain event
                Event.getNext(plannedMeetings, workingHours, true);
                firstIt = Event.getEndOfOngoingEvent(plannedMeetings, workingHours, false);
            } else {
                ArrayList<String> toAdd = new ArrayList<>();
                toAdd.add(firstIt);
                toAdd.add(Event.addTime(firstIt, meetingDuration));
                result.add(toAdd);
                firstIt = Event.addTime(firstIt, "00:01");
            }
        }

        for (int i = result.size() - 1; i >= 1; i--) {
            if (Objects.equals(Event.addTime(result.get(i - 1).get(0), "00:01"), result.get(i).get(0))) {
                String newEnd = result.get(i).get(1);
                result.remove(i);
                result.get(i - 1).set(1, newEnd);
            }
        }

        result.add(endMeeting);

        System.out.println(result);
    }
}
