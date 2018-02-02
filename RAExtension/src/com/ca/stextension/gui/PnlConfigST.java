package com.ca.stextension.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.ca.stextension.engine.Runner;
import com.starteam.ChangeRequest;
import com.starteam.EnumeratedValue;
import com.starteam.Project;
import com.starteam.User;
import com.starteam.View;
import com.starteam.ViewMemberCollection;

public class PnlConfigST extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel lblProjeto = new JLabel("Projeto:");
	private JLabel lblView = new JLabel("View:");
	private JLabel lblStatusCR = new JLabel("Gerar vers\u00E3o para as CRs no status:");
	
	private JLabel lblStatusSucesso = new JLabel("Ap\u00F3s Build Com sucesso, alterar o status para:");
	private JLabel lblStatusFalha = new JLabel("Se o Build falhar, alterar o status para:");
	private JComboBox<Project> cmbProjeto = new JComboBox<Project>();
	private JComboBox<View> cmbView = new JComboBox<View>();
	private JComboBox<EnumeratedValue> cmbStatusCR = new JComboBox<EnumeratedValue>();
	private JComboBox<EnumeratedValue> cmbStatusCRSucesso = new JComboBox<EnumeratedValue>();
	private JComboBox<EnumeratedValue> cmbStatusCRFalha = new JComboBox<EnumeratedValue>();
	
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JPanel pnlSucesso = new JPanel();
	private JPanel pnlFalha = new JPanel();
	private JLabel lblUsuarioSucesso = new JLabel("E encaminhar a CR para o usu\u00E1rio:");
	private JLabel lblUsuarioFalha = new JLabel("E encaminhar a CR para o usu\u00E1rio:");
	private JComboBox<User> cmbUsuarioSucesso = new JComboBox<User>();
	private JComboBox<User> cmbUsuarioFalha = new JComboBox<User>();

	private JButton btnSalvar = new JButton("Salvar");
	private JButton btnEditar = new JButton("Editar");
	private JLabel lblAdicionarOComentrio = new JLabel("Adicionar o coment\u00E1rio abaixo:");
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JLabel label = new JLabel("Adicionar o coment\u00E1rio abaixo:");
	private JTextArea txtDescSucesso = new JTextArea();
	private JTextArea txtDescFalha = new JTextArea();
	
	public PnlConfigST() {
		setLayout(null);
		tabbedPane.setBounds(261, 32, 357, 308);
		
		add(tabbedPane);
		
		tabbedPane.addTab("Build com Sucesso", null, pnlSucesso, null);
		pnlSucesso.setLayout(null);
		lblStatusSucesso.setBounds(12, 13, 304, 16);
		pnlSucesso.add(lblStatusSucesso);
		cmbStatusCRSucesso.setBounds(12, 31, 304, 22);
		pnlSucesso.add(cmbStatusCRSucesso);
		lblUsuarioSucesso.setBounds(12, 66, 304, 16);
		
		pnlSucesso.add(lblUsuarioSucesso);
		cmbUsuarioSucesso.setBounds(12, 85, 304, 22);
		
		pnlSucesso.add(cmbUsuarioSucesso);
		lblAdicionarOComentrio.setBounds(12, 120, 304, 16);
		
		pnlSucesso.add(lblAdicionarOComentrio);
		scrollPane.setBounds(12, 138, 304, 127);
		
		pnlSucesso.add(scrollPane);
		
		scrollPane.setViewportView(txtDescSucesso);
		tabbedPane.addTab("Build com Falha", null, pnlFalha, null);
		pnlFalha.setLayout(null);
		lblStatusFalha.setBounds(12, 13, 280, 16);
		pnlFalha.add(lblStatusFalha);
		cmbStatusCRFalha.setBounds(12, 31, 304, 22);
		pnlFalha.add(cmbStatusCRFalha);
		lblUsuarioFalha.setBounds(12, 66, 304, 16);
		
		pnlFalha.add(lblUsuarioFalha);
		cmbUsuarioFalha.setBounds(12, 85, 304, 22);
		
		pnlFalha.add(cmbUsuarioFalha);
		scrollPane_1.setBounds(12, 138, 304, 127);
		
		pnlFalha.add(scrollPane_1);
		
		scrollPane_1.setViewportView(txtDescFalha);
		label.setBounds(12, 120, 304, 16);
		
		pnlFalha.add(label);
		lblProjeto.setBounds(12, 32, 63, 16);
		add(lblProjeto);
		cmbProjeto.setBounds(12, 50, 221, 22);
		add(cmbProjeto);
		lblView.setBounds(12, 85, 56, 16);
		add(lblView);
		cmbView.setBounds(12, 103, 221, 22);
		add(cmbView);
		btnSalvar.setBounds(521, 353, 97, 25);
		add(btnSalvar);
		btnEditar.setBounds(412, 353, 97, 25);
		add(btnEditar);
		btnEditar.setEnabled(false);
		lblStatusCR.setBounds(12, 138, 221, 16);
		add(lblStatusCR);
		cmbStatusCR.setBounds(12, 156, 221, 22);
		add(cmbStatusCR);
		
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbProjeto.setEnabled(true);
				cmbView.setEnabled(true);
				cmbStatusCR.setEnabled(true);
				cmbStatusCRSucesso.setEnabled(true);
				cmbStatusCRFalha.setEnabled(true);
				cmbUsuarioSucesso.setEnabled(true);
				cmbUsuarioFalha.setEnabled(true);
				txtDescSucesso.setEnabled(true);
				txtDescFalha.setEnabled(true);
				btnSalvar.setEnabled(true);
				btnEditar.setEnabled(false);
			}
		});
		
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbProjeto.setEnabled(false);
				cmbView.setEnabled(false);
				cmbStatusCR.setEnabled(false);
				cmbStatusCRSucesso.setEnabled(false);
				cmbStatusCRFalha.setEnabled(false);
				cmbUsuarioSucesso.setEnabled(false);
				cmbUsuarioFalha.setEnabled(false);
				txtDescSucesso.setEnabled(false);
				txtDescFalha.setEnabled(false);
				btnSalvar.setEnabled(false);
				btnEditar.setEnabled(true);
			}
		});
		
		cmbView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbStatusCR.removeAllItems();
				cmbStatusCRSucesso.removeAllItems();
				cmbStatusCRFalha.removeAllItems();
				
				View view = cmbView.getItemAt(cmbView.getSelectedIndex());
				if (view != null) {
					view.getRootFolder().refreshItems(Runner.getST().getTypes().CHANGE_REQUEST, null, 1);
					ViewMemberCollection files = view.getRootFolder().getItems(Runner.getST().getTypes().CHANGE_REQUEST);
					for (Object obj : files.toArray()) {
						for (EnumeratedValue status : ((ChangeRequest) obj).getProperties().STATUS.getAllValues()) {
							cmbStatusCR.addItem(status);
							cmbStatusCRSucesso.addItem(status);
							cmbStatusCRFalha.addItem(status);
						}
						break;
					}
				}
			}
		});
		
		cmbProjeto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbView.removeAllItems();
				cmbUsuarioSucesso.removeAllItems();
				cmbUsuarioFalha.removeAllItems();
				
				for (View view : cmbProjeto.getItemAt(cmbProjeto.getSelectedIndex()).getViews()) {
					cmbView.addItem(view);
				}
				
				for (User user : Runner.getST().getUsers()) {
					cmbUsuarioSucesso.addItem(user);
					cmbUsuarioFalha.addItem(user);
				}
			}
		});
	}
	
	public JLabel getLblProjeto() {
		return lblProjeto;
	}

	public JLabel getLblStatusCR() {
		return lblStatusCR;
	}

	public JLabel getLblStatusSucesso() {
		return lblStatusSucesso;
	}

	public JLabel getLblStatusFalha() {
		return lblStatusFalha;
	}

	public JComboBox<Project> getCmbProjeto() {
		return cmbProjeto;
	}

	public JComboBox<EnumeratedValue> getCmbStatusCR() {
		return cmbStatusCR;
	}

	public JComboBox<EnumeratedValue> getCmbStatusCRSucesso() {
		return cmbStatusCRSucesso;
	}

	public JComboBox<EnumeratedValue> getCmbStatusCRFalha() {
		return cmbStatusCRFalha;
	}
	
	public JButton getBtnSalvar() {
		return btnSalvar;
	}
	
	public JButton getBtnEditar() {
		return btnEditar;
	}

	public JLabel getLblView() {
		return lblView;
	}

	public JComboBox<View> getCmbView() {
		return cmbView;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public JPanel getPnlSucesso() {
		return pnlSucesso;
	}

	public JPanel getPnlFalha() {
		return pnlFalha;
	}

	public JLabel getLblUsuarioSucesso() {
		return lblUsuarioSucesso;
	}

	public JLabel getLblUsuarioFalha() {
		return lblUsuarioFalha;
	}

	public JComboBox<User> getCmbUsuarioSucesso() {
		return cmbUsuarioSucesso;
	}

	public JComboBox<User> getCmbUsuarioFalha() {
		return cmbUsuarioFalha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JLabel getLblAdicionarOComentrio() {
		return lblAdicionarOComentrio;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JScrollPane getScrollPane_1() {
		return scrollPane_1;
	}

	public JLabel getLabel() {
		return label;
	}

	public JTextArea getTxtDescSucesso() {
		return txtDescSucesso;
	}

	public JTextArea getTxtDescFalha() {
		return txtDescFalha;
	}
}
