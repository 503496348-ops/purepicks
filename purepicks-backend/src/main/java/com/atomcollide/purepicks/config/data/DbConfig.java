package com.atomcollide.purepicks.config.data;

import lombok.Data;

@Data
public class DbConfig {
    private String type;
    private String host;
    private int port;
    private String schema;
    private String username;
    private String password;
    private String key = "purepicks-datasource";
}
