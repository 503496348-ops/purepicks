
package com.atomcollide.purepicks.data.jdbc.catalog;


import com.atomcollide.purepicks.data.SimpleTable;
import com.atomcollide.purepicks.data.TableColumn;
import com.atomcollide.purepicks.data.exception.CatalogException;

import java.sql.Connection;
import java.util.List;

public interface JdbcCatalog {

    List<SimpleTable> listTables(Connection connection, String schema) throws CatalogException;

    List<TableColumn> getTableColumns(Connection connection, String tablePath, String schema) throws CatalogException;
}
