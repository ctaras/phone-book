package com.github.ctaras;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoneBookApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoadsDataSource() {
        assertNotNull(applicationContext.getBean(DataSource.class));
    }

    @Test
    public void contextLoadsPropertyPlaceholderConfigurer() {
        assertNotNull(applicationContext.getBean(PropertyPlaceholderConfigurer.class));
    }
}
