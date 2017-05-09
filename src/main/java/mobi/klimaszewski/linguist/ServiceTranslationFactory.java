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
            remoteInterface = LinguistServicesRemoteInterface.Stub.asInterface(service);
            synchronized (lock) {
                isConnected = true;
                lock.notify();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            synchronized (lock) {
                isConnected = false;
                lock.notify();
            }
        }
    };
    private LinguistServicesRemoteInterface remoteInterface;
    private Context context;

    public ServiceTranslationFactory(Context context) {
        this.context = context;
        Intent service = new Intent("mobi.klimaszewski.action.TRANSLATE");
        service.setPackage("mobi.klimaszewski.linguist.services");
        context.bindService(service, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public List<String> translate(List<String> text, String fromCode, String toCode) {
        synchronized (lock) {
            if (!isConnected) {
                try {
                    //TODO Handle case when it was disconnected
                    lock.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        try {
            return remoteInterface.translate(context.getPackageName(), fromCode, toCode, text);
        } catch (RemoteException e) {
            LL.d("Fuck " + e.getLocalizedMessage());
            return text;
        }
    }
}
