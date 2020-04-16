package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;

public class S3Store extends AmazonS3Client {

    private AmazonS3Client amazonS3Client;
    private String bucketName;

    public S3Store(AmazonS3Client amazonS3Client, String bucketName) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
    }

    public AmazonS3Client getAmazonS3Client() {
        return amazonS3Client;
    }

    public void setAmazonS3Client(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
