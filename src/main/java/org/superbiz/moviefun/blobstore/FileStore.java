package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;

@Component
public class FileStore implements BlobStore {

    @Autowired
    private S3Store s3Store;

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.contentType);
        s3Store.putObject(s3Store.getBucketName(), blob.name,
                 blob.inputStream , objectMetadata);
        // ...
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        // ...
        S3Object s3object = s3Store.getObject(s3Store.getBucketName(), name);
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        Blob b = new Blob(name, inputStream,s3object.getObjectMetadata().getContentType() );
        Optional<Blob> op = Optional.of(b);

        return op;
    }

    @Override
    public void deleteAll() {
        // ...
        s3Store.deleteBucket(s3Store.getBucketName());
    }
}