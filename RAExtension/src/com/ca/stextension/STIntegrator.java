package com.ca.stextension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.ca.stextension.engine.STBridgeServer;

public class STIntegrator {

	public static void main(String[] args) {
		
		if (args.length < 2 || "-c-s-f".indexOf(args[1]) < 0)
			printUsage();
		else if (args[1].equals("-c"))
			System.out.println(checkout(args));
		else if (args[1].equals("-s") || args[1].equals("-f"))
			System.out.println(closeCR(args[2].equals("s"), args));
		else
			printUsage();
		
		System.exit(0);
	}
	
	private static void printUsage() {
		System.out.println("Invalid operation.");
		System.out.println();
		System.out.println("Usage: ST [PORT][PARAMS]");
		System.out.println("Possible params:");
		System.out.println("-c [PATH] Check-out the source code and copy to the specified Path");
		System.out.println("          Caution! Before copying the checked-out files, the specified Path will be cleaned.");
		System.out.println("-s        Update the CRs with success data");
		System.out.println("-f        Update the CRs with failure data");
	}
	
	private static String checkout(String[] args) {
		return (sendMessage(Integer.parseInt(args[0]), "checkout|"+ args[2]));
	}
	
	private static String closeCR(boolean success, String[] args) {
		return (sendMessage(Integer.parseInt(args[0]), "closeCR|"+ String.valueOf(success) +"|"+ args[2]));
	}
	
	private static String sendMessage(int port, String message) {
		String answer = "";
		
		try {
			Socket socket = new Socket(STBridgeServer.HOST, port);
			
	        //Parametros de entrada
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	        out.println(message);
	        
	        //Lendo o retorno
	        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        answer = input.readLine();
	        
	        socket.close();
		}
		catch (Exception x) {
			x.printStackTrace();
			answer = x.getMessage();
		}
		
		return answer;
	}
}
