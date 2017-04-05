package org.frameworkset.spi.remote.dubbo;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

public class Consumer {
	@Test
	public void test(){
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("dubbo-consumer.xml");
		TestProviderInf testProviderInf = context.getTBeanObject("testservice", TestProviderInf.class);
		System.out.println(testProviderInf.test("hi,dubbo!"));
	}

}
