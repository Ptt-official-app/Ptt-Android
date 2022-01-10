# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,InnerClasses,Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes AnnotationDefault
-keepattributes *Annotation*

-dontskipnonpubliclibraryclasses
-forceprocessing
-optimizationpasses 5
-dontpreverify
-allowaccessmodification

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep public class androidx.** { *; }
# support library stuff
-keep public class androidx.preference.** { *; }
-keep public class androidx.appcompat.app.** { *; }

-keep public class org.jsoup.**
-keep class com.google.gson.**

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# OkHttp


-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }
-dontwarn okio.**
-dontwarn okhttp3.**

-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**
-dontwarn rx.**

-keep class com.fasterxml.jackson.** { *;}
-dontwarn com.fasterxml.jackson.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform


-keep @interface android.support.annotation.Keep

-dontwarn java.lang.annotation.Annotation

-keep public class java.lang.annotation.Annotation { *;}
-dontwarn java.lang.annotation.**

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# -------------Fresco-----------------
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
# -------------Fresco-----------------
-keep class cc.ptt.android.data.model.remote.** { *; }