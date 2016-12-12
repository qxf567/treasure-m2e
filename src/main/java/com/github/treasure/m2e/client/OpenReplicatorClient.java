package com.github.treasure.m2e.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.treasure.m2e.service.DBConfig;
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
	DBConfig config= (DBConfig) ac.getBean(DBConfig.class);
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
	or.start();
	      //批量操作时的处理
//     	EsOperator operator = (EsOperator) ac.getBean(EsOperator.class);
//	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//	executor.scheduleAtFixedRate(operator, 2, 5, TimeUnit.SECONDS);
    }

}