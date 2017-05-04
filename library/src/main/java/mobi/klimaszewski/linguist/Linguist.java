package mobi.klimaszewski.linguist;

import android.content.Context;

import java.util.Set;

public class Linguist {
    private static final String LINGUIST = "Linguist ";
    private Set<Integer> resources;

    public Linguist(Set<Integer> resources){
        this.resources = resources;
    }

    public Linguist(Set<Integer> appStringResources, Set<String> stringsToTranslate) {

    }

    public String translate(String string) {
        int length = string.length();
        int linguistLength = LINGUIST.length();
        int wordCount = length / linguistLength;
        String result = "";
        for (int i = 0; i < wordCount; i++) {
            result += LINGUIST;
        }
        result += LINGUIST.substring(0, length % linguistLength);
        return result;
    }

    public CharSequence translate(CharSequence text) {
        return text != null ? new StringBuffer(translate(text.toString())): text;
    }

    public CharSequence[] translate(CharSequence[] textArray) {
        CharSequence[] charSequences = new CharSequence[textArray.length];
        for(int i= 0; i < textArray.length; i++){
            charSequences[i]=translate(textArray[i]);
        }
        return charSequences;
    }

    public String[] translate(String[] textArray) {
        String[] charSequences = new String[textArray.length];
        for(int i= 0; i < textArray.length; i++){
            charSequences[i]=translate(textArray[i]);
        }
        return charSequences;
    }

    public CharSequence translate(int id, CharSequence text) {
        if(resources.contains(id)){
            return translate(text);
        }
        return text;
    }

    public String translate(int id, String text) {
        if(resources.contains(id)){
            return translate(text);
        }
        return text;
    }

    public CharSequence[] translate(int id, CharSequence[] textArray) {
        if(resources.contains(id)){
            return translate(textArray);
        }
        return textArray;
    }

    public String[] translate(int id, String[] textArray) {
        if(resources.contains(id)){
            return translate(textArray);
        }
        return textArray;
    }

    public static class Builder {

        private Context context;
        private Class[] stringClasses;
        private boolean isPrefetched;

        public Builder(Context context, Class... stringClasses){
            this.context = context;
            this.stringClasses = stringClasses;
        }

        public Builder setPrefetch(boolean isPrefetched){
            this.isPrefetched = isPrefetched;
            return this;
        }

        public Linguist build(){
            Set<Integer> appStringResources = Utils.getAppStringResources(context, stringClasses);
            if(isPrefetched){
                return new Linguist(appStringResources,Utils.getAppStrings(context,appStringResources));
            }
            return new Linguist(appStringResources);
        }
    }
}
