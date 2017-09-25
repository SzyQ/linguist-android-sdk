package io.stringx;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.stringx.client.R;

class StringXActivityBase extends AppCompatActivity {


    public static final int REQUEST_CODE = 3001;
    public static final String PACKAGE_NAME = "io.stringx";
    public static final String STRINGX_ACTIVITY = "io.stringx.MainActivity";
    public static final String KEY_PACKAGE = "KEY_PACKAGE";
    private static final int REQUEST_CODE_TRANSLATE = 3002;
    private BroadcastReceiver receiver;

    protected void startStringXService() {
        final StringX stringX = StringX.get(this);
        stringX.setEnabled(true);
        stringX.setEnabled(stringX.getDeviceLanguage(), true);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_NAME, STRINGX_ACTIVITY));
        intent.putExtra(KEY_PACKAGE, getPackageName());
        try {
            startActivityForResult(intent, REQUEST_CODE_TRANSLATE);
        } catch (ActivityNotFoundException e) {
            CharSequence label;
            try {
                label = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.loadLabel(getPackageManager());
            } catch (PackageManager.NameNotFoundException e1) {
                label = getString(R.string.sX_app);
            }
            new AlertDialog.Builder(this)
                    .setTitle(R.string.sX_dialog_title)
                    .setMessage(label + " " + getString(R.string.sX_dialog_message))
                    .setPositiveButton(R.string.sX_dialog_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            registerPackageChangedReceiver();
                            Utils.openStore(getApplicationContext(), PACKAGE_NAME);
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TRANSLATE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
                StringX.get(this).invalidate();
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(RestartBroadcast.ACTION));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerPackageChangedReceiver() {
        if (receiver != null) {
            return;
        }
        LL.d("Registering package listener");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LL.d("Package added: " + intent.getData());
                Uri data = intent.getData();
                if (data != null && PACKAGE_NAME.equals(data.getSchemeSpecificPart())) {
                    Intent stringxIntent = new Intent();
                    stringxIntent.setComponent(new ComponentName(PACKAGE_NAME, STRINGX_ACTIVITY));
                    stringxIntent.putExtra(KEY_PACKAGE, getPackageName());
                    try {
                        startActivityForResult(stringxIntent, REQUEST_CODE_TRANSLATE);
                    } catch (Exception ignored) {
                        LL.e("Failed to start stringx!", ignored);
                    }
                    unregisterReceiver();
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private void unregisterReceiver() {
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception ignored) {

            }
        }
    }
}
