package com.okay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ahmet on 6.10.2017.
 */
@Configuration
@ConfigurationProperties(prefix = "local")
public class DirectoryConfiguration {

    private String directory;

    private int threadSize;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getThreadSize() {
        return threadSize;
    }

    public void setThreadSize(int threadSize) {
        this.threadSize = threadSize;
    }
}