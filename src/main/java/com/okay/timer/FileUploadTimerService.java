package com.okay.timer;

import com.okay.config.ServerConfiguration;
import com.okay.exception.DirectoryException;
import com.okay.manager.FileManager;
import com.okay.util.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ahmet on 6.10.2017.
 */
@Component
public class FileUploadTimerService {

    private static final long ONE_MINUTE = 60 * 1000;

    private Logger logger = LoggerFactory.getLogger(FileUploadTimerService.class);

    @Autowired
    private ServerConfiguration serverConfiguration;

    @Autowired
    private FileManager fileManager;

    @Scheduled(cron = "*/30 * * * * *")
    public void timeTick() {
        logger.info("FileUploadTimerService started.");

        ConcurrentHashMap<String, Long> pathMap = fileManager.getPathMap();
        Iterator<String> iterator = pathMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();

            Long lastModifiedTime = pathMap.get(key);

            long difference = System.currentTimeMillis() - ONE_MINUTE;
            if (lastModifiedTime < difference) {
                File file = new File(key);

                try {
                    // upload file
                    UploadUtils.uploadFile(serverConfiguration, file);

                    // delete from map
                    fileManager.removeFile(file);
                } catch (DirectoryException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}