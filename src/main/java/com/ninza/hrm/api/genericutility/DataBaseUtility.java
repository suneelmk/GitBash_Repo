package com.ninza.hrm.api.genericutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Driver;

public class DataBaseUtility {
	FileUtility fLib=new FileUtility();
	
	Connection conn;
	public void getDbconnection(String url, String username,String password) throws SQLException
	{
		try {
		Driver driver=new Driver();
		DriverManager.registerDriver(driver);
		 conn = DriverManager.getConnection(url, username, password);
		}catch(Exception e)
		{
			
		}
		
	}
	
	
	public void getDbconnection() throws SQLException
	{
		try {
		Driver driver=new Driver();
		DriverManager.registerDriver(driver);
		 conn = DriverManager.getConnection(fLib.getDataFromPropertiesFile("DBUrl"), fLib.getDataFromPropertiesFile("DB_username"), fLib.getDataFromPropertiesFile("DB_Password"));
		}catch(Exception e)
		{
			
		}
		
	}
	
	
	public void closeConnection() throws SQLException
	{
		conn.close();
	}
	
	
	public boolean excecuteSelectQueryVerifyAndGetData(String query,int index,String projectName) throws SQLException
	{ 
		ResultSet result=null;
		try {
	
		Statement stat = conn.createStatement();
		 result = stat.executeQuery(query);
		}
		catch(Exception e)
		{
			
		}
		boolean flag = false;
		while (result.next()) {
			if (result.getString(index).equals(projectName)) {
				flag = true;
				break;
			}
		}
		if(flag)
		{
			System.out.println(projectName+"data verified in database table");
			return true;
		}else
		{
			System.out.println(projectName+"data not verified in database table");
			return false;
		}
	}
	
	
	public int executeNonSelectQuery(String query)
	{
		int result=0;
		try {
			
			Statement stat = conn.createStatement();
			 result = stat.executeUpdate(query);
			}
		catch(Exception e)
		{
			
		}
		return result;
	}
}


