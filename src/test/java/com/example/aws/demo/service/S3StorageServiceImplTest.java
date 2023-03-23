package com.example.aws.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.example.aws.demo.config.S3Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
		S3StorageServiceImpl.class,
		S3Configuration.class
})
class S3StorageServiceImplTest {

	@TempDir
	Path tempDir;

	@Autowired
	private S3StorageService s3StorageService;

	@Test
	void downloadAsByteArray() {
		byte[] byteArray = s3StorageService.downloadAsByteArray("test-bucket", "test.txt");
		assertThat(byteArray).isNotEmpty();
		assertThat(new String(byteArray, UTF_8)).isEqualTo("Hi This is file from init script.");
	}

	@Test
	void testGetS3Url() {
		String s3Url = s3StorageService.getS3Url("test-bucket", "test.txt");
		assertThat(s3Url).isEqualTo("http://test-bucket.s3.localhost.localstack.cloud:4566/test.txt");

	}

	@Test
	void testUpload() throws IOException {
		File file = new File(tempDir.toFile(), "test1.txt");
		file.deleteOnExit();
		Files.write(file.toPath(), List.of("Hi this is temp file."));
		boolean b = s3StorageService.uploadFile("test-bucket", "test1.txt", file);
		assertThat(b).isTrue();
		byte[] bytes = s3StorageService.downloadAsByteArray("test-bucket", "test1.txt");
		assertThat(new String(bytes, UTF_8)).isEqualTo("Hi this is temp file." + System.lineSeparator());
	}
}