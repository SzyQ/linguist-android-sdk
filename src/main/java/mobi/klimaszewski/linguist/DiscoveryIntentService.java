package mobi.klimaszewski.linguist;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DiscoveryIntentService extends IntentService {

    public DiscoveryIntentService() {
        super("Discovery");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LL.d("Discovery fetch: " + getApplicationContext().getPackageName());
        Linguist.getInstance().replyToService();
        LL.d("Discovery replied: " + getApplicationContext().getPackageName());
    }
}
