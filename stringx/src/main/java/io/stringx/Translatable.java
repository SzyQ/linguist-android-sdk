package io.stringx;

/**
 * {@link android.app.Application} needs to implement this interface to allow {@link StringX} access across the app and proper configuration
 *
 * @see <a href="https://www.stringx.io/docs/guides">Guides</a>
 */
public interface Translatable {

    /**
     * Provides {@link StringX instance}
     */
    StringX getStringX();
}
