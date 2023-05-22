import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;


public class Parser {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: <meeting duration> <schedule1> <schedule2");
            return;
        }

        String[] jsons = new String[2];
        String meetingDuration = args[0];
        jsons[0] = args[1];
        jsons[1] = args[2];

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
        String secondIt = commonWorkingHours.getStart();

        while (Event.compareTime(Event.addTime(firstIt, meetingDuration), commonWorkingHours.getEnd()) != 1
                || Event.compareTime(Event.addTime(secondIt, meetingDuration), commonWorkingHours.getEnd()) != 1) {
            String nextEvent = Event.getNext(plannedMeetings, workingHours);

            if (Event.isTimeInsideEvent(firstIt, plannedMeetings)) {
                firstIt = secondIt = Event.getEndOfOngoingEvent(firstIt, plannedMeetings, meetingDuration, workingHours);
            } else if (Event.compareTime(Event.addTime(firstIt, meetingDuration), nextEvent) != -1 ||
                    Event.compareTime(Event.addTime(secondIt, meetingDuration), nextEvent) != -1) {
                // If either worker will be busy in next 'meetingDuration' minutes, we increase both iterators to match the end of certain event
                firstIt = secondIt = Event.getEndOfOngoingEvent(firstIt, plannedMeetings, meetingDuration, workingHours);
            } else {
                ArrayList<String> toAdd = new ArrayList<>();
                toAdd.add(firstIt);
                toAdd.add(Event.addTime(firstIt, Event.addTime(firstIt, meetingDuration)));
                result.add(toAdd);
                firstIt = Event.addTime(firstIt, "00:01");
                secondIt = Event.addTime(secondIt, "00:01");
            }
        }

        System.out.println(result);
    }
}
