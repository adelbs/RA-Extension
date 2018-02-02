package com.ca.ra.ext.jdbctool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.nolio.platform.shared.api.Password;

public class Testes {

	public static void main(String[] args) throws Exception {
		
		ResultSetHandler<Object[]> rsh = new ResultSetHandler<Object[]>() {
		    public Object[] handle(ResultSet rs) throws SQLException {
		        if (!rs.next()) {
		            return null;
		        }
		    
		        ResultSetMetaData meta = rs.getMetaData();
		        int cols = meta.getColumnCount();
		        Object[] result = new Object[cols];

		        for (int i = 0; i < cols; i++) {
		            result[i] = rs.getObject(i + 1);
		        }

		        return result;
		    }
		};
		
		
		String hostName = "130.200.216.108";
		String serverName = "ol_srv0095";
		Integer port = 9090;
		String schema = "teste";
		String user = "informix";
		Password pwd = new Password("CAdemo123");
		
		DriverJDBC jdbcDriver = DriverJDBC.INFORMIX;
		DriverManager.registerDriver((Driver) Class.forName(jdbcDriver.getDriver()).newInstance());
		
		Connection conn = DriverManager.getConnection(jdbcDriver.getConnectionString(hostName, serverName, port, schema), user, pwd.getPassword());
		
		QueryRunner run = new QueryRunner();
		
		Object[] result = run.query(conn, readDBScripts("C:\\Users\\jacfe02\\Desktop\\teste.sql").toString(), rsh);
		//Object[] result = run.query(conn, "select * from teste;", rsh);
		
	}
	
	public static StringBuffer readDBScripts(String aSQLScriptFilePath) throws IOException {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
			String str;
			while ((str = in.readLine()) != null) {

				sb.append(str + "\n ");
				
			}
			
			in.close();
		} catch (Exception e) {
			System.err.println("Failed to Execute" + aSQLScriptFilePath + ". The error is" + e.getMessage());
		}
		return sb;
	}

}
