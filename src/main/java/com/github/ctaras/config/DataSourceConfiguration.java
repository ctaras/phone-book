package com.github.ctaras.config;

import com.github.ctaras.repository.DataSourceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    private final DataSourceInstance instance;

    @Autowired
    public DataSourceConfiguration(DataSourceInstance instance) {
        this.instance = instance;
    }

    @Bean
    public DataSource dataSource() {
        return instance.newDataSource();
    }

}
