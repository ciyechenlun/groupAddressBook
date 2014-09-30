package com.cmcc.zysoft.groupaddressbook.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmcc.zysoft.sellmanager.model.UserCompany;
public class UserCompanyServiceTest extends BaseJunitTest{
	 @Autowired	
	private UserCompanyService ucService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAdd() {
		UserCompany uc =new UserCompany();
		uc.setCompanyId("003");
		uc.setEmployeeName("张四");
		uc.setMobile("15689451526");
		uc.setDelFlag("0");
		uc.setDisplayOrder(111);
		String departmentId = "ff8080813f14599e013f14974c10016d";
		String headshipId="4028814f4494e849014495343fdf000f";
		String gridNumber = "8899";
		String result = ucService.add(uc, departmentId, headshipId, gridNumber);
		assertEquals("正确", "SAME", result);
		List<UserCompany> list = this.ucService.findByNamedParam("mobile", "15689451526");
		assertNotNull(list);
	}

}
