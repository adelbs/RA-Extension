package com.ca.stextension.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.ca.stextension.gui.MainFrame;
import com.ca.stextension.gui.PnlConfigST;
import com.starteam.ChangeRequest;
import com.starteam.ChangeRequest.Status;
import com.starteam.CheckoutManager;
import com.starteam.EnumeratedValue;
import com.starteam.File;
import com.starteam.Folder;
import com.starteam.User;
import com.starteam.View;
import com.starteam.ViewMemberCollection;

public class STBridgeServer extends Thread {

	private static boolean isAlive = true;
	
	public static final String HOST = "localhost";
	private static int port;
	private static MainFrame mainF;
	
	public STBridgeServer(int port, MainFrame mainF) {
		STBridgeServer.port = port;
		STBridgeServer.mainF = mainF;
	}
	
	@Override
	public void run() {

		InetSocketAddress address = new InetSocketAddress(HOST, port);
		ServerSocket listener = null;
		Socket socket;
		BufferedReader input;
		PrintWriter out = null;
		String cmdLine;
		String[] cmd;
		
		try {
			listener = new ServerSocket();
			listener.bind(address, 100);

			while (isAlive && listener != null) {
				try {
					socket = listener.accept();
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(socket.getOutputStream(), true);
					cmdLine = input.readLine();
					cmd = cmdLine.split("\\|");
					
					if (Runner.isConnected()) {
						if (cmd[0].equals("checkout"))
							out.println(checkout(cmd[1]));
						else if (cmd[0].equals("closeCR"))
							out.println(updateCR(cmd[1].equalsIgnoreCase("true")));
						else {
							mainF.log("Recebido comando invalido para execução: "+ cmdLine);
							out.println("Invalid Operation.");
						}
					}
					else {
						out.println("Not connected.");
					}
					
					out.flush();
				}
				catch (Exception x) {
					mainF.log("Erro ao processar solicitação de execução: "+ x.getMessage());
					x.printStackTrace();
					
					if (out != null)
						out.println(x.getMessage());
				}
			}
		}
		catch (Exception x) {
			mainF.log("Erro tentando iniciar o server: "+ x.getMessage());
			x.printStackTrace();
		}
	}
	
	private String checkout(String path) {
		mainF.log("Fazendo Check-out dos arquivos...");
		
	/*	View view = mainF.getPnlConfigST().getCmbView().getItemAt(mainF.getPnlConfigST().getCmbView().getSelectedIndex());
		CheckoutManager checkManager = view.createCheckoutManager();
		checkManager.getOptions().setForceCheckout(true);
		
		checkout(view.getRootFolder(), checkManager);
		checkManager.commit();*/
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "Success!";
	}
	
	private void checkout(Folder folder, CheckoutManager checkManager) {
		ViewMemberCollection folders = folder.getItems(Runner.getST().getTypes().FOLDER);
		for (Object obj : folders.toArray()) {
			checkManager.checkout((Folder) obj);
			checkout((Folder) obj, checkManager);
		}
		
		for (Folder subFolder : folder.getSubFolders()) {
			checkManager.checkout(subFolder);
			checkout(subFolder, checkManager);
		}

		ViewMemberCollection files = folder.getItems(Runner.getST().getTypes().FILE);
		for (Object obj : files.toArray()) 
			checkManager.checkout((File) obj);
	}
	
	private String updateCR(boolean success) {

		PnlConfigST pnlConfig = mainF.getPnlConfigST();
		View view = pnlConfig.getCmbView().getItemAt(pnlConfig.getCmbView().getSelectedIndex());
		view.getRootFolder().refreshItems(Runner.getST().getTypes().CHANGE_REQUEST, null, 1);
		
		EnumeratedValue statusSucesso = pnlConfig.getCmbStatusCRSucesso().getItemAt(
				pnlConfig.getCmbStatusCRSucesso().getSelectedIndex());
		EnumeratedValue statusFalha = pnlConfig.getCmbStatusCRFalha().getItemAt(
				pnlConfig.getCmbStatusCRFalha().getSelectedIndex());
		User userSucesso = pnlConfig.getCmbUsuarioSucesso().getItemAt(
				pnlConfig.getCmbUsuarioSucesso().getSelectedIndex());
		User userFalha = pnlConfig.getCmbUsuarioFalha().getItemAt(
				pnlConfig.getCmbUsuarioFalha().getSelectedIndex());
		
		for (ChangeRequest cr : Runner.getListCRs()) {
			
			mainF.log("Atualizando status das CRs...");
			
			if (success) {
				cr.setStatus((Status) statusSucesso);
				cr.setResponsibility(userSucesso);
				cr.setSynopsis(cr.getSynopsis() +"\n\n------------------\n\n"+ pnlConfig.getTxtDescSucesso().getText());
			}
			else {
				cr.setStatus((Status) statusFalha);
				cr.setResponsibility(userFalha);
				cr.setSynopsis(cr.getSynopsis() +"\n\n------------------\n\n"+ pnlConfig.getTxtDescFalha().getText());
			}
			
			cr.update();
		}
		
		Runner.getListCRs().clear();
		
		return "Success!";
	}
}
