package com.ca.ra.ext.jdbctool;

public enum DriverJDBC {

	INFORMIX("com.informix.jdbc.IfxDriver", "jdbc:informix-sqli://<hostname>:<port>/<schema>:INFORMIXSERVER=<servername>");
	
	private String driver;
	private String url;
	
	DriverJDBC(String driver, String url) {
		this.driver = driver;
		this.url = url;
	}
	
	public String getDriver() {
		return driver;
	}
	
	public String getConnectionString(String hostName, String servername, Integer port, String schema) {
		return url.replaceAll("<hostname>", hostName).
				replaceAll("<port>", String.valueOf(port)).
				replaceAll("<servername>", servername).
				replaceAll("<schema>", schema);
	}
}
