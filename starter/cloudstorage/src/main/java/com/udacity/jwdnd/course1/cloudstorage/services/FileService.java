package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addFile(File newFile) {
        fileMapper.addFile(newFile);
    }

    public File[] getFileListByUserId(Integer userId) {
        File[] fileArr = fileMapper.getFileListByUserId(userId);

        return fileArr;
    }
}
