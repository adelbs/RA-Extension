package com.ca.stextension.engine;

import java.awt.Color;
import java.util.ArrayList;

import com.ca.stextension.gui.MainFrame;
import com.ca.stextension.gui.PnlConfigST;
import com.starteam.ChangeRequest;
import com.starteam.EnumeratedValue;
import com.starteam.Project;
import com.starteam.Server;
import com.starteam.ServerInfo;
import com.starteam.View;
import com.starteam.ViewMemberCollection;

public class Runner extends Thread {

	private static boolean isAlive = true;

	private static boolean isConnected = false;
	private static ServerInfo stServerInfo;
	private static Server stServer;
	private static MainFrame mainF;
	
	private static ArrayList<ChangeRequest> listCRs;
	
	public static int sequencial = 0;
	
	public Runner(MainFrame mainF) {
		Runner.mainF = mainF;
		listCRs = new ArrayList<ChangeRequest>();
	}
	
	@Override
	public void run() {
		while(isAlive) {
			try {
				if (getST() != null && mainF.getPnlConfigST().getBtnEditar().isEnabled()) {
					
					ChangeRequest cr;
					PnlConfigST pnlConfig = mainF.getPnlConfigST();
					View view = pnlConfig.getCmbView().getItemAt(pnlConfig.getCmbView().getSelectedIndex());
					
					view.getRootFolder().refreshItems(getST().getTypes().CHANGE_REQUEST, null, 1);
					ViewMemberCollection crList = view.getRootFolder().getItems(getST().getTypes().CHANGE_REQUEST);
					
					EnumeratedValue statusTrigger = pnlConfig.getCmbStatusCR().getItemAt(
							pnlConfig.getCmbStatusCR().getSelectedIndex());
					
					boolean triggered = false;
					
					for (Object obj : crList.toArray()) {
						cr = (ChangeRequest) obj;
						
						if (cr.getStatus().getCode() == statusTrigger.getCode() && !listCRs.contains(cr)) {
							listCRs.add(cr);
							triggered = true;
						}
					}
					
					if (triggered) {
						mainF.log("Iniciando fluxo RA...");
						Runtime.getRuntime().exec("c:\\projects\\cmd.cmd "+ sequencial);
						sequencial++;
					}
				}
				
				Thread.sleep(500);
			}
			catch(Exception x) {
				mainF.log(x.getMessage());
				x.printStackTrace();
			}
		}
	}
	
	public static Server getST() {

		if (stServerInfo != null) {
			try {
				mainF.getPnlConn().getBtnConectarST().setEnabled(false);
				mainF.getPnlConn().getTxtServidorST().setEnabled(false);
				mainF.getPnlConn().getTxtPortaST().setEnabled(false);
				mainF.getPnlConn().getTxtUsuarioST().setEnabled(false);
				mainF.getPnlConn().getTxtPasswordST().setEnabled(false);
				
				if (stServer == null && stServerInfo != null)
					stServer = new Server(stServerInfo);
				
				if (!stServer.isConnected())
					stServer.connect();
				
				if (!stServer.isLoggedOn()) {
					stServer.logOn(stServerInfo.getUserName(), stServerInfo.getPassword());
					mainF.log("Conectado no Starteam!");
				}

				validatePnlST(stServer);
				mainF.getTabbedPane().setEnabledAt(1, true);
				
				mainF.getPnlConn().getBtnDesconectarST().setEnabled(true);
				mainF.getPnlConn().getLblStatusST().setForeground(Color.BLUE);
				mainF.getPnlConn().getLblStatusST().setText("Conectado");
				isConnected = true;
			}
			catch (Exception x) {
				x.printStackTrace();
				mainF.log(x.getMessage());
				
				stServerInfo = null;
				stServer = null;
			}
		}
		else {
			//Se der algum erro de conexao
			mainF.getTabbedPane().setSelectedIndex(0);
			mainF.getTabbedPane().setEnabledAt(1, false);

			mainF.getPnlConn().getTxtServidorST().setEnabled(true);
			mainF.getPnlConn().getTxtPortaST().setEnabled(true);
			mainF.getPnlConn().getTxtUsuarioST().setEnabled(true);
			mainF.getPnlConn().getTxtPasswordST().setEnabled(true);
			mainF.getPnlConn().getBtnConectarST().setEnabled(true);
			mainF.getPnlConn().getLblStatusST().setForeground(Color.RED);
			mainF.getPnlConn().getLblStatusST().setText("Desconectado");
			isConnected = false;
			
			stServer = null;
		}
		
		return stServer;
	}

	private static void validatePnlST(Server stServer) {
		
		//Verificando combo de projetos
		if (stServer != null) {
			boolean encontrouProjeto = false;
			for (Project project : stServer.getProjects()) {
				encontrouProjeto = false;
				for (int i = 0; i < mainF.getPnlConfigST().getCmbProjeto().getItemCount(); i++) {
					encontrouProjeto = (!encontrouProjeto ?
							mainF.getPnlConfigST().getCmbProjeto().getItemAt(i).equals(project) : encontrouProjeto);
					if (encontrouProjeto) break;
				}
				if (!encontrouProjeto)
					mainF.getPnlConfigST().getCmbProjeto().addItem(project);
				
			}
		}		
		
	}
	
	public static ServerInfo getStServerInfo() {
		return stServerInfo;
	}

	public static void setStServerInfo(ServerInfo stServerInfo) {
		Runner.stServerInfo = stServerInfo;
	}

	public static ArrayList<ChangeRequest> getListCRs() {
		return listCRs;
	}

	public static boolean isConnected() {
		return isConnected;
	}
}
