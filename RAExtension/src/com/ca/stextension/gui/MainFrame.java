package com.ca.stextension.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel lblBG = new JLabel("");
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private SysTray sysTray;
	
	//Abas
	private PnlConnection pnlConn = new PnlConnection();
	private PnlConfigST pnlConfigST = new PnlConfigST();
	
	public MainFrame(SysTray sysTray) {
		this.sysTray = sysTray;
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				toolTip("RA Extension", "O RA Extension continua em execução...");
			}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
		});
		
		this.sysTray.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setVisible(true);
				}
			}
		});

		
		//*** Configurações da janela principal ***
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/resource/package.png")));
		setTitle("RA Extension v.0.1");
		setResizable(false);
		getContentPane().setLayout(null);
		
		//Tamanho e posição
		setBounds(0, 0, 634, 596);
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		setLocation(x, y);
		
		//Imagem de fundo do titulo
		lblBG.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblBG.setIcon(new ImageIcon(MainFrame.class.getResource("/resource/background.png")));
		lblBG.setBounds(-2, -2, 636, 143);
		getContentPane().add(lblBG);
		
		//*** Adicionando as Abas ***
		tabbedPane.setBounds(-2, 140, 636, 435);
		getContentPane().add(tabbedPane);
		tabbedPane.addTab("Conexão", null, pnlConn, null);
		tabbedPane.addTab("Configurações StarTeam", null, pnlConfigST, null);

		//Exibindo a tela
		setVisible(true);
	}
	
	public void log(String message) {
		sysTray.displayMessage("RA Extension", message, TrayIcon.MessageType.INFO);
		pnlConn.log(message);
	}
	
	private void toolTip(String title, String text) {
		sysTray.displayMessage(title, text, TrayIcon.MessageType.INFO);
	}
	
	public JLabel getLblBG() {
		return lblBG;
	}

	public PnlConnection getPnlConn() {
		return pnlConn;
	}

	public PnlConfigST getPnlConfigST() {
		return pnlConfigST;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

}
