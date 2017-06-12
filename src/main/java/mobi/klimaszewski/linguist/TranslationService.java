package mobi.klimaszewski.linguist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.Map;

import mobi.klimaszewski.services.TranslationConfig;
import mobi.klimaszewski.services.TranslationInterface;

public class TranslationService extends Service {

    private final IBinder binder = new TranslationInterface.Stub() {

        @Override
        public TranslationConfig getConfig() throws RemoteException {
            LL.d("Retrieving config");
            TranslationConfig translationConfig = new TranslationConfig();
            translationConfig.strings = Linguist.getInstance().fetch();
            translationConfig.originalCode = Linguist.getAppDefaultLocale().getLanguage();
            translationConfig.desiredCode = Linguist.getDeviceDefaultLocale().getLanguage();
            LL.d("Got "+translationConfig.strings.size()+": "+translationConfig.originalCode+"="+translationConfig.desiredCode);
            return translationConfig;
        }

        @Override
        public void onTranslationCompleted(Map translation) throws RemoteException {
            LL.d("Applying translation");
            Linguist.getInstance().applyTranslation(translation);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
