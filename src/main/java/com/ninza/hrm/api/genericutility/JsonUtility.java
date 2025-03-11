package com.ninza.hrm.api.genericutility;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import io.restassured.response.Response;

public class JsonUtility {
	FileUtility fLib=new FileUtility();

	public String getAccessToken() throws IOException
	{
		 Response resp = given()
				  .formParam("client_id", fLib.getDataFromPropertiesFile("ClientID"))
				.formParam("client_secret", fLib.getDataFromPropertiesFile("ClientSecret"))
				.formParam("grant_type", "client_credentials")
				.when()
				.post("http://49.249.28.218:8180/auth/realms/ninza/protocol/openid-connect/token");
		resp.then().log().all();
  
		// capture token from response
		
		String token = resp.jsonPath().get("access_token");
      return token;
	}
}
