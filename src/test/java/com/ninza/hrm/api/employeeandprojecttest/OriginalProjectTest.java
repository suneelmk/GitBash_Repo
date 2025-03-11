package com.ninza.hrm.api.employeeandprojecttest;

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

public class OriginalProjectTest extends BaseApiClass {

	

	Project_Pojo pobj;

	@Test
	public void addSingleProjectWithCreated() throws SQLException, IOException {

		String Baseurl = fLib.getDataFromPropertiesFile("BASEUrl");

		String expMsg = "Successfully Added";
		String projectName = "abc" + jLib.getRandomNumber();

		// verification in API layer

		pobj = new Project_Pojo(projectName, "Created", "suki", 0);
		Response resp = given().contentType(ContentType.JSON).body(pobj).when().post(Baseurl + IEndPoint.AddProj);
		resp.then().assertThat().statusCode(201).assertThat().contentType(ContentType.JSON).assertThat()
				.time(Matchers.lessThan(3000L)).log().all();
		String actualMsg = resp.jsonPath().get("msg");
		Assert.assertEquals(actualMsg, expMsg);

		// verify projectName in DB layer

		dbLib.getDbconnection();

		boolean flag = dbLib.excecuteSelectQueryVerifyAndGetData("select * from project", 4, projectName);

		Assert.assertTrue(flag, "Project in DB not verified");
		// conn.close();

		dbLib.closeConnection();

	}

	@Test(dependsOnMethods = "addSingleProjectWithCreated")
	public void createDuplicateProjectTest() throws IOException {
		
		String Baseurl = fLib.getDataFromPropertiesFile("BASEUrl");

		
		given().contentType(ContentType.JSON).body(pobj).when().post(Baseurl + IEndPoint.AddProj).then().assertThat()
				.statusCode(409).log().all();

	}

}
