# WXLog
**This is an Android log library witch is based on Logan and Timber.**


**LogPrinter contains two tree plant, DebugTree and ReleaseTree. DebugTree writes log to console of android studio, but ReleaseTree encrypts the log and writes it to the local disk after compression.**

# Getting Started(Just Android)

### Gradle 

Add following content to your root project `build.gradle` file:

```groovy
allprojects {
    repositories {
        maven { url "https://wangxue1214.bintray.com/AndroidLibrary" }
    }
}
```

then add following content in the sub-project `build.gradle` file:

```groovy
implementation 'com.wangxue.log_printer:log_printer:1.0.1'
```

### Usage

**Initialization settings must be made before you use it, it is recommended in Application, for example:**

```java
LogConfig.init(this, BuildConfig.Debug, "abcdefghijkmlnop", "0123456789012345");
```

**Print log:**
```java
LogPrinter.e("TAG", "print log");
```
or
```java
LogPrinter.e("TAG", "this is number %d", i);
```

**Parse log:**
```java
new DecryptExecutor(inputStream, outputStream).parseLog();
```

**Upload log:**
```java
String url = "http://192.168.119.63:8080/test/logupload";//换成自己的url
HashMap<String, String> headers = new HashMap<>();
headers.put("Content-Type", "application/octet-stream"); //二进制流
headers.put("client", "android");
new SendLogBuilder()
        .setHeaders(headers)
        .setMethod(SendLogBuilder.POST)
        .setUrl(url)
        .build()
        .doIT();
```

### Note

**See sample for more details**

### License

WXLog is licensed under the Apache License 2.0 - see the LICENSE file for details.
