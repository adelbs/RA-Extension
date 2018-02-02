package com.ca.stextension;

import com.ca.stextension.engine.Runner;
import com.ca.stextension.engine.STBridgeServer;
import com.ca.stextension.gui.MainFrame;
import com.ca.stextension.gui.SysTray;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1 || isNaN(args[0])) {
			printUsage();
			System.exit(0);
		}

		SysTray sysTray = new SysTray();
		MainFrame mainF = new MainFrame(sysTray);
		
		Runner.sequencial = Integer.parseInt(args[1]);
		
		new Runner(mainF).start();
		new STBridgeServer(Integer.parseInt(args[0]), mainF).start();
	}
	
	private static void printUsage() {
		System.out.println("Invalid operation.");
		System.out.println();
		System.out.println("Usage: [RAExtension][PORT]");
	}

	private static boolean isNaN(String val) {
		boolean result = false;
		
		try {
			Integer.parseInt(val);
		}
		catch (Exception x) {
			result = true;
		}
		
		return result;
	}
}
