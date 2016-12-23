package com.github.treasure.m2e.plugin;

import java.util.concurrent.TimeUnit;

import org.elasticsearch.river.AbstractRiverComponent;
import org.elasticsearch.river.River;
import org.elasticsearch.river.RiverName;
import org.elasticsearch.river.RiverSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.treasure.m2e.service.DBConfig;
import com.github.treasure.m2e.service.LogPosition;
import com.github.treasure.m2e.service.NotificationListener;
import com.google.code.or.OpenReplicator;

public class BinRiver extends AbstractRiverComponent implements River{

    OpenReplicator or = new OpenReplicator();
    private ApplicationContext ac;
    protected BinRiver(RiverName riverName, RiverSettings settings) {
	super(riverName, settings);
	ac = new ClassPathXmlApplicationContext("spring.xml");
    }

    @Override
    public void start() {
	NotificationListener lister = (NotificationListener) ac.getBean(NotificationListener.class);
	LogPosition ps = (LogPosition) ac.getBean(LogPosition.class);
	DBConfig config= (DBConfig) ac.getBean(DBConfig.class);
	
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
	    e.printStackTrace();
	}
    }

    @Override
    public void close() {
	or.stopQuietly(3000, TimeUnit.SECONDS);
	
    }

}
