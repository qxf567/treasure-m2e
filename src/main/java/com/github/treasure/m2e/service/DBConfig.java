package com.github.treasure.m2e.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConfig {
    @Value("${treasure.m2e.host}")
    private String host;
    @Value("${treasure.m2e.port}")
    private Integer port;
    @Value("${treasure.m2e.username}")
    private String userName;
    @Value("${treasure.m2e.passwd}")
    private String passwd;

    public String getHost() {
	return host;
    }

    public void setHost(String host) {
	this.host = host;
    }

    public Integer getPort() {
	return port;
    }

    public void setPort(Integer port) {
	this.port = port;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPasswd() {
	return passwd;
    }

    public void setPasswd(String passwd) {
	this.passwd = passwd;
    }

}
