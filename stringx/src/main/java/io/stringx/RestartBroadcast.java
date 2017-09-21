package io.stringx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class RestartBroadcast extends BroadcastReceiver {

    public static final String ACTION = "io.stringx.ACTION_RESTART";
    private Activity activity;

    public RestartBroadcast(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
