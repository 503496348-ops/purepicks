package com.atomcollide.purepicks.data.jdbc.catalog.h2;

import com.google.auto.service.AutoService;
import com.atomcollide.purepicks.data.jdbc.catalog.JdbcCatalog;
import com.atomcollide.purepicks.data.jdbc.catalog.JdbcCatalogFactory;
import com.atomcollide.purepicks.data.jdbc.dialect.DialectEnum;


@AutoService(JdbcCatalogFactory.class)
public class H2CatalogFactory implements JdbcCatalogFactory {
    @Override
    public DialectEnum jdbcDialect() {
        return DialectEnum.H2;
    }

    @Override
    public JdbcCatalog createCatalog() {
        return new H2SqlCatalog();
    }
}
