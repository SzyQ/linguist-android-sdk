package mobi.klimaszewski.linguist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LinguistDiscovery extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LL.d("Discovery requested");
        context.startService(new Intent(context, DiscoveryIntentService.class));
    }
}
