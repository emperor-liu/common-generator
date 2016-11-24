package com.lljqiu.generator.dao_maker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lljqiu.generator.config.CreateBean;
import com.lljqiu.generator.config.Field;
import com.lljqiu.generator.config.ParserInfo;
import com.lljqiu.generator.config.SourceConfig;
import com.lljqiu.generator.config.TableList;
import com.lljqiu.generator.utils.CreateFiles;
import com.lljqiu.generator.utils.DMException;

/** 
 * <p>文件名称: DataobjectMaker.java</p>
 * 
 * <p>文件功能: main执行方法
 *  <pre>
 * DO类生成工具 
 * </pre></p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年4月13日 下午5:05:05</p>
 * 
 * <p>版本: version 1.0 </p>
 *
 * <p>输入说明: </p>
 *
 * <p>输出说明: </p>
 *
 * <p>程序流程: </p>
 * 
 * <p>============================================</p>
 * <p>修改序号:</p>
 * <p>时间:	 </p>
 * <p>修改者:  </p>
 * <p>修改内容:  </p>
 * <p>============================================</p>
 */
public class DateObjectMaker {

    private static Logger       log = LoggerFactory.getLogger(DateObjectMaker.class);
    private static final String KEY = DateObjectMaker.class.getName()+"###$$&&";

    public void createConfig(SourceConfig sourceConfig) {
        log.debug(KEY + "start create config ...");
        try {
            Connection conn = createConn(sourceConfig);
            DatabaseMetaData metaData = conn.getMetaData();
            List<TableList> tableList = getTables(metaData, sourceConfig);
            CreateBean createBean = putInfo(sourceConfig);

            for (int i = 0; i < tableList.size(); i++) {
                StringBuilder sqlColumn = new StringBuilder(); //所有的字段集合
                String tableName = tableList.get(i).getTableName();
                ResultSet colRet = metaData.getColumns(null, "%", tableName, "%");
                while (colRet.next()) {
                    sqlColumn.append(colRet.getString("COLUMN_NAME")).append(",");
                }
                log.debug("table name is 【{}】  ;table columns 【{}】 ", tableName, sqlColumn.toString());
                String tbStrs[] = tableName.split("_");
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tbStrs.length; j++) {
                    sb.append(tbStrs[j].substring(0, 1).toUpperCase()).append(
                            tbStrs[j].substring(1).toLowerCase());
                }
                ParserInfo parserInfo = new ParserInfo(conn, tableName, null);
                createBean.setParserInfo(parserInfo);

                createBean.setFieldsContent(getFieldsContent(parserInfo));
                createBean.setGetterSetterContent(getGetterSetterContent(parserInfo));
                createBean.setFileName(sb.toString());
                CreateFiles.createBean(createBean); //create vo

//                createBean.setDaoName(sb.toString());
                CreateFiles.createDao(createBean); //create Dao

                createBean.setResultMapContent(getResultMapContent(parserInfo));
                createBean.setSqlColumn(sqlColumn.toString().substring(0,
                        sqlColumn.toString().length() - 1));
                createBean.setTableName(tableName);

                CreateFiles.createMyBatisXml(createBean);

            }
            CreateFiles.createDataSource(createBean);
            CreateFiles.createDataSourceConfig(createBean);
            log.debug("create success");
        } catch (Exception e) {
            throw new DMException("create config error", e);
        }

    }

    /** 
    * Description：get tables 
    * @param metaData Connection info
    * @param sourceConfig
    * @return List<TableList>
    * @author name：
     **/
    @SuppressWarnings("finally")
    private static List<TableList> getTables(DatabaseMetaData metaData, SourceConfig sourceConfig) {
        TableList tableName = null;
        log.debug(KEY + "get config tables");
        List<TableList> tableList = sourceConfig.getTableList(); //get Tables
        try {
            if (tableList == null) { //config Table is null ,get All Tables;
                tableList = new ArrayList<TableList>();
                ResultSet tables = metaData.getTables(sourceConfig.getDbName(),
                        sourceConfig.getUsername(), null, new String[] { "TABLE" });

                while (tables.next()) {
                    tableName = new TableList();
                    tableName.setTableName(tables.getString("TABLE_NAME"));
                    tableList.add(tableName);
                }
            }
        } catch (SQLException e) {
            throw new DMException("get Tables error", e);
        } finally {
            log.debug(KEY + listToString(tableList, ","));
            return tableList;
        }

    }

    private static String listToString(List<TableList> list, String delimiter) {
        if (list == null) {
            throw new DMException("list is null",null);
        }

        if (list.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Object item : list) {
            builder.append(item.toString() + delimiter);
        }
        builder.delete(builder.length() - delimiter.length(), builder.length());
        return builder.toString();
    }

    /** 
    * Description： 创建链接
    * @param sourceConfig    链接配置
    * @return Connection
    * @author name：
     **/
    @SuppressWarnings("finally")
    private static Connection createConn(SourceConfig sourceConfig) {
        Connection conn = null;
        try {
            log.debug(KEY + "create a link[{}]" ,sourceConfig.getDateSourceUrl());
            conn = DriverManager.getConnection(sourceConfig.getDateSourceUrl(),
                    sourceConfig.getUsername(), sourceConfig.getPassword());
        } catch (SQLException e) {
            throw new DMException("create connection error", e);
        } finally {

            return conn;
        }
    }

    /** 
     * Description：vo -- 获取所有列
     * @return String
     * @author name：
      **/
    private static String getFieldsContent(ParserInfo parserInfo) {
        List<Field> FIELDS = parserInfo.getFields();
        StringBuffer buffer = new StringBuffer();
        String template = "\t/** #<propertyDescription> */\n\tprivate #<javaType> #<propertyName>;\n";
        for (Field field : FIELDS) {
            String property = template.replaceAll("#<javaType>", field.getJavaType());
            property = property.replaceAll("#<propertyName>", field.getPropertyName());
            String description = field.getDescription();
            if (description != null && !description.trim().equals("")) {
                property = property.replaceAll("#<propertyDescription>", description);
            } else {
                property = property.replaceAll("#<propertyDescription>", "");
            }
            buffer.append(property);
        }
        return buffer.toString();
    }

    /** 
     * Description：create set get function
     * @return String
     * @author name：
      **/
    private static String getGetterSetterContent(ParserInfo parserInfo) {
        StringBuffer buffer = new StringBuffer();
        List<Field> FIELDS = parserInfo.getFields();
        for (Field field : FIELDS) {
            buffer.append(getGetterContent(field));
            buffer.append(getSetterContent(field));
        }
        return buffer.toString();
    }

    /** 
     * Description：create get content
     * @return String
     * @author name：lljqiu
      **/
    private static String getGetterContent(Field field) {
        String fieldType = field.getFieldType();
        String templateGetter = "\tpublic #<javaType> get#<metholdPostName>() {\n\t\treturn #<propertyName>;\n\t}\n\n";
        if ("BIT".equals(fieldType)) {
            templateGetter = "\tpublic #<javaType> is#<metholdPostName>() {\n\t\treturn #<propertyName>;\n\t}\n\n";
        }
        return getMetholdContent(templateGetter, field);
    }

    /** 
     * Description：create set content
     * @return String
     * @author name：lljqiu
      **/
    private static String getSetterContent(Field field) {
        String templateSetter = "\tpublic void set#<metholdPostName>(#<javaType> #<propertyName>) {\n\t\tthis.#<propertyName> = #<propertyName>;\n\t}\n\n";
        return getMetholdContent(templateSetter, field);
    }
    
    /** 
     * Description：create Methold content
     * @return String
     * @author name：lljqiu
      **/
    private static String getMetholdContent(String template, Field field) {
        StringBuffer buffer = new StringBuffer();
        String methold = template.replaceAll("#<javaType>", field.getJavaType());
        String propertyName = field.getPropertyName();
        methold = methold.replaceAll("#<metholdPostName>", propertyName.substring(0, 1)
                .toUpperCase() + propertyName.substring(1));
        methold = methold.replaceAll("#<propertyName>", field.getPropertyName());
        buffer.append(methold);
        return buffer.toString();
    }

    /** 
     * Description：create mybatis resultMap
     * @return String
     * @author name：lljqiu
      **/
    private static String getResultMapContent(ParserInfo parserInfo) {
        List<Field> fields = parserInfo.getFields();
        StringBuffer buffer = new StringBuffer();
        Field field = fields.get(0);
        String resultMapRowContent = getResultMapRowContent(field);
        buffer.append(resultMapRowContent);

        for (int i = 1; i < fields.size(); i++) {
            field = fields.get(i);
            String result = getResultMapRowContent(field);
            buffer.append("\n" + result);
        }
        return buffer.toString();
    }
    /** 
     * Description：create result column list
     * @return String
     * @author name：lljqiu
      **/
    private static String getResultMapRowContent(Field field) {
        String template = "\t\t<result column=\"#<keyField>\" property=\"#<key>\" />";
        String result = template.replaceAll("#<keyField>", field.getFieldName());
        result = result.replaceAll("#<key>", field.getPropertyName());
        return result;
    }

    /** 
    * Description：put 基本信息
    * @param sourceConfig
    * @return CreateBean
    * @author name：lljqiu
     **/
    private static CreateBean putInfo(SourceConfig sourceConfig) {
        CreateBean createBean = new CreateBean();
        createBean.setFilePackage(sourceConfig.getFilePackage());
        createBean.setFileVoPackage(sourceConfig.getFileVoPackage());
        createBean.setFileDaoPackage(sourceConfig.getFileDaoPackage());
        createBean.setFileMapperPath(sourceConfig.getFileMapperPath());
        createBean.setFilePath(sourceConfig.getFilePath());
        createBean.setDbType(sourceConfig.getDbType());
        createBean.setDbName(sourceConfig.getDbName());
        createBean.setUsername(sourceConfig.getUsername());
        createBean.setPassword(sourceConfig.getPassword());
        createBean.setDateSourceUrl(sourceConfig.getDateSourceUrl());
        createBean.setOsType(sourceConfig.getOsType());
        return createBean;
    }
}
