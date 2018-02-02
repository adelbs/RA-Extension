package com.ca.stextension.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import com.ca.stextension.engine.Runner;
import com.starteam.ServerInfo;

public class PnlConnection extends JPanel {

	private static final long serialVersionUID = 1L;
	
	//Componentes Starteam
	private JLabel lblServidorhostST = new JLabel("Servidor (Host):");
	private JLabel lblPortaST = new JLabel("Porta:");
	private JLabel lblUsurioST = new JLabel("Usu\u00E1rio:");
	private JLabel lblSenhaST = new JLabel("Senha:");
	private JTextField txtServidorST = new JTextField("localhost");
	private JTextField txtPortaST = new JTextField("49201");
	private JTextField txtUsuarioST = new JTextField();
	private JPasswordField txtPasswordST = new JPasswordField();
	private JButton btnConectarST = new JButton("Conectar");
	private JButton btnDesconectarST = new JButton("Desconectar");
	private JPanel pnlStatusST = new JPanel();
	private JLabel lblStatusST = new JLabel("Desconectado");
	
	private JTextPane txtLog;
	
	public PnlConnection() {
		setLayout(null);
		
		//*** Painel de conexao com o StarTeam
		JPanel pnlST = new JPanel();
		pnlST.setLayout(null);
		pnlST.setBorder(new TitledBorder(null, "StarTeam", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlST.setBounds(12, 13, 298, 256);
		add(pnlST);
		
		//Labels
		lblServidorhostST.setBounds(12, 39, 121, 16);
		pnlST.add(lblServidorhostST);
		
		lblPortaST.setBounds(12, 92, 56, 16);
		pnlST.add(lblPortaST);
		
		lblUsurioST.setBounds(12, 145, 56, 16);
		pnlST.add(lblUsurioST);
		
		lblSenhaST.setBounds(12, 198, 56, 16);
		pnlST.add(lblSenhaST);

		//Campos e botões
		txtServidorST.setColumns(10);
		txtServidorST.setBounds(12, 57, 274, 22);
		pnlST.add(txtServidorST);
		
		txtPortaST.setColumns(10);
		txtPortaST.setBounds(12, 110, 116, 22);
		pnlST.add(txtPortaST);
		
		txtUsuarioST.setColumns(10);
		txtUsuarioST.setBounds(12, 163, 274, 22);
		pnlST.add(txtUsuarioST);
		
		txtPasswordST.setBounds(12, 216, 274, 22);
		pnlST.add(txtPasswordST);
		pnlStatusST.setBounds(322, 13, 287, 67);
		add(pnlStatusST);
		
		//Status
		pnlStatusST.setBorder(new TitledBorder(null, "STATUS:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		lblStatusST.setFont(new Font("Tahoma", Font.PLAIN, 20));
		pnlStatusST.add(lblStatusST);
		btnDesconectarST.setBounds(488, 244, 121, 25);
		add(btnDesconectarST);
		btnDesconectarST.setEnabled(false);
		btnConectarST.setBounds(355, 244, 121, 25);
		add(btnConectarST);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 282, 597, 103);
		add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollLog = new JScrollPane();
		scrollLog.setBounds(12, 23, 573, 70);
		panel.add(scrollLog);
		
		txtLog = new JTextPane();
		txtLog.setEditable(false);
		scrollLog.setViewportView(txtLog);
		btnConectarST.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnConectarST.setEnabled(false);
				ServerInfo stServerInfo = new ServerInfo();
				stServerInfo.setHost(txtServidorST.getText());
				stServerInfo.setPort(Integer.parseInt(txtPortaST.getText()));
				stServerInfo.setUserName(txtUsuarioST.getText());
				stServerInfo.setPassword(String.valueOf(txtPasswordST.getPassword()));
				
				lblStatusST.setText("Conectando...");
				Runner.setStServerInfo(stServerInfo);
			}
		});
		btnDesconectarST.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDesconectarST.setEnabled(false);
				
				lblStatusST.setText("Desconectando...");
				Runner.setStServerInfo(null);
			}
		});
	}

	public void log(String message) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		String newLine = cal.get(GregorianCalendar.HOUR_OF_DAY) +":"+ cal.get(GregorianCalendar.MINUTE) +
				":"+ cal.get(GregorianCalendar.SECOND) +" - "+ message;
		txtLog.setText(txtLog.getText() +"\n"+ newLine);
	}
	
	public JLabel getLblServidorhostST() {
		return lblServidorhostST;
	}

	public JLabel getLblPortaST() {
		return lblPortaST;
	}

	public JLabel getLblUsurioST() {
		return lblUsurioST;
	}

	public JLabel getLblSenhaST() {
		return lblSenhaST;
	}

	public JTextField getTxtServidorST() {
		return txtServidorST;
	}

	public JTextField getTxtPortaST() {
		return txtPortaST;
	}

	public JTextField getTxtUsuarioST() {
		return txtUsuarioST;
	}

	public JPasswordField getTxtPasswordST() {
		return txtPasswordST;
	}

	public JButton getBtnConectarST() {
		return btnConectarST;
	}

	public JButton getBtnDesconectarST() {
		return btnDesconectarST;
	}

	public JPanel getPnlStatusST() {
		return pnlStatusST;
	}

	public JLabel getLblStatusST() {
		return lblStatusST;
	}
}
