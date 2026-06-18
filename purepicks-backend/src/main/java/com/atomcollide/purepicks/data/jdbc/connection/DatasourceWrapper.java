package com.atomcollide.purepicks.data.jdbc.connection;

import com.atomcollide.purepicks.data.jdbc.catalog.JdbcCatalog;
import com.atomcollide.purepicks.data.jdbc.dialect.JdbcDialect;
import lombok.Data;

import javax.sql.DataSource;

@Data
public class DatasourceWrapper {

    private DataSource dataSource;

    private JdbcDialect jdbcDialect;

    private JdbcCatalog catalog;

    private Long freshTime;
}
