package com.github.ctaras.repository.mysql;

import org.junit.Test;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.theInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MySQLConfigTest {
    @Test
    public void jdbcTemplate() {
        DataSource dataSource = mock(DataSource.class);
        MySQLConfig mySQLConfig = new MySQLConfig(dataSource);
        assertThat(mySQLConfig.jdbcTemplate(), notNullValue());
        assertThat(mySQLConfig.jdbcTemplate().getDataSource(), theInstance(dataSource));
    }

}