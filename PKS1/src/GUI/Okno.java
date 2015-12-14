package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Okno extends JFrame{
	
	private JTextArea textovaOblast;
	private JScrollPane scroll;
	private JButton btn_analyzuj;
	private JTextField cesta;
	private JButton btn_vyber;
	private JPanel hornyPanel;
	private JComboBox<String> komboBox;
	private String[] filter = new String[] {"ALL","HTTP","HTTPS","TELNET","FTP-data","FTP-control","SSH","TFTP","ICMP","ARP"};
	
	public Okno() throws HeadlessException {
		super("Sietovy analyzator - Jakub Adam");
		textovaOblast = new JTextArea();
		scroll = new JScrollPane(textovaOblast);
		btn_analyzuj = new JButton("Analyzuj!   ");
		btn_vyber = new JButton("Zvoľ súbor");
		cesta = new JTextField();
		komboBox = new JComboBox<String>(filter);
		
		
		textovaOblast.setFont(new Font("Monospaced",Font.PLAIN,12));
		textovaOblast.setEditable(false);
		
		this.setLayout(new BorderLayout());
		this.add(scroll,BorderLayout.CENTER);
		Box pravyBox = Box.createVerticalBox();
		this.add(pravyBox,BorderLayout.EAST);
		pravyBox.add(Box.createVerticalStrut(10));
		pravyBox.add(btn_analyzuj);
		pravyBox.add(Box.createVerticalStrut(10));
		pravyBox.add(komboBox);
		komboBox.setAlignmentX(LEFT_ALIGNMENT);
		pravyBox.add(Box.createVerticalStrut(10));
		komboBox.setMaximumSize(btn_analyzuj.getMaximumSize());
		
		hornyPanel = new JPanel();
		hornyPanel.setLayout(new BorderLayout());
		hornyPanel.add(cesta, BorderLayout.CENTER);
		hornyPanel.add(btn_vyber, BorderLayout.EAST);
		
		this.add(hornyPanel, BorderLayout.NORTH);
		
		this.setSize(600, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public JButton getBtn_analyzuj() {
		return btn_analyzuj;
	}

	public void setBtn_analyzuj(JButton btn_analyzuj) {
		this.btn_analyzuj = btn_analyzuj;
	}

	public JTextField getCesta() {
		return cesta;
	}

	public void setCesta(JTextField cesta) {
		this.cesta = cesta;
	}

	public JButton getBtn_vyber() {
		return btn_vyber;
	}

	public void setBtn_vyber(JButton btn_vyber) {
		this.btn_vyber = btn_vyber;
	}

	public static void main(String[] args) {
		//new Okno();

	}

	public JTextArea getTextovaOblast() {
		return textovaOblast;
	}

	public void setTextovaOblast(JTextArea textovaOblast) {
		this.textovaOblast = textovaOblast;
	}

	public JComboBox<String> getKomboBox() {
		return komboBox;
	}

	public void setKomboBox(JComboBox<String> komboBox) {
		this.komboBox = komboBox;
	}
	
	

}
