package com.atomcollide.purepicks.data.jdbc.dialect.h2;

import com.google.auto.service.AutoService;
import com.atomcollide.purepicks.data.jdbc.dialect.DialectEnum;
import com.atomcollide.purepicks.data.jdbc.dialect.JdbcDialect;
import com.atomcollide.purepicks.data.jdbc.dialect.JdbcDialectFactory;

@AutoService(JdbcDialectFactory.class)
public class H2DialectFactory implements JdbcDialectFactory {
    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith(DialectEnum.H2.getUrlPrefix());
    }

    @Override
    public JdbcDialect create() {
        return new H2Dialect();
    }
}
