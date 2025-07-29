/*
package com.innocito.axcl.service;

import com.innocito.axcl.model.FileModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
    private final AwsService awsService;

    public FileModel uploadFile(final File file, String userId) throws IOException {
        if (file == null) {
            return null;
        }
        return awsService.uploadFile(file, userId);
    }

    public String getFileWithCustomExpiry(String fileName, Long expiryInMillis) {
        if (StringUtils.isNotBlank(fileName)) {
            return awsService.getFile(fileName, expiryInMillis);
        } else {
            return null;
        }
    }

    public List<FileModel> uploadFiles(final List<MultipartFile> multipartFiles, String userId) throws IOException {
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return null;
        }
        return awsService.uploadFiles(multipartFiles, userId);
    }

    public void deleteFiles(List<String> fileNames) {
        if (!CollectionUtils.isEmpty(fileNames)) {
            awsService.deleteMultipleObjects(fileNames);
        }
    }

    public String getFile(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            return awsService.getFile(fileName, null);
        } else {
            return null;
        }
    }
}*/
