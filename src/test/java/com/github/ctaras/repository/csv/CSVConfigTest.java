package com.github.ctaras.repository.csv;

import com.github.ctaras.repository.DataStruct;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CSVConfigTest {

    private final String path = "path";
    private final String userTable = "userTable";
    private final String contactTable = "contactTable";
    private final String ext = ".csv";
    private CSVConfig csvConfig;

    @Before
    public void setUp() {
        DataStruct dataStruct = mock(DataStruct.class);
        csvConfig = new CSVConfig(dataStruct);
        csvConfig.path = path;
    }

    @Test
    public void getUserTablePath() {
        assertThat(csvConfig.getUserTablePath(), notNullValue());
        assertThat(csvConfig.getUserTablePath(), not(""));
    }

    @Test
    public void getContactTablePath() {
        assertThat(csvConfig.getContactTablePath(), notNullValue());
        assertThat(csvConfig.getContactTablePath(), not(""));
    }

}