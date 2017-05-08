package mobi.klimaszewski.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

public class RemoteService extends Service {
    private static final String LINGUIST = "Linguist ";
    private final IBinder binder = new LinguistServicesRemoteInterface.Stub() {
        @Override
        public String translate(String packageName, String originalCode, String desiredCode, String text) throws RemoteException {
            int length = text.length();
            int linguistLength = LINGUIST.length();
            int wordCount = length / linguistLength;
            String result = "";
            for (int i = 0; i < wordCount; i++) {
                result += LINGUIST;
            }
            result += LINGUIST.substring(0, length % linguistLength);
            return result;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
