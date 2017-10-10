package com.okay.timer;

import com.okay.config.DirectoryConfiguration;
import com.okay.manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Created by ahmet on 6.10.2017.
 */
@Component
public class DirectoryListenerTimerService {

    private Logger logger = LoggerFactory.getLogger(DirectoryListenerTimerService.class);

    @Autowired
    private DirectoryConfiguration configuration;

    @Autowired
    private FileManager fileManager;

    @Scheduled(cron = "0 0/15 * * * ?")
    public void timeTick() {
        logger.info("DirectoryListenerTimerService started.");

        String directory = configuration.getDirectory();
        Path directoryPath = Paths.get(directory);
        File[] files = directoryPath.toFile().listFiles();

        for (File file : files) {
            fileManager.addFile(file);
        }
    }
}