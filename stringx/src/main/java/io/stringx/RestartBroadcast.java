package io.stringx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;


public class RestartBroadcast<T extends Activity> extends BroadcastReceiver {

    public static final String ACTION = "io.stringx.ACTION_RESTART";
    private Activity activity;
    private Class<T> activityClass;

    public RestartBroadcast(Activity activity, Class<T> activityClass) {
        this.activity = activity;
        this.activityClass = activityClass;
    }

    public static <T extends Activity> RestartBroadcast register(Activity activity, Class<T> activityClass) {
        RestartBroadcast broadcast = new RestartBroadcast(activity, activityClass);
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcast, new IntentFilter(RestartBroadcast.ACTION));
        return broadcast;
    }

    public static void unregister(Activity activity, RestartBroadcast broadcast) {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcast);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();
        Intent newIntent = new Intent(activity, activityClass);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(newIntent);
    }
}
