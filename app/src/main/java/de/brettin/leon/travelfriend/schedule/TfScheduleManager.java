package de.brettin.leon.travelfriend.schedule;

import android.content.Context;

/**
 * Created by Leon on 06.04.18.
 */

public class TfScheduleManager {

    public static void schedule(Context context) {
        TfPositionUpdateScheduler positionUpdateScheduler = new TfPositionUpdateScheduler();
        positionUpdateScheduler.scheduleTask(context);
    }
}
