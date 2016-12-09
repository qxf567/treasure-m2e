package com.github.treasure.m2e.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableInfo {
	private static Logger logger = LoggerFactory.getLogger(TableInfo.class);
	
	/**
	 * key:databaseName+""+tableName
	 * value:columns name
	 */
	private static Map<String, List<String>> columnsMap = new HashMap<String, List<String>>();
	private String host;
	private Integer port;
	private String username;
	private String password;
	
	public TableInfo(String host,String username,String password,Integer port){
		this.host=host;
		this.username=username;
		this.password=password;
		this.port = port;
		if(columnsMap==null||columnsMap.size()==0){
			MysqlConnection.setConnection(this.host,this.port,this.username,this.password);
		    columnsMap = MysqlConnection.getColumns();
		}
	}
	
	public Map<String, List<String>> getMap(){
		return columnsMap;
	}
	
	public List<String> getColumns(String databaseName,String tableName){
		if(StringUtils.isEmpty(databaseName)||StringUtils.isEmpty(tableName)){
			return null;
		}
		String key = databaseName + "."+tableName;
		List<String> list =null;
		if(columnsMap.size()==0){
			MysqlConnection.setConnection(this.host,this.port,this.username,this.password);
		    columnsMap = MysqlConnection.getColumns();
			list = columnsMap.get(key);
		}else{
			list=columnsMap.get(key);
			if(list==null||list.size()==0){
				MysqlConnection.setConnection(this.host,this.port,this.username,this.password);
				columnsMap = MysqlConnection.getColumns();
				list = columnsMap.get(key);
			}
			
		}
		return list;
	}
}