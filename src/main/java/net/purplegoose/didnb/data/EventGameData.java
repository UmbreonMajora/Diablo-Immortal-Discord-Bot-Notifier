package net.purplegoose.didnb.data;
/*
 * Weekday is null, when it's every day.
 */
public class EventGameData {

    private final String warnRange;
    private final String weekday;
    private final String eventStartTime;

    public EventGameData(String warnRange, String weekday, String eventStartTime) {
        this.warnRange = warnRange;
        this.weekday = weekday;
        this.eventStartTime = eventStartTime;
    }

    public String getWarnRange() {
        return warnRange;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }
}
