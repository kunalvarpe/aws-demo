package com.example.aws.demo.config;

import java.net.URI;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3CrtAsyncClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Configuration
public class S3Configuration {
	@Value("${aws.access-key}")
	private String accessKey;

	@Value("${aws.secret-key}")
	private String secretKey;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.endpoint:#{null}}")
	private String endpoint;

	@Bean
	public S3AsyncClient s3AsyncClient(AwsCredentialsProvider awsCredentialsProvider) {
		S3CrtAsyncClientBuilder s3CrtAsyncClientBuilder = S3AsyncClient.crtBuilder()
				.region(Region.of(region))
				.credentialsProvider(awsCredentialsProvider)
				.targetThroughputInGbps(20.0)
				.minimumPartSizeInBytes(8 * MB);
		if (StringUtils.hasText(endpoint)) {
			s3CrtAsyncClientBuilder.endpointOverride(URI.create(endpoint));
		}
		return s3CrtAsyncClientBuilder.build();
	}

	@Bean
	AwsCredentialsProvider awsCredentialsProvider() {
		return () -> AwsBasicCredentials.create(accessKey, secretKey);
	}
}