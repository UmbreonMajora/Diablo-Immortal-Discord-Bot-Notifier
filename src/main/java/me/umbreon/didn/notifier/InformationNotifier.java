package me.umbreon.didn.notifier;

import me.umbreon.didn.utils.TimeUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class InformationNotifier {

    public void runInformationNotifier(JDA jda) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setActivity(Activity.playing("Diablo Immortal | " + jda.getGuilds().size()));
            }
        }, TimeUtil.getNextFullMinute(), 300 * 1000);
    }

}
