package com.ninza.hrm.api.employeeandprojecttest;

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

public class OriginalEmployeeTest extends BaseApiClass {
	

	@Test
	public void addEmpoyeeTest() throws SQLException, IOException {

		String	Baseurl = fLib.getDataFromPropertiesFile("BASEUrl");

		String projectname = "abc" + jLib.getRandomNumber();
		String username = "suni" + jLib.getRandomNumber();
		// Api 1-->add a project inside server

		Project_Pojo pobj = new Project_Pojo(projectname, "Created", "suki", 0);
		given().contentType(ContentType.JSON).body(pobj).when().post(Baseurl + IEndPoint.AddProj).then()

				.log().all();

		// Api2-add employee to same project

		EmployeePojo emp = new EmployeePojo("Lead", "06/08/1995", "sunil123@gmail.com", username, 5, "9876543210",
				projectname, "QA", username);

		given().contentType(ContentType.JSON).body(emp).when().post(Baseurl + IEndPoint.AddEmp).then().assertThat()
				.statusCode(201).statusLine("HTTP/1.1 201 ").contentType(ContentType.JSON)
				.time(Matchers.lessThan(3000L))

				.log().all();

		// verify employee in DB

		dbLib.getDbconnection();
		boolean flag = dbLib.excecuteSelectQueryVerifyAndGetData("select * from employee", 11, username);
		Assert.assertTrue(flag, "employee in DB not verified");

		// conn.close();

		dbLib.closeConnection();

	}

	@Test
	public void addEmpoyeeWithoutEmailTest() throws SQLException, IOException {
//		Random random = new Random();
//		int rannum = random.nextInt(5000);
		String Baseurl = fLib.getDataFromPropertiesFile("BASEUrl");

		String projectname = "abc" +jLib.getRandomNumber();
		String username = "suni" + jLib.getRandomNumber();
		// Api 1-->add a project inside server
		Project_Pojo pobj = new Project_Pojo(projectname, "Created", "suki", 0);
		given().contentType(ContentType.JSON).body(pobj).when().post(Baseurl + IEndPoint.AddProj).then()

				.log().all();

		// Api2-add employee to same project

		EmployeePojo emp = new EmployeePojo("Lead", "06/08/1995", "", username, 5, "9876543210", projectname, "QA",
				username);

		given().contentType(ContentType.JSON).body(emp).when().post(Baseurl + IEndPoint.AddEmp).then().assertThat()
				.statusCode(500)

				.log().all();

	}
}
