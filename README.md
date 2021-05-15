## Static StackOverflow Analysis for Android

Stackwall is a static tool that takes your `StackOverflowError` and APK and finds out which parts of your application
are using the most stack space.

### Getting Started

You will need a Java11 or higher JVM to run Stackwall. Stackwall currently has no releases, so you'll need to build it
yourself. This situation will hopefully improve soon.

1. Clone the repository
1. `./gradlew assemble`
1. Unpack the archive file in `build/distributions`
1. Run `./bin/stackwall <application.apk> <stackoverflow.txt>`

StackOverflow files are simply plain text files containing the stack-trace of the `StackOverflowError` your are looking
to analise. This is most typically in the standard Java format:

```log
java.lang.StackOverflowError
    at android.view.View.getDrawableState(View.java:11556)
    at android.widget.TextView.onDraw(TextView.java:4863)
    at android.view.View.draw(View.java:10983)
    at android.view.View.getDisplayList(View.java:10422)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.draw(View.java:10986)
    at android.view.View.getDisplayList(View.java:10422)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.getDisplayList(View.java:10420)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.getDisplayList(View.java:10420)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.getDisplayList(View.java:10420)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.draw(View.java:10986)
    at android.widget.FrameLayout.draw(FrameLayout.java:450)
    at android.widget.ScrollView.draw(ScrollView.java:1524)
    at android.view.View.getDisplayList(View.java:10422)
    at android.view.ViewGroup.drawChild(ViewGroup.java:2850)
    at android.view.ViewGroup.dispatchDraw(ViewGroup.java:2489)
    at android.view.View.draw(View.java:10986)
    at android.widget.FrameLayout.draw(FrameLayout.java:450)
    at android.view.View.getDisplayList(View.java:10422)
```

### Other StackOverflow Formats

Stackwall should be able to read most `StackOverflowError` traces (plain, ADB logs, etc), if you find a variant that
isn't understood, please open a Pull Request or log an Issue and be sure to include a test and full example.

## TODO
Stackwall is still in a very early phase of development, these are major outstanding issues that need to be addressed:

1. build / test / release process
1. an index of platform methods to count against
1. verify and improve the accuracy of the output (currently it's a best-guess)
1. command-line options
1. more test coverage