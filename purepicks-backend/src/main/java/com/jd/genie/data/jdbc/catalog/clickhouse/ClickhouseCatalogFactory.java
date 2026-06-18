package com.atomcollide.purepicks.data.jdbc.catalog.clickhouse;

import com.google.auto.service.AutoService;
import com.atomcollide.purepicks.data.jdbc.catalog.JdbcCatalog;
import com.atomcollide.purepicks.data.jdbc.catalog.JdbcCatalogFactory;
import com.atomcollide.purepicks.data.jdbc.dialect.DialectEnum;

@AutoService(JdbcCatalogFactory.class)
public class ClickhouseCatalogFactory implements JdbcCatalogFactory {
    @Override
    public DialectEnum jdbcDialect() {
        return DialectEnum.CLICKHOUSE;
    }

    @Override
    public JdbcCatalog createCatalog() {
        return new ClickhouseCatalog();
    }
}
