package io.stringx;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ClientService extends Service {

    private FetchTask task;
    private final IBinder binder = new TranslationInterface.Stub() {

        @Override
        public void getConfig(final ConfigCallback callback) throws RemoteException {
            if (task != null) {
                task.cancel(true);
            }
            task = new FetchTask();
            task.execute(callback, getApplicationContext());
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

    private static class FetchTask extends AsyncTask<Object, Integer, Void> {

        @Override
        protected Void doInBackground(Object... configCallbacks) {
            ConfigCallback callback = (ConfigCallback) configCallbacks[0];
            Context context = (Context) configCallbacks[1];
            try {
                LL.d("Retrieving config");
                callback.onStarted();
                StringX stringX = StringX.get(context);
                if (stringX != null) {
                    List<Language> supportedLanguages = stringX.getSupportedLanguages();
                    List<String> supportedLanguageCodes = new ArrayList<>();
                    for (Language language : supportedLanguages) {
                        supportedLanguageCodes.add(language.getCode());
                    }

                    Options options = stringX.getOptions();
                    callback.onBasicInfoReceived(
                            context.getPackageName(),
                            options.getMode().name(),
                            options.getDefaultLanguage().getCode(),
                            stringX.getDeviceLanguage().getCode(),
                            supportedLanguageCodes);

                    List<String> mainStrings = new ArrayList<>();
                    List<String> mainStringNames = new ArrayList<>();
                    List<Integer> mainStringIds = new ArrayList<>();
                    ResourceProvider provider = ResourceProvider.newResourceProvider(context, callback);
                    provider.fetchDefaultStrings(mainStrings, mainStringNames, mainStringIds);
                    provider.fetchStringIdentifiers(mainStrings);
                    callback.onFinished();
                    LL.d("Config sent");
                } else {
                    callback.onFinished();
                }
            } catch (RemoteException | UnsupportedLanguageException e) {
                LL.e("Failed to get config", e);
            }
            return null;
        }
    }

}
