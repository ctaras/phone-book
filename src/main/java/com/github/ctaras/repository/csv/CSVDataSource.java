package com.github.ctaras.repository.csv;

import com.github.ctaras.repository.DataSourceInstance;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Profile("csv")
@Component
public class CSVDataSource implements DataSourceInstance {

    private static final Logger logger =
            LoggerFactory.getLogger(CSVDataSource.class);

    @Value("${lardi.datasource.path:}")
    private String path;

    public CSVDataSource() {
    }

    public CSVDataSource(String path) {
        this();
        this.path = path;
    }

    public DataSource newDataSource() {

        Log.d(logger, () -> new StringBuilder()
                .append("DataSource parameters: ")
                .append("\npath: ")
                .append(path)
                .toString()
        );

        DataSource dataSource = DataSourceBuilder.create().build();

        Log.i(logger, () -> "Created empty DataSource; mode [CSV]");

        return dataSource;
    }
}
