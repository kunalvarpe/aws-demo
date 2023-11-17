package com.example.aws.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Kunal Varpe
 */
@ConfigurationProperties(prefix = "cloud.aws")
public record S3Properties(
		String accessKey,
		String secretKey,
		String region,
		String endpoint) {
}
