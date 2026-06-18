package com.atomcollide.purepicks.data.provider.jdbc;

import com.atomcollide.purepicks.data.jdbc.JdbcConnectionConfig;
import com.atomcollide.purepicks.data.provider.DataQueryRequest;
import lombok.Data;

@Data
public class JdbcQueryRequest implements DataQueryRequest {

    private JdbcConnectionConfig jdbcConnectionConfig;
    private String sql;
    private int limit;

    private int pageIndex;
    private int pageSize;
}
