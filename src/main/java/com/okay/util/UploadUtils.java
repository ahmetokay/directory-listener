package com.okay.util;

import com.jcraft.jsch.*;
import com.okay.config.ServerConfiguration;
import com.okay.exception.DirectoryException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ahmet on 6.10.2017.
 */
public class UploadUtils {

    private static Logger logger = LoggerFactory.getLogger(UploadUtils.class);

    public static void uploadFile(ServerConfiguration configuration, File file) throws DirectoryException {
        FileInputStream fileInputStream = null;
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            logger.info("FTP connection is opening.");
            JSch jsch = new JSch();
            session = jsch.getSession(configuration.getUsername(), configuration.getIp(), 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "password");
            session.setPassword(configuration.getPassword());
            session.connect(5000);
            Channel channel = session.openChannel("sftp");
            channelSftp = (ChannelSftp) channel;
            channelSftp.connect(5000);

            logger.info("FTP connection is opened.");

            fileInputStream = new FileInputStream(file);

            long startTime = System.nanoTime();
            logger.info("File upload is starting. file:" + file.getName());
            channelSftp.put(fileInputStream, file.getName(), ChannelSftp.OVERWRITE);
            logger.info("File upload is finished. time:" + ((System.nanoTime() - startTime) / 1000000));
        } catch (JSchException e) {
            logger.error("JSchException error occurred.", e);
            throw new DirectoryException(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException error occurred.", e);
            throw new DirectoryException(e.getMessage());
        } catch (SftpException e) {
            logger.error("SftpException error occurred.", e);
            throw new DirectoryException(e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (session != null) {
                session.disconnect();
            }
            IOUtils.closeQuietly(fileInputStream);
        }
    }
}