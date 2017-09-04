package io.stringx;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Locale;
import java.util.Map;

public final class ClientService extends Service {

    private final IBinder binder = new TranslationInterface.Stub() {


        @Override
        public void getConfig(final ConfigCallback callback) throws RemoteException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LL.d("Retrieving config");
                    Linguist linguist = Linguist.get(getApplicationContext());
                    TranslationConfig translationConfig = new TranslationConfig();
                    if (linguist != null) {
                        Locale locale = linguist.getAppDefaultLocale();
                        translationConfig.packageName = getPackageName();
                        translationConfig.defaultLanguage = Language.fromCode(locale.getLanguage());
                        translationConfig.desiredLanguage = Language.fromCode(linguist.getDeviceDefaultLocale().getLanguage());
                        linguist.fetch(translationConfig);
                        LL.d("Got " + translationConfig.resources.size() + ": " + translationConfig.defaultLanguage);
                    }
                    try {
                        callback.onConfigCreated(translationConfig);
                    } catch (RemoteException e) {
                        LL.e("Failed to get config", e);
                    }
                }
            }).start();

        }

        @Override
        public void onTranslationCompleted(Map translation) throws RemoteException {
            LL.d("Applying translation");
            Linguist linguist = Linguist.get(getApplicationContext());
            if (linguist != null) {
                linguist.applyTranslation(translation);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
