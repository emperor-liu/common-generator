package com.lljqiu.generator;

import java.sql.SQLException;
import java.util.List;

import com.lljqiu.generator.config.SourceConfig;
import com.lljqiu.generator.config.TableList;
import com.lljqiu.generator.dao_maker.DateObjectMaker;

public class Test {

    public static void main(String[] args) throws SQLException {

        String dateSourceUrl = "jdbc:mysql://127.0.0.1:3306/icat";
        String userName = "root";
        String password = "root";
        String dbType = "mysql";
        String dbName = "icat";
        String filePackage = "com.cmcc.crawler";
        String sequenceName = "a";
        List<TableList> tableList = null;
        SourceConfig sourceConfig = new SourceConfig(dateSourceUrl, userName,
                password, dbName, dbType, tableList, filePackage, sequenceName);
        DateObjectMaker dateObjectMaker = new DateObjectMaker();
        dateObjectMaker.createConfig(sourceConfig);
    }

}
