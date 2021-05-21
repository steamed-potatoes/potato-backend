package com.potato.external.external.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface S3Service {

    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

}
