package de.brettin.leon.travelfriend.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Schedules the position update of the users.
 */
public class TfPositionUpdateScheduler extends BroadcastReceiver {

    public void scheduleTask(Context context) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(context, 121141,
                new Intent(context, TfPositionUpdateScheduler.class), PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // Starts Intent to update Position
        //Intent updatePositionIntent = new Intent(context, TfUpdatePositionIntentService.class);
        // context.startService(updatePositionIntent);


        ComponentName serviceComponent = new ComponentName(context, TfUpdatePositionIntentService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000);
        builder.setOverrideDeadline(10 * 1000);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
