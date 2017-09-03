package io.stringx;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.Locale;
import java.util.Map;

public final class ClientService extends Service {

    private final IBinder binder = new TranslationInterface.Stub() {

        @Override
        public TranslationConfig getConfig() throws RemoteException {
            LL.d("Retrieving config");
            Linguist linguist = Linguist.get(getApplicationContext());
            TranslationConfig translationConfig = new TranslationConfig();
            if (linguist != null) {
                Locale locale = linguist.getAppDefaultLocale();
                translationConfig.packageName = getPackageName();
                translationConfig.resources = linguist.fetch(locale);
                translationConfig.defaultLanguage = Language.fromCode(locale.getLanguage());
                translationConfig.desiredLanguage = Language.fromCode(linguist.getDeviceDefaultLocale().getLanguage());
                LL.d("Got " + translationConfig.resources.size() + ": " + translationConfig.defaultLanguage);
            }
            return translationConfig;
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
