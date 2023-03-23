#!/bin/bash
awslocal s3 mb s3://test-bucket
awslocal s3 cp /home/test.txt s3://test-bucket