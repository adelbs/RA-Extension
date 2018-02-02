package com.ca.stextension.gui;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SysTray extends TrayIcon {

	public SysTray() {
		super(Toolkit.getDefaultToolkit().createImage(SysTray.class.getResource("/resource/packagesmall.png")), 
				"RA Extension", getMenu());
		
		try {
			SystemTray.getSystemTray().add(this);
		} 
		catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private static PopupMenu getMenu() {
		PopupMenu menu = new PopupMenu();
		MenuItem mConectar = new MenuItem("Conectar");
		mConectar.setEnabled(false);
		
		MenuItem mDesconectar = new MenuItem("Desconectar");
		
		MenuItem mFechar = new MenuItem("Fechar");
		mFechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menu.add(mConectar);
		menu.add(mDesconectar);
		menu.addSeparator();
		menu.add(mFechar);
		
		return menu;
	}
}
