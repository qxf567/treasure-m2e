package com.client;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.treasure.m2e.service.LogPosition;
import com.github.treasure.m2e.service.NotificationListener;
import com.google.code.or.OpenReplicator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-test.xml")
public class OpenReplicatorTest {
    public static void main(String args[]) throws Exception {

	  ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-test.xml");
	    
	  NotificationListener  lister= (NotificationListener) ac.getBean(NotificationListener.class);
	  	  LogPosition  ps= (LogPosition) ac.getBean(LogPosition.class);
//	  LevlDBPosition  ps= (LevlDBPosition) ac.getBean(LevlDBPosition.class);
	OpenReplicator or = new OpenReplicator();
	or.setUser("root");
	or.setPassword("123456");
	or.setHost("10.1.34.201");
	or.setPort(3306);
	// or.setServerId(17725);
	String re = ps.readPosition().trim();
	String names [] = re.split(",");
	or.setBinlogPosition(Long.valueOf(names[1]));
	or.setBinlogFileName(names[0]);
	
	or.setBinlogEventListener(lister);
	or.start();

	// final OpenParser op = new OpenParser();
	// op.setUser("root");
	// op.setPassword("123456");
	// op.setHost("10.1.34.201");
	// op.setPort(3306);
	// op.setServerId(17725);
	// op.setStartPosition(4);
	// op.setBinlogFileName("mysqlbin-log.000021");
	// op.setBinlogFilePath("/data/mysqldb");
	// op.setBinlogEventListener(new BinlogEventListener() {
	// public void onEvents(BinlogEventV4 event) {
	// if(event instanceof XidEvent) {
	// System.out.println(event);
	// }
	// }
	// });
	// op.start();

	//

    }
    @Test
    public void test() {
	OpenReplicator or = new OpenReplicator();
	or.setUser("root");
	or.setPassword("123456");
	or.setHost("10.1.34.201");
	or.setPort(3306);
	// or.setServerId(17725);
	// or.setBinlogPosition(104621073);
	or.setBinlogFileName("mysqlbin-log.000001");

	try {
	    or.start();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}