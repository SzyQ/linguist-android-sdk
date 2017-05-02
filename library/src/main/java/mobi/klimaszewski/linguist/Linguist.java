package mobi.klimaszewski.linguist;

public class Linguist {
    private static final String LINGUIST = "Linguist ";
    private int pointer = 0;

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
        return new StringBuffer(translate(text.toString()));
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
}
