package com.atomcollide.purepicks.data.provider;


import com.atomcollide.purepicks.data.QueryResult;


public interface DataProvider<T extends DataQueryRequest> {

    QueryResult queryData(T request) throws Exception;

    boolean queryForTest(T request);
}
