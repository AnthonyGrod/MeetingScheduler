public class WorkingHours extends Event {

    public boolean areMeetingsPossible() {
        if (compareTime(this.start, this.end) != -1) {
            return false;
        }
        return true;
    }

    public static WorkingHours getCommonHours(WorkingHours w1, WorkingHours w2) {
        WorkingHours res = new WorkingHours();

        res.setStart(compareTime(w1.getStart(), w2.getStart()) == -1 ? w2.getStart() : w1.getStart());
        res.setEnd(compareTime(w1.getEnd(), w2.getEnd()) == -1 ? w1.getEnd() : w2.getEnd());

        return res;
    }

}