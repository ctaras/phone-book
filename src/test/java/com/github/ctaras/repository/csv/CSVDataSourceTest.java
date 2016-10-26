package com.github.ctaras.repository.csv;

import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class CSVDataSourceTest {
    private final String path = "path";

    @Test
    public void newDataSource() {
        CSVDataSource csvDataSource = new CSVDataSource(path);
        assertThat(csvDataSource.newDataSource(), notNullValue());
    }

}