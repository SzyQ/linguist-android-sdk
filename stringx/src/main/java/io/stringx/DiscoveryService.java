package io.stringx;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import io.stringx.DiscoveryInterface;


public final class DiscoveryService extends Service {

    private final IBinder binder = new DiscoveryInterface.Stub() {
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
