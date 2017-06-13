package mobi.klimaszewski.linguist;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import mobi.klimaszewski.services.DiscoveryInterface;

public class ServiceTranslationFactory implements TranslationsFactory {

    private final Object lock = new Object();
    private boolean isConnected;
    @Nullable
    private DiscoveryInterface remoteInterface;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (lock) {
                LL.d("Connected to remote service");
                remoteInterface = DiscoveryInterface.Stub.asInterface(service);
                isConnected = true;
                lock.notify();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            synchronized (lock) {
                LL.d("Failed to connect to remote service!");
                isConnected = false;
                remoteInterface = null;
                lock.notify();
            }
        }
    };
    private Context context;

    public ServiceTranslationFactory(Context context) {
        this.context = context;
    }

    @Override
    public void hello(String packageName) {
        try {
            connect();
            if (remoteInterface != null) {
                remoteInterface.hello(packageName);
            }
        } catch (RemoteException e) {
            LL.e("Failed to hello to service", e);
        } finally {
            disconnect();
        }
    }

    private synchronized void connect() {
        synchronized (lock) {
            LL.d("Connecting to service");
            Intent service = new Intent();
            service.setComponent(new ComponentName("mobi.klimaszewski.linguist.services", "mobi.klimaszewski.services.DiscoveryService"));
            try {
                context.bindService(service, connection, Context.BIND_AUTO_CREATE);
                if (!isConnected) {
                    try {
                        lock.wait();
                        LL.d("Connected to service");
                    } catch (InterruptedException ignored) {
                        LL.e("Error connecting", ignored);
                    }
                } else {
                    LL.d("IS already connected: " + remoteInterface);
                }
            } catch (SecurityException e) {
                LL.e("Not allowed!", e);
            }
        }
    }

    private void disconnect() {
        synchronized (lock) {
            if (isConnected) {
                context.unbindService(connection);
            }
            remoteInterface = null;
            isConnected = false;
        }
    }
}
