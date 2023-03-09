package com.example.aws.demo.service;

import com.example.aws.demo.config.S3Configuration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
		S3StorageServiceImpl.class,
		S3Configuration.class
})
class S3StorageServiceImplTest {

	@Autowired
	private S3StorageService s3StorageService;

	@Test
	void downloadAsByteArray() {
		byte[] byteArray = s3StorageService.downloadAsByteArray("io-web-local", "test.txt");
		assertThat(byteArray).isNotEmpty();
	}

	@Test
	void testGetS3Url() {
		String s3Url = s3StorageService.getS3Url("io-web-local", "test.txt");
		assertThat(s3Url).isEqualTo("https://io-web-local.s3.amazonaws.com/test.txt");

	}
}