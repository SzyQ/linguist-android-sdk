package mobi.klimaszewski.linguist;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class LinguistOverlayActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 3001;
    private static final int REQUEST_CODE_TRANSLATE = 3002;
    private View close;
    private View neverTranslate;
    private View translate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        close = findViewById(R.id.close);
        neverTranslate = findViewById(R.id.never_translate);
        translate = findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = "mobi.klimaszewski.linguist.services";
                Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
                intent.putExtra("TEST","Dupa");
                try {
                    startActivityForResult(intent, REQUEST_CODE_TRANSLATE);
                } catch (ActivityNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }
        });
        neverTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linguist.getInstance().setNeverTranslate(true);
                setResult(RESULT_OK);
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_TRANSLATE){
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }else{
                setResult(RESULT_CANCELED);
                finish();
                Toast.makeText(this,"Failed to translate",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
