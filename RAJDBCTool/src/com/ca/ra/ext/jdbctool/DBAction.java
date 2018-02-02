package com.ca.ra.ext.jdbctool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nolio.platform.shared.api.ActionDescriptor;
import com.nolio.platform.shared.api.ActionResult;
import com.nolio.platform.shared.api.NolioAction;
import com.nolio.platform.shared.api.ParameterDescriptor;
import com.nolio.platform.shared.api.Password;

@ActionDescriptor(name = "Execute JDBC Script Action", description = "This action executes a script at any JDBC connection.", category = "JDBC")
public class DBAction implements NolioAction {

	private static final long serialVersionUID = 1L;

	@ParameterDescriptor(name = "Database", description = "The database to connect", out = false, in = true, nullable = false,
	// defaultValueAsString = "INFORMIX",
	order = 1)
	private DriverJDBC jdbcDriver = DriverJDBC.INFORMIX;

	@ParameterDescriptor(name = "Hostname", description = "The database hostname to connect", out = false, in = true, nullable = false,
	// defaultValueAsString = "localhost",
	order = 2)
	private String hostName = "localhost";

	@ParameterDescriptor(name = "ServerName", description = "The server name to connect", out = false, in = true, nullable = true,
	// defaultValueAsString = "localhost",
	order = 2)
	private String serverName = "localhost";

	@ParameterDescriptor(name = "Port", description = "The port to connect", out = false, in = true, nullable = false,
	// defaultValueAsString = "3306",
	order = 3)
	private Integer port = 3306;

	@ParameterDescriptor(name = "Schema", description = "The schema to connect", out = false, in = true, nullable = true,
	// defaultValueAsString = "nolio",
	order = 4)
	private String schema = "nolio_db";

	@ParameterDescriptor(name = "User", description = "The user to connect", out = false, in = true, nullable = false,
	// defaultValueAsString = "",
	order = 5)
	private String user = "informix";

	@ParameterDescriptor(name = "Password", description = "The password to connect", out = false, in = true, nullable = false,
	// defaultValueAsString = "",
	order = 6)
	private Password pwd = new Password("CAdemo123");

	@ParameterDescriptor(name = "Transaction", description = "Turn the Transactions On or Off", out = false, in = true, nullable = false,
	// defaultValueAsString = "",
	order = 7)
	private Boolean isTransaction = new Boolean(true);

	@ParameterDescriptor(name = "File", description = "The file (.sql) with the statements to execute", out = false, in = true, nullable = false, 
	defaultValueAsString = "", 
	order = 8)
	private String[] fileName;

	//@ParameterDescriptor(name = "Delimiter", description = "The delimiter character", out = false, in = true, nullable = false,
	// defaultValueAsString = ";", order = 8)
	private String delimiter = ";";

	public static void main(String[] args) {
		DBAction action = new DBAction();

		action.hostName = "130.200.216.108";
		action.serverName = "ol_srv0095";
		action.port = 9090;
		action.schema = "teste";
		action.user = "informix";
		action.pwd = new Password("CAdemo123");
		//action.isTransaction = false;
		action.fileName = new String[]{"C:\\Users\\jacfe02\\Desktop\\teste_andrea.sql"};
		//action.delimiter = ";";

		action.executeAction();

	}
	
	@Override
	public ActionResult executeAction() {

		ActionResult result;

		Connection conn = null;
		Statement stmt = null;
		String logResult = "";

		ArrayList<Connection> conList = new ArrayList<Connection>();
		ArrayList<Statement> stmtList = new ArrayList<Statement>();

		try {

			for (String file : fileName) {
				
				/*
				 * Dentro da classe dos drivers existe um bloco estátco para registrar o Driver. 
				 * Como o Classloader da Action é destruido a cada execução da action, o bloco estático não é mais executado, e o Driver
				 * some de dentro do DriverManager. Por isso estamos chamando o método registerDriver para forçar que ele seja registrado.
				 */
				DriverManager.registerDriver((Driver) Class.forName(jdbcDriver.getDriver()).newInstance());
				
				logResult += "Iniciando \r \n";
				
				logResult += "Opening the file : " + file + "\n \r";
				ArrayList<StringBuffer> buffers = (ArrayList<StringBuffer>) readDBScripts(file);
								
				logResult += "Driver carregado \n \r";
				
				conn = DriverManager.getConnection(jdbcDriver.getConnectionString(hostName, serverName, port, schema), user, pwd.getPassword());
				
				logResult += "Conectado \n \r";

				if (isTransaction)
					conn.setAutoCommit(false);
				conList.add(conn);

				logResult += "Connect to host " + hostName + " port " + port + " database " + schema + " servername "+ serverName + "\n \r";
				stmt = conn.createStatement();
				stmtList.add(stmt);

				for (StringBuffer bf : buffers) {
					if (!bf.toString().trim().equals("")) {
						if (!bf.toString().trim().startsWith("--") && !bf.toString().trim().startsWith("//")
								&& !bf.toString().trim().startsWith("/*") && !bf.toString().trim().startsWith("#")
								&& !bf.toString().trim().startsWith("#")) {
							logResult += ("Executing line : " + bf.toString() + "\n");
							stmt.execute(bf.toString());
						}
					}
				}
			}

			if (isTransaction)
				commitAllScripts(conList);

			result = new ActionResult(true, logResult + "All statements were executed successfuly!");
		} catch (Exception x) {
			x.printStackTrace();
			result = new ActionResult(false,
					logResult + "Error executing the SQL statement.\n Doing RollBack... \n \r" + x.getMessage(), x);

			try {
				roolBackAllScripts(conList);
				closeAllConnections(conList, stmtList);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				closeAllConnections(conList, stmtList);
			} catch (Exception x) {
				x.printStackTrace();
			}
		}

		return result;
	}

	public List<StringBuffer> readDBScripts(String aSQLScriptFilePath) throws Exception {
		
		ArrayList<StringBuffer> buffers = new ArrayList<StringBuffer>();

		boolean isFileEmpty = true;
		
		if (!new File(aSQLScriptFilePath).exists())
			throw new Exception("File not found!");
		
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
			String str;
			while ((str = in.readLine()) != null) {
				isFileEmpty = false;
				
				if (!str.equals("/")) {
					if (str.toUpperCase().trim().startsWith("DATABASE")) {
						schema = str.split("@")[0].split(" ")[1].trim();
						serverName = str.split("@")[1].replace(delimiter, "").trim();
					} else {
						sb.append(str + "\n ");
					}
	
					if (str.toUpperCase().contains("CREATE") && (str.toUpperCase().contains("PROCEDURE") || str.toUpperCase().contains("FUNCTION"))) {
						sb = readProcedure(in, str);
						buffers.add(sb);
						sb = new StringBuffer();
					}

					
					
					if (str.toString().trim().startsWith("--") || str.toString().trim().startsWith("//")
							|| str.toString().trim().startsWith("/*") || str.toString().trim().startsWith("#")
							|| str.toString().trim().startsWith("#") || str.trim().endsWith(delimiter)) {

						buffers.add(sb);
						sb = new StringBuffer();
					}
				}
			}
			in.close();
			
			
			if (!sb.toString().trim().equals("") || !isFileEmpty && buffers.size() == 0)
				throw new Exception("Invalid syntax or missing ';'");
		} 
		catch (Exception e) {
			System.err.println("Failed to Execute" + aSQLScriptFilePath + ". The error is: " + e.getMessage());
			throw e;
		}
		return buffers;
	}

	public StringBuffer readProcedure(BufferedReader in, String procStart) throws IOException {
		StringBuffer sb = new StringBuffer();
		try {
			String str;
			sb.append(procStart + "\n");
			while ((str = in.readLine()) != null) {
				sb.append(str + "\n");
				if (str.toUpperCase().trim().contains("END PROCEDURE") || str.toUpperCase().trim().contains("END FUNCTION")) {
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("Failed to create procedure");
		}
		return sb;
	}

	private void closeAllConnections(ArrayList<Connection> conList, ArrayList<Statement> stmtList) throws Exception {

		for (Statement stmt : stmtList) {
			if (stmt != null)
				stmt.close();
		}
		for (Connection con : conList) {
			if (con != null)
				con.close();
		}
	}

	private void commitAllScripts(ArrayList<Connection> conList) throws Exception {

		for (Connection con : conList) {
			con.commit();
		}
	}

	private void roolBackAllScripts(ArrayList<Connection> conList) throws Exception {

		for (Connection con : conList) {
			con.rollback();
		}
	}

}
