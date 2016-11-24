package com.lljqiu.generator.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParserInfo {

	// #<tableName>
	private String tableName;
	// #<tableComment>
	private String tableComment;
	// #<sequenceName>
	private String sequenceName;

	private List<Field> fields;

	/**
	 * 取得数据库表的列信息
	 * @param connection  链接信息
	 * @param tableName 表明
	 * @param sequenceName 
	 * @throws Exception
	 */
	public ParserInfo(Connection connection, String tableName, String sequenceName) throws Exception {
	    this.tableName = tableName;
		fields = new ArrayList<Field>();
		if (sequenceName == null) {
			sequenceName = tableName + "_S";
		}
		this.sequenceName = sequenceName;
//		String javaName = getJavaName(tableName);

		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
		ResultSetMetaData metaData = rs.getMetaData();
		DatabaseMetaData dbMetaData = connection.getMetaData();
		int columnCount = metaData.getColumnCount();
		String primaryKey = null;
		ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
		if (primaryKeys.next()) {
			primaryKey = primaryKeys.getString(4);
		}
		ResultSet tables = dbMetaData.getTables(null, null, tableName, null);
		if (tables.next()) {
			tableComment = tables.getString(5);
			if (tableComment == null) {
				tableComment = "";
			}
		}
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			String columnName = metaData.getColumnName(columnIndex);
			String columnTypeName = metaData.getColumnTypeName(columnIndex);
			ResultSet column = dbMetaData.getColumns(null, null, tableName, columnName);
			column.next();
			String description = column.getString(12);
			String type = columnTypeName.toUpperCase();
			int index = type.indexOf('(');
			if (index != -1) {
				type = type.substring(0, index);
			}
			int scale = metaData.getScale(columnIndex);
			int precision = metaData.getPrecision(columnIndex);
			Field field = new Field(columnName, type, getPropertyKeyByColumnName(columnName), columnName.equals(primaryKey), precision, scale);
			field.setFieldName(columnName);
			if ("NO".equals(column.getString(18))) {
				field.setMandatory(true);
			}
			field.setDescription(description);
			fields.add(field);
		}
		rs.close();
		statement.close();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}


	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	private String getPropertyKeyByColumnName(String columnName) {
		String[] split = columnName.split("_");
		StringBuffer buffer = new StringBuffer();
		buffer.append(split[0].toLowerCase());
		for (int i = 1; i < split.length; i++) {
			String lowerCaseSplit = split[i].toLowerCase();
			String firstUpperChar = (lowerCaseSplit.charAt(0) + "").toUpperCase();
			buffer.append(firstUpperChar + lowerCaseSplit.substring(1));
		}
		return buffer.toString();
	}
}
