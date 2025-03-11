package com.ninza.hrm.api.projecttest;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.sql.SQLException;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ninza.hrm.api.baseclass.BaseApiClass;
import com.ninza.hrm.api.pojoclass.Project_Pojo;
import com.ninza.hrm.constants.endpoints.IEndPoint;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ProjectTest extends BaseApiClass{
	
	

	Project_Pojo pobj;
	@Test
	public void addSingleProjectWithCreated() throws SQLException, IOException {

//		Random random = new Random(); 
//		int num = random.nextInt(5000);
		
	//	String Baseurl=fLib.getDataFromPropertiesFile("BASEUrl");
		
		String expMsg = "Successfully Added";
		String projectName = "abc" + jLib.getRandomNumber();

		// verification in API layer

		 pobj = new Project_Pojo(projectName, "Created", "suki", 0);
		Response resp = given()
				.spec(specReqObj)
				.body(pobj)
				.when()
				.post(IEndPoint.AddProj);
		resp.then()
		.assertThat().statusCode(201)
		
		.assertThat()
				.time(Matchers.lessThan(3000L))
				.spec(specRespObj)
				.log().all();
		String actualMsg = resp.jsonPath().get("msg");
		Assert.assertEquals(actualMsg, expMsg);

		// verify projectName in DB layer
		
		
//		boolean flag = false;
//		Driver driver = new Driver();
//		DriverManager.registerDriver(driver);
//		Connection conn = DriverManager.getConnection("jdbc:mysql://49.249.28.218:3333/ninza_hrm", "root", "root");
//		ResultSet result = conn.createStatement().executeQuery("select * from project");
		dbLib.getDbconnection();
		boolean flag=dbLib.excecuteSelectQueryVerifyAndGetData("select * from project", 4, projectName);
		/*
		 * while (result.next()) { if (result.getString(4).equals(projectName)) { flag =
		 * true; break; } }
		 */

		Assert.assertTrue(flag, "Project in DB not verified");
		//conn.close();
		
		// Assert.assertTrue(flag,"Project in DB not verified");
		
	}

	@Test(dependsOnMethods = "addSingleProjectWithCreated")
	public void createDuplicateProjectTest() throws IOException 
	{
		// String Baseurl=fLib.getDataFromPropertiesFile("BASEUrl");

		 given()
		 .spec(specReqObj)
			.body(pobj)
			.when()
			.post(IEndPoint.AddProj)
		 .then()
		 .assertThat().statusCode(409)
		 .spec(specRespObj)
		 .log().all();

		
	}

}
