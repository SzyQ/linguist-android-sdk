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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.stringx.app.R;

public final class LinguistOverlayActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 3001;
    public static final String PACKAGE_NAME = "io.stringx";
    private static final int REQUEST_CODE_TRANSLATE = 3002;
    public static final String STRINGX_ACTIVITY = "io.stringx.MainActivity";
    public static final String KEY_PACKAGE = "KEY_PACKAGE";
    private BroadcastReceiver receiver;
    private boolean isWaitingForAppInstall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        setupTranslateButton();
        setupNeverTranslateButton();
        setupCloseButton();
        setupMessage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TRANSLATE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupMessage() {
        TextView message = findViewById(R.id.message);
        final Linguist linguist = Linguist.get(LinguistOverlayActivity.this);
        String defaultLanguage = linguist.getAppDefaultLocale().getDisplayLanguage();
        message.setText(getString(R.string.translate_message, defaultLanguage));
    }

    private void setupCloseButton() {
        View close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void setupNeverTranslateButton() {
        TextView neverTranslate = findViewById(R.id.never_translate);
        final Linguist linguist = Linguist.get(LinguistOverlayActivity.this);
        String deviceLanguage = linguist.getAppDefaultLocale().getDisplayLanguage();
        neverTranslate.setText(getString(R.string.never_translate, deviceLanguage));
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linguist.setNeverTranslate(true);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setupTranslateButton() {
        View translate = findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        label = getString(R.string.app);
                    }
                    new AlertDialog.Builder(LinguistOverlayActivity.this)
                            .setTitle(R.string.dialog_title)
                            .setMessage(label + " " + getString(R.string.dialog_message))
                            .setPositiveButton(R.string.dialog_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    registerPackageChangedReceiver();
                                    Utils.openStore(getApplicationContext(), PACKAGE_NAME);
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                LL.d("Package added: "+intent.getData());
                Uri data = intent.getData();
                if (data != null && PACKAGE_NAME.equals(data.getSchemeSpecificPart())) {
                    Intent stringxIntent = new Intent();
                    stringxIntent.setComponent(new ComponentName(PACKAGE_NAME, STRINGX_ACTIVITY));
                    stringxIntent.putExtra(KEY_PACKAGE, getPackageName());
                    try {
                        startActivityForResult(stringxIntent, REQUEST_CODE_TRANSLATE);
                    }catch (Exception ignored){
                        LL.e("Failed to start stringx!",ignored);
                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        ActivityManager service = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//                        for (ActivityManager.AppTask appTask : service.getAppTasks()) {
//                            appTask.moveToFront();
//                            break;
//                        }
                        unregisterReceiver();
//                    }
                }
            }
        };
        registerReceiver(receiver, intentFilter);
        isWaitingForAppInstall = true;
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
