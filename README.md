What is stringX
-------------

stringX is a single integrated platform delivering automated solution to translate your app.
This native Android SDK will enable your app to use all of these features.

Getting started
---------------
1. Implement Translatable interface in your custom Application class
```java
public class App extends Application implements Translatable {

    private StringX stringX;

    @Override
    public StringX getStringX() {
        return stringX;
    }
}
```
2. Configure stringX
```java
    @Override
    public void onCreate() {
        super.onCreate();
        Options options = new Options.Builder(this, Language.English)
                .setSupportedLanguages(Language.Polish)
                .addStrings(R.string.class)
                .excludeString(R.string.app_name)
                .excludeStrings(android.support.v7.appcompat.R.string.class)
                .build();
        stringx = new StringX(options);
    }
```

Requirements
------------
### Android 16+

How to run the demo
-------------------
Import the DeveloperSample project in the samples folder into your IDE. Run as a normal Android application on your device or emulator.
Install and Run the stringX service application: https://play.google.com/store/apps/details?id=io.stringx


Contributing
------------
We would love to see your contributions! Follow these steps:

1. Fork this repository.
2. Create a branch (`git checkout -b my_awesome_feature`)
3. Commit your changes (`git commit -m "Awesome feature"`)
4. Push to the branch (`git push origin my_awesome_feature`)
5. Open a Pull Request.

License
-------
© Copyright Szymon Klimaszewski or licensors. Distributed under the [Apache 2.0 License](LICENSE).  