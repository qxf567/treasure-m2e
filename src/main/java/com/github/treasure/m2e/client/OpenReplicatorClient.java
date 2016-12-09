package com.github.treasure.m2e.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.treasure.m2e.service.LogPosition;
import com.github.treasure.m2e.service.NotificationListener;
import com.google.code.or.OpenReplicator;

/**
 * 程序总入口
 * 
 * */
public class OpenReplicatorClient {

    public static void main(String args[]) throws Exception {
//	InputStream is = OpenReplicatorClient.class.getResourceAsStream("classpath:spring.xml");
	ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
	NotificationListener lister = (NotificationListener) ac.getBean(NotificationListener.class);
	LogPosition ps = (LogPosition) ac.getBean(LogPosition.class);
	// LevlDBPosition ps= (LevlDBPosition) ac.getBean(LevlDBPosition.class);
	OpenReplicator or = new OpenReplicator();
	or.setUser("root");
	or.setPassword("123456");
	or.setHost("10.1.34.201");
	or.setPort(3306);
	// or.setServerId(17725);
	String re = ps.readPosition().trim();
	String names[] = re.split(",");
	or.setBinlogPosition(Long.valueOf(names[1]));
	or.setBinlogFileName(names[0]);

	or.setBinlogEventListener(lister);
	or.start();
    }

}