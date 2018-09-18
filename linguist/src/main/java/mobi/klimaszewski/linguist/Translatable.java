package mobi.klimaszewski.linguist;

/**
 * {@link android.app.Application} needs to implement this interface to allow {@link Linguist} access across the app and proper configuration
 *
 * @see <a href="https://linguist.klimaszewski.mobi/docs/guides">Guides</a>
 */
public interface Translatable {

    /**
     * Provides {@link Linguist instance}
     */
    Linguist getLinguist();
}
