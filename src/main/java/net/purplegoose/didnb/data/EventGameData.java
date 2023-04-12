package net.purplegoose.didnb.data;

import lombok.Data;

@Data
public class EventGameData {

    private final String warnRange;
    private final String weekday; // Weekday is null, when it's every day.
    private final String eventStartTime;

}
