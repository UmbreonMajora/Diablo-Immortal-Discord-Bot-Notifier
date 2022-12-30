package me.umbreon.didn.notifier;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class InformationNotifier {

    public void runInformationNotifier(JDA jda) {
        Date nextFullMinuteTime = getNextFullMinuteTime();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(Activity.playing("Diablo Immortal | " + jda.getGuilds().size()));
            }
        }, nextFullMinuteTime, 60 * 1000);
    }

    private Date getNextFullMinuteTime() {
        Date date = Calendar.getInstance().getTime();
        date.setMinutes(date.getMinutes() + 1);
        date.setSeconds(0);
        return date;
    }
}
