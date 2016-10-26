package com.github.ctaras.repository.mysql;

import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class MySQLDataSourceTest {

    private final String username = "username";
    private final String password = "password";
    private final String url = "url";
    private final String driverClassName = "driverClassName";

    @Test
    public void newDataSource() {
        MySQLDataSource mySQLDataSource = new MySQLDataSource(username, password, url, driverClassName);
        assertThat(mySQLDataSource.newDataSource(), notNullValue());
    }

}