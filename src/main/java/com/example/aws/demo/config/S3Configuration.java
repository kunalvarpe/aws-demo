package com.example.aws.demo.config;

import java.net.URI;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3CrtAsyncClientBuilder;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import static software.amazon.awssdk.transfer.s3.SizeConstant.MB;

@Configuration(proxyBeanMethods = false)
public class S3Configuration {

	@Bean
	public S3AsyncClient s3AsyncClient(AwsCredentialsProvider awsCredentialsProvider,
			S3Properties s3Properties) {
		S3CrtAsyncClientBuilder s3CrtAsyncClientBuilder = S3AsyncClient.crtBuilder()
				.region(Region.of(s3Properties.region()))
				.credentialsProvider(awsCredentialsProvider)
				.targetThroughputInGbps(20.0)
				.minimumPartSizeInBytes(8 * MB);
		if (StringUtils.hasText(s3Properties.endpoint())) {
			s3CrtAsyncClientBuilder.endpointOverride(URI.create(s3Properties.endpoint()));
		}
		return s3CrtAsyncClientBuilder.build();
	}

	@Bean
	public S3TransferManager s3TransferManager(S3AsyncClient s3AsyncClient) {
		return S3TransferManager.builder().s3Client(s3AsyncClient).build();
	}

	@Bean
	AwsCredentialsProvider awsCredentialsProvider(S3Properties s3Properties) {
		return () -> AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey());
	}
}