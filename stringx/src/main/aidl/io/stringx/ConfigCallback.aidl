package io.stringx;

import java.util.List;

//There is 1MB limit for Parcel, so the data needs to be sent in chunks from client
interface ConfigCallback {

    void onStarted();

    void onBasicInfoReceived(String infoJson);

    void onDefaultStringsReceived(inout List<String> defaultStrings);

    void onDefaultStringNamesReceived(inout List<String> defaultStringNames);

    void onDefaultStringIdsReceived(inout int[] defaultStringIds);

    void onLanguageReceived(String languageCode, inout int[] stringIds);

    void onFinished();

    void onError(int code);

}
