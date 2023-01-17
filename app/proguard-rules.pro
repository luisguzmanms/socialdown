# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
-keep class com.huxq17.download.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keep public class * implements com.ixuea.android.downloader.db.DownloadDBController

-ignorewarnings


#amplitude
-keep class com.google.android.gms.ads.** { *; }
    -dontwarn okio.**

-keep class com.google.ads.** # Don't proguard AdMob classes
-dontwarn com.google.ads.** # Temporary workaround for v6.2.1. It gives a warning that you can ignore

-keep class com.google.unity.** { *;}
-keep class com.google.android.gms.ads.** {  *;}
-keep class com.google.ads.mediation.admob.AdMobAdapter {  *;}

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**


-dontwarn com.google.auto.value.AutoValue
-dontwarn com.google.auto.value.AutoValue$Builder


##  ________________________________________________________________________________________________________________

-dontwarn org.**
-dontwarn com.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn sun.**



##  ________________________________________________________________________________________________________________

-keep class android.** { *; }
-keep class org.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class sun.** { *; }
-keep class de.mindpipe.** { *; }
-keep class com.j256.** { *; }




    ##  ________________________________________________________________________________________________________________
    ## Preserve line numbers in the obfuscated stack traces.
    -keepattributes LineNumberTable
    -keepattributes SourceFile


    -keepattributes Signature
    -keepattributes *Annotation*
    -keepattributes InnerClasses,EnclosingMethod


    # Basic ProGuard rules for Firebase Android SDK 2.0.0+
    -keep class com.firebase.** { *; }

    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }

    -keep class com.firebase.** { *; }
    -keepnames class com.fasterxml.jackson.** { *; }
    -keepnames class javax.servlet.** { *; }
    -keepnames class org.ietf.jgss.** { *; }
    -dontwarn org.w3c.dom.**
    -dontwarn org.joda.time.**
    -dontwarn org.shaded.apache.**
    -dontwarn org.ietf.jgss.**
    -dontwarn com.firebase.**
    -dontnote com.firebase.client.core.GaePlatform


##  ________________________________________________________________________________________________________________

-dontwarn org.**
-dontwarn com.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn sun.**



##  ________________________________________________________________________________________________________________

-keep class android.** { *; }
-keep class org.** { *; }
-keep class java.** { *; }
-keep class javax.** { *; }
-keep class sun.** { *; }
-keep class de.mindpipe.** { *; }
-keep class com.j256.** { *; }


##  ________________________________________________________________________________________________________________
## Preserve line numbers in the obfuscated stack traces.
-keepattributes LineNumberTable
-keepattributes SourceFile

-dontwarn com.google.**



## solucion porblema serializado de model en firefase
-keep class com.google.firebase.example.fireeats.model.** { *; }

-keepclassmembers class com.lamesa.socialdown.model** { *; }


-keep public class * extends android.view.View{*;}

-ignorewarnings
-dontoptimize
-dontobfuscate
-dontskipnonpubliclibraryclasses

-ignorewarnings

-keep class com.samsung.** { *; }
-dontwarn com.samsung.**
-dontwarn com.samsung.multiscreen.BuildConfig
-dontwarn lombok.**

-keep class com.kongzue.dialogx.** { *; }
-dontwarn com.kongzue.dialogx.**

# 额外的，建议将 android.view 也列入 keep 范围：
-keep class android.view.** { *; }

# 若启用模糊效果，请增加如下配置：
-dontwarn androidx.renderscript.**
-keep public class androidx.renderscript.** { *; }