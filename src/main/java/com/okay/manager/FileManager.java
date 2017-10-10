package com.okay.manager;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ahmet on 6.10.2017.
 */
public interface FileManager {

    void addFile(File file);

    ConcurrentHashMap<String, Long> getPathMap();

    void removeFile(File file);
}