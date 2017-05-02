package mobi.klimaszewski.linguist.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import mobi.klimaszewski.linguist.LinguistActivity;
import mobi.klimaszewski.linguist.R;

public class DemoActivity extends LinguistActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        View view = findViewById(R.id.codeText);
        ((TextView) view).setText(getString(R.string.text_code));
    }
}