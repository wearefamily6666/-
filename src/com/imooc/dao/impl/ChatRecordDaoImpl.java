package com.imooc.dao.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.imooc.dao.IChatRecordDao;
import com.imooc.pojo.Chat_Record;
/**
 * 群聊记录dao实现类
 * @author Administrator
 *
 */

public class ChatRecordDaoImpl extends HibernateDaoSupport implements IChatRecordDao {

	//插入数据
	@Override
	public void save(Chat_Record chat_Record) {
		this.getHibernateTemplate().save(chat_Record);
	}

}
