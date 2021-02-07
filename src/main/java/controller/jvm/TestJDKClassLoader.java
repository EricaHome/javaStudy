package controller.jvm;

import sun.misc.Launcher;

import java.net.URL;

/**
 * @author Erica
 * @date 2021/2/7 22:16
 * @description TODO
 */
public class TestJDKClassLoader {

    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader()); // null
        //System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader());
        System.out.println(TestJDKClassLoader.class.getClassLoader()); // jdk.internal.loader.ClassLoaders$AppClassLoader@726f3b58
        System.out.println("---------------------------------------------");
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassLoader = appClassLoader.getParent();
        ClassLoader bootstrapLoader = extClassLoader.getParent();
        System.out.println("the bootstrapLoader : " + bootstrapLoader); // null
        System.out.println("the extClassloader : " + extClassLoader); // jdk.internal.loader.ClassLoaders$PlatformClassLoader@e73f9ac
        System.out.println("the appClassLoader : " + appClassLoader); // jdk.internal.loader.ClassLoaders$AppClassLoader@726f3b58
        System.out.println("---------------------------------------------");
        System.out.println("bootstrapLoader加载以下文件：");
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i]);
        }
        System.out.println();
        System.out.println("extClassloader加载以下文件：");
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println();
        System.out.println("appClassLoader加载以下文件：");
        System.out.println(System.getProperty("java.class.path"));
        System.out.println("---------------------------------------------");
    }

}
