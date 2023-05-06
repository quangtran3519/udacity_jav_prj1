package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Service
public class FileService {

    public final Logger logger = LoggerFactory.getLogger(HashService.class);
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesByUserId(Integer userId) {
        logger.info(" start get Files by user id {}", userId);
        return fileMapper.getFilesByUserId(userId);

    }

    public File getFileById(Integer id) {
        logger.info(" start get File by id {}", id);
        return fileMapper.getFileById(id);
    }


    public void insertFile(MultipartFile fileUpload, Integer userId) throws IOException {
        logger.info(" start insert File {}", userId);

        File file = new File();
        file.setFileData(fileUpload.getBytes());
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        file.setContentType(fileUpload.getContentType());
        file.setUserId(userId);
        file.setFileName(fileUpload.getOriginalFilename());

        int result = fileMapper.insetFile(file);
        if (result == 0) {
            throw new RuntimeException("insert File is failed");
        }
    }

    public void deleteFile(Integer id) {
        logger.info(" start deleteFile  {}", id);
        int result = fileMapper.deleteFile(id);
        if (result == 0) {
            throw new RuntimeException("delete File is failed");
        }
    }

    public boolean checkFileExist(String originalFilename, Integer userId) {
        logger.info(" start check file isFileAvailable {}  {}", originalFilename, userId);


        File file = fileMapper.getFileByUserIdAndUserName(userId, originalFilename);

        return Objects.nonNull(file);
    }
}
