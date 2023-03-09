package com.example.aws.demo.service;

import java.io.File;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDownload;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.DownloadRequest;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
public class S3StorageServiceImpl implements S3StorageService {

	public static final String ERROR_MSG_FORMAT = "Error performing S3 client operation";

	private static final Logger LOG = LoggerFactory.getLogger(S3StorageServiceImpl.class);

	private final S3AsyncClient s3AsyncClient;

	public S3StorageServiceImpl(S3AsyncClient s3AsyncClient) {
		this.s3AsyncClient = s3AsyncClient;
	}

	@Override
	public byte[] downloadAsByteArray(String bucketName, String keyName) {
		try (S3TransferManager transferManager = S3TransferManager.builder().s3Client(s3AsyncClient).build()) {
			DownloadRequest<ResponseBytes<GetObjectResponse>> downloadFileRequest =
					DownloadRequest.builder()
							.getObjectRequest(req -> req.bucket(bucketName).key(keyName))
							.responseTransformer(AsyncResponseTransformer.toBytes())
							.addTransferListener(LoggingTransferListener.create())
							.build();

			// Wait for the transfer to complete
			return transferManager.download(downloadFileRequest)
					.completionFuture()
					.thenApply(CompletedDownload::result)
					.thenApply(BytesWrapper::asByteArray)
					.join();
		}
		catch (CompletionException e) {
			LOG.error(ERROR_MSG_FORMAT, e);
			return new byte[0];
		}
	}

	@Override
	public boolean uploadFile(String bucketName, String keyName, File file) {
		try (S3TransferManager transferManager = S3TransferManager.builder().s3Client(s3AsyncClient).build()) {
			UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
					.source(file)
					.putObjectRequest(reqBuilder -> reqBuilder.bucket(bucketName).key(keyName))
					.addTransferListener(LoggingTransferListener.create())
					.build();
			return transferManager.uploadFile(uploadFileRequest)
					.completionFuture()
					.thenApply(CompletedFileUpload::response)
					.thenApply(PutObjectResponse::eTag)
					.thenApply(StringUtils::hasText)
					.join();
		}
	}

	@Override
	public String getS3Url(String bucketName, String keyName) {
		return s3AsyncClient.utilities().getUrl(req -> req.bucket(bucketName).key(keyName)).toString();
	}
}