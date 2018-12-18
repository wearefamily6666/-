package com.imooc.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import com.imooc.service.impl.ChatGroupServiceImpl;

public class ChatGroupServiceTest {
	
	@Test
	public void testGetTime() {
		ChatGroupServiceImpl chatGroupServiceImpl=new ChatGroupServiceImpl();
		try {
		System.out.println("时间："+	chatGroupServiceImpl.getTime(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
