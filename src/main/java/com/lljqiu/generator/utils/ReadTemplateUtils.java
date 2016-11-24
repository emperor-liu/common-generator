package com.lljqiu.generator.utils;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class ReadTemplateUtils {
    public static final Integer DATESOURCECONFIG              = 1;

    /** dao 包路径 */
    public static final String  SPRING_DATESOURCE_DAO_PACKAGE = "daoPackage";
    /** xml路径*/
    public static final String  SPRING_DATESOURCE_XML_PACKAGE = "xmlPackage";

    public static final String  VM_DATE_SOURCE_CONFIG         = "template/spring-datasource.vm";
    public static final String  VM_DAO_TEMPLATE               = "template/dao-template.vm";
    public static final String  VM_MODEL_TEMPLATE             = "template/vo-template.vm";
    public static final String  VM_MYSQLDAO_TEMPLATE          = "template/mysqlxml-template.vm";
    public static final String  VM_ORACLEDAO_TEMPLATE         = "template/oraclexml-template.vm";
    public static final String  VM_DATASOURCE_TEMPLATE         = "template/datasource.vm";
    public static final String  VMENCODING                    = "UTF-8";
    private static Properties   vmprop                        = new Properties();
    //初始化
    static {
        vmprop.setProperty(Velocity.ENCODING_DEFAULT, VMENCODING);
        vmprop.setProperty(Velocity.INPUT_ENCODING, VMENCODING);
        vmprop.setProperty(Velocity.OUTPUT_ENCODING, VMENCODING);
        vmprop.setProperty(Velocity.RESOURCE_LOADER, "class");
        vmprop.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        try {
            Velocity.init(vmprop);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 
    * Description： 读取模板
    * @param templateFile
    * @param contextMap
    * @throws Exception
    * @return String
    * @author name：
     **/
    public static String getTemplateContent(String templateFile, Map<Object, Object> contextMap)
            throws Exception {
        if (StringUtils.isBlank(templateFile) || contextMap == null) {
            throw new Exception("参数[templateFile]或【contextMap】不能为空");
        }
        VelocityContext context = new VelocityContext(contextMap);
        StringWriter sw = new StringWriter();
        Template template = null;
        try {
            template = Velocity.getTemplate(templateFile);
            template.merge(context, sw);
        } catch (Exception e) {
            throw new Exception("read vm error");
        }
        return sw.toString();
    }

    enum VM_dao {
        voPackage,
        daoPackage,
        daoName,
        voName
    }

    enum VM_Model {
        voPackage,
        voName,
        serialVersion,
        fieldsContent,
        getterSetterContent
    }

    enum VM_Mysql {
        daoPackage,
        daoName,
        voPackage,
        voName,
        resultMapContent,
        tableName,
        fields,
        sequenceName,
        keyField,
        key,
        insertContent,
        updateContent
    }

    enum VM_Oracle {
        daoPackage,
        daoName,
        voPackage,
        voName,
        resultMapContent,
        tableName,
        fields,
        sequenceName,
        keyField,
        key,
        insertContent,
        updateContent
    }

    public static void main(String[] args) throws Exception {
        //        Map<String, Object> contextMap = new HashMap<String, Object>();
        //        contextMap.put(SPRING_DATESOURCE_XML_PACKAGE, "a");
        //        contextMap.put(SPRING_DATESOURCE_DAO_PACKAGE, "a");
        //        System.out.println(getTemplateContent(VM_DATE_SOURCE_CONFIG, contextMap));
        //        System.out.println(VM_dao.daoName);
        Map<Object, Object> contextMap = new HashMap<Object, Object>();
        contextMap.put(VM_dao.daoName.name(), "a");
        contextMap.put(VM_dao.daoPackage.name(), "b");
        contextMap.put(VM_dao.voPackage.name(), "bb");
        contextMap.put(VM_dao.voName.name(), "bbbbb");
        System.out.println(getTemplateContent(VM_DAO_TEMPLATE, contextMap));
    }
}
