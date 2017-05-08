/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package mobi.klimaszewski.linguist.services.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "api",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.services.linguist.klimaszewski.mobi",
                ownerName = "backend.services.linguist.klimaszewski.mobi",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "translate")
    public Translation translate(@Named("text") String text) {
        Translation response = new Translation();
        response.setOriginalText(text);
//        Translate translate = TranslateOptions
//                .getDefaultInstance()
//                .getService();
//        response.setTranslatedText(translate.translate(text, Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage("pl")).getTranslatedText());
//        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//        Key key = datastore.newKeyFactory().setKind("Translation").newKey(name);
//        Entity entity = datastore.get(key);
//        Value<Text> original = entity.getValue("original");
//        response.setData(original.get().getValue());
        return response;
    }

}
