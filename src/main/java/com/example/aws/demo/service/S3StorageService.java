package com.example.aws.demo.service;

import java.io.File;

public interface S3StorageService {
	byte[] downloadAsByteArray(String bucketName, String keyName);

	boolean uploadFile(String bucketName, String keyName, File file);

	String getS3Url(String bucketName, String keyName);
}
