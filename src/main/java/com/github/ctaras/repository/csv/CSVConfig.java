package com.github.ctaras.repository.csv;

import com.github.ctaras.repository.DataStruct;
import com.github.ctaras.util.Log;
import org.relique.jdbc.csv.CsvDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Profile("csv")
@Component
public class CSVConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(CSVConfig.class);
    final String ext = ".csv";
    final String fileSeparator = File.separator;
    final String columnSeparator;
    final String lineSeparator;
    private final DataStruct dt;
    @Value("${lardi.datasource.path:}")
    public String path;

    @Autowired
    public CSVConfig(DataStruct data) {
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        } catch (ClassNotFoundException ex) {
            Log.e(logger, () -> "Error of loading CSV driver", ex);
        }

        columnSeparator = CsvDriver.DEFAULT_SEPARATOR;
        lineSeparator = System.getProperty("line.separator");
        this.dt = data;
    }

    @PostConstruct
    private void initDataBase() throws IOException {
        File rootDir = new File(path);
        if (!rootDir.exists()) {
            throw new NoSuchFileException(path);
        }

        if (!rootDir.isDirectory()) {
            throw new NotDirectoryException(path);
        }

        String userColumns = String.format("%s,%s,%s,%s",
                dt.USER_ID, dt.USER_LOGIN, dt.USER_PASSWD, dt.USER_FULL_NAME);

        createTableFile(getUserTablePath(), userColumns);

        String contactColumns = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                dt.CON_ID, dt.USER_ID, dt.CON_LAST_NAME, dt.CON_FIRST_NAME,
                dt.CON_MIDDLE_NAME, dt.CON_MOB_PHONE_NUM, dt.CON_HOME_PHONE_NUM,
                dt.CON_ADDRESS, dt.CON_EMAIL);

        createTableFile(getContactTablePath(), contactColumns);
    }

    private void createTableFile(String tablePath, String columns) throws IOException {
        File table = new File(tablePath);
        if (table.exists() && table.isFile()) {
            return;
        }

        if (table.createNewFile()) {
            Files.write(table.toPath(), columns.getBytes(), StandardOpenOption.APPEND);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:relique:csv:" + path);
    }

    public String getUserTablePath() {
        return path + fileSeparator + dt.USER_TABLE + ext;
    }

    public String getContactTablePath() {
        return path + fileSeparator + dt.CONTACT_TABLE + ext;
    }
}
