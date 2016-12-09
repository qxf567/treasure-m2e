package com.github.treasure.m2e.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.treasure.m2e.model.LogEvent;
import com.github.treasure.m2e.model.TableInfo;
import com.github.treasure.m2e.utils.DBKeyCovert;
import com.github.treasure.m2e.utils.ObjectUtil;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;
import com.google.code.or.binlog.BinlogEventV4Header;
import com.google.code.or.binlog.impl.event.DeleteRowsEventV2;
import com.google.code.or.binlog.impl.event.TableMapEvent;
import com.google.code.or.binlog.impl.event.UpdateRowsEventV2;
import com.google.code.or.common.glossary.Column;
import com.google.code.or.common.glossary.Pair;
import com.google.code.or.common.glossary.Row;
import com.google.code.or.common.glossary.column.BlobColumn;
import com.google.code.or.common.glossary.column.StringColumn;
import com.lashou.treasure.es.entity.ESOrdersDetailEntity;
import com.lashou.treasure.es.entity.ESOrdersEntity;
import com.lashou.treasure.es.entity.ProductEntity;
import com.lashou.treasure.es.entity.TreasureTermEntity;

/**
 * 
 * 将binlog数据异步到elasticsearch
 * 
 */

@Component
public class NotificationListener implements BinlogEventListener {

    private static Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private String databaseName = "treasure_online&";

    Map<Long, String> tableMap = new HashMap<Long, String>();

    @Autowired
    private EsService esService;
    @Autowired
    private LogPosition posService;
    @Autowired
    private DBConfig dbconfig;

    // @Autowired
    // private LevlDBPosition posService;

    public void onEvents(BinlogEventV4 event) {

	if (event == null) {
	    logger.error("binlog event is null");
	    return;
	}

	if (event instanceof TableMapEvent) {
	    TableMapEvent tableMapEvent = (TableMapEvent) event;
	    StringColumn tableName = tableMapEvent.getTableName();
	    StringColumn datbaseName = tableMapEvent.getDatabaseName();
	    Long tableId = tableMapEvent.getTableId();
	    // 只处理夺宝库
	    if (datbaseName.toString().indexOf("treasure") > -1) {
		if (!tableMap.containsKey(tableId)) {
		    tableMap.put(tableId, datbaseName + "&" + tableName);
		}
	    }
	} else if (event instanceof UpdateRowsEventV2) {
	    UpdateRowsEventV2 updateRowsEvent = (UpdateRowsEventV2) event;
	    Long tableId = updateRowsEvent.getTableId();
	    if (!tableMap.containsKey(tableId)) {
		return;
	    }
	    try {
		String databaseTable = tableMap.get(tableId);
		String[] dt = databaseTable.split("&");
		LogEvent logEvent = new LogEvent(updateRowsEvent);
		List<Pair<Row>> rows = updateRowsEvent.getRows();
		List<Column> cols_after = null;
		List<Column> cols_before = null;
		for (Pair<Row> p : rows) {
		    Row after = p.getAfter();
		    Row before = p.getBefore();
		    cols_after = after.getColumns();
		    cols_before = before.getColumns();
		    logEvent.setBefore(getMap(cols_before, dt[0], dt[1]));
		    logEvent.setAfter(getMap(cols_after, dt[0], dt[1]));
		}
		Object o = null;
		if ((databaseName + "product").equals(databaseTable)) {
		    ProductEntity pEntity = new ProductEntity();
		    o = map2Object(logEvent, pEntity);
		} else if ((databaseName + "treasure_term").equals(databaseTable)) {
		    TreasureTermEntity term = new TreasureTermEntity();
		    map2Object(logEvent, term);
		    o = map2Object(logEvent, term);
		} else if (StringUtils.endsWith(databaseTable, "orders")) {
		    ESOrdersEntity orders = new ESOrdersEntity();
		    o = map2Object(logEvent, orders);
		} else if (StringUtils.endsWith(databaseTable, "orders_detail")) {
		    ESOrdersDetailEntity od = new ESOrdersDetailEntity();
		    o = map2Object(logEvent, od);
		}

		esService.serviceSave(dt[1], o);
		System.out.println("UpdateRowsEventV2--logEvent after:" + o);
	    } catch (Exception e) {
		System.out.println("error -----------------" + e);
		e.printStackTrace();
	    }
	} else if (event instanceof DeleteRowsEventV2) {
	    DeleteRowsEventV2 deleteRowsEvent = (DeleteRowsEventV2) event;
	    Long tableId = deleteRowsEvent.getTableId();
	    if (!tableMap.containsKey(tableId)) {
		return;
	    }
	    String databaseTable = tableMap.get(tableId);
	    String[] dt = databaseTable.split("&");
	    List<Row> rows = deleteRowsEvent.getRows();
	    Row row = rows.get(0);
	    Column col = row.getColumns().get(0);
	    esService.serviceDelete(dt[1], (Long) col.getValue());
	}
	BinlogEventV4Header header = event.getHeader();
	System.out.println(header.getPosition() + "," + header.getNextPosition());
	posService.writePosition(header.getBinlogFileName(), header.getPosition());
    }

    private Object map2Object(LogEvent logEvent, Object object) {
	Map<String, String> map2 = new HashMap<String, String>();
	Set<String> keys = logEvent.getAfter().keySet();
	for (String key : keys) {
	    String v = logEvent.getAfter().get(key);
	    String key2 = DBKeyCovert.convertToJava(key);
	    map2.put(key2, v);
	}

	try {
	    ObjectUtil.map2Object(map2, object);
	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error("map 2 object error:" + e);
	}
	return object;
    }

    private Map<String, String> getMap(List<Column> cols, String databaseName, String tableName) {
	if (cols == null || cols == null) {
	    return null;
	}
	List<String> columnNames = null;
	try {
	    columnNames = new TableInfo(dbconfig.getHost(), dbconfig.getUserName(), dbconfig.getPasswd(), dbconfig.getPort())
		    .getColumns(databaseName, tableName);
	} catch (Exception e) {
	    logger.error("tableInfo error:" + e);
	}
	if (columnNames == null) {
	    return null;
	}
	if (columnNames.size() != cols.size()) {
	    logger.error("the size does not match...");
	    return null;
	}
	Map<String, String> map = new HashMap<String, String>();
	for (int i = 0; i < columnNames.size(); i++) {
	    // if(afterCols.get(i).getValue()==null ||
	    // afterCols.get(i).getValue().equals(beforeCols.get(i).getValue())){
	    // continue;
	    // }
	    if (cols.get(i).getValue() == null) {
		map.put(columnNames.get(i).toString(), "");
	    } else {
		Column col = cols.get(i);
		if (col instanceof BlobColumn) {
		    BlobColumn c = (BlobColumn) col;
		    map.put(columnNames.get(i).toString(), new String(c.getValue()));
		} else {
		    map.put(columnNames.get(i).toString(), cols.get(i).toString());
		}

	    }
	}
	return map;
    }
}