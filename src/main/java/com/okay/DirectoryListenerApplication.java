package com.okay;

import com.okay.manager.DirectoryManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * Listen specific directory and upload FTP server.
 * FTP server configuration load from application.yml file, listen directory is also in yml file.
 * <p>
 * Created by ahmet on 6.10.2017.
 */
@SpringBootApplication
@EnableScheduling
public class DirectoryListenerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DirectoryListenerApplication.class);

        DirectoryManager directoryManager = ctx.getBean(DirectoryManager.class);
        try {
            directoryManager.listenDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}