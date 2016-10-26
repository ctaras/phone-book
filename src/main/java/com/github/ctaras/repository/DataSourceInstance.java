package com.github.ctaras.repository;

import javax.sql.DataSource;

public interface DataSourceInstance {
    DataSource newDataSource();
}
