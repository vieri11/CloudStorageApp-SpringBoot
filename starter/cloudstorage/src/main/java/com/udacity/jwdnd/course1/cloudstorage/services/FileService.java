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

    public File viewFile(Integer fileId) {
        File file = fileMapper.getFileByFileId(fileId);

        return file;
    }

    public void deleteFile(Integer fileId) {
        fileMapper.deleteFileById(fileId);
    }

    private String[] getFileNamesByUserId(Integer userId) {
        return fileMapper.getFileNamesByUserId(userId);
    }

    public Boolean isDup (String originalFileName, Integer userId) {
        for(String fileName : getFileNamesByUserId(userId)) {
            if(fileName.equals(originalFileName)) {
                return true;
            }
        }

        return false;
    }
}
