package mobi.klimaszewski.linguist;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import mobi.klimaszewski.services.LinguistServicesRemoteInterface;

public class ServiceTranslationFactory implements TranslationsFactory {

    private final Object lock = new Object();
    private boolean isConnected;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (lock) {
                LL.d("Connected to remote service");
                remoteInterface = LinguistServicesRemoteInterface.Stub.asInterface(service);
                isConnected = true;
                lock.notify();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            synchronized (lock) {
                LL.d("Failed to connect to remote service!");
                isConnected = false;
                lock.notify();
            }
        }
    };
    private LinguistServicesRemoteInterface remoteInterface;
    private Context context;

    public ServiceTranslationFactory(Context context) {
        this.context = context;
    }

    @Override
    public List<String> translate(List<String> text, String fromCode, String toCode) {
        try {
            connect();
            return remoteInterface.translate(context.getPackageName(), fromCode, toCode, text);
        } catch (RemoteException e) {
            LL.d("Fuck " + e.getLocalizedMessage());
            return text;
        } finally {
            disconnect();
        }
    }

    @Override
    public void reply(String packageName, int charactersCount, String name) {
        try {
            connect();
            remoteInterface.hello(packageName, charactersCount, name);
        } catch (RemoteException e) {
            LL.e("Failed to reply to service", e);
        } finally {
            disconnect();
        }
    }

    private synchronized void connect() {
        synchronized (lock) {
            Intent service = new Intent("mobi.klimaszewski.action.TRANSLATE");
            service.setPackage("mobi.klimaszewski.linguist.services");
            context.bindService(service, connection, Context.BIND_AUTO_CREATE);
            if (!isConnected) {
                try {
                    lock.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    private synchronized void disconnect() {
        synchronized (lock) {
            if (isConnected) {
                context.unbindService(connection);
            }
            remoteInterface = null;
        }
    }
}
