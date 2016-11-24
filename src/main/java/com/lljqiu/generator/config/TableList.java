package com.lljqiu.generator.config;

import java.io.Serializable;

public class TableList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String            tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
