package com.lljqiu.generator.config;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lljqiu.generator.utils.DMException;

public class SourceConfig implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 实例：
     * jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8
     */
    private String            dateSourceUrl;
    private String            username;
    private String            password;
    private String            dbName;
    private String            dbType;               // 数据库类型 mysql oracle db2 sql-server
    private String            sequenceName;
    private List<TableList>   tableList;
    private String            filePath;
    private String            filePackage;
    private String            fileDaoPackage;
    private String            fileVoPackage;
    private String            fileMapperPath;
    private String            osType;

    // * @param mapperLocations sessionFactory mapperLocations config
    // 例：com/asdc/dal/mapper/ || config/mybatis
    /**
     * 生成 dao po xml 配置文件
     * <ul>
     * if(filePath == null)
     * <li>win filePath=C:\\Users\\Administrator\\Desktop</li>
     * <li>linux filePath=/root/Desktop</li>
     * <li>mac filePath=/root/Desktop</li>
     * </ul>
     * @param dateSourceUrl  
     *      例： jdbc:mysql://127.0.0.1:3306/test --  不可为空
     * @param userName
     *      数据库链接名 -- 不可为空
     * @param password
     *      数据库连接密码
     * @param dbName
     *      数据库名
     * @param dbType
     *      数据库类型
     * @param tableList
     *      数据库表集合 可为空，如果为空则导出该数据库中所有表结构
     * @param filePackage
     *      文件包路径 可为空，默认com.lljqiu.test
     * @param sequenceName
     *      序列号 当且仅当db Type＝mysql 时可为空
     * 
     */
    public SourceConfig(String dateSourceUrl, String userName, String password, String dbName, String dbType,
                        List<TableList> tableList, String filePackage, String sequenceName) {
        if (StringUtils.isBlank(dateSourceUrl) || StringUtils.isBlank(userName) || StringUtils.isBlank(password)
                || StringUtils.isBlank(dbName) || StringUtils.isBlank(dbType)) {
            throw new DMException("db config is null");
        }
        if(dbType.equalsIgnoreCase("oracle")){
            DMException.checkCondition(StringUtils.isBlank(sequenceName), "sequenceName not bull");
        }
        this.dateSourceUrl = dateSourceUrl;
        this.username = userName;
        this.password = password;
        this.dbName = dbName;
        this.dbType = dbType;
        if (StringUtils.isBlank(filePackage)) {
            filePackage = "com.lljqiu.test";
        }
        this.fileMapperPath = filePackage + ".mapper";
        this.fileDaoPackage = filePackage + ".dao";
        this.fileVoPackage = filePackage + ".do";
        this.filePackage = filePackage;
        this.sequenceName = sequenceName;
        this.tableList = tableList;
        this.osType = System.getProperty("os.name").toUpperCase();
        if (StringUtils.isBlank(filePath)) {
            if (osType.indexOf("WINDOWS") != -1) {
                this.osType = "WINDOWS";
                this.filePath = "C:\\Users\\Administrator\\Desktop";
            } else if (osType.indexOf("LINUX") != -1) {
                this.osType = "LINUX";
                this.filePath = "/root/Desktop";
            } else if (osType.indexOf("MAC") != -1) {
                this.osType = "MAC";
                this.filePath = "/Users/"+System.getProperty("user.name")+"/Desktop";
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<TableList> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableList> tableList) {
        this.tableList = tableList;
    }

    public String getDateSourceUrl() {
        return dateSourceUrl;
    }

    public void setDateSourceUrl(String dateSourceUrl) {
        this.dateSourceUrl = dateSourceUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilePackage() {
        return filePackage;
    }

    public void setFilePackage(String filePackage) {
        this.filePackage = filePackage;
    }

    public String getFileDaoPackage() {
        return fileDaoPackage;
    }

    public void setFileDaoPackage(String fileDaoPackage) {
        this.fileDaoPackage = fileDaoPackage;
    }

    public String getFileVoPackage() {
        return fileVoPackage;
    }

    public void setFileVoPackage(String fileVoPackage) {
        this.fileVoPackage = fileVoPackage;
    }

    public String getFileMapperPath() {
        return fileMapperPath;
    }

    public void setFileMapperPath(String fileMapperPath) {
        this.fileMapperPath = fileMapperPath;
    }

    /**
     * @return the osType
     */
    public String getOsType() {
        return osType;
    }

    /**
     * @param osType the osType to set
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }

}
