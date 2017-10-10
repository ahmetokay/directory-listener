package com.okay.manager.impl;

import com.okay.config.DirectoryConfiguration;
import com.okay.manager.DirectoryManager;
import com.okay.manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ahmet on 6.10.2017.
 */
@Component
public class DirectoryManagerImpl implements DirectoryManager {

    private Logger logger = LoggerFactory.getLogger(DirectoryManagerImpl.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private DirectoryConfiguration configuration;

    @Autowired
    private FileManager fileManager;

    public void listenDirectory() throws IOException, InterruptedException {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path listenPath = Paths.get(configuration.getDirectory());
            listenPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey key = watchService.take();
            do {
                if (key != null) {
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        Path name = (Path) event.context();
                        Path absoluteFilePath = Paths.get(listenPath.toString(), name.toString());

                        executorService.submit(() -> fileManager.addFile(absoluteFilePath.toFile()));
                    }
                }
            } while (key.reset());
        }
    }
}