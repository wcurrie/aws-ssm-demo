package com.example.config;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AwsSsmConfig {

    private static final Log LOG = LogFactory.getLog(AwsSsmConfig.class);

    @Bean
    public AWSSimpleSystemsManagement awsClient() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    @Bean
    public PropertySourceLocator ssmPropertySourceLocator(AWSSimpleSystemsManagement awsClient) {
        return environment -> {
            LOG.info("Retrieving config from AWS EC2 parameter store");
            GetParametersResult parameters = awsClient.getParameters(new GetParametersRequest()
                    .withNames("db.password")
                    .withWithDecryption(true));
            Map<String, Object> config = new HashMap<>();
            parameters.getParameters().forEach(parameter -> {
                LOG.info("Found " + parameter.getName() + " " + parameter.getType());
                config.put(parameter.getName(), parameter.getValue());
            });
            return new MapPropertySource("aws-ssm", config);
        };
    }
}
