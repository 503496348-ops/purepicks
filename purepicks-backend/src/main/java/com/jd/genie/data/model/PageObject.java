package com.atomcollide.purepicks.data.model;

import lombok.Data;

import java.util.List;

@Data
public class PageObject<T extends Object> {
    List<T> dataList;
    int totalCount;
}
