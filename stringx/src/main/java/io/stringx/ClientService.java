package io.stringx;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ClientService extends Service {

    private final IBinder binder = new TranslationInterface.Stub() {

        @SuppressLint("StaticFieldLeak")
        @Override
        public void getConfig(final ConfigCallback callback) throws RemoteException {
            new AsyncTask<ConfigCallback, Integer, Void>() {
                @Override
                protected Void doInBackground(ConfigCallback... configCallbacks) {
                    try {
                        LL.d("Retrieving config");
                        callback.onStarted();
                        StringX stringX = StringX.get(getApplicationContext());
                        if (stringX != null) {
                            List<Language> supportedLanguages = stringX.getSupportedLanguages();
                            List<String> supportedLanguageCodes = new ArrayList<>();
                            for (Language language : supportedLanguages) {
                                supportedLanguageCodes.add(language.getCode());
                            }

                            Options options = stringX.getOptions();
                            callback.onBasicInfoReceived(
                                    getPackageName(),
                                    options.getMode().name(),
                                    options.getDefaultLanguage().getCode(),
                                    stringX.getDeviceLanguage().getCode(),
                                    supportedLanguageCodes);
                            stringX.fetch(callback);
                            callback.onFinished();
                            LL.d("Config sent");
                        } else {
                            callback.onFinished();
                        }
                    } catch (RemoteException e) {
                        LL.e("Failed to get config", e);
                    }
                    return null;
                }
            }.execute(callback);
        }

        @Override
        public void onTranslationCompleted(Map translation) throws RemoteException {
            LL.d("Applying translation");
            StringX stringX = StringX.get(getApplicationContext());
            if (stringX != null) {
                stringX.applyTranslation(translation);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
