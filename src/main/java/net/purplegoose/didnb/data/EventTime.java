package net.purplegoose.didnb.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.purplegoose.didnb.enums.GameEvent;
import net.purplegoose.didnb.enums.Weekday;

@AllArgsConstructor
@Data
public class EventTime {

    GameEvent eventName;
    Weekday weekday;

    String warnStartTime;
    String warnEndTime;

    int startHour;
    int startMinute;

    int endHour;
    int endMinute;

}