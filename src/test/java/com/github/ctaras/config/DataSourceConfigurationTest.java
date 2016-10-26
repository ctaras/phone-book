package com.github.ctaras.config;

import com.github.ctaras.repository.DataSourceInstance;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DataSourceConfigurationTest {
    @Test
    public void dataSource() {
        DataSourceInstance instance = mock(DataSourceInstance.class);
        when(instance.newDataSource()).thenReturn(DataSourceBuilder.create().build());

        DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration(instance);

        assertThat(dataSourceConfiguration.dataSource(), notNullValue());
        verify(instance).newDataSource();
    }

}