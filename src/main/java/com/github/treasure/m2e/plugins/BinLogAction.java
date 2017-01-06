package com.github.treasure.m2e.plugins;

import java.util.concurrent.atomic.AtomicBoolean;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.Table;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.cat.AbstractCatAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.treasure.m2e.service.DBConfig;
import com.github.treasure.m2e.service.LogPosition;
import com.github.treasure.m2e.service.NotificationListener;
import com.google.code.or.OpenReplicator;

public class BinLogAction extends AbstractCatAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinLogAction.class);
    private AtomicBoolean count = new AtomicBoolean(true);
    private ApplicationContext ac;
    @Inject
    public BinLogAction(Settings settings, RestController controller, Client client) {
	super(settings, controller, client);
	if (this.count.get()) {
	    LOGGER.info("enter...BinLogAction..........");
	    controller.registerHandler(RestRequest.Method.GET, "/_cat/binlog", this);
	    this.count.set(false);
	    ac = new ClassPathXmlApplicationContext("spring.xml");
	} else {
	    LOGGER.info("count is > 0");
	}
    }

    @Override
    protected void doRequest(RestRequest request, RestChannel channel, Client client) {
	RestResponse response = new BytesRestResponse(RestStatus.OK, "SUCCESS");
	channel.sendResponse(response);
	try {
	    NotificationListener lister = (NotificationListener) ac.getBean(NotificationListener.class);
	    LogPosition ps = (LogPosition) ac.getBean(LogPosition.class);
	    DBConfig config = (DBConfig) ac.getBean(DBConfig.class);
	    OpenReplicator or = new OpenReplicator();
	    or.setUser(config.getUserName());
	    or.setPassword(config.getPasswd());
	    or.setHost(config.getHost());
	    or.setPort(config.getPort());
	    // or.setServerId(17725);
	    String re = ps.readPosition().trim();
	    String names[] = re.split(",");
	    or.setBinlogPosition(Long.valueOf(names[1]));
	    or.setBinlogFileName(names[0]);

	    or.setBinlogEventListener(lister);
	    try {
		or.start();
	    } catch (Exception e) {
		LOGGER.error("error",e);
		e.printStackTrace();
	    }

	} catch (Throwable e) {
	    try {
		channel.sendResponse(new BytesRestResponse(channel, e));
	    } catch (Throwable e1) {
		LOGGER.error("failed to send failure response", e1, new Object[0]);
	    }
	}
    }

    protected void documentation(StringBuilder sb) {
	sb.append(documentation());
    }

    public static String documentation() {
	return "/_cat/binlog";
    }

    protected Table getTableWithHeader(RestRequest request) {
	Table table = new Table();
	table.startHeaders();
	table.addCell("test111", "desc:test8888888888888");
	table.endHeaders();
	return table;
    }
}
