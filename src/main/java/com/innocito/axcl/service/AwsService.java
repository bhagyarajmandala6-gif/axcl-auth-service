package com.innocito.axcl.service;

import com.innocito.axcl.config.AWSS3ConfigProperties;
import com.innocito.axcl.model.FileModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.innocito.axcl.util.ApplicationConstants.*;
import static com.innocito.axcl.util.MessageConstants.*;
import static com.innocito.axcl.util.PropertyNameConstants.SPRING_PROFILES_ACTIVE;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsService {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AWSS3ConfigProperties awss3ConfigProperties;
    @Value(SPRING_PROFILES_ACTIVE)
    private String activeProfile;

    public FileModel uploadFile(final File file, String userId) throws IOException {
        if (file == null) {
            return null;
        }

        List<String> uploadedFileNames = new ArrayList<>();
        FileModel fileModel = new FileModel();
        try {
            String hexString = ObjectId.get().toHexString();
            final String uploadedFileName = userId + DOUBLE_QUOTES_WITH_SLASH + hexString + DOUBLE_QUOTES_WITH_UNDERSCORE + file.getName();
            log.info("Uploading file with name {}", uploadedFileName);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awss3ConfigProperties.getBucketName())
                    .key(uploadedFileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            uploadedFileNames.add(uploadedFileName);
            fileModel.setId(hexString);
            fileModel.setName(file.getName());
            fileModel.setUploadedName(uploadedFileName);
            Files.delete(file.toPath()); // Remove the file locally created in the project folder
        } catch (IOException e) {
            log.error("Exception in method uploadFile : Error {} occurred while deleting temporary file", e.getMessage());
        } catch (Exception e) {
            log.error("Exception in method uploadFile : Error {} occurred", e.getMessage());
            deleteMultipleObjects(uploadedFileNames);
            throw e;
        }
        return fileModel;
    }

    public void deleteMultipleObjects(List<String> fileNames) {
        try {
            List<ObjectIdentifier> toDelete = fileNames.stream()
                    .map(name -> ObjectIdentifier.builder().key(name).build())
                    .collect(Collectors.toList());
            Delete delete = Delete.builder()
                    .objects(toDelete)
                    .build();
            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(awss3ConfigProperties.getBucketName())
                    .delete(delete)
                    .build();
            s3Client.deleteObjects(deleteObjectsRequest);
        } catch (Exception e) {
            log.error("Exception in method deleteMultipleObjects : Error {} occurred", e.getMessage());
            throw e;
        }
    }

    public boolean doesObjectExist(String bucketName, String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true; // Object exists
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                log.error("inside doesObjectExist(404) method for key : {}", key);
                return false; // Object does not exist
            }
            log.error("inside doesObjectExist method for key : {} and error is {}", key, e.getMessage());
            return false; // Rethrow other errors (e.g., permission issues)
        }
    }

    public String getFile(String fileName, Long expiryInMillis) {
        if (!doesObjectExist(awss3ConfigProperties.getBucketName(), fileName)) {
            return null;
        }

        log.info("Generating signed URL for file name {}", fileName);
        return generatePreSignedUrl(fileName, expiryInMillis);
    }

    private String generatePreSignedUrl(String fileName, Long expiryInMillis) {
        try {
            long expTimeMillis = Objects.requireNonNullElseGet(expiryInMillis, awss3ConfigProperties::getPreSignedUrlExpiryInMillis);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(awss3ConfigProperties.getBucketName())
                    .key(fileName)
                    .build();

            GetObjectPresignRequest preSignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMillis(expTimeMillis))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(preSignRequest).url().toString();
        } catch (Exception e) {
            log.error("inside generatePreSignedUrl method for fileName : {} and error is {}", fileName, e.getMessage());
            throw e;
        }
    }

    public List<FileModel> uploadFiles(final List<MultipartFile> multipartFiles, String userId) throws IOException {
        if (CollectionUtils.isEmpty(multipartFiles)) {
            return null;
        }

        List<FileModel> uploadedFiles = new ArrayList<>();
        List<String> uploadedFileNames = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    final File file = convertMultiPartFileToFile(multipartFile);
                    FileModel fileModel = uploadFile(file, userId);
                    uploadedFileNames.add(fileModel.getUploadedName());
                    uploadedFiles.add(fileModel);
                }
            }
            return uploadedFiles;
        } catch (IOException e) {
            log.error("Exception in method uploadFiles : Error {} occurred while deleting temporary file", e.getLocalizedMessage());
            deleteMultipleObjects(uploadedFileNames);
            throw e;
        }
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) throws IOException {
        final File file;
        if (activeProfile.equalsIgnoreCase(LOCAL)) {
            file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        } else {
            file = new File(System.getProperty("java.io.tmpdir"), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        }
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Error {} occurred while converting the multipart file", e.getLocalizedMessage());
            throw e;
        }
        return file;
    }
}
