package com.suntek.scheduler.test;

import com.suntek.scheduler.ui.PSService;
import com.suntek.scheduler.util.SecurityUtil;

import junit.framework.TestCase;

public class AllTests extends TestCase {

	public void setup(){
		PSService.getService().connect();
	}
	
	public void testEncryption(){
		String name = "Ang-Bobo";		
		String cryptName = SecurityUtil.encrypt(name);
		System.out.println(name+" = "+cryptName);
		assertEquals(name, SecurityUtil.decrypt(cryptName));

		String name1 = "Rajiveramandamontartey";		
		String cryptName1 = SecurityUtil.encrypt(name1);
		System.out.println(name1+" = "+cryptName1);
		assertEquals(name1, SecurityUtil.decrypt(cryptName1));

		
		String ssn = "612836574";
		String cryptSSN = SecurityUtil.encrypt(ssn);
		System.out.println(ssn+" = "+cryptSSN);
		assertEquals(ssn, SecurityUtil.decrypt(cryptSSN));		
	}			
	
	public void testDecryption(){
		String txt = "Ut2b3SUL+ajVynicpm+uaw==";
		String cryptName = SecurityUtil.decrypt(txt);
		System.out.println(txt+"="+cryptName);
	}
}
