# --> Create s3 bucket
echo $(awslocal s3 mb s3://io-web-local)
# --> Create test.txt file
echo $(awslocal s3 cp /home/test.txt s3://io-web-local)
# --> List S3 Buckets
echo $(awslocal s3 ls s3://io-web-local)