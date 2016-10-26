package com.github.ctaras.config;

import com.github.ctaras.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class PropertiesConfigurationTest {

    private final String nameExternalConfig = Constants.NAME_EXTERNAL_CONFIG;
    private String originalValue = null;

    @Before
    public void setUp() {
        originalValue = System.getProperty(nameExternalConfig);
    }

    @After
    public void tearDown() {
        if (originalValue != null) {
            System.setProperty(nameExternalConfig, originalValue);
        }
    }

    @Test(expected = InvalidParameterException.class)
    public void propertiesWrongSystemProperty() {
        System.clearProperty(nameExternalConfig);
        PropertiesConfiguration.properties();
    }

    @Test
    public void properties() {
        System.setProperty(nameExternalConfig, "file.properties");
        assertThat(PropertiesConfiguration.properties(), notNullValue());
    }

}