package com.ninza.hrm.api.employeetest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ninza.hrm.api.baseclass.BaseApiClass;
import com.ninza.hrm.api.pojoclass.EmployeePojo;
import com.ninza.hrm.api.pojoclass.Project_Pojo;
import com.ninza.hrm.constants.endpoints.IEndPoint;

import io.restassured.http.ContentType;

public class EmployeeTest extends BaseApiClass{
	

	
	
	
	
	@Test
	public void addEmpoyeeTest() throws SQLException, IOException {
//		Random random = new Random();
//		int rannum = random.nextInt(5000);
		String	Baseurl=fLib.getDataFromPropertiesFile("BASEUrl");
		
		 
		String projectname = "abc" + jLib.getRandomNumber();
		String username = "suni" + jLib.getRandomNumber();
		// Api 1-->add a project inside server
		Project_Pojo pobj = new Project_Pojo(projectname, "Created", "suki", 0);
		given()
		.spec(specReqObj)
		.body(pobj)
		.when()
		.post(IEndPoint.AddProj)
		.then()
         .spec(specRespObj)
				.log().all();

		// capture the projectId from the response
//		String pname = resp.jsonPath().get("projectName");
//		System.out.println(pname);

		// Api2-add employee to same project

//String designation, String dob, String email, String empName, double experience,
//			String mobileNo, String project, String role, String username) 

		EmployeePojo emp = new EmployeePojo("Lead", "06/08/1995", "sunil123@gmail.com", username, 5, "9876543210",
				projectname, "QA", username);

		given()
		.spec(specReqObj)
		.body(emp)
		.when()
		.post(IEndPoint.AddEmp)
		.then()
				.assertThat().statusCode(201).statusLine("HTTP/1.1 201 ")
				
				.time(Matchers.lessThan(3000L))
                 .spec(specRespObj)
				.log().all();

		// verify employee in DB
		//boolean flag = false;
//		Driver driver = new Driver();
//		DriverManager.registerDriver(driver);
//		Connection conn = DriverManager.getConnection(dburl, "root", "root");
		
		
//		ResultSet result = conn.createStatement().executeQuery("select * from employee");
		boolean flag = dbLib.excecuteSelectQueryVerifyAndGetData("select * from employee", 11, username);
		/*
		 * while (result.next()) { if (result.getString(11).equals(username)) { flag =
		 * true; break; } }
		 */
		

		Assert.assertTrue(flag, "employee in DB not verified");
		//conn.close();
		

	}

	@Test
	public void addEmpoyeeWithoutEmailTest() throws SQLException, IOException {
//		Random random = new Random();
//		int rannum = random.nextInt(5000);
		String	Baseurl=fLib.getDataFromPropertiesFile("BASEUrl");

	
		String projectname = "abc" + jLib.getRandomNumber();
		String username = "suni" + jLib.getRandomNumber();
		// Api 1-->add a project inside server
		Project_Pojo pobj = new Project_Pojo(projectname, "Created", "suki", 0);
		given()
		.spec(specReqObj)
		.body(pobj)
		.when()
		.post(IEndPoint.AddProj)
		.then()
         .spec(specRespObj)
				.log().all();

		// capture the projectId from the response
//		String pname = resp.jsonPath().get("projectName");
//		System.out.println(pname);

		// Api2-add employee to same project

//String designation, String dob, String email, String empName, double experience,
//			String mobileNo, String project, String role, String username) 

		EmployeePojo emp = new EmployeePojo("Lead", "06/08/1995", "", username, 5, "9876543210", projectname, "QA",
				username);

		given()
		.spec(specReqObj)
		.body(emp)
		.when()
		.post(IEndPoint.AddEmp)
		.then()
				.assertThat().statusCode(500)
                .spec(specRespObj)
				.log().all();

	}
	
}
