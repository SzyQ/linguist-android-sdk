package io.stringx;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import io.stringx.sdk.BuildConfig;

/**
 * Entry point to an app from <a href="https://play.google.com/store/apps/details?id=io.stringx">stringX App</a>
 * Fetches resources and pass over to translating app
 */
public final class ClientService extends Service {

    private static final int ERROR_UNSUPPORTED_LANGUAGE = 0;
    private static final int ERROR_GENERAL = 1;

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
    };

    private static String writeClientInfo(Context context, List<String> supportedLanguageCodes, Options options) throws IOException, UnsupportedLanguageException {
        StringWriter out = new StringWriter(1024);
        JsonWriter writer = new JsonWriter(out);
        writer.beginObject();
        {
            writer.name("packageName").value(context.getPackageName());
            writer.name("defaultLanguageCode").value(options.getDefaultLanguage().getCode());
            writer.name("deviceLanguageCode").value(StringX.get(context).getDeviceLanguage().getCode());
            {
                writer.name("supportedLanguages");
                writer.beginArray();
                for (String supportedLanguageCode : supportedLanguageCodes) {
                    writer.value(supportedLanguageCode);
                }
                writer.endArray();
            }
            writer.name("version").value(BuildConfig.VERSION_CODE);
            writer.name("debug").value(BuildConfig.DEBUG);
        }
        writer.endObject();
        writer.close();
        return out.toString();
    }

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
                try {
                    SXLog.d("Retrieving config");
                    callback.onStarted();
                    StringX stringX = StringX.get(context);
                    if (stringX != null) {
                        List<Language> supportedLanguages = stringX.getSupportedLanguages();
                        List<String> supportedLanguageCodes = new ArrayList<>();
                        for (Language language : supportedLanguages) {
                            supportedLanguageCodes.add(language.getCode());
                        }

                        Options options = stringX.getOptions();
                        String infoJson = writeClientInfo(context, supportedLanguageCodes, options);
                        callback.onBasicInfoReceived(infoJson);

                        List<String> mainStrings = new ArrayList<>();
                        List<String> mainStringNames = new ArrayList<>();
                        List<Integer> mainStringIds = new ArrayList<>();
                        ResourceProvider provider = new ResourceProvider(context, callback);
                        provider.fetchDefaultStrings(mainStrings, mainStringNames, mainStringIds);
                        provider.fetchStringIdentifiers(mainStrings);
                        callback.onFinished();
                        SXLog.d("Config sent");
                    } else {
                        callback.onFinished();
                    }
                } catch (UnsupportedLanguageException e) {
                    callback.onError(ERROR_UNSUPPORTED_LANGUAGE);
                } catch (IOException e) {
                    callback.onError(ERROR_GENERAL);
                }
            } catch (Exception e) {
                SXLog.e("Failed to get config", e);
            }
            return null;
        }
    }

}
