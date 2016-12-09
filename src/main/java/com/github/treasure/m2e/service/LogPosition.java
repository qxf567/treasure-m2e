package com.github.treasure.m2e.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

/** 利用本地文件实现记录binlog读取的位置记录 */
@Service
public class LogPosition {

   
    public void writePosition(String logName, Long pos) {
	try {
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("LOCK_POS"));
	    bos.write((logName + "," + pos).getBytes());
	    bos.flush();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}finally{
	    
	}
    }

    public String readPosition() {
	try {
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream("LOCK_POS"));
	    byte[] b = new byte[100];
	    bis.read(b);
	    return new String(b);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }



}
