package com.whllow.community;

import com.whllow.community.dao.hlloDao;
import com.whllow.community.service.AppServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext ac;

//	@Autowired
//	private UserMapper userMapper;

//	@Test
//	public void testSelectId(){
//		User user = userMapper.selectById(101);
//		System.out.println(user);
//	}



	@Test
	void contextLoads() {
		System.out.println(ac);
		hlloDao hlloDao = ac.getBean(hlloDao.class);
		System.out.println(hlloDao.select());

		hlloDao  = ac.getBean("MyBaits",hlloDao.class);
		System.out.println(hlloDao.select());

	}

	@Test
	public void test1(){
		AppServer as1 = ac.getBean(AppServer.class);
		AppServer as2 = ac.getBean(AppServer.class);
		System.out.println(as1==as2);
	}

	@Test
	public void testBean(){
		SimpleDateFormat simpleDateFormat = ac.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	@Qualifier("MyBaits")
	private hlloDao hlloDao;

	@Autowired
	private  SimpleDateFormat simpleDateFormat;

	@Autowired
	private AppServer appServer;

	@Test
	public void testAuto(){
		System.out.println(hlloDao.select());
		System.out.println(simpleDateFormat.format(new Date()));
		System.out.println(appServer);
	}



	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ac = applicationContext;
	}
}
