package com.lljqiu.generator.config;

import java.io.Serializable;
import java.util.List;

/** 
 * <p>文件名称: CreateBean.java</p>
 * 
 * <p>文件功能: CreateFiles config
 * 
 * <pre>
 *      createFiles config .
 * </pre>
 * </p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015-4-15 下午2:17:26</p>
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
public class CreateBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3916389652983297145L;
    private String            dateSourceUrl;
    private String            username;
    private String            password;
    private String            dbName;
    private String            dbType;                                  //数据库类型 mysql oracle db2 sql-server
    private String            sequenceName;
    private List<TableList>   tableList;
    private String            filePath;
    private String            filePackage;
    private String            fileVoPackage;
    private String            fileDaoPackage;
    private String            fileName;
    private ParserInfo        parserInfo;
    private String            fieldsContent;
    private String            getterSetterContent;
    private String            resultMapContent;
    private String            sqlColumn;
    private String            tableName;
    private String            fileMapperPath;
    private String osType;

    /** 
     * Description： mybatis xml InsterCount
     * @return String 
     * <pre>
     *   </if test="apiName != null and apiName != ''">
     *      api_name = #{apiName, javaType=String, jdbcType=VARCHAR},
     *   </if>
     *   </if test="apiExplain != null and apiExplain != ''">
     *      api_explain = #{apiExplain, javaType=String, jdbcType=VARCHAR}
     *   </if>
     * </pre>
     * @author name：lljqiu 
      **/
    public String getUpdateContent() {
        List<Field> fields = parserInfo.getFields();
        StringBuffer buffer = new StringBuffer();
        StringBuffer tmp = null;
        Field field = null;
        String updateRowContent = null;
        for (int i = 1; i < fields.size(); i++) {
            tmp = new StringBuffer();
            field = fields.get(i);
            updateRowContent = getUpdateRowContent(field);
            if(i == fields.size()-1){
                tmp.append(updateRowContent + "\n");
            } else {
                tmp.append(updateRowContent + ",\n");
            }
            updateRowContent = getUpdateIf(tmp.toString(), field);
            buffer.append(updateRowContent);
        }
        return buffer.toString();
    }

    private String getUpdateIf(String str, Field field) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\t\t<if test=\"" + field.getPropertyName() + " != null and "
                + field.getPropertyName() + " != ''\">\n");
        buffer.append("\t\t" + str);
        buffer.append("\t\t</if>\n");
        return buffer.toString();
    }

    private String getUpdateRowContent(Field field) {
        String template = "\t\t#<fieldName> = #{#<property>, javaType=#<javaType>, jdbcType=#<jdbcType>}";
        String result = template.replaceAll("#<fieldName>", field.getFieldName());
        result = result.replaceAll("#<property>", field.getPropertyName());
        result = result.replaceAll("#<javaType>", field.getJavaType());
        result = result.replaceAll("#<jdbcType>", Field.getJdbcType(field.getFieldType()));
        return result;
    }

    /** 
    * Description： mybatis xml InsterCount
    * @return String 
    * <pre>
    *   #{id, javaType=String, jdbcType=VARCHAR},
    *   #{name, javaType=String, jdbcType=VARCHAR},
    *   #{password, javaType=String, jdbcType=VARCHAR}
    * </pre>
    * @author name：lljqiu 
     **/
    public String getInsertContent() {
        List<Field> fields = parserInfo.getFields();
        StringBuffer buffer = new StringBuffer();
        Field field = fields.get(0);
        String insertRowContent = getInsertRowContent(field);
        buffer.append(insertRowContent);

        for (int i = 1; i < fields.size(); i++) {
            field = fields.get(i);
            insertRowContent = getInsertRowContent(field);
            buffer.append(",\n" + insertRowContent);
        }
        return buffer.toString();
    }

    /** 
    * Description：
    * @param field
    * @return  #{id, javaType=String, jdbcType=VARCHAR},
    * @author name：lljqiu
     **/
    private String getInsertRowContent(Field field) {
        String template = "\t\t#{#<property>, javaType=#<javaType>, jdbcType=#<jdbcType>}";
        String result = template.replaceAll("#<property>", field.getPropertyName());
        result = result.replaceAll("#<jdbcType>", Field.getJdbcType(field.getFieldType()));
        result = result.replaceAll("#<javaType>", field.getJavaType());
        return result;
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
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

    public List<TableList> getTableList() {
        return tableList;
    }

    public void setTableList(List<TableList> tableList) {
        this.tableList = tableList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePackage() {
        return filePackage;
    }

    public void setFilePackage(String filePackage) {
        this.filePackage = filePackage;
    }

    public ParserInfo getParserInfo() {
        return parserInfo;
    }

    public void setParserInfo(ParserInfo parserInfo) {
        this.parserInfo = parserInfo;
    }

    public String getFieldsContent() {
        return fieldsContent;
    }

    public void setFieldsContent(String fieldsContent) {
        this.fieldsContent = fieldsContent;
    }

    public String getGetterSetterContent() {
        return getterSetterContent;
    }

    public void setGetterSetterContent(String getterSetterContent) {
        this.getterSetterContent = getterSetterContent;
    }

    public String getResultMapContent() {
        return resultMapContent;
    }

    public void setResultMapContent(String resultMapContent) {
        this.resultMapContent = resultMapContent;
    }

    public String getSqlColumn() {
        return sqlColumn;
    }

    public void setSqlColumn(String sqlColumn) {
        this.sqlColumn = sqlColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileVoPackage() {
        return fileVoPackage;
    }

    public void setFileVoPackage(String fileVoPackage) {
        this.fileVoPackage = fileVoPackage;
    }

    public String getFileDaoPackage() {
        return fileDaoPackage;
    }

    public void setFileDaoPackage(String fileDaoPackage) {
        this.fileDaoPackage = fileDaoPackage;
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
