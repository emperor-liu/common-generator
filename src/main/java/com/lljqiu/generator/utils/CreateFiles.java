package com.lljqiu.generator.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lljqiu.generator.config.CreateBean;
import com.lljqiu.generator.config.Field;
import com.lljqiu.generator.config.ParserInfo;
import com.lljqiu.generator.utils.ReadTemplateUtils.VM_Model;
import com.lljqiu.generator.utils.ReadTemplateUtils.VM_Mysql;
import com.lljqiu.generator.utils.ReadTemplateUtils.VM_dao;

/** 
 * <p>文件名称: CreateFiles.java</p>
 * 
 * <p>文件功能: 创建 xml dao bean等文件</p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015-4-14 下午3:59:36</p>
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
public class CreateFiles {

    private static final String DEFAULT_ENCODEING = "UTF-8";
    private static Logger       log               = LoggerFactory.getLogger(CreateFiles.class);

    /** 
    * Description： create interfaceDao 
    * @param createBean {@link CreateBean}
    * @return void
    * @author name：lljqiu
     **/
    public static void createDao(CreateBean createBean) {
        try {
            log.debug("create interface【{}】 dao start", createBean.getFileName());
            Map<Object, Object> contextMap = new HashMap<Object, Object>();
            contextMap.put(VM_dao.voPackage.name(), createBean.getFileVoPackage());
            contextMap.put(VM_dao.voName.name(), createBean.getFileName() + "DO");
            contextMap.put(VM_dao.daoPackage.name(), createBean.getFileDaoPackage());
            contextMap.put(VM_dao.daoName.name(), createBean.getFileName() + "Dao");
            String path = getFillePath(createBean)+ "dao/"+createBean.getFileName() +"Dao.java";
            
            createFile(path, ReadTemplateUtils.VM_DAO_TEMPLATE, contextMap);
            log.debug("create interface【{}】 dao SUCCESS", createBean.getFileName());
        } catch (Exception e) {
            throw new DMException("create interface eror", e);
        }

    }

    /** 
     * Description：create &*do.java
     * @param createBean {@link CreateBean}
     * @return void
     * @author name： lljqiu
      **/
    public static void createBean(CreateBean createBean) {
        try {
            log.debug("create vobean name 【{}】 start", createBean.getFileName());
            Map<Object, Object> contextMap = new HashMap<Object, Object>();
            contextMap.put(VM_Model.voPackage.name(), createBean.getFileVoPackage());
            contextMap.put(VM_Model.voName.name(), createBean.getFileName() + "DO");
            contextMap.put(VM_Model.serialVersion.name(), "1");
            contextMap.put(VM_Model.fieldsContent.name(), createBean.getFieldsContent());
            contextMap
                    .put(VM_Model.getterSetterContent.name(), createBean.getGetterSetterContent());
            String path = getFillePath(createBean)+ "dataobject/"+createBean.getFileName() +"DO.java";
            createFile(path, ReadTemplateUtils.VM_MODEL_TEMPLATE, contextMap);
            log.debug("create vobean name 【{}】 SUCCESS", createBean.getFileName());
        } catch (Exception e) {
            throw new DMException("create vobean error", e);
        }
    }

    /** 
     * Description：create*Dao-mybatis xml
     * @param createBean {@link CreateBean}
     * @return void
     * @author name： lljqiu
      **/
    public static void createMyBatisXml(CreateBean createBean) {
        try {
            log.debug("create MyBatisXml name 【{}】 start", createBean.getFileName());
            Map<Object, Object> contextMap = new HashMap<Object, Object>();
            contextMap.put(VM_Mysql.daoPackage.name(), createBean.getFileDaoPackage());
            contextMap.put(VM_Mysql.daoName.name(), createBean.getFileName() + "Dao");
            contextMap.put(VM_Mysql.voPackage.name(), createBean.getFileVoPackage());
            contextMap.put(VM_Mysql.voName.name(), createBean.getFileName() + "DO");
            contextMap.put(VM_Mysql.resultMapContent.name(), createBean.getResultMapContent());
            contextMap.put(VM_Mysql.fields.name(), createBean.getSqlColumn());
            contextMap.put(VM_Mysql.tableName.name(), createBean.getTableName());
            contextMap.put(VM_Mysql.sequenceName.name(), createBean.getParserInfo()
                    .getSequenceName());

            Field keyField = getKeyField(createBean.getParserInfo());
            if (keyField != null) {
                contextMap.put(VM_Mysql.keyField.name(), keyField.getFieldName());
                contextMap.put(VM_Mysql.key.name(), keyField.getPropertyName());
            }

            contextMap.put(VM_Mysql.insertContent.name(), createBean.getInsertContent());
            contextMap.put(VM_Mysql.updateContent.name(), createBean.getUpdateContent());
            String path = getFillePath(createBean)+ "mapper/"+createBean.getFileName() +"Dao.xml";
            if (createBean.getDbType().equalsIgnoreCase("mysql")) {
                createFile(path, ReadTemplateUtils.VM_MYSQLDAO_TEMPLATE, contextMap);
            } else if (createBean.getDbType().equalsIgnoreCase("oracle")) {
                createFile(path, ReadTemplateUtils.VM_ORACLEDAO_TEMPLATE, contextMap);
            }
            log.debug("create MyBatisXml name【{}】  SUCCESS", createBean.getFileName());
        } catch (Exception e) {
            throw new DMException("create MyBatisXml error", e);
        }
    }

    private static Field getKeyField(ParserInfo parserInfo) {
        List<Field> fields = parserInfo.getFields();
        for (Field field : fields) {
            if (field.isKey()) {
                return field;
            }
        }
        return null;
    }

    /** 
    * Description：create spring-datesource.xml
    * @param createBean {@link CreateBean}
    * @return void
    * @author name：
     **/
    public static void createDataSourceConfig(CreateBean createBean) {
        try {
            log.debug(
                    "create spring-datesource.xml sessionFactory mapperLocations【{}】 MapperScannerConfigurer basePackage【{}】",
                    createBean.getFilePackage(), createBean.getFilePackage());
            log.debug("create xml path 【{}】", createBean.getFilePackage());
            Map<Object, Object> contextMap = new HashMap<Object, Object>();
            contextMap.put(ReadTemplateUtils.SPRING_DATESOURCE_XML_PACKAGE, createBean
                    .getFilePackage().replaceAll("\\.", "/"));
            contextMap.put(ReadTemplateUtils.SPRING_DATESOURCE_DAO_PACKAGE,
                    createBean.getFilePackage());

            String path = getFillePath(createBean)+ "spring-datesource.xml";
            createFile(path, ReadTemplateUtils.VM_DATE_SOURCE_CONFIG, contextMap);
            log.debug("create spring-datesource.xml SUCCESS");
        } catch (Exception e) {
            throw new DMException("create spring-datesource.xml error", e);
        }
    }

    public static void createDataSource(CreateBean createBean) {
        try {
            log.debug("create datesource.pro ");
            log.debug("create xml path 【{}】", createBean.getFilePackage());
            Map<Object, Object> contextMap = new HashMap<Object, Object>();
            if(createBean.getDbType().equalsIgnoreCase("mysql")){
                contextMap.put("driver", "com.mysql.jdbc.Driver");
            }else if(createBean.getDbType().equalsIgnoreCase("oracle")){
                contextMap.put("driver", "oracle.jdbc.OracleDriver");
            }
            contextMap.put("url", createBean.getDateSourceUrl());
            contextMap.put("username",createBean.getUsername());
            contextMap.put("password",createBean.getPassword());

            String path = getFillePath(createBean)+ "datasource.properties";
//            if(createBean.getOsType().equalsIgnoreCase("mac") || createBean.getOsType().equalsIgnoreCase("linux")){
//            	path = createBean.getFilePath()+"/"
//            			+ createBean.getFilePackage().replaceAll("\\.", "/") + "/datasource.properties";
//            }else{
//            	 path = createBean.getFilePath()+"\\"
//                       + createBean.getFilePackage().replaceAll("\\.", "\\\\") + "\\datasource.properties";
//            }
            createFile(path, ReadTemplateUtils.VM_DATASOURCE_TEMPLATE, contextMap);
            log.debug("create spring-datesource.xml SUCCESS");
        } catch (Exception e) {
            throw new DMException("create spring-datesource.xml error", e);
        }
    }
    
    /** 
     * Description：获取文件路径
     * @param createBean
     * @throws Exception
     * @return String
     * @author name：liujie <br>email: jie_liu1@asdc.com.cn
     **/
    private static String getFillePath(CreateBean createBean) throws Exception {
        String path = null;
        if(createBean.getOsType().equalsIgnoreCase("mac") || createBean.getOsType().equalsIgnoreCase("linux")){
            path = createBean.getFilePath()+"/"
                    + createBean.getFilePackage().replaceAll("\\.", "/") + "/";
        }else{
             path = createBean.getFilePath()+"\\"
                   + createBean.getFilePackage().replaceAll("\\.", "\\\\") + "\\";
        }
        return path;
    }

    /** 
    * Description：
    * @param path  文件存放路径
    * @param vm_type 选取的模板标识
    * @param map 相应的数值
    * @throws Exception
    * @return void
    * @author name：lljqiu
     **/
    private static void createFile(String path, String vm_type, Map<Object, Object> map)
            throws Exception {
        log.debug("file Path 【{}】", path);
        File file = new File(path);
        file.getParentFile().mkdirs();

        //        FileUtil.mkdir(path);
        FileOutputStream os = new FileOutputStream(file);
        os.write(ReadTemplateUtils.getTemplateContent(vm_type, map).getBytes(DEFAULT_ENCODEING));
        os.flush();
        os.close();

    }

}
