package com.github.ctaras.repository.mysql;

import com.github.ctaras.repository.DataSourceInstance;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Profile("mysql")
@Component
public class MySQLDataSource implements DataSourceInstance {

    private static final Logger logger =
            LoggerFactory.getLogger(MySQLDataSource.class);

    @Value("${lardi.datasource.username:}")
    private String username;

    @Value("${lardi.datasource.password:}")
    private String password;

    @Value("${lardi.datasource.url:}")
    private String url;

    @Value("${lardi.datasource.driver-class-name:}")
    private String driverClassName;

    public MySQLDataSource() {
    }

    public MySQLDataSource(String username, String password, String url, String driverClassName) {
        this();
        this.username = username;
        this.password = password;
        this.url = url;
        this.driverClassName = driverClassName;
    }

    public DataSource newDataSource() {

        Log.d(logger, () ->
                new StringBuilder()
                        .append("DataSource parameters: ")
                        .append("\nusername: ")
                        .append(username)
                        .append("\npassword: ")
                        .append(password)
                        .append("\nurl: ")
                        .append(url)
                        .append("\ndriverClassName: ")
                        .append(driverClassName)
                        .toString()
        );

        DataSource dataSource = DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName(driverClassName)
                .build();

        Log.i(logger, () -> "Created DataSource; mode [MySQL]");

        return dataSource;
    }
}
