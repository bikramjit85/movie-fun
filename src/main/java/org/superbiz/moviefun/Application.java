package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.S3Store;
import org.superbiz.moviefun.blobstore.ServiceCredentials;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }

    @Bean
    ServiceCredentials serviceCredentials(@Value("${vcap.services}") String vcapServices) {
        return new ServiceCredentials(vcapServices);
    }

    @Bean
    public S3Store s3Store(
            ServiceCredentials serviceCredentials,
            @Value("${vcap.services.photo-storage.credentials.endpoint:#{null}}") String endpoint
    ) {
        String photoStorageAccessKeyId = serviceCredentials.getCredential("photo-storage",
                "user-provided", "access_key_id");
        String photoStorageSecretKey = serviceCredentials.getCredential("photo-storage",
                "user-provided", "secret_access_key");
        String photoStorageBucket = serviceCredentials.getCredential("photo-storage",
                "user-provided", "bucket");

        AWSCredentials credentials = new BasicAWSCredentials(photoStorageAccessKeyId, photoStorageSecretKey);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        if (endpoint != null) {
            s3Client.setEndpoint(endpoint);
        }

       return new S3Store(s3Client, photoStorageBucket);
    }


    public static void main2(String[] args) throws IOException {

        AWSCredentials credentials = new BasicAWSCredentials("AKIAVGIED6HQTYCU6ATL", "l78ZV8WLSjFv18mTtdjl/BIgS3t0BX70aic8wAvK");

        AmazonS3Client s3Client = new AmazonS3Client(credentials);
         String endpoint = "https://s3.us-east-2.amazonaws.com";
        s3Client.setEndpoint(endpoint);

        String bucketName = "test898989.moviefunpalarch";

        String key ="first1";
        File file = new File("c:\\t\\test.txt");


        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult result = s3Client.putObject(putObjectRequest);
        System.out.println("Put file '" + "' under key " + key + " to bucket " + bucketName);


    }
}
