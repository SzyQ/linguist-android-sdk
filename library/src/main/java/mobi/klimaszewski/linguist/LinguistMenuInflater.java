package mobi.klimaszewski.linguist;


import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LinguistMenuInflater extends MenuInflater {

    private MenuInflater inflater;
    private final Linguist linguist;

    public LinguistMenuInflater(Context context, MenuInflater inflater) {
        super(context);
        this.inflater = inflater;
        linguist = LinguistApp.getLinguist(context);
    }

    @Override
    public void inflate(@MenuRes int menuRes, Menu menu) {
        inflater.inflate(menuRes, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setTitle(linguist.translate(item.getTitle()));
            item.setTitleCondensed(linguist.translate(item.getTitleCondensed()));
        }
    }
}
