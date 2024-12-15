package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public String[] getListFilesByUser(Integer userId) {
        return fileMapper.getFileListings(userId);
    }

    public File getFile(String filename) {
        return fileMapper.getFile(filename);
    }

    public void deleteFileByUser(String filename, Integer userid) {
        fileMapper.deleteFileByUser(filename, userid);
    }

    public int insert(File file) {
        return fileMapper.insert(file);
    }
}
