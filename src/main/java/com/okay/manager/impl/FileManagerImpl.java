package com.okay.manager.impl;

import com.okay.manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ahmet on 6.10.2017.
 */
@Component
@Scope("singleton")
public class FileManagerImpl implements FileManager {

    private Logger logger = LoggerFactory.getLogger(FileManagerImpl.class);

    private ConcurrentHashMap<String, Long> pathMap = new ConcurrentHashMap<>();

    @Override
    public void addFile(File file) {
        String filePath = file.getPath();

        if (pathMap.containsKey(filePath)) {
            // exist
            pathMap.remove(filePath);
            pathMap.put(filePath, file.lastModified());
        } else {
            // not exist
            pathMap.put(filePath, file.lastModified());
        }
    }

    @Override
    public ConcurrentHashMap<String, Long> getPathMap() {
        return pathMap;
    }

    @Override
    public void removeFile(File file) {
        pathMap.remove(file.getPath());

        // delete from file system
        file.delete();
    }
}