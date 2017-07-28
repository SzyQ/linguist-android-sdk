package mobi.klimaszewski.linguist;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import mobi.klimaszewski.services.DiscoveryInterface;


public final class DiscoveryService extends Service {

    private final IBinder binder = new DiscoveryInterface.Stub() {
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
