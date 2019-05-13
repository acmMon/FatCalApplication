package com.example.fatcalapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;

public class SampleBootReceiver extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentSchedule   = new Intent(context, ScheduledIntentService.class);
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            // Set the alarm to start at approximately 11:59 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);


            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }
}
