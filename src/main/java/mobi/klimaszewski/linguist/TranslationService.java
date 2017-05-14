package mobi.klimaszewski.linguist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Map;

import mobi.klimaszewski.services.TranslationInterface;

public class TranslationService extends Service {

    private final IBinder binder = new TranslationInterface.Stub() {

        @Override
        public List<String> onStringsRequested() throws RemoteException {
            LL.d("Requested strings");
            return Linguist.getInstance().fetch();
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
