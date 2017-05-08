package mobi.klimaszewski.services;


interface LinguistServicesRemoteInterface {
    String translate(String packageName, String originalCode, String desiredCode, String text);
}
