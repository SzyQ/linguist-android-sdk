package mobi.klimaszewski.linguist;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuInflater;

public class LinguistMenuInflater extends MenuInflater {

    public static MenuInflater wrap(Activity activity, MenuInflater menuInflater) {
        return new LinguistMenuInflater(activity,menuInflater);
    }

    private final Linguist linguist;

    private MenuInflater inflater;

    public LinguistMenuInflater(Context context, MenuInflater inflater) {
        super(context);
        this.inflater = inflater;
        linguist = Linguist.getInstance();
    }

    @Override
    public void inflate(@MenuRes int menuRes, Menu menu) {
        inflater.inflate(menuRes, menu);
        linguist.translate(menu);
    }
}
