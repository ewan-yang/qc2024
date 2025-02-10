package com.tecpie.platform.s3.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${aws.s3.accessKey}")
    private String accessKey;
    @Value("${aws.s3.secretKey}")
    private String secretKey;

    @Value("${aws.s3.endpoint}")
    private String endpoint;
    @Value(("${aws.s3.mode}"))
    private Boolean mode;
    @Value("${aws.s3.signingRegion}")
    private String signingRegion;

    @Bean("amazonS3Client")
    public AmazonS3 amazonS3Client() {
        com.amazonaws.ClientConfiguration clientConfig = new com.amazonaws.ClientConfiguration();
        // Using HTTPS protocol
        clientConfig.setProtocol(com.amazonaws.Protocol.HTTP);
        clientConfig.setSignerOverride("S3SignerType");

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        if (mode) {
            return AmazonS3ClientBuilder.standard()
                    .withClientConfiguration(clientConfig)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(AWSS3RegionConfig.getRegions(endpoint))
                    .build();
        } else {
            return AmazonS3ClientBuilder.standard()
                    .withClientConfiguration(clientConfig)
                    .withEndpointConfiguration(new EndpointConfiguration(endpoint, signingRegion))
                    .withCredentials(
                            new AWSStaticCredentialsProvider(credentials))
                    .build();
        }
    }
}
