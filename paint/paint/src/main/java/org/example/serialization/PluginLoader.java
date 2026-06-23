package org.example.serialization;

import org.example.factories.ShapeFactory;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginLoader {

    public static List<ShapeFactory> loadPlugins(String directory) {
        List<ShapeFactory> loadedFactories = new ArrayList<>();
        File loc = new File(directory);
        if (!loc.exists()) loc.mkdir();

        File[] flist = loc.listFiles((dir, name) -> name.endsWith(".jar"));
        if (flist == null || flist.length == 0) return loadedFactories;

        try {
            URL[] urls = new URL[flist.length];
            for (int i = 0; i < flist.length; i++) {
                urls[i] = flist[i].toURI().toURL();
            }

            URLClassLoader ucl = new URLClassLoader(urls, ShapeFactory.class.getClassLoader());

            ServiceLoader<ShapeFactory> sl = ServiceLoader.load(ShapeFactory.class, ucl);
            for (ShapeFactory factory : sl) {
                loadedFactories.add(factory);
            }
        } catch (Exception e) {
            System.err.println("Failed to load shape plugins from directory: " + directory);
            e.printStackTrace();
        }

        return loadedFactories;
    }

    public static List<DataProcessor> loadDataPlugins(String directory) {
        List<DataProcessor> loadedProcessors = new ArrayList<>();
        File loc = new File(directory);
        if (!loc.exists()) loc.mkdir();

        File[] flist = loc.listFiles((dir, name) -> name.endsWith(".jar"));
        if (flist == null || flist.length == 0) return loadedProcessors;

        try {
            URL[] urls = new URL[flist.length];
            for (int i = 0; i < flist.length; i++) {
                urls[i] = flist[i].toURI().toURL();
            }
            URLClassLoader ucl = new URLClassLoader(urls, DataProcessor.class.getClassLoader());
            ServiceLoader<DataProcessor> sl = ServiceLoader.load(DataProcessor.class, ucl);
            for (DataProcessor processor : sl) {
                loadedProcessors.add(processor);
            }
        } catch (Exception e) {
            System.err.println("Failed to load data processor plugins from directory: " + directory);
            e.printStackTrace();
        }

        return loadedProcessors;
    }
}