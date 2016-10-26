package com.github.ctaras.config;

import com.github.ctaras.Constants;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.security.InvalidParameterException;
import java.text.MessageFormat;

@Configuration
public class PropertiesConfiguration {

    @Bean
    public static PropertyPlaceholderConfigurer properties() {
        String path = System.getProperty(Constants.NAME_EXTERNAL_CONFIG);

        if (path == null || path.isEmpty()) {
            throw new InvalidParameterException(
                    MessageFormat.format("Missing value for JVM parameter [{0}]! " +
                                    "Please, set JVM parameter: " +
                                    "[java -D{0}=<profile>.properties -Dspring.profiles.active=<profile>]",
                            Constants.NAME_EXTERNAL_CONFIG));
        }

        final PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();

        ppc.setIgnoreResourceNotFound(true);
        ppc.setLocation(new FileSystemResource(path));

        return ppc;
    }
}
