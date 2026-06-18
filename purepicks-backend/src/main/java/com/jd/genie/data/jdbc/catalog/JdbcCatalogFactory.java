package com.atomcollide.purepicks.data.jdbc.catalog;


import com.atomcollide.purepicks.data.jdbc.dialect.DialectEnum;

public interface JdbcCatalogFactory {

    DialectEnum jdbcDialect();

    /**
     * Creates a {@link JdbcCatalog} using the options.
     */
    JdbcCatalog createCatalog();
}
