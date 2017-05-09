package mobi.klimaszewski.services;

import java.util.List;

interface LinguistServicesRemoteInterface {
    List<String> translate(String packageName, String originalCode, String desiredCode,in List<String> text);
}
