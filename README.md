# aws-demo
This project demonstrate the issue in `Spring Boot` + `localstack` The issue is that localstack S3 service enabled and uploaded some files to s3 bucket. The `S3TransferManager.download(DownloadRequest req)` method is not able to download that file. However, when try to get the S3 URL for same file from localstack s3 bucket it is returning the s3 url which is strange.

## Usage

Make LocalStack container up and running.

```bash
docker compose up -d
```

Open `S3StorageServiceImplTest` file and run the test.

## Contributing

Pull request are welcome. If I am missing any config or doing wrong with use case please open pull request.

Please make sure to update test as appropriate.
