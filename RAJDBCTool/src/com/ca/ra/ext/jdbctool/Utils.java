package com.ca.ra.ext.jdbctool;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
//import com.informix.jdbc.IfxDriver;

public class Utils {

	public static Connection getConnection(String hostName, String serverName, Integer port, String schema, String user,
			String pwd) throws ClassNotFoundException, SQLException, MalformedURLException, InstantiationException,
					IllegalAccessException {

		DriverJDBC jdbcDriver = DriverJDBC.INFORMIX;

		URL u = new URL("jar:file:/opt/ReleaseAutomationServer/actionslib/ifxjdbc.jar!/");
		// URL u = new
		// URL("jar:file:C:\\Users\\sarro05\\RA\\informix\\ifxjdbc.jar!/");
		String classname = jdbcDriver.getDriver();
		// URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		// Class.forName(classname, true, classL);
		// DriverManager.getConnection("jdbc:postgresql://host/db", "user",
		// "pw");

		// Driver d = (Driver)Class.forName(classname, true,
		// classL).newInstance();
		// DriverManager.registerDriver(new DriverShim(d));

		Connection conn = DriverManager
				.getConnection(jdbcDriver.getConnectionString(hostName, serverName, port, schema), user, pwd);

		// java.sql.DriverManager.registerDriver(new
		// com.informix.jdbc.IfxDriver());
		// Class.forName(jdbcDriver.getDriver());
		// Connection conn =
		// DriverManager.getConnection(jdbcDriver.getConnectionString(hostName,
		// serverName, port, schema),
		// user, pwd);
		return conn;
	}

	public static Connection getConnection2(String hostName, String serverName, Integer port, String schema,
			String user, String pwd) throws Exception {
		DriverJDBC jdbcDriver = DriverJDBC.INFORMIX;

		String dbUrl = jdbcDriver.getConnectionString(hostName, serverName, port, schema);
		String userName = user;
		String password = pwd;
		String driverName = jdbcDriver.getDriver();

		final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		String jar = "/opt/ReleaseAutomationServer/actionlib/ifxjdbc.jar";

		// System.out.println(this.getClass().getClassLoader().getClass() + " is
		// the class loader");
		// System.out.println("Before loading the drivers in driver manager are
		// ");
		// listDrivers();
		Connection connection = null;
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		method.setAccessible(true);
		method.invoke(loader, new File(jar).toURI().toURL());

		Class<?> classToLoad = Class.forName(driverName);
		Driver driver = (Driver) classToLoad.newInstance();
		DriverManager.registerDriver(new DriverShim(driver));
		connection = DriverManager.getConnection(dbUrl, userName, password);
		System.out.println(
				"is Connection null " + (connection == null) + ". After loading the drivers in driver manager are ");
		// Do business specific action
		// listDrivers();
		// DriverManager.deregisterDriver(driver);

		return connection;
	}

	private static void listDrivers() {
		java.util.Enumeration enumeration = java.sql.DriverManager.getDrivers();
		while (enumeration.hasMoreElements()) {
			Driver driverAsObject = (Driver) enumeration.nextElement();
			System.out.println("Driver: " + driverAsObject + ":" + driverAsObject.getMajorVersion() + ":"
					+ driverAsObject.getMinorVersion());
		}
	}

}
