# WXLog
**This is an Android log library witch is based on Logan and Timber.**


**LogPrinter contains two tree plant, DebugTree and ReleaseTree. DebugTree writes log to console of android studio, but ReleaseTree encrypts the log and writes it to the local disk after compression.**

# Getting Started(Just Android)

### Gradle 

Add following content to your root project `build.gradle` file:

```groovy
allprojects {
    repositories {
        maven { url "https://wangxue1214.bintray.com/WXLog" }
    }
}
```

then add following content in the sub-project `build.gradle` file:

```groovy
implementation 'com.wangxue.log_printer:log_printer:1.0.0'
```

### Usage

Initialization settings must be made before you use it, it is recommended in Application, for example:

```java
LogConfig.init(this, BuildConfig.Debug, null, null, false);
```

Print log:
```java
LogPrinter.e("TAG", "print log");
```
or
```java
LogPrinter.e("TAG", "this is number %d", i);
```

Parse log:
```java
new DecryptExecutor("your password", inputStream, outputStream).parseLog();
```

Upload log:
```java
new RealSendLogRunnable.SendLogBuilder()
                .setHeaders(headers)
                .setParameters(parameters)
                .setSSLSocketFactory(sslSocketFactory)
                .setMethod(RealSendLogRunnable.SendLogBuilder.POST)
                .setUrl(url)
                .build()
                .doIT();
```                
